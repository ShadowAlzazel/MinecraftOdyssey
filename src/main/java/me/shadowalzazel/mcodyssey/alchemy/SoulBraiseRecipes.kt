package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.base.SoulBraiseRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.BraiseBase
import me.shadowalzazel.mcodyssey.common.items.custom.Ingredients
import me.shadowalzazel.mcodyssey.common.items.custom.Miscellaneous
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object SoulBraiseRecipes {

    // Item results
    private val SOUL_CRYSTAL_RECIPE = SoulBraiseRecipe(
        itemResult = Ingredients.SOUL_QUARTZ,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.QUARTZ, 1)),
        braiseMaterial = setOf(Material.SCULK),
        BraiseBase.PLUS)

    val SOUL_STEEL_RECIPE = SoulBraiseRecipe(
        itemResult = Ingredients.SOUL_STEEL_INGOT,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.RAW_IRON, 1)),
        braiseMaterial = setOf(Material.SCULK),
        BraiseBase.PLUS)

    private val SCULK_POINTER_RECIPE = SoulBraiseRecipe(
        itemResult = Miscellaneous.SCULK_POINTER,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.RECOVERY_COMPASS, 1)),
        braiseMaterial = setOf(Material.CALIBRATED_SCULK_SENSOR),
        BraiseBase.PLUS)

    private val SCULK_POINTER_RECALIBRATE_RECIPE = SoulBraiseRecipe(
        itemResult = Miscellaneous.SCULK_POINTER,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.COMPASS, 1)),
        braiseMaterial = setOf(Material.CALIBRATED_SCULK_SENSOR),
        BraiseBase.PLUS)

    // TODO: WIP
    private val SCULK_HEART_RECIPE = SoulBraiseRecipe(
        itemResult = Miscellaneous.SCULK_HEART,
        mobSummon = null,
        braiseIngredients = listOf(Ingredients.WARDEN_ENTRAILS.newItemStack(1)),
        braiseMaterial = setOf(Material.SCULK_CATALYST),
        BraiseBase.PLUS)

    // TODO: WIP
    private val CRYING_GOLD_RECIPE = SoulBraiseRecipe(
        itemResult = Miscellaneous.CRYING_GOLD,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.RAW_GOLD, 1)),
        braiseMaterial = setOf(Material.CRYING_OBSIDIAN),
        BraiseBase.PLUS)

    // Summon Results
    private val ROTTING_SUMMON_RECIPE = SoulBraiseRecipe(
        itemResult = Ingredients.SOUL_QUARTZ,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.ROTTEN_FLESH, 1)),
        braiseMaterial = setOf(Material.BONE_BLOCK),
        BraiseBase.PLUS)

    // Set
    val BRAISE_SET = setOf(
        SOUL_CRYSTAL_RECIPE,
        SOUL_STEEL_RECIPE,
        SCULK_POINTER_RECIPE,
        SCULK_POINTER_RECALIBRATE_RECIPE,

        SCULK_HEART_RECIPE,
        CRYING_GOLD_RECIPE,
    )

}