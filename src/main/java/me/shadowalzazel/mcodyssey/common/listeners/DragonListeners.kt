package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.entity.EnderDragonFireballHitEvent
import com.destroystokyo.paper.event.entity.EnderDragonShootFireballEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.tasks.dragon_tasks.DragonLightningStormTask
import me.shadowalzazel.mcodyssey.common.tasks.dragon_tasks.LightningCloudTask
import me.shadowalzazel.mcodyssey.common.tasks.dragon_tasks.LightningEyeTask
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.*
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EnderDragonChangePhaseEvent
import org.bukkit.event.entity.ExplosionPrimeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import kotlin.math.pow

object DragonListeners : Listener {

    // Wraps a handler body so one bad event logs an error instead of
    // propagating. This is the single biggest "stop the server dying" win.
    private inline fun safely(context: String, block: () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {
            Odyssey.instance.logger.severe("DragonListeners error in $context: ${t.message}")
            t.printStackTrace()
        }
    }

    // Returns the highest-block location for this column ONLY if its chunk is
    // already loaded. Never forces a synchronous chunk load, which is what
    // deadlocked the server thread when toHighestLocation was called during
    // chunk/entity loading.
    private fun Location.highestIfLoaded(): Location? {
        val w = world ?: return null
        if (!w.isChunkLoaded(blockX shr 4, blockZ shr 4)) return null
        return clone().toHighestLocation(HeightMap.MOTION_BLOCKING)
    }

