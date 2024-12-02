package me.shadowalzazel.mcodyssey.server.commands.admin


import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveItem : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val isOp = sender is Player && sender.isOp
        val isCommandBlock = sender is BlockCommandSender
        if (!isOp && !isCommandBlock) return false
        if (args == null) return false
        if (args.size > 3) return false
        // Variables
        val player = sender.server.getPlayer(args[0]) ?: return false
        val itemName = args[1]
        val argAmount = if (args.size == 3) args[2].toInt() else 1
        val amount = minOf(64, maxOf(1, argAmount))
        // Change to Base
        //val item = ITemS
       // player.inventory.addItem(item)
        //return true
        return true
    }
}
