package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import org.bukkit.entity.LivingEntity


sealed class DomainRune: ArcaneRune(), RayTracerAndDetector {
    // Domain runes change the casting context of the spell.
    // Changes like the location or entity where it originates or targets

    fun change(kernel: CastingContext, context: CastingContext) {

        val domain = context.clone()
        var successful = true

        // Want to change context
        when (this) {
            is Origin -> {
                domain.castingLocation = kernel.castingLocation
            }
            is Trace -> {
                val traceEntity = getEntityRayTrace(
                    domain.castingLocation,
                    domain.direction,
                    listOf(context.caster),
                    20.0,
                    0.05)

                // Check if target
                if (traceEntity is LivingEntity) {
                    domain.targetLocation = traceEntity.location
                    domain.target = traceEntity
                }
                // If not try ray trace again
                else {
                    val traceLocation = getHitLocationRayTrace(
                        domain.castingLocation,
                        domain.direction,
                        listOf(context.caster),
                        32.0,
                        0.05)
                    if (traceLocation != null) {
                        domain.targetLocation = traceLocation
                    } else {
                        successful = false
                    }
                }

            }
            is Self -> {
                // Add self to target-able entities
                if (domain.caster in domain.ignoredTargets) {
                    domain.ignoredTargets.remove(domain.caster)
                }
                domain.target = domain.caster
            }
            is Next -> {
                val target = domain.target
                if (target != null) {
                    // Move to eye height
                    domain.castingLocation = if (target is LivingEntity) {
                        target.eyeLocation
                    } else {
                        target.location.clone().add(0.0, 0.5, 0.0)
                    }
                }
                else if (domain.targetLocation != null) {
                    domain.castingLocation = domain.targetLocation!!
                }
                else {
                    successful = false
                }
            }
            is Nearby -> {
                // Get Nearby entities if not target or ignored
                val nearby = domain.castingLocation.getNearbyLivingEntities(4.0)
                nearby.remove(domain.target) // Remove self
                nearby.removeAll { it in domain.ignoredTargets } // Remove if in the `ignore` list
                // Continue without errors
                if (nearby.isNotEmpty()) {  // Ignore empty list
                    // Sort list to nearest
                    val sortedNearby = nearby.sortedBy { it.location.distance(domain.castingLocation) }
                    val nearestTarget = sortedNearby.first()
                    // Set Target, Location and Direction
                    domain.target = nearestTarget
                    domain.targetLocation = nearestTarget.eyeLocation
                    domain.direction = nearestTarget.eyeLocation.clone().subtract(domain.castingLocation).toVector()
                }
                else {
                    successful = false
                }
            }
            is Invert -> {
                // Swap domains
                if (domain.targetLocation != null) {
                    val temp = domain.castingLocation
                    domain.castingLocation = domain.targetLocation!!
                    domain.targetLocation = temp
                }
                else {
                    successful = false
                }
            }
            is Differ -> {
                if (domain.target != null) domain.ignoredTargets.add(domain.target!!)
                else successful = false
            }
            else -> successful = false
        }
        // Apply changes to the context
        if (successful) {
            context.also {
                it.castingLocation = domain.castingLocation
                it.direction = domain.direction
                it.target = domain.target
                it.targetLocation = domain.targetLocation
                // ignore targets
                for (e in domain.ignoredTargets) {
                    if (e !in it.ignoredTargets) it.ignoredTargets.add(e)
                }
            }
        }

    }

    // Sets the cast location back to the ORIGINAL cast
    data object Origin : DomainRune() {
        override val name = "origin"
        override val displayName = "Origin"
    }

    // This changes the `castingLocation` to the CURRENT `targetLocation`
    data object Next : DomainRune() {
        override val name = "next"
        override val displayName = "next"
    }

    data object Trace : DomainRune() {
        override val name = "trace"
        override val displayName = "trace"
    }

    data object Link : DomainRune() {
        override val name = "link"
        override val displayName = "link"
    }

    data object Self : DomainRune() {
        override val name = "self"
        override val displayName = "self"
    }

    // This `returns` the nearest entity. Can stack with other variable runes
    // Sets the `target` to the nearest `entity`
    data object Nearby : DomainRune() {
        override val name = "nearby"
        override val displayName = "nearby"
    }

    // Looks for a new entity/location that is NOT the same
    data object Differ : DomainRune() {
        override val name = "differ"
        override val displayName = "differ"
    }

    // Switches the `cast` and `target` locations/entities in the context
    data object Invert : DomainRune() {
        override val name = "invert"
        override val displayName = "Invert"
    }

}