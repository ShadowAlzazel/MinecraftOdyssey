package me.shadowalzazel.mcodyssey.commands.admin


import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveItem : CommandExecutor, ItemCreator {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        // Variables
        val itemName = args[0]
        val amount = minOf(64, maxOf(1, args[1].toInt()))
        // Change to Base
        val item = createItemFromName(itemName, amount) ?: return false // MAYBE CREATE A LAMBDA TO MATCH TO DIFFERENT CREATE FUNCTIONS
        sender.inventory.addItem(item)
        return true
    }
}
