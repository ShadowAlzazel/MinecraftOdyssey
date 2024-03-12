package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Equipment
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Equipment {

    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            arcaneWandRecipe(),
            warpingWandRecipe(),
            compactCrossbowRecipe(),
            autoCrossbowRecipe()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/



    private fun arcaneWandRecipe(): ShapedRecipe {
        val result = Equipment.ARCANE_WAND.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_wand_recipe"), result).apply {
            shape("A", "S", "S")
            setIngredient('A', Material.AMETHYST_CLUSTER)
            setIngredient('S', Material.STICK)
            group = "wands"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun warpingWandRecipe(): ShapedRecipe {
        val result = Equipment.WARPING_WAND.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "warping_wand_recipe"), result).apply {
            shape("W", "V", "V")
            setIngredient('W', Material.WARPED_STEM)
            setIngredient('V', Material.TWISTING_VINES)
            group = "wands"//??
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun compactCrossbowRecipe(): ShapedRecipe {
        val result = Equipment.COMPACT_CROSSBOW.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "compact_crossbow"), result).apply {
            shape(" C ", "STS", " L ")
            setIngredient('C', Material.CRIMSON_PLANKS)
            setIngredient('S', Material.STRING)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('L', Material.STICK)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun autoCrossbowRecipe(): ShapedRecipe {
        val result = Equipment.AUTO_CROSSBOW.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "auto_crossbow"), result).apply {
            shape("NBN", "STS", "CBC")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BLAZE_ROD)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('S', Material.STRING)
            setIngredient('C', Material.COPPER_INGOT)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

}