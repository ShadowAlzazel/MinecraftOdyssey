package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import me.shadowalzazel.mcodyssey.items.creators.ToolCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveWeapon : CommandExecutor, ItemCreator {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val isOp = sender is Player && sender.isOp
        val isCommandBlock = sender is BlockCommandSender
        if (!isOp && !isCommandBlock) return false
        if (args == null) return false
        if (args.size != 3) return false
        // Variables
        val player = sender.server.getPlayer(args[0]) ?: return false
        val material = args[1]
        val tool = args[2]
        val foundMaterial = ToolMaterial.valueOf(material.uppercase())
        // Create tools
        val toolCreator = ToolCreator()
        // give all
        if (tool == "*") {
            for (toolType in ToolType.entries) {
                val weapon = toolCreator.createToolStack(foundMaterial, toolType)
                player.inventory.addItem(weapon)
            }
            return true
        }
        // Give one specific
        val foundTool = ToolType.valueOf(tool.uppercase())
        val weapon = toolCreator.createToolStack(foundMaterial, foundTool)
        player.inventory.addItem(weapon)
        return true
    }

}