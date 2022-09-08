package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.SuenPhenomena
import me.shadowalzazel.mcodyssey.phenomenon.UtuPhenomena
import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TriggerPhenomenon : CommandExecutor {

    //private var spawnCooldown = mutableMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.size == 1) {
                val someWorld = sender.location.world
                var somePhenomenon: OdysseyPhenomenon? = null
                when (args[0]) {
                    "blood_moon" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.BLOOD_MOON
                        }
                    }
                    "breezy_winds" -> {
                        somePhenomenon = UtuPhenomena.BREEZY_WINDS
                    }
                    "dance_of_the_bio" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.DANCE_OF_THE_BIOLUMINESCENT
                        }
                    }
                }
                if (somePhenomenon!!.phenomenonType == PhenomenonTypes.SUEN) {
                    somePhenomenon.successfulActivation(someWorld)
                    MinecraftOdyssey.instance.currentSuenPhenomenon = somePhenomenon
                    MinecraftOdyssey.instance.suenPhenomenonActive = true
                }
                else if (somePhenomenon.phenomenonType == PhenomenonTypes.UTU) {
                    somePhenomenon.successfulActivation(someWorld)
                    MinecraftOdyssey.instance.currentUtuPhenomenon = somePhenomenon
                    MinecraftOdyssey.instance.utuPhenomenonActive = true
                }
            }
        }
        return true
    }
}