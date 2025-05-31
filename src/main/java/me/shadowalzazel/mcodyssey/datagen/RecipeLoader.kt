package me.shadowalzazel.mcodyssey.datagen

import me.shadowalzazel.mcodyssey.datagen.recipes.*
import me.shadowalzazel.mcodyssey.datagen.recipes.crafting.EquipmentRecipes
import me.shadowalzazel.mcodyssey.datagen.recipes.crafting.FoodRecipes
import me.shadowalzazel.mcodyssey.datagen.recipes.crafting.ItemRecipes
import org.bukkit.inventory.Recipe

class RecipeLoader {

    fun createAllRecipes(): List<Recipe> {
        val recipeList: MutableList<Recipe> = mutableListOf()
        recipeList.apply {
            addAll(GeneratedRecipes().getRecipes())
            addAll(ItemRecipes().getRecipes())
            addAll(EquipmentRecipes().getRecipes())
            addAll(FoodRecipes().getRecipes())
            addAll(CookingRecipes().getRecipes())
            addAll(MerchantRecipes().getRecipes())
            addAll(SmeltingRecipes().getRecipes())
            addAll(UtilityCrafting().getRecipes())
            addAll(UtilitySmithing().getRecipes())
        }
        return recipeList
    }
    // hierarchy
}