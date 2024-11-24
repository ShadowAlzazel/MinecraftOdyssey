package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

@Suppress("UnstableApiUsage")
class ConflagrateTask(
    private val victim: LivingEntity,
    private val damage: Double
) : BukkitRunnable()
{
    override fun run() {
        if (!victim.isDead) {
            val source = DamageSource.builder(DamageType.ON_FIRE).build()
            victim.damage(damage, source)
            with(victim.world) {
                spawnParticle(Particle.FLAME, victim.location, 20, 0.22, 0.22, 0.22)
                spawnParticle(Particle.LAVA, victim.location, 10, 0.12, 0.32, 0.12)
                playSound(victim.location, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 3.5F, 0.9F)
            }
        }
        this.cancel()
    }
}