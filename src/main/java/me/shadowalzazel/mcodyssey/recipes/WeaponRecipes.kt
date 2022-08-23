package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyWeapons
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object WeaponRecipes {

    private val WOODEN_KATANA_RECIPE = createWoodenKatanaRecipe()
    private val GOLDEN_KATANA_RECIPE = createGoldenKatanaRecipe()
    private val STONE_KATANA_RECIPE = createStoneKatanaRecipe()
    private val IRON_KATANA_RECIPE = createIronKatanaRecipe()
    private val DIAMOND_KATANA_RECIPE = createDiamondKatanaRecipe()
    private val NETHERITE_KATANA_RECIPE = createNetheriteKatanaRecipe()

    val recipeSet: Set<Recipe> = setOf(WOODEN_KATANA_RECIPE, GOLDEN_KATANA_RECIPE, STONE_KATANA_RECIPE, IRON_KATANA_RECIPE, DIAMOND_KATANA_RECIPE, NETHERITE_KATANA_RECIPE)


    private fun createWoodenKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createGoldenKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.GOLD_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createStoneKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonekatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.COBBLESTONE)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createIronKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.IRON_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createDiamondKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.DIAMOND)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }


    // ?? TEMP
    private fun createNetheriteKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.NETHERITE_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "netheritekatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.NETHERITE_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }


}