package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

class SmeltingRecipes : ChoiceManager {

    private fun glyphicItemFurnaceRecipe(): BlastingRecipe {
        val result = ItemStack(Material.BRICK)
        val input = RecipeChoice.MaterialChoice(Material.CLAY_BALL)
        val experience = 200.0F
        val time = 20 * 20
        return BlastingRecipe(
            NamespacedKey(Odyssey.instance, "glyphic_item_furnace"),
            result,
            input,
            experience,
            time
        )
    }

    private fun heatedTitaniumRecipe(): BlastingRecipe {
        val result = Item.HEATED_TITANIUM_INGOT.newItemStack(1)
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
        val result = Item.ANODIZED_TITANIUM_INGOT.newItemStack(1)
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