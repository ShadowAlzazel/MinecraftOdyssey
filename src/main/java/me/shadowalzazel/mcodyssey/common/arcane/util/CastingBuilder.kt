package me.shadowalzazel.mcodyssey.common.arcane.util

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import org.bukkit.Particle
import org.bukkit.damage.DamageType

@Suppress("UnstableApiUsage")
data class CastingBuilder(
    var damage: Double = 0.0,
    var damageType: DamageType = DamageType.MAGIC,
    var radius: Double = 0.0,
    var range: Double = 0.0,
    var aimAssist: Double = 0.0,
    var delayInTicks: Long = 0L,
    var particle: Particle = Particle.WITCH,
    val storedRunes: MutableList<ArcaneRune> = mutableListOf()
) {

    // Adds a rune but DOES not apply its modification
    fun storeRune(rune: ArcaneRune) {
        this.storedRunes.add(rune)
    }

    /**
     * This function is to use with the builder to add other runes.
     * This adds the runes modifiers to the builder.
     */
    fun addRune(rune: ArcaneRune) {
        // Apply modifiers (exhaustive List)
        when(rune) {
            is ModifierRune.Range -> this.range += rune.value
            is ModifierRune.Convergence -> this.aimAssist += rune.value
            is ModifierRune.Amplify -> this.damage += rune.value
            is ModifierRune.Wide -> this.radius += rune.value
            is ModifierRune.Source -> {
                this.damageType = rune.damageType
                this.particle = rune.particle
            }
            is ModifierRune.Delay -> this.delayInTicks += (rune.value * 20).toLong()
            else -> {}
        }
    }

    /**
     * This function uses all stored runes to build class
     */
    fun buildStored() {
        if (storedRunes.isEmpty()) return
        for (r in storedRunes) {
            addRune(r)
        }
        storedRunes.removeAll { true } // Remove all runes
    }



}