package me.shadowalzazel.mcodyssey.common.arcane.runes

import org.bukkit.Particle
import org.bukkit.damage.DamageType

@Suppress("UnstableApiUsage")
sealed class ModifierRune : ArcaneRune() {
    abstract val value: Double

    class Wide(value: Double?) : ModifierRune() {
        override val name = "wide"
        override val displayName = "Wide"
        override val value = value ?: 0.0
    }

    class Speed(value: Double?) : ModifierRune() {
        override val name = "speed"
        override val displayName = "speed"
        override val value = value ?: 0.0
    }

    // THIS IS A VARIABLE RUNE
    // Requires a TARGET
    class Vulnerability(value: Double?) : ModifierRune() {
        override val name = "speed"
        override val displayName = "speed"
        override val value = value ?: 0.0
    }

    class Invert(value: Double?) : ModifierRune() {
        override val name = "speed"
        override val displayName = "speed"
        override val value = value ?: 0.0
    }

    // ENVIRONMENT RUNES
    class Light(value: Double?) : ModifierRune() {
        override val name = "speed"
        override val displayName = "speed"
        override val value = value ?: 0.0
    }

    class Source(
        val damageType: DamageType,
        val particle: Particle
    ) : ModifierRune() {
        override val name = "source"
        override val displayName = "source"
        override val value = 0.0
    }

    // Is how potent the manifestation is i.e. DAMAGE
    class Amplify(value: Double?) : ModifierRune() {
        override val name = "amplify"
        override val displayName = "amplify"
        override val value = value ?: 0.0
    }

    // How Precise the manifestation is
    class Convergence(value: Double?) : ModifierRune() {
        override val name = "convergence"
        override val displayName = "Convergence"
        override val value = value ?: 0.0
    }

    class Range(value: Double?) : ModifierRune() {
        override val name = "range"
        override val displayName = "range"
        override val value = value ?: 0.0
    }


}