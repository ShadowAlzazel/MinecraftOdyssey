package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.odysseyUtility.OdysseyItems
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffectType

object OdysseyDropsListeners : Listener {

    private fun droppedItemSound(somePlayer: Player) {
        somePlayer.playSound(somePlayer.location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 0.9F)
        somePlayer.playSound(somePlayer.location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1.5F, 0.9F)
        somePlayer.playSound(somePlayer.location, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1.5F, 0.9F)
        somePlayer.playSound(somePlayer.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5F, 0.9F)
        somePlayer.world.spawnParticle(Particle.TOTEM, somePlayer.location, 25, 2.0, 1.0, 2.0)
        somePlayer.world.spawnParticle(Particle.END_ROD, somePlayer.location, 25, 2.0, 1.0, 2.0)
        somePlayer.world.spawnParticle(Particle.GLOW, somePlayer.location, 35, 2.0, 1.0, 2.0)
        println("Dropped Item Gilded ${somePlayer.name}")
    }





    @EventHandler
    fun mobDrops(event: EntityDeathEvent) {
        if (MinecraftOdyssey.instance.ambassadorDefeated) {
            if (event.entity.killer is Player) {
                val somePlayer: Player = event.entity.killer as Player
                if (event.entity.hasLineOfSight(somePlayer)) {

                    //Looting and luck
                    var looting = 0.0
                    if (somePlayer.equipment.itemInMainHand.itemMeta != null) {
                        if (somePlayer.equipment.itemInMainHand.itemMeta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                            looting += (somePlayer.activeItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) * 1.0)
                        }
                    }
                    var luck = 0.0
                    if (somePlayer.hasPotionEffect(PotionEffectType.LUCK)) {
                        // no detect for level?
                        luck += 1.25
                    }
                    else if (somePlayer.hasPotionEffect(PotionEffectType.UNLUCK)) {
                        luck -= 0.75
                    }

                    // Loot check and entity check
                    when (event.entity) {
                        is Stray -> {
                            if ((1.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.FREEZING_ASPECT, 1))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Creeper -> {
                            if ((1.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.EXPLODING, 1))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Hoglin -> {
                            if ((1.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.HEMORRHAGE, 1))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is PigZombie -> {
                            if ((1.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(
                                        OdysseyEnchantments.BANE_OF_THE_SWINE,
                                        1
                                    ))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Vindicator -> {
                            if ((3.25 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(
                                        OdysseyEnchantments.BANE_OF_THE_ILLAGER,
                                        1
                                    ))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Squid -> {
                            if ((5.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(
                                        OdysseyEnchantments.SQUIDIFY,
                                        1
                                    ))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Husk -> {
                            if ((1.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(
                                        OdysseyEnchantments.DECAYING_TOUCH,
                                        1
                                    ))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Drowned -> {
                            if ((1.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(
                                        OdysseyEnchantments.BANE_OF_THE_SEA,
                                        1
                                    ))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        is Shulker -> {
                            if ((2.5 + luck + looting) * 10 > (0..1000).random()) {
                                somePlayer.world.dropItem(
                                    event.entity.location,
                                    (OdysseyItems.GILDED_BOOK.createGildedBook(
                                        OdysseyEnchantments.VOID_STRIKE,
                                        1
                                    ))
                                )
                                droppedItemSound(somePlayer)
                            }
                        }
                        else -> {
                        }
                    }
                }


                /*
                if (event.entity.getTargetEntity(25) == somePlayer) {
                    if (event.entity.hasLineOfSight(somePlayer)) {
                        if (event.entity.ticksLived > 20 * 5) {


                            }
                        }
                    }
                }

                 */
            }
        }
    }




}