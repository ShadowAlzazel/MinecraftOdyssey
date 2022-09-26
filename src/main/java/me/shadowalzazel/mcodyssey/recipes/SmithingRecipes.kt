package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.SmithingRecipe

object SmithingRecipes {

    // Register Recipes
    fun registerRecipes(): List<SmithingRecipe> {
        return listOf(
            createGildedBookActivationRecipe(),
            createGildedCombiningRecipe(),
            createSoulSteelSmithingRecipe(),
            createOdysseyGildedSmithingRecipe(),
            createOdysseyUpgradingSmithingRecipe()
        )
    }

    /* ---------------------------------------------------------------------------*/

    // Main for combining books
    private fun createGildedBookActivationRecipe(): SmithingRecipe {
        return SmithingRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "gildedbooklegacyactiation"),
            ItemStack(Material.AIR),
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
            RecipeChoice.MaterialChoice(Material.GOLD_INGOT)
        )
    }

    private fun createGildedCombiningRecipe(): SmithingRecipe {
        return SmithingRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "gildedbookcombining"),
            ItemStack(Material.AIR),
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)
        )
    }

    private fun createSoulSteelSmithingRecipe(): SmithingRecipe {
        return SmithingRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "soulsteelsmithing"),
            ItemStack(Material.AIR),
            RecipeChoice.MaterialChoice(Material.IRON_SWORD),
            RecipeChoice.MaterialChoice(Material.IRON_INGOT)
        )
    }

    private fun createOdysseyGildedSmithingRecipe(): SmithingRecipe {
        return SmithingRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "odysseygildedsmithing"),
            ItemStack(Material.AIR),
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
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)
        )
    }

    private fun createOdysseyUpgradingSmithingRecipe(): SmithingRecipe {
        return SmithingRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "odysseyupgrading"),
            ItemStack(Material.AIR),
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
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD, Material.SUNFLOWER,
            ),
            RecipeChoice.MaterialChoice(Material.DRAGON_EGG)
            // EXACT CHOICES LATER
        )
    }

}