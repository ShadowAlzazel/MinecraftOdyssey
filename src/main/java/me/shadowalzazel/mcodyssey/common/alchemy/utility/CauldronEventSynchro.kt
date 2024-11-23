package me.shadowalzazel.mcodyssey.common.alchemy.utility

import me.shadowalzazel.mcodyssey.common.alchemy.base.AlchemyCauldronRecipe
import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

// Used to Return to the main thread after async checks
class CauldronEventSynchro(private val recipe: AlchemyCauldronRecipe, private val items: MutableCollection<Item>) : BukkitRunnable() {

    override fun run() {
        recipe.synchroSuccessHandler(items)
        this.cancel()
    }

}