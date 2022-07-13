package me.shadowalzazel.mcodyssey.phenomenons

import org.bukkit.ChatColor
import org.bukkit.World

open class NightlyPhenomenon(name: String, rate: Int) : Phenomenon(name, rate) {

    override fun phenomenonActivation(phenomenonWorld: World, rollRate: Int){

        val nightMessages = listOf<String>("The night remains bleak...")
        val randomMessage = nightMessages.random()

        if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
            occurrenceRate = 10
        }
        else {
            println("No Nightly Phenomenon Occur")

            // Send daily fail message
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("${ChatColor.ITALIC}$randomMessage")
            }
            occurrenceRate += 8

            // Check if event has great chance of occurring
            if (occurrenceRate > 80) {
                if (hasWarning) {
                    val failMessage: String = phenomenonWarning(phenomenonWorld)
                    for (aPlayer in phenomenonWorld.players) {
                        aPlayer.sendMessage("${ChatColor.ITALIC}$failMessage")
                    }
                }
            }
        }
    }
}

class BloodMoon : NightlyPhenomenon("BloodMoon", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blood moon is happening at ${phenomenonWorld.name}!")

    }

}

class BlueMoon : NightlyPhenomenon("BlueMoon", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blue moon is happening at ${phenomenonWorld.name}!")
        //trigger on bed click

    }

}