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
            ItemStack(Material.GLOW_BERRIES, 3),
            ItemStack(Material.GLOW_INK_SAC, 3),
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
            ItemStack(Material.PUMPKIN, 2),
            ItemStack(Material.SUGAR, 4),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.FIRE)

    private val POTION_OF_CONSTITUTION_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_CONSTITUTION,
        ingredientList = listOf(
            ItemStack(Material.POPPED_CHORUS_FRUIT, 5),
            ItemStack(Material.GLISTERING_MELON_SLICE, 5),
            ItemStack(Material.SOUL_SAND, 5),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val POTION_OF_STONE_SKIN_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_STONE_SKIN,
        ingredientList = listOf(
            ItemStack(Material.STONE, 8),
            ItemStack(Material.LEATHER, 8),
            ItemStack(Material.CRYING_OBSIDIAN, 4),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val POTION_OF_WRATH_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POTION_OF_WRATH,
        ingredientList = listOf(
            ItemStack(Material.WARPED_FUNGUS, 4),
            Ingredients.COAGULATED_BLOOD.createItemStack(4),
            ItemStack(Material.CRIMSON_FUNGUS, 4),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val FLASK_OF_DECAY_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_DECAY,
        ingredientList = listOf(
            ItemStack(Material.ROTTEN_FLESH, 8),
            ItemStack(Material.BROWN_MUSHROOM, 6),
            Potions.POTION_OF_WITHERING.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.SOUL_FIRE)

    private val FLASK_OF_FROST_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_FROST,
        ingredientList = listOf(
            ItemStack(Material.BLUE_ICE, 4),
            ItemStack(Material.SNOWBALL, 8),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.SOUL_FIRE)

    private val FLASK_OF_DOUSE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_DOUSE,
        ingredientList = listOf(
            ItemStack(Material.CHARCOAL, 4),
            ItemStack(Material.NETHERRACK, 4),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 3,
        fireMaterial = Material.FIRE)

    private val POTION_OF_ABLAZE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_ABLAZE,
        ingredientList = listOf(
            ItemStack(Material.BLAZE_POWDER, 8),
            ItemStack(Material.FIRE_CHARGE, 4),
            ItemStack(Material.HONEY_BLOCK, 1),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.FIRE)

    private val FLASK_OF_ROSE_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_ROSE,
        ingredientList = listOf(
            ItemStack(Material.PRISMARINE_SHARD, 6),
            ItemStack(Material.CACTUS, 4),
            ItemStack(Material.ROSE_BUSH, 4),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val FLASK_OF_MIASMA_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.FLASK_OF_MIASMA,
        ingredientList = listOf(
            ItemStack(Material.PUFFERFISH, 3),
            ItemStack(Material.WARPED_FUNGUS, 5),
            ItemStack(Material.FERMENTED_SPIDER_EYE, 5),
            ItemStack(Material.DRAGON_BREATH, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 5,
        fireMaterial = Material.FIRE)

    private val BOTTLED_SOULS_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.BOTTLE_OF_SOULS,
        ingredientList = listOf(
            Ingredients.ECTOPLASM.createItemStack(3),
            Ingredients.SOUL_CRYSTAL.createItemStack(4),
            ItemStack(Material.CRYING_OBSIDIAN, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    private val POTION_OF_SHIMMER_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.BOTTLE_OF_SHIMMER,
        ingredientList = listOf(
            ItemStack(Material.DIAMOND, 2),
            ItemStack(Material.PRISMARINE_SHARD, 6),
            ItemStack(Material.GLOW_BERRIES, 6),
            ItemStack(Material.AMETHYST_CLUSTER, 2),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 5,
        fireMaterial = Material.FIRE)

    private val POLTERGEIST_BREW_RECIPE = AlchemyCauldronRecipe(
        brewedPotion = Potions.POLTERGEIST_BREW,
        ingredientList = listOf(
            Ingredients.ECTOPLASM.createItemStack(2),
            Ingredients.COAGULATED_BLOOD.createItemStack(4),
            ItemStack(Material.ROTTEN_FLESH, 8),
            Potions.CRYSTALLINE_POTION.createPotion()),
        ingredientSize = 4,
        fireMaterial = Material.SOUL_FIRE)

    // Set
    val CAULDRON_SET = setOf(CRYSTALLINE_POTION_RECIPE, POTION_OF_LEVITATION_RECIPE, POTION_OF_WITHERING_RECIPE, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK_RECIPE,
        POTION_OF_CONSTITUTION_RECIPE, POTION_OF_STONE_SKIN_RECIPE, POTION_OF_WRATH_RECIPE, POTION_OF_RESISTANCE_RECIPE, POTION_OF_HASTE_RECIPE, FLASK_OF_DECAY_RECIPE,
        FLASK_OF_DOUSE_RECIPE, POTION_OF_ABLAZE_RECIPE, FLASK_OF_FROST_RECIPE, FLASK_OF_ROSE_RECIPE, FLASK_OF_MIASMA_RECIPE, BOTTLED_SOULS_RECIPE, POLTERGEIST_BREW_RECIPE,
        POTION_OF_SHIMMER_RECIPE)

}