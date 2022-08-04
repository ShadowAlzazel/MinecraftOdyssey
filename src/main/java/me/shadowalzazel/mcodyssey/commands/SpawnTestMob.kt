package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SpawnTestMob : CommandExecutor {

    //private var spawnCooldown = mutableMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            OdysseyMobs.VANGUARD.createMob(sender.world, sender.location)
        }
        return true
    }
}