package me.shadowalzazel.mcodyssey.common.listeners.utility

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.generator.structure.Structure
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.scheduler.BukkitRunnable

class SculkFinderSynchro(val block: Block, val item: ItemStack) : BukkitRunnable() {

    override fun run() {
        val compass = item.clone()
        val ancientCity = block.world.locateNearestStructure(block.location, Structure.ANCIENT_CITY, 3000, false)
        // If no result re-create
        if (ancientCity == null) {
            block.world.dropItem(block.location, item)
            this.cancel()
            return
        }

        println(ancientCity.location)
        ancientCity.location.y = -64.0
        ancientCity.location.block.type = Material.LODESTONE // TODO: Very Annoying try to find fix
        val newMeta = (compass.itemMeta as CompassMeta).also {
            it.lodestone = ancientCity.location
            it.isLodestoneTracked = true
            it.lodestone = ancientCity.location
        }
        compass.itemMeta = newMeta
        block.world.dropItem(block.location, compass)
        this.cancel()
    }

}