package me.shadowalzazel.mcodyssey.commands

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.generator.structure.Structure
import org.bukkit.generator.structure.StructureType

object LocateStructureAsync : CommandExecutor {

    //private var spawnCooldown = mutableMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.size == 1) {
                when (args[0]) {
                    "ruined_portal" -> {
                        val somePortal = sender.world.locateNearestStructure(sender.location, StructureType.RUINED_PORTAL, 320, false)
                        if (somePortal != null) {
                            println("${somePortal.structure} at ${somePortal.location}")
                        }
                    }
                }
            }
        }
        return true
    }
}