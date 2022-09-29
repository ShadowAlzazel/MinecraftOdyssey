package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.SuenPhenomena
import me.shadowalzazel.mcodyssey.phenomenon.UtuPhenomena
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
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
                    "blue_moon" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.BLUE_MOON
                        }
                    }
                    "blood_moon" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.BLOOD_MOON
                        }
                    }
                    "breezy_winds" -> {
                        somePhenomenon = UtuPhenomena.BREEZY_WINDS
                    }
                    "slime_rain" -> {
                        somePhenomenon = UtuPhenomena.SLIME_SHOWER
                    }
                    "dance_of_the_bio" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.DANCE_OF_THE_BIOLUMINESCENT
                        }
                    }
                    "abyss" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.ABYSSAL_NIGHT
                        }
                    }
                    "acid_rain" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.ACIDIC_DOWNPOUR
                        }
                    }
                    "chill" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.CHILLING_NIGHT
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