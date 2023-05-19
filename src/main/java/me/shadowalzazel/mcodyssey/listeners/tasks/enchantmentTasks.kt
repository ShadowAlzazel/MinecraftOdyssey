package me.shadowalzazel.mcodyssey.listeners.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BurstBarrageTask(
    private val shooter: LivingEntity,
    private val amount: Int,
    private val velocity: Vector,
    private val projectile: Projectile) : BukkitRunnable()
{
    private var counter = 0
    private var timer = System.currentTimeMillis()
    private val initialTags = mutableListOf<String>()

    override fun run() {
        counter += 1
        // Check if tag removed
        if (EntityTags.IS_BURST_BARRAGING !in shooter.scoreboardTags) { this.cancel() }
        if (counter == 1)  { initialTags.addAll(projectile.scoreboardTags) }

        // Spawn projectile and set velocity
        shooter.launchProjectile(projectile.javaClass).also {
            it.addScoreboardTag(EntityTags.REPLICATED_ARROW)
            if (it is Arrow) {
                it.shooter = shooter
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                it.basePotionData = (projectile as Arrow).basePotionData
            }
            else if (it is ThrownPotion) {
                it.shooter = shooter
                it.item = (projectile as ThrownPotion).item
            }
            // Projectile
            it.scoreboardTags.addAll(initialTags)
            it.velocity = shooter.eyeLocation.direction.clone().normalize().multiply(velocity.length() - 0.1)
        }
        // TODO: Fix for effect arrows !!!!

        val timeElapsed = System.currentTimeMillis() - timer
        if (counter > amount || timeElapsed > (0.2 * (amount + 1)) * 1000) {
            shooter.removeScoreboardTag(EntityTags.IS_BURST_BARRAGING)
            this.cancel()
        }
    }
}

// GRAVITY_WELL Task
class GravityWellTask(
    private val victim: LivingEntity,
    private val attacker: LivingEntity,
    private val modifier: Int,
    private val maxCounter: Int) : BukkitRunnable()
{
    private var timer = System.currentTimeMillis()
    private var counter = 0
    private var singularityLocation = victim.location.clone()

    override fun run() {
        victim.also { victim ->
            counter += 1
            // Check if Gravity Well entity has tag
            if (EffectTags.GRAVITY_WELLED !in victim.scoreboardTags) { this.cancel() }
            if (victim.scoreboardTags.contains(EntityTags.FALLING_SINGULARITY) || victim.scoreboardTags.contains(EntityTags.MOVING_SINGULARITY)) { singularityLocation = victim.location.clone() }

            // Spawn particles and get nearby entities
            with(victim.world) {
                val someLocation = singularityLocation.clone().add(0.0, 0.25, 0.0)
                spawnParticle(Particle.END_ROD, someLocation, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT_MAGIC, someLocation, 45, 0.5, 0.40, 0.5)
                spawnParticle(Particle.PORTAL, someLocation, 55, 0.5, 0.4, 0.5)
                spawnParticle(Particle.SONIC_BOOM, someLocation, 2, 0.0, 0.0, 0.0)
            }

            victim.location.getNearbyLivingEntities(modifier.toDouble()).forEach {
                if (!it.scoreboardTags.contains(EntityTags.FALLING_SINGULARITY) && it != attacker) {
                    it.teleport(singularityLocation.clone().add((-3..3).random() * 0.08, 0.1, (-3..3).random() * 0.08))
                    if (counter % 2 == 0) { it.damage(0.5 * modifier) }
                }
            }

            // Timer
            val timeElapsed = System.currentTimeMillis() - timer
            if (counter > maxCounter || victim.health <= 0.25 || timeElapsed > (maxCounter / 2) * 1000) {
                if (!victim.isDead) { victim.scoreboardTags.remove(EffectTags.GRAVITY_WELLED) }
                this.cancel()
            }
        }
    }
}

