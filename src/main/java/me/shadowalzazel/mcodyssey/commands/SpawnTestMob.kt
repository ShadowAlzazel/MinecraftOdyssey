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
            if (args.size == 1) {
                when (args[0]) {
                    "savage" -> {
                        OdysseyMobs.SAVAGE.createMob(sender.world, sender.location)
                    }
                    "vanguard" -> {
                        OdysseyMobs.VANGUARD.createMob(sender.world, sender.location)
                    }
                    "sculk_crawler" -> {
                        OdysseyMobs.SCULK_CRAWLER.createMob(sender.world, sender.location)
                    }
                    "treasure_pig" -> {
                        OdysseyMobs.TREASURE_PIG.createMob(sender.world, sender.location)
                    }
                    "preacher" -> {
                        OdysseyMobs.PREACHER_OF_THE_ABYSS.createMob(sender.world, sender.location)
                    }
                }
            }
        }
        return true
    }
}