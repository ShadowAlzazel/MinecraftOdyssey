package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext


sealed class DomainRune: ArcaneRune() {
    // Domain runes change the casting context of the spell.
    // Changes like the location or entity where it originates or targets

    fun change(original: CastingContext, current: CastingContext) {
        when (this) {
            is Origin -> {
                current.castingLocation = original.castingLocation
            }
            is Target -> {
                if (current.targetLocation != null) {
                    current.castingLocation = current.targetLocation!!
                }
            }
            is Nearby -> {
                val nearby = current.castingLocation.getNearbyLivingEntities(16.0)
                if (nearby.isEmpty()) return
                val nearest = nearby.sortedBy { it.location.distance(current.castingLocation) }
                current.target = nearest.first()
                current.targetLocation = current.target!!.location
            }
            is Invert -> {
                if (current.targetLocation == null) return
                val temp = current.castingLocation
                current.castingLocation = current.targetLocation!!
                current.targetLocation = temp
            }
            else -> {}
        }
    }

    // Sets the cast location back to the ORIGINAL cast
    data object Origin : DomainRune() {
        override val name = "origin"
        override val displayName = "Origin"
    }

    // This changes the castingLocation to the CURRENT targetLocation
    data object Target : DomainRune() {
        override val name = "target"
        override val displayName = "Target"
    }

    data object Link : DomainRune() {
        override val name = "link"
        override val displayName = "link"
    }

    // This `returns` the nearest entity. Can stack with other variable runes
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