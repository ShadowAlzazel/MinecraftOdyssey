package me.shadowalzazel.mcodyssey.enigmaticSorcery

import me.shadowalzazel.mcodyssey.enigmaticSorcery.recipes.RottingSummonRecipe
import me.shadowalzazel.mcodyssey.enigmaticSorcery.recipes.SoulCrystalRecipe
import me.shadowalzazel.mcodyssey.enigmaticSorcery.recipes.SoulSteelIngotRecipe

object EnigmaticSorceryRecipes {


    // Item results
    private val SOUL_CRYSTAL_RECIPE = SoulCrystalRecipe
    private val SOUL_STEEL_RECIPE = SoulSteelIngotRecipe

    // Summon Results
    private val ROTTING_SUMMON_RECIPE = RottingSummonRecipe


    // Set
    val recipeSet = setOf(
        SOUL_CRYSTAL_RECIPE, SOUL_STEEL_RECIPE,
        ROTTING_SUMMON_RECIPE)

}