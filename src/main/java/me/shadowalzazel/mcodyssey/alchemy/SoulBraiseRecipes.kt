package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.recipes.RottingSummonRecipe
import me.shadowalzazel.mcodyssey.alchemy.recipes.SculkHeartRecipe
import me.shadowalzazel.mcodyssey.alchemy.recipes.SoulCrystalRecipe
import me.shadowalzazel.mcodyssey.alchemy.recipes.SoulSteelIngotRecipe

object SoulBraiseRecipes {

    // Item results
    private val SOUL_CRYSTAL_RECIPE = SoulCrystalRecipe
    private val SOUL_STEEL_RECIPE = SoulSteelIngotRecipe
    private val SCULK_HEART_RECIPE = SculkHeartRecipe

    // Summon Results
    private val ROTTING_SUMMON_RECIPE = RottingSummonRecipe

    // Set
    val BRAISE_SET = setOf(
        SOUL_CRYSTAL_RECIPE,
        SOUL_STEEL_RECIPE,
        SCULK_HEART_RECIPE,
        ROTTING_SUMMON_RECIPE
    )

}