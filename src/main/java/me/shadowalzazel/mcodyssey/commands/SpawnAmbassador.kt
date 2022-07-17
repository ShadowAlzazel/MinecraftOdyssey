package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

object SpawnAmbassador : CommandExecutor {

    private var spawnCooldown = mutableMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val aPlayer = sender
            if (aPlayer.isOp) {
                if (!MinecraftOdyssey.instance.activeBoss) {
                    if (!spawnCooldown.containsKey(aPlayer.uniqueId)) {
                        spawnCooldown[aPlayer.uniqueId] = System.currentTimeMillis()
                        MinecraftOdyssey.instance.activeBoss = true
                        println("${aPlayer.name}Spawned the Ambassador")
                    }
                    else {
                        val timeElapsed: Long = System.currentTimeMillis()- spawnCooldown[aPlayer.uniqueId]!!
                        if (timeElapsed >= 5000) {
                            spawnCooldown[aPlayer.uniqueId] = System.currentTimeMillis()
                            MinecraftOdyssey.instance.activeBoss = true
                            println("${aPlayer.name}Spawned the Ambassador")
                        }
                        else {
                            println("Cannot Spawn anymore Bosses")
                            return false
                        }
                    }
                    return true
                }
            }
        }
        return false
    }
}

