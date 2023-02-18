package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object PlaceOdysseyStructure : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.size == 1) {
                when (args[0]) {
                    "stone_pillars" -> {
                        sender.server.structureManager.loadStructure(NamespacedKey(Odyssey.instance,"stone_pillars_1"))?.place(sender.location, true, StructureRotation.NONE, Mirror.NONE, 0, 1F, Random(2121L))
                    }
                }
            }
        }
        return true
    }

}