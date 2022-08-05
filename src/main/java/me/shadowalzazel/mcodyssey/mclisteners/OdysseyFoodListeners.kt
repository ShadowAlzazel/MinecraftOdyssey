package me.shadowalzazel.mcodyssey.mclisteners

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyFoodListeners : Listener {

    @EventHandler
    fun eatingOdysseyFood(event: PlayerItemConsumeEvent) {
        if (event.item.hasItemMeta()) {
            if (event.item.itemMeta.hasLore()) {
                val somePlayer = event.player
                val someFood = event.item
                val foodLore = someFood.itemMeta.lore
                var satisfyingFood = true

                when (event.item.type) {

                    Material.COOKIE -> {
                        if ("§7A beetroot cookie!" in foodLore!!) {
                            val beetrootCookieEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 0)
                            somePlayer.addPotionEffect(beetrootCookieEffect)
                        }
                        else if ("§7A pumpkin cookie!" in foodLore) {
                            val beetrootCookieEffect = PotionEffect(PotionEffectType.FAST_DIGGING, 4 * 20, 0)
                            somePlayer.addPotionEffect(beetrootCookieEffect)
                        }
                        else if ("§7A honey cookie!" in foodLore) {
                            val beetrootCookieEffect = PotionEffect(PotionEffectType.ABSORPTION, 4 * 20, 0)
                            somePlayer.addPotionEffect(beetrootCookieEffect)
                        }
                        else if ("§7An apple cookie!" in foodLore) {
                            val beetrootCookieEffect = PotionEffect(PotionEffectType.REGENERATION, 4 * 20, 0)
                            somePlayer.addPotionEffect(beetrootCookieEffect)
                        }

                    }
                    else -> {
                        satisfyingFood = false
                    }
                }

                if (satisfyingFood) {
                    somePlayer.world.spawnParticle(Particle.HEART, somePlayer.location, 10, 0.5, 0.5, 0.5)
                    somePlayer.world.spawnParticle(Particle.COMPOSTER, somePlayer.location, 15, 0.5, 0.5, 0.5)
                }

            }
        }
    }



}