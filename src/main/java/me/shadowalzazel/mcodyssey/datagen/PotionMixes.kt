package me.shadowalzazel.mcodyssey.datagen

import io.papermc.paper.potion.PotionMix
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.common.listeners.GlyphListeners.hasTag
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

object PotionMixes {

    fun getMixes(): List<PotionMix> {
        return listOf(
            potionVialMix(),
            potionExtendedPlusMix(),
            potionUpgradedPlusMix(),
            potionAuraMix(),
            potionBlastMix()
        )
    }

    private fun potionVialMix(): PotionMix {
        val inputPredicate = PotionMix.createPredicateChoice {
            !it.hasTag(ItemDataTags.IS_POTION_VIAL) && it.type == Material.POTION
        }
        val result = Item.POTION_VIAL.newItemStack(1)
        val ingredient = RecipeChoice.MaterialChoice(
            Material.PRISMARINE_CRYSTALS
        )
        return PotionMix(
            NamespacedKey(Odyssey.instance, "vial_brewing"),
            result,
            inputPredicate,
            ingredient
        )
    }

    private fun potionUpgradedPlusMix(): PotionMix {
        val inputPredicate = PotionMix.createPredicateChoice {
            !it.hasTag(ItemDataTags.IS_UPGRADED_PLUS) && it.type == Material.POTION
        }
        val result = ItemStack(Material.POTION)
        val ingredient = RecipeChoice.MaterialChoice(Material.GLOW_BERRIES)
        return PotionMix(
            NamespacedKey(Odyssey.instance, "upgraded_plus_brewing"),
            result,
            inputPredicate,
            ingredient
        )
    }

    private fun potionExtendedPlusMix(): PotionMix {
        val inputPredicate = PotionMix.createPredicateChoice {
            !it.hasTag(ItemDataTags.IS_EXTENDED_PLUS) && it.type == Material.POTION
        }
        val result = ItemStack(Material.POTION)
        val ingredient = RecipeChoice.MaterialChoice(Material.HONEY_BOTTLE)
        return PotionMix(
            NamespacedKey(Odyssey.instance, "extended_plus_brewing"),
            result,
            inputPredicate,
            ingredient
        )
    }

    private fun potionAuraMix(): PotionMix {
        val inputPredicate = PotionMix.createPredicateChoice {
            !it.hasTag(ItemDataTags.IS_AURA_POTION) && it.type == Material.POTION
        }
        val result = ItemStack(Material.POTION)
        val ingredient = RecipeChoice.MaterialChoice(Material.EXPERIENCE_BOTTLE)
        return PotionMix(
            NamespacedKey(Odyssey.instance, "aura_potion_brewing"),
            result,
            inputPredicate,
            ingredient
        )
    }

    private fun potionBlastMix(): PotionMix {
        val inputPredicate = PotionMix.createPredicateChoice {
            !it.hasTag(ItemDataTags.IS_BLAST_POTION) && it.type == Material.SPLASH_POTION
        }
        val result = ItemStack(Material.POTION)
        val ingredient = RecipeChoice.MaterialChoice(Material.FIRE_CHARGE)
        return PotionMix(
            NamespacedKey(Odyssey.instance, "blast_potion_brewing"),
            result,
            inputPredicate,
            ingredient
        )
    }


}