package me.shadowalzazel.mcodyssey.common.alchemy

import me.shadowalzazel.mcodyssey.common.alchemy.utility.IngredientChoice
import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.Material

object EngulfingRecipes {

    private val SOUL_CRYSTAL_RECIPE = EngulfingRecipe(
        result = Item.SOUL_QUARTZ.newItemStack(1),
        fuel = listOf(Material.SOUL_FIRE),
        ingredient = IngredientChoice.MaterialChoice(Material.QUARTZ, 1),
        engulfment = setOf(Material.SCULK))


    val SOUL_STEEL_INGOT_RECIPE = EngulfingRecipe(
        result = Item.SOUL_STEEL_INGOT.newItemStack(1),
        fuel = listOf(Material.SOUL_FIRE),
        ingredient = IngredientChoice.MaterialChoice(Material.RAW_IRON, 1),
        engulfment = setOf(Material.SCULK))


    private val SCULK_POINTER_RECIPE = EngulfingRecipe(
        result = Item.SCULK_POINTER.newItemStack(1),
        fuel = listOf(Material.SOUL_FIRE),
        ingredient = IngredientChoice.MaterialChoice(Material.RECOVERY_COMPASS, 1),
        engulfment = setOf(Material.CALIBRATED_SCULK_SENSOR))


    private val SCULK_HEART_RECIPE = EngulfingRecipe(
        result = Item.SCULK_HEART.newItemStack(1),
        fuel = listOf(Material.SOUL_FIRE),
        ingredient = IngredientChoice.ItemNameChoice("sculk_entrails", 1),
        engulfment = setOf(Material.SCULK_CATALYST))


    private val ECTOPLASM_RECIPE = EngulfingRecipe(
        result = Item.ECTOPLASM.newItemStack(1),
        fuel = listOf(Material.SOUL_FIRE),
        ingredient = IngredientChoice.MaterialChoice(Material.BONE, 1),
        engulfment = setOf(Material.SOUL_SAND))

    /*-----------------------------------------------------------------------------------------------*/

    val allRecipes = setOf(
        SOUL_CRYSTAL_RECIPE,
        SOUL_STEEL_INGOT_RECIPE,
        SCULK_POINTER_RECIPE,
        SCULK_HEART_RECIPE,
        ECTOPLASM_RECIPE
    )


}