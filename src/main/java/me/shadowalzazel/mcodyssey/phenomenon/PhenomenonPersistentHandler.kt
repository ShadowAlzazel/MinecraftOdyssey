package me.shadowalzazel.mcodyssey.phenomenon

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.scheduler.BukkitRunnable

class PhenomenonPersistentHandler : BukkitRunnable() {

    override fun run() {
        // Check if active for persistent
        with(MinecraftOdyssey.instance) {
            if (utuPhenomenonActive) {
                currentUtuPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
            else if (suenPhenomenonActive) {
                currentSuenPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
        }
    }
}