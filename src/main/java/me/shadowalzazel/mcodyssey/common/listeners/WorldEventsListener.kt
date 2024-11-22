package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.OminousBottleMeta

@Suppress("UnstableApiUsage")
object WorldEventsListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun spawningHandler(event: CreatureSpawnEvent) {
        if (!Odyssey.instance.worldEventManager.isDailyEventActive) return
        val dailyEvent = Odyssey.instance.worldEventManager.currentDailyEvent!!
        dailyEvent.persistentSpawningHandler(event)
    }


    @EventHandler
    fun ominousBottleHandler(event: PlayerItemConsumeEvent) {
        if (event.item.type != Material.OMINOUS_BOTTLE) return
        val bottle = event.item
        val bottleMeta = event.item.itemMeta as OminousBottleMeta
        val amplifier = bottleMeta.amplifier
    }


}