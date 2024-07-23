package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.recipes.crafting.*
import me.shadowalzazel.mcodyssey.recipes.creators.ToolRecipeCreator
import me.shadowalzazel.mcodyssey.recipes.creators.WeaponRecipeCreator
import me.shadowalzazel.mcodyssey.recipes.smelting.Kiln
import me.shadowalzazel.mcodyssey.recipes.smelting.Tempering
import org.bukkit.inventory.Recipe

class RecipeManager {

    fun createAllRecipes(): List<Recipe> {
        val recipeList: MutableList<Recipe> = mutableListOf()
        recipeList.apply {
            addAll(Misc().getRecipes())
            addAll(Food().getRecipes())
            addAll(Merchant().getRecipes())
            addAll(Weapons().getRecipes())
            addAll(Equipment().getRecipes())
            addAll(Kiln().getRecipes())
            addAll(Tempering().getRecipes())
            addAll(UtilitySmithing().getRecipes())
            addAll(ToolRecipeCreator().generateToolRecipes())
            addAll(WeaponRecipeCreator().generateWeaponRecipes())
            addAll(Utility().getRecipes())
        }
        return recipeList
    }

}