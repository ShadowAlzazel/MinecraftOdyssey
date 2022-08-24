package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.utility.Phenomenon
import org.bukkit.ChatColor
import org.bukkit.World

open class SolarPhenomenon(name: String, rate: Int, growthRate: Int, warning: Int) : Phenomenon(name, rate, growthRate, warning) {

    private val dayMessages = listOf("An uneventful day proceeds...", "Looks like nothing is happening today...", "MULTIPLE HOSTILES INCOM.. False alarm. Nothing is going on.",
        "What is next...", "Hello World!", "Just another ordinary day...", "The forecast predicts... Nothing.",
        "No distinct events are Schedu-Predicted for today.", "The standard cycle was not disturbed...", "A normal day follows...", "Just plain Today...", "The only thing that happens is nothing at all")

    override val hasWarning: Boolean = true

    // Activation Check function
    override fun phenomenonActivation(phenomenonWorld: World, rollRate: Int): Boolean {
        val randomMessage = dayMessages.random()

        // Activate Effects if rolled
        if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
            occurrenceRate = occurrenceRestartRate
            return true
        }
        else {
            MinecraftOdyssey.instance.logger.info("$phenomenonName Solar Phenomenon Did Not Occur")
            // Send daily fail message
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("${ChatColor.ITALIC}$randomMessage")
            }
            // Grow Fail Rate
            occurrenceRate += occurrenceFailGrowthRate
            // Check if event has a warning
            if (hasWarning){
                if (occurrenceRate >= warningThreshold) {
                    // Warning Message
                    val failMessage: String = phenomenonWarning(phenomenonWorld)
                    for (aPlayer in phenomenonWorld.players) {
                        aPlayer.sendMessage("${ChatColor.ITALIC}$failMessage")
                    }
                }
            }
            return false
        }
    }
    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "Found an Exception! :D"
    }

}