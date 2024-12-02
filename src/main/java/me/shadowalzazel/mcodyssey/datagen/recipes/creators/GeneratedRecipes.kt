package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.inventory.Recipe

class GeneratedRecipes : ChoiceManager {

    fun getRecipes(): List<Recipe> {
        val rocketCreator = RocketRecipeCreator()
        return listOf(
            rocketCreator.blazingRocketsRecipe(1, "one"),
            rocketCreator.blazingRocketsRecipe(2, "two"),
            rocketCreator.blazingRocketsRecipe(3, "three"),
            rocketCreator.blazingRocketsRecipe(4, "four"),
            rocketCreator.blazingRocketsRecipe(5, "five")
        )
    }


}