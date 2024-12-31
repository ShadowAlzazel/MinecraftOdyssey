package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import me.shadowalzazel.mcodyssey.datagen.recipes.creators.*
import org.bukkit.inventory.Recipe

class GeneratedRecipes : ChoiceManager {

    fun getRecipes(): List<Recipe> {
        val rocketCreator = RocketRecipeCreator()
        val recipeList = mutableListOf<Recipe>(
            rocketCreator.blazingRocketsRecipe(1, "one"),
            rocketCreator.blazingRocketsRecipe(2, "two"),
            rocketCreator.blazingRocketsRecipe(3, "three"),
            rocketCreator.blazingRocketsRecipe(4, "four"),
            rocketCreator.blazingRocketsRecipe(5, "five"))
        recipeList.addAll(ToolRecipeCreator().generateToolRecipes())
        recipeList.addAll(WeaponRecipeCreator().generateWeaponRecipes())
        recipeList.addAll(ArmorRecipeCreator().generateArmorRecipes())
        recipeList.addAll(ItemRecipeCreator().generateItemRecipes())
        return recipeList
    }


}