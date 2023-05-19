package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object FoodListeners : Listener {

    // Main Function for food related consumption events
    @EventHandler
    fun eatingOdysseyFood(event: PlayerItemConsumeEvent) {
        if (!event.item.hasOdysseyTag()) {
            return
            //println(event.item.itemMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING])
        }
        if (!event.item.itemMeta.hasCustomModelData()) {
            return
        }

        var satisfyingFood = true
        when (event.item.type) {
            Material.COOKIE -> {
                // Cookie match
                when (event.item.itemMeta.customModelData) {
                    ItemModels.BEETROOT_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 0))
                    }
                    ItemModels.PUMPKIN_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 5 * 20, 0))
                    }
                    ItemModels.HONEY_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 4 * 20, 0))
                    }
                    ItemModels.APPLE_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 0))
                    }
                    ItemModels.BERRY_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0))
                    }
                    ItemModels.GLOW_BERRY_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0))
                    }
                    ItemModels.MELON_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 0))
                    }
                    ItemModels.SUGAR_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 0))
                    }
                    ItemModels.GOLDEN_COOKIE -> {
                        event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 15 * 20, 1))
                    }
                    ItemModels.CHORUS_COOKIE -> {
                        event.player.teleport(event.player.location.clone().add(((0..50).random().div(10.0)), 8.0, ((0..50).random().div(10.0))))
                    }
                }
            }
            else -> {
                satisfyingFood = false
            }
        }
        if (satisfyingFood) {
            event.player.world.spawnParticle(Particle.HEART, event.player.location, 10, 0.5, 0.5, 0.5)
            event.player.world.spawnParticle(Particle.COMPOSTER, event.player.location, 15, 0.5, 0.5, 0.5)
        }
    }

}