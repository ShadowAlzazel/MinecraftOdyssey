package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

// Used to Return to the main thread after async checks
class CauldronEventSynchro(private val cauldronRecipe: AlchemyCauldronRecipe, private val itemResults: MutableCollection<Item>) : BukkitRunnable() {

    override fun run() {
        cauldronRecipe.successfulRecipeHandler(itemResults)
        this.cancel()
    }

}