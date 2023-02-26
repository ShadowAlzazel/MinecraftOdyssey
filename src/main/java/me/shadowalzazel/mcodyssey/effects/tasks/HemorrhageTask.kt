package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// HEMORRHAGE task
class HemorrhageTask(private val hemorrhageVictim: LivingEntity, private val hemorrhageFactor: Int) : BukkitRunnable() {
    private var hemorrhageCooldown = System.currentTimeMillis()
    private var counter = 0
    override fun run() {
        hemorrhageVictim.also {
            counter += 1
            // Check if still hemorrhaging
            if (EffectTags.HEMORRHAGING !in it.scoreboardTags) { this.cancel() }

            // Particles
            with(it.world) {
                val bloodDust = Particle.DustOptions(Color.fromBGR(0, 0, 115), 1.0F)
                val bloodBlockBreak = Material.REDSTONE_BLOCK.createBlockData()
                val bloodBlockDust = Material.NETHERRACK.createBlockData()
                val someLocation = it.location.clone().add(0.0, 0.35, 0.0)
                spawnParticle(Particle.REDSTONE , someLocation, 75, 0.95, 0.75, 0.95, bloodDust)
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 95, 0.95, 0.8, 0.95, bloodBlockBreak)
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, bloodBlockDust)
                spawnParticle(Particle.DAMAGE_INDICATOR, someLocation, 25, 0.25, 0.25, 0.25)
            }

            // Damage
            it.damage(hemorrhageFactor.toDouble())

            // Timer
            val timeElapsed = System.currentTimeMillis() - hemorrhageCooldown
            if (6 < counter || it.health <= 0.1 || timeElapsed > 9 * 1000) { // 9 sec
                if (!it.isDead) { it.scoreboardTags.remove(EffectTags.HEMORRHAGING) }
                this.cancel()
            }
        }
    }
}