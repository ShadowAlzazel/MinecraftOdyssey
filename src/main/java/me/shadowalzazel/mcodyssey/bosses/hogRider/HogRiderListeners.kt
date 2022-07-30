package me.shadowalzazel.mcodyssey.bosses.hogRider

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.hogRider.HogRiderBoss
import org.bukkit.entity.Hoglin
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent

object HogRiderListeners : Listener {


    @EventHandler
    fun onHogTakeDamage(event: EntityDamageByEntityEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is HogRiderBoss) {
                val hogRiderBoss = MinecraftOdyssey.instance.currentBoss as HogRiderBoss
                val hogRiderBossID = hogRiderBoss.bossEntityRider!!.uniqueId

                if (event.entity is Hoglin) {
                    val warthogID = hogRiderBoss.bossEntityMount!!.uniqueId
                    if (event.entity.uniqueId == warthogID) {
                        hogRiderBoss.hogRiderAttacks(event.entity.passengers[0] as PiglinBrute)
                    }
                }
                else if (event.entity.uniqueId == hogRiderBossID) {
                    hogRiderBoss.hogRiderAttacks(event.entity as PiglinBrute)

                }
            }
        }
    }



    @EventHandler
    fun onHogRiderDeath(event: EntityDeathEvent) {
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