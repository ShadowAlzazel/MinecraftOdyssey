package me.shadowalzazel.mcodyssey.common.arcane.util

import org.bukkit.Particle
import org.bukkit.damage.DamageType

@Suppress("UnstableApiUsage")
data class ManifestBuilder(
    var damage: Double = 0.0,
    var damageType: DamageType = DamageType.MAGIC,
    var radius: Double = 1.0,
    var range: Double = 1.0,
    var aimAssist: Double = 0.3,
    var delayInTicks: Long = 0L,
    var particle: Particle = Particle.WITCH
)