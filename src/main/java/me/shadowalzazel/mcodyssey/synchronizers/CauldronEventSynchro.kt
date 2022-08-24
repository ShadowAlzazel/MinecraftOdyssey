package me.shadowalzazel.mcodyssey.synchronizers

import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyAlchemyCauldronRecipe
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class CauldronEventSynchro(private val synchroRecipe: OdysseyAlchemyCauldronRecipe, private val synchroItem: MutableCollection<Item>) : BukkitRunnable() {

    override fun run() {
        synchroRecipe.alchemicalAntithesis(synchroItem)
        this.cancel()
    }

}