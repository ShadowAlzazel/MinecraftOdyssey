package me.shadowalzazel.mcodyssey.commands.admin


import me.shadowalzazel.mcodyssey.items.Arcane
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveItem : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        // Switch
        val itemToGive: OdysseyItem? = when (args[0])  {
            "tome_of_promotion" -> {
                Arcane.TOME_OF_PROMOTION
            }
            "tome_of_embrace" -> {
                Arcane.TOME_OF_EMBRACE
            }
            else -> {
                null
            }
        }
        // Check
        if (itemToGive != null) {
            val amount = minOf(64, maxOf(1, args[1].toInt()))
            val item = itemToGive.createItemStack(amount)
            sender.inventory.addItem(item)
        }
        else {
            return false
        }
        return true
    }
}
