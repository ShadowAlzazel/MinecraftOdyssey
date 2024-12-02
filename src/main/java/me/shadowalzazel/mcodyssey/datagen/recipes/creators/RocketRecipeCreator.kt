package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.recipe.CraftingBookCategory

class RocketRecipeCreator {




    internal fun blazingRocketsRecipe(tier: Int, tierName: String): ShapelessRecipe {
        // New Rocket
        val rocket = Item.BLAZING_ROCKET.newItemStack(3)
        rocket.itemMeta = (rocket.itemMeta as FireworkMeta).also {
            it.power = tier + 3
        }
        // Recipe
        val recipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "blazing_rocket_tier_$tierName"), rocket).apply {
            addIngredient(2, Material.GUNPOWDER)
            addIngredient(tier, Material.BLAZE_POWDER)
            addIngredient(1, Material.PAPER)
            addIngredient(RecipeChoice.MaterialChoice(listOf(Material.GUNPOWDER, Material.FIREWORK_STAR)))
            category = CraftingBookCategory.MISC
            group = "blazing_rockets"
        }
        return recipe
    }

}