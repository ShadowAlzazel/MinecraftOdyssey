package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.EnchantRegistryManager
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Arcane : EnchantRegistryManager {

    // Extension function to create enchanted books when this is called
    fun OdysseyItem.createGildedBook(enchantment: OdysseyEnchantment, level: Int): ItemStack {
        val registeredEnchant = convertToBukkitEnchant(enchantment) ?: return ItemStack(Material.AIR)
        val newBook = this.createItemStack(1)
        newBook.itemMeta = newBook.itemMeta.also {
            it.addEnchant(registeredEnchant, level, true)
            val textLore = mutableListOf(enchantment.displayLore(level)) + Component.text("") + enchantment.getDescriptionToolTip(level)
            val nameComponent = it.displayName()
            it.displayName(nameComponent?.append(Component.text(" - " + enchantment.translatableName, enchantment.subtype.displayColor)))
            it.lore(textLore)
        }
        return newBook
    }

    /*
    fun OdysseyItem.createS(enchantment: Enchantment, level: Int): ItemStack {
        val newBook = this.createItemStack(1)
        newBook.itemMeta = newBook.itemMeta.also {
            it.addEnchant(enchantment, level, true)
            it.displayName(enchantment.displayName(level))
        }
        return newBook
    }

     */


    val ARCANE_BOOK = OdysseyItem(
        name = "arcane_book",
        material = Material.BOOK,
        displayName = Component.text("Arcane Book", TextColor.color(191, 146, 239), TextDecoration.ITALIC),
        lore = listOf(Component.text("A book with arcane potential", TextColor.color(191, 146, 239)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ARCANE_BOOK)

    val GILDED_BOOK = OdysseyItem(
        name = "gilded_book",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Gilded Book", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
        customModel = ItemModels.GILDED_BOOK)

    val TOME_OF_DISCHARGE = OdysseyItem(
        name = "tome_of_discharge",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Discharge", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Removes one enchantment from an enchant slot",
            TextColor.color(193, 94, 54)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_DISCHARGE)

    val TOME_OF_PROMOTION = OdysseyItem(
        name = "tome_of_promotion",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Promotion", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Increases the level of an enchantment by one, up to the max",
            TextColor.color(210, 255, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_PROMOTION)

    val TOME_OF_HARMONY = OdysseyItem(
        name = "tome_of_harmony",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Harmony", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Resets the repair and combination cost of an item",
            TextColor.color(237, 165, 247)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_HARMONY)

    val TOME_OF_BANISHMENT = OdysseyItem(
        name = "tome_of_banishment",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Banishment", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Deletes an empty enchant slot from the item",
            TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_BANISHMENT)

    val TOME_OF_EMBRACE = OdysseyItem(
        name = "tome_of_embrace",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Embrace", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Adds an empty enchant slot to the item",
            TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_EMBRACE)

    val TOME_OF_REPLICATION = OdysseyItem(
        name = "tome_of_replication",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Replication", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Replicates the enchantments of an enchanted book",
            TextColor.color(64, 128, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_REPLICATION)

    val TOME_OF_EXPENDITURE = OdysseyItem(
        name = "tome_of_expenditure",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Expenditure", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("Converts one enchantment to an enchanted book", TextColor.color(114, 176, 54))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("Destroys the Item in the Process", TextColor.color(114, 176, 54))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_EXPENDITURE)

    val TOME_OF_EUPHONY = OdysseyItem(
        name = "tome_of_euphony",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Euphony", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("Increases the level of one normal enchantment, up to twice the max", TextColor.color(94, 210, 215))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("Requires one empty Gilded Slot", TextColor.color(94, 210, 215))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_EUPHONY)

    val TOME_OF_AVARICE = OdysseyItem(
        name = "tome_of_avarice",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Avarice", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("Adds one Gilded Slot to up to a max of 3", TextColor.color(169, 48, 48))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("Requires at least four regular enchantments", TextColor.color(169, 48, 48))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_AVARICE)

    val TOME_OF_POLYMERIZATION = OdysseyItem(
        name = "tome_of_polymerization",
        material = Material.ENCHANTED_BOOK,
        displayName = Component.text("Tome of Polymerization", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("Can absorb any non conflicting enchantment.", TextColor.color(250, 128, 148))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("Applies the stored enchantments to an item with no cost", TextColor.color(150, 228, 148))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOME_OF_POLYMERIZATION)

}