package me.shadowalzazel.mcodyssey.recipe_creators.smithing

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Foods
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Templates
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*

class Weapon {

    fun getRecipes(): List<SmithingRecipe> {
        return listOf(
            createKatanaRecipe(),
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

    private fun createKatanaRecipe(): SmithingTransformRecipe {
        val result = ItemStack(Material.PAPER)
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "katana_smithing"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.WOODEN_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }



}