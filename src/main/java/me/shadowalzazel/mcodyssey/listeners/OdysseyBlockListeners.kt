package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.assets.ItemModels
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

object OdysseyBlockListeners : Listener {

    @EventHandler
    fun onCustomBlockPlace(event: BlockPlaceEvent) {
        if (event.itemInHand.itemMeta?.hasCustomModelData() == true) {
            with(event.itemInHand.itemMeta.customModelData) {
                when(this) {
                    ItemModels.VIOLET -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }


}