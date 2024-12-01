package me.shadowalzazel.mcodyssey.datagen.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.custom.Equipment
import me.shadowalzazel.mcodyssey.common.items.custom.Ingredients
import me.shadowalzazel.mcodyssey.common.items.custom.Miscellaneous
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.createGlyphicItem
import me.shadowalzazel.mcodyssey.datagen.items.ItemCreator
import me.shadowalzazel.mcodyssey.datagen.recipes.ChoiceManager
import me.shadowalzazel.mcodyssey.datagen.recipes.creators.RocketRecipeCreator
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.recipe.CraftingBookCategory

class Misc : ChoiceManager, ItemCreator {

    fun getRecipes(): List<Recipe> {
        val rocketCreator = RocketRecipeCreator()

        return listOf(
            fragmentedOrbRecipe(),
            clayTotemRecipe(),
            claySkullRecipe(),
            clayDowelRecipe(),
            fragmentedRodsRecipe(),
            clayKeyRecipe(),

            blankTomeRecipe(),
            bundleRecipe(),
            enigmaticOmamoriRecipe(),
            irradiatedFruitRecipe(),
            soulCatalystRecipe(),
            soulSpiceRecipe(),
            silverIngotRecipe(),

            soulSteelUpgradeTemplateRecipe(),
            titaniumUpgradeTemplateRecipe(),
            iridiumUpgradeTemplateRecipe(),
            imperialArmorTrimRecipe(),
            mithrilUpgradeTemplateRecipe(),

            rocketCreator.blazingRocketsRecipe(1, "one"),
            rocketCreator.blazingRocketsRecipe(2, "two"),
            rocketCreator.blazingRocketsRecipe(3, "three"),
            rocketCreator.blazingRocketsRecipe(4, "four"),
            rocketCreator.blazingRocketsRecipe(5, "five")
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Runeware
    private fun fragmentedOrbRecipe(): ShapedRecipe {
        val result = Glyphsherds.CLAY_ORB.createGlyphicItem(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fragmented_orb"), result).apply {
            shape(" C ", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayTotemRecipe(): ShapedRecipe {
        val result = Glyphsherds.CLAY_TOTEM.createGlyphicItem(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_totem"), result).apply {
            shape(" L ", "CCC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun claySkullRecipe(): ShapedRecipe {
        val result = Glyphsherds.CLAY_SKULL.createGlyphicItem(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_skull"), result).apply {
            shape("   ", "LCL", "CCC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun clayDowelRecipe(): ShapedRecipe {
        val result = Glyphsherds.CLAY_DOWEL.createGlyphicItem(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_dowel"), result).apply {
            shape("  C", " L ", "C  ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun fragmentedRodsRecipe(): ShapedRecipe {
        val result = Glyphsherds.CLAY_RODS.createGlyphicItem(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fragmented_rods"), result).apply {
            shape("F F", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayKeyRecipe(): ShapedRecipe {
        val result = Glyphsherds.CLAY_KEY.createGlyphicItem(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_key"), result).apply {
            shape("L ", "C ", "CC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    // Misc
    private fun blankTomeRecipe(): ShapedRecipe {
        val result = Miscellaneous.BLANK_TOME.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blank_tome"), result).apply {
            shape(" C ", "ABA", " C ")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('B', Material.BOOK)
            setIngredient('C', Material.PRISMARINE_CRYSTALS)
        }
        return recipe
    }

    private fun bundleRecipe(): ShapedRecipe {
        val result = ItemStack(Material.BUNDLE, 1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bundle"), result).apply {
            shape(" S ", "RFR", " R ")
            setIngredient('F', Material.RABBIT_FOOT)
            setIngredient('S', Material.STRING)
            setIngredient('R', Material.RABBIT_HIDE)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }


    private fun silverIngotRecipe(): ShapedRecipe {
        val result = Ingredients.SILVER_INGOT.createStack(1)
        val nugget = Ingredients.SILVER_NUGGET.createStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "silver_ingot"), result).apply {
            shape("NNN", "NNN", "NNN")
            setIngredient('N', RecipeChoice.ExactChoice(nugget))
        }
        return recipe
    }


    private fun enigmaticOmamoriRecipe(): ShapedRecipe {
        val result = Miscellaneous.ENIGMATIC_OMAMORI.newItemStack(1)
        val soulCrystal = Ingredients.SOUL_QUARTZ.newItemStack(1)
        val soulIngot = Ingredients.SOUL_STEEL_INGOT.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "enigmatic_omamori"), result).apply {
            shape(" W ", "XYX", "XZX")
            setIngredient('Z', RecipeChoice.ExactChoice(soulIngot))
            setIngredient('Y', RecipeChoice.ExactChoice(soulCrystal))
            setIngredient('X', Material.PAPER)
            setIngredient('W', Material.STRING)
        }
        return recipe
    }

    private fun irradiatedFruitRecipe(): ShapedRecipe {
        val result = Miscellaneous.IRRADIATED_FRUIT.newItemStack(1)
        val irradiatedRod = Ingredients.IRRADIATED_ROD.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "irradiated_fruit"), result).apply {
            shape("XZX", "ZYZ", "XZX")
            setIngredient('X', RecipeChoice.ExactChoice(irradiatedRod))
            setIngredient('Y', Material.GLOW_BERRIES)
            setIngredient('Z', Material.TINTED_GLASS)
        }
        return recipe
    }

    private fun soulCatalystRecipe(): ShapedRecipe {
        val result = Miscellaneous.SOUL_CATALYST.newItemStack(1)
        val soulCrystal = Ingredients.SOUL_QUARTZ.newItemStack(1)
        val ectoplasm = Ingredients.ECTOPLASM.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_catalyst"), result).apply {
            shape("XZX", "ZYZ", "XZX")
            setIngredient('X', Material.TINTED_GLASS)
            setIngredient('Y', RecipeChoice.ExactChoice(ectoplasm))
            setIngredient('Z', RecipeChoice.ExactChoice(soulCrystal))
        }
        return recipe
    }

    private fun soulSpiceRecipe(): ShapelessRecipe {
        val result = Miscellaneous.SOUL_SPICE.newItemStack(3)
        val soulCrystal = Ingredients.SOUL_QUARTZ.newItemStack(1)
        // Recipe
        val recipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "soul_spice"), result).apply {
            addIngredient(1, Material.GLOWSTONE_DUST)
            addIngredient(1, Material.SUGAR)
            addIngredient(1, soulCrystal)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/
    private fun soulSteelUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Equipment.SOUL_STEEL_UPGRADE_TEMPLATE.newItemStack(1)
        val soulCrystal = Ingredients.SOUL_QUARTZ.newItemStack(1)
        val ectoplasm = Ingredients.ECTOPLASM.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_steel_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', RecipeChoice.ExactChoice(soulCrystal))
            setIngredient('E', RecipeChoice.ExactChoice(ectoplasm))
            setIngredient('S', Material.SOUL_SAND)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun titaniumUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Equipment.TITANIUM_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "titanium_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Material.IRON_INGOT)
            setIngredient('E', Material.FLINT)
            setIngredient('S', Material.TUFF)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun iridiumUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Equipment.IRIDIUM_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iridium_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Material.BREEZE_ROD)
            setIngredient('E', Material.AMETHYST_SHARD)
            setIngredient('S', Material.OBSIDIAN)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun mithrilUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Equipment.MITHRIL_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "mithril_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', silverChoices())
            setIngredient('E', Material.DIAMOND)
            setIngredient('S', Material.COBBLED_DEEPSLATE)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun imperialArmorTrimRecipe(): ShapedRecipe {
        val result = Equipment.IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(2)
        val trim = Equipment.IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "imperial_armor_trim_recipe"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', RecipeChoice.ExactChoice(trim))
            setIngredient('E', Material.TUFF)
            setIngredient('S', Material.DIAMOND)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }
}