package me.shadowalzazel.mcodyssey.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PostItem : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        val item = sender.inventory.itemInMainHand
        if (!item.hasItemMeta()) return false
        // Send message
        return true
    }

}