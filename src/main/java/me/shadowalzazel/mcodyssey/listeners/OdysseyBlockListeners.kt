package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.ItemModels
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Server
import org.bukkit.block.Block
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import java.util.*

object OdysseyBlockListeners : Listener {

    @EventHandler
    fun onCustomBlockPlace(event: BlockPlaceEvent) {
        if (event.itemInHand.itemMeta?.hasCustomModelData() == true) {
            when(event.itemInHand.itemMeta.customModelData) {
                ItemModels.ELENCUILE_SAPLING -> {
                    println("I")
                    if (!checkValidIdescineTree(event.block)) { event.isCancelled }
                    else { spawnIdescineTree(event.block, event.player.server) }
                }
                else -> {

                }
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private fun checkValidIdescineTree(someBlock: Block): Boolean {
        val lattice = setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
        for (lat in lattice) {
            if (someBlock.location.clone().toCenterLocation().add(lat.first, -1.0, lat.second).block.type != Material.ANCIENT_DEBRIS) { return false }
        }
        return true
    }


    private fun spawnIdescineTree(someBlock: Block, server: Server) {
        println("Q")
        server.structureManager.loadStructure(NamespacedKey(Odyssey.instance,"idescine_tree"))?.place(someBlock.location.toCenterLocation(), true, StructureRotation.NONE, Mirror.NONE, 0, 1F, Random(2121L))
        someBlock.location.getNearbyEntities(10.0, 10.0, 10.0).forEach {
            if (it.type == EntityType.ITEM_FRAME) {
                if ((it as ItemFrame).item.type == Material.YELLOW_GLAZED_TERRACOTTA) {
                    it.remove()
                    println("XX")
                }
            }
        }

    }



}