package me.shadowalzazel.mcodyssey.recipe_creators

import me.shadowalzazel.mcodyssey.recipe_creators.crafting.*
import me.shadowalzazel.mcodyssey.recipe_creators.smelting.Kiln
import org.bukkit.inventory.Recipe

class RecipeManager {

    fun createAllRecipes(): List<Recipe> {
        val recipeList: MutableList<Recipe> = mutableListOf()
        recipeList.apply {
            addAll(Misc().getRecipes())
            addAll(Food().getRecipes())
            addAll(Merchant().getRecipes())
            addAll(Smithing().getRecipes())
            addAll(Weapons().getRecipes())
            addAll(Helmets().getRecipes())
            addAll(Equipment().getRecipes())
            addAll(Kiln().getRecipes())
        }
        return recipeList
    }

}