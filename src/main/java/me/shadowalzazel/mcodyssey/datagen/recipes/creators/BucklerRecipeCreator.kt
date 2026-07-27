package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class BucklerRecipeCreator : ChoiceManager {

    private val pattern = listOf(
        " X ",
        "X|X",
        " X "
    )

    private val bucklerMaterials = listOf(
        "wooden",
        "golden",
        "iron",
        "diamond",
        "netherite",
        "copper",
        "silver",
        "soul_steel",
        "titanium",
        "anodized_titanium",
        "iridium",
        "mithril",
        "crystal_alloy"
    )

    private fun createBucklerRecipe(material: String): Recipe {
        // Create Result
        val itemName = "${material}_buckler"
        val result = Item.DataItem(itemName).newItemStack(1)
        val resultKey = NamespacedKey("odyssey", itemName)

        // Get ingredients
        val ingredientMap = materialKeys[material]!!.toMutableMap()

        val recipe = ShapedRecipe(resultKey, result).apply {
            shape(*pattern.toTypedArray())
            val patternChars = pattern.reduce { acc, s -> if (s !in acc) acc + s else acc }
            for (ingredient in ingredientMap) {
                if (ingredient.key in patternChars) {
                    setIngredient(ingredient.key, ingredient.value)
                }
            }
            group = "buckler"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    fun generateBucklerRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        for (mat in bucklerMaterials) {
            recipes.add(createBucklerRecipe(mat))
        }
        return recipes
    }
}