package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.common.mobs.OdysseyMobs
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SummonMob : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args.size != 1) return false
        // Variables
        val mobName = args[0]
        val location = sender.location
        val world = sender.world
        val entity = OdysseyMobs.spawn(mobName, world, location)
        if (entity == null) {
            sender.sendMessage("Unknown mob: $mobName")
            return false
        }

        return true
    }

}