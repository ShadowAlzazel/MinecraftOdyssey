package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantmentsManager
import me.shadowalzazel.mcodyssey.tasks.enchantment_tasks.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

object RangedListeners : Listener, EnchantmentsManager {

    private val currentOverchargeTasks = mutableMapOf<UUID, OverchargeTask>()

    // Main function for enchantments relating to shooting bows
    @EventHandler
    fun mainBowShotHandler(event: EntityShootBowEvent) {
        if (event.bow?.hasItemMeta() == false) { return }
        val projectile = event.projectile
        if (projectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW)) { return }
        val shooter = event.entity
        val bow = event.bow ?: return
        // Priority is in the order !!
        // Loop for all enchants
        for (enchant in bow.enchantments) {
            when (enchant.key.getNameId()) {
                "alchemy_artillery" -> {
                    alchemyArtilleryShoot(projectile, enchant.value)
                }
                "ballistics" -> {
                    ballisticsEnchantmentShoot(projectile, enchant.value)
                }
                "bola_shot" -> {
                    bolaShotEnchantmentShoot(projectile, enchant.value)
                }
                "burst_barrage" -> {
                    burstBarrageEnchantmentShoot(projectile, shooter, enchant.value)
                }
                "chain_reaction" -> {
                    chainReactionEnchantmentShoot(projectile, enchant.value)
                }
                "cluster_shot" -> {
                    clusterShotEnchantmentShoot(projectile, enchant.value)
                }
                "deadeye" -> {
                    deadeyeEnchantmentShoot(projectile, enchant.value)
                }
                "death_from_above" -> {
                    deathFromAboveEnchantmentShoot(projectile, enchant.value)
                }
                "double_tap" -> {
                    doubleTapEnchantmentShoot(projectile, shooter)
                }
                "entanglement" -> {
                    entanglementEnchantmentShoot(projectile, enchant.value)
                }
                "fan_fire" -> {
                    fanFireEnchantmentShoot(projectile, shooter, enchant.value)
                }
                "lucky_draw" -> {
                    event.setConsumeItem(!luckyDrawEnchantmentShoot(enchant.value))
                }
                "luxpose" -> {
                    luxposeEnchantmentShoot(projectile, enchant.value)
                }
                "gale" -> {
                    galeEnchantmentShoot(shooter, projectile, enchant.value)
                }
                "overcharge" -> {
                    overchargeEnchantmentShoot(projectile, shooter, bow)
                }
                "perpetual" -> {
                    perpetualProjectileEnchantmentShoot(projectile, enchant.value)
                }
                "ricochet" -> {
                    ricochetEnchantmentShoot(projectile, enchant.value)
                }
                "sharpshooter" -> {
                    sharpshooterEnchantmentShoot(projectile, enchant.value)
                }
                "single_out" -> {
                    singleOutEnchantmentShoot(projectile, enchant.value)
                }
                "singularity_shot" -> {
                    singularityShotEnchantmentShoot(projectile, enchant.value, shooter)
                }
                "rend" -> {
                    rendEnchantmentShoot(projectile, enchant.value)
                }
                "temporal" -> {
                    temporalTorrentEnchantmentShoot(projectile, enchant.value)
                }
                "vulnerocity" -> {
                    vulnerocityEnchantmentShoot(projectile, enchant.value)
                }
            }
        }
    }
    // Main function for enchantments relating to projectile damage
    @EventHandler
    fun mainProjectileDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) { return }
        if (event.damager !is Projectile) { return }
        if (event.entity !is LivingEntity) { return }
        val projectile = event.damager as Projectile
        if (projectile.shooter == null) { return }
        val shooter: LivingEntity = projectile.shooter as LivingEntity
        val victim: LivingEntity = event.entity as LivingEntity
        // Loop over arrow projectile tags
        // TODO: Maybe sort in priority the tags
        for (tag in projectile.scoreboardTags) {
            when (tag) {
                EntityTags.BALLISTICS_ARROW -> {
                    event.damage += ballisticsEnchantmentHit(projectile)
                }
                EntityTags.DEADEYE_ARROW -> {
                    event.damage += deadeyeEnchantmentHit(projectile, victim)
                }
                EntityTags.DEATH_FROM_ABOVE_ARROW -> {
                    event.damage += deathFromAboveEnchantmentHit(projectile, victim, shooter)
                }
                EntityTags.ENTANGLEMENT_ARROW -> {
                    event.damage += entanglementEnchantmentHit(projectile, victim, shooter)
                }
                EntityTags.LUXPOSE_ARROW -> {
                    event.damage += luxposeEnchantmentHit(projectile, victim)
                }
                EntityTags.OVERCHARGE_ARROW -> {
                    event.damage += overchargeEnchantmentHit(projectile)
                }
                EntityTags.RICOCHET_ARROW -> {
                    event.damage += ricochetEnchantmentEntityHit(projectile)
                }
                EntityTags.SHARPSHOOTER_ARROW -> {
                    event.damage += sharpshooterEnchantmentHit(projectile)
                }
                EntityTags.SINGLE_OUT_ARROW -> {
                    event.damage += singleOutEnchantmentHit(projectile, victim)
                }
                EntityTags.SOUL_REND_ARROW -> {
                    soulRendEnchantmentHit(shooter, projectile, victim)
                }
            }
        }
    }

    // Main function for enchantments relating to projectile hits
    @EventHandler
    fun mainProjectileHitHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (event.hitEntity is LivingEntity) {
            val victim: LivingEntity = event.hitEntity as LivingEntity
            if (projectile.scoreboardTags.isEmpty()) return
            val tags = projectile.scoreboardTags.toSet() // Prevent async problems
            for (tag in tags) {
                when (tag) {
                    EntityTags.BOLA_SHOT_ARROW -> {
                        bolaShotEnchantmentHit(projectile, victim)
                    }
                    EntityTags.CHAIN_REACTION_ARROW -> {
                        chainReactionEnchantmentHit(projectile, victim)
                    }
                    EntityTags.CLUSTER_SHOT_ARROW -> {
                        clusterShotEnchantmentHit(projectile, victim)
                    }
                    EntityTags.VULNEROCITY_ARROW -> {
                        vulnerocityEnchantmentHit(projectile, victim)
                    }
                }
            }
        }
        else if (event.hitBlock != null) {
            if (projectile.scoreboardTags.contains(EntityTags.RICOCHET_ARROW)) {
                ricochetEnchantmentBlockHit(projectile, event.hitBlockFace!!.direction)
            }
        }
    }

    // Main function for enchantments relating to loading crossbows
    @EventHandler
    fun crossbowLoadingHandler(event: EntityLoadCrossbowEvent) {
        if (event.crossbow.hasItemMeta()) {
            return
            /*
            val someCrossbow = event.crossbow

            for (enchant in someCrossbow.enchantments) {
                // Continue if not OdysseyEnchant
                //val gildedEnchant = findOdysseyEnchant(enchant.key) ?: continue
                when (enchant.key) {
                    OdysseyEnchantments.ALCHEMY_ARTILLERY -> {
                        // Load More Damage
                    }
                }
            }
             */
        }
    }

    // Main function for readying arrows
    @EventHandler
    fun bowReadyHandler(event: PlayerReadyArrowEvent) {
        // Only works in survival
        if (event.bow.hasItemMeta()) {
            val bow = event.bow
            val player = event.player
            for (enchant in bow.enchantments) {
                when (enchant.key.getNameId()) {
                    "overcharge" -> {
                        overchargeEnchantmentLoad(player, bow, enchant.value)
                    }
                }
            }
        }
    }

    @EventHandler
    fun bowSwapHandsHandler(event: PlayerSwapHandItemsEvent) {
        if (event.offHandItem.type != Material.BOW && event.offHandItem.type != Material.CROSSBOW) return
        val offHand = event.offHandItem
        if (!offHand.hasItemMeta()) return
        if (offHand.hasEnchantment("rend")) {
            soulRendEnchantmentActivate(event.player)
        }
    }

    // TODO: QuickHands -> auto reload?? maybe compact??
    // Helper function for cooldown
    private fun cooldownManager(hitter: LivingEntity, message: String, cooldownMap: MutableMap<UUID, Long>, timer: Double): Boolean {
        if (!cooldownMap.containsKey(hitter.uniqueId)) {
            cooldownMap[hitter.uniqueId] = 0L
        }
        // Cooldown Timer
        val timeElapsed: Long = System.currentTimeMillis() - cooldownMap[hitter.uniqueId]!!
        return if (timeElapsed > timer * 1000) {
            cooldownMap[hitter.uniqueId] = System.currentTimeMillis()
            true
        } else {
            hitter.sendActionBar(
                Component.text(
                    "$message on Cooldown (Time Remaining: ${timer - ((timeElapsed / 1) * 0.001)}s)",
                    TextColor.color(155, 155, 155)
                )
            )
            false
        }
    }

    // Helper function to clone projectiles and their tags and to omit provided ones
    internal fun Projectile.cloneAndTag(projectile: Projectile, omitList: List<String> = listOf()) {
        // CAN HAVE SET SHOOT ON to launch event again but need to fix bugs
        if (this is AbstractArrow) {
            isPersistent = false
            fireTicks = projectile.fireTicks
            pickupStatus = AbstractArrow.PickupStatus.DISALLOWED

        }
        if (this is Arrow) {
            for (effect in (projectile as Arrow).customEffects) {
                addCustomEffect(effect, true)
            }
            basePotionType = projectile.basePotionType
            pierceLevel = maxOf(projectile.pierceLevel - 1, 0)
        }
        if (this is ThrownPotion) {
            item = (projectile as ThrownPotion).item
        }
        if (this is Firework) {
            fireworkMeta = (projectile as Firework).fireworkMeta
            //setGravity(false)
        }
        // Tags
        for (tag in projectile.scoreboardTags) {
            if (tag in omitList) continue
            //if (tag == EntityTags.ORIGINAL_ARROW)
            this.addScoreboardTag(tag)
        }
        // Stats
        fireTicks = projectile.fireTicks
        setGravity(projectile.hasGravity()) // Perpetual
        // Shooter
        shooter = projectile.shooter
        setHasLeftShooter(false)
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun alchemyArtilleryShoot(projectile: Entity, level: Int) {
        if (projectile is Arrow) {
            if (projectile.hasCustomEffects()) {
                // Copy
                val effectList = mutableListOf<PotionEffect>()
                for (effect in projectile.customEffects) {
                    val type = effect.type
                    val duration = (effect.duration * (1 + (0.2 * level))).toInt() // 20/40/60%
                    effectList.add(PotionEffect(type, duration, effect.amplifier))
                }
                for (effect in effectList) {
                    projectile.addCustomEffect(effect, true)
                }
                projectile.velocity.multiply(1 + (0.1 * level))
            }
            // TODO: Fix for 1.20.5 basePotionType
            // getBasePotionType.toEffect
        }
        else if (projectile is ThrownPotion) {
            val effectList = mutableListOf<PotionEffect>()
            for (effect in projectile.effects) {
                val type = effect.type
                val duration = (effect.duration * (1 + (0.2 * level))).toInt() // 20/40/60%
                effectList.add(PotionEffect(type, duration, effect.amplifier))
            }
            val potionMeta = projectile.potionMeta.clone()
            for (effect in effectList) {
                potionMeta.addCustomEffect(effect, true)
            }
            projectile.potionMeta = potionMeta
            projectile.velocity.multiply(1 + (0.1 * level))
        }

    }

    private fun ballisticsEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.BALLISTICS_ARROW)
            setIntTag(EntityTags.BALLISTICS_MODIFIER, level)
        }
    }

    private fun ballisticsEnchantmentHit(projectile: Projectile): Double {
        val modifier = projectile.getIntTag(EntityTags.BALLISTICS_MODIFIER) ?: return 0.0
        return modifier * 1.0
    }

    private fun bolaShotEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.BOLA_SHOT_ARROW)
            setIntTag(EntityTags.BOLA_SHOT_MODIFIER, level)
        }
    }

    private fun bolaShotEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (victim.location.block.type != Material.AIR) return
        // Get modifier
        val modifier = projectile.getIntTag(EntityTags.BOLA_SHOT_MODIFIER) ?: return
        victim.location.block.type = Material.COBWEB
        victim.addPotionEffects(listOf(
            PotionEffect(PotionEffectType.SLOWNESS, 20 * (3 + modifier), 0),
            PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * (3 + modifier), 0)))
    }

    private fun burstBarrageEnchantmentShoot(projectile: Entity, shooter: LivingEntity, level: Int) {
        if (!shooter.scoreboardTags.contains(EntityTags.IS_BURST_BARRAGING) && !projectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW) && projectile is Projectile)  {
            projectile.run {
                if (this is Arrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                if (this is SpectralArrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                if (this is AbstractArrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
            }
            shooter.addScoreboardTag(EntityTags.IS_BURST_BARRAGING)
            val initialVelocity = projectile.velocity.clone()
            // Tasks
            val task = BurstBarrageTask(shooter, level, initialVelocity, projectile)
            task.runTaskTimer(Odyssey.instance, 3, 3)
        }

    }

    private fun chainReactionEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.CHAIN_REACTION_ARROW)
            setIntTag(EntityTags.CHAIN_REACTION_MODIFIER, level + 2)
            addScoreboardTag(EntityTags.ORIGINAL_ARROW)
        }
    }

    private fun chainReactionEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (projectile.scoreboardTags.contains(EntityTags.CHAIN_REACTION_SPAWNED)) return
        val modifier = projectile.getIntTag(EntityTags.CHAIN_REACTION_MODIFIER) ?: return
        if (modifier <= 1) return
        // Get Entities
        val lastHitID = projectile.getIntTag(EntityTags.CHAIN_REACTION_LAST_HIT) ?: 0
        val origin = projectile.location.clone()
        val nearby = victim.location.getNearbyLivingEntities(12.0).filter {
            (it != projectile.shooter && it != victim && it.entityId != lastHitID)
        }
        // Get Nearest
        if (nearby.isEmpty()) return
        var closest = nearby.first()
        var distance = closest.location.distance(origin)
        val speed = 2.22
        if (nearby.size < 10) {
            for (entity in nearby) {
                if (entity == closest) continue
                val entityDistance = entity.location.distance(origin)
                if (entityDistance < distance) {
                    distance = entityDistance
                    closest = entity
                }
            }
        }
        // Velocity
        val destination = closest.location.clone()
        val velocity = destination.clone().subtract(origin).toVector().normalize().multiply(speed)
        // Prevent infinite spawns
        if (!projectile.scoreboardTags.contains(EntityTags.ORIGINAL_ARROW)) {
            projectile.addScoreboardTag(EntityTags.CHAIN_REACTION_SPAWNED)
        }
        (projectile.world.spawnEntity(origin, projectile.type) as Projectile).also {
            it.velocity = velocity
            it.addScoreboardTag(EntityTags.CHAIN_REACTION_ARROW)
            it.setIntTag(EntityTags.CHAIN_REACTION_LAST_HIT, victim.entityId)
            it.setIntTag(EntityTags.CHAIN_REACTION_MODIFIER, modifier - 1)
            it.cloneAndTag(projectile, listOf(EntityTags.ORIGINAL_ARROW))
        }
    }

    private fun clusterShotEnchantmentShoot(projectile: Entity, level: Int) {
        projectile.run {
            addScoreboardTag(EntityTags.CLUSTER_SHOT_ARROW)
            addScoreboardTag(EntityTags.ORIGINAL_ARROW)
            setIntTag(EntityTags.CLUSTER_SHOT_MODIFIER, level)
            if (this is Arrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            if (this is SpectralArrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            if (this is AbstractArrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
        }
    }

    private fun clusterShotEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (!projectile.scoreboardTags.contains(EntityTags.ORIGINAL_ARROW)) return
        val modifier = projectile.getIntTag(EntityTags.CLUSTER_SHOT_MODIFIER) ?: return
        // Radius
        val radiusAmount = 2 + (modifier * 2)
        for (x in 1..radiusAmount) { // 4/6/8/10/12
            // Math
            val angle = Math.PI * 2 * (x / (radiusAmount * 1.0))
            val angularCoordinates: Pair<Double, Double> = Pair(5 * cos(angle), 5 * sin(angle))
            val origin = victim.eyeLocation.clone()
            val destination = origin.clone().add(angularCoordinates.first, 0.1, angularCoordinates.second)
            val newVelocity = destination.subtract(origin).toVector().normalize().multiply(1.77)
            val spawnLocation = origin.clone().add(0.0, 0.5, 0.0)
            (projectile.world.spawnEntity(spawnLocation, projectile.type) as Projectile).also {
                // Projectile
                it.velocity = newVelocity
                it.shooter = projectile.shooter
                it.cloneAndTag(projectile, listOf(EntityTags.CLUSTER_SHOT_ARROW))
            }
        }
    }

    private fun deadeyeEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.DEADEYE_ARROW)
            setIntTag(EntityTags.DEADEYE_MODIFIER, level)
        }
    }

    private fun deadeyeEnchantmentHit(projectile: Entity, victim: LivingEntity): Double {
        val modifier = projectile.getIntTag(EntityTags.DEADEYE_MODIFIER) ?: return 0.0
        val distance = projectile.location.distance(victim.eyeLocation)
        if (distance <= 1.15) {
            return 3.0 + (modifier * 2)
        }
        return 0.0
    }

    private fun deathFromAboveEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.DEATH_FROM_ABOVE_ARROW)
            setIntTag(EntityTags.DEATH_FROM_ABOVE_MODIFIER, level)
        }
    }

    private fun deathFromAboveEnchantmentHit(projectile: Projectile, victim: LivingEntity, shooter: LivingEntity): Double {
        val modifier = projectile.getIntTag(EntityTags.DEATH_FROM_ABOVE_MODIFIER) ?: return 0.0
        val height = 4 + (modifier * 4)
        if (victim.location.distance(shooter.location) > height) {
            return 1.5 + (modifier * 1.5)
        }
        return 0.0
    }

    private fun doubleTapEnchantmentShoot(projectile: Entity, shooter: LivingEntity) {
        if (projectile !is Projectile) return
        if (projectile.scoreboardTags.contains(EntityTags.DOUBLE_TAP_SHOT)) return
        (shooter.world.spawnEntity(shooter.eyeLocation, projectile.type) as Projectile).also {
            it.cloneAndTag(projectile)
            it.addScoreboardTag(EntityTags.DOUBLE_TAP_SHOT)
            //val speed = projectile.velocity.clone().length() * 0.95
            //it.velocity = shooter.eyeLocation.direction.clone().normalize().multiply(speed)
            it.velocity = projectile.velocity.clone().multiply(0.95)
        }

    }

    // Quantum Entanglement does TP (legendary affix)
    private fun entanglementEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.ENTANGLEMENT_ARROW)
            setIntTag(EntityTags.ENTANGLEMENT_MODIFIER, level)
        }
    }

    private fun entanglementEnchantmentHit(projectile: Projectile, victim: LivingEntity, shooter: LivingEntity): Double {
        val modifier = projectile.getIntTag(EntityTags.ENTANGLEMENT_MODIFIER) ?: return 0.0
        victim.addScoreboardTag(EntityTags.ENTANGLED)
        val nearby = victim.location.getNearbyLivingEntities(10.0).filter {
            it.scoreboardTags.contains(EntityTags.ENTANGLED) && it != victim
        }
        if (nearby.isEmpty()) return 0.0
        val other = nearby.first()
        // Math
        val origin1 = victim.eyeLocation.clone()
        val destination1 = other.eyeLocation.clone()
        val origin2 = other.eyeLocation.clone()
        val destination2 = victim.eyeLocation.clone()
        val distance = origin1.distance(origin2)
        val velocity1 = destination1.subtract(origin1).toVector().normalize().multiply(0.5 + (0.125 * distance))
        val velocity2 = destination2.subtract(origin2).toVector().normalize().multiply(0.5 + (0.125 * distance))
        victim.velocity = velocity1
        victim.scoreboardTags.remove(EntityTags.ENTANGLED)
        other.velocity = velocity2
        other.scoreboardTags.remove(EntityTags.ENTANGLED)
        // Damage
        other.damage(modifier * 1.0)
        return modifier * 1.0
    }

    private fun fanFireEnchantmentShoot(projectile: Entity, shooter: LivingEntity, level: Int) {
        if (projectile !is Projectile) return
        if (projectile.scoreboardTags.contains(EntityTags.FAN_FIRE_SHOT)) return
        // Math
        val vector1 = shooter.eyeLocation.direction
        val getVector2 = { destination: Location, orig: Location -> destination.clone().subtract(orig).toVector() }
        val nearby = shooter.world.getNearbyLivingEntities(shooter.location, 8.0).filter {
            it != shooter && vector1.angle(getVector2(it.eyeLocation, shooter.eyeLocation)) < 1.74533
        }
        if (nearby.isEmpty()) return
        val counter = minOf(nearby.size, level)
        val origin = shooter.eyeLocation.clone()
        // Run shoot
        for (x in 1..counter) {
            val target = nearby[x - 1]
            (shooter.world.spawnEntity(shooter.eyeLocation, projectile.type) as Projectile).also {
                it.cloneAndTag(projectile)
                it.scoreboardTags.add(EntityTags.FAN_FIRE_SHOT)
                // Velocity
                val speed = projectile.velocity.length() * 0.5
                val destination = target.eyeLocation.clone()
                val velocity = destination.subtract(origin).toVector().normalize().multiply(speed)
                it.velocity = velocity
            }
        }
    }

    private fun galeEnchantmentShoot(shooter: LivingEntity, projectile: Entity, level: Int) {
        if (projectile.velocity.length() <= 2.2) return
        shooter.world.playSound(shooter.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 2.5F, 1.5F)
        val task = GaleWindTask(shooter, level)
        task.runTaskLater(Odyssey.instance, 5)
    }

    private fun luckyDrawEnchantmentShoot(enchantmentStrength: Int): Boolean {
        return (enchantmentStrength * 10) + 7 > (0..100).random()
    }

    private fun luxposeEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.LUXPOSE_ARROW)
            setIntTag(EntityTags.LUXPOSE_MODIFIER, level)
        }
    }

    private fun luxposeEnchantmentHit(projectile: Projectile, victim: LivingEntity): Double {
        if (!victim.isGlowing) return 0.0
        val modifier = projectile.getIntTag(EntityTags.LUXPOSE_MODIFIER) ?: return 0.0
        return modifier * 1.0
    }

    private fun overchargeEnchantmentLoad(player: Player, bow: ItemStack, level: Int) {
        if (player.scoreboardTags.contains(EntityTags.OVERCHARGING)) return
        player.scoreboardTags.add(EntityTags.OVERCHARGING)
        // Prevent Spam
        player.setIntTag(EntityTags.OVERCHARGE_MODIFIER, 0)
        // Task
        val task = OverchargeTask(player, bow, level)
        val delay = (20 * 2)
        if (currentOverchargeTasks[player.uniqueId] != null) {
            currentOverchargeTasks[player.uniqueId]?.cancel()
        }
        currentOverchargeTasks[player.uniqueId] = task
        task.runTaskTimer(Odyssey.instance, delay.toLong(), 10) // Run Effects every other
    }

    private fun overchargeEnchantmentShoot(projectile: Entity, shooter: LivingEntity, bow: ItemStack) {
        if (!shooter.scoreboardTags.contains(EntityTags.OVERCHARGING)) return
        val modifier = shooter.getIntTag(EntityTags.OVERCHARGE_MODIFIER) ?: return
        // Remove all responsible tags
        shooter.removeScoreboardTag(EntityTags.OVERCHARGING)
        shooter.removeTag(EntityTags.OVERCHARGE_MODIFIER)
        if (modifier == 0) return
        // Get task
        val task = currentOverchargeTasks[shooter.uniqueId] ?: return
        val taskBow = task.bow
        task.cancel()
        if (taskBow != bow) return

        with(projectile) {
            addScoreboardTag(EntityTags.OVERCHARGE_ARROW)
            setIntTag(EntityTags.OVERCHARGE_MODIFIER, modifier)
            velocity = projectile.velocity.multiply(1 + (modifier * 0.2))
        }
    }

    private fun overchargeEnchantmentHit(projectile: Projectile): Double {
        val modifier = projectile.getIntTag(EntityTags.OVERCHARGE_MODIFIER) ?: return 0.0
        return 3.0 * modifier
    }

    private fun perpetualProjectileEnchantmentShoot(projectile: Entity, level: Int) {
        projectile.run {
            addScoreboardTag(EntityTags.PERPETUAL_ARROW)
            setIntTag(EntityTags.PERPETUAL_MODIFIER, level)
            setGravity(false)
            isPersistent = false
        }
    }

    private fun ricochetEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.RICOCHET_ARROW)
            setIntTag(EntityTags.RICOCHET_BOUNCE, 0)
            setIntTag(EntityTags.RICOCHET_MODIFIER, level)
        }
    }

    private fun ricochetEnchantmentEntityHit(projectile: Projectile): Double {
        val modifier = projectile.getIntTag(EntityTags.RICOCHET_BOUNCE) ?: return 0.0
        return modifier * 2.0 // Add 2 * bounce
    }

    private fun ricochetEnchantmentBlockHit(projectile: Projectile, normalVector: Vector) {
        val bounce = projectile.getIntTag(EntityTags.RICOCHET_BOUNCE) ?: return
        val modifier = projectile.getIntTag(EntityTags.RICOCHET_MODIFIER) ?: return
        // Check if it can bounce more
        if (bounce < modifier) {
            projectile.setIntTag(EntityTags.RICOCHET_BOUNCE, bounce + 1)
        }
        else {
            return
        }
        // Spawn New Arrow
        (projectile.world.spawnEntity(projectile.location.clone(), projectile.type) as Projectile).also {
            it.cloneAndTag(projectile)
            it.setIntTag(EntityTags.RICOCHET_BOUNCE, bounce + 1)
            it.setIntTag(EntityTags.RICOCHET_MODIFIER, modifier)
            // Math
            val normal = normalVector.clone().normalize()
            val input = projectile.velocity.clone().normalize()
            it.velocity = input.subtract(normal.multiply(input.dot(normal) * 2.0)).normalize().multiply(max(projectile.velocity.length() - 0.3, 0.0))
        }
        projectile.remove()
    }

    private fun sharpshooterEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            if (velocity.length() <= 2.6) return
            addScoreboardTag(EntityTags.SHARPSHOOTER_ARROW)
            setIntTag(EntityTags.SHARPSHOOTER_MODIFIER, level)
            velocity = velocity.multiply(1 + (0.1 * level))
        }
    }

    private fun sharpshooterEnchantmentHit(projectile: Entity): Double {
        val modifier = projectile.getIntTag(EntityTags.SHARPSHOOTER_MODIFIER) ?: return 0.0
        return modifier * 0.5
    }

    private fun singleOutEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.SINGLE_OUT_ARROW)
            setIntTag(EntityTags.SINGLE_OUT_MODIFIER, level)
        }
    }

    private fun singleOutEnchantmentHit(projectile: Entity, victim: LivingEntity): Double {
        val modifier = projectile.getIntTag(EntityTags.SHARPSHOOTER_MODIFIER) ?: return 0.0
        if (victim.location.world.getNearbyLivingEntities(victim.location, 16.0).isNotEmpty()) return 0.0
        return (modifier * 2.0)
    }

    private fun singularityShotEnchantmentShoot(projectile: Entity, level: Int, shooter: LivingEntity) {
        with(projectile) {
            // Task
            addScoreboardTag(EffectTags.GRAVITY_WELLED)
            addScoreboardTag(EntityTags.MOVING_SINGULARITY)
            val modifier = (level * 1) + 1
            val maxCount = (level * 2) * 2
            val singularityShotTask = GravitySingularityTask(projectile, shooter, modifier, maxCount)
            singularityShotTask.runTaskTimer(Odyssey.instance, 0, 10)
            velocity.multiply(1.0 - (0.1 * modifier))
        }
    }

    private fun rendEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.SOUL_REND_ARROW)
            setIntTag(EntityTags.SOUL_REND_MODIFIER, level)
        }
    }

    private fun soulRendEnchantmentHit(shooter: LivingEntity, projectile: Projectile, victim: LivingEntity) {
        val modifier = projectile.getIntTag(EntityTags.SOUL_REND_MODIFIER) ?: return
        victim.setIntTag(EntityTags.SOUL_RENDED_BY, shooter.entityId)
        victim.setIntTag(EntityTags.SOUL_REND_MODIFIER, modifier)
        victim.world.spawnParticle(Particle.SCULK_SOUL, victim.location, 5, 0.15, 0.15, 0.15)
    }

    private fun soulRendEnchantmentActivate(activator: LivingEntity) {
        activator.location.getNearbyLivingEntities(32.0).forEach {
            val isSoulRended = it.getIntTag(EntityTags.SOUL_RENDED_BY) == activator.entityId
            if (isSoulRended) {
                val modifier = it.getIntTag(EntityTags.SOUL_REND_MODIFIER)!!
                // Damage
                val soulRendDamage = it.arrowsInBody * (modifier * 1.0)
                it.damage(soulRendDamage, activator)
                it.arrowsInBody = 0
                it.removeTag(EntityTags.SOUL_RENDED_BY)
                it.removeTag(EntityTags.SOUL_REND_MODIFIER)
                it.world.spawnParticle(Particle.SOUL, it.location, 15, 0.05, 0.35, 0.05)
                it.world.spawnParticle(Particle.SCULK_SOUL, it.location, 15, 0.25, 0.35, 0.25)
            }
        }
    }

    private fun temporalTorrentEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            if (projectile !is Projectile) return
            addScoreboardTag(EntityTags.TEMPORAL_ARROW)
            setIntTag(EntityTags.TORRENT_MODIFIER, level)
            // Task
            val task = TemporalTorrentTask(level, projectile.velocity.clone(), projectile)
            projectile.velocity = projectile.velocity.clone().multiply(1.0 - (0.1 * level))
            task.runTaskTimer(Odyssey.instance, 0, 4) // Every 4 ticks / 0.2 secs
        }
    }

    private fun vulnerocityEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.VULNEROCITY_ARROW)
            setIntTag(EntityTags.VULNEROCITY_MODIFIER, level)
        }
    }

    private fun vulnerocityEnchantmentHit(projectile: Entity, victim: LivingEntity) {
        val modifier = projectile.getIntTag(EntityTags.VULNEROCITY_MODIFIER) ?: return
        victim.noDamageTicks -= modifier * 2
    }

}