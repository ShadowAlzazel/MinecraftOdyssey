package me.shadowalzazel.mcodyssey.effects

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
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

// ABLAZE task
class BlazingTask(dousedEntity: LivingEntity, private val blazingFactor: Int, private val blazingCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()

    private val blazingVictim = dousedEntity
    private var counter = 0
    override fun run() {
        if (blazingVictim.isInWaterOrRainOrBubbleColumn) {
            this.cancel()
        }
        if ("Ablaze" !in blazingVictim.scoreboardTags) {
            this.cancel()
        }

        //Particles
        val someLocation = blazingVictim.location.clone()
        someLocation.y += 1.0

        val ignitedDust = Material.PUMPKIN.createBlockData()
        blazingVictim.world.spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, ignitedDust)

        blazingVictim.world.spawnParticle(Particle.SMALL_FLAME, someLocation, 50, 1.05, 0.25, 1.05)
        blazingVictim.world.spawnParticle(Particle.SMOKE_NORMAL, someLocation, 10, 1.25, 0.25, 1.25)
        blazingVictim.world.spawnParticle(Particle.FLAME, someLocation, 35, 0.75, 0.25, 0.75)
        blazingVictim.world.spawnParticle(Particle.SMOKE_LARGE, someLocation, 5, 0.75, 0.25, 0.75)
        blazingVictim.world.spawnParticle(Particle.LAVA, someLocation, 85, 0.75, 0.25, 0.75)

        blazingVictim.damage(1.0 + (blazingFactor * 0.75))

        counter += 1

        val timeElapsed = System.currentTimeMillis() - dousingCooldown
        if (blazingCount < counter || blazingVictim.health <= 0.0 || timeElapsed > blazingCount * 1000) {
            if (!blazingVictim.isDead) blazingVictim.scoreboardTags.remove("Ablaze")
            this.cancel()
        }

    }

}

// DECAYING task
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
class DousedTask(private val dousedVictim: LivingEntity, private val douseCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if ("Doused" !in dousedVictim.scoreboardTags) {
            this.cancel()
        }

        // Check if on fire
        if (dousedVictim.fireTicks > 0) {
            var dousePower = 0
            for (x in 1..3) {
                if ("Doused_Factor_$x" in dousedVictim.scoreboardTags) {
                    dousedVictim.scoreboardTags.remove("Doused_Factor_$x")
                    dousePower = x
                }
            }
            // Do ablaze effects
            dousedVictim.scoreboardTags.remove("Doused")
            dousedVictim.addScoreboardTag("Ablaze")
            dousedVictim.fireTicks = 20 * ((dousePower * 4) + 4) + 1
            val blazeDouseTask = BlazingTask(dousedVictim, dousePower, ((dousePower * 4) + 4))
            blazeDouseTask.runTaskTimer(MinecraftOdyssey.instance, 1, 20)
            this.cancel()
        }

        // Particles
        val dousingBlock = Material.COAL_BLOCK.createBlockData()
        val dousingDust = Material.BLACK_CONCRETE_POWDER.createBlockData()
        dousedVictim.world.spawnParticle(Particle.BLOCK_CRACK, dousedVictim.location, 35, 0.45, 0.25, 0.45, dousingBlock)
        dousedVictim.world.spawnParticle(Particle.FALLING_DUST, dousedVictim.location, 35, 0.45, 0.25, 0.45, dousingDust)
        dousedVictim.world.spawnParticle(Particle.SMOKE_NORMAL, dousedVictim.location, 15, 0.25, 1.0, 0.25)

        // Timing
        val timeElapsed = System.currentTimeMillis() - dousingCooldown
        if (douseCount < counter || dousedVictim.health <= 0.50 || timeElapsed > (douseCount) * 1000) {
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

// FREEZING task
class FreezingTask(freezingEntity: LivingEntity, private val freezeFactor: Int, private val freezingCount: Int) : BukkitRunnable() {
    private var freezeCooldown = System.currentTimeMillis()

    private val freezingVictim = freezingEntity
    private var counter = 0
    override fun run() {
        counter += 1
        if ("Freezing" !in freezingVictim.scoreboardTags) {
            this.cancel()
        }
        // Damage
        freezingVictim.freezeTicks = 100
        freezingVictim.damage(freezeFactor.toDouble())

        // Freeze Particles
        val freezingBlock = Material.BLUE_ICE.createBlockData()
        val freezingDust = Material.LAPIS_BLOCK.createBlockData()
        freezingVictim.world.spawnParticle(Particle.BLOCK_CRACK, freezingVictim.location, 35, 0.95, 0.8, 0.95, freezingBlock)
        freezingVictim.world.spawnParticle(Particle.FALLING_DUST, freezingVictim.location, 35, 0.75, 0.25, 0.75, freezingDust)
        freezingVictim.world.spawnParticle(Particle.WHITE_ASH, freezingVictim.location, 75, 1.0, 1.0, 1.0)
        freezingVictim.world.spawnParticle(Particle.SNOWBALL, freezingVictim.location, 45, 0.5, 1.0, 0.5)
        freezingVictim.world.spawnParticle(Particle.FALLING_DRIPSTONE_WATER, freezingVictim.location, 25, 0.5, 1.0, 0.5)

        // Timing
        val timeElapsed = System.currentTimeMillis() - freezeCooldown
        if (freezingCount < counter || freezingVictim.health <= 0.5 || timeElapsed > freezingCount * 1000) {
            freezingVictim.freezeTicks = 0
            if (!freezingVictim.isDead) freezingVictim.scoreboardTags.remove("Freezing")
            this.cancel()
        }

    }

}


// HONEYED task
class HoneyedTask(honeyedEntity: LivingEntity, private val honeyFactor: Int, private val honeyCount: Int) : BukkitRunnable() {
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
        if (honeyCount < counter || honeyedVictim.health <= 1.0 || timeElapsed > (honeyCount / 2 ) * 1000) {
            if (!honeyedVictim.isDead) { honeyedVictim.scoreboardTags.remove("Honeyed") }
            this.cancel()
        }
    }
}

