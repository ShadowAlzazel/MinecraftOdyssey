package me.shadowalzazel.mcodyssey.datagen.recipes

import me.shadowalzazel.mcodyssey.util.constants.ItemModels
import me.shadowalzazel.mcodyssey.common.items.custom.Ingredients
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

interface ChoiceManager {

    fun silverChoices(): RecipeChoice.ExactChoice {
        val exactItem = Ingredients.SILVER_INGOT.newItemStack(1)
        val dataItem = ItemStack(Material.IRON_INGOT).apply {
            val meta = itemMeta
            meta.setCustomModelData(ItemModels.SILVER_INGOT)
            val itemName = Component.text("silver_ingot")
            meta.itemName(itemName)
            val name = Component.text("Silver Ingot")
                .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
            meta.displayName(name)
            itemMeta = meta
        }
        return RecipeChoice.ExactChoice(exactItem, dataItem)
    }

    fun titaniumChoices(): RecipeChoice.ExactChoice {
        val exactItem = Ingredients.TITANIUM_INGOT.newItemStack(1)
        val dataItem = ItemStack(Material.IRON_INGOT).apply {
            val meta = itemMeta
            meta.setCustomModelData(ItemModels.TITANIUM_INGOT)
            val itemName = Component.text("titanium_ingot")
            meta.itemName(itemName)
            val name = Component.text("Titanium Ingot")
                .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
            meta.displayName(name)
            itemMeta = meta
        }
        return RecipeChoice.ExactChoice(exactItem, dataItem)
    }

    fun heatedTitaniumChoices(): RecipeChoice.ExactChoice {
        val exactItem = Ingredients.HEATED_TITANIUM_INGOT.newItemStack(1)
        val dataItem = ItemStack(Material.IRON_INGOT).apply {
            val meta = itemMeta
            meta.setCustomModelData(ItemModels.HEATED_TITANIUM_INGOT)
            val itemName = Component.text("heated_titanium_ingot")
            meta.itemName(itemName)
            val name = Component.text("Heated Titanium Ingot")
                .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
            meta.displayName(name)
            itemMeta = meta
        }
        return RecipeChoice.ExactChoice(exactItem, dataItem)
    }

    fun anodizedTitaniumChoices(): RecipeChoice.ExactChoice {
        val exactItem = Ingredients.ANODIZED_TITANIUM_INGOT.newItemStack(1)
        val dataItem = ItemStack(Material.IRON_INGOT).apply {
            val meta = itemMeta
            meta.setCustomModelData(ItemModels.ANODIZED_TITANIUM_INGOT)
            val itemName = Component.text("anodized_titanium_ingot")
            meta.itemName(itemName)
            val name = Component.text("Anodized Titanium Ingot")
                .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
            meta.displayName(name)
            itemMeta = meta
        }
        return RecipeChoice.ExactChoice(exactItem, dataItem)
    }

    fun iridiumChoices(): RecipeChoice.ExactChoice {
        val exactItem = Ingredients.IRIDIUM_INGOT.newItemStack(1)
        val dataItem = ItemStack(Material.IRON_INGOT).apply {
            val meta = itemMeta
            meta.setCustomModelData(ItemModels.IRIDIUM_INGOT)
            val itemName = Component.text("iridium_ingot")
            meta.itemName(itemName)
            val name = Component.text("Iridium Ingot")
                .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
            meta.displayName(name)
            itemMeta = meta
        }
        return RecipeChoice.ExactChoice(exactItem, dataItem)
    }

    fun mithrilChoices(): RecipeChoice.ExactChoice {
        val exactItem = Ingredients.MITHRIL_INGOT.newItemStack(1)
        val dataItem = ItemStack(Material.IRON_INGOT).apply {
            val meta = itemMeta
            meta.setCustomModelData(ItemModels.MITHRIL_INGOT)
            val itemName = Component.text("mithril_ingot")
            meta.itemName(itemName)
            val name = Component.text("Mithril Ingot")
                .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
            meta.displayName(name)
            itemMeta = meta
        }
        return RecipeChoice.ExactChoice(exactItem, dataItem)
    }

    // Const
    val WOOD_CHOICES: RecipeChoice.MaterialChoice
        get() = RecipeChoice.MaterialChoice(
            Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS,
            Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS
        )


    // Keys
    val materialKeys: Map<String, Map<Char, RecipeChoice>>
        get() = mapOf(
            // Minecraft
            "wooden" to mapOf(
                'X' to WOOD_CHOICES,
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
                'X' to RecipeChoice.ExactChoice(Ingredients.SOUL_STEEL_INGOT.newItemStack(1)),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "titanium" to mapOf(
                'X' to titaniumChoices(),
                '|' to RecipeChoice.MaterialChoice(Material.STICK)
            ),
            "anodized_titanium" to mapOf(
                'X' to RecipeChoice.ExactChoice(Ingredients.ANODIZED_TITANIUM_INGOT.newItemStack(1)),
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