// FROSTY_FUSE TASK
class FrostyFuseTask(
    private val victim: LivingEntity,
    private val factor: Int,
    private val fuseCount: Int): BukkitRunnable()
{
    private var counter = 0
    private var timer = System.currentTimeMillis()
    // Run every 5 ticks
    override fun run() {
        victim.also { victim ->
            if (victim.isDead) { this.cancel() }
            if (EffectTags.FROSTY_FUSED !in victim.scoreboardTags) { this.cancel() }
            counter += 1

            with(victim.world) {
                val freezingBlock = org.bukkit.Material.SNOW_BLOCK.createBlockData()
                val freezingDust = org.bukkit.Material.PACKED_ICE.createBlockData()
                val victimLocation = victim.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK_CRACK, victimLocation, 5, 0.35, 0.4, 0.35, freezingBlock)
                spawnParticle(Particle.FALLING_DUST, victimLocation, 5, 0.25, 0.25, 0.25, freezingDust)
                spawnParticle(Particle.FALLING_DRIPSTONE_WATER, victimLocation, 5, 0.2, 0.4, 0.2)
            }
            // Timer
            val timeElapsed = System.currentTimeMillis() - timer
            if (counter > fuseCount || timeElapsed > (fuseCount / 4) * 1000) {
                victim.scoreboardTags.remove(EffectTags.FROSTY_FUSED)

                // Explosion
                with(victim.world) {
                    val freezingBlock = org.bukkit.Material.BLUE_ICE.createBlockData()
                    val freezingDust = org.bukkit.Material.LAPIS_BLOCK.createBlockData()
                    val explosionLocation = victim.location.clone().add(0.0, 0.5, 0.0)
                    spawnParticle(Particle.BLOCK_CRACK, explosionLocation, 35, 0.75, 0.8, 0.75, freezingBlock)
                    spawnParticle(Particle.FALLING_DUST, explosionLocation, 35, 0.45, 0.35, 0.45, freezingDust)
                    spawnParticle(Particle.FALLING_DRIPSTONE_WATER, explosionLocation, 35, 0.5, 1.0, 0.5)
                    spawnParticle(Particle.SNOWBALL, explosionLocation, 45, 0.5, 1.0, 0.5)
                    spawnParticle(Particle.EXPLOSION_LARGE, explosionLocation, 15, 1.0, 0.5, 1.0)
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
class ArcaneCellTask(
    private val victim: LivingEntity,
    private val jailedLocation: Location,
    private val cellCount: Int) : BukkitRunnable()
{
    private var counter = 0
    private var timer = System.currentTimeMillis()
    // Run every 5 ticks
    override fun run() {
        victim.also {
            if (it.isDead) { this.cancel() }
            if (EffectTags.ARCANE_JAILED !in it.scoreboardTags) { this.cancel() }
            counter += 1
            // TODO: Make circular cell purplish/blueish
            it.teleport(jailedLocation)

            // Timer
            val timeElapsed = System.currentTimeMillis() - timer
            if (counter > cellCount || timeElapsed > (timer / 4) * 1000) {
                it.scoreboardTags.remove(EffectTags.ARCANE_JAILED)
                this.cancel()
            }
        }

    }
}

// FROG_FRIGHT TASK
class FrogFrightTask(
    private val victim: LivingEntity,
    private val pullDirection: Vector) : BukkitRunnable()
{
    override fun run() {
        if (!victim.isDead) {
            victim.velocity = pullDirection.clone().multiply(-1.1)
        }
        this.cancel()
    }
}

// OVERCHARGE TASK
class OverchargeTask(
    private val player: Player,
    private val bow: ItemStack,
    private val factor: Int) : BukkitRunnable()
{
    override fun run() {
        player.also {
            // Checks if mount is player to add effects
            if (it.activeItem != bow) {
                if (it.scoreboardTags.contains("Bow_Overcharging")) {
                    it.scoreboardTags.remove("Bow_Overcharging")
                }

                for (x in 1..5) {
                    if (it.scoreboardTags.contains("Bow_Overcharge_Modifier_$x")) {
                        it.scoreboardTags.remove("Bow_Overcharge_Modifier_$x")
                    }
                }
                this.cancel()
                return
            }

            for (x in 0..5) {
                if (it.scoreboardTags.contains("Bow_Overcharge_Modifier_$x") && x <= factor) {
                    it.scoreboardTags.remove("Bow_Overcharge_Modifier_$x")
                    it.scoreboardTags.add("Bow_Overcharge_Modifier_${x + 1}")
                    with(it.world) {
                        spawnParticle(Particle.END_ROD, it.location, 15 * x, 0.5, 0.5, 0.5)
                        spawnParticle(Particle.ELECTRIC_SPARK, it.location, 15 * x, 0.5, 0.5, 0.5)
                        playSound(it.location, Sound.BLOCK_MANGROVE_ROOTS_PLACE, 2.5F, 0.9F)
                        if (x == factor) {
                            playSound(it.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.5F, 1.8F)
                        }
                    }
                    it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 3, x * 2))
                    break
                }
            }
        }
    }
}

class GaleWindTask(
    private val entity: LivingEntity,
    private val modifier: Int) : BukkitRunnable()
{
    override fun run() {
        entity.world.playSound(entity.location, Sound.ITEM_TRIDENT_RIPTIDE_2, 2.5F, 1.2F)
        entity.velocity = entity.eyeLocation.direction.clone().normalize().multiply(modifier * 0.5)
        this.cancel()
    }
}


// SPEEDY_SPURS TASK
// TODO: Change base movement of horse
class SpeedySpursTask(
    private val player: LivingEntity,
    private val mount: LivingEntity,
    private val modifier: Int) : BukkitRunnable()
{
    override fun run() {
        // Checks if mount is player to add effects
        if (player !in mount.passengers) { this.cancel() }
        val speedEffect = PotionEffect(PotionEffectType.SPEED, 10 * 20 , modifier - 1)
        mount.addPotionEffect(speedEffect)
    }
}