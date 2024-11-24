package me.shadowalzazel.mcodyssey.server.commands.admin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SummonDoppelganger : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 1) return false
        val playerName = args[0]
        //if (sender.server.getOfflinePlayer(playerName).isOnline)

        return true
    }
}