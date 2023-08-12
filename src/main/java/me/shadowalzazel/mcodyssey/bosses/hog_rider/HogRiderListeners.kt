package me.shadowalzazel.mcodyssey.bosses.hog_rider

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.entity.Hoglin
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent

object HogRiderListeners : Listener {


    /*
    @EventHandler
    fun onHogTakeDamage(event: EntityDamageByEntityEvent) {
        if (Odyssey.instance.isBossActive) {
            if (Odyssey.instance.worldBoss is HogRiderBoss) {
                val hogRiderBoss = Odyssey.instance.worldBoss as HogRiderBoss
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
        if (Odyssey.instance.isBossActive) {
            if (Odyssey.instance.worldBoss is HogRiderBoss) {
                val hogRiderBoss = Odyssey.instance.worldBoss as HogRiderBoss
                val hogRiderBossID = hogRiderBoss.bossEntityRider!!.uniqueId
                if (event.entity.uniqueId == hogRiderBossID) {
                    // Check if Player Kill
                    if (event.entity.killer is Player) {
                        //hogRiderBoss.defeatedBoss(hogRiderBoss.bossEntity!!, event.entity.killer as Player)
                        Odyssey.instance.isBossActive = false
                        Odyssey.instance.isAmbassadorDefeated = true
                        Odyssey.instance.worldBoss = null
                        Odyssey.instance.bossDespawnTimer = System.currentTimeMillis()
                    }
                    // Last Hit
                    else {
                        val vanquisher = event.entity.getTargetEntity(10)
                        if (vanquisher != null) {
                            if (vanquisher is Player) {
                                //hogRiderBoss.defeatedBoss(hogRiderBoss.bossEntity!!, vanquisher)
                                Odyssey.instance.isBossActive = false
                                Odyssey.instance.isAmbassadorDefeated = true
                                Odyssey.instance.worldBoss = null
                                Odyssey.instance.bossDespawnTimer = System.currentTimeMillis()
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

     */





}