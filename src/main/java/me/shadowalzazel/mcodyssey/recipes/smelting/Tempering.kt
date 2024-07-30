package me.shadowalzazel.mcodyssey.recipes.smelting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.recipes.ChoiceManager
import org.bukkit.NamespacedKey
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

class Tempering : ChoiceManager {

    fun getRecipes(): List<Recipe> {
        return listOf(
            heatedTitaniumRecipe(),
            anodizedTitaniumRecipe()
        )
    }

    private fun heatedTitaniumRecipe(): BlastingRecipe {
        val result: ItemStack = Ingredients.HEATED_TITANIUM_INGOT.newItemStack(1)
        val input = titaniumChoices()
        val experience = 200.0F
        val time = 20 * 20

        return BlastingRecipe(
            NamespacedKey(Odyssey.instance, "heated_titanium_furnace"),
            result,
            input,
            experience,
            time
        )
    }

    private fun anodizedTitaniumRecipe(): BlastingRecipe {
        val result: ItemStack = Ingredients.ANODIZED_TITANIUM_INGOT.newItemStack(1)
        val input = heatedTitaniumChoices()
        val experience = 200.0F
        val time = 20 * 20
        return BlastingRecipe(
            NamespacedKey(Odyssey.instance, "anodized_titanium_furnace"),
            result,
            input,
            experience,
            time
        )
    }


}