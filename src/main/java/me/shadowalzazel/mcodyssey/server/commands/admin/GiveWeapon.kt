package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.common.items.ToolMaker
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveWeapon : CommandExecutor, ToolMaker {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val isOp = sender is Player && sender.isOp
        val isCommandBlock = sender is BlockCommandSender
        if (!isOp && !isCommandBlock) return false
        if (args.size != 3) return false
        // Variables
        val player = sender.server.getPlayer(args[0]) ?: return false
        val material = args[1]
        val tool = args[2]
        val foundMaterial = ToolMaterial.valueOf(material.uppercase())
        // give all
        if (tool == "*") {
            for (toolType in ToolType.entries) {
                val weapon = createToolStack(foundMaterial, toolType)
                player.inventory.addItem(weapon)
            }
            return true
        }
        // Give one specific
        val foundTool = ToolType.valueOf(tool.uppercase())
        val weapon = createToolStack(foundMaterial, foundTool)
        player.inventory.addItem(weapon)
        return true
    }

}