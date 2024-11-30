package me.shadowalzazel.mcodyssey.common.items

import me.shadowalzazel.mcodyssey.api.LootTableManager
import org.bukkit.inventory.ItemStack

// This Represents an Item generated from loot/item from datapack
open class DataItem(
    val name: String,
    val location: String?=null,
) {

    open fun newItemStack(amount: Int): ItemStack {
        val item = LootTableManager.getItemFromOdysseyLoot(this.location ?: name)
        item.amount = amount
        return item
    }

    companion object {
        val IRIDIUM_INGOT = DataItem("iridium_ingot")
    }

}