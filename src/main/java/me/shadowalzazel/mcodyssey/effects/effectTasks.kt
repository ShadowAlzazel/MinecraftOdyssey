package me.shadowalzazel.mcodyssey.effects

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

// BLAZING task
class BlazingTask(dousedEntity: LivingEntity, private val douseFactor: Int, private val blazingCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()

    private val dousedVictim = dousedEntity
    private var counter = 0
    override fun run() {
        if (dousedVictim.isInWaterOrRainOrBubbleColumn) {
            this.cancel()
        }
        if ("Ablaze" !in dousedVictim.scoreboardTags) {
            this.cancel()
        }

        //Particles
        val someLocation = dousedVictim.location.clone()
        someLocation.y += 1.0

        val ignitedDust = Material.PUMPKIN.createBlockData()
        dousedVictim.world.spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, ignitedDust)

        dousedVictim.world.spawnParticle(Particle.SMALL_FLAME, someLocation, 50, 1.05, 0.25, 1.05)
        dousedVictim.world.spawnParticle(Particle.SMOKE_NORMAL, someLocation, 10, 1.25, 0.25, 1.25)
        dousedVictim.world.spawnParticle(Particle.FLAME, someLocation, 35, 0.75, 0.25, 0.75, 0)
        dousedVictim.world.spawnParticle(Particle.SMOKE_LARGE, someLocation, 5, 0.75, 0.25, 0.75)
        dousedVictim.world.spawnParticle(Particle.LAVA, someLocation, 85, 0.75, 0.25, 0.75)

        dousedVictim.damage(1.0 + (douseFactor * 0.75))

        counter += 1

        val timeElapsed = System.currentTimeMillis() - dousingCooldown
        if (blazingCount < counter || dousedVictim.health <= 0.0 || timeElapsed > blazingCount * 1000) {
            if (!dousedVictim.isDead) dousedVictim.scoreboardTags.remove("Ablaze")
            this.cancel()
        }

    }

}

// DECAYING_TOUCH task
class DecayingTask(decayingEntity: LivingEntity, private val decayingFactor: Int, private val decayingCount: Int) : BukkitRunnable() {
    private var decayCooldown = System.currentTimeMillis()
    private val decayingVictim = decayingEntity
    private var counter = 0

    override fun run() {
        counter += 1
        if ("Decaying" !in decayingVictim.scoreboardTags) {
            this.cancel()
        }

        decayingVictim.world.spawnParticle(Particle.SPORE_BLOSSOM_AIR , decayingVictim.location, 45, 0.5, 0.75, 0.5)
        decayingVictim.world.spawnParticle(Particle.GLOW, decayingVictim.location, 15, 0.75, 0.8, 0.75)
        decayingVictim.world.spawnParticle(Particle.GLOW_SQUID_INK, decayingVictim.location, 15, 0.25, 0.25, 0.25)
        decayingVictim.world.spawnParticle(Particle.SNEEZE, decayingVictim.location, 45, 0.25, 0.25, 0.25)
        decayingVictim.world.spawnParticle(Particle.SCRAPE, decayingVictim.location, 15, 0.25, 0.4, 0.25)
        decayingVictim.damage(decayingFactor.toDouble() * 0.75)

        val timeElapsed = System.currentTimeMillis() - decayCooldown
        // Every 2 sec
        if (decayingCount < counter || decayingVictim.health <= 0.5 || timeElapsed > (decayingCount * 2) * 1000) {
            if (!decayingVictim.isDead) decayingVictim.scoreboardTags.remove("Decaying")
            this.cancel()
        }

    }

}

