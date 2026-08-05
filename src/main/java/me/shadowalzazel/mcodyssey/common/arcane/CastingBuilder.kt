package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import org.bukkit.Particle
import org.bukkit.damage.DamageType

/**
 * The mutable "spec sheet" a casting rune reads from when it manifests.
 *
 * The whole modifier vocabulary is intentionally SMALL and generic (see [applyModifier]).
 * Each casting rune decides what a field like `spread` or `range` means for it, so a
 * player never has to learn a rune-per-parameter. Values are signed, so Spread(-1.0)
 * narrows, Range(-4.0) shortens, etc. — one rune covers both directions.
 */
data class CastingBuilder(
    // --- Core ---
    var damage: Double = 0.0,                       // Amplify
    var damageType: DamageType = DamageType.MAGIC,  // set from the spell's source
    var particle: Particle = Particle.WITCH,        // set from the spell's source
    // --- Shape / reach (each casting rune interprets these) ---
    var range: Double = 0.0,                        // Range      -> length / distance / lifetime
    var spread: Double = 0.0,                       // Spread       -> beam width / cone angle / zone radius / ball size
    var speed: Double = 0.5,                        // Speed      -> projectile speed
    var aimAssist: Double = 0.0,                    // Convergence-> accuracy / snap
    var delayInTicks: Long = 0L,                    // Delay
    // --- Toggles ---
    var gravity: Boolean = false,                   // Gravity    -> projectiles fall
    var invert: Boolean = false,                    // Invert     -> flip a rune's shape (e.g. cone apex)
    // --- Runes waiting to be folded in ---
    val storedRunes: MutableList<ArcaneRune> = mutableListOf()
) {

    /** Queue a rune to be applied later. Modifier order does not matter, so queuing is safe. */
    fun storeRune(rune: ArcaneRune) {
        storedRunes.add(rune)
    }

    /**
     * The entire modifier grammar. Keep this list short; expressiveness comes from
     * combining a few generic modifiers with many casting runes, not from many modifiers.
     */
    fun applyModifier(rune: ModifierRune) {
        when (rune) {
            is ModifierRune.Amplify     -> damage       += rune.value
            is ModifierRune.Range       -> range        += rune.value
            is ModifierRune.Spread      -> spread       += rune.value
            is ModifierRune.Speed       -> speed        += rune.value
            is ModifierRune.Convergence -> aimAssist    += rune.value
            is ModifierRune.Delay       -> delayInTicks += (rune.value * 20).toLong()
            is ModifierRune.Gravity     -> gravity = true
            is ModifierRune.Invert      -> invert = true
            // Legacy: the element used to be a rune. It is now a separate spell tag,
            // but we still honour a Source rune if one shows up in an old sequence.
            is ModifierRune.Source      -> { damageType = rune.damageType; particle = rune.particle }
            else -> {}
        }
    }

    /** Fold every queued rune into the fields above, then clear the queue. */
    fun buildStored() {
        if (storedRunes.isEmpty()) return
        for (r in storedRunes) if (r is ModifierRune) applyModifier(r)
        storedRunes.clear()
    }
}