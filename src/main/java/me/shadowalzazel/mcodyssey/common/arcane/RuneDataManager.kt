package me.shadowalzazel.mcodyssey.common.arcane

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface RuneDataManager : DataTagManager {

    /**
     * Combines a list of strings into a single dot-separated string.
     *
     * Example:
     *   combineRunes(listOf("range", "amplify", "beam")) -> "range.amplify.beam"
     */
    private fun combineRunes(runes: List<String>): String =
        runes.joinToString(separator = ".")


    /**
     * Parses a dot-separated string back into a list of strings.
     * Uses a regular expression to split on literal “.” characters.
     *
     * Example:
     *   parseRunes("runeA.runeB.runeC") -> ["range", "amplify", "beam"]
     */
    private fun parseRunes(combined: String): List<String> {
        // Regex("\\.") splits on “.”; the backslash is needed because '.' is special in regex.
        // Filtering out any empty segments in case of leading/trailing dots or consecutive dots.
        return combined
            .split(Regex("\\."))
            .filter { it.isNotEmpty() }
    }

    /**
     * Reads the inscribed runes of an item.
     * Inscribed runes are stored as `custom_data`
     *
     */
    fun readInscribedRunes(item: ItemStack): List<ArcaneRune>? {
        val combinedRunes = item.getStringTag(ItemDataTags.INSCRIBED_RUNES) ?: return null

        // Use parser to deconstruct rune list
        val listRunesNames = parseRunes(combinedRunes)

        // Now create list of Arcane Rune
        val runes = mutableListOf<ArcaneRune>()
        for (runeName in listRunesNames) {
            val rune = ArcaneRune.fromNameID(runeName) ?: continue
            runes.add(rune)
        }

        return runes
    }


    /**
     * Stores the runes onto an item in the form of `custom_data`
     */
    fun storeInscribedRunes(item: ItemStack, runes: List<ArcaneRune>) {
        // Get names from runes list
        val runesNames = mutableListOf<String>()
        for (r in runes) {
            runesNames.add(r.name)
        }
        // Combine
        val combinedRunes = combineRunes(runesNames)
        // Store
        item.setStringTag(ItemDataTags.INSCRIBED_RUNES, combinedRunes)
    }


    fun readBundledRunes(item: ItemStack): List<ArcaneRune>? {
        // Temp
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

        // Return null if empty
        return if (runes.isEmpty()) null else runes
    }

}