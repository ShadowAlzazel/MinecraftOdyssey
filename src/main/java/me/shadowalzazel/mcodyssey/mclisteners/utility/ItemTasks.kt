package me.shadowalzazel.mcodyssey.mclisteners.utility

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class UnstableAntimatterTask(private val somePlayer: Player) : BukkitRunnable() {
    private var someCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        // some timer
        counter += 1

        val timeElapsed = System.currentTimeMillis() - someCooldown
        val isUnstable = ("Unstable_Crafting" in somePlayer.scoreboardTags)
        val stabalized = ("Clear_Instability" in somePlayer.scoreboardTags)

        if (stabalized) {
            somePlayer.scoreboardTags.remove("Unstable_Crafting")
            somePlayer.scoreboardTags.remove("Clear_Instability")
            this.cancel()
        }
        else if ((10 * 20 < counter || timeElapsed > 10 * 1000) && isUnstable) {
            somePlayer.scoreboardTags.remove("Unstable_Crafting")
            somePlayer.damage(314.15)
            println("Died! Unstable!")

            println(somePlayer.inventory.contents)

            //remove item
            this.cancel()
        }
    }
}