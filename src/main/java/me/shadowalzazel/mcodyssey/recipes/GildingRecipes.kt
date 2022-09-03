package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.SmithingRecipe

object GildingRecipes {

    // Main for combining books
    var GILDED_BOOK_LEGACY_ACTIVATION: SmithingRecipe = SmithingRecipe(
        NamespacedKey(MinecraftOdyssey.instance, "gildedbooklegacyactiation"),
        ItemStack(Material.AIR),
        RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
        RecipeChoice.MaterialChoice(Material.GOLD_INGOT)
    )

    // Main for combining books
    var GILDED_BOOK_COMBINING: SmithingRecipe = SmithingRecipe(
        NamespacedKey(MinecraftOdyssey.instance, "gildedbookcombining"),
        ItemStack(Material.AIR),
        RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
        RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)
    )

    // Main for gilded books to equipment
    var ODYSSEY_GILDED_SMITHING: SmithingRecipe = SmithingRecipe(
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

    //Change to special later
    var GILDED_ITEM_UPGRADING: SmithingRecipe = SmithingRecipe(
        NamespacedKey(MinecraftOdyssey.instance, "gildingupgrading"),
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
    )
}