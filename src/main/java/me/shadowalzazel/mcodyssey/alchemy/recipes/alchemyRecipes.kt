package me.shadowalzazel.mcodyssey.alchemy.recipes

import me.shadowalzazel.mcodyssey.alchemy.AlchemyPotions
import me.shadowalzazel.mcodyssey.alchemy.utility.AwkwardPotion
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyAlchemyCauldronRecipe
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


// temp internal
// CRYSTALLINE_POTION
internal object CrystallinePotionRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.CRYSTALLINE_POTION, ItemStack(Material.AMETHYST_SHARD, 2), AwkwardPotion.createAwkwardPotion(), Material.FIRE)

// POTION_OF_LEVITATION_RECIPE
object PotionOfLevitationRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_LEVITATION, ItemStack(Material.SHULKER_SHELL, 1), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.SOUL_FIRE)

// POTION_OF_WITHERING_RECIPE
object PotionOfWitheringRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_WITHERING, ItemStack(Material.WITHER_ROSE, 1), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.SOUL_FIRE)

// POTION_OF_BIOLUMINESCENCE
object PotionOfBioluminescenceRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_BIOLUMINESCENCE, ItemStack(Material.GLOW_BERRIES, 3), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.FIRE)

// POTION_OF_LUCK_RECIPE
object PotionOfLuckRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_LUCK, ItemStack(Material.NAUTILUS_SHELL, 1), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.FIRE)

// POTION_OF_RESISTANCE_RECIPE
object PotionOfResistanceRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_RESISTANCE, ItemStack(Material.DIAMOND, 1), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.FIRE)

// POTION_OF_HASTE_RECIPE
object PotionOfHasteRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_HASTE, ItemStack(Material.PUMPKIN, 1), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.FIRE)

// POTION_OF_DECAY_RECIPE
object PotionOfDecayRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_DECAY, ItemStack(Material.ROTTEN_FLESH, 8), AlchemyPotions.POTION_OF_WITHERING.createItemStack(1), Material.SOUL_FIRE)

// POTION_OF_SHIMMER_RECIPE
object PotionOfShimmerRecipe : OdysseyAlchemyCauldronRecipe(AlchemyPotions.POTION_OF_SHIMMER, ItemStack(Material.END_CRYSTAL, 2), AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1), Material.SOUL_FIRE)