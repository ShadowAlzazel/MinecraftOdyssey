package me.shadowalzazel.mcodyssey.phenomenon

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.scheduler.BukkitRunnable

class PersistentPhenomenonHandler : BukkitRunnable() {

    override fun run() {
        // Check if active for persistent
        with(MinecraftOdyssey.instance) {
            if (isSolarPhenomenonActive) {
                currentSolarPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
            else if (isLunarPhenomenonActive) {
                currentLunarPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
        }
    }

    // TODO: If near allay, prevent effects
}