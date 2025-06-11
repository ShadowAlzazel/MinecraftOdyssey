package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.ArcaneContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity

// Category sealed class for "Manifestation"
// These runes determine the fundamental way magic is expressed
@Suppress("UnstableApiUsage")
sealed class ManifestationRune : ArcaneRune(), RayTracerAndDetector, AttackHelper, VectorParticles {
    // Abstract Methods for runes and their implementation
    abstract fun manifest(context: ArcaneContext)

    // Class entry points for usage across systems
    fun cast(context: ArcaneContext) {
        this.manifest(context)
    }


    // List of all manifestation runes

    class Projectile : ManifestationRune() {
        override val name = "projectile"
        override val displayName = "Projectile"

        override fun manifest(context: ArcaneContext) {}
    }

    class Beam : ManifestationRune() {
        override val name = "beam"
        override val displayName = "Beam"

        override fun manifest(context: ArcaneContext) {
            // Logic
            val endLocation: Location
            val startLocation: Location
            val caster = context.caster

            // Core
            var damageType = DamageType.MAGIC
            var particle = Particle.WITCH

            // Vars for modifiers
            var totalRange = 16.0 // Default for NOW
            var aimAssist = 0.5
            var damage = 4.0
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

        override fun manifest(context: ArcaneContext) {}
    }

    class Zone : ManifestationRune() {
        override val name = "zone"
        override val displayName = "Zone"

        override fun manifest(context: ArcaneContext) {}
    }
}