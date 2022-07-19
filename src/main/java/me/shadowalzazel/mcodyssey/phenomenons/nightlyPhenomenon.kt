package me.shadowalzazel.mcodyssey.phenomenons

import me.shadowalzazel.mcodyssey.phenomenons.utility.Phenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

open class NightlyPhenomenon(name: String, rate: Int, growthRate: Int, warning: Int) : Phenomenon(name, rate, growthRate, warning) {

    override fun phenomenonActivation(phenomenonWorld: World, rollRate: Int): Boolean {

        val nightMessages = listOf<String>("The night remains bleak...")
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

class BloodMoon : NightlyPhenomenon("BloodMoon", 15, 3, 0) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blood moon is happening at ${phenomenonWorld.name}!")
        // Blood Moon Effects
        val blueMoonEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 1)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(blueMoonEffect)
            aPlayer.sendMessage("${ChatColor.BLUE}A blue moon illuminates the night...")
        }

    }

}

class BlueMoon : NightlyPhenomenon("BlueMoon", 15, 3, 0) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blood moon is happening at ${phenomenonWorld.name}!")
        // Blue Moon Effects
        val bloodMoonEffect = PotionEffect(PotionEffectType.UNLUCK, 12000, 0)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(bloodMoonEffect)
            aPlayer.sendMessage("${ChatColor.DARK_RED}A blood moon is happening!")
        }


    }

}