package me.shadowalzazel.mcodyssey.alchemy.utility

import org.bukkit.inventory.BrewerInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class BrewingEventSynchro(private val brewSlotsMap: MutableMap<Int, ItemStack?>, private val brewerInventory: BrewerInventory) : BukkitRunnable() {

    override fun run() {
        for (brew in brewSlotsMap) { brewerInventory.setItem(brew.key, brew.value) }
        this.cancel()
    }

}