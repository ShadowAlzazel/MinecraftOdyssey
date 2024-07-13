package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.rune_writing.SpaceRuneManager
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ItemListeners : Listener, SpaceRuneManager {

    @EventHandler
    fun itemUseOnDropHandler(event: PlayerDropItemEvent) {
        if (!event.itemDrop.itemStack.hasOdysseyItemTag()) return
        // For all Item on Drop Uses
        when (event.itemDrop.itemStack.getItemIdentifier()) {
            "soul_spice" -> {
                soulSpiceItemHandler(event)
            }
            "spacerune_tablet" -> {
                spaceRuneItemHandler(event)
            }
            else -> {

            }
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

    private fun spaceRuneItemHandler(event: PlayerDropItemEvent) {
        val entityItem = event.itemDrop
        if (entityItem.itemStack.amount != 1) return
        val item = entityItem.itemStack
        val player = event.player
        if (player.hasCooldown(item.type)) return
        if (!item.hasTag(ItemDataTags.IS_SPACERUNE)) return
        // Run TP
        spaceRuneTabletTeleport(player, item)
        event.isCancelled = true
    }

}