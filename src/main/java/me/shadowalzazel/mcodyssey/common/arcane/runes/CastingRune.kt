package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneCaster
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneSource
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneTarget
import me.shadowalzazel.mcodyssey.common.arcane.CastingBuilder
import me.shadowalzazel.mcodyssey.common.arcane.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.*
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

/**
 * Casting runes are the *forms* a spell can take. They read defaults + modifiers from a
 * [CastingBuilder] and manifest the [ArcaneSource] into the world.
 *
 * Completion: a form must report when it is finished so the spell can chain onward. Most
 * forms are synchronous and are marked done automatically. A form that keeps working after
 * manifest() returns (a projectile in flight) sets [defersCompletion] = true and calls the
 * provided `onComplete` itself once it resolves.
 *
 * TO ADD A NEW CASTING RUNE:
 *   1. Add a `class Foo : CastingRune()` with name/displayName.
 *   2. build(): set the DEFAULT builder fields it uses (modifiers add on top).
 *   3. manifest(): do the effect and set context.targetLocation / context.target to where
 *      the form ends, so the next rune continues from there.
 *   4. If it finishes later (async), set `defersCompletion = true` and call onComplete()
 *      when it actually resolves.
 *   5. Register its name in ArcaneRune.fromNameID(). Nothing else needs to change.
 */
sealed class CastingRune : ArcaneRune(), RayTracerAndDetector, AttackHelper, VectorParticles {

    /** True if the form finishes after manifest() returns (e.g. a projectile). */
    open val defersCompletion: Boolean get() = false

    /** Set default builder fields for this rune. Modifiers are folded in afterwards. */
    abstract fun build(builder: CastingBuilder)

    /**
     * Express the source into the world. For deferred forms, keep the [onComplete] handle
     * and call it when the effect actually resolves; synchronous forms may ignore it.
     */
    abstract fun manifest(
        source: ArcaneSource,
        context: CastingContext,
        builder: CastingBuilder,
        onComplete: () -> Unit
    )

    /** Entry point used by the spell engine; honours Delay, then runs the form. */
    fun cast(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
        if (builder.delayInTicks > 0) {
            DelayedCastRunner(this, source, context, builder, onComplete)
                .runTaskLater(Odyssey.instance, builder.delayInTicks)
        } else {
            runManifest(source, context, builder, onComplete)
        }
    }

