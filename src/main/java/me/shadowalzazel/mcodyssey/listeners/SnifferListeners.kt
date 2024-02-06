package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.ARCHAIC_NAMESPACE
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.items.Other
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Sniffer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent

object SnifferListeners : Listener {


    // Drops based on humid, veg, weird
    // Rain

    @EventHandler
    fun enableSnifferDigArchaic(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.SNIFFER) return
        // Check if special food for sniffer
        val hand = event.player.equipment.itemInMainHand
        val snifferFood = listOf(Material.BEETROOT_SEEDS, Material.BROWN_MUSHROOM)
        if (hand.type !in snifferFood) return
        // FInd biome
        val sniffer = event.rightClicked as Sniffer
        val biome = sniffer.world.getBiome(sniffer.location)
        println("Key: " + biome.key)
        println("T-Key: " + biome.translationKey())
        println("State:" + sniffer.state)
        if (sniffer.canDig() && sniffer.state != Sniffer.State.FEELING_HAPPY) {
            sniffer.state = Sniffer.State.FEELING_HAPPY
            //
            // remove item
            // Particle
            with(sniffer.world) {
                spawnParticle(Particle.HEART, sniffer.location, 10, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, sniffer.location, 10, 0.5, 0.5, 0.5)
                playSound(sniffer.location, Sound.ENTITY_SNIFFER_HAPPY, 1.2F, 0.5F)
            }
            sniffer.addScoreboardTag(EntityTags.CAN_DIG_BIOME_SEEDS)

        }

        // TEMP ON SNIFFER DIG SET NAMESPACE ON ITEM

    }

    @EventHandler
    fun snifferDigDrop(event: EntityDropItemEvent) {
        if (event.entity.type != EntityType.SNIFFER) return
        val sniffer = event.entity as Sniffer
        if (!sniffer.scoreboardTags.contains(EntityTags.CAN_DIG_BIOME_SEEDS)) return // TODO: Change IDl TEMP DETECTION
        // create seed TEMP
        /*
        event.itemDrop.itemStack = ItemStack(Material.WHEAT_SEEDS, 1).apply {
            addTag(ItemTags.IS_ARCHAIC)
            itemMeta.displayName(Component.text("Archaic Seed"))
        }

         */

        val viableSeeds = listOf(Other.ASPEN_SEED, Other.MAPLE_SEED, Other.SAKURA_SEED, Other.REDWOOD_SEED)
        val seed = viableSeeds.random()

        event.itemDrop.itemStack = seed.createItemStack(1).apply {
            addTag(ItemTags.IS_ARCHAIC_SEED)
            // TODO: Temp when maybe move to constructor
            val namespaceSeed =  when(seed) {
                Other.ASPEN_SEED -> {
                    "odyssey:aspen_forest/tree_aspen"
                }
                Other.MAPLE_SEED -> {
                    "terralith:highlands/forest/trees_maple"
                }
                Other.SAKURA_SEED -> {
                    "odyssey:inazuma/tree_fancy_sakura"
                }
                Other.REDWOOD_SEED -> {
                    "odyssey:redwood/tree_redwood_big"
                }
                else -> {
                    "none"
                }
            }
            addStringTag(ARCHAIC_NAMESPACE, namespaceSeed)
        }

        println(event.entity.type.toString() + " Drops " + event.itemDrop)

    }

    @EventHandler
    fun placeSeed(event: BlockPlaceEvent) {
        if (event.player.equipment?.itemInMainHand?.type != Material.WHEAT_SEEDS) return
        val archaicSeed = event.player.equipment!!.itemInMainHand
        if (!archaicSeed.hasItemMeta()) return
        if (!archaicSeed.hasTag(ItemTags.IS_ARCHAIC_SEED)) return
        if (event.blockAgainst.type != Material.FARMLAND) return
        // Run
        println("PASSED EVENT CHECK")
        event.player.performCommand("place_feature_archaic_seed")
    }

}