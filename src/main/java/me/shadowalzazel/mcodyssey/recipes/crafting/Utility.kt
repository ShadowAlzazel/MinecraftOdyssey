package me.shadowalzazel.mcodyssey.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.recipes.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Utility : ChoiceManager {
    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            cloneRunesherd()
        )
    }

    private fun cloneRunesherd(): ShapedRecipe {
        val result = ItemStack(Material.CLAY_BALL, 1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "runepeice_from_runesherd"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('E',  silverChoices())
            setIngredient('C', Material.BRICK)
            setIngredient('S', Material.CLAY_BALL)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

}