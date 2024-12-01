package me.shadowalzazel.mcodyssey.common.alchemy

import me.shadowalzazel.mcodyssey.common.alchemy.utility.CauldronFuels
import me.shadowalzazel.mcodyssey.common.alchemy.utility.IngredientChoice
import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.Material
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

object CauldronRecipes {

    /*-----------------------------------------------------------------------------------------------*/
    // Singular Potions

    private val CRYSTALLINE_POTION_RECIPE = CauldronAlchemyRecipe(
        result = Item.CRYSTALLINE_POTION.newItemStack(3),
        itemCount = 2,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.MaterialChoice(Material.AMETHYST_SHARD, 1),
            IngredientChoice.PotionTypeChoice(PotionType.AWKWARD)))


    private val POTION_OF_LEVITATION_RECIPE = CauldronAlchemyRecipe(
        result = Item.POTION_OF_LEVITATION.newItemStack(3),
        itemCount = 2,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.PotionTypeChoice(PotionType.AWKWARD),
            IngredientChoice.MaterialChoice(Material.SHULKER_SHELL, 1)))

    /*-----------------------------------------------------------------------------------------------*/
    // Powerful Potions

    private val POTION_OF_STONE_SKIN_RECIPE = CauldronAlchemyRecipe(
        result = Item.POTION_OF_STONE_SKIN.newItemStack(3),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.ItemNameChoice("crystalline_potion"),
            IngredientChoice.MaterialChoice(Material.STONE, 1),
            IngredientChoice.MaterialChoice(Material.LEATHER, 1),
            IngredientChoice.MaterialChoice(Material.ARMADILLO_SCUTE, 1)))


    private val POTION_OF_WRATH_RECIPE = CauldronAlchemyRecipe(
        result = Item.POTION_OF_WRATH.newItemStack(3),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.ItemNameChoice("crystalline_potion"),
            IngredientChoice.ItemNameChoice("coagulated_blood"),
            IngredientChoice.MaterialChoice(Material.CRIMSON_FUNGUS, 1),
            IngredientChoice.MaterialChoice(Material.WARPED_FUNGUS, 1)))


    private val POTION_OF_ZOOM_RECIPE = CauldronAlchemyRecipe(
        result = Item.POTION_OF_ZOOM.newItemStack(3),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.ItemNameChoice("crystalline_potion"),
            IngredientChoice.MaterialChoice(Material.SUGAR, 1),
            IngredientChoice.MaterialChoice(Material.COCOA_BEANS, 1),
            IngredientChoice.MaterialChoice(Material.FEATHER, 1)))


    private val POTION_OF_SHIMMER_RECIPE = CauldronAlchemyRecipe(
        result = Item.POTION_OF_SHIMMER.newItemStack(3),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.ItemNameChoice("crystalline_potion"),
            IngredientChoice.MaterialChoice(Material.DIAMOND, 1),
            IngredientChoice.MaterialChoice(Material.GLOW_BERRIES, 1),
            IngredientChoice.MaterialChoice(Material.AMETHYST_SHARD, 1)))


    /*-----------------------------------------------------------------------------------------------*/
    // Concoctions

    private val ANGLERS_CONCOCTION_RECIPE = CauldronAlchemyRecipe(
        result = Item.ANGLERS_CONCOCTION.newItemStack(1),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.PotionEffectChoice(PotionEffectType.WATER_BREATHING),
            IngredientChoice.PotionEffectChoice(PotionEffectType.NIGHT_VISION),
            IngredientChoice.MaterialChoice(Material.KELP, 1),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 1)),
        isCombination = true)


    private val SPELUNKERS_CONCOCTION_RECIPE = CauldronAlchemyRecipe(
        result = Item.SPELUNKERS_CONCOCTION.newItemStack(1),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.PotionEffectChoice(PotionEffectType.HASTE),
            IngredientChoice.PotionEffectChoice(PotionEffectType.GLOWING),
            IngredientChoice.MaterialChoice(Material.RAW_GOLD, 1),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 1)),
        isCombination = true)


    private val NETHER_OWL_CONCOCTION_RECIPE = CauldronAlchemyRecipe(
        result = Item.NETHER_OWL_CONCOCTION.newItemStack(1),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.PotionEffectChoice(PotionEffectType.FIRE_RESISTANCE),
            IngredientChoice.PotionEffectChoice(PotionEffectType.NIGHT_VISION),
            IngredientChoice.MaterialChoice(Material.WEEPING_VINES, 1),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 1)),
        isCombination = true)

    // Custom Concoctions
    private val CUSTOM_CONCOCTION_2_RECIPE = CauldronAlchemyRecipe(
        result = Item.CUSTOM_CONCOCTION.newItemStack(1),
        itemCount = 3,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 2)),
        isCombination = true)

    // Custom Concoctions
    private val CUSTOM_CONCOCTION_3_RECIPE = CauldronAlchemyRecipe(
        result = Item.CUSTOM_CONCOCTION.newItemStack(1),
        itemCount = 4,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 3)),
        isCombination = true)

    // Custom Concoctions
    private val CUSTOM_CONCOCTION_4_RECIPE = CauldronAlchemyRecipe(
        result = Item.CUSTOM_CONCOCTION.newItemStack(1),
        itemCount = 5,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 4)),
        isCombination = true)

    // Custom Concoctions
    private val CUSTOM_CONCOCTION_5_RECIPE = CauldronAlchemyRecipe(
        result = Item.CUSTOM_CONCOCTION.newItemStack(1),
        itemCount = 6,
        fuels = CauldronFuels.FIRE_FUELS,
        ingredients = listOf(
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.AnyEffectChoice(),
            IngredientChoice.MaterialChoice(Material.POPPED_CHORUS_FRUIT, 5)),
        isCombination = true)


    /*-----------------------------------------------------------------------------------------------*/

    val allRecipes = setOf(
        CRYSTALLINE_POTION_RECIPE,
        POTION_OF_LEVITATION_RECIPE,
        POTION_OF_STONE_SKIN_RECIPE,
        POTION_OF_WRATH_RECIPE,
        POTION_OF_ZOOM_RECIPE,
        POTION_OF_SHIMMER_RECIPE,
        ANGLERS_CONCOCTION_RECIPE,
        NETHER_OWL_CONCOCTION_RECIPE,
        SPELUNKERS_CONCOCTION_RECIPE,
        CUSTOM_CONCOCTION_2_RECIPE,
        CUSTOM_CONCOCTION_3_RECIPE,
        CUSTOM_CONCOCTION_4_RECIPE,
        CUSTOM_CONCOCTION_5_RECIPE
    )

}