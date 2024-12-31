package me.shadowalzazel.mcodyssey.unused.bosses.the_ambassador

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
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
    private var ambassador: TheAmbassador? = null

    private fun isActive(): Boolean {
        with(Odyssey.instance.bossManager) {
            val hasBoss = (hasBossActive && (currentBoss is TheAmbassador && currentBoss!!.isActive))
            if (hasBoss) ambassador = currentBoss as TheAmbassador
            return hasBoss
        }
    }

    private fun isAngered(): Boolean {
        if (!isActive()) return false
        if (ambassador!!.isAngered) return true
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
                it.damage(25.0)
                it.world.spawnParticle(Particle.CRIT, it.location, 95, 1.5, 1.0, 1.5)
            }
        }
    }

    @EventHandler
    fun itemGiftHandler(event: PlayerDropItemEvent) {
        if (!isActive()) return
        if (isAngered()) return
        if (event.player !in ambassador!!.illusioner.getNearbyEntities(1.5, 1.5, 1.5)) return
        // Sentries Passed
        ambassador!!.appeasementCheck(event.player, event.itemDrop)
    }

    @EventHandler
    fun playerElytraHandler(event: PlayerElytraBoostEvent) {
        if (!isActive()) return
        if (!isAngered()) return
        if (event.player !in ambassador!!.illusioner.getNearbyEntities(15.0, 15.0, 15.0)) return
        // Pull Back
        ambassador!!.voidPullBackAttack(event.player)
    }

    @EventHandler
    fun ambassadorDamageHandler(event: EntityDamageByEntityEvent) {
        if (!isActive()) return
        //println(event.damager.name)
        if (event.entity.uniqueId != ambassador!!.illusioner.uniqueId) return
        // Damage
        ambassador!!.takeDamageHandler(event.damager, event.damage)
    }

    @EventHandler
    fun ambassadorDeathHandler(event: EntityDeathEvent) {
        if (!isActive()) return
        if (event.entity.uniqueId != ambassador!!.illusioner.uniqueId) return
        // Defeat
        ambassador!!.defeatedBoss(ambassador!!.illusioner, event.entity.killer)
        Odyssey.instance.bossManager.also {
            it.hasBossActive = false
            it.isAmbassadorDefeated = true
            it.currentBoss = null
            it.timeUntilBossDespawn = System.currentTimeMillis()
        }
    }


}