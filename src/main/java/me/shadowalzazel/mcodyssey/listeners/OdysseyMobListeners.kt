package me.shadowalzazel.mcodyssey.listeners

import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

object OdysseyMobListeners : Listener {

    @EventHandler
    fun mainSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            when (event.entity) {
                is WanderingTrader -> {

                }

            }
        }
    }

}