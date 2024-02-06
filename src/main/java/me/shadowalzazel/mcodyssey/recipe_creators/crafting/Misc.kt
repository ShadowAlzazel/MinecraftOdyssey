package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.*
import me.shadowalzazel.mcodyssey.items.Runesherds.createRuneware
import me.shadowalzazel.mcodyssey.items.Runesherds.createSherdStack
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.recipe.CraftingBookCategory

class Misc {

    fun getRecipes(): List<Recipe> {
        return listOf(
            tempRunesherdRecipe(),
            tempRunesherd2Recipe(),

            arcaneBookRecipe(),
            bundleRecipe(),
            enigmaticOmamoriRecipe(),
            fragmentedOrbRecipe(),
            irradiatedFruitRecipe(),
            soulCatalystRecipe(),
            soulSteelUpgradeTemplateRecipe(),
            soulSpiceRecipe(),

            blazingRocketsRecipe(1, "one"),
            blazingRocketsRecipe(2, "two"),
            blazingRocketsRecipe(3, "three"),
            blazingRocketsRecipe(4, "four"),
            blazingRocketsRecipe(5, "five")
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun tempRunesherdRecipe(): ShapedRecipe {
        val result = Runesherds.ASSAULT_RUNESHERD.createSherdStack()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "temp_runesherd"), result).apply {
            shape(" B ", "BDB", " B ")
            setIngredient('B', Material.BRICK)
            setIngredient('D', Material.DIAMOND)
        }
        return recipe
    }

    private fun tempRunesherd2Recipe(): ShapedRecipe {
        val result = Runesherds.GUARD_RUNESHERD.createSherdStack()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "temp_runesherd2"), result).apply {
            shape(" B ", "BEB", " B ")
            setIngredient('B', Material.BRICK)
            setIngredient('E', Material.EMERALD)
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun arcaneBookRecipe(): ShapedRecipe {
        val result = Arcane.ARCANE_BOOK.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_book"), result).apply {
            shape(" A ", "ABA", " AC")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('B', Material.ENCHANTED_BOOK)
            setIngredient('C', Material.PRISMARINE_CRYSTALS)
        }
        return recipe
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

    private fun fragmentedOrbRecipe(): ShapedRecipe {
        val result = Runesherds.FRAGMENTED_ORB.createRuneware(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fragmented_orb"), result).apply {
            shape(" C ", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

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

    private fun soulSpiceRecipe(): ShapelessRecipe {
        val result = Miscellaneous.SOUL_SPICE.createItemStack(3)
        val soulCrystal = Ingredients.SOUL_QUARTZ.createItemStack(1)
        // Recipe
        val recipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "soul_spice"), result).apply {
            addIngredient(1, Material.GLOWSTONE_DUST)
            addIngredient(1, Material.REDSTONE)
            addIngredient(1, soulCrystal)
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

    /*-----------------------------------------------------------------------------------------------*/

    private fun blazingRocketsRecipe(tier: Int, tierName: String): ShapelessRecipe {
        // New Rocket
        val rocket = Miscellaneous.BLAZING_ROCKET.createItemStack(3)
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