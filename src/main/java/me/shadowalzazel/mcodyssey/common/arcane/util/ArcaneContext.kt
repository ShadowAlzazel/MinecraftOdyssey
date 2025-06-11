package me.shadowalzazel.mcodyssey.common.arcane.util

import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

data class ArcaneContext(
    val caster: LivingEntity,
    val target: Entity?,
    val targetLocation: Location,
    val world: World,
    val modifiers: List<ModifierRune> = emptyList()
    )