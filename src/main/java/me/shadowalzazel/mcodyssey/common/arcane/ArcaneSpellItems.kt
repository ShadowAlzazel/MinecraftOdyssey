package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.CastingRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.DomainRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Clean entry points for the rune-spell items, replacing the pen/scroll/wand/scepter handlers
 * that used to live in ArcaneEquipmentManager (along with the duplicated checkOffhandRunes and
 * the old stylus). Each one resolves a source, builds a basic caster context, and hands off to
 * [ArcaneCasting] — no ArcaneSpellBuilder, no hardcoded rune lists inline.
 *
 * Wire these from your existing event listener:
 *   - spell scroll used   -> castScroll(player, scroll)
 *   - arcane pen used     -> openPenWriter(player, pen)     (right-click / interact, NOT consume)
 *   - wand used           -> castWand(player)
 *   - scepter used        -> castScepter(player)
 */
object ArcaneSpellItems {

    /** Cast whatever spell is inscribed on the scroll. */
    fun castScroll(caster: Player, scroll: ItemStack): Boolean =
        ArcaneCasting.castFromItem(scroll, ArcaneCasting.basicContext(caster))

    /** Open the writer dialog for the pen. Never casts, never touches the pen's runes. */
    fun openPenWriter(caster: Player, pen: ItemStack) = SpellPenDialog.open(caster, pen)

    private fun castBuiltIn(caster: Player, spell: String, cooldown: Int): Boolean {
        val equipment = caster.equipment ?: return false
        val tool = equipment.itemInMainHand
        if (caster.getCooldown(tool) > 0) return false
        tool.damage(1, caster)
        val source = ArcaneSource.getSourceFromRawItem(equipment.itemInOffHand) ?: ArcaneSource.Magic
        val ok = ArcaneCasting.castSpellString(
            spell,
            ArcaneCasting.basicContext(caster),
            source)
        caster.setCooldown(tool, cooldown)
        return ok
    }


    /** Built-in wand: fires its fixed default spell (as spell-code), element taken from the off-hand. */
    fun castBuiltInWand(caster: Player): Boolean = castBuiltIn(
        caster,
        BuiltInSpells.WAND,
        cooldown = 20)

    /** Built-in scepter: fires its fixed default spell (as functions), element taken from the off-hand. */
    fun castBuiltInScepter(caster: Player): Boolean = ArcaneCasting.castSequence(
        source = ArcaneSource.getSourceFromRawItem(caster.equipment.itemInOffHand) ?: ArcaneSource.Magic,
        context = ArcaneCasting.basicContext(caster),
        runes = listOf<ArcaneRune>(
            ModifierRune.Range(32.0),
            ModifierRune.Amplify(4.0),
            ModifierRune.Convergence(0.1),
            DomainRune.Direct,
            CastingRune.Zone(),
        )
    )

}

/**
 * Built-in, code-defined spells — written in the exact language the pen writer emits, so a
 * designer tweaks them here and they behave identically to a player-authored scroll.
 */
object BuiltInSpells : RuneDataManager {
    const val WAND = "range:16.0;amplify:4.0;convergence:0.35;beam"
    const val SCEPTER = "range:16.0;amplify:3.0;convergence:0.1;direct;zone"

    /** Optionally stamp a built-in onto an item at craft time so it casts via castFromItem. */
    fun inscribeOnto(item: ItemStack, spell: String) {
        item.setStringTag(ItemDataTags.INSCRIBED_RUNES, spell)
    }
}