package me.shadowalzazel.mcodyssey.mclisteners.utility

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable


// BUZZY_BEES task
class HoneyedTask(honeyedEntity: LivingEntity, private val honeyFactor: Int) : BukkitRunnable() {
    private var honeyCooldown = System.currentTimeMillis()

    private val honeyedVictim = honeyedEntity
    private var counter = 0
    override fun run() {
        println("Effect Honey")
        // some timer
        honeyedVictim.world.spawnParticle(Particle.DRIPPING_HONEY, honeyedVictim.location, 15, 0.25, 1.0, 0.25)
        honeyedVictim.world.spawnParticle(Particle.FALLING_HONEY, honeyedVictim.location, 8, 0.25, 1.0, 0.25)
        honeyedVictim.world.spawnParticle(Particle.LANDING_HONEY, honeyedVictim.location, 8, 0.25, 0.4, 0.25)
        counter += 1

        val timeElapsed = System.currentTimeMillis() - honeyCooldown
        if (((3 * honeyFactor) + 3) < counter || honeyedVictim.health <= 1.0 || timeElapsed > ((3 * honeyFactor) + 3) * 1000) {
            this.cancel()
        }
    }
}

// DOUSED task
class DousedTask(dousedEntity: LivingEntity) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()

    private val dousedVictim = dousedEntity
    private var counter = 0
    override fun run() {
        if ("Doused" !in dousedVictim.scoreboardTags) {
            this.cancel()
        }

        val dousingBlock = Material.COAL_BLOCK.createBlockData()
        val dousingDust = Material.BLACK_CONCRETE_POWDER.createBlockData()
        dousedVictim.world.spawnParticle(Particle.BLOCK_CRACK, dousedVictim.location, 35, 0.45, 0.25, 0.45, dousingBlock)
        dousedVictim.world.spawnParticle(Particle.FALLING_DUST, dousedVictim.location, 35, 0.45, 0.25, 0.45, dousingDust)
        dousedVictim.world.spawnParticle(Particle.SMOKE_NORMAL, dousedVictim.location, 15, 0.25, 1.0, 0.25)
        counter += 1

        val timeElapsed = System.currentTimeMillis() - dousingCooldown
        if (10 < counter || dousedVictim.health <= 0.50 || timeElapsed > 10 * 1000) {
            var removeDouse = false
            if ("Doused" in dousedVictim.scoreboardTags) {
                removeDouse = true
            }
            if (removeDouse) {
                dousedVictim.scoreboardTags.remove("Doused")
            }
            this.cancel()
        }

    }

}

// DOUSED task 2
class DouseIgniteTask(dousedEntity: LivingEntity, private val douseFactor: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()

    private val dousedVictim = dousedEntity
    private var counter = 0
    override fun run() {
        if (dousedVictim.isInWaterOrRainOrBubbleColumn) {
            this.cancel()
        }

        //Particles
        val someLocation = dousedVictim.location.clone()
        someLocation.y += 1.0

        val ignitedDust = Material.PUMPKIN.createBlockData()
        dousedVictim.world.spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, ignitedDust)

        dousedVictim.world.spawnParticle(Particle.SMALL_FLAME, someLocation, 50, 1.05, 0.25, 1.05)
        dousedVictim.world.spawnParticle(Particle.SMOKE_NORMAL, someLocation, 10, 1.25, 0.25, 1.25)
        dousedVictim.world.spawnParticle(Particle.FLAME, someLocation, 35, 0.75, 0.25, 0.75)
        dousedVictim.world.spawnParticle(Particle.SMOKE_LARGE, someLocation, 5, 0.75, 0.25, 0.75)
        dousedVictim.world.spawnParticle(Particle.LAVA, someLocation, 85, 0.75, 0.25, 0.75)

        dousedVictim.damage(1.0 + (douseFactor * 0.75))

        counter += 1

        val timeElapsed = System.currentTimeMillis() - dousingCooldown
        if (((douseFactor * 4) + 4) < counter || dousedVictim.health <= 0.0 || timeElapsed > ((douseFactor * 4) + 4) * 1000) {
            this.cancel()
        }

    }

}


// DECAYING_TOUCH task
class DecayingTask(decayingEntity: LivingEntity, private val decayingTouchFactor: Int) : BukkitRunnable() {
    private var decayCooldown = System.currentTimeMillis()

    private val decayingVictim = decayingEntity
    private var counter = 0
    // Stacked

    override fun run() {

        decayingVictim.world.spawnParticle(Particle.SPORE_BLOSSOM_AIR , decayingVictim.location, 45, 0.5, 0.75, 0.5)
        decayingVictim.world.spawnParticle(Particle.GLOW, decayingVictim.location, 15, 0.75, 0.8, 0.75)
        decayingVictim.world.spawnParticle(Particle.GLOW_SQUID_INK, decayingVictim.location, 15, 0.25, 0.25, 0.25)
        decayingVictim.world.spawnParticle(Particle.SNEEZE, decayingVictim.location, 45, 0.25, 0.25, 0.25)
        decayingVictim.world.spawnParticle(Particle.SCRAPE, decayingVictim.location, 15, 0.25, 0.4, 0.25)
        decayingVictim.damage(decayingTouchFactor.toDouble() * 0.75)
        counter += 1

        val timeElapsed = System.currentTimeMillis() - decayCooldown
        // 10 sec
        if (5 < counter || decayingVictim.health <= 0.5 || timeElapsed > 10 * 1000) {
            this.cancel()
        }

    }

}


// FREEZING_ASPECT task
class FreezingTask(freezingEntity: LivingEntity, private val freezeFactor: Int) : BukkitRunnable() {

