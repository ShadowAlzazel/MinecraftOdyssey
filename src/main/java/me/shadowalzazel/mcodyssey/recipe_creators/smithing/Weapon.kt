package me.shadowalzazel.mcodyssey.recipe_creators.smithing

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Templates
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
            diamondKatanaRecipe(),
            ironKatanaRecipe(),
        )
    }

    // Maybe have event listener?

    private fun diamondKatanaRecipe(): SmithingTransformRecipe {
        val result = Weapons.DIAMOND_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "diamond_katana_smithing"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.DIAMOND_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun ironKatanaRecipe(): SmithingTransformRecipe {
        val result = Weapons.IRON_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "iron_katana_smithing"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun ironDaggerRecipe(): SmithingTransformRecipe {
        val result = Weapons.IRON_DAGGER.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "iron_dagger_smithing"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.GOLD_INGOT)
        )
    }




}