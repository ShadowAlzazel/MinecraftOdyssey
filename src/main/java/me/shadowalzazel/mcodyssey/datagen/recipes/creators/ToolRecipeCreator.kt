package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.ToolMaker
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class ToolRecipeCreator : DataTagManager, ChoiceManager, ToolMaker {

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
        val result = createToolStack(material, type, amount)
        // Create name variables
        val itemName = result.getItemIdentifier()!!
        val resultKey = NamespacedKey(Odyssey.instance, itemName)
        // Get Pattern/Shape
        val pattern = toolPatterns[type.toolName]!!
        // Create Recipe keys and ingredients
        val ingredientMap = materialKeys[material.nameId]!!.toMutableMap()
        // Assemble recipe
        val recipe = ShapedRecipe(resultKey, result).apply {
            shape(*pattern.toTypedArray())
            val patternChars = pattern.reduce { acc, s -> if (s !in acc) acc + s else acc}
            for (ingredient in ingredientMap) {
                if (ingredient.key in patternChars) {
                    setIngredient(ingredient.key, ingredient.value)
                }
            }
            group = type.toolName
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    fun generateToolRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        // Get Base Tools
        val baseTools = listOf(ToolType.SWORD, ToolType.PICKAXE, ToolType.AXE, ToolType.SHOVEL, ToolType.HOE)
        val baseMaterials = listOf(
            ToolMaterial.WOODEN, ToolMaterial.GOLDEN, ToolMaterial.STONE,
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