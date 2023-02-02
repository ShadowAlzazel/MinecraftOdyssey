package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.FireworkExplodeEvent
import org.bukkit.event.player.PlayerDropItemEvent

object AmbassadorListeners: Listener {

    // Function to check if current boss is ambassador
    private fun ambassadorActiveCheck(): Boolean {
        with(MinecraftOdyssey.instance) {
            return (isBossActive && currentBoss is AmbassadorBoss) && ((currentBoss as AmbassadorBoss).bossActive)
        }
    }

    @EventHandler
    fun superFireworkExplode(event: FireworkExplodeEvent) {
        if (ambassadorActiveCheck() && event.entity.scoreboardTags.contains("super_firework")) {
            with(event.entity.world) {
                val boomLocation = event.entity.location
                playSound(boomLocation, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
                playSound(boomLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.5F, 0.8F)
                playSound(boomLocation, Sound.ENTITY_IRON_GOLEM_DEATH, 2.0F, 0.8F)
                spawnParticle(Particle.FLASH, boomLocation, 5, 1.0, 1.0, 1.0)
                spawnParticle(Particle.LAVA, boomLocation, 35, 1.5, 1.0, 1.5)
            }
            event.entity.getNearbyEntities(2.5, 2.5, 2.5).forEach {
                if (it is LivingEntity) {
                    it.damage(18.5)
                    it.world.spawnParticle(Particle.CRIT_MAGIC, it.location, 45, 1.5, 1.0, 1.5)
                }
            }
        }
    }

    @EventHandler
    fun itemGiftHandler(event: PlayerDropItemEvent) {
        if (ambassadorActiveCheck()) {
            val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
            if (!ambassadorBoss.angered) {
                if (event.player in ambassadorBoss.bossEntity!!.getNearbyEntities(1.5, 1.5, 1.5)) {
                    ambassadorBoss.appeasementCheck(event.player, event.itemDrop)
                }
            }
        }
    }

    @EventHandler
    fun onElytraActivation(event: PlayerElytraBoostEvent) {
        if (ambassadorActiveCheck()) {
            val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
            if (!ambassadorBoss.angered) {
                if (event.player in ambassadorBoss.bossEntity!!.getNearbyEntities(1.5, 1.5, 1.5)) {
                    event.isCancelled
                    ambassadorBoss.voidPullBackAttack(event.player)
                }
            }
        }
    }

    @EventHandler
    fun onAmbassadorTakeDamage(event: EntityDamageByEntityEvent) {
        if (ambassadorActiveCheck()) {
            val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
            if (event.entity.uniqueId == ambassadorBoss.bossEntity!!.uniqueId) {
                ambassadorBoss.damageHandler(event.damager, event.damage)
            }
        }
    }

    @EventHandler
    fun onAmbassadorDeath(event: EntityDeathEvent) {
        if (ambassadorActiveCheck()) {
            val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
            if (event.entity.uniqueId == ambassadorBoss.bossEntity!!.uniqueId) {
                ambassadorBoss.defeatedBoss(ambassadorBoss.bossEntity!!, event.entity.killer)
                MinecraftOdyssey.instance.also {
                    it.isBossActive = false
                    it.isAmbassadorDefeated = true
                    it.currentBoss = null
                    it.bossDespawnTimer = System.currentTimeMillis()
                }
            }
        }
    }


}