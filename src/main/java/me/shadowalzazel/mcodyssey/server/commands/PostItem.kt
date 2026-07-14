package me.shadowalzazel.mcodyssey.server.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PostItem : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED))
            return true
        }

        val item = sender.inventory.itemInMainHand

        if (item.type == Material.AIR) {
            sender.sendMessage(Component.text("You aren't holding anything!", NamedTextColor.RED))
            return true
        }

        // Get display name (custom name if set, otherwise the item's default translated name)
        val displayName = if (item.hasItemMeta() && item.itemMeta.hasDisplayName()) {
            item.itemMeta.displayName()!!
        } else {
            item.effectiveName()
        }

        // Build the hoverable component
        val itemComponent = displayName
            .decoration(TextDecoration.ITALIC, false)
            .hoverEvent(item.asHoverEvent())

        // Build the full chat message: "<PlayerName> shows off [Item Name]"
        val message = Component.text()
            .append(Component.text(sender.name, NamedTextColor.YELLOW))
            .append(Component.text(" shows off ", NamedTextColor.GRAY))
            .append(itemComponent)
            .build()

        Bukkit.broadcast(message)

        return true
    }
}