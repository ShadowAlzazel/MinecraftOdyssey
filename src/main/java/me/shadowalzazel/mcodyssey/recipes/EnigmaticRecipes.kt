package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object EnigmaticRecipes {

    // Register Recipes
    fun registerRecipes(): List<ShapedRecipe> {
        return listOf(
            createSorrowingSoulRecipe(),
            createSoulCatalystRecipe(),
            createEnigmaticOmamoriRecipe()
        )
    }

    /* ---------------------------------------------------------------------------*/

    private fun createSorrowingSoulRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.SORROWING_SOUL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "sorrowingsoul"), someResult)
        val exactEctoplasm = OdysseyItems.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "XYX", "XZX")
        someRecipe.setIngredient('Z', Material.PAPER)
        someRecipe.setIngredient('X', Material.ROTTEN_FLESH)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        return someRecipe
    }

    private fun createSoulCatalystRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.SOUL_CATALYST.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "soulcatalyst"), someResult)
        val exactSoulCrystal = OdysseyItems.SOUL_CRYSTAL.createItemStack(1)
        val exactEctoplasm = OdysseyItems.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', Material.TINTED_GLASS)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactSoulCrystal))
        return someRecipe
    }

    private fun createEnigmaticOmamoriRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.ENIGMATIC_OMAMORI.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "enigmaticomamori"), someResult)
        val exactSoulCrystal = OdysseyItems.SOUL_CRYSTAL.createItemStack(1)
        val exactSoulIngot = OdysseyItems.SOUL_STEEL_INGOT.createItemStack(1)

        someRecipe.shape(" W ", "XYX", "XZX")
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactSoulIngot))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactSoulCrystal))
        someRecipe.setIngredient('X', Material.PAPER)
        someRecipe.setIngredient('W', Material.STRING)
        return someRecipe
    }


}