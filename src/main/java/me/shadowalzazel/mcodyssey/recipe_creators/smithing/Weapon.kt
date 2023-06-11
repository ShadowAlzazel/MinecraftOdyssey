package me.shadowalzazel.mcodyssey.recipe_creators.smithing

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.SmithingRecipe
import org.bukkit.inventory.SmithingTransformRecipe

class Weapon {

    fun getRecipes(): List<SmithingRecipe> {
        return listOf(
            createDiamondKatanaRecipe(),
            createIronKatanaRecipe(),
        )
    }

    // Maybe have event listener?

    private fun createDiamondKatanaRecipe(): SmithingTransformRecipe {
        val result = Weapons.DIAMOND_KATANA.createWeapon()
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "diamond_katana_smithing"),
            result,
            RecipeChoice.MaterialChoice(Material.PAPER),
            RecipeChoice.MaterialChoice(Material.DIAMOND_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun createIronKatanaRecipe(): SmithingTransformRecipe {
        val result = Weapons.GOLDEN_KATANA.createWeapon()
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "golden_katana_smithing"),
            result,
            RecipeChoice.MaterialChoice(Material.PAPER),
            RecipeChoice.MaterialChoice(Material.GOLDEN_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

}