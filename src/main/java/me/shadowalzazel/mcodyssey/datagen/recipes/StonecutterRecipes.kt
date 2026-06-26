package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.StonecuttingRecipe

@Suppress("UnstableApiUsage")
class StonecutterRecipes : ChoiceManager {

    fun getRecipes(): List<Recipe> {
        return listOf(
            cinnabarShardRecipe(),
            sulfurPowderRecipe()
        )
    }


    private fun cinnabarShardRecipe(): StonecuttingRecipe {
        val result = Item.CINNABAR_SHARD.newItemStack(8)
        val input = RecipeChoice.itemType(ItemType.CINNABAR)
        return StonecuttingRecipe(
            NamespacedKey(Odyssey.instance, "cinnabar_shard_stonecutter"),
            result,
            input)
    }

    private fun sulfurPowderRecipe(): StonecuttingRecipe {
        val result = Item.SULFUR_POWDER.newItemStack(8)
        val input = RecipeChoice.itemType(ItemType.SULFUR)
        return StonecuttingRecipe(
            NamespacedKey(Odyssey.instance, "sulfur_powder_stonecutter"),
            result,
            input)
    }


}