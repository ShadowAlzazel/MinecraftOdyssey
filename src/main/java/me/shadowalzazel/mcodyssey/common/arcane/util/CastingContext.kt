package me.shadowalzazel.mcodyssey.common.arcane.util

import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

data class CastingContext(
    val caster: LivingEntity,
    val world: World,
    var castingLocation: Location,
    var target: Entity?,
    var targetLocation: Location?,
    val modifiers: List<ModifierRune> = emptyList()
    )