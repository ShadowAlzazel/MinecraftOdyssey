package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.enchantments.api.EnchantabilityHandler
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

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
        item.updatePoints(resetLore = true, toggleToolTip = true)
    }

}