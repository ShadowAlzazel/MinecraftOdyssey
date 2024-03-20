package me.shadowalzazel.mcodyssey.recipe_creators.brewing

import io.papermc.paper.potion.PotionMix
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

object BrewerMixes : AlchemyManager {

    fun getMixes(): List<PotionMix> {
        return listOf(
            vailBrewingPotionMix(),
            potionExtendedPlusMix(),
            potionUpgradedPlusMix()
        )
    }

    private fun vailBrewingPotionMix(): PotionMix {
        // Water
        val potionItem = ItemStack(Material.POTION, 1)
        val somePotionMeta = potionItem.itemMeta as PotionMeta
        somePotionMeta.basePotionData = PotionData(PotionType.WATER)
        potionItem.itemMeta = somePotionMeta

        val result: ItemStack = createPotionVials(potionItem)
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
        val somePotionMeta = potionItem.itemMeta as PotionMeta
        somePotionMeta.basePotionData = PotionData(PotionType.WATER)
        potionItem.itemMeta = somePotionMeta

        val result: ItemStack = createPotionVials(potionItem)
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
        val somePotionMeta = potionItem.itemMeta as PotionMeta
        somePotionMeta.basePotionData = PotionData(PotionType.WATER)
        potionItem.itemMeta = somePotionMeta

        val result: ItemStack = createPotionVials(potionItem)
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