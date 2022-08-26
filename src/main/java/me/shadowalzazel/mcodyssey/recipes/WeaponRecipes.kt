package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyWeapons
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object WeaponRecipes {

    // Katana Recipes
    private val WOODEN_KATANA_RECIPE = createWoodenKatanaRecipe()
    private val GOLDEN_KATANA_RECIPE = createGoldenKatanaRecipe()
    private val STONE_KATANA_RECIPE = createStoneKatanaRecipe()
    private val IRON_KATANA_RECIPE = createIronKatanaRecipe()
    private val DIAMOND_KATANA_RECIPE = createDiamondKatanaRecipe()
    private val NETHERITE_KATANA_RECIPE = createNetheriteKatanaRecipe()

    // Claymore Recipes
    private val WOODEN_CLAYMORE_RECIPE = createWoodenClaymoreRecipe()
    private val GOLDEN_CLAYMORE_RECIPE = createGoldenClaymoreRecipe()
    private val STONE_CLAYMORE_RECIPE = createStoneClaymoreRecipe()
    private val IRON_CLAYMORE_RECIPE = createIronClaymoreRecipe()
    private val DIAMOND_CLAYMORE_RECIPE = createDiamondClaymoreRecipe()

    // Spaer Recipes
    private val WOODEN_SPEAR_RECIPE = createWoodenSpearRecipe()
    private val GOLDEN_SPEAR_RECIPE = createGoldenSpearRecipe()
    private val STONE_SPEAR_RECIPE = createStoneSpearRecipe()
    private val IRON_SPEAR_RECIPE = createIronSpearRecipe()
    private val DIAMOND_SPEAR_RECIPE = createDiamondSpearRecipe()

    // Recipes
    val recipeSet: Set<Recipe> = setOf(WOODEN_KATANA_RECIPE, GOLDEN_KATANA_RECIPE, STONE_KATANA_RECIPE, IRON_KATANA_RECIPE, DIAMOND_KATANA_RECIPE, NETHERITE_KATANA_RECIPE,
        WOODEN_CLAYMORE_RECIPE, GOLDEN_CLAYMORE_RECIPE, STONE_CLAYMORE_RECIPE, IRON_CLAYMORE_RECIPE, DIAMOND_CLAYMORE_RECIPE,
        WOODEN_SPEAR_RECIPE, GOLDEN_SPEAR_RECIPE, STONE_SPEAR_RECIPE, IRON_SPEAR_RECIPE, DIAMOND_SPEAR_RECIPE)

    /*----------------------------------------KATANAS-----------------------------------------*/

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
        someRecipe.setIngredient('Y', Material.BLAZE_ROD)
        return someRecipe
    }


    /*----------------------------------------CLAYMORES-----------------------------------------*/

    private fun createWoodenClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }


    private fun createGoldenClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.GOLD_INGOT))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }


    private fun createStoneClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stoneclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.COBBLESTONE))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }


    private fun createIronClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.IRON_INGOT))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }


    private fun createDiamondClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondclaymore"), someResult).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.DIAMOND))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------KATANAS-----------------------------------------*/

    private fun createWoodenSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    private fun createStoneSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonespear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    private fun createIronSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    private fun createDiamondSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }



}