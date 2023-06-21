package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.Runic
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.meta.FireworkMeta

class Misc {

    // Register Recipes
    fun getRecipes(): List<Recipe> {
        return listOf(
            //createNeutroniumIngotRecipe(),
            //createPurelyUnstableAntimatterCrystalRecipe(),
            //createFruitOfErishkigalRecipe(),
            createSoulCatalystRecipe(),
            createEnigmaticOmamoriRecipe(),
            createIrradiatedFruitRecipe(),
            createArcaneBookRecipe(),
            createDurationRocket(1, "one"),
            createDurationRocket(2, "two"),
            createDurationRocket(3, "three"),
            createDurationRocket(4, "four"),
            createDurationRocket(5, "five")
        )
    }

    /* ---------------------------------------------------------------------------*/

    // NEUTRONIUM_INGOT
    /*
    private fun createNeutroniumIngotRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.NEUTRONIUM_BARK_INGOT.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "neutroniumbarkingot"), someResult)
        val exactNeutroniumScrap = Miscellaneous.NEUTRONIUM_BARK_SCRAPS.createItemStack(1)
        val exactGoldAlloy = Miscellaneous.PURE_ALLOY_GOLD.createItemStack(1)

        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactNeutroniumScrap))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactGoldAlloy))
        return someRecipe
    }

     */

    /*
    // UNSTABLE_ANTIMATTER_CRYSTAL
    private fun createPurelyUnstableAntimatterCrystalRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.PURE_ANTIMATTER_CRYSTAL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "purelyunstableantimattercrystal"), someResult)
        val exactImpureAntimatter = Miscellaneous.IMPURE_ANTIMATTER_SHARD.createItemStack(1)
        val exactScrap = Miscellaneous.NEUTRONIUM_BARK_SCRAPS.createItemStack(1)
        val exactDiamond = Miscellaneous.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactDiamond))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactImpureAntimatter))
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactScrap))
        return someRecipe
    }

     */
    // FRUIT_OF_ERISHKIGAL
    /*
    private fun createFruitOfErishkigalRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.FRUIT_OF_ERISHKIGAL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fruitoferishkigal"), someResult)
        val exactPureAntimatter = Miscellaneous.PURE_ANTIMATTER_CRYSTAL.createItemStack(1)
        val exactIdescineEssence = Miscellaneous.ELENCUILE_ESSENCE.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactIdescineEssence))
        someRecipe.setIngredient('Y', Material.ENCHANTED_GOLDEN_APPLE)
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactPureAntimatter))
        return someRecipe
    }

     */

    /*
    private fun createSorrowingSoulRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.SORROWING_SOUL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "sorrowingsoul"), someResult)
        val exactEctoplasm = Materials.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "XYX", "XZX")
        someRecipe.setIngredient('Z', Material.PAPER)
        someRecipe.setIngredient('X', Material.ROTTEN_FLESH)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        return someRecipe
    }

    //
    private fun createHourglassOfBabelRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.HOURGLASS_FROM_BABEL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "hourglass_from_babel"), someResult)
        val exactShard = Ingredients.IRRADIATED_SHARD.createItemStack(1)
        val exactGoldAlloy = ItemStack(Material.GOLD_BLOCK)

        someRecipe.shape("XYX", " Z ", "XYX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactGoldAlloy))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactShard))
        someRecipe.setIngredient('Z', Material.SAND)
        return someRecipe
    }


     */

    private fun createIrradiatedFruitRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.IRRADIATED_FRUIT.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "irradiated_fruit"), someResult)
        val exactIrradiatedRod = Ingredients.IRRADIATED_ROD.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactIrradiatedRod))
        someRecipe.setIngredient('Y', Material.GLOW_BERRIES)
        someRecipe.setIngredient('Z', Material.TINTED_GLASS)
        return someRecipe
    }

    private fun createArcaneBookRecipe(): ShapedRecipe {
        val someResult = Runic.ARCANE_BOOK.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_book"), someResult)

        someRecipe.shape(" A ", "ABA", " AC")
        someRecipe.setIngredient('A', Material.AMETHYST_SHARD)
        someRecipe.setIngredient('B', Material.ENCHANTED_BOOK)
        someRecipe.setIngredient('C', Material.PRISMARINE_CRYSTALS)
        return someRecipe
    }

    private fun createSoulCatalystRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.SOUL_CATALYST.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_catalyst"), someResult)
        val exactSoulCrystal = Ingredients.SOUL_CRYSTAL.createItemStack(1)
        val exactEctoplasm = Ingredients.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', Material.TINTED_GLASS)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactSoulCrystal))
        return someRecipe
    }

    private fun createEnigmaticOmamoriRecipe(): ShapedRecipe {
        val someResult = Miscellaneous.ENIGMATIC_OMAMORI.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "enigmatic_omamori"), someResult)
        val exactSoulCrystal = Ingredients.SOUL_CRYSTAL.createItemStack(1)
        val exactSoulIngot = Ingredients.SOUL_STEEL_INGOT.createItemStack(1)

        someRecipe.shape(" W ", "XYX", "XZX")
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactSoulIngot))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactSoulCrystal))
        someRecipe.setIngredient('X', Material.PAPER)
        someRecipe.setIngredient('W', Material.STRING)
        return someRecipe
    }

    private fun createDurationRocket(tier: Int, tierNum: String): ShapelessRecipe {
        // New Rocket
        val newRocket = ItemStack(Material.FIREWORK_ROCKET, 1)
        newRocket.itemMeta = (newRocket.itemMeta as FireworkMeta).also {
            it.power = tier + 3
            it.lore(listOf(Component.text("Danger!", TextColor.color(255, 55, 55)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        }
        // Recipe
        val someRecipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "duration_rocket_tier_$tierNum"), newRocket).apply {
            addIngredient(2, Material.GUNPOWDER)
            addIngredient(tier, Material.BLAZE_POWDER)
            addIngredient(1, Material.PAPER)
            addIngredient(RecipeChoice.MaterialChoice(listOf(Material.GUNPOWDER, Material.FIREWORK_STAR)))
            group = "reckless_rockets"
        }
        return someRecipe
    }

}