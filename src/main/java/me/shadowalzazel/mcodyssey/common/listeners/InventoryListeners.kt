package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.EnchantabilityHandler
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object InventoryListeners : Listener, EnchantabilityHandler {

    @EventHandler
    fun mainClickEvent(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (!item.hasItemMeta()) return
        toolTipHandler(event)
    }

    private fun toolTipHandler(event: InventoryClickEvent) {
        if (event.click != ClickType.RIGHT) return
        val item = event.currentItem ?: return
        if (!item.hasItemMeta() && item.type != Material.ENCHANTED_BOOK) return
        // Check if Enchanted Item
        if (item.itemMeta.hasEnchants()) {
            item.updatePoints(resetLore = true, toggleToolTip = true)
            return
        }
        if (item.itemMeta is EnchantmentStorageMeta) {
            item.updatePoints(resetLore = true, toggleToolTip = true)
            return
        }
    }

}