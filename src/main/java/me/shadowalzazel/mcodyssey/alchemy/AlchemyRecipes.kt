package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.recipes.*
import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe

object AlchemyRecipes {

    // Potion Results
    private val CRYSTALLINE_POTION_RECIPE: AlchemyCauldronRecipe = CrystallinePotionRecipe
    private val POTION_OF_LEVITATION_RECIPE: AlchemyCauldronRecipe = PotionOfLevitationRecipe
    private val POTION_OF_WITHERING_RECIPE: AlchemyCauldronRecipe = PotionOfWitheringRecipe
    private val POTION_OF_BIOLUMINESCENCE: AlchemyCauldronRecipe = PotionOfBioluminescenceRecipe
    private val POTION_OF_LUCK_RECIPE: AlchemyCauldronRecipe = PotionOfLuckRecipe
    private val POTION_OF_RESISTANCE_RECIPE: AlchemyCauldronRecipe = PotionOfResistanceRecipe
    private val POTION_OF_HASTE_RECIPE: AlchemyCauldronRecipe = PotionOfHasteRecipe
    private val POTION_OF_CONSTITUTION_RECIPE: AlchemyCauldronRecipe = PotionOfConstitutionRecipe
    private val POTION_OF_STONE_SKIN_RECIPE: AlchemyCauldronRecipe = PotionOfStoneSkinRecipe
    private val POTION_OF_WRATH_RECIPE: AlchemyCauldronRecipe = PotionOfWrathRecipe
    private val POTION_OF_DECAY_RECIPE: AlchemyCauldronRecipe = PotionOfDecayRecipe
    private val POTION_OF_FROST_RECIPE: AlchemyCauldronRecipe = PotionOfFrostRecipe
    private val POTION_OF_DOUSE_RECIPE: AlchemyCauldronRecipe = PotionOfDouseRecipe
    private val POTION_OF_ABLAZE_RECIPE: AlchemyCauldronRecipe = PotionOfAblazeRecipe
    private val POTION_OF_THORNS_RECIPE: AlchemyCauldronRecipe = PotionOfThornsRecipe
    private val PUFFY_PRICKLY_POTION_RECIPE: AlchemyCauldronRecipe = PuffyPricklyPotionRecipe
    private val BOTTLED_SOULS_RECIPE: AlchemyCauldronRecipe = BottledSoulsRecipe
    private val POLTERGEIST_BREW_RECIPE: AlchemyCauldronRecipe = PoltergeistBrewRecipe
    private val POTION_OF_SHIMMER_RECIPE: AlchemyCauldronRecipe = PotionOfShimmerRecipe

    // Set
    val recipeSet = setOf(CRYSTALLINE_POTION_RECIPE, POTION_OF_LEVITATION_RECIPE, POTION_OF_WITHERING_RECIPE, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK_RECIPE, POTION_OF_CONSTITUTION_RECIPE,
        POTION_OF_STONE_SKIN_RECIPE, POTION_OF_WRATH_RECIPE,
        POTION_OF_RESISTANCE_RECIPE, POTION_OF_HASTE_RECIPE, POTION_OF_DECAY_RECIPE, POTION_OF_DOUSE_RECIPE, POTION_OF_ABLAZE_RECIPE, POTION_OF_FROST_RECIPE,
        POTION_OF_THORNS_RECIPE, PUFFY_PRICKLY_POTION_RECIPE, BOTTLED_SOULS_RECIPE, POLTERGEIST_BREW_RECIPE,
        POTION_OF_SHIMMER_RECIPE)

}