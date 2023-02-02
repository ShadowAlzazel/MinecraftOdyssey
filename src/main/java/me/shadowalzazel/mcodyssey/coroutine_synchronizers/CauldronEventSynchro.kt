package me.shadowalzazel.mcodyssey.coroutine_synchronizers

import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

class CauldronEventSynchro(private val synchroRecipe: AlchemyCauldronRecipe, private val synchroItem: MutableCollection<Item>) : BukkitRunnable() {

    override fun run() {
        synchroRecipe.alchemyHandler(synchroItem)
        this.cancel()
    }

}