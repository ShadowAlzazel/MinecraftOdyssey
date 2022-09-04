 package me.shadowalzazel.mcodyssey.effects

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

// FROG_FRIGHT task
class FrogFrightTask(private val toungedEntity: LivingEntity?, private val someVector: Vector) : BukkitRunnable() {
    override fun run() {
        if (toungedEntity != null) {
            if (!toungedEntity.isDead) {
                toungedEntity.velocity = someVector.clone().multiply(-1.1)
            }
        }
        this.cancel()
    }
}

// GRAVITY_WELL Task
class GravitationalAttract(private val gravityWellVictim: LivingEntity, private val collapser: LivingEntity, private val gravityFactor: Int, private val gravityWellCounter: Int) : BukkitRunnable() {
    private var attractCooldown = System.currentTimeMillis()
    private val singularityLocation = gravityWellVictim.location.clone()
    private var counter = 0

    override fun run() {
        gravityWellVictim.also {
            counter += 1
            // Check if Gravity Well entity has tag
            if ("Gravity_Well" !in it.scoreboardTags) { this.cancel() }

            // Spawn particles and get nearby entities
            val nearbyVictims = with(it.world) {
                val someLocation = it.location.clone().add(0.0, 0.25, 0.0)
                spawnParticle(Particle.END_ROD, someLocation, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT_MAGIC, someLocation, 45, 0.5, 0.40, 0.5)
                spawnParticle(Particle.PORTAL, someLocation, 55, 0.5, 0.4, 0.5)
                spawnParticle(Particle.SONIC_BOOM, someLocation, 2, 0.0, 0.0, 0.0)
                getNearbyLivingEntities(singularityLocation, gravityFactor.toDouble())
            }
            // remove collapser
            nearbyVictims.remove(collapser)

            // Pulling
            for (gravitating in nearbyVictims) {
                val newLocation = singularityLocation.clone()
                gravitating.teleport(newLocation.add((-3..3).random() * 0.08, 0.1, (-3..3).random() * 0.08))
                // Do damage every other tick
                if (counter % 2 == 0) { gravitating.damage(1.0) }
            }

            // Timer
            val timeElapsed = System.currentTimeMillis() - attractCooldown
            if (gravityWellCounter < counter || it.health <= 0.25 || timeElapsed > gravityWellCounter * 1000) {
                if (!it.isDead) { it.scoreboardTags.remove("Gravity_Well") }
                this.cancel()
            }
        }
    }
}

// HEMORRHAGE task
class HemorrhageTask(private val hemorrhageVictim: LivingEntity, private val hemorrhageFactor: Int) : BukkitRunnable() {
    private var hemorrhageCooldown = System.currentTimeMillis()
    private var counter = 0
    override fun run() {
        hemorrhageVictim.also {
            counter += 1
            // Check if still hemorrhaging
            if ("Hemorrhaging" !in it.scoreboardTags) { this.cancel() }

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
                if (!it.isDead) { it.scoreboardTags.remove("Hemorrhaging") }
                this.cancel()
            }
        }
    }
}

class SpeedySpursTask(private val mountedPlayer: LivingEntity, private val mountEntity: LivingEntity, private val spursFactor: Int) : BukkitRunnable() {
    override fun run() {
        // Checks if mount is player to add effects
        if (mountedPlayer !in mountEntity.passengers) { this.cancel() }
        val speedEffect = PotionEffect(PotionEffectType.SPEED, 10 * 20 , spursFactor - 1)
        mountEntity.addPotionEffect(speedEffect)
    }
}