    /** Runs manifest and auto-completes for synchronous forms. */
    internal fun runManifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
        manifest(source, context, builder, onComplete)
        if (!defersCompletion) onComplete()
    }

    /** Applies this rune's defaults into the provided builder. */
    fun assemble(provided: CastingBuilder) = build(provided)

    // Runs a delayed cast one tick-batch later.
    class DelayedCastRunner(
        private val rune: CastingRune,
        private val source: ArcaneSource,
        private val context: CastingContext,
        private val builder: CastingBuilder,
        private val onComplete: () -> Unit
    ) : BukkitRunnable() {
        override fun run() = rune.runManifest(source, context, builder, onComplete)
    }

    // Small helper: caster-owned living entities to ignore in target scans.
    protected fun CastingContext.filteredLivingIgnores(): List<LivingEntity> =
        ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }

    // -----------------------------------------------------------------------------------
    //  POINT — a single-target tap at the current target/location.
    // -----------------------------------------------------------------------------------
    class Point : CastingRune() {
        override val name = "point"
        override val displayName = "Point"

        override fun build(builder: CastingBuilder) {
            builder.damage = 0.0
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val target = context.target
            val point: Location
            if (target?.entityTarget is LivingEntity) {
                source.invoke(target, context.caster, context.direction, builder.damage)
                point = target.entityTarget.eyeLocation
            } else {
                point = context.targetLocation ?: context.castingLocation
            }
            spawnPointParticles(builder.particle, point, 10, 0.05)
            context.targetLocation = point
        }
    }

    // -----------------------------------------------------------------------------------
    //  BEAM — a straight ray. Wide -> more forgiving/wider, Range -> longer.
    // -----------------------------------------------------------------------------------
    class Beam : CastingRune() {
        override val name = "beam"
        override val displayName = "Beam"

        override fun build(builder: CastingBuilder) {
            builder.damage = 1.0
            builder.range = 16.0
            builder.aimAssist = 0.25
            builder.spread = 0.0
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val caster = context.caster
            val range = builder.range.coerceAtLeast(1.0)
            // A wider beam simply snaps to targets more generously.
            val aim = (builder.aimAssist + builder.spread * 0.1).coerceAtLeast(0.0)

            val castLoc = context.castingLocation
            val beamDir = context.targetLocation?.clone()?.subtract(castLoc)?.toVector() ?: context.direction
            val filter = context.filteredLivingIgnores()

            val end: Location
            val hitEntity = getEntityRayTrace(castLoc, beamDir, filter, range, aim)
            if (hitEntity is LivingEntity) {
                val newTarget = ArcaneTarget(entityTarget = hitEntity)
                context.target = newTarget
                source.invoke(newTarget, caster, beamDir, builder.damage)
                end = hitEntity.eyeLocation
            } else {
                val hitBlock = context.world.rayTraceBlocks(castLoc, context.direction, range, FluidCollisionMode.NEVER)?.hitBlock
                if (hitBlock != null) {
                    val newTarget = ArcaneTarget(blockTarget = hitBlock)
                    context.target = newTarget
                    source.invoke(newTarget, caster, beamDir, builder.damage)
                    end = hitBlock.location.toCenterLocation()
                } else {
                    end = castLoc.clone().add(context.direction.clone().normalize().multiply(range))
                }
            }

            spawnLineParticles(
                particle = builder.particle,
                start = castLoc,
                end = end,
                count = (end.distance(castLoc) * 6).toInt()
            )
            context.targetLocation = end
            context.world.playSound(castLoc, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }
    }

    // -----------------------------------------------------------------------------------
    //  DISPERSE — a cone/cloud. Wide -> wider angle, Range -> further, Invert -> apex flips
    //             (base at the caster, tip out: a converging cone instead of a spreading one).
    // -----------------------------------------------------------------------------------
    class Disperse : CastingRune() {
        override val name = "disperse"
        override val displayName = "Disperse"

        override fun build(builder: CastingBuilder) {
            builder.damage = 2.0
            builder.range = 8.0
            builder.spread = 0.0
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val caster = context.caster
            val reach = builder.range.coerceAtLeast(1.0)
            val halfAngle = Math.toRadians((20.0 + builder.spread * 8.0).coerceIn(5.0, 80.0))

            val baseDir = context.direction.clone().normalize()
            val apex = if (builder.invert)
                context.castingLocation.clone().add(baseDir.clone().multiply(reach))
            else
                context.castingLocation.clone()
            val coneDir = if (builder.invert) baseDir.clone().multiply(-1.0) else baseDir

            val filter = context.filteredLivingIgnores()
            apex.getNearbyLivingEntities(reach).forEach { e ->
                if (e in filter) return@forEach
                val to = e.location.clone().add(0.0, e.height / 2.0, 0.0).subtract(apex).toVector()
                if (to.length() <= reach && to.angle(coneDir) <= halfAngle) {
                    source.invoke(ArcaneTarget(entityTarget = e), caster, baseDir, builder.damage)
                }
            }

            spawnConeParticles(builder.particle, apex, coneDir, reach, halfAngle)
            context.targetLocation = apex.clone().add(coneDir.clone().multiply(reach))
            context.world.playSound(context.castingLocation, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.5F, 1.6F)
        }

        private fun spawnConeParticles(particle: Particle, apex: Location, dir: Vector, reach: Double, halfAngle: Double) {
            val world = apex.world
            val ref = if (Math.abs(dir.y) > 0.9) Vector(1.0, 0.0, 0.0) else Vector(0.0, 1.0, 0.0)
            val right = dir.clone().getCrossProduct(ref).normalize()
            val up = right.clone().getCrossProduct(dir).normalize()
            val steps = 10
            for (s in 1..steps) {
                val dist = reach * s / steps
                val ringR = Math.tan(halfAngle) * dist
                val center = apex.clone().add(dir.clone().multiply(dist))
                val points = 6 + s
                for (p in 0 until points) {
                    val a = 2 * Math.PI * p / points
                    val offset = right.clone().multiply(Math.cos(a) * ringR)
                        .add(up.clone().multiply(Math.sin(a) * ringR))
                    world.spawnParticle(particle, center.clone().add(offset), 1, 0.0, 0.0, 0.0, 0.0)
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------
    //  BALL — a slow, floaty particle orb. No gravity by default; big, showy trail.
    // -----------------------------------------------------------------------------------
    class Ball : CastingRune() {
        override val name = "ball"
        override val displayName = "Ball"
        override val defersCompletion = true

        override fun build(builder: CastingBuilder) {
            builder.damage = 3.0
            builder.range = 24.0
            builder.spread = 0.6      // orb radius
            builder.speed = 0.4
            builder.gravity = false
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            ArcaneProjectile(source, context, builder, context.caster, context.castingLocation, context.direction, onComplete)
                .runTaskTimer(Odyssey.instance, 0, 1)
        }
    }

    // -----------------------------------------------------------------------------------
    //  BOLT — a fast particle dart. Tight trail; the Gravity rune makes it arc.
    // -----------------------------------------------------------------------------------
    class Bolt : CastingRune() {
        override val name = "bolt"
        override val displayName = "Bolt"
        override val defersCompletion = true

        override fun build(builder: CastingBuilder) {
            builder.damage = 4.0
            builder.range = 32.0
            builder.spread = 0.25     // dart radius
            builder.speed = 1.1
            builder.gravity = false   // enabled by the Gravity rune
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            ArcaneProjectile(source, context, builder, context.caster, context.castingLocation, context.direction, onComplete)
                .runTaskTimer(Odyssey.instance, 0, 1)
        }
    }

    // -----------------------------------------------------------------------------------
    //  ZONE — an area burst at the current target location. Wide -> bigger radius.
    // -----------------------------------------------------------------------------------
    class Zone : CastingRune() {
        override val name = "zone"
        override val displayName = "Zone"

        override fun build(builder: CastingBuilder) {
            builder.damage = 0.0
            builder.range = 16.0
            builder.spread = 3.0      // radius
            builder.aimAssist = 0.1
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val caster = context.caster
            val radius = builder.spread.coerceAtLeast(0.5)
            val center = context.targetLocation ?: context.castingLocation
            val filter = context.filteredLivingIgnores()

            center.getNearbyLivingEntities(radius).forEach {
                if (it !in filter) {
                    source.invoke(ArcaneTarget(entityTarget = it), caster, context.direction, builder.damage)
                }
            }

            spawnCircleParticles(
                particle = builder.particle,
                center = center,
                upDirection = Vector(0, 1, 0),
                radius = radius,
                heightOffset = 0.25,
                count = (radius * Math.PI * 7).toInt()
            )
            context.targetLocation = center
            context.world.playSound(center, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }
    }

    // ===================================================================================
    //  ARCANE PROJECTILE — the flashy, purely-particle traveller shared by Ball and Bolt.
    //  A moving point that leaves a particle trail, checks block/entity collisions against
    //  its own radius, and on impact updates the shared context AND calls onResolve so the
    //  next rune in the sequence continues from the point of impact.
    // ===================================================================================
    class ArcaneProjectile(
        private val source: ArcaneSource,
        private val context: CastingContext,
        builder: CastingBuilder,
        private val caster: ArcaneCaster,
        start: Location,
        direction: Vector,
        // Called once when the projectile resolves (hit or expiry). Resumes the spell chain.
        private val onResolve: (() -> Unit)? = null
    ) : BukkitRunnable() {

        private val world = context.world
        private val pos: Location = start.clone()
        private val velocity: Vector = direction.clone().normalize().multiply(builder.speed.coerceAtLeast(0.05))
        private val size: Double = builder.spread.coerceAtLeast(0.2)
        private val useGravity: Boolean = builder.gravity
        private val damage: Double = builder.damage
        private val particle: Particle = builder.particle
        private val maxTicks: Int =
            (builder.range / builder.speed.coerceAtLeast(0.05)).toInt().coerceIn(5, 400)
        private val ignored: List<LivingEntity> = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
        private var ticksLived = 0

        override fun run() {
            if (ticksLived >= maxTicks) { resolve(null); return }

            // Block collision along this tick's travel.
            val step = velocity.length()
            val blockHit = world.rayTraceBlocks(pos, velocity, step, FluidCollisionMode.NEVER)
            if (blockHit?.hitBlock != null) {
                pos.add(velocity.clone().normalize().multiply(blockHit.hitPosition.distance(pos.toVector())))
                resolve(ArcaneTarget(blockTarget = blockHit.hitBlock))
                return
            }

            // Entity collision inside the projectile's body.
            val nearby = pos.getNearbyLivingEntities(size).filter { it !in ignored }
            if (nearby.isNotEmpty()) {
                resolve(ArcaneTarget(entityTarget = nearby.minByOrNull { it.location.distance(pos) }!!))
                return
            }

            if (ticksLived % 2 == 0) {
                world.spawnParticle(particle, pos, 6, size * 0.4, size * 0.4, size * 0.4, 0.0)
            }

            if (useGravity) velocity.y -= 0.035
            pos.add(velocity)
            ticksLived++
        }

        private fun resolve(target: ArcaneTarget?) {
            world.spawnParticle(particle, pos, 24, size * 0.6, size * 0.6, size * 0.6, 0.02)
            if (target != null) {
                source.invoke(target, caster, velocity, damage)
                context.target = target
            }
            // Hand the impact point to the shared context, then resume the chain.
            context.targetLocation = pos.clone()
            onResolve?.invoke()
            cancel()
        }
    }
}