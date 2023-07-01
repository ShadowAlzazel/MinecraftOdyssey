package me.shadowalzazel.mcodyssey.recipe_creators.smithing

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Templates
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.SmithingRecipe
import org.bukkit.inventory.SmithingTransformRecipe

class Templating {

    fun getRecipes(): List<SmithingRecipe> {
        return listOf(
            diamondKatanaTemplatingRecipe(),
            ironKatanaTemplatingRecipe(),
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun netheriteKatanaTemplatingRecipe(): SmithingTransformRecipe {
        val result = Weapons.NETHERITE_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "netherite_katana_templating"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.DIAMOND_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun diamondKatanaTemplatingRecipe(): SmithingTransformRecipe {
        val result = Weapons.DIAMOND_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "diamond_katana_templating"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.DIAMOND_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun ironKatanaTemplatingRecipe(): SmithingTransformRecipe {
        val result = Weapons.IRON_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "iron_katana_templating"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun goldenKatanaTemplatingRecipe(): SmithingTransformRecipe {
        val result = Weapons.GOLDEN_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "golden_katana_templating"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun stoneKatanaTemplatingRecipe(): SmithingTransformRecipe {
        val result = Weapons.STONE_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "stone_katana_templating"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    private fun woodenKatanaTemplatingRecipe(): SmithingTransformRecipe {
        val result = Weapons.WOODEN_KATANA.createWeapon()
        val template = Templates.KATANA_TEMPLATE.createItemStack(1)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "wooden_katana_templating"),
            result,
            RecipeChoice.ExactChoice(template),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.COPPER_INGOT)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

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