package me.shadowalzazel.mcodyssey.recipe_creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Templates
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*

class Smithing {

    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            templateTestRecipe()
        )
    }


    // SHAPED
    private fun templateTestRecipe(): ShapedRecipe {
        val someResult = Templates.KATANA_TEMPLATE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "templatetestrecipe"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.DRAGON_EGG)
        someRecipe.setIngredient('Y', Material.GOLDEN_SWORD)
        return someRecipe
    }



}