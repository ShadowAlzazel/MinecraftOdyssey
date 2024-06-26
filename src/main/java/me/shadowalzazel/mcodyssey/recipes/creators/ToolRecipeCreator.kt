package me.shadowalzazel.mcodyssey.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import me.shadowalzazel.mcodyssey.items.creators.ToolCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.recipes.ChoiceManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class ToolRecipeCreator : ItemCreator, DataTagManager, ChoiceManager {

    private val toolCreator = ToolCreator()

    private val toolPatterns = mapOf(
        "pickaxe" to listOf(
            "XXX",
            " | ",
            " | "),
        "axe" to listOf(
            "XX",
            "X|",
            " |"),
        "shovel" to listOf(
            "X",
            "|",
            "|"),
        "hoe" to listOf(
            "XX",
            " |",
            " |"),
        "sword" to listOf(
            "X",
            "X",
            "|")
    )

    private fun createToolRecipe(material: ToolMaterial, type: ToolType): Recipe {
        val amount = 1
        val result = toolCreator.createToolStack(material, type, amount)
        // Create name variables
        val itemName = result.getItemIdentifier()!!
        val resultKey = NamespacedKey(Odyssey.instance, itemName)
        // Get Pattern/Shape
        val pattern = toolPatterns[type.itemName]!!
        // Create Recipe keys and ingredients
        val ingredientMap = materialKeys[material.itemName]!!.toMutableMap()
        // Assemble recipe
        val recipe = ShapedRecipe(resultKey, result).apply {
            shape(*pattern.toTypedArray())
            val patternChars = pattern.reduce { acc, s -> if (s !in acc) acc + s else acc}
            for (ingredient in ingredientMap) {
                if (ingredient.key in patternChars) {
                    setIngredient(ingredient.key, ingredient.value)
                }
            }
            group = type.itemName
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    fun generateToolRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        // Get Base Tools
        val baseTools = listOf(ToolType.SWORD, ToolType.PICKAXE, ToolType.AXE, ToolType.SHOVEL, ToolType.HOE)
        val baseMaterials = listOf(ToolMaterial.WOODEN, ToolMaterial.GOLDEN, ToolMaterial.STONE,
            ToolMaterial.IRON, ToolMaterial.DIAMOND, ToolMaterial.NETHERITE)
        val toolMaterialEntries = ToolMaterial.entries.toMutableList()
        toolMaterialEntries.removeAll(baseMaterials)
        // Iterator For model type weapons
        for (mat in toolMaterialEntries) {
            for (type in baseTools) {
                recipes.add(createToolRecipe(mat, type))
            }
        }
        return recipes
    }

}