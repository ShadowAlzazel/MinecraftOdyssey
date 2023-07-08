package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.alchemy.base.SoulBraiseRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.BraiseBase
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object SoulBraiseRecipes {

    // Item results
    private val SOUL_CRYSTAL_RECIPE = SoulBraiseRecipe(
        itemResult = Ingredients.SOUL_CRYSTAL,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.QUARTZ, 1)),
        braiseMaterial = setOf(Material.SCULK),
        BraiseBase.PLUS)

    private val SOUL_STEEL_RECIPE = SoulBraiseRecipe(
        itemResult = Ingredients.SOUL_STEEL_INGOT,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.RAW_IRON, 1)),
        braiseMaterial = setOf(Material.SCULK),
        BraiseBase.PLUS)

    private val SCULK_HEART_RECIPE = SoulBraiseRecipe(
        itemResult = Miscellaneous.SCULK_HEART,
        mobSummon = null,
        braiseIngredients = listOf(Ingredients.WARDEN_ENTRAILS.createItemStack(1)),
        braiseMaterial = setOf(Material.SCULK_CATALYST),
        BraiseBase.PLUS)

    // Summon Results
    private val ROTTING_SUMMON_RECIPE = SoulBraiseRecipe(
        itemResult = Ingredients.SOUL_CRYSTAL,
        mobSummon = null,
        braiseIngredients = listOf(ItemStack(Material.ROTTEN_FLESH, 1)),
        braiseMaterial = setOf(Material.BONE_BLOCK),
        BraiseBase.PLUS)

    // Set
    val BRAISE_SET = setOf(
        SOUL_CRYSTAL_RECIPE,
        SOUL_STEEL_RECIPE,
        SCULK_HEART_RECIPE,
    )

}