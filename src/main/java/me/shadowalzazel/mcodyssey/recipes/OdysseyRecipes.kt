package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.items.OdysseyWeapons
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object OdysseyRecipes {

    private val NEUTRONIUM_BARK_INGOT_RECIPE: ShapedRecipe = createNeutroniumIngot()
    //private val NEUTRONIUM_BARK_SWORD_RECIPE: ShapedRecipe = createNeutroniumSword() // Disabled
    val PURE_ANTIMATTER_CRYSTAL_RECIPE: ShapedRecipe = createPurelyUnstableAntimatterCrystalRecipe()
    val FRUIT_OF_ERISHKIGAL_RECIPE: ShapedRecipe = createFruitOfErishkigal()

    val recipeSet = setOf<Recipe>(NEUTRONIUM_BARK_INGOT_RECIPE, PURE_ANTIMATTER_CRYSTAL_RECIPE, FRUIT_OF_ERISHKIGAL_RECIPE)


    //
    private fun createNeutroniumIngot(): ShapedRecipe {
        val someResult = OdysseyItems.NEUTRONIUM_BARK_INGOT.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "neutroniumbarkingot"), someResult)
        val exactNeutroniumScrap = OdysseyItems.NEUTRONIUM_BARK_SCRAPS.createItemStack(1)
        val exactGoldAlloy = OdysseyItems.PURE_ALLOY_GOLD.createItemStack(1)

        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactNeutroniumScrap))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactGoldAlloy))
        return someRecipe
    }

    /*
    //
    private fun createNeutroniumSword(): ShapedRecipe {
        val someResult = OdysseyWeapons.NEUTRONIUM_BARK_SWORD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "neutroniumbarksword"), someResult)
        val exactNeutroniumBarkIngot = OdysseyItems.NEUTRONIUM_BARK_INGOT.createItemStack(1)

        someRecipe.shape("X", "X", "Y")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactNeutroniumBarkIngot))
        someRecipe.setIngredient('Y', Material.BLAZE_ROD)
        return someRecipe
    }

     */

    // Purely Unstable
    private fun createPurelyUnstableAntimatterCrystalRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.PURE_ANTIMATTER_CRYSTAL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "purelyunstableantimattercrystal"), someResult)
        val exactImpureAntimatter = OdysseyItems.IMPURE_ANTIMATTER_SHARD.createItemStack(1)
        val exactScrap = OdysseyItems.NEUTRONIUM_BARK_SCRAPS.createItemStack(1)
        val exactDiamond = OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactDiamond))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactImpureAntimatter))
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactScrap))
        return someRecipe
    }

    //
    private fun createFruitOfErishkigal(): ShapedRecipe {
        val someResult = OdysseyItems.FRUIT_OF_ERISHKIGAL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "fruitoferishkigal"), someResult)
        val exactPureAntimatter = OdysseyItems.PURE_ANTIMATTER_CRYSTAL.createItemStack(1)
        val exactIdescineEssence = OdysseyItems.IDESCINE_ESSENCE.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactIdescineEssence))
        someRecipe.setIngredient('Y', Material.ENCHANTED_GOLDEN_APPLE)
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactPureAntimatter))
        return someRecipe
    }




}