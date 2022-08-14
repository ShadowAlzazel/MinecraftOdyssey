package me.shadowalzazel.mcodyssey.phenomenon.nightlyPhenomena.utilty

import me.shadowalzazel.mcodyssey.phenomenon.utility.Phenomenon
import org.bukkit.ChatColor
import org.bukkit.World

open class NightlyPhenomenon(name: String, rate: Int, growthRate: Int, warning: Int) : Phenomenon(name, rate, growthRate, warning) {

    override fun phenomenonActivation(phenomenonWorld: World, rollRate: Int): Boolean {

        val nightMessages = listOf("The night remains bleak...")
        val randomMessage = nightMessages.random()

        if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
            occurrenceRate = occurranceRestartRate
            return true
        }
        else {
            println("No Nightly Phenomenon Occur")
            // Send nightly fail message
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("${ChatColor.ITALIC}$randomMessage")
            }
            occurrenceRate += occurranceFailGrowthRate

            // Check if event has great chance of occurring
            if (occurrenceRate > warningThreshold) {
                if (hasWarning) {
                    val failMessage: String = phenomenonWarning(phenomenonWorld)
                    for (aPlayer in phenomenonWorld.players) {
                        aPlayer.sendMessage("${ChatColor.ITALIC}$failMessage")
                    }
                }
            }
            return false
        }
    }
}
