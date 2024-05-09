package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

object WorldEventsListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun spawningHandler(event: CreatureSpawnEvent) {
        if (!Odyssey.instance.worldEventManager.isDailyEventActive) return
        val dailyEvent = Odyssey.instance.worldEventManager.currentDailyEvent!!
        dailyEvent.persistentSpawningHandler(event)
    }


}