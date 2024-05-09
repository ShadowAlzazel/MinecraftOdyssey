package me.shadowalzazel.mcodyssey.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Equipment
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Equipment : ItemCreator {

    fun getRecipes(): List<Recipe> {
        return listOf(
            warpingWandRecipe(),
            explosiveArrowRecipe(),
            hornedHelmetRecipe(),
            arcaneWandRecipe(),
            arcaneBladeRecipe(),
            arcaneScepterRecipe(),
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    private fun hornedHelmetRecipe(): ShapedRecipe {
        val result = Equipment.HORNED_HELMET.createArmor(2.0)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "horned_helmet"), result).apply {
            shape("HCH", "ICI")
            setIngredient('I', Material.IRON_INGOT)
            setIngredient('H', Material.GOAT_HORN)
            setIngredient('C', Material.COPPER_INGOT)
            group = "helmets"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun explosiveArrowRecipe(): ShapedRecipe {
        val result = Equipment.EXPLOSIVE_ARROW.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "explosive_arrow"), result).apply {
            shape("G", "A")
            setIngredient('G', Material.GUNPOWDER)
            setIngredient('A', Material.ARROW)
            group = "arrows"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/
    private fun arcaneWandRecipe(): ShapedRecipe {
        val result = Equipment.ARCANE_WAND.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_wand"), result).apply {
            shape("A", "S")
            setIngredient('A', Material.AMETHYST_CLUSTER)
            setIngredient('S', Material.STICK)
            group = "arcane_wands"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun arcaneBladeRecipe(): ShapedRecipe {
        val result = Equipment.ARCANE_BLADE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_blade"), result).apply {
            shape("A A", "AQA", " S ")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('Q', Material.GOLD_INGOT)
            setIngredient('S', Material.STICK)
            group = "arcane_blades"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun arcaneScepterRecipe(): ShapedRecipe {
        val result = Equipment.ARCANE_SCEPTER.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_scepter"), result).apply {
            shape(" Q ", "ASA", " S ")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('Q', Material.AMETHYST_CLUSTER)
            setIngredient('S', Material.STICK)
            group = "arcane_scepters"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun warpingWandRecipe(): ShapedRecipe {
        val result = Equipment.WARPING_WAND.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "warping_wand"), result).apply {
            shape("W", "V", "V")
            setIngredient('W', Material.WARPED_FUNGUS)
            setIngredient('V', Material.TWISTING_VINES)
            group = "wands"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


}