    @EventHandler
    fun dragonPhaseHandler(event: EnderDragonChangePhaseEvent) = safely("dragonPhaseHandler") {
        val dragon = event.entity

        when (event.currentPhase) {
            EnderDragon.Phase.LEAVE_PORTAL -> {
                event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                return@safely
            }
            else -> {
                // Target
                val surface = dragon.location.clone().toHighestLocation(HeightMap.MOTION_BLOCKING)
                val dragonLocation = dragon.location.clone()
                val target = surface.getNearbyLivingEntities(30.0).find { it is Player }
                if (target != null) {
                    val destination = target.location.clone()
                    val vector = destination.clone().subtract(dragonLocation).toVector().normalize().multiply(2.3)
                    (dragon.world.spawnEntity(dragonLocation, EntityType.DRAGON_FIREBALL) as DragonFireball).apply {
                        velocity = vector
                        shooter = dragon
                        addScoreboardTag(EntityTags.DRAGON_BOMB)
                        setHasLeftShooter(false)
                    }
                }
            }
        }

        when (event.newPhase) {
            EnderDragon.Phase.ROAR_BEFORE_ATTACK -> {
                dragon.getNearbyEntities(24.0, 16.0, 24.0).forEach {
                    if (it is Player) {
                        it.addPotionEffects(
                            listOf(
                                PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 0),
                                PotionEffect(PotionEffectType.MINING_FATIGUE, 10 * 20, 0),
                                PotionEffect(PotionEffectType.NAUSEA, 1 * 20, 3)
                            )
                        )
                    }
                }
            }
            EnderDragon.Phase.HOVER -> {
                if (5 > (1..10).random()) {
                    event.newPhase = EnderDragon.Phase.STRAFING
                }
            }
            EnderDragon.Phase.STRAFING -> {
                val ground = dragon.location.clone().toHighestLocation(HeightMap.MOTION_BLOCKING)
                val origin = dragon.location.clone()
                ground.getNearbyEntities(20.0, 16.0, 20.0).forEach { player ->
                    if (player is Player) {
                        val destination = player.location.clone()
                        val vector = destination.clone().subtract(origin).toVector().normalize().multiply(2.3)
                        (player.world.spawnEntity(origin, EntityType.DRAGON_FIREBALL) as DragonFireball).apply {
                            velocity = vector
                            shooter = dragon
                            addScoreboardTag(EntityTags.DRAGON_BOMB)
                            setHasLeftShooter(false)
                        }
                    }
                }
                with(ground.world) {
                    spawnEntity(ground, EntityType.LIGHTNING_BOLT)
                    spawnParticle(Particle.SONIC_BOOM, ground, 1, 0.0, 0.0, 0.0)
                    spawnParticle(Particle.ELECTRIC_SPARK, ground, 25, 0.4, 0.1, 0.5)
                }
            }
            EnderDragon.Phase.LAND_ON_PORTAL -> {
                if (1 > (0..1).random()) {
                    event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                    dragon.addScoreboardTag(EntityTags.LEFT_PORTAL)
                }
            }
            EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET -> {
                if (1 > (0..3).random()) {
                    event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                }
            }
            EnderDragon.Phase.LEAVE_PORTAL -> {
                dragon.addScoreboardTag(EntityTags.LEFT_PORTAL)
            }
            EnderDragon.Phase.CIRCLING -> {
                if (7 > (1..10).random()) {
                    event.newPhase = EnderDragon.Phase.STRAFING
                }
            }
            EnderDragon.Phase.FLY_TO_PORTAL -> {
                if (7 > (1..11).random()) {
                    event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                } else if (dragon.isValid) {
                    DragonLightningStormTask(dragon).runTaskTimer(Odyssey.instance, 5, 15)
                }
            }
            else -> {
            }
        }
    }

    @EventHandler
    fun dragonRespawn(event: CreatureSpawnEvent) = safely("dragonRespawn") {
        if (event.entityType != EntityType.ENDER_DRAGON) return@safely
    }

    @EventHandler
    fun dragonShootHandler(event: EnderDragonShootFireballEvent) = safely("dragonShootHandler") {
        val fireball = event.fireball
        if (6 > (1..10).random()) {
            event.fireball.addScoreboardTag(EntityTags.DRAGON_BOMB)
            // FIX: launchProjectile needs the Bukkit interface class, not the
            // CraftBukkit impl class returned by fireball.javaClass. The old
            // code threw IllegalArgumentException on every shot.
            event.entity.launchProjectile(DragonFireball::class.java, fireball.velocity).apply {
                addScoreboardTag(EntityTags.DRAGON_BOMB)
                shooter = fireball.shooter
                setHasLeftShooter(false)
            }
            event.entity.launchProjectile(DragonFireball::class.java, fireball.velocity).apply {
                addScoreboardTag(EntityTags.DRAGON_BOMB)
                setHasLeftShooter(false)
            }
        } else {
            event.entity.world.playSound(event.fireball.location, Sound.ENTITY_BLAZE_SHOOT, 24F, 0.6F)
            event.fireball.addScoreboardTag(EntityTags.LIGHTNING_BALL)
        }
    }

    @EventHandler
    fun dragonFireballHandler(event: EnderDragonFireballHitEvent) = safely("dragonFireballHandler") {
        val fireball = event.entity
        if (fireball.scoreboardTags.contains(EntityTags.DRAGON_BOMB)) {
            // Fireball
            with(fireball.world) {
                // Particles
                spawnParticle(Particle.WITCH, fireball.location, 45, 1.0, 0.2, 1.0)
                // Firework
                (spawnEntity(fireball.location, org.bukkit.entity.EntityType.FIREWORK_ROCKET) as Firework).also {
                    val newMeta = it.fireworkMeta
                    newMeta.power = 110
                    newMeta.addEffect(
                        FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.FUCHSIA)
                            .withFade(Color.FUCHSIA).trail(false).flicker(false).build()
                    )
                    it.fireworkMeta = newMeta
                    it.velocity = Vector(0.0, -3.0, 0.0)
                    it.ticksToDetonate = 1
                }
            }
            // Damage
            val radius = 6.5
            for (entity in fireball.location.getNearbyLivingEntities(radius)) {
                val distance = entity.location.distance(fireball.location)
                val power = (maxOf(radius - distance, 0.0)).pow(1.5) + (maxOf(radius - distance, 0.0)).times(1) + (radius / 2)
                val damageSource = DamageSource.builder(DamageType.MAGIC).build()
                entity.damage(power + 3.0, damageSource)
            }
        }
        if (fireball.scoreboardTags.contains(EntityTags.LIGHTNING_BALL)) {
            event.areaEffectCloud.particle = Particle.ELECTRIC_SPARK
            event.areaEffectCloud.radius += 2.25F
            // Null-safe: shooter may be gone by the time the ball lands.
            val shooter = fireball.shooter as? EnderDragon ?: return@safely
            LightningCloudTask(shooter, event.areaEffectCloud).runTaskTimer(Odyssey.instance, 5, 25)
        }
    }

    @EventHandler
    fun enderCrystalExplosion(event: ExplosionPrimeEvent) = safely("enderCrystalExplosion") {
        if (event.entityType != EntityType.END_CRYSTAL) return@safely
        val world = event.entity.location.world ?: return@safely
        val battle = world.enderDragonBattle ?: return@safely
        val dragon = battle.enderDragon ?: return@safely
        dragon.phase = EnderDragon.Phase.CHARGE_PLAYER

        val eye = (world.spawnEntity(event.entity.location.clone().add(0.0, 2.0, 0.0), EntityType.ITEM_DISPLAY) as ItemDisplay).apply {
            setItemStack(ItemStack(Material.ENDER_EYE, 1))
            glowColorOverride = Color.FUCHSIA
            isGlowing = true
            viewRange = 100F
            billboard = Display.Billboard.CENTER
            displayHeight = 40.0F
            displayWidth = 40.0F
            brightness = Display.Brightness(14, 14)
        }
        LightningEyeTask(dragon, eye).runTaskTimer(Odyssey.instance, 5, 25)
    }
}