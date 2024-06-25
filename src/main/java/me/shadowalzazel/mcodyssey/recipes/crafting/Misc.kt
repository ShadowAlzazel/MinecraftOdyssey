package me.shadowalzazel.mcodyssey.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.*
import me.shadowalzazel.mcodyssey.items.Equipment
import me.shadowalzazel.mcodyssey.items.Runesherds.createRuneware
import me.shadowalzazel.mcodyssey.listeners.EnchantingListeners.createStack
import me.shadowalzazel.mcodyssey.recipes.creators.BlazingRocketsCreator
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.recipe.CraftingBookCategory

class Misc {

    fun getRecipes(): List<Recipe> {
        val rocketCreator = BlazingRocketsCreator()

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
        val result = Runesherds.FRAGMENTED_ORB.createRuneware(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fragmented_orb"), result).apply {
            shape(" C ", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayTotemRecipe(): ShapedRecipe {
        val result = Runesherds.CLAY_TOTEM.createRuneware(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_totem"), result).apply {
            shape(" L ", "CCC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun claySkullRecipe(): ShapedRecipe {
        val result = Runesherds.CLAY_SKULL.createRuneware(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_skull"), result).apply {
            shape("   ", "LCL", "CCC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun clayDowelRecipe(): ShapedRecipe {
        val result = Runesherds.CLAY_DOWEL.createRuneware(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_dowel"), result).apply {
            shape("  C", " L ", "C  ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun fragmentedRodsRecipe(): ShapedRecipe {
        val result = Runesherds.FRAGMENTED_RODS.createRuneware(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "fragmented_rods"), result).apply {
            shape("F F", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayKeyRecipe(): ShapedRecipe {
        val result = Runesherds.CLAY_KEY.createRuneware(1)
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