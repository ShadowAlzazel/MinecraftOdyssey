package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.ToolMaker
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class WeaponRecipeCreator : DataTagManager, ChoiceManager, ToolMaker {

    // Patterns
    private val weaponPatterns = mapOf(
        // Other Overrides
        "shuriken" to listOf(
            " X ",
            "XCX",
            " X "),
        // Sword Overrides
        "katana" to listOf(
            "  X",
            "CX ",
            "|  "),
        "claymore" to listOf(
            " X ",
            "XXX",
            " | "),
        "dagger" to listOf(
            " X",
            "| "),
        "rapier" to listOf(
            "|XX"),
        "cutlass" to listOf(
            " X",
            " X",
            "|C"),
        "saber" to listOf(
            " X",
            "X ",
            "| "),
        "sickle" to listOf(
            " X",
            "|X"),
        "chakram" to listOf(
            "XC",
            "|X"),
        "kunai" to listOf(
            "X",
            "|",
            "C"),
        "longsword" to listOf(
            "  X",
            " X ",
            "|  "),
        "arm_blade" to listOf(
            " X ",
            "XCX"),
        "zweihander" to listOf(
            "X X",
            " X ",
            "C X"),
        "kriegsmesser" to listOf(
            " XX",
            "XX ",
            "|  "),
        // Shovel Overrides
        "spear" to listOf(
            "  X",
            " | ",
            "|  "),
        "halberd" to listOf(
            " XX",
            " |C",
            "|  "),
        "lance" to listOf(
            "  X",
            "C| ",
            "|C "),
        // Axe Overrides
        "longaxe" to listOf(
            "|XX",
            "|XX",
            "|  "),
        "poleaxe" to listOf(
            "XXX",
            " |X",
            "|  "),
        "glaive" to listOf(
            " XX",
            " |X",
            "|  "),
        "battlesaw" to listOf(
            "|CX",
            "|XX",
            "|  "),
        // Pickaxe Overrides
        "warhammer" to listOf(
            " X ",
            "X|X",
            " | "),
        // Hoe Overrides
        "scythe" to listOf(
            "CXX",
            "|  ",
            "|  ")
    )

    // Complementary Keys
    private val weaponSpecialMaterialKeys = mapOf(
        "shuriken" to mapOf(
            'C' to MaterialChoice(Material.IRON_NUGGET)
        ),
        "katana" to mapOf(
            'C' to MaterialChoice(Material.COPPER_INGOT)
        ),
        "cutlass" to mapOf(
            'C' to MaterialChoice(Material.GOLD_INGOT)
        ),
        "kunai" to mapOf(
            'C' to MaterialChoice(Material.IRON_NUGGET)
        ),
        "chakram" to mapOf(
            'C' to MaterialChoice(Material.RABBIT_HIDE)
        ),
        "halberd" to mapOf(
            'C' to MaterialChoice(Material.PRISMARINE_SHARD)
        ),
        "lance" to mapOf(
            'C' to MaterialChoice(Material.RABBIT_HIDE)
        ),
        "arm_blade" to mapOf(
            'C' to MaterialChoice(Material.RABBIT_HIDE)
        ),
        "zweihander" to mapOf(
            'C' to MaterialChoice(Material.BREEZE_ROD)
        ),
        "battlesaw" to mapOf(
            'C' to MaterialChoice(Material.HEAVY_CORE)
        ),
        "scythe" to mapOf(
            'C' to MaterialChoice(Material.COPPER_INGOT)
        ),
    )

    private fun createWeaponRecipe(material: ToolMaterial, type: ToolType): Recipe {
        val amount = if (type == ToolType.SHURIKEN) { 16 } else { 1 } // MOVE TO DICT LATER
        val result = createToolStack(material, type, amount)
        // Create name variables
        val itemName = result.getItemIdentifier()!!
        val resultKey = NamespacedKey(Odyssey.instance, itemName)
        // Get Pattern/Shape
        val pattern = weaponPatterns[type.toolName]!!
        // Create Recipe keys and ingredients
        val ingredientMap = materialKeys[material.nameId]!!.toMutableMap()
        val extraIngredientMap = weaponSpecialMaterialKeys[type.toolName]
        if (extraIngredientMap != null) {
            ingredientMap.putAll(extraIngredientMap)
        }
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

    fun generateWeaponRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        // Get entries from enums
        val baseTools = listOf(ToolType.SWORD, ToolType.PICKAXE, ToolType.AXE, ToolType.SHOVEL, ToolType.HOE)
        val weaponTypeEntries = ToolType.entries.toMutableList()
        weaponTypeEntries.removeAll(baseTools)
        val toolMaterialEntries = ToolMaterial.entries.toList()
        // Iterator For model type weapons
        for (mat in toolMaterialEntries) {
            for (type in weaponTypeEntries) {
                recipes.add(createWeaponRecipe(mat, type))
            }
        }
        return recipes
    }

}