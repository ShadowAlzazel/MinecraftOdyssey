package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.listeners.PetListener.getStringTag
import me.shadowalzazel.mcodyssey.common.listeners.SmithingListeners.getItemNameId
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Particle
import org.bukkit.damage.DamageType
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class ArcaneSpellBuilder(
    val itemSource: ItemStack,
    val additionalItems: List<ItemStack>
) {

    private var arcaneSource: ArcaneSource? = null
    private var isBuildable: Boolean = false
    val runeSequence: MutableList<ArcaneRune> = mutableListOf()

    /**
     * Basic function to detect if a spell can even be built
     */
    fun canBuildSpell(): Boolean {
        val possibleSource = getSourceFromItem(itemSource)
        if (possibleSource != null) {
            arcaneSource = possibleSource
        } else {
            return false
        }

        isBuildable = true
        return true
    }



    /**
     * This starts the build process for the spell
     */
    fun formSpell(context: CastingContext): ArcaneSpell {
        createRuneSequence()

        // If pass
        //arcaneSource ?: return
        println("Making Spell with this sequence: \n")
        var index = 0
        for (r in runeSequence) {
            println("Rune [${index}] [${r.name}] $r \n")
            index += 1
        }

        val spell = ArcaneSpell(arcaneSource!!, context, runeSequence)
        return spell
    }


    fun insertSequence(runeList: List<ArcaneRune>) {
        for (r in runeList) {
            runeSequence.add(r)
        }
    }


    // ----------------------------------------------------------
    // These methods and functions are for BUILDING the spell

    /**
     * This gets an Arcane source from an item
     */
    fun getSourceFromItem(item: ItemStack): ArcaneSource? {
        return when (item.getItemNameId()) {
            // GEM-SOURCES
            "ruby" -> ArcaneSource.Fire
            "neptunian" -> ArcaneSource.Frost
            "amethyst_shard" -> ArcaneSource.Magic
            "jovianite" -> ArcaneSource.Radiant
            "ender_eye" -> ArcaneSource.Void
            "soul_quartz" -> ArcaneSource.Soul
            // TOOL-SOURCES
            "arcane_blade" -> ArcaneSource.Magic
            "arcane_book" -> ArcaneSource.Magic
            "arcane_wand" -> ArcaneSource.Magic
            "arcane_pen" -> ArcaneSource.Magic
            "arcane_scepter" -> ArcaneSource.Magic
            "arcane_orb" -> ArcaneSource.Magic
            else -> null
        }

    }


    /**
     * This gets an arcane runes from an item
     */
    fun getRuneFromItem(item: ItemStack): ArcaneRune? {
        val runeName = item.getStringTag(ItemDataTags.STORED_ARCANE_RUNE)
        val readRune = when (runeName) {
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
        // -------------------------------------
        // TODO: Temporary item_name reader for testing
        if (readRune == null) {
            val directRune = when(item.getItemNameId()) {
                "clock" -> ModifierRune.Delay(2.0)
                "heart_of_the_sea" -> DomainRune.Target
                "ender_eye" -> DomainRune.Nearby
                "nether_star" -> DomainRune.Origin
                "popped_chorus_fruit" -> DomainRune.Invert
                "diamond" -> ModifierRune.Amplify(4.0)
                "ruby" -> ModifierRune.Source(DamageType.IN_FIRE, Particle.FLAME)
                "emerald" -> ModifierRune.Wide(2.0)
                "echo_shard" -> ModifierRune.Source(DamageType.SONIC_BOOM, Particle.SONIC_BOOM)
                "alexandrite" -> ManifestationRune.Beam()
                "snowball" -> ManifestationRune.Zone()
                "neptunian" -> ModifierRune.Source(DamageType.FREEZE, Particle.SNOWFLAKE)
                "kunzite" -> ModifierRune.Convergence(1.0)
                "amethyst_shard" -> ModifierRune.Range(16.0)
                "jovianite" -> ModifierRune.Source(DamageType.MAGIC, Particle.WAX_OFF)
                else -> null
            }
            return directRune
        }
        return readRune
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
     * This is a serial method. This reads runes 1 at a time, and executes in that order.
     */
    fun readRunesSerial(items: List<ItemStack>): MutableList<ArcaneRune> {
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
     * 
     * Returns a Rune Sequence 
     */
    fun decompressUnorderedRunes(runes: MutableList<ArcaneRune>) {
        val runeSequence = mutableListOf<ArcaneRune>()
        var availableCasts = runes.count { it is ManifestationRune }
        var availableModifiers = runes.count { it is ModifierRune }
        var availableKernels = runes.count { it is DomainRune }
        var availableVars = runes.count { it is VariableRune }

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


    fun createRuneSequence() {
        // TODO: Temporary Logic
        // IF pen or scroll
        // readRunesUnordered -> decompress Unordered Runes

        val runesRead = readRunesSerial(additionalItems)
        if (runesRead.isEmpty()) return

        // Go from left to right, and insert into sequence
        for (r in runesRead) {
            // ADD algorithm checks later
            runeSequence.add(r)
        }

    }



}