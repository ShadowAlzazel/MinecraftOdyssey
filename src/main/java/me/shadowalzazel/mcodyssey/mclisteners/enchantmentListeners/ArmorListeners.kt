package me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.effects.SpeedySpursTask
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object ArmorListeners : Listener {

    private var playersFruitfulFareCooldown = mutableMapOf<UUID, Long>()
    private var playersPotionBarrierCooldown = mutableMapOf<UUID, Long>()


    // COWARDICE enchantment effects
    @EventHandler
    fun cowardiceEnchantment(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val cowardiceEntity = event.entity as LivingEntity
            if (cowardiceEntity.equipment != null) {
                if (cowardiceEntity.equipment!!.leggings != null) {
                    val someArmor = cowardiceEntity.equipment!!.leggings!!
                    if (someArmor.hasItemMeta()) {
                        if (someArmor.itemMeta.hasEnchant(OdysseyEnchantments.COWARDICE)) {
                            val cowardiceFactor = someArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.COWARDICE)
                            val cowardiceEffect = PotionEffect(PotionEffectType.SPEED, 6 * 20 , cowardiceFactor)
                            val nearbyEnemies = cowardiceEntity.world.getNearbyLivingEntities(cowardiceEntity.location, 2.5)
                            if (event.damager in nearbyEnemies) {
                                cowardiceEntity.velocity = cowardiceEntity.location.add(0.0, 0.50, 0.0).subtract(event.damager.location).toVector().multiply(0.75)
                            }
                            cowardiceEntity.addPotionEffect(cowardiceEffect)
                        }
                    }
                }
            }
        }
    }


    // FRUITFUL_FARE enchantment effects
    @EventHandler
    fun fruitfulFareEnchantment(event: PlayerItemConsumeEvent) {
        val somePlayer: Player = event.player
        if (somePlayer.inventory.chestplate != null) {
            if (somePlayer.inventory.chestplate!!.hasItemMeta()) {
                val someArmor = somePlayer.inventory.chestplate
                if (someArmor!!.itemMeta.hasEnchant(OdysseyEnchantments.FRUITFUL_FARE)) {
                    val fruitfulFareFactor = someArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.FRUITFUL_FARE)
                    if (somePlayer.gameMode != GameMode.SPECTATOR) {
                        // ADD later all with keys for more heal
                        val someFoods = listOf(Material.APPLE, Material.PUMPKIN_PIE, Material.HONEY_BOTTLE, Material.DRIED_KELP, Material.GOLDEN_CARROT)

                        if (event.item.type in someFoods) {
                            if (!playersFruitfulFareCooldown.containsKey(somePlayer.uniqueId)) {
                                playersFruitfulFareCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - playersFruitfulFareCooldown[somePlayer.uniqueId]!!
                            if (timeElapsed > (10 - fruitfulFareFactor) * 1000) {
                                playersFruitfulFareCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                // Effects
                                val currentHealth = somePlayer.health
                                println(somePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
                                println(currentHealth)
                                if (somePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value < currentHealth + (1 + fruitfulFareFactor)) {
                                    somePlayer.health = somePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                                }
                                else {
                                    somePlayer.health += (1 + fruitfulFareFactor)
                                }
                                // Particles
                                somePlayer.world.spawnParticle(Particle.HEART, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.world.spawnParticle(Particle.VILLAGER_HAPPY, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.world.spawnParticle(Particle.COMPOSTER, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.playSound(somePlayer.location, Sound.ENTITY_STRIDER_HAPPY, 1.5F, 0.5F)
                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.5F, 0.5F)
                                somePlayer.playSound(somePlayer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)

                            }

                        }
                    }
                }
            }
        }
    }

    // POTION_BARRIER enchantment effects
    @EventHandler
    fun potionBarrierEnchantment(event: PlayerItemConsumeEvent) {
        val somePlayer: Player = event.player
        if (somePlayer.inventory.chestplate != null) {
            if (somePlayer.inventory.chestplate!!.hasItemMeta()) {
                val someArmor = somePlayer.inventory.chestplate
                if (someArmor!!.itemMeta.hasEnchant(OdysseyEnchantments.POTION_BARRIER)) {
                    val potionBarrierFactor = someArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.POTION_BARRIER)
                    if (somePlayer.gameMode != GameMode.SPECTATOR) {
                        // Fix for water bottle
                        if (event.item.type == Material.POTION) {
                            if (!ArmorListeners.playersPotionBarrierCooldown.containsKey(somePlayer.uniqueId)) {
                                ArmorListeners.playersPotionBarrierCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - ArmorListeners.playersPotionBarrierCooldown[somePlayer.uniqueId]!!
                            if (timeElapsed > (15 - potionBarrierFactor) * 1000) {
                                ArmorListeners.playersPotionBarrierCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                // Effects
                                val potionBarrierEffect = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ((potionBarrierFactor * 2) + 3) * 20, 1)
                                somePlayer.addPotionEffect(potionBarrierEffect)
                                // Particles
                                somePlayer.world.spawnParticle(Particle.SCRAPE, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.world.spawnParticle(Particle.ELECTRIC_SPARK, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.world.spawnParticle(Particle.COMPOSTER, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.playSound(somePlayer.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
                                somePlayer.playSound(somePlayer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)

                            }

                        }
                    }
                }
            }
        }
    }


    // SPEEDY_SPURS enchantment effects
    @EventHandler
    fun speedySpursEnchantment(event: VehicleEnterEvent) {
        if (event.entered is Player) {
            if (event.vehicle is LivingEntity) {
                val somePlayer = event.entered as Player
                val someMount = event.vehicle as LivingEntity
                if (somePlayer.gameMode != GameMode.SPECTATOR) {
                    if (somePlayer.inventory.boots != null) {
                        if (somePlayer.inventory.boots!!.hasItemMeta()) {
                            if (somePlayer.inventory.boots!!.itemMeta!!.hasEnchant(OdysseyEnchantments.SPEEDY_SPURS)) {
                                val speedySpursFactor = somePlayer.inventory.boots!!.getEnchantmentLevel(OdysseyEnchantments.SPEEDY_SPURS)
                                val someSpeedySpursTask = SpeedySpursTask(somePlayer, someMount, speedySpursFactor)
                                someSpeedySpursTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10 * 20)
                            }
                        }
                    }
                }
            }
        }
    }


    // SPOREFUL enchantment effects
    @EventHandler
    fun sporefulEnchantment(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val sporingEntity = event.entity as LivingEntity
            if (sporingEntity.equipment != null) {
                if (sporingEntity.equipment!!.leggings != null) {
                    val someArmor = sporingEntity.equipment!!.leggings!!
                    if (someArmor.hasItemMeta()) {
                        if (someArmor.itemMeta.hasEnchant(OdysseyEnchantments.SPOREFUL)) {
                            val sporingFactor = someArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.SPOREFUL)
                            val nearbyEnemies = sporingEntity.world.getNearbyLivingEntities(sporingEntity.location, sporingFactor.toDouble() * 0.75)
                            nearbyEnemies.remove(sporingEntity)
                            // Effects
                            val sporegenesisEffect = PotionEffect(PotionEffectType.POISON, ((sporingFactor * 2) + 2) * 20, 0)
                            val sporefusionEffect = PotionEffect(PotionEffectType.CONFUSION, ((sporingFactor * 2) + 2) * 20, 1)
                            val sporeSlowEffect = PotionEffect(PotionEffectType.SLOW, 20, 0)
                            // Particles
                            sporingEntity.world.spawnParticle(Particle.GLOW_SQUID_INK, sporingEntity.location, 65, 0.5, 0.5, 0.5)
                            sporingEntity.world.spawnParticle(Particle.GLOW, sporingEntity.location, 45, 0.5, 0.5, 0.5)
                            sporingEntity.world.spawnParticle(Particle.WARPED_SPORE, sporingEntity.location, 95, 0.75, 0.5, 0.75)
                            sporingEntity.world.spawnParticle(Particle.SNEEZE, sporingEntity.location, 65, 0.15, 0.15, 0.15)
                            sporingEntity.world.spawnParticle(Particle.FALLING_SPORE_BLOSSOM, sporingEntity.location, 85, 1.0, 0.5, 1.0)

                            for (threat in nearbyEnemies) {
                                threat.addPotionEffect(sporegenesisEffect)
                                threat.addPotionEffect(sporefusionEffect)
                                threat.addPotionEffect(sporeSlowEffect)
                            }

                        }
                    }
                }
            }
        }
    }


    // SQUIDIFY enchantment effects
    @EventHandler
    fun squidifyEnchantment(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val squidingEntity = event.entity as LivingEntity
            if (squidingEntity.equipment != null) {
                if (squidingEntity.equipment!!.leggings != null) {
                    val someArmor = squidingEntity.equipment!!.leggings!!
                    if (someArmor.hasItemMeta()) {
                        if (someArmor.itemMeta.hasEnchant(OdysseyEnchantments.SQUIDIFY)) {
                            val squidingFactor = someArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.SQUIDIFY)
                            val nearbyEnemies = squidingEntity.world.getNearbyLivingEntities(squidingEntity.location, squidingFactor.toDouble() * 0.75)
                            nearbyEnemies.remove(squidingEntity)
                            // Effects
                            val squidInkEffect = PotionEffect(PotionEffectType.BLINDNESS, (squidingFactor * 2) + 2, 1)
                            val squidSlowEffect = PotionEffect(PotionEffectType.SLOW, squidingFactor * 20, 2)
                            // Particles
                            squidingEntity.world.spawnParticle(Particle.ASH, squidingEntity.location, 95, 1.5, 0.5, 1.5)
                            squidingEntity.world.spawnParticle(Particle.SPELL_MOB_AMBIENT, squidingEntity.location, 55, 0.75, 0.5, 0.75)
                            squidingEntity.world.spawnParticle(Particle.SQUID_INK, squidingEntity.location, 85, 0.75, 0.5, 0.75)
                            squidingEntity.world.spawnParticle(Particle.SMOKE_LARGE, squidingEntity.location, 85, 1.0, 0.5, 1.0)

                            for (threat in nearbyEnemies) {
                                threat.addPotionEffect(squidInkEffect)
                                threat.addPotionEffect(squidSlowEffect)
                            }

                        }
                    }
                }
            }
        }
    }


    // VENGEFUL enchantment effects
    @EventHandler
    fun vengefulEnchantment(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val vengefulEntity = event.entity as LivingEntity
            if (vengefulEntity.equipment != null) {
                if (vengefulEntity.equipment!!.chestplate != null) {
                    val someChestplate = vengefulEntity.equipment!!.chestplate!!
                    if (someChestplate.hasItemMeta()) {
                        if (someChestplate.itemMeta.hasEnchant(OdysseyEnchantments.VENGEFUL)) {
                            val vengefulFactor = someChestplate.itemMeta.getEnchantLevel(OdysseyEnchantments.VENGEFUL)
                            val nearbyEnemies = vengefulEntity.world.getNearbyLivingEntities(vengefulEntity.location, vengefulFactor.toDouble() + 0.5)

                            if (event.damager in nearbyEnemies) {
                                vengefulEntity.attack(event.damager)
                            }
                        }
                    }
                }
            }
        }
    }

}