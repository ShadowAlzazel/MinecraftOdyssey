package me.shadowalzazel.mcodyssey.common.arcane

import org.bukkit.scheduler.BukkitRunnable

class AsyncCastingManager(
    val arcaneSpell: ArcaneSpell
) : BukkitRunnable() {

    var waitTicks = 0
    var waitUntilNextTick = false

    override fun run() {
        // Finished
        if (arcaneSpell.isFinished()) {
            this.cancel()
            return
        }

        if (waitUntilNextTick) {
            waitUntilNextTick = false
            return
        }

        if (arcaneSpell.hasCreateSignal) {
            arcaneSpell.hasCreateSignal = false
            arcaneSpell.createCastingCycle()
        }

        if (arcaneSpell.hasCastingSignal) {
            // Reset the signal
            arcaneSpell.hasCastingSignal = false
            arcaneSpell.isCasting = true
            // Run the spell
            arcaneSpell.runCastingCycle()
        }



    }

}