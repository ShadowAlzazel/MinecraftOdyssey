package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemModels
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object OdysseyFoodListeners : Listener {

    // Main Function for food related consumption events
    @EventHandler
    fun eatingOdysseyFood(event: PlayerItemConsumeEvent) {
        if (event.item.itemMeta.hasLore()) {
            if (event.item.itemMeta.hasCustomModelData()) {
                val somePlayer = event.player
                val someFood = event.item
                val foodLore = (someFood.lore()!!.first() as TextComponent).content()
                var satisfyingFood = true
                when (someFood.type) {
                    Material.COOKIE -> {
                        // Cookie match
                        when (someFood.itemMeta.customModelData) {
                            ItemModels.BEETROOT_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 0))
                            }
                            ItemModels.PUMPKIN_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 5 * 20, 0))
                            }
                            ItemModels.HONEY_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 4 * 20, 0))
                            }
                            ItemModels.APPLE_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 0))
                            }
                            ItemModels.BERRY_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0))
                            }
                            ItemModels.GLOWBERRY_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0))
                            }
                            ItemModels.MELON_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 0))
                            }
                            ItemModels.SUGAR_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 0))
                            }
                            ItemModels.GOLDEN_COOKIE -> {
                                somePlayer.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 15 * 20, 1))
                            }
                            ItemModels.CHORUS_COOKIE -> {
                                somePlayer.teleport(somePlayer.location.clone().add(((0..50).random().div(10.0)), 8.0, ((0..50).random().div(10.0))))
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