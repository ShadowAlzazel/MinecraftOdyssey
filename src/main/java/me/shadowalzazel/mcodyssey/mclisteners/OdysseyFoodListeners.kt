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
                val foodLore = someFood.itemMeta.lore!!.first()
                var satisfyingFood = true

                when (event.item.type) {
                    Material.COOKIE -> {
                        when (foodLore) {
                            "§7A beetroot cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A pumpkin cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.FAST_DIGGING, 5 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A honey cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.ABSORPTION, 4 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7An apple cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A berry cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A glow-berry cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A melon cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A sugar cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.SPEED, 5 * 20, 0)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
                            "§7A golden cookie!" -> {
                                val someCookieEffect = PotionEffect(PotionEffectType.ABSORPTION, 10 * 20, 2)
                                somePlayer.addPotionEffect(someCookieEffect)
                            }
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