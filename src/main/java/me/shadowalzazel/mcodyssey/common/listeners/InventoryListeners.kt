package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.ItemToolTipManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.meta.EnchantmentStorageMeta

@Suppress("UnstableApiUsage")
object InventoryListeners : Listener, ItemToolTipManager {

    @EventHandler
    fun mainClickEvent(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (!item.hasItemMeta()) return
        toolTipHandler(event)
    }

    private fun toolTipHandler(event: InventoryClickEvent) {
        if (event.click != ClickType.RIGHT) return
        val item = event.currentItem ?: return
        // Ignore if
        item.updateToolTip()
    }

}