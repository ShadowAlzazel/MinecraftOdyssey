package me.shadowalzazel.mcodyssey.effects

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// ABLAZE task
class BlazingTask(private val blazingVictim: LivingEntity, private val blazingFactor: Int, private val blazingCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        blazingVictim.also {
            counter += 1
            // Check if in water or not ablaze
            if (it.isInWaterOrRainOrBubbleColumn || "Ablaze" !in it.scoreboardTags) { this.cancel() }

            // Spawn particles in world
            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 1.0, 0.0)
                val ignitedDust = Material.PUMPKIN.createBlockData()
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, ignitedDust)
                spawnParticle(Particle.SMALL_FLAME, someLocation, 50, 1.05, 0.25, 1.05)
                spawnParticle(Particle.SMOKE_NORMAL, someLocation, 10, 1.25, 0.25, 1.25)
                spawnParticle(Particle.FLAME, someLocation, 35, 0.75, 0.25, 0.75)
                spawnParticle(Particle.SMOKE_LARGE, someLocation, 5, 0.75, 0.25, 0.75)
                spawnParticle(Particle.LAVA, someLocation, 85, 0.75, 0.25, 0.75)
            }

            // Damage
            it.damage(1.0 + (blazingFactor * 0.75))

            // Every 1 sec
            val timeElapsed = System.currentTimeMillis() - dousingCooldown
            if (blazingCount < counter || it.health <= 0.0 || timeElapsed > blazingCount * 1000) {
                if (!it.isDead) { it.scoreboardTags.remove("Ablaze") }
                this.cancel()
            }
        }
    }

}

// DECAYING task
class DecayingTask(private val decayingVictim: LivingEntity, private val decayingFactor: Int, private val decayingCount: Int) : BukkitRunnable() {
    private var decayCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        decayingVictim.also {
            counter += 1
            // Check if no longer decaying
            if ("Decaying" !in decayingVictim.scoreboardTags) { this.cancel() }

            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.SPORE_BLOSSOM_AIR , someLocation, 45, 0.5, 0.75, 0.5)
                spawnParticle(Particle.GLOW, someLocation, 15, 0.75, 0.8, 0.75)
                spawnParticle(Particle.GLOW_SQUID_INK, someLocation, 15, 0.25, 0.25, 0.25)
                spawnParticle(Particle.SNEEZE, someLocation, 45, 0.25, 0.25, 0.25)
                spawnParticle(Particle.SCRAPE, someLocation, 15, 0.25, 0.4, 0.25)
            }

            // Damage
            it.damage(decayingFactor.toDouble() * 0.75)

            // Every 2 sec
            val timeElapsed = System.currentTimeMillis() - decayCooldown
            if (decayingCount < counter || it.health <= 0.5 || timeElapsed > (decayingCount * 2) * 1000) {
                if (!it.isDead) it.scoreboardTags.remove("Decaying")
                this.cancel()
            }
        }
    }

}

// DOUSED task
class DousedTask(private val dousedVictim: LivingEntity, private val douseCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        dousedVictim.also {
            counter += 1
            // Check if no longer Doused
            if ("Doused" !in it.scoreboardTags) { this.cancel() }

            // Check if on fire
            if (it.fireTicks > 0) {
                var dousePower = 0
                // Remove all douses
                for (x in 1..3) {
                    if ("Doused_Factor_$x" in it.scoreboardTags) {
                        it.scoreboardTags.remove("Doused_Factor_$x")
                        dousePower = x
                    }
                }
                // Do ablaze effects
                it.scoreboardTags.remove("Doused")
                it.addScoreboardTag("Ablaze")
                it.fireTicks = 20 * ((dousePower * 4) + 4) + 1
                // Run task
                val blazeDouseTask = BlazingTask(it, dousePower, ((dousePower * 4) + 4))
                blazeDouseTask.runTaskTimer(MinecraftOdyssey.instance, 1, 20)
                this.cancel()
            }

            // Particles
            with(it.world) {
                val dousingBlock = Material.COAL_BLOCK.createBlockData()
                val dousingDust = Material.BLACK_CONCRETE_POWDER.createBlockData()
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 35, 0.45, 0.25, 0.45, dousingBlock)
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.45, 0.25, 0.45, dousingDust)
                spawnParticle(Particle.SMOKE_NORMAL, someLocation, 15, 0.25, 1.0, 0.25)
            }

            // Timing
            val timeElapsed = System.currentTimeMillis() - dousingCooldown
            if (douseCount < counter || it.health <= 0.50 || timeElapsed > (douseCount) * 1000) {
                if ("Doused" in it.scoreboardTags && !it.isDead) { it.scoreboardTags.remove("Doused") }
                this.cancel()
            }
        }
    }
}

