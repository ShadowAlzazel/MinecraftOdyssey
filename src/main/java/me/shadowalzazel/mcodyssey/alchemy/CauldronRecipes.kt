package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.AwkwardPotion
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Potions
import me.shadowalzazel.mcodyssey.items.Potions.createPotion
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object CauldronRecipes {

    // Potion Results
    private val CRYSTALLINE_POTION_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.CRYSTALLINE_POTION,
        ingredientList =  listOf(
            ItemStack(Material.AMETHYST_SHARD, 1),
            AwkwardPotion.createAwkwardPotion()),
        ingredientSize =  2,
        fireMaterial = Material.FIRE)

    private val POTION_OF_LEVITATION_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_LEVITATION,
        ingredientList = listOf(
            ItemStack(Material.SHULKER_SHELL, 1),
            ItemStack(Material.PHANTOM_MEMBRANE, 2),
            ItemStack(Material.FEATHER, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val POTION_OF_WITHERING_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_WITHERING,
        ingredientList = listOf(
            ItemStack(Material.WITHER_ROSE, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 2,
        fireMaterial = Material.SOUL_FIRE)

    private val POTION_OF_BIOLUMINESCENCE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_BIOLUMINESCENCE,
        ingredientList = listOf(
            ItemStack(Material.GLOW_BERRIES, 2),
            ItemStack(Material.GLOW_INK_SAC, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.FIRE)

    private val POTION_OF_LUCK_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_LUCK,
        ingredientList = listOf(
            ItemStack(Material.NAUTILUS_SHELL, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 2,
        fireMaterial = Material.FIRE)

    private val POTION_OF_RESISTANCE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_RESISTANCE,
        ingredientList = listOf(
            ItemStack(Material.DIAMOND, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 2,
        fireMaterial = Material.FIRE)

    private val POTION_OF_HASTE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_HASTE,
        ingredientList = listOf(
            ItemStack(Material.PUMPKIN, 1),
            ItemStack(Material.SUGAR, 3),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.FIRE)

    private val POTION_OF_CONSTITUTION_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_CONSTITUTION,
        ingredientList = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 2),
            ItemStack(Material.GLISTERING_MELON_SLICE, 2),
            ItemStack(Material.SOUL_SOIL, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val POTION_OF_STONE_SKIN_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_STONE_SKIN,
        ingredientList = listOf(
            ItemStack(Material.STONE, 2),
            ItemStack(Material.LEATHER, 3),
            ItemStack(Material.CRYING_OBSIDIAN, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val POTION_OF_WRATH_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_WRATH,
        ingredientList = listOf(
            ItemStack(Material.WARPED_FUNGUS, 2),
            Ingredients.COAGULATED_BLOOD.createItemStack(2),
            ItemStack(Material.CRIMSON_FUNGUS, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val POTION_OF_WHIZ_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_WHIZ,
        ingredientList = listOf(
            ItemStack(Material.SUGAR, 2),
            ItemStack(Material.COCOA_BEANS, 1),
            ItemStack(Material.WARPED_FUNGUS, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val FLASK_OF_DECAY_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_DECAY,
        ingredientList = listOf(
            ItemStack(Material.ROTTEN_FLESH, 3),
            ItemStack(Material.BROWN_MUSHROOM, 2),
            Potions.POTION_OF_WITHERING.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.SOUL_FIRE)

    private val FLASK_OF_FROST_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_FROST,
        ingredientList = listOf(
            ItemStack(Material.BLUE_ICE, 1),
            ItemStack(Material.SNOWBALL, 4),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.SOUL_FIRE)

    private val FLASK_OF_DOUSE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_DOUSE,
        ingredientList = listOf(
            ItemStack(Material.CHARCOAL, 2),
            ItemStack(Material.NETHERRACK, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.FIRE)

    private val POTION_OF_ABLAZE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_ABLAZE,
        ingredientList = listOf(
            ItemStack(Material.BLAZE_POWDER, 3),
            ItemStack(Material.FIRE_CHARGE, 1),
            ItemStack(Material.HONEYCOMB, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val FLASK_OF_ROSE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_ROSE,
        ingredientList = listOf(
            ItemStack(Material.PRISMARINE_SHARD, 2),
            ItemStack(Material.CACTUS, 2),
            ItemStack(Material.ROSE_BUSH, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val FLASK_OF_MIASMA_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_MIASMA,
        ingredientList = listOf(
            ItemStack(Material.PUFFERFISH, 1),
            ItemStack(Material.WARPED_FUNGUS, 2),
            ItemStack(Material.FERMENTED_SPIDER_EYE, 1),
            ItemStack(Material.DRAGON_BREATH, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 5,
        fireMaterial = Material.FIRE)

    private val BOTTLED_SOULS_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.BOTTLE_OF_SOULS,
        ingredientList = listOf(
            Ingredients.ECTOPLASM.createItemStack(2),
            Ingredients.SOUL_CRYSTAL.createItemStack(2),
            ItemStack(Material.CRYING_OBSIDIAN, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val POTION_OF_SHIMMER_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.BOTTLE_OF_SHIMMER,
        ingredientList = listOf(
            ItemStack(Material.DIAMOND, 1),
            ItemStack(Material.PRISMARINE_SHARD, 2),
            ItemStack(Material.GLOW_BERRIES, 2),
            ItemStack(Material.AMETHYST_CLUSTER, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 5,
        fireMaterial = Material.FIRE)

    private val POLTERGEIST_BREW_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POLTERGEIST_BREW,
        ingredientList = listOf(
            Ingredients.ECTOPLASM.createItemStack(1),
            Ingredients.COAGULATED_BLOOD.createItemStack(2),
            ItemStack(Material.ROTTEN_FLESH, 3),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    // Set
    val CAULDRON_SET = setOf(CRYSTALLINE_POTION_RECIPE, POTION_OF_LEVITATION_RECIPE, POTION_OF_WITHERING_RECIPE, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK_RECIPE,
        POTION_OF_CONSTITUTION_RECIPE, POTION_OF_STONE_SKIN_RECIPE, POTION_OF_WRATH_RECIPE, POTION_OF_RESISTANCE_RECIPE, POTION_OF_HASTE_RECIPE, POTION_OF_WHIZ_RECIPE,
        FLASK_OF_DECAY_RECIPE,
        FLASK_OF_DOUSE_RECIPE, POTION_OF_ABLAZE_RECIPE, FLASK_OF_FROST_RECIPE, FLASK_OF_ROSE_RECIPE, FLASK_OF_MIASMA_RECIPE, BOTTLED_SOULS_RECIPE, POLTERGEIST_BREW_RECIPE,
        POTION_OF_SHIMMER_RECIPE)

}