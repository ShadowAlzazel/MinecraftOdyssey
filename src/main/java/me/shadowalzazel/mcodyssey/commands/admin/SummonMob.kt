package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.common.mobs.hostile.Preacher
import me.shadowalzazel.mcodyssey.common.mobs.hostile.Ruined
import me.shadowalzazel.mcodyssey.common.mobs.hostile.Savage
import me.shadowalzazel.mcodyssey.common.mobs.hostile.Vanguard
import me.shadowalzazel.mcodyssey.common.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.common.mobs.passive.TreasurePig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SummonMob : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 1) return false
        // Variables
        val mobName = args[0]
        val location = sender.location
        val world = sender.world
        //val amount = minOf(64, maxOf(1, args[1].toInt()))
        when(mobName) {
            "savage" -> {
                Savage.createMob(world, location)
            }
            "savage_knight" -> {
                Savage.createKnight(world, location)
            }
            "vanguard" -> {
                Vanguard.createMob(world, location)
            }
            "vanguard_knight" -> {
                Vanguard.createKnight(world, location)
            }
            "preacher" -> {
                Preacher.createMob(world, location)
            }
            "ruined" -> {
                Ruined.createMob(world, location)
            }
            "treasure_pig" -> {
                TreasurePig.createMob(world, location)
            }
            "dubious_dealer" -> {
                DubiousDealer.createMob(world, location)
            }
        }

        return true
    }

}