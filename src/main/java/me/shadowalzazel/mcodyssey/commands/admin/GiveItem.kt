package me.shadowalzazel.mcodyssey.commands.admin


import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveItem : CommandExecutor, ItemCreator {

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
        val item = createItemFromName(itemName, amount) ?: return false // MAYBE CREATE A LAMBDA TO MATCH TO DIFFERENT CREATE FUNCTIONS
        player.inventory.addItem(item)
        return true
    }
}
