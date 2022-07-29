package me.shadowalzazel.mcodyssey.bosses.hogRider

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.hogRider.HogRiderBoss
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

object HogRiderListeners : Listener {

    @EventHandler
    fun onAmbassadorDeath(event: EntityDeathEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is HogRiderBoss) {
                val hogRiderBoss = MinecraftOdyssey.instance.currentBoss as HogRiderBoss
                val hogRiderBossID = hogRiderBoss.bossEntityRider!!.uniqueId
                if (event.entity.uniqueId == hogRiderBossID) {
                    // Check if Player Kill
                    if (event.entity.killer is Player) {
                        //hogRiderBoss.defeatedBoss(hogRiderBoss.bossEntity!!, event.entity.killer as Player)
                        MinecraftOdyssey.instance.activeBoss = false
                        MinecraftOdyssey.instance.ambassadorDefeated = true
                        MinecraftOdyssey.instance.currentBoss = null
                        MinecraftOdyssey.instance.bossDespawnTimer = System.currentTimeMillis()
                    }
                    // Last Hit
                    else {
                        val vanquisher = event.entity.getTargetEntity(10)
                        if (vanquisher != null) {
                            if (vanquisher is Player) {
                                //hogRiderBoss.defeatedBoss(hogRiderBoss.bossEntity!!, vanquisher)
                                MinecraftOdyssey.instance.activeBoss = false
                                MinecraftOdyssey.instance.ambassadorDefeated = true
                                MinecraftOdyssey.instance.currentBoss = null
                                MinecraftOdyssey.instance.bossDespawnTimer = System.currentTimeMillis()
                            }
                            else {
                                event.isCancelled = true
                            }
                        }
                        else {
                            event.isCancelled = true
                        }
                    }
                }
            }
        }
    }





}