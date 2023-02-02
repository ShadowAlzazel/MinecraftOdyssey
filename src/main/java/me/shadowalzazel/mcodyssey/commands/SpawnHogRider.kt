package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.hog_rider.HogRiderBoss
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object SpawnHogRider : CommandExecutor {

    private var spawnCooldown = mutableMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (sender.isOp) {
                if (!MinecraftOdyssey.instance.activeBoss) {
                    if (!spawnCooldown.containsKey(sender.uniqueId)) {
                        spawnCooldown[sender.uniqueId] = System.currentTimeMillis()
                        MinecraftOdyssey.instance.currentBoss = HogRiderBoss()
                        MinecraftOdyssey.instance.activeBoss = true
                        val hogRiderBoss = MinecraftOdyssey.instance.currentBoss as HogRiderBoss
                        hogRiderBoss.createBoss(sender.world, sender.location)
                        println("${sender.name}Spawned HOG RIDERRRR!!!!!!!!!!!!!!!")
                    }
                    else {
                        val timeElapsed: Long = System.currentTimeMillis()- spawnCooldown[sender.uniqueId]!!
                        if (timeElapsed >= 5000) {
                            spawnCooldown[sender.uniqueId] = System.currentTimeMillis()
                            MinecraftOdyssey.instance.currentBoss = HogRiderBoss()
                            MinecraftOdyssey.instance.activeBoss = true
                            val hogRiderBoss = MinecraftOdyssey.instance.currentBoss as HogRiderBoss
                            hogRiderBoss.createBoss(sender.world, sender.location)
                            println("${sender.name}Spawned HOG RIDERRRR!!!!!!!!!!!!!!!")
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