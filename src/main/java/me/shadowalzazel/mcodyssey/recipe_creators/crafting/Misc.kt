package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.Runic
import me.shadowalzazel.mcodyssey.items.Templates
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.recipe.CraftingBookCategory

class Misc {

    fun getRecipes(): List<Recipe> {
        return listOf(
            soulCatalystRecipe(),
            enigmaticOmamoriRecipe(),
            irradiatedFruitRecipe(),
            arcaneBookRecipe(),
            bundleRecipe(),
            soulSteelUpgradeTemplateRecipe(),

            recklessRocketRecipes(1, "one"),
            recklessRocketRecipes(2, "two"),
            recklessRocketRecipes(3, "three"),
            recklessRocketRecipes(4, "four"),
            recklessRocketRecipes(5, "five")
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun irradiatedFruitRecipe(): ShapedRecipe {
        val result = Miscellaneous.IRRADIATED_FRUIT.createItemStack(1)
        val irradiatedRod = Ingredients.IRRADIATED_ROD.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "irradiated_fruit"), result).apply {
            shape("XZX", "ZYZ", "XZX")
            setIngredient('X', RecipeChoice.ExactChoice(irradiatedRod))
            setIngredient('Y', Material.GLOW_BERRIES)
            setIngredient('Z', Material.TINTED_GLASS)
        }
        return recipe
    }

    private fun arcaneBookRecipe(): ShapedRecipe {
        val result = Runic.ARCANE_BOOK.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_book"), result).apply {
            shape(" A ", "ABA", " AC")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('B', Material.ENCHANTED_BOOK)
            setIngredient('C', Material.PRISMARINE_CRYSTALS)
        }
        return recipe
    }

    private fun soulCatalystRecipe(): ShapedRecipe {
        val result = Miscellaneous.SOUL_CATALYST.createItemStack(1)
        val soulCrystal = Ingredients.SOUL_QUARTZ.createItemStack(1)
        val ectoplasm = Ingredients.ECTOPLASM.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_catalyst"), result).apply {
            shape("XZX", "ZYZ", "XZX")
            setIngredient('X', Material.TINTED_GLASS)
            setIngredient('Y', RecipeChoice.ExactChoice(ectoplasm))
            setIngredient('Z', RecipeChoice.ExactChoice(soulCrystal))
        }
        return recipe
    }

    private fun enigmaticOmamoriRecipe(): ShapedRecipe {
        val result = Miscellaneous.ENIGMATIC_OMAMORI.createItemStack(1)
        val soulCrystal = Ingredients.SOUL_QUARTZ.createItemStack(1)
        val soulIngot = Ingredients.SOUL_STEEL_INGOT.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "enigmatic_omamori"), result).apply {
            shape(" W ", "XYX", "XZX")
            setIngredient('Z', RecipeChoice.ExactChoice(soulIngot))
            setIngredient('Y', RecipeChoice.ExactChoice(soulCrystal))
            setIngredient('X', Material.PAPER)
            setIngredient('W', Material.STRING)
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun recklessRocketRecipes(tier: Int, tierNum: String): ShapelessRecipe {
        // New Rocket
        val newRocket = ItemStack(Material.FIREWORK_ROCKET, 1)
        newRocket.itemMeta = (newRocket.itemMeta as FireworkMeta).also {
            it.power = tier + 3
            it.lore(listOf(Component.text("Danger!", TextColor.color(255, 55, 55)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        }
        // Recipe
        val someRecipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "duration_rocket_tier_$tierNum"), newRocket).apply {
            addIngredient(2, Material.GUNPOWDER)
            addIngredient(tier, Material.BLAZE_POWDER)
            addIngredient(1, Material.PAPER)
            addIngredient(RecipeChoice.MaterialChoice(listOf(Material.GUNPOWDER, Material.FIREWORK_STAR)))
            group = "reckless_rockets"
        }
        return someRecipe
    }

    private fun bundleRecipe(): ShapedRecipe {
        val result = ItemStack(Material.BUNDLE, 1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bundle_crafting"), result).apply {
            shape(" S ", "RFR", " R ")
            setIngredient('F', Material.RABBIT_FOOT)
            setIngredient('S', Material.STRING)
            setIngredient('R', Material.RABBIT_HIDE)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun soulSteelUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Templates.SOUL_STEEL_UPGRADE_TEMPLATE.createItemStack(1)
        val soulCrystal = Ingredients.SOUL_QUARTZ.createItemStack(1)
        val ectoplasm = Ingredients.ECTOPLASM.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_steel_upgrade_template_crafting"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', RecipeChoice.ExactChoice(soulCrystal))
            setIngredient('E', RecipeChoice.ExactChoice(ectoplasm))
            setIngredient('S', Material.SOUL_SAND)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

}