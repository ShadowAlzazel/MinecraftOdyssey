package me.shadowalzazel.mcodyssey.recipe_creators

import me.shadowalzazel.mcodyssey.recipe_creators.crafting.Food
import me.shadowalzazel.mcodyssey.recipe_creators.crafting.Misc
import me.shadowalzazel.mcodyssey.recipe_creators.crafting.Weapon
import org.bukkit.inventory.Recipe

class RecipeManager {

    fun createAllRecipes(): List<Recipe> {
        val recipeList: MutableList<Recipe> = mutableListOf()
        recipeList.apply {
            addAll(Weapon().getRecipes())
            addAll(Misc().getRecipes())
            addAll(Food().getRecipes())
            addAll(Merchant().getRecipes())
            addAll(Smithing().getRecipes())
        }

        return recipeList
    }

}