@file:OptIn(DelicateCoroutinesApi::class)

package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mclisteners.utility.FreezingTask
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object EnchantmentListeners : Listener {

    // EXPLODING enchantment effects
    @EventHandler
    fun explodingEnchant(event: EntityDeathEvent) {
        if (event.entity.killer is Player) {
            if (event.entity !is Player) {
                val somePlayer = event.entity.killer!!
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.EXPLODING)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {
                            // Test message
                            somePlayer.sendMessage("BOOM!")
                            // Boom variables
                            val boomLocation = event.entity.location
                            val boomMagnitude =  someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.EXPLODING)

                            // Fireball
                            val boomExplosion: Fireball = somePlayer.world.spawnEntity(boomLocation, EntityType.FIREBALL) as Fireball
                            boomExplosion.setIsIncendiary(false)
                            boomExplosion.yield = 0.0F
                            boomExplosion.direction = Vector(0.0, -3.0, 0.0)

                            // Firework effect and color
                            val boomFirework: Firework = somePlayer.world.spawnEntity(boomLocation, EntityType.FIREWORK) as Firework
                            val boomFireworkMeta = boomFirework.fireworkMeta
                            boomFireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).withFade(Color.FUCHSIA).trail(true).flicker(true).build())
                            boomFireworkMeta.power = boomMagnitude * 20
                            boomFirework.fireworkMeta = boomFireworkMeta
                            boomFirework.velocity = Vector(0.0, -3.0, 0.0)

                            // Particles
                            somePlayer.world.spawnParticle(Particle.FLASH, boomLocation, 5, 1.0, 1.0, 1.0)
                            somePlayer.world.spawnParticle(Particle.LAVA, boomLocation, 35, 1.5, 1.0, 1.5)

                            /*
                            GlobalScope.launch {
                                println("Some New Task")
                                delay(2000)
                                println("YAY NO STOPS!")
                                somePlayer.sendMessage("FINALLY!!!!")


                            }
                            */
                        }
                    }
                }
            }
        }
    }


    // FREEZING enchantment effects
    @EventHandler
    fun applyFreezing(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            if (event.entity is LivingEntity) {
                val somePlayer: Player = event.damager as Player
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.FREEZING_ASPECT)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {
                            val freezeFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.FREEZING_ASPECT)
                            val freezingEntity: LivingEntity = event.entity as LivingEntity
                            val freezingSlow = PotionEffect(PotionEffectType.SLOW, freezeFactor * 20 * 3, freezeFactor)
                            freezingEntity.addPotionEffect(freezingSlow)

                            val freezingTask = FreezingTask(freezingEntity, freezeFactor)
                            freezingTask.runTaskTimerAsynchronously(MinecraftOdyssey.instance, 0, 20)


                            GlobalScope.launch {
                                var freezeCooldown = System.currentTimeMillis()
                                var counter = 0
                                // TEST
                                freezingEntity.freezeTicks = 100
                                while(freezeFactor * 3 > counter) {
                                    val timeElapsed = System.currentTimeMillis() - freezeCooldown
                                    if (timeElapsed >= 1000) {
                                        freezeCooldown = System.currentTimeMillis()
                                        freezingEntity.damage(1.0)
                                        counter += 1

                                    }

                                }
                                freezingEntity.freezeTicks = 0
                            }
                            //END COROUTINE
                        }
                    }
                }
            }
        }
    }



}