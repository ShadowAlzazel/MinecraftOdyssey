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

    val potionSet = setOf(CRYSTALLINE_POTION, POTION_OF_LEVITATION, POTION_OF_WITHERING, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK)
}