package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

/**
 * The single place spells are launched — from an item's stored sequence, from a serialized
 * spell string, or from an explicit rune list.
 *
 * Casting is READ-ONLY with respect to the item: it never consumes or edits runes. The pen's
 * dialog edits sequences; physical runes are only spent when a final spell is inscribed.
 */
object ArcaneCasting : RuneDataManager {

    /**
     * Reads the spell stored on [item] and casts it. Source resolution order: the sequence's
     * own stored source, then the item's innate source, then [fallbackSource].
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
     * Casts a serialized spell string (the same "@source;name:value;..." language the pen
     * writer produces). Used by built-in items whose spell is code-defined.
     */
    fun castSpellString(
        data: String,
        context: CastingContext,
        fallbackSource: ArcaneSource = ArcaneSource.Magic
    ): Boolean {
        val (storedSource, runes) = deserializeSpell(data)
        return launch(storedSource ?: fallbackSource, context, runes)
    }

    /** Casts an explicit rune list. */
    fun castSequence(
        source: ArcaneSource,
        context: CastingContext,
        runes: List<ArcaneRune>): Boolean =
        launch(source, context, runes)

    /** The standard player-cast context: eye position, look direction, no pre-set target. */
    fun basicContext(caster: LivingEntity): CastingContext {
        val eye = caster.eyeLocation
        return CastingContext(
            caster = ArcaneCaster(entityCaster = caster),
            world = caster.world,
            castingLocation = eye,
            direction = eye.direction
        )
    }

    private fun launch(source: ArcaneSource, context: CastingContext, runes: List<ArcaneRune>): Boolean {
        if (runes.isEmpty()) return false
        ArcaneSpell(source, context, runes).castSpell()
        return true
    }
}