package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

// FROSTY_FUSE task
class FrostyFuseTask(
    private val victim: LivingEntity,
    private val modifier: Int,
    private val fuseCount: Int): BukkitRunnable()
{
    private var counter = 0

    override fun run() {    // Run every 5 ticks
        victim.also { victim ->
            if (victim.isDead) { this.cancel() }
            if (EffectTags.FROSTY_FUSED !in victim.scoreboardTags) { this.cancel() }
            counter += 1

            with(victim.world) {
                val freezingBlock = org.bukkit.Material.SNOW_BLOCK.createBlockData()
                val freezingDust = org.bukkit.Material.PACKED_ICE.createBlockData()
                val victimLocation = victim.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK, victimLocation, 5, 0.35, 0.4, 0.35, freezingBlock)
                spawnParticle(Particle.FALLING_DUST, victimLocation, 5, 0.25, 0.25, 0.25, freezingDust)
                spawnParticle(Particle.FALLING_DRIPSTONE_WATER, victimLocation, 5, 0.2, 0.4, 0.2)
            }
            if (counter > fuseCount) {
                victim.scoreboardTags.remove(EffectTags.FROSTY_FUSED)
                // Explosion Particles
                with(victim.world) {
                    val freezingBlock = org.bukkit.Material.BLUE_ICE.createBlockData()
                    val freezingDust = org.bukkit.Material.LAPIS_BLOCK.createBlockData()
                    val explosionLocation = victim.location.clone().add(0.0, 0.5, 0.0)
                    spawnParticle(Particle.BLOCK, explosionLocation, 35, 0.75, 0.8, 0.75, freezingBlock)
                    spawnParticle(Particle.FALLING_DUST, explosionLocation, 35, 0.45, 0.35, 0.45, freezingDust)
                    spawnParticle(Particle.FALLING_DRIPSTONE_WATER, explosionLocation, 35, 0.5, 1.0, 0.5)
                    spawnParticle(Particle.ITEM_SNOWBALL, explosionLocation, 45, 0.5, 1.0, 0.5)
                    playSound(victim.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 4.2F, 0.5F)
                }
                // Damage
                victim.location.getNearbyLivingEntities(3.0).forEach {
                    it.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, ((20 * modifier) + 3), 0))
                    it.damage(modifier * 2.5)
                    it.freezeTicks = 15 + (20 * modifier)
                }

                // Create ice pillar?
                if (victim.eyeLocation.block.type in listOf(Material.AIR, Material.WATER)) {
                    victim.eyeLocation.block.type = Material.PACKED_ICE
                }
                if (victim.eyeLocation.clone().subtract(0.0, 1.0, 0.0).block.type in listOf(Material.AIR, Material.WATER)) {
                    victim.eyeLocation.clone().subtract(0.0, 1.0, 0.0).block.type = Material.PACKED_ICE
                }

                this.cancel()
            }
        }
    }
}