package me.shadowalzazel.mcodyssey.common.arcane

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.AugmentRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.inventory.ItemStack

/**
 * Reads and writes spells as a single serialized string on ANY item.
 *
 * Format:  [ "@" sourceName ";" ] token { ";" token }
 *          token := runeName [ ":" doubleValue ]
 *
 * The separators are ";" and ":" — deliberately NOT "." — because tuned values like
 * "16.0" contain a dot, which the old dot-joined format silently corrupted. Storing the
 * value alongside the name is what lets a tuned Range(24.0) or Amplify(7.5) survive a
 * round-trip; the old format dropped every value and re-hardcoded a default on read.
 */
@Suppress("UnstableApiUsage")
interface RuneDataManager : DataTagManager {

    // --------------------------------------------------------------------------------
    //  Serialization
    // --------------------------------------------------------------------------------

    /** Turn a rune sequence (and optional source) into a storable string. */
    fun serializeSpell(runes: List<ArcaneRune>, source: ArcaneSource? = null): String {
        val body = runes.joinToString(";") { runeToken(it) }
        return if (source != null) "@${source.name};$body" else body
    }

    /** Parse a serialized string back into (source?, runes). Unknown tokens are skipped. */
    fun deserializeSpell(data: String): Pair<ArcaneSource?, List<ArcaneRune>> {
        var body = data
        var source: ArcaneSource? = null
        if (body.startsWith("@")) {
            val cut = body.indexOf(';')
            val srcName = if (cut >= 0) body.substring(1, cut) else body.substring(1)
            source = arcaneSourceByName(srcName)
            body = if (cut >= 0) body.substring(cut + 1) else ""
        }
        val runes = body.split(";")
            .filter { it.isNotEmpty() }
            .mapNotNull { token ->
                val i = token.indexOf(':')
                val runeName = if (i >= 0) token.substring(0, i) else token
                val arg = if (i >= 0) token.substring(i + 1).toDoubleOrNull() else null
                ArcaneRune.fromNameID(runeName, arg)
            }
        return source to runes
    }

    // Encode a single rune, carrying its tunable value where it has one.
    private fun runeToken(r: ArcaneRune): String = when (r) {
        is ModifierRune.Source -> r.name                 // legacy: element is a separate tag now
        is ModifierRune        -> "${r.name}:${r.value}"
        is AugmentRune.Heal    -> "${r.name}:${r.value}"
        is AugmentRune.Break   -> "${r.name}:${r.value}"
        else                   -> r.name
    }

    private fun arcaneSourceByName(name: String): ArcaneSource? = when (name) {
        "fire"    -> ArcaneSource.Fire
        "frost"   -> ArcaneSource.Frost
        "magic"   -> ArcaneSource.Magic
        "void"    -> ArcaneSource.Void
        "radiant" -> ArcaneSource.Radiant
        "soul"    -> ArcaneSource.Soul
        "aero"    -> ArcaneSource.Aero
        else      -> null
    }

    // --------------------------------------------------------------------------------
    //  Item read/write — any item carrying INSCRIBED_RUNES is a castable spell
    // --------------------------------------------------------------------------------

    /** Store a full spell (sequence + source) onto an item. */
    fun storeSpell(item: ItemStack, runes: List<ArcaneRune>, source: ArcaneSource? = null) {
        item.setStringTag(ItemDataTags.INSCRIBED_RUNES, serializeSpell(runes, source))
    }

    /** Read a full spell from an item, or null if it carries none. */
    fun readSpell(item: ItemStack): Pair<ArcaneSource?, List<ArcaneRune>>? {
        val data = item.getStringTag(ItemDataTags.INSCRIBED_RUNES) ?: return null
        val (source, runes) = deserializeSpell(data)
        return if (runes.isEmpty()) null else source to runes
    }

    /** Back-compat: read only the rune sequence (any stored source is ignored). */
    fun readInscribedRunes(item: ItemStack): List<ArcaneRune>? = readSpell(item)?.second

    /** Back-compat: store only the rune sequence (no source header). */
    fun storeInscribedRunes(item: ItemStack, runes: List<ArcaneRune>) = storeSpell(item, runes, null)

    /**
     * Read the physical rune ITEMS out of a bundle (e.g. the pen's contents).
     * This is the palette the dialog writer offers — it never mutates the bundle.
     */
    fun readBundledRunes(item: ItemStack): List<ArcaneRune>? {
        val bundleContents = item.getData(DataComponentTypes.BUNDLE_CONTENTS) ?: return null
        val runeItems = bundleContents.contents()
        val runes = mutableListOf<ArcaneRune>()
        for (i in runeItems) {
            val runeID = i.getStringTag(ItemDataTags.STORED_ARCANE_RUNE)
            val namedRune = ArcaneRune.fromNameID(runeID ?: "none")
            if (namedRune == null) {
                val directRune = ArcaneRune.fromRawItem(i) ?: continue
                runes.add(directRune)
            } else {
                runes.add(namedRune)
            }
        }
        return if (runes.isEmpty()) null else runes
    }
}