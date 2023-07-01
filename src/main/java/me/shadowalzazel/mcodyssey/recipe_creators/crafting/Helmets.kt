package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Helmets
import me.shadowalzazel.mcodyssey.items.Helmets.createArmor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Helmets {

    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            hornedHelmetRecipe()
        )
    }

    /*---------------------------------------------------------------------------------------*/

    private fun hornedHelmetRecipe(): ShapedRecipe {
        val result = Helmets.HORNED_HELMET.createArmor(1.0)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "horned_helmet_recipe"), result).apply {
            shape("HCH", "ICI")
            setIngredient('I', Material.IRON_INGOT)
            setIngredient('H', Material.GOAT_HORN)
            setIngredient('C', Material.COPPER_INGOT)
            group = "helmets"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

}