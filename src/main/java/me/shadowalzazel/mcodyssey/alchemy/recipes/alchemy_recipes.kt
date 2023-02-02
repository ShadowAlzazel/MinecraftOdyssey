 package me.shadowalzazel.mcodyssey.alchemy.recipes

import me.shadowalzazel.mcodyssey.items.OdysseyPotions
import me.shadowalzazel.mcodyssey.alchemy.base.AwkwardPotion
import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// CRYSTALLINE_POTION_RECIPE
object CrystallinePotionRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.CRYSTALLINE_POTION,
    listOf(
        ItemStack(Material.AMETHYST_SHARD, 1),
        AwkwardPotion.createAwkwardPotion()),
    2, Material.FIRE)

// POTION_OF_LEVITATION_RECIPE
object PotionOfLevitationRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_LEVITATION,
    listOf(
        ItemStack(Material.SHULKER_SHELL, 1),
        ItemStack(Material.PHANTOM_MEMBRANE, 2),
        ItemStack(Material.FEATHER, 2),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_WITHERING_RECIPE
object PotionOfWitheringRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_WITHERING,
    listOf(
        ItemStack(Material.WITHER_ROSE, 1),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    2, Material.SOUL_FIRE)

// POTION_OF_BIOLUMINESCENCE
object PotionOfBioluminescenceRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_BIOLUMINESCENCE,
    listOf(
        ItemStack(Material.GLOW_BERRIES, 3),
        ItemStack(Material.GLOW_INK_SAC, 3),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// POTION_OF_LUCK_RECIPE
object PotionOfLuckRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_LUCK,
    listOf(
        ItemStack(Material.NAUTILUS_SHELL, 1),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    2, Material.FIRE)

// POTION_OF_RESISTANCE_RECIPE
object PotionOfResistanceRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_RESISTANCE, listOf(
    ItemStack(Material.DIAMOND, 1),
    OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    2, Material.FIRE)

// POTION_OF_HASTE_RECIPE
object PotionOfHasteRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_HASTE,
    listOf(
        ItemStack(Material.PUMPKIN, 2),
        ItemStack(Material.SUGAR, 4),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// POTION_OF_CONSTITUTION_RECIPE
object PotionOfConstitutionRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_CONSTITUTION,
    listOf(
        ItemStack(Material.POPPED_CHORUS_FRUIT, 5),
        ItemStack(Material.GLISTERING_MELON_SLICE, 5),
        ItemStack(Material.SOUL_SAND, 5),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_STONE_SKIN_RECIPE
object PotionOfStoneSkinRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_STONE_SKIN,
    listOf(
        ItemStack(Material.STONE, 8),
        ItemStack(Material.LEATHER, 8),
        ItemStack(Material.CRYING_OBSIDIAN, 4),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.FIRE)

// POTION_OF_WRATH_RECIPE
object PotionOfWrathRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_WRATH,
    listOf(
        ItemStack(Material.WARPED_FUNGUS, 4),
        OdysseyItems.COAGULATED_BLOOD.createItemStack(4),
        ItemStack(Material.CRIMSON_FUNGUS, 4),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_DECAY_RECIPE
object PotionOfDecayRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_DECAY,
    listOf(
        ItemStack(Material.ROTTEN_FLESH, 8),
        OdysseyPotions.POTION_OF_WITHERING.createItemStack(1)),
    2, Material.SOUL_FIRE)

// POTION_OF_FROST_RECIPE
object PotionOfFrostRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_FROST,
    listOf(
        ItemStack(Material.BLUE_ICE, 4),
        ItemStack(Material.SNOWBALL, 8),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.SOUL_FIRE)

// POTION_OF_DOUSE_RECIPE
object PotionOfDouseRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_DOUSE,
    listOf(
        ItemStack(Material.CHARCOAL, 4),
        ItemStack(Material.NETHERRACK, 4),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// POTION_OF_ABLAZE_RECIPE
object PotionOfAblazeRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_ABLAZE,
    listOf(
        ItemStack(Material.BLAZE_POWDER, 8),
        ItemStack(Material.FIRE_CHARGE, 4),
        ItemStack(Material.HONEY_BLOCK, 1),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.FIRE)

// POTION_OF_THORNS_RECIPE
object PotionOfThornsRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_THORNS,
    listOf(
        ItemStack(Material.PRISMARINE_SHARD, 6),
        ItemStack(Material.CACTUS, 4),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// PUFFY_PRICKLY_POTION_RECIPE
object PuffyPricklyPotionRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.PUFFY_PRICKLY_POTION,
    listOf(
        ItemStack(Material.PUFFERFISH, 2),
        ItemStack(Material.HONEY_BLOCK, 2),
        OdysseyPotions.POTION_OF_THORNS.createItemStack(1)),
    3, Material.SOUL_FIRE)

 // PUFFED_MIASMA_POTION_RECIPE
 object PuffedMiasmaPotionRecipe : AlchemyCauldronRecipe(
     OdysseyPotions.PUFFY_PRICKLY_POTION,
     listOf(
         ItemStack(Material.PUFFERFISH, 5),
         ItemStack(Material.WARPED_FUNGUS, 5),
         ItemStack(Material.FERMENTED_SPIDER_EYE, 5),
         ItemStack(Material.SPONGE, 3),
         ItemStack(Material.HONEY_BLOCK, 3),
         OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
     6, Material.SOUL_FIRE)

// BOTTLED_SOULS_RECIPE
object BottledSoulsRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.BOTTLED_SOULS,
    listOf(
        OdysseyItems.ECTOPLASM.createItemStack(3),
        OdysseyItems.SOUL_CRYSTAL.createItemStack(4),
        ItemStack(Material.CRYING_OBSIDIAN, 2),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POLTERGEIST_BREW_RECIPE
object PoltergeistBrewRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POLTERGEIST_BREW,
    listOf(
        OdysseyItems.ECTOPLASM.createItemStack(2),
        OdysseyItems.COAGULATED_BLOOD.createItemStack(4),
        ItemStack(Material.ROTTEN_FLESH, 8),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_SHIMMER_RECIPE
object PotionOfShimmerRecipe : AlchemyCauldronRecipe(
    OdysseyPotions.POTION_OF_SHIMMER,
    listOf(
        ItemStack(Material.DIAMOND, 2),
        ItemStack(Material.PRISMARINE_SHARD, 6),
        ItemStack(Material.GLOW_BERRIES, 6),
        ItemStack(Material.AMETHYST_CLUSTER, 2),
        OdysseyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    5, Material.FIRE)