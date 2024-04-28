package me.shadowalzazel.mcodyssey.tasks

import org.bukkit.Color
import org.bukkit.Particle
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
                //it.inventory.remove(Miscellaneous.PURE_ANTIMATTER_CRYSTAL.createItemStack(1))
                it.damage(314.15)
                this.cancel()
            }
        }
    }
}


class TemporalStasisTask(private val somePlayer: Player) : BukkitRunnable() {

    override fun run() {
        if (somePlayer.isOnline) {
            // Particles
            with(somePlayer.world) {
                val blockLight = Particle.DustOptions(Color.fromBGR(231, 166, 95), 1.0F)
                val blockBreak = org.bukkit.Material.GOLD_BLOCK.createBlockData()
                val blockDust = org.bukkit.Material.GOLD_BLOCK.createBlockData()
                val someLocation = somePlayer.location.clone().add(0.0, 0.35, 0.0)
                spawnParticle(Particle.DUST , someLocation, 75, 0.95, 0.75, 0.95, blockLight)
                spawnParticle(Particle.BLOCK, someLocation, 95, 0.95, 0.8, 0.95, blockBreak)
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, blockDust)
            }
            somePlayer.isInvulnerable = false
        }
        else {
            somePlayer.isInvulnerable = false
        }
        somePlayer.scoreboardTags.remove("Temporal_Stasis")
        this.cancel()
    }

}