// FREEZING task
class FreezingTask(private val freezingVictim: LivingEntity, private val freezeFactor: Int, private val freezingCount: Int) : BukkitRunnable() {
    private var freezeCooldown = System.currentTimeMillis()
    private var counter = 0
    override fun run() {
        freezingVictim.also {
            counter += 1
            // Check if still freezing
            if ("Freezing" !in it.scoreboardTags) { this.cancel() }

            // Particles
            with(it.world) {
                val freezingBlock = Material.BLUE_ICE.createBlockData()
                val freezingDust = Material.LAPIS_BLOCK.createBlockData()
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 35, 0.95, 0.8, 0.95, freezingBlock)
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, freezingDust)
                spawnParticle(Particle.WHITE_ASH, someLocation, 75, 1.0, 1.0, 1.0)
                spawnParticle(Particle.SNOWBALL, someLocation, 45, 0.5, 1.0, 0.5)
                spawnParticle(Particle.FALLING_DRIPSTONE_WATER, someLocation, 25, 0.5, 1.0, 0.5)
            }

            // Damage
            it.freezeTicks = 100
            it.damage(freezeFactor.toDouble())

            // Timing
            val timeElapsed = System.currentTimeMillis() - freezeCooldown
            if (freezingCount < counter || it.health <= 0.5 || timeElapsed > freezingCount * 1000) {
                it.freezeTicks = 0
                if (!it.isDead) { it.scoreboardTags.remove("Freezing") }
                this.cancel()
            }
        }
    }
}


// HONEYED task
class HoneyedTask(private val honeyedVictim: LivingEntity, private val honeyFactor: Int, private val honeyCount: Int) : BukkitRunnable() {
    private var honeyCooldown = System.currentTimeMillis() // TODO: IS this shared??
    private var counter = 0
    override fun run() {
        honeyedVictim.also {
            counter += 1
            // Check if still honeyed
            if ("Honeyed" !in it.scoreboardTags) { this.cancel() }

            // Particles
            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.DRIPPING_HONEY, someLocation, 15, 0.25, 1.0, 0.25)
                spawnParticle(Particle.FALLING_HONEY, someLocation, 8, 0.25, 1.0, 0.25)
                spawnParticle(Particle.LANDING_HONEY, someLocation, 8, 0.25, 0.4, 0.25)
            }

            // Sticky
            it.velocity.y = 0.0

            // Timing
            val timeElapsed = System.currentTimeMillis() - honeyCooldown
            if (honeyCount < counter || it.health <= 1.0 || timeElapsed > (honeyCount / 2 ) * 1000) {
                if (!it.isDead) { it.scoreboardTags.remove("Honeyed") }
                this.cancel()
            }

        }

    }
}


// THORNS TASK
class ThornsTask(private val thornEntity: LivingEntity, private val thornCount: Int) : BukkitRunnable() {
    private var thornsCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (OdysseyEffectTags.THORNY !in thornEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - thornsCooldown
        if (thornCount < counter || timeElapsed > thornCount * 1000) {
            thornEntity.scoreboardTags.remove(OdysseyEffectTags.THORNY)
            this.cancel()
        }
    }

}


// PUFFY N PRICKLY TASK
class PuffyPricklyTask(private val thornEntity: LivingEntity, private val puffyPricklyCount: Int) : BukkitRunnable() {
    private var puffyPricklyCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (OdysseyEffectTags.PUFFY_PRICKLY !in thornEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - puffyPricklyCooldown
        if (puffyPricklyCount < counter || timeElapsed > puffyPricklyCount * 1000) {
            thornEntity.scoreboardTags.remove(OdysseyEffectTags.PUFFY_PRICKLY)
            this.cancel()
        }
    }

}


// Accursed TASK
class AccursedTask(private val accursedEntity: LivingEntity, private val accursedCounter: Int) : BukkitRunnable() {
    private var accursedCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (OdysseyEffectTags.THORNY !in accursedEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - accursedCooldown
        if (accursedCounter < counter || timeElapsed > accursedCounter * 1000) {
            accursedEntity.scoreboardTags.remove(OdysseyEffectTags.THORNY)
            this.cancel()
        }
    }

}
