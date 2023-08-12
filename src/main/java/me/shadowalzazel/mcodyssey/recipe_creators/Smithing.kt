package me.shadowalzazel.mcodyssey.recipe_creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Templates
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*

class Smithing {

    fun getRecipes(): List<Recipe> {
        return listOf(
            bookCombining(),
            bookSmithing(),
            soulSteelUpgrading(),
            engraving(),
            customTrimming()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // Book/Tome + Book/Tome
    private fun bookCombining(): SmithingTransformRecipe {
        val result = ItemStack(Material.ENCHANTED_BOOK)
        val template = RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "book_combining"),
            result,
            template,
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
            RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS, Material.GOLD_NUGGET)
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
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD,
                Material.SUNFLOWER,
            ),
            RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS, Material.GOLD_NUGGET)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // Soul Steel Template + Item + Soul Steel Ingot
    private fun soulSteelUpgrading(): SmithingTransformRecipe {
        val result = Ingredients.SOUL_STEEL_INGOT.createItemStack(1)
        val template = RecipeChoice.ExactChoice(Templates.SOUL_STEEL_UPGRADE_TEMPLATE.createItemStack(1))
        val ingot = RecipeChoice.ExactChoice(Ingredients.SOUL_STEEL_INGOT.createItemStack(1))

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "soul_steel_upgrading"),
            result,
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
            ),
            ingot
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/


    private fun engraving(): SmithingTransformRecipe {
        val result = ItemStack(Material.AMETHYST_SHARD)
        val template = RecipeChoice.MaterialChoice(Material.PAPER)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "amethyst_engraving"),
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
                Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.AMETHYST_SHARD, Material.IRON_INGOT,
                Material.PAPER,
                Material.RAW_GOLD, Material.RAW_IRON, Material.GOLD_NUGGET, Material.IRON_NUGGET,
                Material.SUNFLOWER
            ),
            RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun customTrimming(): SmithingTrimRecipe {
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
            Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE
        )

        val armor = RecipeChoice.MaterialChoice(
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
        )

        return SmithingTrimRecipe(
            NamespacedKey(Odyssey.instance, "custom_trims"),
            template,
            armor,
            RecipeChoice.MaterialChoice(
                Material.EMERALD,
                Material.QUARTZ,
                Material.OBSIDIAN
            )
        )
    }

}