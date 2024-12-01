package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.Material
import org.bukkit.inventory.RecipeChoice

interface ChoiceManager {

    fun silverChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.SILVER_INGOT.newItemStack(1)
        val dataItem = Item.SILVER_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    fun titaniumChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.TITANIUM_INGOT.newItemStack(1)
        val dataItem = Item.TITANIUM_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    fun heatedTitaniumChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.HEATED_TITANIUM_INGOT.newItemStack(1)
        val dataItem = Item.HEATED_TITANIUM_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    fun anodizedTitaniumChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.ANODIZED_TITANIUM_INGOT.newItemStack(1)
        val dataItem = Item.ANODIZED_TITANIUM_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    fun iridiumChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.IRIDIUM_INGOT.newItemStack(1)
        val dataItem = Item.IRIDIUM_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    fun mithrilChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.MITHRIL_INGOT.newItemStack(1)
        val dataItem = Item.MITHRIL_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    fun soulSteelChoices(): RecipeChoice.ExactChoice {
        val bukkitItem = Item.SOUL_STEEL_INGOT.newItemStack(1)
        val dataItem = Item.SOUL_STEEL_INGOT.newItemStack(1, false)
        return RecipeChoice.ExactChoice(bukkitItem, dataItem)
    }

    // Const
    fun woodChoices(): RecipeChoice.MaterialChoice {
        return RecipeChoice.MaterialChoice(
            Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS,
            Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS,
            Material.CHERRY_PLANKS, Material.MANGROVE_PLANKS
        )
    }


    // Keys
    val materialKeys: Map<String, Map<Char, RecipeChoice>>
        get() = mapOf(
            // Minecraft
            "wooden" to mapOf(
                'X' to woodChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "stone" to mapOf(
                'X' to RecipeChoice.MaterialChoice(Material.COBBLESTONE),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "golden" to mapOf(
                'X' to RecipeChoice.MaterialChoice(Material.GOLD_INGOT),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "iron" to mapOf(
                'X' to RecipeChoice.MaterialChoice(Material.IRON_INGOT),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "diamond" to mapOf(
                'X' to RecipeChoice.MaterialChoice(Material.DIAMOND),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "netherite" to mapOf(
                'X' to RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            // Odyssey
            "copper" to mapOf(
                'X' to RecipeChoice.MaterialChoice(Material.COPPER_INGOT),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "silver" to mapOf(
                'X' to silverChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "soul_steel" to mapOf(
                'X' to soulSteelChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "titanium" to mapOf(
                'X' to titaniumChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "anodized_titanium" to mapOf(
                'X' to anodizedTitaniumChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "iridium" to mapOf(
                'X' to iridiumChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "mithril" to mapOf(
                'X' to mithrilChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
        )

}