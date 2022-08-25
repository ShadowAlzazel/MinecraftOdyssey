package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.models.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack


object GildedBook : OdysseyItem("Gilded Book",
    Material.ENCHANTED_BOOK,
    Component.text("Gilded Book", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    customModel = CustomModels.GILDED_BOOK) {
    fun createGildedBook(gildedEnchantment: Enchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)

        // Assign meta
        newGildedBook.itemMeta = newGildedBook.itemMeta.also {
            it.addEnchant(gildedEnchantment, level, true)
            it.setCustomModelData(CustomModels.GILDED_BOOK)
            it.displayName(odysseyDisplayName)
            it.lore = listOf("${ChatColor.GOLD}${gildedEnchantment.name} ${romanNumeralList[level]}")
        }

        return newGildedBook
    }
}
