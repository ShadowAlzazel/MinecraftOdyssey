package me.shadowalzazel.mcodyssey.mobs.base

import org.bukkit.entity.FallingBlock
import org.bukkit.scheduler.BukkitRunnable

class FallingBlockTimer(private val someFallingBlock: FallingBlock) : BukkitRunnable() {

    // Run every 10 sec
    override fun run() {
        if (!someFallingBlock.isOnGround) {
            println("Q")
            if (someFallingBlock.ticksLived > 1) someFallingBlock.ticksLived = 1
        }
        else {
            this.cancel()
        }
    }

}