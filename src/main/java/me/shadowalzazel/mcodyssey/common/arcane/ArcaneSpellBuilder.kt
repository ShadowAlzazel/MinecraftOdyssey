package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import org.bukkit.inventory.ItemStack

/**
 * The init method handles the building of the runeSequence
 */
@Suppress("UnstableApiUsage")
class ArcaneSpellBuilder(
    arcaneItem: ItemStack,
    additionalItems: List<ItemStack> = listOf(),
    providedRunes: List<ArcaneRune>? = null,
    providedSource: ArcaneSource? = null
) : RuneDataManager {

    private var arcaneSource: ArcaneSource? = null
    private var isBuildable: Boolean = true
    private val runeSequence: MutableList<ArcaneRune> = mutableListOf()

    // ------------ INIT METHODS -------------------

    init {
        // ----------- SOURCE -----------
        // check Arcane Item and see if it has a source
        val itemSource = ArcaneSource.getSourceFromRawItem(arcaneItem)
        if (providedSource != null) {
            arcaneSource = providedSource
        }
        else if (itemSource != null) {
            arcaneSource = itemSource
        }
        else {
            isBuildable = false
        }
        // Check Name
        val itemName = arcaneItem.getItemNameId()
        // ----------- INSCRIBED OR ITEM RUNES -----------
        if (itemName == "spell_scroll") {
            // Scroll read inscribed
            val inscribedRunes = readInscribedRunes(arcaneItem)
            if (inscribedRunes == null) {
                isBuildable = false
            }
            // For ARCANE TOOLS
            else {
                insertSequence(inscribedRunes)
            }
        }
        else { // ADDITIONAL ITEMS IS USUALLY PEN
            readRunesFromItemList(additionalItems)
        }
        // ----------- PROVIDED RUNES -----------
        if (providedRunes != null) {
            insertSequence(providedRunes)
        }
    }

    /**
     * Create the rune sequence by READING from additional items
     */
    private fun readRunesFromItemList(items: List<ItemStack>) {
        val runeList = mutableListOf<ArcaneRune>()
        // TODO: Method HERE! To deconstruct a PACKED bundle of items
        val unPackedItems = items

        // End
        for (item in unPackedItems) {
            val itemRune = ArcaneRune.getRuneFromItem(item) ?: continue
            runeList.add(itemRune)
        }
        if (runeList.isEmpty()) {
            return
        }
        // Go from left to right, and insert into sequence
        insertSequence(runeList)
    }


    // ------------ GLOBAL METHODS -------------------

    /**
     * Basic function to detect if a spell can even be built
     */
    fun canBuildSpell(): Boolean {
        return isBuildable
    }

    fun getRuneSequence(): List<ArcaneRune> {
        return runeSequence
    }

    fun insertSequence(runeList: List<ArcaneRune>) {
        for (r in runeList) {
            runeSequence.add(r)
        }
    }

    /**
     * This starts the build process for the spell
     */
    fun buildSpell(context: CastingContext): ArcaneSpell {
        // If pass
        //arcaneSource ?: return
        println("Making Spell with this sequence:")
        var index = 0
        for (r in runeSequence) {
            println("Rune [${index}] [${r.name}] $r")
            index += 1
        }
        println("")

        val spell = ArcaneSpell(arcaneSource!!, context, runeSequence)
        return spell
    }

    // ----------------------------------------------------------
    // These methods and functions are for BUILDING the spell


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
            val itemRune = ArcaneRune.getRuneFromItem(item) ?: continue
            addRunes(itemRune, items.size)
        }
        return runes
    }


    /**
     * Since bundles destroy the sequence. There are a set of rules for rune writing/reading anyways.
     * The algorithm follows this sequence and tries to reconstruct the original sequence.
     * An ALTERNATIVE way is to just rename the runes themselves.
     * 
     * Returns a Rune Sequence 
     */
    fun decompressUnorderedRunes(runes: MutableList<ArcaneRune>) {
        val runeSequence = mutableListOf<ArcaneRune>()
        var availableCasts = runes.count { it is CastingRune }
        var availableModifiers = runes.count { it is ModifierRune }
        var availableKernels = runes.count { it is DomainRune }
        var availableVars = runes.count { it is AugmentRune }

        val modifiersPerCast = availableModifiers / availableCasts
        val overflowModifiers = availableModifiers % availableCasts

        // Start the algorithm
        var listPointer = 0 // Where the list pointer is
        var runesRead = 0 // Amount of runes read
        val runeCount = runes.count()

        for (i in 0 until  availableCasts) {
            // 1. Start with a cast
 

        }
    }

}