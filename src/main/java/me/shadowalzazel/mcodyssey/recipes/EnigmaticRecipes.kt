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
            createSoulCatalystRecipe()
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

        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', RecipeChoice.ExactChoice(exactSoulCrystal))
        someRecipe.setIngredient('Y', Material.TINTED_GLASS)
        return someRecipe
    }



}