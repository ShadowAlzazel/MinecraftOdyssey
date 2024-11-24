package me.shadowalzazel.mcodyssey.datagen.recipes.smelting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.createGlyphicItem
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice

class Kiln {

    fun getRecipes(): List<Recipe> {
        return listOf(
            runewareFiringRecipe()
        )
    }

    private fun runewareFiringRecipe(): BlastingRecipe {
        val result: ItemStack = Glyphsherds.GLAZED_ORB.createGlyphicItem(1) // TEMP NAME/ITEM
        val input = RecipeChoice.MaterialChoice(
            Material.CLAY_BALL
        )
        val experience = 200.0F
        val time = 20 * 20

        return BlastingRecipe(
            NamespacedKey(Odyssey.instance, "runeware_firing"),
            result,
            input,
            experience,
            time
        )
    }

}