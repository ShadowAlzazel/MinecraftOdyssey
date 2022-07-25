package me.shadowalzazel.mcodyssey.bosses.theAmbassador

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.entity.Illusioner
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.world.TimeSkipEvent

object AmbassadorListeners: Listener {

    @EventHandler
    fun appeasementItemDropCheck(event: PlayerDropItemEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
                val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                if (ambassadorBoss.patience > 0) {
                    val playersNearAmbassador = ambassadorBoss.bossEntity!!.world.getNearbyPlayers(ambassadorBoss.bossEntity!!.location, 1.5)
                    if (event.player in playersNearAmbassador) {
                        ambassadorBoss.appeasementCheck(event.player, event.itemDrop)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onElytraActivation(event: PlayerElytraBoostEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
                val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                if (ambassadorBoss.patience <= 0) {
                    val playersNearAmbassador = ambassadorBoss.bossEntity!!.world.getNearbyPlayers(ambassadorBoss.bossEntity!!.location, 12.5)
                    if (event.player in playersNearAmbassador) {
                        event.isCancelled = true
                        ambassadorBoss.voidPullBackAttack(event.player)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onAmbassadorTakeDamage(event: EntityDamageByEntityEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
                val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                val ambassadorEntity: Illusioner = ambassadorBoss.bossEntity as Illusioner
                if (event.entity == ambassadorEntity) {
                    ambassadorBoss.detectDamage(event.damager, event.damage)

                }
            }
        }
    }

    @EventHandler
    fun checkAmbassadorDay(event: TimeSkipEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
                val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                val timeElapsed = System.currentTimeMillis() - ambassadorBoss.despawnTimer
                val globalTimeElapsed = System.currentTimeMillis() - MinecraftOdyssey.instance.bossDespawnTimer
                if (timeElapsed >= 3000000 && globalTimeElapsed > 3000000) { //3000000
                    ambassadorBoss.departBoss()
                    ambassadorBoss.bossEntity!!.remove()
                    MinecraftOdyssey.instance.activeBoss = false
                    MinecraftOdyssey.instance.ambassadorDefeated = true
                    MinecraftOdyssey.instance.currentBoss = null
                }
            }
        }

    }

    @EventHandler
    fun onAmbassadorDeath(event: EntityDeathEvent) {
        if (MinecraftOdyssey.instance.activeBoss) {
            if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
                val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                val ambassadorBossID = ambassadorBoss.bossEntity!!.uniqueId
                if (event.entity.uniqueId == ambassadorBossID) {
                    // Check if Player Kill
                    if (event.entity.killer is Player) {
                        ambassadorBoss.defeatedBoss(ambassadorBoss.bossEntity!!, event.entity.killer as Player)
                        MinecraftOdyssey.instance.activeBoss = false
                        MinecraftOdyssey.instance.ambassadorDefeated = true
                        MinecraftOdyssey.instance.currentBoss = null
                    }
                    // Last Hit
                    else {
                        val vanquisher = event.entity.getTargetEntity(10)
                        if (vanquisher != null) {
                            if (vanquisher is Player) {
                                ambassadorBoss.defeatedBoss(ambassadorBoss.bossEntity!!, vanquisher)
                                MinecraftOdyssey.instance.activeBoss = false
                                MinecraftOdyssey.instance.ambassadorDefeated = true
                                MinecraftOdyssey.instance.currentBoss = null
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