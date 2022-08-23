package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta


object GildedBook : OdysseyItem("Gilded Book", Material.ENCHANTED_BOOK, Component.text("${ChatColor.GOLD}${ChatColor.ITALIC}Gilded Book"), customModel = CustomModels.GILDED_BOOK) {
    fun createGildedBook(gildedEnchantment: Enchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        newGildedBook.addUnsafeEnchantment(gildedEnchantment, level)
        val gildedMeta: ItemMeta = newGildedBook.itemMeta

        //
        gildedMeta.setCustomModelData(6906066)

        val someBookLore = listOf("${ChatColor.GOLD}${gildedEnchantment.name} ${romanNumeralList[level]}")
        gildedMeta.lore = someBookLore
        gildedMeta.displayName(displayName)
        newGildedBook.itemMeta = gildedMeta
        return newGildedBook
    }
}
