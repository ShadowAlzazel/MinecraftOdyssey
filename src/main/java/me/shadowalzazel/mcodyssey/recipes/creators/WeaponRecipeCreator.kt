package me.shadowalzazel.mcodyssey.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import me.shadowalzazel.mcodyssey.items.creators.ToolCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class WeaponRecipeCreator : ItemCreator {

    private val toolCreator = ToolCreator()

    // Const
    private val WOOD_CHOICES: MaterialChoice = MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS,
        Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS)

    // Patterns
    private val toolPatterns = mapOf(
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

    // Keys
    private val materialKeys = mapOf<String, Map<Char, RecipeChoice>>(
        // Minecraft
        "wooden" to mapOf(
            'X' to WOOD_CHOICES,
            '|' to MaterialChoice(Material.STICK)
        ),
        "stone" to mapOf(
            'X' to MaterialChoice(Material.COBBLESTONE),
            '|' to MaterialChoice(Material.STICK)
        ),
        "golden" to mapOf(
            'X' to MaterialChoice(Material.GOLD_INGOT),
            '|' to MaterialChoice(Material.STICK)
        ),
        "iron" to mapOf(
            'X' to MaterialChoice(Material.IRON_INGOT),
            '|' to MaterialChoice(Material.STICK)
        ),
        "diamond" to mapOf(
            'X' to MaterialChoice(Material.DIAMOND),
            '|' to MaterialChoice(Material.STICK)
        ),
        "netherite" to mapOf(
            'X' to MaterialChoice(Material.NETHERITE_INGOT),
            '|' to MaterialChoice(Material.STICK)
        ),
        // Odyssey
        "copper" to mapOf(
            'X' to MaterialChoice(Material.COPPER_INGOT),
            '|' to MaterialChoice(Material.STICK)
        ),
        "silver" to mapOf(
            'X' to ExactChoice(Ingredients.SILVER_INGOT.createItemStack(1)),
            '|' to MaterialChoice(Material.STICK)
        ),
        "soul_steel" to mapOf(
            'X' to ExactChoice(Ingredients.SOUL_STEEL_INGOT.createItemStack(1)),
            '|' to MaterialChoice(Material.STICK)
        ),
        "titanium" to mapOf(
            'X' to ExactChoice(Ingredients.TITANIUM_INGOT.createItemStack(1)),
            '|' to MaterialChoice(Material.STICK)
        ),
        "andonized_titanium" to mapOf(
            'X' to ExactChoice(Ingredients.ANDONIZED_TITANIUM_INGOT.createItemStack(1)),
            '|' to MaterialChoice(Material.STICK)
        ),
        "iridium" to mapOf(
            'X' to ExactChoice(Ingredients.IRIDIUM_INGOT.createItemStack(1)),
            '|' to MaterialChoice(Material.STICK)
        ),
        "mithril" to mapOf(
            'X' to ExactChoice(Ingredients.MITHRIL_INGOT.createItemStack(1)),
            '|' to MaterialChoice(Material.STICK)
        ),
    )

    // Complementary Keys
    private val toolKeys = mapOf(
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
        "scythe" to mapOf(
            'C' to MaterialChoice(Material.COPPER_INGOT)
        ),
    )

    private fun createWeaponRecipe(material: ToolMaterial, type: ToolType): Recipe {
        val amount = if (type == ToolType.SHURIKEN) { 16 } else { 1 } // MOVE TO DICT LATER
        val result = toolCreator.createToolStack(material, type, amount)
        // Create name variables
        val itemName = result.getOdysseyTag()!!
        val resultKey = NamespacedKey(Odyssey.instance, itemName)
        // Get Pattern/Shape
        val pattern = toolPatterns[type.itemName]!!
        // Create Recipe keys and ingredients
        val ingredientMap = materialKeys[material.itemName]!!.toMutableMap()
        val toolMap = toolKeys[type.itemName]
        if (toolMap != null) {
            ingredientMap.putAll(toolMap)
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
            group = type.itemName
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    fun generateWeaponRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        // Get entries from enums
        val toolTypeEntries = ToolType.entries.toList()
        val toolMaterialEntries = ToolMaterial.entries.toList()
        // Iterator For model type weapons
        for (mat in toolMaterialEntries) {
            for (type in toolTypeEntries) {
                recipes.add(createWeaponRecipe(mat, type))
            }
        }
        //

        return recipes
    }

}