    private var freezeCooldown = System.currentTimeMillis()
    private val freezingVictim = freezingEntity
    private var counter = 0

    override fun run() {
        println("Effect LOL")
        // some timer
        freezingVictim.freezeTicks = 100
        freezingVictim.damage(freezeFactor.toDouble())

        val freezingBlock = Material.BLUE_ICE.createBlockData()
        val freezingDust = Material.LAPIS_BLOCK.createBlockData()
        freezingVictim.world.spawnParticle(Particle.BLOCK_CRACK, freezingVictim.location, 35, 0.95, 0.8, 0.95, freezingBlock)
        freezingVictim.world.spawnParticle(Particle.FALLING_DUST, freezingVictim.location, 35, 0.75, 0.25, 0.75, freezingDust)

        freezingVictim.world.spawnParticle(Particle.WHITE_ASH, freezingVictim.location, 75, 1.0, 1.0, 1.0)
        freezingVictim.world.spawnParticle(Particle.SNOWBALL, freezingVictim.location, 45, 0.5, 1.0, 0.5)
        freezingVictim.world.spawnParticle(Particle.FALLING_DRIPSTONE_WATER, freezingVictim.location, 25, 0.5, 1.0, 0.5)
        counter += 1
        // MAYBE DO MORE FREEZE TO MAGMA / BLAZE
        val timeElapsed = System.currentTimeMillis() - freezeCooldown
        if (freezeFactor * 3 < counter || freezingVictim.health <= 1.0 || timeElapsed > freezeFactor * 3 * 1000) {
            freezingVictim.freezeTicks = 0
            this.cancel()
        }

    }
}


// GRAVITY_WELL Task
class GravitationalAttract(gravitatingEntity: LivingEntity, singularityPoint: Location, private val gravityFactor: Int, private val collapser: Player) : BukkitRunnable() {
    private var attractCooldown = System.currentTimeMillis()
    private val gravitatingVictim = gravitatingEntity
    private val singularityLocation = singularityPoint

    private var counter = 0
    override fun run() {
        val timeElapsed = System.currentTimeMillis() - attractCooldown
        //println("Effect Gravity")
        gravitatingVictim.world.spawnParticle(Particle.END_ROD, gravitatingVictim.location, 25, 0.5, 0.5, 0.5)
        gravitatingVictim.world.spawnParticle(Particle.LAVA, gravitatingVictim.location, 20, 0.5, 0.5, 0.5)
        gravitatingVictim.world.spawnParticle(Particle.CRIT_MAGIC, gravitatingVictim.location, 45, 0.5, 0.40, 0.5)
        gravitatingVictim.world.spawnParticle(Particle.SMOKE_LARGE, gravitatingVictim.location, 85, 0.1, 0.1, 0.1)
        gravitatingVictim.world.spawnParticle(Particle.PORTAL, gravitatingVictim.location, 55, 0.5, 0.4, 0.5)
        gravitatingVictim.world.spawnParticle(Particle.FLASH, gravitatingVictim.location, 3, 0.1, 0.3, 0.1)
        counter += 1

        val nearbyVictims = gravitatingVictim.world.getNearbyLivingEntities(singularityLocation, gravityFactor.toDouble())
        nearbyVictims.remove(collapser)
        for (gravitating in nearbyVictims) {
            //gravitating.velocity = singularityLocation.add(0.0, 0.15, 0.0).subtract(gravitating.location).toVector().multiply(0.05)
            val closeLocation = singularityLocation.clone()
            val randomX = (-3..3).random() * 0.08
            val randomZ = (-3..3).random() * 0.08
            gravitating.teleport(closeLocation.add(randomX, 0.1, randomZ))

            if (counter % 2 == 0) {
                gravitating.damage(1.0)
            }

        }

        if ((3 + gravityFactor) * 2 < counter || gravitatingVictim.health <= 0.25 || timeElapsed > (3 + gravityFactor) * 1000) {
            this.cancel()
        }

    }
}


// DECAYING_TOUCH task
class HemorrhageTask(hemorrhagingEntity: LivingEntity, private val hemorrhageFactor: Int) : BukkitRunnable() {
    private var hemorrhageCooldown = System.currentTimeMillis()
    private val hemorrhageVictim = hemorrhagingEntity
    private var counter = 0
    // Stacked

    override fun run() {

        val bloodDust = DustOptions(Color.fromBGR(0, 0, 115), 1.0F)
        val bloodBlockBreak = Material.REDSTONE_BLOCK.createBlockData()
        val bloodBlockDust = Material.NETHERRACK.createBlockData()

        val higherLocation = hemorrhageVictim.location.clone()
        higherLocation.y += 0.35

        hemorrhageVictim.world.spawnParticle(Particle.REDSTONE , higherLocation, 75, 0.95, 0.75, 0.95, bloodDust)
        hemorrhageVictim.world.spawnParticle(Particle.BLOCK_CRACK, higherLocation, 95, 0.95, 0.8, 0.95, bloodBlockBreak)
        hemorrhageVictim.world.spawnParticle(Particle.FALLING_DUST, higherLocation, 35, 0.75, 0.25, 0.75, bloodBlockDust)
        hemorrhageVictim.world.spawnParticle(Particle.DAMAGE_INDICATOR, higherLocation, 25, 0.25, 0.25, 0.25)
        hemorrhageVictim.damage(hemorrhageFactor.toDouble() - 0.5)
        counter += 1

        val timeElapsed = System.currentTimeMillis() - hemorrhageCooldown
        // 9 sec
        if (6 < counter || hemorrhageVictim.health <= 0.1 || timeElapsed > 9 * 1000) {
            this.cancel()
        }

    }

}