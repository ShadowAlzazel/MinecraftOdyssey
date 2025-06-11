package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

// Category sealed class for "Manifestation"
// These runes determine the fundamental way magic is expressed
@Suppress("UnstableApiUsage")
sealed class ManifestationRune : ArcaneRune(), RayTracerAndDetector,
    AttackHelper, VectorParticles {
    // Abstract Methods for runes and their implementation
    abstract fun manifest(context: CastingContext)

    // Class entry points for usage across systems
    fun cast(context: CastingContext) {
        this.manifest(context)
    }


    // List of all manifestation runes

    class Projectile : ManifestationRune() {
        override val name = "projectile"
        override val displayName = "Projectile"

        override fun manifest(context: CastingContext) {}
    }

    class Beam : ManifestationRune() {
        override val name = "beam"
        override val displayName = "Beam"

        override fun manifest(context: CastingContext) {
            // Logic
            val endLocation: Location
            val startLocation: Location
            val caster = context.caster

            // ------------ MODIFIERS --------------
            // Core
            var damage = 4.0
            var damageType = DamageType.MAGIC
            var particle = Particle.WITCH
            // Modifier Variables
            var totalRange = 16.0 // Default for NOW
            var aimAssist = 0.5
            // Apply modifiers (exhaustive List)
            context.modifiers.forEach { modifier ->
                when(modifier) {
                    is ModifierRune.Range -> totalRange += modifier.value
                    is ModifierRune.Convergence -> aimAssist += modifier.value
                    is ModifierRune.Amplify -> damage += modifier.value
                    is ModifierRune.Source -> {
                        damageType = modifier.damageType
                        particle = modifier.particle
                    }
                    else -> {}
                }
            }

            // ------------ STUFF -----------------
            // Temporary ray trace func
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
            val particleCount = endLocation.distance(caster.location) * 6
            spawnLineParticles(
                particle = particle,
                start = caster.location,
                end = endLocation,
                count = particleCount.toInt()
            )
            caster.world.playSound(caster.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }

    class Slice : ManifestationRune() {
        override val name = "slice"
        override val displayName = "Slice"

        override fun manifest(context: CastingContext) {
        }
    }

    class Zone : ManifestationRune() {
        override val name = "zone"
        override val displayName = "Zone"

        override fun manifest(context: CastingContext) {
            // ------------ MODIFIERS --------------
            // Core
            var damage = 4.0
            var damageType = DamageType.MAGIC
            var particle = Particle.WITCH
            // Modifier Variables
            var range = 16.0
            var radius = 3.0
            var aimAssist = 0.25
            // Apply modifiers (exhaustive List)
            context.modifiers.forEach { modifier ->
                when(modifier) {
                    is ModifierRune.Range -> range += modifier.value
                    is ModifierRune.Convergence -> aimAssist += modifier.value
                    is ModifierRune.Amplify -> damage += modifier.value
                    is ModifierRune.Wide -> radius += modifier.value
                    is ModifierRune.Source -> {
                        damageType = modifier.damageType
                        particle = modifier.particle
                    }
                    else -> {}
                }
            }
            // ------------ STUFF -----------------
            // Circle Logic
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
}