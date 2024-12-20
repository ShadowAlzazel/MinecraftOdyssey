package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class ItemRecipeCreator : ChoiceManager {

    internal fun generateItemRecipes(): List<ShapedRecipe> {
        val patterns = listOf(Item.VOYAGER_PART_PATTERN, Item.SERAPH_PART_PATTERN, Item.DANGER_PART_PATTERN, Item.HUMBLE_PART_PATTERN,
            Item.IMPERIAL_PART_PATTERN, Item.MARAUDER_PART_PATTERN, Item.VANDAL_PART_PATTERN, Item.CRUSADER_PART_PATTERN, Item.FANCY_PART_PATTERN)

        val parts = listOf(Item.BLADE_PART_UPGRADE_TEMPLATE, Item.HILT_PART_UPGRADE_TEMPLATE,
            Item.POMMEL_PART_UPGRADE_TEMPLATE, Item.HANDLE_PART_UPGRADE_TEMPLATE)

        return mutableListOf<ShapedRecipe>().apply {
            addAll(patterns.map { patternReplicateRecipe(it) })
            addAll(parts.map { partUpgradeReplicateRecipe(it) })
        }
    }


    private fun patternReplicateRecipe(item: Item): ShapedRecipe {
        val result = item.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "${item.name}_replicate"), result).apply {
            shape("R", "X", "D")
            setIngredient('R', Material.RABBIT_HIDE)
            setIngredient('X', item.toRecipeChoice())
            setIngredient('D', Material.DIAMOND)
        }
        return recipe
    }

    private fun partUpgradeReplicateRecipe(item: Item): ShapedRecipe {
        val result = item.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "${item.name}_replicate"), result).apply {
            shape("G", "X")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('X', item.toRecipeChoice())
        }
        return recipe
    }

}