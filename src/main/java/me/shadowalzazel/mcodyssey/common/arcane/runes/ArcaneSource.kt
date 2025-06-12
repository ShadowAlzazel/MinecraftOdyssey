package me.shadowalzazel.mcodyssey.common.arcane.runes

import org.bukkit.Particle
import org.bukkit.damage.DamageType

@Suppress("UnstableApiUsage")
sealed class ArcaneSource(
    val name: String,
    val damageType: DamageType,
    val particle: Particle,
) {

    // The source of attribute and appearance of the magic being used

    data object Fire: ArcaneSource("fire", DamageType.ON_FIRE, Particle.FLAME)
    data object Frost: ArcaneSource("frost", DamageType.FREEZE, Particle.SNOWFLAKE)
    data object Magic: ArcaneSource("magic", DamageType.MAGIC, Particle.WITCH)
    data object Void: ArcaneSource("void", DamageType.OUT_OF_WORLD, Particle.ENCHANT)
    data object Radiant: ArcaneSource("radiant", DamageType.MAGIC, Particle.WAX_OFF)
    data object Soul: ArcaneSource("soul", DamageType.MAGIC, Particle.SCULK_SOUL)



}