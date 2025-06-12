package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.arcane.util.ManifestBuild
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
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
        val build: ManifestBuild) : BukkitRunnable() {
        override fun run() {
            rune.manifest(context, build)
        }
    }

    // Abstract Methods for runes and their implementation
    abstract fun formulate(context: CastingContext): ManifestBuild

    abstract fun manifest(context: CastingContext, build: ManifestBuild)

    // Class entry points for usage across systems
    fun cast(context: CastingContext) {
        //this.manifest(context)
        val build = formulate(context)
        // Delay
        if (build.delayInTicks > 0) {
            val runner = ManifestAsyncRunner(this, context, build)
            runner.runTaskLater(Odyssey.instance, build.delayInTicks)
        } else {
            this.manifest(context, build)
        }

    }


    // List of all manifestation runes

    class Zone : ManifestationRune() {
        override val name = "zone"
        override val displayName = "Zone"

        // A function that prepares modifiers and other runes before the spell is manifested
        override fun formulate(context: CastingContext): ManifestBuild {
            // ------------ Formulate --------------
            // Core
            var damage = 4.0
            var damageType = DamageType.MAGIC
            var particle = Particle.WITCH
            // Modifier Variables
            var range = 16.0
            var radius = 3.0
            var aimAssist = 0.25
            var delay = 0.0
            // Apply modifiers (exhaustive List)
            // FOR NOW is NOTE Sequential
            // TODO: Priority and cyclic order
            context.modifiers.forEachIndexed { i, modifier ->
                when(modifier) {
                    is ModifierRune.Range -> range += modifier.value
                    is ModifierRune.Convergence -> aimAssist += modifier.value
                    is ModifierRune.Amplify -> damage += modifier.value
                    is ModifierRune.Wide -> radius += modifier.value
                    is ModifierRune.Source -> {
                        damageType = modifier.damageType
                        particle = modifier.particle
                    }
                    is ModifierRune.Delay -> delay += modifier.value
                    else -> {}
                }
                // TODO: STOP!! if we reach another manifestation rune
            }
            // Create Build
            delay *= 20 // convert delay to Ticks
            return ManifestBuild(
                damage = damage,
                damageType = damageType,
                radius = radius,
                range = range,
                aimAssist = aimAssist,
                delayInTicks = delay.toLong(),
                particle = particle
            )
            // TODO also return index of used RUNES

        }

        override fun manifest(context: CastingContext, build: ManifestBuild) {
            // Unpack build
            val range = build.range
            val aimAssist = build.aimAssist
            val radius = build.radius
            val damage = build.damage
            val damageType = build.damageType
            val particle = build.particle

            // Circle Logic
            // TODO: Move detection and location logic to the FORMULATE stage
            val circleCenter = getRayTraceLocation(context.caster, range, aimAssist) ?: return
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

        override fun formulate(context: CastingContext): ManifestBuild {
            // Start Build
            val build = ManifestBuild()

            // Apply modifiers to build (exhaustive List)
            context.modifiers.forEach { modifier ->
                when(modifier) {
                    is ModifierRune.Range -> build.range += modifier.value
                    is ModifierRune.Convergence -> build.aimAssist += modifier.value
                    is ModifierRune.Amplify -> build.damage += modifier.value
                    is ModifierRune.Source -> { // TODO: TEMP
                        build.damageType = modifier.damageType
                        build.particle = modifier.particle
                    }
                    else -> {}
                }
            }
            // TODO: STOP!! if we reach another manifestation rune
            return build
        }

        override fun manifest(context: CastingContext, build: ManifestBuild) {
            // Unpack context
            val caster = context.caster
            val castLocation = context.castingLocation
            // Unpack build
            val totalRange = build.range
            val aimAssist = build.aimAssist
            val damageType = build.damageType
            val damage = build.damage
            val particle = build.particle

            // Temporary ray trace func
            val endLocation: Location
            val target = getRayTraceEntity(caster, totalRange, aimAssist)
            // After running target checks
            if (target is LivingEntity) {
                val damageSource = createEntityDamageSource(caster, null, damageType)
                target.damage(damage, damageSource)
                endLocation = target.eyeLocation
            }
            else {
                endLocation = caster.eyeLocation.clone().add(caster.eyeLocation.direction.clone().normalize().multiply(totalRange))
            }

            // Particles in Line
            val particleCount = endLocation.distance(castLocation) * 6
            spawnLineParticles(
                particle = particle,
                start = castLocation,
                end = endLocation,
                count = particleCount.toInt()
            )
            context.world.playSound(caster.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }

    class Projectile : ManifestationRune() {
        override val name = "projectile"
        override val displayName = "Projectile"

        override fun formulate(context: CastingContext): ManifestBuild {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, build: ManifestBuild) {
            TODO("Not yet implemented")
        }
    }

    class Slice : ManifestationRune() {
        override val name = "slice"
        override val displayName = "Slice"

        override fun formulate(context: CastingContext): ManifestBuild {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, build: ManifestBuild) {
            TODO("Not yet implemented")
        }
    }


}