package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import org.bukkit.entity.LivingEntity


sealed class DomainRune: ArcaneRune() {
    // Domain runes change the casting context of the spell.
    // Changes like the location or entity where it originates or targets

    fun change(original: CastingContext, context: CastingContext) {

        val changing = context.clone()
        var successful = true

        // Want to change context
        when (this) {
            is Origin -> {
                changing.castingLocation = original.castingLocation
            }
            is Next -> {
                val target = changing.target
                if (target != null) {
                    // Move to eye height
                    changing.castingLocation = if (target is LivingEntity) {
                        target.eyeLocation
                    } else {
                        target.location.clone().add(0.0, 0.5, 0.0)
                    }
                }
                else if (changing.targetLocation != null) {
                    changing.castingLocation = changing.targetLocation!!
                }
                else {
                    successful = false
                }
            }
            is Nearby -> {
                // Get Nearby entities if not target or ignored
                val nearby = changing.castingLocation.getNearbyLivingEntities(4.0)
                nearby.remove(changing.target) // Remove self
                nearby.removeAll { it in changing.ignoredTargets } // Remove if in the `ignore` list
                // Continue without errors
                if (nearby.isNotEmpty()) {  // Ignore empty list
                    // Sort list to nearest
                    val sortedNearby = nearby.sortedBy { it.location.distance(changing.castingLocation) }
                    val nearestTarget = sortedNearby.first()
                    // Set Target, Location and Direction
                    changing.target = nearestTarget
                    changing.targetLocation = nearestTarget.eyeLocation
                    changing.direction = nearestTarget.eyeLocation.clone().subtract(changing.castingLocation).toVector()
                }
                else {
                    successful = false
                }
            }
            is Invert -> {
                // Swap domains
                if (context.targetLocation != null) {
                    val temp = changing.castingLocation
                    changing.castingLocation = changing.targetLocation!!
                    changing.targetLocation = temp
                }
                else {
                    successful = false
                }
            }
            is Differ -> {
                if (context.target != null) changing.ignoredTargets.add(changing.target!!)
                else successful = false
            }
            else -> successful = false
        }
        // Apply changes to the context
        if (successful) {
            context.apply {
                castingLocation = changing.castingLocation
                direction = changing.direction
                target = changing.target
                targetLocation = changing.targetLocation
                // ignore targets
                for (e in changing.ignoredTargets) {
                    if (e !in ignoredTargets) ignoredTargets.add(e)
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
        override val name = "target"
        override val displayName = "Target"
    }

    data object Link : DomainRune() {
        override val name = "link"
        override val displayName = "link"
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