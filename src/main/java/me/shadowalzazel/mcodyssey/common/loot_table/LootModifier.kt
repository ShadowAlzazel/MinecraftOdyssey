package me.shadowalzazel.mcodyssey.common.loot_table

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.common.items.Item
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface LootModifier {

    fun newArcaneBook(enchantment: Enchantment, level: Int=1): ItemStack {
        val book = Item.ARCANE_BOOK.newItemStack()
        val builder = ItemEnchantments.itemEnchantments(mapOf(enchantment to level))
        book.setData(DataComponentTypes.STORED_ENCHANTMENTS, builder)
        return book
    }


}