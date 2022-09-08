package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object EnigmaticRecipes {

    private val SORROWING_SOUL_RECIPE = createSorrowingSoul()

    val recipeSet = listOf(SORROWING_SOUL_RECIPE)

    /* ---------------------------------------------------------------------------*/

    private fun createSorrowingSoul(): ShapedRecipe {
        val someResult = OdysseyItems.SORROWING_SOUL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "sorrowingsoul"), someResult)
        val exactEctoplasm = OdysseyItems.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "XYX", "XZX")
        someRecipe.setIngredient('Z', Material.PAPER)
        someRecipe.setIngredient('X', Material.ROTTEN_FLESH)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        return someRecipe
    }


}