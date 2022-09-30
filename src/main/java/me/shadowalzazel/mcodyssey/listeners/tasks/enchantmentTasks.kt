package me.shadowalzazel.mcodyssey.listeners.tasks

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BurstBarrageTask(private val someShooter: LivingEntity, private val burstAmount: Int, private val burstVelocity: Vector, private val burstProjectile: Entity) : BukkitRunnable() {
    private var counter = 0
    private var burstTimer = System.currentTimeMillis()

    override fun run() {
        counter += 1
        // Check if tag removed
        if ("Burst_Shooting" !in someShooter.scoreboardTags) { this.cancel() }

        // Spawn projectile and set velocity
        //someShooter.launchProjectile((burstProjectile as Projectile).javaClass)

        //someShooter.world.spawnEntity(someShooter.location.clone().add(0.0, 1.5, 0.0), burstProjectile.type).also {
        someShooter.launchProjectile((burstProjectile as Projectile).javaClass).also {
            it.addScoreboardTag("Copied_Burst_Arrow")
            if (it is Arrow) {
                it.basePotionData = (burstProjectile as Arrow).basePotionData
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            else if (it is ThrownPotion) {
                it.item = (burstProjectile as ThrownPotion).item
            }
            // Projectile
            if (it is Projectile) {
                for (tag in burstProjectile.scoreboardTags) {
                    it.scoreboardTags.add(tag)
                }
                println("Q")
                it.velocity = someShooter.eyeLocation.direction.clone().normalize().multiply(burstVelocity.length() - 0.1)
            }
        }
        // Fix for effect arrows !!!!

        val timeElapsed = System.currentTimeMillis() - burstTimer
        if (counter > burstAmount || timeElapsed > (0.2 * (burstAmount + 1)) * 1000) {
            someShooter.removeScoreboardTag("Burst_Shooting")
            this.cancel()
        }
    }
}

// GRAVITY_WELL Task
class GravityWellTask(private val gravityWellVictim: LivingEntity, private val collapser: LivingEntity, private val gravityFactor: Int, private val gravityWellCounter: Int) : BukkitRunnable() {
    private var gravityTimer = System.currentTimeMillis()
    private var counter = 0
    private var singularityLocation = gravityWellVictim.location.clone()

    override fun run() {
        gravityWellVictim.also { victim ->
            counter += 1
            // Check if Gravity Well entity has tag
            if ("Gravity_Well" !in victim.scoreboardTags) { this.cancel() }
            if (victim.scoreboardTags.contains("Falling_Singularity")) { singularityLocation = victim.location.clone() }

            // Spawn particles and get nearby entities
            with(victim.world) {
                val someLocation = victim.location.clone().add(0.0, 0.25, 0.0)
                spawnParticle(Particle.END_ROD, someLocation, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT_MAGIC, someLocation, 45, 0.5, 0.40, 0.5)
                spawnParticle(Particle.PORTAL, someLocation, 55, 0.5, 0.4, 0.5)
                spawnParticle(Particle.SONIC_BOOM, someLocation, 2, 0.0, 0.0, 0.0)
            }

            victim.location.getNearbyLivingEntities(gravityFactor.toDouble()).forEach {
                if (!it.scoreboardTags.contains("Falling_Singularity") && it != collapser) {
                    it.teleport(singularityLocation.clone().add((-3..3).random() * 0.08, 0.1, (-3..3).random() * 0.08))
                    if (counter % 2 == 0) { it.damage(0.5 * gravityFactor) }
                }
            }

            // Timer
            val timeElapsed = System.currentTimeMillis() - gravityTimer
            if (counter > gravityWellCounter || victim.health <= 0.25 || timeElapsed > (gravityWellCounter / 2) * 1000) {
                if (!victim.isDead) { victim.scoreboardTags.remove("Gravity_Well") }
                this.cancel()
            }
        }
    }
}

// FROSTY_FUSE TASK
class FrostyFuseTask(private val frostyFuseVictim: LivingEntity, private val factor: Int, private val fuseCount: Int): BukkitRunnable() {
    private var counter = 0
    private var fuseTimer = System.currentTimeMillis()
    // Run every 5 ticks
    override fun run() {
        frostyFuseVictim.also { victim ->
            if (victim.isDead) { this.cancel() }
            if ("Frosted_Fuse" !in victim.scoreboardTags) { this.cancel() }
            counter += 1

            with(victim.world) {
                val freezingBlock = org.bukkit.Material.SNOW_BLOCK.createBlockData()
                val freezingDust = org.bukkit.Material.PACKED_ICE.createBlockData()
                val someLocation = victim.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 5, 0.35, 0.4, 0.35, freezingBlock)
                spawnParticle(Particle.FALLING_DUST, someLocation, 5, 0.25, 0.25, 0.25, freezingDust)
                spawnParticle(Particle.FALLING_DRIPSTONE_WATER, someLocation, 5, 0.2, 0.4, 0.2)
            }

            // Timer
            val timeElapsed = System.currentTimeMillis() - fuseTimer
            if (counter > fuseCount || timeElapsed > (fuseCount / 4) * 1000) {
                // Remove tag
                victim.scoreboardTags.remove("Frosted_Fuse")

                // Explosion
                with(victim.world) {
                    val freezingBlock = org.bukkit.Material.BLUE_ICE.createBlockData()
                    val freezingDust = org.bukkit.Material.LAPIS_BLOCK.createBlockData()
                    val someLocation = victim.location.clone().add(0.0, 0.5, 0.0)
                    spawnParticle(Particle.BLOCK_CRACK, someLocation, 35, 0.75, 0.8, 0.75, freezingBlock)
                    spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.45, 0.35, 0.45, freezingDust)
                    spawnParticle(Particle.FALLING_DRIPSTONE_WATER, someLocation, 35, 0.5, 1.0, 0.5)
                    spawnParticle(Particle.SNOWBALL, someLocation, 45, 0.5, 1.0, 0.5)
                    spawnParticle(Particle.EXPLOSION_LARGE, someLocation, 15, 1.0, 0.5, 1.0)
                    playSound(victim.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 4.2F, 0.5F)
                }
                // Damage
                victim.location.getNearbyLivingEntities(3.0).forEach {
                    it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, ((20 * factor) + 3), 0))
                    it.damage(factor * 2.5)
                    it.freezeTicks = 15 + (20 * factor)
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


// ARCANE_CELL TASK
class ArcaneCellTask(private val arcaneVictim: LivingEntity, private val jailedLocation: Location, private val cellCount: Int) : BukkitRunnable() {
    private var counter = 0
    private var cellTimer = System.currentTimeMillis()
    // Run every 5 ticks
    override fun run() {
        arcaneVictim.also {
            if (it.isDead) { this.cancel() }
            if ("Arcane_Jailed" !in it.scoreboardTags) { this.cancel() }
            counter += 1
            // TODO: Make circular cell purplish/blueish
            it.teleport(jailedLocation)

            // Timer
            val timeElapsed = System.currentTimeMillis() - cellTimer
            if (counter > cellCount || timeElapsed > (cellTimer / 4) * 1000) {
                it.scoreboardTags.remove("Arcane_Jailed")
                this.cancel()
            }
        }

    }
}

// FROG_FRIGHT TASK
class FrogFrightTask(private val toungedEntity: LivingEntity, private val someVector: Vector) : BukkitRunnable() {
    override fun run() {
        if (!toungedEntity.isDead) {
            toungedEntity.velocity = someVector.clone().multiply(-1.1)
        }
        this.cancel()
    }
}

// SPEEDY_SPURS TASK
class SpeedySpursTask(private val mountedPlayer: LivingEntity, private val mountEntity: LivingEntity, private val spursFactor: Int) : BukkitRunnable() {
    override fun run() {
        // Checks if mount is player to add effects
        if (mountedPlayer !in mountEntity.passengers) { this.cancel() }
        val speedEffect = PotionEffect(PotionEffectType.SPEED, 10 * 20 , spursFactor - 1)
        mountEntity.addPotionEffect(speedEffect)
    }
}