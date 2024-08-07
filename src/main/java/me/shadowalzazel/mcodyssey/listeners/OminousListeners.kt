package me.shadowalzazel.mcodyssey.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.OminousBottleMeta

object OminousListeners : Listener {

    @EventHandler
    fun ominousBottleHandler(event: PlayerItemConsumeEvent) {
        if (event.item.type != Material.OMINOUS_BOTTLE) return
        val bottleMeta = event.item.itemMeta as OminousBottleMeta
        val level = bottleMeta.amplifier + 1
    }



}