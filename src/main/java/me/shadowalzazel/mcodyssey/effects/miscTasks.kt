package me.shadowalzazel.mcodyssey.effects

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
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
        counter += 1
        if ("Gravity_Well" !in gravityWellVictim.scoreboardTags) {
            this.cancel()
        }

        // Particles
        gravityWellVictim.world.spawnParticle(Particle.END_ROD, gravityWellVictim.location, 25, 0.5, 0.5, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.LAVA, gravityWellVictim.location, 20, 0.5, 0.5, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.CRIT_MAGIC, gravityWellVictim.location, 45, 0.5, 0.40, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.SMOKE_LARGE, gravityWellVictim.location, 85, 0.1, 0.1, 0.1)
        gravityWellVictim.world.spawnParticle(Particle.PORTAL, gravityWellVictim.location, 55, 0.5, 0.4, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.FLASH, gravityWellVictim.location, 3, 0.1, 0.3, 0.1)

        // Pull
        val nearbyVictims = gravityWellVictim.world.getNearbyLivingEntities(singularityLocation, gravityFactor.toDouble())
        nearbyVictims.remove(collapser)
        for (gravitating in nearbyVictims) {
            val closeLocation = singularityLocation.clone()
            val randomX = (-3..3).random() * 0.08
            val randomZ = (-3..3).random() * 0.08
            gravitating.teleport(closeLocation.add(randomX, 0.1, randomZ))
            if (counter % 2 == 0) {
                gravitating.damage(1.0)
            }
        }

        // Timer
        val timeElapsed = System.currentTimeMillis() - attractCooldown
        if (gravityWellCounter < counter || gravityWellVictim.health <= 0.25 || timeElapsed > gravityWellCounter * 1000) {
            if (!gravityWellVictim.isDead) gravityWellVictim.scoreboardTags.remove("Gravity_Well")
            this.cancel()
        }

    }
}

// HEMORRHAGE task
class HemorrhageTask(hemorrhagingEntity: LivingEntity, private val hemorrhageFactor: Int) : BukkitRunnable() {
    private var hemorrhageCooldown = System.currentTimeMillis()
    private val hemorrhageVictim = hemorrhagingEntity
    private var counter = 0
    override fun run() {
        counter += 1
        if ("Hemorrhaging" !in hemorrhageVictim.scoreboardTags) {
            this.cancel()
        }

        // Particles
        val bloodDust = Particle.DustOptions(Color.fromBGR(0, 0, 115), 1.0F)
        val bloodBlockBreak = Material.REDSTONE_BLOCK.createBlockData()
        val bloodBlockDust = Material.NETHERRACK.createBlockData()
        val higherLocation = hemorrhageVictim.location.clone()
        higherLocation.y += 0.3
        hemorrhageVictim.world.spawnParticle(Particle.REDSTONE , higherLocation, 75, 0.95, 0.75, 0.95, bloodDust)
        hemorrhageVictim.world.spawnParticle(Particle.BLOCK_CRACK, higherLocation, 95, 0.95, 0.8, 0.95, bloodBlockBreak)
        hemorrhageVictim.world.spawnParticle(Particle.FALLING_DUST, higherLocation, 35, 0.75, 0.25, 0.75, bloodBlockDust)
        hemorrhageVictim.world.spawnParticle(Particle.DAMAGE_INDICATOR, higherLocation, 25, 0.25, 0.25, 0.25)

        // Damage
        hemorrhageVictim.damage(hemorrhageFactor.toDouble())

        val timeElapsed = System.currentTimeMillis() - hemorrhageCooldown
        // Timer
        if (6 < counter || hemorrhageVictim.health <= 0.1 || timeElapsed > 9 * 1000) { // 9 sec
            if (!hemorrhageVictim.isDead) hemorrhageVictim.scoreboardTags.remove("Hemorrhaging")
            this.cancel()
        }
    }

}