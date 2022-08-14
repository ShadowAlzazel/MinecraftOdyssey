package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.potions.*
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion

object AlchemyPotions {

    val CRYSTALLINE_POTION: OdysseyPotion = CrystallinePotion
    //
    val POTION_OF_LEVITATION: OdysseyPotion = PotionOfLevitation
    val POTION_OF_WITHERING: OdysseyPotion = PotionOfWithering
    val POTION_OF_BIOLUMINESCENCE: OdysseyPotion = PotionOfBioluminescence
    val POTION_OF_LUCK: OdysseyPotion = PotionOfLuck
    val POTION_OF_RESISTANCE: OdysseyPotion = PotionOfResistance
    val POTION_OF_HASTE: OdysseyPotion = PotionOfHaste
    //
    val POTION_OF_DECAY: OdysseyPotion = PotionOfDecay
    val POTION_OF_FROST: OdysseyPotion = PotionOfFrost
    val POTION_OF_DOUSE: OdysseyPotion = PotionOfDouse
    val POTION_OF_ABLAZE: OdysseyPotion = PotionOfAblaze

    //
    val POTION_OF_SHIMMER: OdysseyPotion = PotionOfShimmer

    val potionSet = setOf(CRYSTALLINE_POTION, POTION_OF_LEVITATION, POTION_OF_WITHERING, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK, POTION_OF_RESISTANCE, POTION_OF_HASTE,
        POTION_OF_DECAY, POTION_OF_FROST, POTION_OF_DOUSE, POTION_OF_ABLAZE, POTION_OF_SHIMMER)
}