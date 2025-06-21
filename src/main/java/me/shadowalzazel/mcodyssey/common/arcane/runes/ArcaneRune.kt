package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.RuneDataManager
import me.shadowalzazel.mcodyssey.common.arcane.runes.DomainRune.Kernel.getItemNameId
import me.shadowalzazel.mcodyssey.common.arcane.runes.DomainRune.Kernel.getStringTag
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Particle
import org.bukkit.damage.DamageType
import org.bukkit.inventory.ItemStack

// Base sealed class for all runes
@Suppress("UnstableApiUsage")
sealed class ArcaneRune : RuneDataManager {
    abstract val name: String
    abstract val displayName: String

    companion object {
        fun fromNameID(name: String, value: Double? = null): ArcaneRune? = when (name) {
            // Casting (The form of the spell, a ray or a zone)
            "beam" -> CastingRune.Beam()
            "zone" -> CastingRune.Zone()
            "ball" -> CastingRune.Ball()
            "point" -> CastingRune.Point()
            // Domain (change the casting context like location or target)
            "next" -> DomainRune.Next
            "nearby" -> DomainRune.Nearby
            "origin" -> DomainRune.Origin
            "kernel" -> DomainRune.Kernel
            "swap" -> DomainRune.Swap
            "differ" -> DomainRune.Differ
            "direct" -> DomainRune.Direct
            "self" -> DomainRune.Self
            // "UP" -> targets 1 block up?
            // Augment (effects like breaking blocks or TP)
            "break" -> AugmentRune.Break(value ?: 2.0)
            "coda" -> AugmentRune.Coda
            "pick_up" -> AugmentRune.PickUp
            "teleport" -> AugmentRune.Teleport
            "heal" -> AugmentRune.Heal(value ?: 4.0)
            // "levitation" -> If target is a block, move 1 up! if target is entity -> levitate?
            // Modifier (stat modifiers for other runes)
            "amplify" -> ModifierRune.Amplify(4.0)
            "wide" -> ModifierRune.Wide(value ?: 1.0)
            "delay" -> ModifierRune.Delay(2.0)
            "convergence" -> ModifierRune.Convergence(1.0)
            "range" -> ModifierRune.Range(16.0)
            else -> null
        }

        fun fromRawItem(item: ItemStack): ArcaneRune? = when (item.getItemNameId()) {
            // Casting
            "alexandrite" -> CastingRune.Beam()
            "snowball" -> CastingRune.Zone()
            "arrow" -> CastingRune.Ball()
            "iron_nugget" -> CastingRune.Point()
            // Domain
            "heart_of_the_sea" -> DomainRune.Next
            "ender_eye" -> DomainRune.Nearby
            "nether_star" -> DomainRune.Origin
            "oak_sapling" -> DomainRune.Kernel
            "popped_chorus_fruit" -> DomainRune.Swap
            "coal" -> DomainRune.Differ
            "stick" -> DomainRune.Direct
            "paper" -> DomainRune.Self
            // Augment
            "cactus" -> AugmentRune.Break(2.0)
            "gold_ingot" -> AugmentRune.Coda
            "iron_ingot" -> AugmentRune.PickUp
            "ender_pearl" -> AugmentRune.Teleport
            "honeycomb" -> AugmentRune.Heal(4.0)
            // Modifier
            "diamond" -> ModifierRune.Amplify(4.0)
            "emerald" -> ModifierRune.Wide(1.0)
            "clock" -> ModifierRune.Delay(2.0)
            "kunzite" -> ModifierRune.Convergence(1.0)
            "amethyst_shard" -> ModifierRune.Range(16.0)
            // Modifier Special
            "ruby" -> ModifierRune.Source(DamageType.IN_FIRE, Particle.FLAME)
            "echo_shard" -> ModifierRune.Source(DamageType.SONIC_BOOM, Particle.SONIC_BOOM)
            "neptunian" -> ModifierRune.Source(DamageType.FREEZE, Particle.SNOWFLAKE)
            "jovianite" -> ModifierRune.Source(DamageType.MAGIC, Particle.WAX_OFF)
            else -> null
        }

        /**
         * This gets an arcane runes from an item
         */
        fun getRuneFromItem(item: ItemStack): ArcaneRune? {
            val runeName = item.getStringTag(ItemDataTags.STORED_ARCANE_RUNE)
            val readRune = fromNameID(runeName ?: "none")
            // -------------------------------------
            if (readRune == null) {
                val directRune = fromRawItem(item)
                return directRune
            }
            return readRune
        }


    }


}

// Notes:
/*
    For magic circle: Runes are read in a CLOCK-WISE ORDER!

 */


