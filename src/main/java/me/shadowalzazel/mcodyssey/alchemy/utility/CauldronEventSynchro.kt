package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.alchemy.base.OldCauldronRecipe
import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

// Used to Return to the main thread after async checks
class CauldronEventSynchro(private val recipe: OldCauldronRecipe, private val results: MutableCollection<Item>) : BukkitRunnable() {

    override fun run() {
        recipe.successfulRecipeHandler(results)
        this.cancel()
    }

}