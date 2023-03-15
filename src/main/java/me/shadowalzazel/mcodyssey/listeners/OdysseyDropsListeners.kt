package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Runic
import me.shadowalzazel.mcodyssey.items.Runic.createEnchantedBook
import me.shadowalzazel.mcodyssey.items.Foods
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffectType

object OdysseyDropsListeners : Listener {

    private fun droppedItemSound(somePlayer: Player) {
        with(somePlayer) {
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5F, 0.9F)
            world.spawnParticle(Particle.TOTEM, location, 25, 2.0, 1.0, 2.0)
            world.spawnParticle(Particle.END_ROD, location, 25, 2.0, 1.0, 2.0)
            world.spawnParticle(Particle.GLOW, location, 35, 2.0, 1.0, 2.0)
        }
    }

    @EventHandler
    fun mobDropsHandler(event: EntityDeathEvent) {
        if (Odyssey.instance.isAmbassadorDefeated) {
            if (event.entity.killer is Player) {
                val somePlayer: Player = event.entity.killer as Player
                if (event.entity.hasLineOfSight(somePlayer)) {
                    val enchantmentSet = OdysseyEnchantments.MELEE_SET + OdysseyEnchantments.RANGED_SET

                    event.entity.also {
                        // Looting and luck
                        var looting = 0.0
                        if (somePlayer.equipment.itemInMainHand.itemMeta != null) {
                            if (somePlayer.equipment.itemInMainHand.itemMeta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                                looting += (somePlayer.activeItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) * 1.0)
                            }
                        }
                        var luck = 0.0
                        if (somePlayer.hasPotionEffect(PotionEffectType.LUCK)) {
                            // somePlayer.activePotionEffects
                            // no detect for level?
                            luck += 1.25
                        }
                        else if (somePlayer.hasPotionEffect(PotionEffectType.UNLUCK)) {
                            luck -= 0.75
                        }
                        // Tags
                        var misc = 0.0
                        var hasBloodMoon = false
                        if ("Blood_Moon_Mob" in event.entity.scoreboardTags) {
                            hasBloodMoon = true
                            misc += 8.84
                        }

                        // Loot check and entity check
                        when (it) {
                            is Skeleton -> {
                                if (hasBloodMoon) {
                                    if ((2.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                        it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(enchantmentSet.random(), 1)))
                                        droppedItemSound(somePlayer)
                                    }
                                }
                            }
                            is Husk -> {
                                if ((1.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.DECAYING_TOUCH, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Drowned -> {
                                if ((1.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.BANE_OF_THE_SEA, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is PigZombie -> {
                                if ((1.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.BANE_OF_THE_SWINE, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Zombie -> {
                                if (hasBloodMoon) {
                                    if ((2.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                        it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(enchantmentSet.random(), 1)))
                                        droppedItemSound(somePlayer)
                                    }
                                    it.world.dropItem(it.location, (Ingredients.COAGULATED_BLOOD.createItemStack((1..3).random())))
                                }
                            }
                            is Witch -> {
                                if ((2.25 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.ALCHEMY_ARTILLERY, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Stray -> {
                                if ((1.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.FREEZING_ASPECT, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Creeper -> {
                                // If charged creeper kills catalyst, drop echo shard
                                if ((1.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.EXPLODING, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Hoglin -> {
                                if ((1.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.HEMORRHAGE, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Vindicator -> {
                                if ((3.25 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.BANE_OF_THE_ILLAGER, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Pillager -> {
                                if ((2.25 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.BURST_BARRAGE, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Ravager -> {
                                if ((4.25 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.WHIRLWIND, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Squid -> {
                                if ((3.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.SQUIDIFY, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Shulker -> {
                                if ((2.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.VOID_STRIKE, 1))) // change
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Endermite -> {
                                if ((3.0 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.VOID_STRIKE, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Enderman -> {
                                if ((2.0 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Runic.GILDED_BOOK.createEnchantedBook(OdysseyEnchantments.VOID_JUMP, 1)))
                                    droppedItemSound(somePlayer)
                                }
                            }
                            is Vex -> {
                                if ((4.5 + luck + looting + misc) * 10 > (0..1000).random()) {
                                    it.world.dropItem(it.location, (Miscellaneous.TOTEM_OF_VEXING.createItemStack(1)))
                                }
                            }
                            is ElderGuardian -> {
                                it.world.dropItem(it.location, (Ingredients.IRRADIATED_ROD.createItemStack((1..3).random())))
                                it.world.dropItem(it.location, (Ingredients.IRRADIATED_SHARD.createItemStack((2..5).random())))
                            }
                            is Warden -> {
                                // If has echo? drop echo shards?
                                it.world.dropItem(it.location, (Ingredients.WARDEN_ENTRAILS.createItemStack(1)))
                                it.world.dropItem(it.location, (Miscellaneous.PRIMO_GEM.createItemStack((10..15).random())))
                            }
                            is Wither -> {
                                it.world.dropItem(it.location, (Miscellaneous.PRIMO_GEM.createItemStack((8..12).random())))
                            }
                            is EnderDragon -> {
                                it.world.dropItem(it.location, (Miscellaneous.PRIMO_GEM.createItemStack((10..15).random())))
                            }
                            else -> {
                            }
                        }
                    }

                }
            }
        }
    }


    fun blockDrops(event: BlockDropItemEvent) {
        when(event.block.type) {
            Material.AMETHYST_CLUSTER, Material.LARGE_AMETHYST_BUD -> {
                event.items.forEach {
                    if (it.itemStack.type == Material.AMETHYST_SHARD) {
                        it.world.dropItem(it.location, (Foods.CRYSTAL_CANDY.createItemStack((0..2).random())))
                    }
                }
            }
            else -> {

            }
        }
    }


}