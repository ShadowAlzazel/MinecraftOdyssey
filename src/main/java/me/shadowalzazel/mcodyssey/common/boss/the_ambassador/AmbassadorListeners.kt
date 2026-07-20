package me.shadowalzazel.mcodyssey.common.boss.the_ambassador

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.FireworkExplodeEvent
import org.bukkit.event.player.PlayerDropItemEvent
import kotlin.math.pow

object AmbassadorListeners: Listener {

    // Function to check if current boss is ambassador

    private fun isActive(): Boolean {
        /*
        with(Odyssey.instance.bossManager) {
            val hasBoss = (hasBossActive && (currentBoss is TheAmbassador && currentBoss!!.isActive))
            if (hasBoss) ambassador = currentBoss as TheAmbassador
            return hasBoss
        }

         */
        return false
    }

    private fun isAngered(): Boolean {
        /*
        if (!isActive()) return false
        if (ambassador!!.isAngered) return true
        return false

         */
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
        }

        val center = event.entity.location.clone()
        val radius = 2.5
        val entities = event.entity.getNearbyEntities(radius, radius, radius).filterIsInstance<LivingEntity>()
        for (e in entities) {
            // indirect distance square
            val distance = e.location.distance(center)
            val power = (maxOf(radius - distance, 0.0)).pow(2.0) + (maxOf(radius - distance, 0.0)).times(1) + (radius * 0.5)
            val damageSource = DamageSource.builder(DamageType.EXPLOSION).build()
            e.damage(power + 8.0, damageSource) // Create Damage Source
            e.world.spawnParticle(Particle.ENCHANTED_HIT, e.location, 95, 1.5, 1.0, 1.5)
        }
    }


    fun itemGiftHandler(event: PlayerDropItemEvent) {
        if (!isActive()) return
        if (isAngered()) return
        //if (event.player !in ambassador!!.illusioner.getNearbyEntities(1.5, 1.5, 1.5)) return
        // Sentries Passed
       // ambassador!!.appeasementCheck(event.player, event.itemDrop)
    }


    fun playerElytraHandler(event: PlayerElytraBoostEvent) {
        if (!isActive()) return
        if (!isAngered()) return
       // if (event.player !in ambassador!!.illusioner.getNearbyEntities(15.0, 15.0, 15.0)) return
        // Pull Back
       // ambassador!!.voidPullBackAttack(event.player)
    }


    fun ambassadorDamageHandler(event: EntityDamageByEntityEvent) {
        if (!isActive()) return
        //println(event.damager.name)
       // if (event.entity.uniqueId != ambassador!!.illusioner.uniqueId) return
        // Damage
       // ambassador!!.takeDamageHandler(event.damager, event.damage)
    }

    fun ambassadorDeathHandler(event: EntityDeathEvent) {
        if (!isActive()) return
      //  if (event.entity.uniqueId != ambassador!!.illusioner.uniqueId) return
        // Defeat
       // ambassador!!.defeatedBoss(ambassador!!.illusioner, event.entity.killer)
        /*
        Odyssey.instance.bossManager.also {
            it.hasBossActive = false
            it.isAmbassadorDefeated = true
            it.currentBoss = null
            it.timeUntilBossDespawn = System.currentTimeMillis()
        }

         */
    }


}