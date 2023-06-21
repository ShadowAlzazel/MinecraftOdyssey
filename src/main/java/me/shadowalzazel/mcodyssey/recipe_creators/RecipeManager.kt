package me.shadowalzazel.mcodyssey.recipe_creators

import me.shadowalzazel.mcodyssey.recipe_creators.crafting.Food
import me.shadowalzazel.mcodyssey.recipe_creators.crafting.Misc
import me.shadowalzazel.mcodyssey.recipe_creators.smithing.Weapon
import org.bukkit.inventory.Recipe

class RecipeManager {

    fun createAllRecipes(): List<Recipe> {
        val recipeList: MutableList<Recipe> = mutableListOf()
        recipeList.apply {
            addAll(Misc().getRecipes())
            addAll(Food().getRecipes())
            addAll(Merchant().getRecipes())
            addAll(Weapon().getRecipes())
            addAll(Smithing().getRecipes())
        }

        return recipeList
    }

}