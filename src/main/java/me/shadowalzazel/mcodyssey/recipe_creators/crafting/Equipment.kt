package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Equipment {

    /*----------------------------------------ARCANE-----------------------------------------*/

    private fun warpingWandRecipe(): ShapedRecipe {
        val result = Weapons.WOODEN_KATANA.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_katana_crafting"), result).apply {
            shape("  X", " X ", "YZ ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "katanas"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

}