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


    @EventHandler
    fun superFireworkHandler(event: FireworkExplodeEvent) {
        if (!event.entity.scoreboardTags.contains(EntityTags.SUPER_FIREWORK)) return

        with(event.entity.world) {
            playSound(event.entity.location, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
            playSound(event.entity.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.5F, 0.8F)
            playSound(event.entity.location, Sound.ENTITY_IRON_GOLEM_DEATH, 2.0F, 0.8F)
            //spawnParticle(Particle.FLASH, event.entity.location, 5, 1.0, 1.0, 1.0)
        }

        val center = event.entity.location.clone()
        val radius = 3.0
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


}