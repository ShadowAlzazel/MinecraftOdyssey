package me.shadowalzazel.mcodyssey.synchronizers

import org.bukkit.Location
import org.bukkit.inventory.BrewerInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class BrewingEventSynchro(private val synchroBrews: MutableMap<Int, ItemStack?>, private val synchroInventory: BrewerInventory) : BukkitRunnable() {

    override fun run() {
        for (brew in synchroBrews) { synchroInventory.setItem(brew.key, brew.value) }
        this.cancel()
    }

}