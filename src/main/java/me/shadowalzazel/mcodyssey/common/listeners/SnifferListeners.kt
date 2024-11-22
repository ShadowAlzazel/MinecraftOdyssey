package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.util.DataTagManager
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

object SnifferListeners : Listener, DataTagManager {

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
        // Get Archaic Biome Seeds
        val archaicSeeds = listOf(Miscellaneous.ASPEN_SEED, Miscellaneous.MAPLE_SEED, Miscellaneous.SAKURA_SEED, Miscellaneous.REDWOOD_SEED)
        val seed = archaicSeeds.random()
        val drop = seed.newItemStack(1).apply {
            addTag(ItemDataTags.IS_ARCHAIC_SEED)
            val namespaceSeed =  when(seed.itemName) {
                "aspen_seed" -> {
                    "odyssey:aspen_forest/tree_aspen"
                }
                "maple_seed" -> {
                    "terralith:highlands/forest/trees_maple"
                }
                "sakura_seed" -> {
                    "odyssey:inazuma/tree_fancy_sakura"
                }
                "redwood_seed" -> {
                    "odyssey:redwood/tree_redwood_big"
                }
                else -> {
                    "none"
                }
            }
            addStringTag(ItemDataTags.ARCHAIC_NAMESPACE, namespaceSeed)
        }
        event.itemDrop.itemStack = drop
        println(event.entity.type.toString() + " Drops " + event.itemDrop)

    }

    @EventHandler
    fun placeSeed(event: BlockPlaceEvent) {
        if (event.player.equipment.itemInMainHand.type != Material.WHEAT_SEEDS) return
        val archaicSeed = event.player.equipment.itemInMainHand
        if (!archaicSeed.hasItemMeta()) return
        if (!archaicSeed.hasTag(ItemDataTags.IS_ARCHAIC_SEED)) return
        if (event.blockAgainst.type != Material.FARMLAND) return
        // Run
        println("PASSED EVENT CHECK")
        event.player.performCommand("place_feature_archaic_seed")
    }

}