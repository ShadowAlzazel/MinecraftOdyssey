package me.shadowalzazel.mcodyssey.datagen.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class ItemRecipes : ChoiceManager {

    // Smithing
    private fun soulSteelUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.SOUL_STEEL_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_steel_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Item.SOUL_CRYSTAL.toRecipeChoice())
            setIngredient('E', Item.ECTOPLASM.toRecipeChoice())
            setIngredient('S', Material.SOUL_SAND)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun titaniumUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.TITANIUM_UPGRADE_TEMPLATE.newItemStack(1)
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
        val result = Item.IRIDIUM_UPGRADE_TEMPLATE.newItemStack(1)
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
        val result = Item.MITHRIL_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "mithril_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', silverChoices())
            setIngredient('E', Material.DIAMOND)
            setIngredient('S', Material.COBBLED_DEEPSLATE)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    // Enchanting
    private fun blankTomeRecipe(): ShapedRecipe {
        val result = Item.BLANK_TOME.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blank_tome"), result).apply {
            shape(" C ", "ABA", " C ")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('B', Material.BOOK)
            setIngredient('C', Material.PRISMARINE_CRYSTALS)
        }
        return recipe
    }

    // Glyphic
    private fun clayOrbRecipe(): ShapedRecipe {
        val result = Item.CLAY_ORB.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_orb"), result).apply {
            shape(" C ", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayTotemRecipe(): ShapedRecipe {
        val result = Item.CLAY_TOTEM.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_totem"), result).apply {
            shape(" L ", "CCC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun claySkullRecipe(): ShapedRecipe {
        val result = Item.CLAY_SKULL.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_skull"), result).apply {
            shape("   ", "LCL", "CCC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun clayDowelRecipe(): ShapedRecipe {
        val result = Item.CLAY_DOWEL.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_dowel"), result).apply {
            shape("  C", " L ", "C  ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun clayRodsRecipe(): ShapedRecipe {
        val result = Item.CLAY_RODS.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_rods"), result).apply {
            shape("F F", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayKeyRecipe(): ShapedRecipe {
        val result = Item.CLAY_KEY.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_key"), result).apply {
            shape("L ", "C ", "CC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }


}