package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Foods
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import me.shadowalzazel.mcodyssey.listeners.utility.LootLogic
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.entity.EntityDeathEvent

object LootListeners : Listener, ItemCreator {

    @EventHandler
    fun mobDeathDropHandler(event: EntityDeathEvent) {
        if (event.entity.killer !is Player) {
            return
        }
        // Mineshaft
        if (event.entity.scoreboardTags.contains(EntityTags.IN_MINESHAFT)) {
            println("ROLLED")
            val mobLootLogic = LootLogic(1.0, event.entity, event.entity.killer!!)
            if (mobLootLogic.roll(33.0)) {
                event.entity.world.dropItem(event.entity.location, Ingredients.SILVER_NUGGET.createNewStack(1))
            }
        }
        if (!event.entity.hasLineOfSight(event.entity.killer!!)) {
            return
        }
        mobLootHandler(event.entity, event.entity.killer!!)
    }


    private fun mobLootHandler(mob: LivingEntity, player: Player) {
        // Create a calculation class that handles all the logic
        val mobLootLogic = LootLogic(1.0, mob, player)
        // Loot check and entity check
        when (mob) {
            is Skeleton -> {
                if (mobLootLogic.roll(2.5)) {
                    val rangedSet = OdysseyEnchantments.RANGED_SET.filter { enchant -> enchant !in OdysseyEnchantments.EXOTIC_LIST }
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(rangedSet.random(), 1)))
                    droppedItemSound(player)
                }
            }
            is Husk -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.DECAYING_TOUCH, 1)))
                    droppedItemSound(player)
                }
            }
            is Drowned -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.BANE_OF_THE_SEA, 1)))
                    droppedItemSound(player)
                }
            }
            is PigZombie -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.BANE_OF_THE_SWINE, 1)))
                    droppedItemSound(player)
                }
            }
            is Zombie -> {
                if (mobLootLogic.roll(2.5)) {
                    val meleeSet = OdysseyEnchantments.MELEE_SET.filter { enchant -> enchant !in OdysseyEnchantments.EXOTIC_LIST }
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(meleeSet.random(), 1)))
                    droppedItemSound(player)
                }
                // TODO: Is Blood Moon
                if (mobLootLogic.roll(4.0)) {
                    mob.world.dropItem(mob.location, (Ingredients.COAGULATED_BLOOD.createItemStack((1..3).random())))
                }
            }
            is Witch -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.ALCHEMY_ARTILLERY, 1)))
                    droppedItemSound(player)
                }
            }
            is Stray -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.FREEZING_ASPECT, 1)))
                    droppedItemSound(player)
                }
            }
            is Creeper -> {
                // If charged creeper kills catalyst, drop echo shard
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.EXPLODING, 1)))
                    droppedItemSound(player)
                }
            }
            is Hoglin -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.HEMORRHAGE, 1)))
                    droppedItemSound(player)
                }
            }
            is Vindicator -> {
                if (mobLootLogic.roll(3.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.BANE_OF_THE_ILLAGER, 1)))
                    droppedItemSound(player)
                }
            }
            is Pillager -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.BURST_BARRAGE, 1)))
                    droppedItemSound(player)
                }
            }
            is Ravager -> {
                if (mobLootLogic.roll(5.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.WHIRLWIND, 1)))
                    droppedItemSound(player)
                }
            }
            is Squid -> {
                if (mobLootLogic.roll(3.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.SQUIDIFY, 1)))
                    droppedItemSound(player)
                }
            }
            is Shulker -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.VOID_STRIKE, 1))) // change
                    droppedItemSound(player)
                }
            }
            is Endermite -> {
                if (mobLootLogic.roll(3.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.VOID_STRIKE, 1)))
                    droppedItemSound(player)
                }
            }
            is Enderman -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.VOID_JUMP, 1)))
                    droppedItemSound(player)
                }
            }
            is Vex -> {
                if (mobLootLogic.roll(4.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.TOTEM_OF_VEXING.createItemStack(1)))
                }
            }
            is ElderGuardian -> {
                mob.world.dropItem(mob.location, (Ingredients.IRRADIATED_ROD.createItemStack((1..3).random())))
                mob.world.dropItem(mob.location, (Ingredients.IRRADIATED_SHARD.createItemStack((2..5).random())))
            }
            is Warden -> {
                mob.world.dropItem(mob.location, (Ingredients.WARDEN_ENTRAILS.createItemStack(1)))
                mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.SCULK_SENSITIVE, 1)))
                droppedItemSound(player)
            }
            is Wither -> {
                mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBook(OdysseyEnchantments.BLACK_ROSE, 1)))
                droppedItemSound(player)
            }
            is EnderDragon -> {
            }
            else -> {
            }
        }
        // Check if gilded
        if (mob.scoreboardTags.contains(EntityTags.GILDED_MOB)) {
            droppedItemSound(player)
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

    private fun droppedItemSound(player: Player) {
        with(player) {
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5F, 0.9F)
            world.spawnParticle(Particle.TOTEM, location, 25, 2.0, 1.0, 2.0)
            world.spawnParticle(Particle.END_ROD, location, 25, 2.0, 1.0, 2.0)
            world.spawnParticle(Particle.GLOW, location, 35, 2.0, 1.0, 2.0)
        }
    }

}