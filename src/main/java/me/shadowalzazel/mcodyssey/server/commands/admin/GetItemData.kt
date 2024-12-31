package me.shadowalzazel.mcodyssey.server.commands.admin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player

object GetItemData : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        val item = sender.inventory.itemInMainHand
        if (!item.hasItemMeta()) return false
        val itemByte = item.serializeAsBytes() // For yggdrasil
        val itemAsNMS = CraftItemStack.asNMSCopy(item)
        val itemComponents = itemAsNMS.components
        for (component in itemComponents) {
            val type = component.type
            val value = component.value
            println("Component [$type]: [$value]")
        }
        return true
    }

}