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
import java.util.UUID
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
                context.chainSpawner?.invoke(ArcaneTarget(entityTarget = target.entityTarget))
                context.target = target
            }
            // Hand the impact point to the shared context, then resume the chain.
            context.targetLocation = pos.clone()
            onResolve?.invoke()
            cancel()
        }
    }


    // next to `defersCompletion`, in the base class:
    /** True if this form damages targets over time and its tail should react per-hit. */
    open val chainsOnHit: Boolean get() = false

    /*
     * Ideas:
     *
     * New Casting Runes:
     * `erupt` spews a steady stream upwards. Like the geyser
     * `vortex` spawns a rotating swirling mass like a tornado. Really want that helix rotating particle effect.
     * `familiar` a magic summon. Probably a small ball for now that seeks out targets?
     *
     *
     */

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
                context.chainSpawner?.invoke(ArcaneTarget(entityTarget = hitEntity))
                end = hitEntity.eyeLocation
            } else {
                val hitBlock = context.world.rayTraceBlocks(castLoc, context.direction, range, FluidCollisionMode.NEVER)?.hitBlock
                if (hitBlock != null) {
                    val newTarget = ArcaneTarget(blockTarget = hitBlock)
                    context.target = newTarget
                    source.invoke(newTarget, caster, beamDir, builder.damage)
                    context.chainSpawner?.invoke(ArcaneTarget(entityTarget = hitEntity))
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
                    context.chainSpawner?.invoke(ArcaneTarget(entityTarget = e))
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
                    context.chainSpawner?.invoke(ArcaneTarget(entityTarget = it))
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


    // -----------------------------------------------------------------------------------
    //  RAIN — a burst of small falling drops over the target area. Wide -> radius.
    //         Each drop is an independent falling projectile (does not resume the chain).
    // -----------------------------------------------------------------------------------
    class Rain : CastingRune() {
        override val name = "rain"
        override val displayName = "Rain"

        override fun build(builder: CastingBuilder) {
            builder.damage = 2.0
            builder.range = 12.0   // drop travel / lifetime
            builder.spread = 3.0   // area radius
            builder.speed = 0.8
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val center = context.targetLocation ?: context.castingLocation
            val radius = builder.spread.coerceAtLeast(0.5)
            val drops = 20
            val height = 5.0
            val down = Vector(0.0, -1.0, 0.0)

            repeat(drops) {
                val angle = Math.random() * 2.0 * Math.PI
                val r = sqrt(Math.random()) * radius   // even spread across the disc
                val start = center.clone().add(cos(angle) * r, height, sin(angle) * r)
                // Small, gravity-fed drop on its own context clone so it can't disturb the chain.
                val dropBuilder = builder.copy(gravity = true, spread = 0.35)
                ArcaneProjectile(source, context.clone(), dropBuilder, context.caster, start, down)
                    .runTaskTimer(Odyssey.instance, 0, 1)
            }

            context.targetLocation = center
        }
    }


    // -----------------------------------------------------------------------------------
    //  WAVE — an expanding ring that damages + knocks back what it sweeps over.
    //         Range -> max radius, Wide -> band thickness, Speed -> ring speed.
    // -----------------------------------------------------------------------------------
    class Wave : CastingRune() {
        override val name = "wave"
        override val displayName = "Wave"
        override val defersCompletion = true

        override fun build(builder: CastingBuilder) {
            builder.damage = 3.0
            builder.range = 8.0    // max radius
            builder.spread = 1.0   // band thickness
            builder.speed = 0.5    // blocks per tick
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val center = (context.targetLocation ?: context.castingLocation).clone()
            WaveTask(
                source, context, center,
                maxRadius = builder.range.coerceAtLeast(1.0),
                band = builder.spread.coerceAtLeast(0.5),
                ringSpeed = builder.speed.coerceAtLeast(0.1),
                damage = builder.damage,
                onComplete = onComplete
            ).runTaskTimer(Odyssey.instance, 0, 1)
            context.targetLocation = center
        }

        private class WaveTask(
            private val source: ArcaneSource,
            private val context: CastingContext,
            private val center: Location,
            private val maxRadius: Double,
            private val band: Double,
            private val ringSpeed: Double,
            private val damage: Double,
            private val onComplete: () -> Unit
        ) : BukkitRunnable() {

            private val world = center.world
            private val ignored: List<LivingEntity> = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
            private val alreadyHit = HashSet<UUID>()
            private var radius = 1.0
            private var first = true

            override fun run() {
                if (first) {
                    first = false
                    //world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1.2f, 0.8f)
                    //world.spawnParticle(Particle.EXPLOSION, center, 2)
                }
                drawRing()
                sweepFront()
                radius += ringSpeed
                if (radius > maxRadius) {
                    onComplete()
                    cancel()
                }
            }

            private fun drawRing() {
                val points = maxOf(8, (radius * 6).toInt())
                for (i in 0 until points) {
                    val angle = 2.0 * Math.PI * i / points
                    val at = Location(world, center.x + radius * Math.cos(angle), center.y + 0.25, center.z + radius * Math.sin(angle))
                    world.spawnParticle(source.particle, at, 3, 0.15, 0.1, 0.15, 0.02)
                }
            }

            private fun sweepFront() {
                center.getNearbyLivingEntities(maxRadius + band).forEach { e ->
                    if (e in ignored || e.uniqueId in alreadyHit) return@forEach
                    val dx = e.location.x - center.x
                    val dz = e.location.z - center.z
                    val flat = Math.sqrt(dx * dx + dz * dz)
                    if (flat in (radius - band)..(radius + band)) {
                        alreadyHit += e.uniqueId
                        val out = e.location.toVector().subtract(center.toVector()).setY(0.0)
                        val dir = if (out.lengthSquared() > 1e-4) out.normalize() else e.location.direction.setY(0.0).normalize()
                        source.invoke(ArcaneTarget(entityTarget = e), context.caster, dir, damage)
                        context.chainSpawner?.invoke(ArcaneTarget(entityTarget = e))
                        e.velocity = dir.multiply(0.9).setY(0.55)
                    }
                }
            }
        }
    }


    // -----------------------------------------------------------------------------------
    //  WALL — a standing particle plane that runs from the caster toward the target.
    //         The cast -> target vector is the wall's length axis. Range -> length,
    //         Wide -> height. Applies the source's effect to any entity whose body crosses
    //         it, and re-applies on an interval while they linger inside. Lasts a few
    //         seconds, places nothing.
    // -----------------------------------------------------------------------------------
    class Wall : CastingRune() {
        override val name = "wall"
        override val displayName = "Wall"
        override val chainsOnHit = true

        override fun build(builder: CastingBuilder) {
            builder.damage = 0.0   // bonus on top of the source's own base; source carries the hit
            builder.range = 4.0    // length
            builder.spread = 4.0   // height (tall)
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val start = context.castingLocation.clone().add(0.0, -1.0, 0.0)
            val target = (context.targetLocation ?: context.castingLocation).clone()
            val length = builder.range.coerceIn(1.0, 24.0)   // <- swap for start.distance(target) to span exactly to the target
            val height = builder.spread.coerceIn(1.0, 32.0)

            // The plane runs horizontally from the caster toward the target.
            var along = target.toVector().subtract(start.toVector()).setY(0.0)
            if (along.lengthSquared() < 1e-4) along = context.direction.clone().setY(0.0)
            if (along.lengthSquared() < 1e-4) along = Vector(1.0, 0.0, 0.0)
            along = along.normalize()
            val normal = Vector(-along.z, 0.0, along.x)   // horizontal, perpendicular to the run

            WallTask(source, context, start, along, normal, length, height, builder.damage)
                .runTaskTimer(Odyssey.instance, 0, 1)

            // Wall lingers in the background; the chain continues from the far end immediately.
            context.targetLocation = target
        }

        private class WallTask(
            private val source: ArcaneSource,
            private val context: CastingContext,
            private val base: Location,      // start of the wall (casting location)
            private val along: Vector,       // horizontal length direction (start -> target)
            private val normal: Vector,      // plane normal (horizontal, perpendicular to `along`)
            private val length: Double,
            private val height: Double,
            private val bonus: Double
        ) : BukkitRunnable() {

            private val world = base.world
            private val ignored: List<LivingEntity> = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
            private val lastHitTick = HashMap<UUID, Int>()
            private val thickness = 0.5     // how "solid" the plane is along its normal
            private val applyInterval = 10  // re-apply to a lingering entity every N ticks
            private val lifetimeTicks = 100 // wall duration
            private var tick = 0

            internal val chainedEntities = HashSet<UUID>()

            override fun run() {
                if (tick >= lifetimeTicks) { cancel(); return }
                if (tick % 2 == 0) render()
                sweep()
                tick++
            }

            private fun render() {
                val up = Vector(0.0, 1.0, 0.0)
                val lSteps = maxOf(2, (length * 2).toInt())
                val hSteps = maxOf(2, (height * 2).toInt())
                for (li in 0..lSteps) {
                    val l = length * li / lSteps
                    for (hi in 0..hSteps) {
                        val at = base.clone()
                            .add(along.clone().multiply(l))
                            .add(up.clone().multiply(height * hi / hSteps))
                        world.spawnParticle(source.particle, at, 1, 0.02, 0.02, 0.02, 0.0)
                    }
                }
            }

            private fun sweep() {
                // Search from the middle of the span so the whole length is covered.
                val mid = base.clone().add(along.clone().multiply(length / 2.0))
                val reach = maxOf(length / 2.0, height) + 2.0
                mid.getNearbyLivingEntities(reach).forEach { e ->
                    if (e in ignored) return@forEach
                    // Project the entity's centre into the wall's local frame.
                    val to = e.boundingBox.center.subtract(base.toVector())
                    val halfW = e.width / 2.0
                    val l = to.dot(along)    // offset along the length
                    val n = to.dot(normal)   // distance from the plane
                    val h = to.y             // height above the base
                    val crossing = Math.abs(n) <= thickness + halfW &&
                            l in (-halfW)..(length + halfW) &&
                            h in -0.4..(height + 0.4)
                    if (!crossing) return@forEach

                    val last = lastHitTick[e.uniqueId]
                    if (last == null || tick - last >= applyInterval) {
                        lastHitTick[e.uniqueId] = tick
                        source.invoke(ArcaneTarget(entityTarget = e), context.caster, normal, bonus)
                        // Fork the linked reaction ONCE per entity, on its first hit only.
                        if (chainedEntities.add(e.uniqueId)) {
                            context.chainSpawner?.invoke(ArcaneTarget(entityTarget = e))
                        }
                    }
                }
            }
        }
    }


    // -----------------------------------------------------------------------------------
    //  SLASH — a single perpendicular cut: a 1D line drawn across the caster's look
    //          direction. Applies the source's effect once to anything the line crosses.
    //          Fires instantly, lingers not at all, places nothing. The flat sibling of Wall.
    // -----------------------------------------------------------------------------------
    class Slash : CastingRune() {
        override val name = "slash"
        override val displayName = "Slash"

        override fun build(builder: CastingBuilder) {
            builder.damage = 0.0   // bonus on top of the source's own base; source carries the hit
            builder.spread = 4.0   // length of the cut
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val at = (context.targetLocation ?: context.castingLocation).clone()
            val world = at.world
            val width = builder.spread.coerceIn(1.0, 32.0)
            val halfWidth = width / 2.0
            val thickness = 0.6   // how close along the look axis still counts as cut
            val bonus = builder.damage

            // The cut runs across the caster's horizontal look direction.
            var facing = context.direction.clone().setY(0.0)
            if (facing.lengthSquared() < 1e-4) facing = Vector(1.0, 0.0, 0.0)
            facing = facing.normalize()
            val across = Vector(-facing.z, 0.0, facing.x)

            // Draw the line.
            val steps = maxOf(2, (width * 3).toInt())
            for (i in 0..steps) {
                val w = -halfWidth + width * i / steps
                val point = at.clone().add(across.clone().multiply(w))
                world.spawnParticle(source.particle, point, 1, 0.02, 0.02, 0.02, 0.0)
            }

            // Apply the effect once to anything the line crosses.
            val ignored = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
            at.getNearbyLivingEntities(halfWidth + 2.0).forEach { e ->
                if (e in ignored) return@forEach
                val to = e.boundingBox.center.subtract(at.toVector())
                val halfW = e.width / 2.0
                val a = to.dot(across)   // offset along the cut
                val n = to.dot(facing)   // distance from the cut plane
                val h = to.y             // height relative to the cut
                val crossing = Math.abs(n) <= thickness + halfW &&
                        a in (-halfWidth - halfW)..(halfWidth + halfW) &&
                        Math.abs(h) <= e.height / 2.0 + 0.4
                if (crossing) {
                    source.invoke(ArcaneTarget(entityTarget = e), context.caster, facing, bonus)
                }
            }

            context.targetLocation = at
        }
    }


    // -----------------------------------------------------------------------------------
    //  ERUPT — a geyser: a steady vertical spout from the target. Range -> column height,
    //          Wide -> column radius, Amplify -> damage. Anything caught inside the column
    //          is struck on an interval and launched upward. Lingers a moment, places
    //          nothing. The vertical sibling of Wall.
    // -----------------------------------------------------------------------------------
    class Erupt : CastingRune() {
        override val name = "erupt"
        override val displayName = "Erupt"

        override fun build(builder: CastingBuilder) {
            builder.damage = 3.0    // bonus on top of the source's own base
            builder.range = 6.0     // column height
            builder.spread = 1.5    // column radius
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val base = (context.targetLocation ?: context.castingLocation).clone()
            val height = builder.range.coerceIn(2.0, 24.0)
            val radius = builder.spread.coerceIn(0.5, 8.0)

            GeyserTask(source, context, base, height, radius, builder.damage)
                .runTaskTimer(Odyssey.instance, 0, 1)

            // Spout lingers in the background; the chain continues from its mouth.
            context.targetLocation = base
        }

        private class GeyserTask(
            private val source: ArcaneSource,
            private val context: CastingContext,
            private val base: Location,
            private val height: Double,
            private val radius: Double,
            private val bonus: Double
        ) : BukkitRunnable() {

            private val world = base.world
            private val ignored: List<LivingEntity> = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
            private val lastHitTick = HashMap<UUID, Int>()
            private val chainedEntities = HashSet<UUID>()   // Link reaction: once per entity
            private val applyInterval = 8
            private val lifetimeTicks = 40                  // ~2s
            private var tick = 0

            override fun run() {
                if (tick >= lifetimeTicks) { cancel(); return }
                render()
                sweep()
                tick++
            }

            // A jittering column with a bright "head" that climbs and resets, so the stream
            // keeps pumping upward like a spout.
            private fun render() {
                val steps = maxOf(4, (height * 2).toInt())
                for (i in 0..steps) {
                    val f = i.toDouble() / steps
                    val jitter = radius * (0.15 + 0.25 * f)          // a touch wider up top
                    val a = Math.random() * 2.0 * Math.PI
                    val r = sqrt(Math.random()) * jitter
                    val at = base.clone().add(cos(a) * r, height * f, sin(a) * r)
                    world.spawnParticle(source.particle, at, 1, 0.02, 0.03, 0.02, 0.01)
                }
                val headY = ((tick % 12) / 12.0) * height
                world.spawnParticle(source.particle, base.clone().add(0.0, headY, 0.0), 5, radius * 0.3, 0.1, radius * 0.3, 0.06)
                world.spawnParticle(source.particle, base.clone().add(0.0, 0.15, 0.0), 4, radius * 0.25, 0.1, radius * 0.25, 0.05)
                if (tick == 0) world.playSound(base, Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 1.6f)
            }

            private fun sweep() {
                base.getNearbyLivingEntities(radius + 1.0).forEach { e ->
                    if (e in ignored) return@forEach
                    val dx = e.location.x - base.x
                    val dz = e.location.z - base.z
                    val flat = Math.sqrt(dx * dx + dz * dz)
                    val h = e.location.y - base.y
                    if (flat > radius + e.width / 2.0 || h !in -1.0..(height + 1.0)) return@forEach

                    val last = lastHitTick[e.uniqueId]
                    if (last == null || tick - last >= applyInterval) {
                        lastHitTick[e.uniqueId] = tick
                        source.invoke(ArcaneTarget(entityTarget = e), context.caster, Vector(0.0, 1.0, 0.0), bonus)
                        // Geyser launch: mostly up, a touch outward.
                        val out = if (flat > 1e-4) Vector(dx / flat, 0.0, dz / flat).multiply(0.2) else Vector()
                        e.velocity = out.setY(0.8)
                        if (chainedEntities.add(e.uniqueId)) {
                            context.chainSpawner?.invoke(ArcaneTarget(entityTarget = e))
                        }
                    }
                }
            }
        }
    }


    // -----------------------------------------------------------------------------------
    //  VORTEX — a tornado: rotating helical strands climbing a funnel. Range -> height,
    //           Wide -> crown radius (funnel is narrow at the foot, wide up top), Speed ->
    //           spin rate, Invert -> spins the other way. Entities inside are dragged toward
    //           the eye, spun, lifted, and struck on an interval. Lingers a few seconds,
    //           places nothing. The swirling sibling of Zone.
    // -----------------------------------------------------------------------------------
    class Vortex : CastingRune() {
        override val name = "vortex"
        override val displayName = "Vortex"

        override fun build(builder: CastingBuilder) {
            builder.damage = 2.0
            builder.range = 8.0     // funnel height
            builder.spread = 3.0    // crown radius
            builder.speed = 0.35    // spin rate (radians/tick)
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val center = (context.targetLocation ?: context.castingLocation).clone()
            val height = builder.range.coerceIn(2.0, 24.0)
            val crownRadius = builder.spread.coerceIn(1.0, 12.0)
            val spinDir = if (builder.invert) -1.0 else 1.0
            TornadoTask(source, context, center, height, crownRadius, spinDir, builder.damage)
                .runTaskTimer(Odyssey.instance, 0, 1)
            context.targetLocation = center
        }

        private class TornadoTask(
            private val source: ArcaneSource,
            private val context: CastingContext,
            private val center: Location,
            private val height: Double,
            private val crownRadius: Double,
            private val spinDir: Double,
            private val bonus: Double
        ) : BukkitRunnable() {

            // ============================== KNOBS ==============================
            private val DEBUG              = true
            // Funnel shape
            private val footRadiusFraction = 0.25   // foot radius as a fraction of the crown
            // Motion
            private val secondsPerFootRev  = 4.0    // time for the fastest (foot) ring to make one turn
            private val totalTwistTurns    = 0.70   // static helix winding foot->crown, in turns (< 1 = sweep)
            // Band geometry — ANGULAR
            private val arms               = 3      // number of bands
            private val armWidthDeg        = 80.0  // angular WIDTH of each band; gap = (360/arms - armWidthDeg)
            // Particle density — LINEAR (constant block spacing => sparse foot, dense crown)
            private val arcStepBlocks      = 2.55   // spacing between particles ACROSS a band's arc
            private val climbStepBlocks    = 1.25   // spacing between stacked rings up the column
            private val renderEveryTicks   = 1      // bump to 2 if the particle count is heavy
            // Cadence
            private val applyInterval      = 10
            // ==================================================================

            // ------------------------ DERIVED (don't hand-tune) ------------------------
            private val twoPi         = 2.0 * Math.PI
            private val baseRadius    = (crownRadius * footRadiusFraction).coerceAtLeast(0.4)
            private val armSpacingRad = twoPi / arms                                   // centre-to-centre between bands
            private val armWidthRad   = Math.toRadians(armWidthDeg).coerceIn(0.02, armSpacingRad)  // capped so bands don't fuse
            private val totalTwist    = totalTwistTurns * twoPi

            // Vertical resolution from the desired climb spacing along the average helix.
            private val avgRadius     = (baseRadius + crownRadius) / 2.0
            private val helixArc      = run { val s = avgRadius * totalTwist; sqrt(height * height + s * s) }
            private val planes        = maxOf(4, ceil(helixArc / climbStepBlocks).toInt())
            private val twistPerPlane = totalTwist / planes

            // Angular velocity from CONSTANT tangential speed (v = ω·r), anchored at the foot.
            private val omegaFoot     = spinDir * twoPi / (secondsPerFootRev * 20.0)
            private val tangential    = abs(omegaFoot) * baseRadius
            private fun radiusAt(f: Double) = baseRadius + (crownRadius - baseRadius) * f
            private fun omegaAt(f: Double)  = spinDir * tangential / radiusAt(f)
            private fun spinAt(f: Double)   = omegaAt(f) * tick
            // Particles across a band at radius r: arc length / spacing. THE fix.
            private fun acrossAt(r: Double) = maxOf(1, ceil(r * armWidthRad / arcStepBlocks).toInt())

            private val formTicks = (height * 10).toInt().coerceIn(60, 100)
            private val lifeTicks = 100

            private val world = center.world
            private val ignored: List<LivingEntity> = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
            private val lastHitTick = HashMap<UUID, Int>()
            private val chainedEntities = HashSet<UUID>()
            private var tick = 0

            override fun run() {
                if (tick == 0 && DEBUG) logDerived()
                if (tick >= formTicks + lifeTicks) { cancel(); return }
                if (tick % renderEveryTicks == 0) render()
                if (tick >= formTicks) sweep()
                tick++
            }

            private fun logDerived() {
                Odyssey.instance.logger.info(
                    ("[Vortex] planes=%d  band=%.0f°  gap=%.0f°  foot=%d/band  crown=%d/band  ~%d particles/frame  " +
                            "ωfoot=%.4f  ωcrown=%.4f")
                        .format(
                            planes, Math.toDegrees(armWidthRad), Math.toDegrees(armSpacingRad - armWidthRad),
                            acrossAt(baseRadius) + 1, acrossAt(crownRadius) + 1,
                            arms * planes * (acrossAt(avgRadius) + 1),
                            omegaFoot, omegaAt(1.0)
                        )
                )
            }

            private fun render() {
                val forming = tick < formTicks
                val form = if (forming) tick.toDouble() / formTicks else 1.0
                val bottomF = 1.0 - form   // funnel descends: crown first, foot last

                for (a in 0 until arms) {
                    val armCenter = a * armSpacingRad
                    for (p in 0..planes) {
                        val f = p.toDouble() / planes
                        if (f < bottomF) continue
                        val r = radiusAt(f)
                        val ringAngle = armCenter + twistPerPlane * p + spinAt(f)   // band CENTRE at this height
                        val across = acrossAt(r)                                    // <- radius-driven particle count
                        for (j in 0..across) {
                            val angle = ringAngle - armWidthRad / 2.0 + armWidthRad * j / across
                            val at = center.clone().add(cos(angle) * r, height * f, sin(angle) * r)
                            world.spawnParticle(source.particle, at, 1, 0.0, 0.0, 0.0, 0.0)
                        }
                    }
                }

                if (forming) {
                    val tipR = radiusAt(bottomF)
                    world.spawnParticle(source.particle, center.clone().add(0.0, height * bottomF, 0.0), 6, tipR * 0.4, 0.1, tipR * 0.4, 0.03)
                } else {
                    val gr = radiusAt(0.0)
                    for (a in 0 until 2) {
                        val ga = spinAt(0.0) * 1.7 + a * Math.PI   // ground dust, off-rate so it isn't locked to the bands
                        world.spawnParticle(source.particle, center.clone().add(cos(ga) * gr, 0.1, sin(ga) * gr), 2, 0.05, 0.02, 0.05, 0.0)
                    }
                }
                if (tick % 20 == 0) world.playSound(center, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.4f, 0.7f)
            }

            private fun sweep() {
                center.getNearbyLivingEntities(crownRadius + 1.0).forEach { e ->
                    if (e in ignored) return@forEach
                    val dx = e.location.x - center.x
                    val dz = e.location.z - center.z
                    val flat = Math.sqrt(dx * dx + dz * dz)
                    val h = e.location.y - center.y
                    val f = (h / height).coerceIn(0.0, 1.0)
                    if (flat > radiusAt(f) + e.width / 2.0 || h !in -1.0..(height + 1.0)) return@forEach

                    val hasRadial = flat > 1e-4
                    val inward  = if (hasRadial) Vector(-dx / flat, 0.0, -dz / flat) else Vector(0.0, 0.0, 0.0)
                    val tangent = if (hasRadial) Vector(-dz / flat, 0.0,  dx / flat) else Vector(0.0, 0.0, 0.0)
                    e.velocity = inward.clone().multiply(0.25)
                        .add(tangent.clone().multiply(0.35 * spinDir))
                        .setY(0.28)

                    val last = lastHitTick[e.uniqueId]
                    if (last == null || tick - last >= applyInterval) {
                        lastHitTick[e.uniqueId] = tick
                        val hitDir = if (hasRadial) inward.clone() else Vector(0.0, 1.0, 0.0)
                        source.invoke(ArcaneTarget(entityTarget = e), context.caster, hitDir, bonus)
                        if (chainedEntities.add(e.uniqueId)) {
                            context.chainSpawner?.invoke(ArcaneTarget(entityTarget = e))
                        }
                    }
                }
            }
        }
    }


    // -----------------------------------------------------------------------------------
    //  OLD_WALL — a standing particle plane. Range -> height, Wide -> width. Applies the
    //         source's effect to any entity whose body crosses it, and re-applies on an
    //         interval while they linger inside. Lasts a few seconds, places nothing.
    // -----------------------------------------------------------------------------------
    class OldWall : CastingRune() {
        override val name = "wall"
        override val displayName = "Wall"

        override fun build(builder: CastingBuilder) {
            builder.damage = 0.0   // bonus on top of the source's own base; source carries the hit
            builder.range = 4.0    // height
            builder.spread = 4.0   // width
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder, onComplete: () -> Unit) {
            val base = (context.targetLocation ?: context.castingLocation).clone()
            val height = builder.range.coerceIn(1.0, 24.0)
            val width = builder.spread.coerceIn(1.0, 32.0)

            // The plane stands across the caster's horizontal look direction.
            var facing = context.direction.clone().setY(0.0)
            if (facing.lengthSquared() < 1e-4) facing = Vector(1.0, 0.0, 0.0)
            facing = facing.normalize()
            val across = Vector(-facing.z, 0.0, facing.x)

            WallTask(source, context, base, facing, across, width, height, builder.damage)
                .runTaskTimer(Odyssey.instance, 0, 1)

            // Wall lingers in the background; the chain continues from its base immediately.
            context.targetLocation = base
        }

        private class WallTask(
            private val source: ArcaneSource,
            private val context: CastingContext,
            private val base: Location,
            private val facing: Vector,
            private val across: Vector,
            private val width: Double,
            private val height: Double,
            private val bonus: Double
        ) : BukkitRunnable() {

            private val world = base.world
            private val ignored: List<LivingEntity> = context.ignoredTargets.mapNotNull { it.entityTarget as? LivingEntity }
            private val lastHitTick = HashMap<UUID, Int>()
            private val halfWidth = width / 2.0
            private val thickness = 0.5     // how "solid" the plane is along its normal
            private val applyInterval = 10  // re-apply to a lingering entity every N ticks
            private val lifetimeTicks = 100 // wall duration
            private var tick = 0

            override fun run() {
                if (tick >= lifetimeTicks) { cancel(); return }
                if (tick % 2 == 0) render()
                sweep()
                tick++
            }

            private fun render() {
                val up = Vector(0.0, 1.0, 0.0)
                val wSteps = maxOf(2, (width * 2).toInt())
                val hSteps = maxOf(2, (height * 2).toInt())
                for (wi in 0..wSteps) {
                    val w = -halfWidth + width * wi / wSteps
                    for (hi in 0..hSteps) {
                        val at = base.clone()
                            .add(across.clone().multiply(w))
                            .add(up.clone().multiply(height * hi / hSteps))
                        world.spawnParticle(source.particle, at, 1, 0.02, 0.02, 0.02, 0.0)
                    }
                }
            }

            private fun sweep() {
                val reach = maxOf(halfWidth, height) + 2.0
                base.getNearbyLivingEntities(reach).forEach { e ->
                    if (e in ignored) return@forEach
                    // Project the entity's centre into the wall's local frame.
                    val to = e.boundingBox.center.subtract(base.toVector())
                    val halfW = e.width / 2.0
                    val a = to.dot(across)   // offset along the wall
                    val n = to.dot(facing)   // distance from the plane
                    val h = to.y             // height above the base
                    val crossing = Math.abs(n) <= thickness + halfW &&
                            a in (-halfWidth - halfW)..(halfWidth + halfW) &&
                            h in -0.4..(height + 0.4)
                    if (!crossing) return@forEach

                    val last = lastHitTick[e.uniqueId]
                    if (last == null || tick - last >= applyInterval) {
                        lastHitTick[e.uniqueId] = tick
                        source.invoke(ArcaneTarget(entityTarget = e), context.caster, facing, bonus)
                    }
                }
            }
        }
    }
}