// DOUSED task
class DousedTask(dousedEntity: LivingEntity, private val douseCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()
    private val dousedVictim = dousedEntity
    private var counter = 0

    override fun run() {
        counter += 1
        if ("Doused" !in dousedVictim.scoreboardTags) {
            this.cancel()
        }

        val dousingBlock = Material.COAL_BLOCK.createBlockData()
        val dousingDust = Material.BLACK_CONCRETE_POWDER.createBlockData()
        dousedVictim.world.spawnParticle(Particle.BLOCK_CRACK, dousedVictim.location, 35, 0.45, 0.25, 0.45, dousingBlock)
        dousedVictim.world.spawnParticle(Particle.FALLING_DUST, dousedVictim.location, 35, 0.45, 0.25, 0.45, dousingDust)
        dousedVictim.world.spawnParticle(Particle.SMOKE_NORMAL, dousedVictim.location, 15, 0.25, 1.0, 0.25)

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

// FREEZING_ASPECT task
class FreezingTask(freezingEntity: LivingEntity, private val freezeFactor: Int, private val freezingCount: Int) : BukkitRunnable() {
    private var freezeCooldown = System.currentTimeMillis()

    private val freezingVictim = freezingEntity
    private var counter = 0
    override fun run() {
        counter += 1
        if ("Freezing" !in freezingVictim.scoreboardTags) {
            this.cancel()
        }
        freezingVictim.freezeTicks = 100
        freezingVictim.damage(freezeFactor.toDouble())

        val freezingBlock = Material.BLUE_ICE.createBlockData()
        val freezingDust = Material.LAPIS_BLOCK.createBlockData()
        freezingVictim.world.spawnParticle(Particle.BLOCK_CRACK, freezingVictim.location, 35, 0.95, 0.8, 0.95, freezingBlock)
        freezingVictim.world.spawnParticle(Particle.FALLING_DUST, freezingVictim.location, 35, 0.75, 0.25, 0.75, freezingDust)

        freezingVictim.world.spawnParticle(Particle.WHITE_ASH, freezingVictim.location, 75, 1.0, 1.0, 1.0)
        freezingVictim.world.spawnParticle(Particle.SNOWBALL, freezingVictim.location, 45, 0.5, 1.0, 0.5)
        freezingVictim.world.spawnParticle(Particle.FALLING_DRIPSTONE_WATER, freezingVictim.location, 25, 0.5, 1.0, 0.5)
        // MAYBE DO MORE FREEZE TO MAGMA / BLAZE
        val timeElapsed = System.currentTimeMillis() - freezeCooldown
        if (freezingCount < counter || freezingVictim.health <= 0.5 || timeElapsed > freezingCount * 1000) {
            freezingVictim.freezeTicks = 0
            if (!freezingVictim.isDead) freezingVictim.scoreboardTags.remove("Freezing")
            this.cancel()
        }

    }

}

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
class GravitationalAttract(gravitatingEntity: LivingEntity, singularityPoint: Location, private val gravityFactor: Int, private val collapser: Player) : BukkitRunnable() {
    private var attractCooldown = System.currentTimeMillis()
    private val gravityWellVictim = gravitatingEntity
    private val singularityLocation = singularityPoint
    private var counter = 0

    override fun run() {
        counter += 1
        if ("Gravity_Well" !in gravityWellVictim.scoreboardTags) {
            this.cancel()
        }

        gravityWellVictim.world.spawnParticle(Particle.END_ROD, gravityWellVictim.location, 25, 0.5, 0.5, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.LAVA, gravityWellVictim.location, 20, 0.5, 0.5, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.CRIT_MAGIC, gravityWellVictim.location, 45, 0.5, 0.40, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.SMOKE_LARGE, gravityWellVictim.location, 85, 0.1, 0.1, 0.1)
        gravityWellVictim.world.spawnParticle(Particle.PORTAL, gravityWellVictim.location, 55, 0.5, 0.4, 0.5)
        gravityWellVictim.world.spawnParticle(Particle.FLASH, gravityWellVictim.location, 3, 0.1, 0.3, 0.1)

        val nearbyVictims = gravityWellVictim.world.getNearbyLivingEntities(singularityLocation, gravityFactor.toDouble())
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

        val timeElapsed = System.currentTimeMillis() - attractCooldown
        if ((3 + gravityFactor) * 2 < counter || gravityWellVictim.health <= 0.25 || timeElapsed > (3 + gravityFactor) * 1000) {
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

        val timeElapsed = System.currentTimeMillis() - hemorrhageCooldown
        // 9 sec
        if (6 < counter || hemorrhageVictim.health <= 0.1 || timeElapsed > 9 * 1000) {
            if (!hemorrhageVictim.isDead) hemorrhageVictim.scoreboardTags.remove("Hemorrhaging")
            this.cancel()
        }
    }

}


// BUZZY_BEES task
class HoneyedTask(honeyedEntity: LivingEntity, private val honeyFactor: Int) : BukkitRunnable() {
    private var honeyCooldown = System.currentTimeMillis()

    private val honeyedVictim = honeyedEntity
    private var counter = 0
    override fun run() {
        counter += 1
        if ("Honeyed" !in honeyedVictim.scoreboardTags) {
            this.cancel()
        }

        honeyedVictim.world.spawnParticle(Particle.DRIPPING_HONEY, honeyedVictim.location, 15, 0.25, 1.0, 0.25)
        honeyedVictim.world.spawnParticle(Particle.FALLING_HONEY, honeyedVictim.location, 8, 0.25, 1.0, 0.25)
        honeyedVictim.world.spawnParticle(Particle.LANDING_HONEY, honeyedVictim.location, 8, 0.25, 0.4, 0.25)
        val timeElapsed = System.currentTimeMillis() - honeyCooldown
        if (((3 * honeyFactor) + 3) < counter || honeyedVictim.health <= 1.0 || timeElapsed > ((3 * honeyFactor) + 3) * 1000) {
            if (!honeyedVictim.isDead) honeyedVictim.scoreboardTags.remove("Honeyed")
            this.cancel()
        }
    }
}

class SpeedySpursTask(private val mountedPlayer: Player, private val mountEntity: LivingEntity, private val spursFactor: Int) : BukkitRunnable() {
    override fun run() {
        if (mountedPlayer !in mountEntity.passengers) {
            this.cancel()
        }
        val speedEffect = PotionEffect(PotionEffectType.SPEED, 10 * 20 , spursFactor - 1)
        mountEntity.addPotionEffect(speedEffect)
    }
}
