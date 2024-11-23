package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.custom.Ingredients
import me.shadowalzazel.mcodyssey.common.items.custom.Miscellaneous
import me.shadowalzazel.mcodyssey.datagen.items.ItemCreator
import me.shadowalzazel.mcodyssey.common.listeners.utility.LootLogic
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

object LootListeners : Listener, ItemCreator {

    @EventHandler
    fun mobDeathDropHandler(event: EntityDeathEvent) {
        if (event.entity.killer !is Player) return
        val mob = event.entity
        if (!mob.hasAI()) return
        val killer = mob.killer!!
        // Mineshaft
        if (mob.scoreboardTags.contains(EntityTags.IN_MINESHAFT)) {
            val mobLootLogic = LootLogic(1.0, mob, killer)
            if (mobLootLogic.roll(33.0)) {
                mob.world.dropItem(mob.location, Ingredients.SILVER_NUGGET.createStack(1))
            }
        }
        // Arcane Book Handler
        if (mob.hasLineOfSight(killer)) {
            mobArcaneBookLoot(mob, killer)
        }
    }


    private fun mobArcaneBookLoot(mob: LivingEntity, player: Player) {
        // Create a calculation class that handles all the logic
        val mobLootLogic = LootLogic(1.0, mob, player)
        // Loot check and entity check
        when (mob) {
            is Skeleton -> {
                if (mobLootLogic.roll(2.5)) {
                    val rangedSet = OdysseyEnchantments.RANGED_SET.filter { enchant -> enchant !in OdysseyEnchantments.EXOTIC_LIST }
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(rangedSet.random(), 1)))
                    droppedItemSound(player)
                }
            }
            is Husk -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.DECAY, 1)))
                    droppedItemSound(player)
                }
            }
            is Drowned -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.BANE_OF_THE_SEA, 1)))
                    droppedItemSound(player)
                }
            }
            is PigZombie -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.BANE_OF_THE_SWINE, 1)))
                    droppedItemSound(player)
                }
            }
            is Zombie -> {
                if (mobLootLogic.roll(2.5)) {
                    val meleeSet = OdysseyEnchantments.MELEE_SET.filter { enchant -> enchant !in OdysseyEnchantments.EXOTIC_LIST }
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(meleeSet.random(), 1)))
                    droppedItemSound(player)
                }
                // TODO: Is Blood Moon
                if (mobLootLogic.roll(4.0)) {
                    mob.world.dropItem(mob.location, (Ingredients.COAGULATED_BLOOD.newItemStack((1..3).random())))
                }
            }
            is Witch -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.ALCHEMY_ARTILLERY, 1)))
                    droppedItemSound(player)
                }
            }
            is Stray -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.FREEZING_ASPECT, 1)))
                    droppedItemSound(player)
                }
            }
            is Creeper -> {
                // If charged creeper kills catalyst, drop echo shard
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.EXPLODING, 1)))
                    droppedItemSound(player)
                }
            }
            is Hoglin -> {
                if (mobLootLogic.roll(1.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.HEMORRHAGE, 1)))
                    droppedItemSound(player)
                }
            }
            is Vindicator -> {
                if (mobLootLogic.roll(3.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.BANE_OF_THE_ILLAGER, 1)))
                    droppedItemSound(player)
                }
            }
            is Pillager -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.BURST_BARRAGE, 1)))
                    droppedItemSound(player)
                }
            }
            is Ravager -> {
                if (mobLootLogic.roll(5.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.WHIRLWIND, 1)))
                    droppedItemSound(player)
                }
            }
            is Squid -> {
                if (mobLootLogic.roll(3.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.SQUIDIFY, 1)))
                    droppedItemSound(player)
                }
            }
            is Shulker -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.VOID_STRIKE, 1))) // change
                    droppedItemSound(player)
                }
            }
            is Endermite -> {
                if (mobLootLogic.roll(3.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.VOID_STRIKE, 1)))
                    droppedItemSound(player)
                }
            }
            is Enderman -> {
                if (mobLootLogic.roll(2.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.VOID_JUMP, 1)))
                    droppedItemSound(player)
                }
            }
            is Vex -> {
                if (mobLootLogic.roll(4.5)) {
                    mob.world.dropItem(mob.location, (Miscellaneous.TOTEM_OF_VEXING.newItemStack(1)))
                }
            }
            is ElderGuardian -> {
                mob.world.dropItem(mob.location, (Ingredients.IRRADIATED_ROD.newItemStack((1..3).random())))
                mob.world.dropItem(mob.location, (Ingredients.IRRADIATED_SHARD.newItemStack((2..5).random())))
            }
            is Warden -> {
                mob.world.dropItem(mob.location, (Ingredients.WARDEN_ENTRAILS.newItemStack(1)))
                mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.SCULK_SENSITIVE, 1)))
                droppedItemSound(player)
            }
            is Wither -> {
                mob.world.dropItem(mob.location, (Miscellaneous.ARCANE_BOOK.createArcaneBookStack(OdysseyEnchantments.BLACK_ROSE, 1)))
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

    private fun droppedItemSound(player: Player) {
        with(player) {
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1.5F, 0.9F)
            playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5F, 0.9F)
            world.spawnParticle(Particle.TOTEM_OF_UNDYING, location, 25, 2.0, 1.0, 2.0)
            world.spawnParticle(Particle.END_ROD, location, 25, 2.0, 1.0, 2.0)
            world.spawnParticle(Particle.GLOW, location, 35, 2.0, 1.0, 2.0)
        }
    }

}