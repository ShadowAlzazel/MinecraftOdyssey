package me.shadowalzazel.mcodyssey.synchronizers

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BrewingStand
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.inventory.BrewerInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class BrewingEventSynchro(private val synchroBrews: MutableMap<Int, ItemStack?>, private val synchroBrewingLocation: Location, val synchroEvent: BrewEvent) : BukkitRunnable() {

    //val synchroList: MutableList<ItemStack?>

    override fun run() {
        for (brew in synchroBrews) {
            println(brew)
            /*
            val someBlock: Block = synchroBrewingLocation.block
            if (someBlock.type == Material.BREWING_STAND && someBlock is BrewingStand) {
                val bStand: BrewingStand = someBlock
                val bInv: BrewerInventory = bStand.inventory
                val bCon = bInv.contents!!
                bCon[brew.key] = brew.value
            }
            //synchroBrewingStand!!.inventory.contents[brew.key] = brew.value!!

             */
            synchroEvent.results[brew.key] = brew.value

        }
    }

}