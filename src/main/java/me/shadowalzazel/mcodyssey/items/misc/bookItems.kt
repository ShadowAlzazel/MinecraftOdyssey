package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


object GildedBook : OdysseyItem("Gilded Book",
    Material.ENCHANTED_BOOK,
    Component.text("Gilded Book", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    customModel = CustomModels.GILDED_BOOK) {
    fun createGildedBook(gildedEnchantment: OdysseyEnchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)

        // Assign meta
        newGildedBook.itemMeta = newGildedBook.itemMeta.also {
            it.addEnchant(gildedEnchantment, level, true)
            it.setCustomModelData(CustomModels.GILDED_BOOK)
            it.displayName(odysseyDisplayName)
            val textLore = gildedEnchantment.enchantLore(level)
            it.lore(listOf(textLore.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        }

        return newGildedBook
    }
}


// TOME OF DISCHARGE
object TomeOfDischarge : OdysseyItem("Tome Of Discharge",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Discharge", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_DISCHARGE) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Discharge", TextColor.color(193, 94, 54)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}


// TOME OF PROMOTION
object TomeOfPromotion : OdysseyItem("Tome Of Promotion",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Promotion", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_PROMOTION) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Promotion", TextColor.color(210, 255, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}


// TOME OF REPLICATION
object TomeOfReplication : OdysseyItem("Tome Of Replication",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Replication", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_REPLICATION) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Replication", TextColor.color(64, 128, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}


// TOME OF HARMONY
object TomeOfHarmony : OdysseyItem("Tome Of Harmony",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Harmony", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_HARMONY) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Harmony", TextColor.color(237, 165, 247)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}


// TOME OF HARMONY
object TomeOfEmbrace : OdysseyItem("Tome Of Embrace",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Embrace", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_EMBRACE) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Embrace", TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}


// TOME OF HARMONY
object TomeOfBanishment : OdysseyItem("Tome Of Banishment",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Banishment", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_BANISHMENT) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Banishment", TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}


// TOME OF INFUSION
object TomeOfExpenditure : OdysseyItem("Tome Of Expenditure",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Expenditure", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    customModel = CustomModels.TOME_OF_EXPENDITURE) {

    override fun createItemStack(amount: Int): ItemStack {
        val newTome =  super.createItemStack(amount)
        newTome.lore(listOf(Component.text("Tome of Expenditure", TextColor.color(114, 176, 54)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        return newTome
    }

}