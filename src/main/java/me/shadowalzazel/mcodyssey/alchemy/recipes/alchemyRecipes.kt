package me.shadowalzazel.mcodyssey.alchemy.recipes

import me.shadowalzazel.mcodyssey.alchemy.AlchemyPotions
import me.shadowalzazel.mcodyssey.alchemy.utility.AwkwardPotion
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyAlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


// temp internal
// CRYSTALLINE_POTION
object CrystallinePotionRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.CRYSTALLINE_POTION,
    listOf(
        ItemStack(Material.AMETHYST_SHARD, 1),
        AwkwardPotion.createAwkwardPotion()),
    2, Material.FIRE)

// POTION_OF_LEVITATION_RECIPE
object PotionOfLevitationRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_LEVITATION,
    listOf(
        ItemStack(Material.SHULKER_SHELL, 1),
        ItemStack(Material.PHANTOM_MEMBRANE, 2),
        ItemStack(Material.FEATHER, 2),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_WITHERING_RECIPE
object PotionOfWitheringRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_WITHERING,
    listOf(
        ItemStack(Material.WITHER_ROSE, 1),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    2, Material.SOUL_FIRE)

// POTION_OF_BIOLUMINESCENCE
object PotionOfBioluminescenceRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_BIOLUMINESCENCE,
    listOf(
        ItemStack(Material.GLOW_BERRIES, 3),
        ItemStack(Material.GLOW_INK_SAC, 3),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// POTION_OF_LUCK_RECIPE
object PotionOfLuckRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_LUCK,
    listOf(
        ItemStack(Material.NAUTILUS_SHELL, 1),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    2, Material.FIRE)

// POTION_OF_RESISTANCE_RECIPE
object PotionOfResistanceRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_RESISTANCE, listOf(
    ItemStack(Material.DIAMOND, 1),
    AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    2, Material.FIRE)

// POTION_OF_HASTE_RECIPE
object PotionOfHasteRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_HASTE,
    listOf(
        ItemStack(Material.PUMPKIN, 2),
        ItemStack(Material.SUGAR, 4),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// POTION_OF_CONSTITUTION_RECIPE
object PotionOfConstitutionRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_CONSTITUTION,
    listOf(
        ItemStack(Material.POPPED_CHORUS_FRUIT, 5),
        ItemStack(Material.GLISTERING_MELON_SLICE, 5),
        ItemStack(Material.SOUL_SAND, 5),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_STONE_SKIN_RECIPE
object PotionOfStoneSkinRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_STONE_SKIN,
    listOf(
        ItemStack(Material.STONE, 8),
        ItemStack(Material.LEATHER, 8),
        ItemStack(Material.CRYING_OBSIDIAN, 4),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.FIRE)

// POTION_OF_WRATH_RECIPE
object PotionOfWrathRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_WRATH,
    listOf(
        ItemStack(Material.WARPED_FUNGUS, 6),
        ItemStack(Material.BLAZE_POWDER, 5),
        ItemStack(Material.CRIMSON_FUNGUS, 6),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_DECAY_RECIPE
object PotionOfDecayRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_DECAY,
    listOf(
        ItemStack(Material.ROTTEN_FLESH, 8),
        AlchemyPotions.POTION_OF_WITHERING.createItemStack(1)),
    2, Material.SOUL_FIRE)

// POTION_OF_FROST_RECIPE
object PotionOfFrostRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_FROST,
    listOf(
        ItemStack(Material.BLUE_ICE, 4),
        ItemStack(Material.SNOWBALL, 8),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.SOUL_FIRE)

// POTION_OF_DOUSE_RECIPE
object PotionOfDouseRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_DOUSE,
    listOf(
        ItemStack(Material.CHARCOAL, 4),
        ItemStack(Material.NETHERRACK, 4),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// POTION_OF_ABLAZE_RECIPE
object PotionOfAblazeRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_ABLAZE,
    listOf(
        ItemStack(Material.BLAZE_POWDER, 8),
        ItemStack(Material.FIRE_CHARGE, 4),
        ItemStack(Material.HONEY_BLOCK, 1),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.FIRE)

// POTION_OF_THORNS_RECIPE
object PotionOfThornsRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_THORNS,
    listOf(
        ItemStack(Material.PRISMARINE_SHARD, 6),
        ItemStack(Material.CACTUS, 4),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    3, Material.FIRE)

// PUFFY_PRICKLY_POTION_RECIPE
object PuffyPricklyPotionRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.PUFFY_PRICKLY_POTION,
    listOf(
        ItemStack(Material.PUFFERFISH, 2),
        ItemStack(Material.YELLOW_WOOL, 2),
        AlchemyPotions.POTION_OF_THORNS.createItemStack(1)),
    3, Material.SOUL_FIRE)

// BOTTLED_SOULS_RECIPE
object BottledSoulsRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.BOTTLED_SOULS,
    listOf(
        OdysseyItems.ECTOPLASM.createItemStack(3),
        OdysseyItems.SOUL_CRYSTAL.createItemStack(4),
        ItemStack(Material.CRYING_OBSIDIAN, 4),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    4, Material.SOUL_FIRE)

// POTION_OF_SHIMMER_RECIPE
object PotionOfShimmerRecipe : OdysseyAlchemyCauldronRecipe(
    AlchemyPotions.POTION_OF_SHIMMER,
    listOf(
        ItemStack(Material.DIAMOND, 2),
        ItemStack(Material.PRISMARINE_SHARD, 6),
        ItemStack(Material.GLOW_BERRIES, 6),
        ItemStack(Material.AMETHYST_CLUSTER, 2),
        AlchemyPotions.CRYSTALLINE_POTION.createItemStack(1)),
    5, Material.FIRE)