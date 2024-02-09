package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.AwkwardPotion
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Potions
import me.shadowalzazel.mcodyssey.items.Potions.createPotionStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

object CauldronRecipes {

    val fireFuels = listOf(Material.FIRE, Material.CAMPFIRE)
    val soulFireFuels = listOf(Material.SOUL_FIRE, Material.SOUL_CAMPFIRE)

    private val CRYSTALLINE_POTION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.CRYSTALLINE_POTION,
        ingredientSize = 2,
        specificList = listOf(
            ItemStack(Material.AMETHYST_SHARD, 1),
            AwkwardPotion.createAwkwardPotion()),
        viableFuel = fireFuels)

    private val POTION_OF_LEVITATION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_LEVITATION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.SHULKER_SHELL, 1),
            ItemStack(Material.PHANTOM_MEMBRANE, 2),
            ItemStack(Material.FEATHER, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_WITHERING_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_WITHERING,
        ingredientSize = 2,
        specificList = listOf(
            ItemStack(Material.WITHER_ROSE, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_BIOLUMINESCENCE_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_BIOLUMINESCENCE,
        ingredientSize = 3,
        specificList = listOf(
            ItemStack(Material.GLOW_BERRIES, 2),
            ItemStack(Material.GLOW_INK_SAC, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_LUCK_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_LUCK,
        ingredientSize = 2,
        specificList = listOf(
            ItemStack(Material.NAUTILUS_SHELL, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_RESISTANCE_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_RESISTANCE,
        ingredientSize = 2,
        specificList = listOf(
            ItemStack(Material.DIAMOND, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_HASTE_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_HASTE,
        ingredientSize = 3,
        specificList = listOf(
            ItemStack(Material.PUMPKIN, 1),
            ItemStack(Material.SUGAR, 3),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_CONSTITUTION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_CONSTITUTION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 2),
            ItemStack(Material.GLISTERING_MELON_SLICE, 2),
            ItemStack(Material.SOUL_SOIL, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val POTION_OF_STONE_SKIN_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_STONE_SKIN,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.STONE, 2),
            ItemStack(Material.LEATHER, 3),
            ItemStack(Material.CRYING_OBSIDIAN, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val POTION_OF_WRATH_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_WRATH,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.WARPED_FUNGUS, 2),
            Ingredients.COAGULATED_BLOOD.createItemStack(2),
            ItemStack(Material.CRIMSON_FUNGUS, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val POTION_OF_WHIZ_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.POTION_OF_WHIZ,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.SUGAR, 2),
            ItemStack(Material.COCOA_BEANS, 1),
            ItemStack(Material.WARPED_FUNGUS, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    // TODO: POTION WITH ROOTS

    private val FLASK_OF_ROT_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_ROT,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.ROTTEN_FLESH, 3),
            ItemStack(Material.BROWN_MUSHROOM, 1),
            ItemStack(Material.HANGING_ROOTS, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_FROST_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_FROST,
        ingredientSize = 3,
        specificList = listOf(
            ItemStack(Material.BLUE_ICE, 1),
            ItemStack(Material.SNOWBALL, 4),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_TAR_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_TAR,
        ingredientSize = 3,
        specificList = listOf(
            ItemStack(Material.CHARCOAL, 2),
            ItemStack(Material.NETHERRACK, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_ABLAZE_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_ABLAZE,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.BLAZE_POWDER, 2),
            ItemStack(Material.FIRE_CHARGE, 1),
            ItemStack(Material.MAGMA_CREAM, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_IRRADIATION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_IRRADIATION,
        ingredientSize = 4,
        specificList = listOf(
            Ingredients.IRRADIATED_SHARD.createItemStack(1),
            ItemStack(Material.PRISMARINE_CRYSTALS, 1),
            ItemStack(Material.GHAST_TEAR, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_CORROSION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_CORROSION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.CACTUS, 1),
            ItemStack(Material.RAW_COPPER, 2),
            ItemStack(Material.DRAGON_BREATH, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_MIASMA_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.FLASK_OF_MIASMA,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.PUFFERFISH, 1),
            ItemStack(Material.WARPED_FUNGUS, 2),
            ItemStack(Material.FERMENTED_SPIDER_EYE, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val BOTTLED_SOULS_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.BOTTLE_OF_SOULS,
        ingredientSize = 4,
        specificList = listOf(
            Ingredients.ECTOPLASM.createItemStack(1),
            Ingredients.SOUL_QUARTZ.createItemStack(1),
            ItemStack(Material.CRYING_OBSIDIAN, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val ACCURSED_BREW_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.ACCURSED_BREW,
        ingredientSize = 4,
        specificList = listOf(
            Ingredients.ECTOPLASM.createItemStack(1),
            Ingredients.COAGULATED_BLOOD.createItemStack(2),
            ItemStack(Material.ROTTEN_FLESH, 3),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val BOTTLE_OF_SHIMMER_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.BOTTLE_OF_SHIMMER,
        ingredientSize = 5,
        specificList = listOf(
            ItemStack(Material.DIAMOND, 1),
            ItemStack(Material.PRISMARINE_SHARD, 2),
            ItemStack(Material.GLOW_BERRIES, 2),
            ItemStack(Material.AMETHYST_CLUSTER, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val ANGLERS_CONCOCTION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.ANGLERS_CONCOCTION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.KELP, 2),
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.WATER_BREATHING),
        viableFuel = fireFuels)

    private val SPELUNKERS_CONCOCTION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.SPELUNKERS_CONCOCTION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.RAW_GOLD, 2),
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.GLOWING),
        viableFuel = fireFuels)

    private val NETHER_OWL_CONCOCTION_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.NETHER_OWL_CONCOCTION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.WEEPING_VINES, 2),
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.FIRE_RESISTANCE),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val CUSTOM_CONCOCTION_2_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.CUSTOM_CONCOCTION,
        ingredientSize = 3,
        specificList = listOf(
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(),
        viableFuel = fireFuels)

    private val CUSTOM_CONCOCTION_3_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.CUSTOM_CONCOCTION,
        ingredientSize = 4,
        specificList = listOf(
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(),
        viableFuel = fireFuels)

    private val CUSTOM_CONCOCTION_4_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.CUSTOM_CONCOCTION,
        ingredientSize = 5,
        specificList = listOf(
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(),
        viableFuel = fireFuels)

    private val CUSTOM_CONCOCTION_5_RECIPE = AlchemyCauldronRecipe(
        potion = Potions.CUSTOM_CONCOCTION,
        ingredientSize = 6,
        specificList = listOf(
            ItemStack(Material.CHORUS_FLOWER, 1)),
        isCombination = true,
        comboEffectTypeList = listOf(),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/
    val CAULDRON_RECIPE_SET = setOf(
        CRYSTALLINE_POTION_RECIPE,
        POTION_OF_LEVITATION_RECIPE,
        POTION_OF_WITHERING_RECIPE,
        POTION_OF_BIOLUMINESCENCE_RECIPE,
        POTION_OF_LUCK_RECIPE,
        POTION_OF_RESISTANCE_RECIPE,
        POTION_OF_HASTE_RECIPE,
        POTION_OF_CONSTITUTION_RECIPE,
        POTION_OF_STONE_SKIN_RECIPE,
        POTION_OF_WRATH_RECIPE,
        POTION_OF_WHIZ_RECIPE,
        FLASK_OF_ROT_RECIPE,
        FLASK_OF_FROST_RECIPE,
        FLASK_OF_TAR_RECIPE,
        FLASK_OF_ABLAZE_RECIPE,
        FLASK_OF_IRRADIATION_RECIPE,
        FLASK_OF_CORROSION_RECIPE,
        FLASK_OF_MIASMA_RECIPE,
        BOTTLED_SOULS_RECIPE,
        ACCURSED_BREW_RECIPE,
        BOTTLE_OF_SHIMMER_RECIPE,
        ANGLERS_CONCOCTION_RECIPE,
        SPELUNKERS_CONCOCTION_RECIPE,
        NETHER_OWL_CONCOCTION_RECIPE,
        CUSTOM_CONCOCTION_2_RECIPE,
        CUSTOM_CONCOCTION_3_RECIPE,
        CUSTOM_CONCOCTION_4_RECIPE,
        CUSTOM_CONCOCTION_5_RECIPE
    )
}
