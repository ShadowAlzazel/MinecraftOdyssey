package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.the_ambassador.AmbassadorBoss
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.TimeSkipEvent

object OdysseyBossListeners : Listener {

    // Main function for calling a new boss
    @EventHandler
    fun newBoss(event: TimeSkipEvent) {
        MinecraftOdyssey.instance.run {
            if (!isBossActive) {
                val timeElapsed = System.currentTimeMillis() - timeSinceBoss
                if (timeElapsed >= 90000000) {
                    when ((0..4).random()) {
                        // For all boss RNG
                        0 -> {
                            timeSinceBoss = System.currentTimeMillis()
                            currentBoss = AmbassadorBoss()
                            isBossActive = true
                            (currentBoss as AmbassadorBoss).createBoss(mainWorld!!)
                            println("${mainWorld!!.name}Spawned the Ambassador")
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

}