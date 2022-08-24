package me.shadowalzazel.mcodyssey.listeners.utility

import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class UnstableAntimatterTask(private val somePlayer: Player) : BukkitRunnable() {
    private var someCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        somePlayer.also {
            counter += 1

            // Cancel task if player has clear instability tag
            val playerStabilized: Boolean = with(it.scoreboardTags) {
                // Checks if tags in With scope
                var stable = "Unstable_Crafting" !in this
                if ("Clear_Instability" in this) {
                    remove("Unstable_Crafting")
                    remove("Clear_Instability")
                    stable = true
                    this@UnstableAntimatterTask.cancel()
                }
                stable
            }

            // Timer, Checks if player not stabilized to do effect
            val timeElapsed = System.currentTimeMillis() - someCooldown
            if ((10 * 20 < counter || timeElapsed > 10 * 1000) && !playerStabilized) {
                it.scoreboardTags.remove("Unstable_Crafting")
                it.inventory.remove(OdysseyItems.PURE_ANTIMATTER_CRYSTAL.createItemStack(1))
                it.damage(314.15)
                this.cancel()
            }
        }
    }
}