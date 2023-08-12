package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
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
    private fun isActive(): Boolean {
        with(Odyssey.instance) {
            return (isBossActive && worldBoss is AmbassadorBoss) && ((worldBoss as AmbassadorBoss).bossActive)
        }
    }

    private fun isAngered(): Boolean {
        if (!isActive()) return false
        val boss = Odyssey.instance.worldBoss
        if (boss is AmbassadorBoss && boss.isAngered) return true
        return false
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun superFireworkHandler(event: FireworkExplodeEvent) {
        if (!isActive()) return
        if (!event.entity.scoreboardTags.contains(EntityTags.SUPER_FIREWORK)) return

        with(event.entity.world) {
            playSound(event.entity.location, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
            playSound(event.entity.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.5F, 0.8F)
            playSound(event.entity.location, Sound.ENTITY_IRON_GOLEM_DEATH, 2.0F, 0.8F)
            spawnParticle(Particle.FLASH, event.entity.location, 5, 1.0, 1.0, 1.0)
            spawnParticle(Particle.LAVA, event.entity.location, 35, 1.5, 1.0, 1.5)
        }
        event.entity.getNearbyEntities(2.5, 2.5, 2.5).forEach {
            if (it is LivingEntity) {
                it.damage(18.5)
                it.world.spawnParticle(Particle.CRIT_MAGIC, it.location, 95, 1.5, 1.0, 1.5)
            }
        }
    }

    @EventHandler
    fun itemGiftHandler(event: PlayerDropItemEvent) {
        if (!isActive()) return
        if (isAngered()) return
        val ambassador = Odyssey.instance.worldBoss as AmbassadorBoss
        if (event.player !in ambassador.bossEntity!!.getNearbyEntities(1.5, 1.5, 1.5)) return
        // Sentries Passed
        ambassador.appeasementCheck(event.player, event.itemDrop)
    }

    @EventHandler
    fun playerElytraHandler(event: PlayerElytraBoostEvent) {
        if (!isActive()) return
        if (!isAngered()) return
        val ambassador = Odyssey.instance.worldBoss as AmbassadorBoss
        if (event.player !in ambassador.bossEntity!!.getNearbyEntities(15.0, 15.0, 15.0)) return
        // Pull Back
        ambassador.voidPullBackAttack(event.player)
    }

    @EventHandler
    fun ambassadorDamageHandler(event: EntityDamageByEntityEvent) {
        if (!isActive()) return
        if (!isAngered()) return
        val ambassador = Odyssey.instance.worldBoss as AmbassadorBoss
        if (event.entity.uniqueId != ambassador.bossEntity!!.uniqueId) return
        // Damage
        ambassador.damageHandler(event.damager, event.damage)
    }

    @EventHandler
    fun ambassadorDeathHandler(event: EntityDeathEvent) {
        if (!isActive()) return
        val ambassador = Odyssey.instance.worldBoss as AmbassadorBoss
        if (event.entity.uniqueId != ambassador.bossEntity!!.uniqueId) return
        // Defeat
        ambassador.defeatedBoss(ambassador.bossEntity!!, event.entity.killer)
        Odyssey.instance.also {
            it.isBossActive = false
            it.isAmbassadorDefeated = true
            it.worldBoss = null
            it.bossDespawnTimer = System.currentTimeMillis()
        }
    }


}