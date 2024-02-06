package me.shadowalzazel.mcodyssey.tasks

import org.bukkit.Material
import org.bukkit.block.Barrel
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable

class KegArtisanProcessTask(private val kegBlock: Block) : BukkitRunnable() {

    override fun run() {
        kegFinishedHandler()
        this.cancel()
    }

    private fun kegFinishedHandler() {
        kegBlock.run {
            // Checks
            if (type != Material.BARREL) return
            val kegBottom = location.clone().add(0.0, -1.0, 0.0).block
            if (kegBottom.type != Material.SCAFFOLDING) return
            // TODO: Add hook check
            for (item in (this as Barrel).inventory) {
                if (item.type != Material.SWEET_BERRIES) continue // TODO: DO XOR WHEAT FOR BEER
                if (item.amount != 1) continue
                // CHANGE TO WINE

            }


        }
    }

}