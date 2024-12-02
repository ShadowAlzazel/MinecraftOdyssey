package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ItemListeners : Listener, DataTagManager {

    @EventHandler
    fun itemUseOnDropHandler(event: PlayerDropItemEvent) {
        if (!event.itemDrop.itemStack.hasOdysseyItemTag()) return
        // For all Item on Drop Uses
        when (event.itemDrop.itemStack.getItemIdentifier()) {
            "soul_spice" -> {
                soulSpiceItemHandler(event)
            }
            else -> {

            }
        }
    }

    @EventHandler
    fun eatingFood(event: PlayerItemConsumeEvent) {
        // Buffed Golden Apples
        if (event.item.type == Material.ENCHANTED_GOLDEN_APPLE) {
            event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 3))
            event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 4))
        }
    }

    fun deathHandler(event: EntityDeathEvent) {
        if (event.entity.killer != null && event.droppedExp > 0) {
            // SOUL_STEEL TODO
            var expDrop = 0.0
            val killer = event.entity.killer!!
            val hasSoulSteel = true
            if (hasSoulSteel) expDrop += 0.15
            event.droppedExp += (event.droppedExp * (1 + expDrop)).toInt()
            with(killer.world) {
                spawnParticle(Particle.SOUL, killer.location, 8, 0.05, 0.25, 0.05)
                spawnParticle(Particle.SCULK_SOUL, killer.location, 9, 0.15, 0.25, 0.15)
            }
            return
        }
    }


    /*-----------------------------------------------------------------------------------------------*/

    private fun soulSpiceItemHandler(event: PlayerDropItemEvent) {
        val item = event.itemDrop
        if (item.itemStack.amount != 1) return
        val player = event.player
        if (player.hasCooldown(item.itemStack.type)) return
        // Give Nearby entities glowing
        val nearbyEntities = player.location.getNearbyLivingEntities(20.0).filter {
            (it != player) &&  (!it.isDead)
        }
        nearbyEntities.forEach {
            val glowingEffect = PotionEffect(PotionEffectType.GLOWING, (2 * 20) + 10, 0)
            it.addPotionEffect(glowingEffect)
        }
        player.setCooldown(event.itemDrop.itemStack.type, 5 * 20)
        // World Particles
        with(player.location.world) {
            spawnParticle(Particle.SOUL, player.location, 10, 0.05, 0.35, 0.05)
            spawnParticle(Particle.SCULK_SOUL, player.location, 5, 0.25, 0.35, 0.25)
            playSound(player.location, Sound.PARTICLE_SOUL_ESCAPE, 2.5F, 1.4F)
            playSound(player.location, Sound.BLOCK_SAND_BREAK, 1.0F, 1.6F)
        }
        event.itemDrop.remove()
    }

}