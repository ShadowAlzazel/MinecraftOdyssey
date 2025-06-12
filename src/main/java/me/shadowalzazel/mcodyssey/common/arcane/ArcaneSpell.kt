package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneSource
import me.shadowalzazel.mcodyssey.common.arcane.runes.ManifestationRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import me.shadowalzazel.mcodyssey.common.listeners.PetListener.getStringTag
import me.shadowalzazel.mcodyssey.common.listeners.SmithingListeners.getItemNameId
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.inventory.ItemStack

class ArcaneSpell {


    /**
     * This gets an Arcane source from an item
     */
    fun getSourceFromItem(item: ItemStack): ArcaneSource? {
        return when (item.getItemNameId()) {
            "ruby" -> ArcaneSource.Fire
            "neptunian" -> ArcaneSource.Fire
            "amethyst" -> ArcaneSource.Magic
            "jovianite" -> ArcaneSource.Radiant
            "eye_of_ender" -> ArcaneSource.Void
            "soul_quartz" -> ArcaneSource.Soul
            else -> null
        }

    }


    /**
     * This gets an arcane runes from an item
     */
    fun getRuneFromItem(item: ItemStack): ArcaneRune? {
        val runeName = item.getStringTag(ItemDataTags.STORED_ARCANE_RUNE)
        return when (runeName) {
            "wide" -> ModifierRune.Wide(2.0)
            "amplify" -> ModifierRune.Amplify(4.0)
            "convergence" -> ModifierRune.Convergence(0.5)
            "range" -> ModifierRune.Range(16.0)
            "delay" -> ModifierRune.Delay(2.0)
            "speed" -> ModifierRune.Speed(1.0)
            "beam" -> ManifestationRune.Beam()
            "slice" -> ManifestationRune.Slice()
            "zone" -> ManifestationRune.Zone()
            "projectile" -> ManifestationRune.Projectile()
            "aura" -> ManifestationRune.Aura()
            else -> null
        }
    }


    /**
     *  This reads runes, but does not care for the order.
     *  This does NOT preserve the original sequence.
     */
    fun readRunesUnordered(items: List<ItemStack>): MutableList<ArcaneRune> {
        val runes = mutableListOf<ArcaneRune>()
        val addRunes: (ArcaneRune, Int) -> Unit = { item, count ->
            repeat(count) {
                runes.add(item)
            }
        }
        for (item in items) {
            val itemRune = getRuneFromItem(item) ?: continue
            addRunes(itemRune, items.size)
        }
        return runes
    }


    /**
     * This is a sequential method. This reads runes 1 at a time, and executes in that order.
     */
    fun readRunesSequential(items: List<ItemStack>): MutableList<ArcaneRune> {
        val runes = mutableListOf<ArcaneRune>()
        for (item in items) {
            val itemRune = getRuneFromItem(item) ?: continue
            runes.add(itemRune)
        }
        return runes
    }


    /**
     * Since bundles destroy the sequence. There are a set of rules for rune writing/reading anyways.
     * The algorithm follows this sequence and tries to reconstruct the original sequence.
     * An ALTERNATIVE way is to just rename the runes themselves.
     */
    fun decompressRuneBundle(runes: MutableList<ArcaneRune>) {
        val result = mutableListOf<ArcaneRune>()
        var modifiersLeft = runes.count { it is ModifierRune }


    }



}