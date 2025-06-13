package me.shadowalzazel.mcodyssey.common.arcane.util

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneSource
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

data class CastingContext(
    val caster: LivingEntity,
    val world: World,
    var castingLocation: Location,
    var direction: Vector,
    var target: Entity? = null,
    var targetLocation: Location? = null,
    val runes: List<ArcaneRune> = emptyList()
    ) {

    fun clone(): CastingContext {
        return CastingContext(
            this.caster,
            this.world,
            this.castingLocation,
            this.direction,
            this.target,
            this.targetLocation,
            this.runes
        )

    }

}