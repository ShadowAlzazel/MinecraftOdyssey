package me.shadowalzazel.mcodyssey.datagen

import me.shadowalzazel.mcodyssey.datagen.recipes.FoodRecipes
import me.shadowalzazel.mcodyssey.datagen.recipes.MerchantRecipes
import me.shadowalzazel.mcodyssey.datagen.recipes.UtilitySmithing
import me.shadowalzazel.mcodyssey.datagen.recipes.crafting.*
import me.shadowalzazel.mcodyssey.datagen.recipes.creators.ToolRecipeCreator
import me.shadowalzazel.mcodyssey.datagen.recipes.creators.WeaponRecipeCreator
import org.bukkit.inventory.Recipe

class RecipeManager {

    fun createAllRecipes(): List<Recipe> {
        val recipeList: MutableList<Recipe> = mutableListOf()
        recipeList.apply {
            addAll(Misc().getRecipes())
            addAll(FoodRecipes().getRecipes())
            addAll(MerchantRecipes().getRecipes())
            addAll(UtilitySmithing().getRecipes())
            addAll(ToolRecipeCreator().generateToolRecipes())
            addAll(WeaponRecipeCreator().generateWeaponRecipes())
            addAll(Utility().getRecipes())
        }
        return recipeList
    }

}