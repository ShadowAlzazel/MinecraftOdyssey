package me.shadowalzazel.mcodyssey.datagen

import io.papermc.paper.potion.PotionMix
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
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
            potionUpgradedPlusMix()
        )
    }

    private fun potionVialMix(): PotionMix {
        // Water
        val potionItem = ItemStack(Material.POTION, 1)
        val potionMeta = potionItem.itemMeta as PotionMeta
        potionMeta.basePotionType = PotionType.WATER
        potionItem.itemMeta = potionMeta
        val result = Item.POTION_VIAL.newItemStack(1)
        val input = RecipeChoice.MaterialChoice(
            Material.POTION
        )
        val ingredient = RecipeChoice.MaterialChoice(
            Material.PRISMARINE_CRYSTALS
        )
        return PotionMix(
            NamespacedKey(Odyssey.instance, "vial_brewing"),
            result,
            input,
            ingredient
        )
    }

    private fun potionUpgradedPlusMix(): PotionMix {
        // Water
        val potionItem = ItemStack(Material.POTION, 1)
        val potionMeta = potionItem.itemMeta as PotionMeta
        potionMeta.basePotionType = PotionType.WATER
        potionItem.itemMeta = potionMeta
        val result = Item.POTION_VIAL.newItemStack(1)
        val input = RecipeChoice.MaterialChoice(
            Material.POTION
        )
        val ingredient = RecipeChoice.MaterialChoice(
            Material.GLOW_BERRIES
        )
        return PotionMix(
            NamespacedKey(Odyssey.instance, "upgraded_plus_brewing"),
            result,
            input,
            ingredient
        )
    }

    private fun potionExtendedPlusMix(): PotionMix {
        // Water
        val potionItem = ItemStack(Material.POTION, 1)
        val potionMeta = potionItem.itemMeta as PotionMeta
        potionMeta.basePotionType = PotionType.WATER
        potionItem.itemMeta = potionMeta
        val result = Item.POTION_VIAL.newItemStack(1)
        val input = RecipeChoice.MaterialChoice(
            Material.POTION
        )
        val ingredient = RecipeChoice.MaterialChoice(
            Material.HONEY_BOTTLE
        )
        return PotionMix(
            NamespacedKey(Odyssey.instance, "extended_plus_brewing"),
            result,
            input,
            ingredient
        )
    }

}