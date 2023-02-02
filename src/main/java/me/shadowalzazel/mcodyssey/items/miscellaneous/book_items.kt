package me.shadowalzazel.mcodyssey.items.miscellaneous

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.KnowledgeBookMeta


// ARCANE_BOOK
object ArcaneBook : OdysseyItem("Arcane Book",
    Material.BOOK,
    Component.text("Arcane Book", TextColor.color(191, 146, 239), TextDecoration.ITALIC),
    listOf(Component.text("Arcane Book", TextColor.color(191, 146, 239)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ARCANE_BOOK)

// GILDED BOOK
object GildedBook : OdysseyItem("Gilded Book",
    Material.ENCHANTED_BOOK,
    Component.text("Gilded Book", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    customModel = OdysseyItemModels.GILDED_BOOK) {
    fun createGildedBook(gildedEnchantment: OdysseyEnchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)

        // Assign meta
        newGildedBook.itemMeta = newGildedBook.itemMeta.also {
            it.addEnchant(gildedEnchantment, level, true)
            it.setCustomModelData(OdysseyItemModels.GILDED_BOOK)
            it.displayName(odysseyDisplayName)
            val textLore = gildedEnchantment.enchantLore(level)
            it.lore(listOf(textLore.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        }

        return newGildedBook
    }
}

// RECIPE BOOK
object WeaponEncyclopedia : OdysseyItem( "Weapon Encyclopedia",
    Material.KNOWLEDGE_BOOK,
    Component.text("Tome of Expenditure", TextColor.color(124, 155, 164), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Expenditure", TextColor.color(124, 155, 164)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) {

    override fun createItemStack(amount: Int): ItemStack {
        val newBook =  super.createItemStack(amount)
        newBook.itemMeta = (newBook.itemMeta as KnowledgeBookMeta).also {
            it.recipes.addAll(setOf())
        }
        return newBook
    }

}