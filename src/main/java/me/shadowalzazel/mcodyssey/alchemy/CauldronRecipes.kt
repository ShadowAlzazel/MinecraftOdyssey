package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.AwkwardPotion
import me.shadowalzazel.mcodyssey.common.items.custom.Ingredients
import me.shadowalzazel.mcodyssey.common.items.custom.Potions
import me.shadowalzazel.mcodyssey.common.items.custom.Potions.createPotionStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

object CauldronRecipes {

    val fireFuels = listOf(Material.FIRE, Material.CAMPFIRE)
    val soulFireFuels = listOf(Material.SOUL_FIRE, Material.SOUL_CAMPFIRE)

    private val CRYSTALLINE_POTION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.CRYSTALLINE_POTION,
        countedItems = 2,
        specificIngredients = listOf(
            ItemStack(Material.AMETHYST_SHARD, 1),
            AwkwardPotion.createAwkwardPotion()),
        viableFuel = fireFuels)

    private val POTION_OF_LEVITATION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_LEVITATION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.SHULKER_SHELL, 1),
            ItemStack(Material.PHANTOM_MEMBRANE, 2),
            ItemStack(Material.FEATHER, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_WITHERING_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_WITHERING,
        countedItems = 2,
        specificIngredients = listOf(
            ItemStack(Material.WITHER_ROSE, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_BIOLUMINESCENCE_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_BIOLUMINESCENCE,
        countedItems = 3,
        specificIngredients = listOf(
            ItemStack(Material.GLOW_BERRIES, 2),
            ItemStack(Material.GLOW_INK_SAC, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_LUCK_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_LUCK,
        countedItems = 2,
        specificIngredients = listOf(
            ItemStack(Material.NAUTILUS_SHELL, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_RESISTANCE_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_RESISTANCE,
        countedItems = 2,
        specificIngredients = listOf(
            ItemStack(Material.DIAMOND, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_HASTE_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_HASTE,
        countedItems = 3,
        specificIngredients = listOf(
            ItemStack(Material.PUMPKIN, 1),
            ItemStack(Material.SUGAR, 3),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    private val POTION_OF_CONSTITUTION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_CONSTITUTION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 2),
            ItemStack(Material.GLISTERING_MELON_SLICE, 2),
            ItemStack(Material.SOUL_SOIL, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val POTION_OF_STONE_SKIN_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_STONE_SKIN,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.STONE, 2),
            ItemStack(Material.LEATHER, 3),
            ItemStack(Material.CRYING_OBSIDIAN, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val POTION_OF_WRATH_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_WRATH,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.WARPED_FUNGUS, 2),
            Ingredients.COAGULATED_BLOOD.newItemStack(2),
            ItemStack(Material.CRIMSON_FUNGUS, 2),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val POTION_OF_WHIZ_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.POTION_OF_WHIZ,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.SUGAR, 2),
            ItemStack(Material.COCOA_BEANS, 1),
            ItemStack(Material.WARPED_FUNGUS, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    // TODO: POTION WITH ROOTS

    private val FLASK_OF_ROT_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.FLASK_OF_ROT,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.ROTTEN_FLESH, 3),
            ItemStack(Material.BROWN_MUSHROOM, 1),
            ItemStack(Material.HANGING_ROOTS, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_FROST_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.FLASK_OF_FROST,
        countedItems = 3,
        specificIngredients = listOf(
            ItemStack(Material.BLUE_ICE, 1),
            ItemStack(Material.SNOWBALL, 4),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_ABLAZE_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.FLASK_OF_ABLAZE,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.BLAZE_POWDER, 2),
            ItemStack(Material.FIRE_CHARGE, 1),
            ItemStack(Material.MAGMA_CREAM, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_IRRADIATION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.FLASK_OF_IRRADIATION,
        countedItems = 4,
        specificIngredients = listOf(
            Ingredients.IRRADIATED_SHARD.newItemStack(1),
            ItemStack(Material.PRISMARINE_CRYSTALS, 1),
            ItemStack(Material.GHAST_TEAR, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_CORROSION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.FLASK_OF_CORROSION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.CACTUS, 1),
            ItemStack(Material.RAW_COPPER, 2),
            ItemStack(Material.DRAGON_BREATH, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val FLASK_OF_MIASMA_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.FLASK_OF_MIASMA,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.PUFFERFISH, 1),
            ItemStack(Material.WARPED_FUNGUS, 2),
            ItemStack(Material.FERMENTED_SPIDER_EYE, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val BOTTLED_SOULS_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.BOTTLE_OF_SOULS,
        countedItems = 4,
        specificIngredients = listOf(
            Ingredients.ECTOPLASM.newItemStack(1),
            Ingredients.SOUL_QUARTZ.newItemStack(1),
            ItemStack(Material.CRYING_OBSIDIAN, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    private val ACCURSED_BREW_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.ACCURSED_BREW,
        countedItems = 4,
        specificIngredients = listOf(
            Ingredients.ECTOPLASM.newItemStack(1),
            Ingredients.COAGULATED_BLOOD.newItemStack(2),
            ItemStack(Material.ROTTEN_FLESH, 3),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val BOTTLE_OF_SHIMMER_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.BOTTLE_OF_SHIMMER,
        countedItems = 5,
        specificIngredients = listOf(
            ItemStack(Material.DIAMOND, 1),
            ItemStack(Material.PRISMARINE_SHARD, 2),
            ItemStack(Material.GLOW_BERRIES, 2),
            ItemStack(Material.AMETHYST_CLUSTER, 1),
            Potions.CRYSTALLINE_POTION.createPotionStack()),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val ANGLERS_CONCOCTION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.ANGLERS_CONCOCTION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.KELP, 2),
            ItemStack(Material.POPPED_CHORUS_FRUIT, 1)),
        isCombination = true,
        combinationEffects = listOf(
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.WATER_BREATHING),
        viableFuel = soulFireFuels)

    private val SPELUNKERS_CONCOCTION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.SPELUNKERS_CONCOCTION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.RAW_GOLD, 2),
            ItemStack(Material.POPPED_CHORUS_FRUIT, 1)),
        isCombination = true,
        combinationEffects = listOf(
            PotionEffectType.HASTE,
            PotionEffectType.GLOWING),
        viableFuel = soulFireFuels)

    private val NETHER_OWL_CONCOCTION_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.NETHER_OWL_CONCOCTION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.WEEPING_VINES, 2),
            ItemStack(Material.POPPED_CHORUS_FRUIT, 1)),
        isCombination = true,
        combinationEffects = listOf(
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.FIRE_RESISTANCE),
        viableFuel = soulFireFuels)

    /*-----------------------------------------------------------------------------------------------*/

    private val CUSTOM_CONCOCTION_2_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.CUSTOM_CONCOCTION,
        countedItems = 3,
        specificIngredients = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 2)),
        isCombination = true,
        combinationEffects = listOf(),
        viableFuel = fireFuels)

    private val CUSTOM_CONCOCTION_3_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.CUSTOM_CONCOCTION,
        countedItems = 4,
        specificIngredients = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 3)),
        isCombination = true,
        combinationEffects = listOf(),
        viableFuel = fireFuels)

    private val CUSTOM_CONCOCTION_4_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.CUSTOM_CONCOCTION,
        countedItems = 5,
        specificIngredients = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 4)),
        isCombination = true,
        combinationEffects = listOf(),
        viableFuel = fireFuels)

    private val CUSTOM_CONCOCTION_5_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.CUSTOM_CONCOCTION,
        countedItems = 6,
        specificIngredients = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 5)),
        isCombination = true,
        combinationEffects = listOf(),
        viableFuel = fireFuels)

    /*-----------------------------------------------------------------------------------------------*/
    private val CUSTOM_FOOD_EFFECT_RECIPE = AlchemyCauldronRecipe(
        alchemyResult = Potions.CUSTOM_CONCOCTION,
        countedItems = 2,
        specificIngredients = listOf(
            ItemStack(Material.HONEYCOMB, 1)),
        isCombination = true,
        combinationEffects = listOf(),
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
        CUSTOM_CONCOCTION_5_RECIPE,
        CUSTOM_FOOD_EFFECT_RECIPE,
    )
}
