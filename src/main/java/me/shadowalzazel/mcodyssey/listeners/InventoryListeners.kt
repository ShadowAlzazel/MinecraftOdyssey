package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.items.Miscellaneous.updateEnchantabilityPointsLore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryListeners : Listener {

    @EventHandler
    fun mainClickEvent(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (!item.hasItemMeta()) return
        val meta = item.itemMeta
        if (meta.hasEnchants()) {
            toolTipHandler(event)
        }
    }

    private fun toolTipHandler(event: InventoryClickEvent) {
        if (event.click != ClickType.RIGHT) return
        val item = event.currentItem ?: return
        if (!item.hasItemMeta()) return
        if (!item.itemMeta.hasEnchants()) return
        item.updateEnchantabilityPointsLore(resetLore = true, toggleToolTip = true)
    }

}