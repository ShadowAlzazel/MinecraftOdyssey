package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

object FinalRecipes {

    // Utility
    private val WEAPON_ENCYCLOPEDIA = createWeaponEncyclopediaRecipe()

    val recipeSet = setOf(WEAPON_ENCYCLOPEDIA)


    /*----------------------------------------ENCYCLOPEDIA-----------------------------------------*/

    private fun createWeaponEncyclopediaRecipe(): ShapedRecipe {
        val someResult = OdysseyBooks.WEAPON_ENCYCLOPEDIA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "weaponencyclopedia"), someResult)

        someRecipe.shape(" A ", " B ", " C ")
        someRecipe.setIngredient('A', Material.STONE_SWORD)
        someRecipe.setIngredient('B', Material.BOOK)
        someRecipe.setIngredient('C', Material.EMERALD)
        return someRecipe
    }


}