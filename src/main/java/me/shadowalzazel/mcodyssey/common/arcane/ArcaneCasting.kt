package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import org.bukkit.inventory.ItemStack

/**
 * The single place spells are launched — from an item's stored sequence, or from an
 * explicit code-defined sequence.
 *
 * Casting is READ-ONLY with respect to the item: it never consumes or edits runes. The
 * pen's dialog is what edits a sequence, and physical runes are only spent when a final
 * spell is crafted/inscribed — both are separate steps from casting.
 */
object ArcaneCasting : RuneDataManager {

    /**
     * Reads the spell stored on [item] and casts it in [context]. Works for scrolls, pens,
     * or any future item carrying an inscribed sequence. Source resolution order:
     * the sequence's own stored source, then the item's innate source, then [fallbackSource].
     *
     * @return false if the item carries no valid spell.
     */
    fun castFromItem(
        item: ItemStack,
        context: CastingContext,
        fallbackSource: ArcaneSource = ArcaneSource.Magic
    ): Boolean {
        val (storedSource, runes) = readSpell(item) ?: return false
        val source = storedSource ?: ArcaneSource.getSourceFromRawItem(item) ?: fallbackSource
        return launch(source, context, runes)
    }

    /**
     * Casts an explicit, code-defined sequence. Used by built-in items whose spell is fixed
     * (e.g. the default wand/scepter) until those migrate to stored sequences.
     */
    fun castSequence(source: ArcaneSource, context: CastingContext, runes: List<ArcaneRune>): Boolean =
        launch(source, context, runes)

    private fun launch(source: ArcaneSource, context: CastingContext, runes: List<ArcaneRune>): Boolean {
        if (runes.isEmpty()) return false
        ArcaneSpell(source, context, runes).castSpell()
        return true
    }
}