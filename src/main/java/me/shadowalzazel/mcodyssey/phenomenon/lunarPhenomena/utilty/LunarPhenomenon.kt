package me.shadowalzazel.mcodyssey.phenomenon.lunarPhenomena.utilty

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.utility.Phenomenon
import org.bukkit.ChatColor
import org.bukkit.World

open class LunarPhenomenon(name: String, rate: Int, growthRate: Int, warning: Int) : Phenomenon(name, rate, growthRate, warning) {

    private val nightMessages = listOf("The night remains bleak...")

    override fun phenomenonActivation(phenomenonWorld: World, rollRate: Int): Boolean {

        val randomMessage = nightMessages.random()

        if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
            occurrenceRate = occurrenceRestartRate
            return true
        }
        else {
            MinecraftOdyssey.instance.logger.info("No Nightly Phenomenon Occur")
            // Send nightly fail message
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("${ChatColor.ITALIC}$randomMessage")
            }
            occurrenceRate += occurrenceFailGrowthRate

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
