package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.bosses.the_ambassador.AmbassadorBoss
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

object SpawnAmbassador : CommandExecutor {

    private var spawnCooldown = mutableMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (sender.isOp) {
                if (!Odyssey.instance.isBossActive) {
                    if (!spawnCooldown.containsKey(sender.uniqueId)) {
                        spawnCooldown[sender.uniqueId] = System.currentTimeMillis()
                        Odyssey.instance.currentBoss = AmbassadorBoss()
                        Odyssey.instance.isBossActive = true
                        val ambassadorBoss = Odyssey.instance.currentBoss as AmbassadorBoss
                        ambassadorBoss.createBoss(sender.world)
                        println("${sender.name}Spawned the Ambassador")
                    }
                    else {
                        val timeElapsed: Long = System.currentTimeMillis()- spawnCooldown[sender.uniqueId]!!
                        if (timeElapsed >= 5000) {
                            spawnCooldown[sender.uniqueId] = System.currentTimeMillis()
                            Odyssey.instance.currentBoss = AmbassadorBoss()
                            Odyssey.instance.isBossActive = true
                            val ambassadorBoss = Odyssey.instance.currentBoss as AmbassadorBoss
                            ambassadorBoss.createBoss(sender.world)
                            println("${sender.name}Spawned the Ambassador")
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

