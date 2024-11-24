package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.util.DataKeys
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.inventory.ItemStack

class OdysseyUpdater : DataTagManager {
    // Includes methods to update old mechanics and items from older servers
    fun updateItemModel(item: ItemStack) {
        if (!item.hasItemMeta()) return
        if (item.itemMeta.hasItemModel()) return
        val itemId = item.getItemIdentifier() ?: return
        val meta = item.itemMeta
        meta.itemModel = DataKeys.newKey(itemId)
        
    }


}