package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners.MiscListeners.getItemNameId
import org.bukkit.Particle
import org.bukkit.damage.DamageType
import org.bukkit.inventory.ItemStack

// Base sealed class for all runes
@Suppress("UnstableApiUsage")
sealed class ArcaneRune {
    abstract val name: String
    abstract val displayName: String

    companion object {
        fun fromName(name: String, value: Double? = null): ArcaneRune? = when (name) {
            // Casting
            "beam" -> CastingRune.Beam()
            "zone" -> CastingRune.Zone()
            "ball" -> CastingRune.Ball()
            "point" -> CastingRune.Point()
            // Domain
            "next" -> DomainRune.Next
            "nearby" -> DomainRune.Nearby
            "origin" -> DomainRune.Origin
            "kernel" -> DomainRune.Kernel
            "invert" -> DomainRune.Invert
            "differ" -> DomainRune.Differ
            "trace" -> DomainRune.Trace
            "self" -> DomainRune.Self
            // Augment
            "break" -> AugmentRune.Break(value ?: 2.0)
            "coda" -> AugmentRune.Coda
            "pick_up" -> AugmentRune.PickUp
            "teleport" -> AugmentRune.Teleport
            "heal" -> AugmentRune.Heal(value ?: 4.0)
            // Modifier
            "amplify" -> ModifierRune.Amplify(4.0)
            "wide" -> ModifierRune.Wide(value ?: 1.0)
            "delay" -> ModifierRune.Delay(2.0)
            "convergence" -> ModifierRune.Convergence(1.0)
            "range" -> ModifierRune.Range(16.0)
            else -> null
        }

        fun fromItem(item: ItemStack): ArcaneRune? = when (item.getItemNameId()) {
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
            "popped_chorus_fruit" -> DomainRune.Invert
            "coal" -> DomainRune.Differ
            "stick" -> DomainRune.Trace
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

    }


}

// Notes:
/*
    For magic circle: Runes are read in a CLOCK-WISE ORDER!

 */


