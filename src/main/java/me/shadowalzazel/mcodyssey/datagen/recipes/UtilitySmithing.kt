package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*

class UtilitySmithing : ChoiceManager { // USE THIS CLASS TO CREATE RECIPE WHICH ARE AVAILABLE TO BE MODIFIED

    fun getRecipes(): List<Recipe> {
        return listOf(
            runeInscribing(),
            bookCombining(),
            bookSmithing(),
            toolUpgrading(),
            engraving(),
            glyphAugmenting(),
            customArmorTrimming(),
            customPartUpgrading()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // TODO: Test method to write runes and create a spell_scroll
    private fun runeInscribing(): SmithingTransformRecipe {
        val result = Item.SPELL_SCROLL.newItemStack()
        val template = Item.SPELL_SCROLL.toRecipeChoice()
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "rune_inscribing"),
            result,
            template,
            RecipeChoice.MaterialChoice(Material.BUNDLE),
            RecipeChoice.MaterialChoice(Material.INK_SAC)
        )
    }

    // Book/Tome + Book/Tome
    private fun bookCombining(): SmithingTransformRecipe {
        val result = ItemStack(Material.ENCHANTED_BOOK)
        val template = RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "book_combining"),
            result,
            template,
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
            RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS, Material.LAPIS_LAZULI)
        )
    }

    // Book/Tome + Item
    private fun bookSmithing(): SmithingTransformRecipe {
        val result = ItemStack(Material.ENCHANTED_BOOK)
        val template = RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "book_smithing"),
            result,
            template,
            RecipeChoice.MaterialChoice(
                Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
                Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
                Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
                Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
                Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
                Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
                Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
                Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
                Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD
            ),
            RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS, Material.LAPIS_LAZULI)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // For Upgrading a tool material
    private fun toolUpgrading(): SmithingTransformRecipe {
        val template = RecipeChoice.MaterialChoice(Material.PAPER)
        val addition = RecipeChoice.MaterialChoice(Material.IRON_INGOT)
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "tool_upgrading"),
            ItemStack(Material.IRON_INGOT),
            template,
            RecipeChoice.MaterialChoice(
                Material.IRON_SWORD,
                Material.IRON_AXE,
                Material.IRON_PICKAXE,
                Material.IRON_SHOVEL,
                Material.IRON_HOE,
                Material.IRON_BOOTS,
                Material.IRON_LEGGINGS,
                Material.IRON_CHESTPLATE,
                Material.IRON_HELMET,
                Material.DIAMOND_SWORD,
                Material.DIAMOND_AXE,
                Material.DIAMOND_PICKAXE,
                Material.DIAMOND_SHOVEL,
                Material.DIAMOND_HOE,
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_HELMET,
            ),
            addition
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Amethyst Engraving
    private fun engraving(): SmithingTransformRecipe {
        val result = ItemStack(Material.AMETHYST_SHARD)
        val template = RecipeChoice.MaterialChoice(Material.PAPER)
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "engraving"),
            result,
            template,
            RecipeChoice.MaterialChoice(
                Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
                Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
                Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
                Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
                Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
                Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
                Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
                Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
                Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD,
                Material.POTION,
                Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.AMETHYST_SHARD, Material.IRON_INGOT,
                Material.PAPER,
                Material.RAW_GOLD, Material.RAW_IRON, Material.GOLD_NUGGET, Material.IRON_NUGGET
            ),
            RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // For Cloning Glyphs
    private fun glyphAugmenting(): SmithingTransformRecipe {
        val result = ItemStack(Material.BRICK)
        val template = RecipeChoice.MaterialChoice(Material.BRICK, Material.CLAY_BALL)
        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "glyph_augmenting"),
            result,
            template,
            RecipeChoice.MaterialChoice(
                Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
                Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
                Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
                Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
                Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
                Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
                Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
                Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
                Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD,
                Material.BRICK, Material.NETHER_BRICK, Material.CLAY_BALL, Material.TOTEM_OF_UNDYING,
                Material.STICK, Material.BLAZE_ROD
            ),
            RecipeChoice.MaterialChoice(Material.DIAMOND)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // For Custom Trims
    private fun customArmorTrimming(): SmithingTrimRecipe {
        val template = RecipeChoice.MaterialChoice(
            Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.PAPER
        )
        val armor = RecipeChoice.MaterialChoice(
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
        )
        return SmithingTrimRecipe(
            NamespacedKey(Odyssey.instance, "custom_armor_trimming"),
            template,
            armor,
            RecipeChoice.MaterialChoice(
                Material.IRON_INGOT,
                Material.GOLD_INGOT,
                Material.REDSTONE,
                Material.DIAMOND,
                Material.EMERALD,
                Material.AMETHYST_SHARD,
                Material.NETHERITE_INGOT,
                Material.LAPIS_LAZULI,
                Material.QUARTZ,
                Material.COPPER_INGOT,
                Material.OBSIDIAN,
                Material.RESIN_CLUMP
            )
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // For Custom Parts
    private fun customPartUpgrading(): SmithingTrimRecipe {
        val pattern = RecipeChoice.MaterialChoice(
            Material.PAPER
        )
        val tool = RecipeChoice.MaterialChoice(
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL)
        return SmithingTrimRecipe(
            NamespacedKey(Odyssey.instance, "custom_part_upgrading"),
            pattern,
            tool,
            RecipeChoice.MaterialChoice(
                Material.IRON_INGOT, // For iridium
                Material.GOLD_INGOT
            )
        )
    }

}