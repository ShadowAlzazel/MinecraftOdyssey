package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity

@Suppress("UnstableApiUsage")
sealed class AugmentRune : ArcaneRune() {
    // Variable runes CHANGE how the sequence is READ
    // How the loop/run time behaves

    fun effect(context: CastingContext) {
        when (this) {
            is Coda -> {
                // TODO: Special Case
            }
            is Break -> {
                val block = context.targetLocation?.block
                if (block != null) {
                    // Use the wiki to find the values to break
                    // https://minecraft.wiki/w/Module:Blast_resistance_values
                    block.breakNaturally()
                }
            }
            is PickUp -> {
                val pickUpLocation = context.targetLocation ?: return
                val nearby = pickUpLocation.getNearbyEntities(1.0, 1.0, 1.0)
                if (nearby.isEmpty()) return
                val items = nearby.filter { it is Item }
                if (items.isEmpty()) return
                val itemToPickUp = items.first()
                if (itemToPickUp is Item) {
                    itemToPickUp.teleport(context.castingLocation)
                }
            }
            is Heal -> {
                val target = context.target
                if (target is LivingEntity) {
                    target.heal(this.value)
                }
            }
            is Teleport -> {
                context.target?.teleport(context.castingLocation)
            }
            else -> {}
        }
    }

    // Mimics/Clones the original casting context conditions
    // different from the ORIGIN rune, as that can have the `target` change
    data object Repeat : AugmentRune() {
        override val name = "repeat"
        override val displayName = "Repeat"
    }

    // BEHAVES like a music CODA
    // Goes back to the beginning, IGNORES codas
    data object Coda : AugmentRune() {
        override val name = "coda"
        override val displayName = "coda"
    }

    data object PickUp : AugmentRune() {
        override val name = "pick_up"
        override val displayName = "pick_up"
    }

    class Vulnerability() : AugmentRune() {
        override val name = "vulnerability"
        override val displayName = "vulnerability"
    }

    // ENVIRONMENT RUNES
    class Light()  : AugmentRune() {
        override val name = "light"
        override val displayName = "light"
    }

    class Heal(val value: Double)  : AugmentRune() {
        override val name = "heal"
        override val displayName = "heal"
    }

    class Break(val value: Double = 0.0)  : AugmentRune() {
        override val name = "break"
        override val displayName = "break"
    }

    data object Teleport : AugmentRune() {
        override val name = "teleport"
        override val displayName = "teleport"
    }


    // MAYBE ITEM runes
    // detects if ITEM

    // OR pick up ITEM

}