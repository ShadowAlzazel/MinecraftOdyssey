package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.recipes.*
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyAlchemyCauldronRecipe

object AlchemyRecipes {

    val CRYSTALLINE_POTION_RECIPE: OdysseyAlchemyCauldronRecipe = CrystallinePotionRecipe
    //
    val POTION_OF_LEVITATION_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfLevitationRecipe
    val POTION_OF_WITHERING_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfWitheringRecipe
    val POTION_OF_BIOLUMINESCENCE: OdysseyAlchemyCauldronRecipe = PotionOfBioluminescenceRecipe
    val POTION_OF_LUCK_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfLuckRecipe
    val POTION_OF_RESISTANCE_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfResistanceRecipe
    val POTION_OF_HASTE_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfHasteRecipe
    //
    val POTION_OF_DECAY_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfDecayRecipe
    val POTION_OF_SHIMMER_RECIPE: OdysseyAlchemyCauldronRecipe = PotionOfShimmerRecipe
    //
    val alchemyRecipeSet = setOf(CRYSTALLINE_POTION_RECIPE, POTION_OF_LEVITATION_RECIPE, POTION_OF_WITHERING_RECIPE, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK_RECIPE,
        POTION_OF_RESISTANCE_RECIPE, POTION_OF_HASTE_RECIPE, POTION_OF_DECAY_RECIPE, POTION_OF_SHIMMER_RECIPE)

}