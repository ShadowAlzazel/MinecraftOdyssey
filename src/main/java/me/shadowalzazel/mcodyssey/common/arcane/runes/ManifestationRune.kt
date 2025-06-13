package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.arcane.util.ManifestBuilder
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

// Category sealed class for "Manifestation"
// These runes determine the fundamental way magic is expressed
@Suppress("UnstableApiUsage")
sealed class ManifestationRune : ArcaneRune(), RayTracerAndDetector,
    AttackHelper, VectorParticles {

    // Helper class to run async calls later for runes
    class ManifestAsyncRunner(
        val rune: ManifestationRune,
        val context: CastingContext,
        val build: ManifestBuilder) : BukkitRunnable() {
        override fun run() {
            rune.manifest(context, build)
        }
    }

    // Abstract Methods for runes and their implementation

    // A function that prepares modifiers and other runes before the spell is manifested
    abstract fun build(context: CastingContext): ManifestBuilder

    abstract fun manifest(context: CastingContext, builder: ManifestBuilder)


    /**
     * Class entry points for usage across systems
     */
    fun cast(context: CastingContext, builder: ManifestBuilder) {
        // Delay
        if (builder.delayInTicks > 0) {
            val runner = ManifestAsyncRunner(this, context, builder)
            runner.runTaskLater(Odyssey.instance, builder.delayInTicks)
        } else {
            this.manifest(context, builder)
        }

    }

    /**
     * This function is to use with the builder to add other runes
     */
    fun modify(builder: ManifestBuilder, runeAddOn: ArcaneRune) {
        // Apply modifiers (exhaustive List)
        when(runeAddOn) {
            is ModifierRune.Range -> builder.range += runeAddOn.value
            is ModifierRune.Convergence -> builder.aimAssist += runeAddOn.value
            is ModifierRune.Amplify -> builder.damage += runeAddOn.value
            is ModifierRune.Wide -> builder.radius += runeAddOn.value
            is ModifierRune.Source -> {
                builder.damageType = runeAddOn.damageType
                builder.particle = runeAddOn.particle
            }
            is ModifierRune.Delay -> builder.delayInTicks += (runeAddOn.value * 20).toLong()
            else -> {}
        }
    }


    // List of all manifestation runes

    class Zone : ManifestationRune() {
        override val name = "zone"
        override val displayName = "Zone"


        override fun build(context: CastingContext): ManifestBuilder {
            // DEFAULT build parameters
            val damage = 1.0
            val range = 16.0
            val radius = 3.0
            val aimAssist = 0.1
            // Return the default builder for this rune
            return ManifestBuilder(
                damage = damage,
                radius = radius,
                range = range,
                aimAssist = aimAssist
            )
        }

        override fun manifest(context: CastingContext, builder: ManifestBuilder) {
            // Unpack build
            val range = builder.range
            val aimAssist = builder.aimAssist
            val radius = builder.radius
            val damage = builder.damage
            val damageType = builder.damageType
            val particle = builder.particle

            // Circle Location and Detection Logic
            //val circleCenter = getRayTraceLocation(context.caster, range, aimAssist) ?: return
            val traceLocation = getPathTraceLocation(
                context.castingLocation,
                context.direction,
                listOf(context.caster),
                range,
                raySize = aimAssist)
            val circleCenter = traceLocation ?: context.targetLocation
            circleCenter ?: return

            // Damage Logic
            val damageSource = createEntityDamageSource(context.caster, null, damageType)
            circleCenter.getNearbyLivingEntities(radius).forEach {
                it.damage(damage, damageSource)
            }
            // Particles and effects
            spawnCircleParticles(
                particle = particle,
                center = circleCenter,
                upDirection = Vector(0, 1, 0),
                radius = radius,
                heightOffset = 0.25,
                count = (radius * Math.PI * 7).toInt())
            context.world.playSound(context.castingLocation, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }


    class Beam : ManifestationRune() {
        override val name = "beam"
        override val displayName = "Beam"

        override fun build(context: CastingContext): ManifestBuilder {
            // DEFAULT build parameters
            val damage = 2.0
            val range = 16.0
            val aimAssist = 0.25
            // Return the default builder for this rune
            return ManifestBuilder(
                damage = damage,
                range = range,
                aimAssist = aimAssist
            )
        }

        override fun manifest(context: CastingContext, builder: ManifestBuilder) {
            // Unpack build
            val totalRange = builder.range
            val aimAssist = builder.aimAssist
            val damageType = builder.damageType
            val damage = builder.damage
            val particle = builder.particle

            // Temporary locations for beam
            val startLocation: Location = context.castingLocation ?: return
            val endLocation: Location

            //val target = getRayTraceEntity(context.caster, totalRange, aimAssist)
            val target = getPathTraceEntity(
                startLocation,
                context.direction,
                listOf(context.caster),
                totalRange,
                aimAssist)

            // After running target checks
            if (target is LivingEntity) {
                val damageSource = createEntityDamageSource(context.caster, null, damageType)
                target.damage(damage, damageSource)
                endLocation = target.eyeLocation
            }
            else {
                endLocation = context.castingLocation.clone().add(context.direction.clone().normalize().multiply(totalRange))
            }

            // Particles in Line
            val particleCount = endLocation.distance(context.castingLocation) * 6
            spawnLineParticles(
                particle = particle,
                start = context.castingLocation,
                end = endLocation,
                count = particleCount.toInt()
            )
            context.world.playSound(context.castingLocation, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }

    class Projectile : ManifestationRune() {
        override val name = "projectile"
        override val displayName = "Projectile"

        override fun build(context: CastingContext): ManifestBuilder {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, builder: ManifestBuilder) {
            TODO("Not yet implemented")
        }
    }

    class Slice : ManifestationRune() {
        override val name = "slice"
        override val displayName = "Slice"

        override fun build(context: CastingContext): ManifestBuilder {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, builder: ManifestBuilder) {
            TODO("Not yet implemented")
        }
    }

    class Aura : ManifestationRune() {
        override val name = "aura"
        override val displayName = "aura"

        override fun build(context: CastingContext): ManifestBuilder {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, builder: ManifestBuilder) {
            TODO("Not yet implemented")
        }
    }


}