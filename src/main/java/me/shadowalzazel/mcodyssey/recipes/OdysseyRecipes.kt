package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object OdysseyRecipes {

    // Register Recipes
    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            createNeutroniumIngotRecipe(),
            createPurelyUnstableAntimatterCrystalRecipe(),
            createFruitOfErishkigalRecipe(),
            createIrradiatedFruitRecipe(),
            createArcaneBookRecipe(),
            createHourglassOfBabelRecipe()
        )
    }

    /* ---------------------------------------------------------------------------*/

    // NEUTRONIUM_INGOT
    private fun createNeutroniumIngotRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.NEUTRONIUM_BARK_INGOT.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "neutroniumbarkingot"), someResult)
        val exactNeutroniumScrap = OdysseyItems.NEUTRONIUM_BARK_SCRAPS.createItemStack(1)
        val exactGoldAlloy = OdysseyItems.PURE_ALLOY_GOLD.createItemStack(1)

        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactNeutroniumScrap))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactGoldAlloy))
        return someRecipe
    }

    // UNSTABLE_ANTIMATTER_CRYSTAL
    private fun createPurelyUnstableAntimatterCrystalRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.PURE_ANTIMATTER_CRYSTAL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "purelyunstableantimattercrystal"), someResult)
        val exactImpureAntimatter = OdysseyItems.IMPURE_ANTIMATTER_SHARD.createItemStack(1)
        val exactScrap = OdysseyItems.NEUTRONIUM_BARK_SCRAPS.createItemStack(1)
        val exactDiamond = OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactDiamond))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactImpureAntimatter))
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactScrap))
        return someRecipe
    }

    // FRUIT_OF_ERISHKIGAL
    private fun createFruitOfErishkigalRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.FRUIT_OF_ERISHKIGAL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fruitoferishkigal"), someResult)
        val exactPureAntimatter = OdysseyItems.PURE_ANTIMATTER_CRYSTAL.createItemStack(1)
        val exactIdescineEssence = OdysseyItems.IDESCINE_ESSENCE.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactIdescineEssence))
        someRecipe.setIngredient('Y', Material.ENCHANTED_GOLDEN_APPLE)
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactPureAntimatter))
        return someRecipe
    }

    // IRRADIATED_FRUIT
    private fun createIrradiatedFruitRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.IRRADIATED_FRUIT.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "irradiatedfruit"), someResult)
        val exactIrradiatedRod = OdysseyItems.IRRADIATED_ROD.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactIrradiatedRod))
        someRecipe.setIngredient('Y', Material.GLOW_BERRIES)
        someRecipe.setIngredient('Z', Material.TINTED_GLASS)
        return someRecipe
    }

    // ARCANE_BOOK
    private fun createArcaneBookRecipe(): ShapedRecipe {
        val someResult = OdysseyBooks.ARCANE_BOOK.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcanebook"), someResult)

        someRecipe.shape("DDB", "DAB", "BBC")
        someRecipe.setIngredient('A', Material.ENCHANTED_BOOK)
        someRecipe.setIngredient('B', Material.AMETHYST_SHARD)
        someRecipe.setIngredient('C', Material.DIAMOND)
        someRecipe.setIngredient('D', Material.CHORUS_FLOWER)
        return someRecipe
    }

    //
    private fun createHourglassOfBabelRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.HOURGLASS_FROM_BABEL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "hourglassfrombabel"), someResult)
        val exactShard = OdysseyItems.IRRADIATED_SHARD.createItemStack(1)
        val exactGoldAlloy = OdysseyItems.PURE_ALLOY_GOLD.createItemStack(1)

        someRecipe.shape("XYX", " Z ", "XYX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactGoldAlloy))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactShard))
        someRecipe.setIngredient('Z', Material.SAND)
        return someRecipe
    }

    // ROCKETS

    // MAYBE


}