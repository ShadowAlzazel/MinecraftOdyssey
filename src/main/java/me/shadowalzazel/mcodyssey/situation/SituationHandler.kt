package me.shadowalzazel.mcodyssey.situation

import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable

class SituationHandler(private val mainWorld: World) : BukkitRunnable() {

    override fun run() {
        // Check day times for main world
        if ((23500L <= mainWorld.time) || (mainWorld.time <= 1500L)) {
            // TODO: Temp; make list later
            if (mainWorld.players.isNotEmpty()) {
                val someQ = mainWorld.players.random().location
                Situations.PUMPKIN_HARVEST.runSituation(someQ)
            }

        }

    }


}