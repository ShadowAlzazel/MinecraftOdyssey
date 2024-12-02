package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CampfireRecipe
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice

class CookingRecipes {

    fun getRecipes(): List<Recipe> {
        return listOf(
            baconRecipe(), cookedBrisketRecipe()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Campfire

    private fun baconRecipe(): CampfireRecipe {
        val result = Item.BACON.newItemStack()
        return CampfireRecipe(
            NamespacedKey(Odyssey.instance, "bacon"),
            result,
            Material.COOKED_PORKCHOP,
            1.0F,
            10 * 20
        )
    }

    private fun cookedBrisketRecipe(): CampfireRecipe {
        val result = Item.COOKED_BRISKET.newItemStack()
        val ingredient = RecipeChoice.ExactChoice(Item.BRISKET.newItemStack())
        return CampfireRecipe(
            NamespacedKey(Odyssey.instance, "cooked_brisket"),
            result,
            ingredient,
            1.0F,
            10 * 20
        )
    }


}