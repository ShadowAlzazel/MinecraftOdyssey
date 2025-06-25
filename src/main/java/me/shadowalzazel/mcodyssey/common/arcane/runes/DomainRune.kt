package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.ArcaneTarget
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import org.bukkit.entity.LivingEntity


sealed class DomainRune: ArcaneRune(), RayTracerAndDetector {
    // Domain runes change the casting context of the spell.
    // Changes like the location or entity where it originates or targets

    fun change(original: CastingContext, context: CastingContext) {

        val domain = context.clone()
        var successful = true

        // Want to change context
        when (this) {
            is Kernel -> {
                domain.castingLocation = original.castingLocation
            }
            is Origin -> {
                //val originLocation = context.caster
                val originLocation = context.caster.getLocation()
                domain.castingLocation = originLocation
            }
            is Direct -> {
                val range = 16.0
                val traceEntity = getEntityRayTrace(
                    domain.castingLocation,
                    domain.direction,
                    context.caster.toEntityList(),
                    range,
                    0.05)

                // Check if target
                if (traceEntity is LivingEntity) {
                    domain.targetLocation = traceEntity.location
                    domain.target = ArcaneTarget(entityTarget = traceEntity)
                }
                // If not try ray trace again
                else {
                    val traceLocation = getHitLocationRayTrace(
                        domain.castingLocation,
                        domain.direction,
                        context.caster.toEntityList(),
                        range,
                        0.05)
                    if (traceLocation != null) {
                        domain.targetLocation = traceLocation
                    } else {
                        domain.targetLocation = domain.castingLocation.clone().add(domain.direction.clone().normalize().multiply(range))
                    }
                }

            }
            is Self -> {
                // Add self to target-able entities
                /*
                if (domain.caster in domain.ignoredTargets) {
                    domain.ignoredTargets.remove(domain.caster)
                }
                 */

                val ignoredTargets = domain.ignoredTargets.toList()
                for (n in ignoredTargets) {
                    // Look for self in ignoredTargets, then remove
                    if (n.entityTarget == domain.caster.entityCaster) {
                        domain.ignoredTargets.remove(n)
                        break
                    }
                    if (n.blockTarget == domain.caster.blockCaster) {
                        domain.ignoredTargets.remove(n)
                        break
                    }
                }
                // Convert to target and set as new
                domain.target = domain.caster.convertToTarget()
            }
            is Next -> {
                //val target = domain.target
                if (domain.target != null) {
                    // Move to eye height
                    val entityTarget = domain.target!!.entityTarget
                    if (entityTarget is LivingEntity) {
                        domain.castingLocation = entityTarget.eyeLocation
                    } else {
                        domain.castingLocation = domain.target!!.getLocation()
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
                val nearby = domain.castingLocation.getNearbyLivingEntities(6.0)
                // Remove most recent target to prevent recursive calls
                if (domain.target?.entityTarget is LivingEntity) {
                    val entityTarget = domain.target!!.entityTarget as LivingEntity
                    nearby.remove(entityTarget)
                }
                // Remove if in the ignored list
                for (n in domain.ignoredTargets) {
                    if (n.entityTarget is LivingEntity) {
                        nearby.remove(n.entityTarget)
                    }
                }
                // Continue without errors
                if (nearby.isNotEmpty()) {  // Ignore empty list
                    // Sort list to nearest
                    val sortedNearby = nearby.sortedBy { it.location.distance(domain.castingLocation) }
                    val nearestEntity = sortedNearby.first()
                    // Set Target, Location and Direction
                    domain.target = ArcaneTarget(entityTarget = nearestEntity)
                    domain.targetLocation = nearestEntity.eyeLocation
                    domain.direction = nearestEntity.eyeLocation.clone().subtract(domain.castingLocation).toVector()
                }
                else {
                    successful = false
                }
            }
            is Swap -> {
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

    data object Kernel : DomainRune() {
        override val name = "kernel"
        override val displayName = "Kernel"
    }

    // This changes the `castingLocation` to the CURRENT `targetLocation`
    data object Next : DomainRune() {
        override val name = "next"
        override val displayName = "Next"
    }

    data object Direct : DomainRune() {
        override val name = "direct"
        override val displayName = "Direct"
    }

    data object Link : DomainRune() {
        override val name = "link"
        override val displayName = "Link"
    }

    data object Self : DomainRune() {
        override val name = "self"
        override val displayName = "Self"
    }

    // This `returns` the nearest entity. Can stack with other variable runes
    // Sets the `target` to the nearest `entity`
    data object Nearby : DomainRune() {
        override val name = "nearby"
        override val displayName = "Nearby"
    }

    // Looks for a new entity/location that is NOT the same
    data object Differ : DomainRune() {
        override val name = "differ"
        override val displayName = "Differ"
    }

    // Looks for a new entity/location that is NOT the same
    data object Omni : DomainRune() {
        override val name = "omni"
        override val displayName = "Omni"
    }

    // Switches the `cast` and `target` locations/entities in the context
    data object Swap : DomainRune() {
        override val name = "swap"
        override val displayName = "Swap"
    }

}