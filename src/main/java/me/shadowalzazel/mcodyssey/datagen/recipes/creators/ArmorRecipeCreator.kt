package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class ArmorRecipeCreator : ChoiceManager {

    private val patterns = mapOf(
        "helmet" to listOf(
            "XXX",
            "X X",
            "   "),
        "chestplate" to listOf(
            "X X",
            "XXX",
            "XXX"),
        "leggings" to listOf(
            "XXX",
            "X X",
            "X X"),
        "boots" to listOf(
            "X X",
            "X X",
            "   ")
    )

    private val armorTypes = listOf(
        "helmet",
        "chestplate",
        "leggings",
        "boots"
    )

    private val armorMaterials = listOf(
        "mithril",
        "iridium",
        "soul_steel",
        "titanium",
        "anodized_titanium",
        "copper",
        "silver"
    )

    private fun createToolRecipe(material: String, type: String): Recipe {
        // Create Result
        val itemName = "${material}_${type}"
        val result = Item.DataItem(itemName).newItemStack(1)
        val resultKey = NamespacedKey("odyssey", itemName)
        // Get keys
        val pattern = patterns[type]!!
        val ingredientMap = materialKeys[material]!!.toMutableMap()
        val recipe = ShapedRecipe(resultKey, result).apply {
            shape(*pattern.toTypedArray())
            val patternChars = pattern.reduce { acc, s -> if (s !in acc) acc + s else acc}
            for (ingredient in ingredientMap) {
                if (ingredient.key in patternChars) {
                    setIngredient(ingredient.key, ingredient.value)
                }
            }
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


    fun generateArmorRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        for (mat in armorMaterials) {
            for (type in armorTypes) {
                recipes.add(createToolRecipe(mat, type))
            }
        }

        return recipes
    }


}