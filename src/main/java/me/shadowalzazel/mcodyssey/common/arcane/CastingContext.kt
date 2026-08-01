package me.shadowalzazel.mcodyssey.common.arcane

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

data class CastingContext(
    val caster: ArcaneCaster,
    val world: World,
    var castingLocation: Location,
    var direction: Vector,
    var target: ArcaneTarget? = null,
    var targetLocation: Location? = null,
    val ignoredTargets: MutableList<ArcaneTarget> = mutableListOf(),
    // Set by the engine only while a reactive form is manifesting. Each entity the form
    // damages is handed here so the remaining runes can fork onto it. Deliberately NOT part
    // of the data-class contract and NOT copied by clone() — a sub-spell installs its own.
    var chainSpawner: ((ArcaneTarget) -> Unit)? = null
    ) {

    fun clone(): CastingContext {
        return CastingContext(
            this.caster,
            this.world,
            this.castingLocation,
            this.direction,
            this.target,
            this.targetLocation,
            this.ignoredTargets
        )
    }

}