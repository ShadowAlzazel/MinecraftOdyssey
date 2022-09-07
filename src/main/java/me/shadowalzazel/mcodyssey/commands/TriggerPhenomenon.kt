package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.SuenPhenomena
import me.shadowalzazel.mcodyssey.phenomenon.UtuPhenomena
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
                var somePhenomenon: Phenomenon? = null
                when (args[0]) {
                    "blood_moon" -> {
                        if (someWorld.time > 12000) {
                            somePhenomenon = SuenPhenomena.BLOOD_MOON
                        }
                    }
                    "breezy_winds" -> {
                        somePhenomenon = UtuPhenomena.BREEZY_WINDS
                    }
                }
                if (somePhenomenon is LunarPhenomenon) {
                    somePhenomenon.phenomenonEffect(someWorld)
                    MinecraftOdyssey.instance.currentLunarPhenomenon = somePhenomenon
                    MinecraftOdyssey.instance.lunarPhenomenonActive = true
                }
                else if (somePhenomenon is SolarPhenomenon) {
                    somePhenomenon.phenomenonEffect(someWorld)
                    MinecraftOdyssey.instance.currentSolarPhenomenon = somePhenomenon
                    MinecraftOdyssey.instance.solarPhenomenonActive = true
                }
            }
        }
        return true
    }
}