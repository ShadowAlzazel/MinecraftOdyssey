package me.shadowalzazel.mcodyssey.datagen.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Equipment
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Weapons {

    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            compactCrossbowRecipe(),
            autoCrossbowRecipe(),
            alchemicalBolterRecipe(),
            voidLinkedKunaiRecipe()
        )
    }

    private fun compactCrossbowRecipe(): ShapedRecipe {
        val result = Equipment.COMPACT_CROSSBOW.newItemStack(1)
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
        val result = Equipment.AUTO_CROSSBOW.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "auto_crossbow"), result).apply {
            shape("NHN", "SBS", "CBC")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BLAZE_ROD)
            setIngredient('H', Material.HEAVY_CORE)
            setIngredient('S', Material.STRING)
            setIngredient('C', Material.COPPER_INGOT)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun alchemicalBolterRecipe(): ShapedRecipe {
        val result = Equipment.ALCHEMICAL_BOLTER.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "alchemical_bolter"), result).apply {
            shape("QDQ", "STS", "AQG")
            setIngredient('A', Material.DRAGON_BREATH)
            setIngredient('G', Material.TINTED_GLASS)
            setIngredient('D', Material.DIAMOND)
            setIngredient('Q', Material.QUARTZ)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('S', Material.STRING)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


    private fun voidLinkedKunaiRecipe(): ShapedRecipe {
        val result = Equipment.VOID_LINKED_KUNAI.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "void_linked_kunai"), result).apply {
            shape("N", "B", "E")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BLAZE_ROD)  // TODO :Change to breeze rod
            setIngredient('E', Material.ENDER_EYE)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

}