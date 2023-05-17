package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.tasks.BurstBarrageTask
import me.shadowalzazel.mcodyssey.listeners.tasks.GaleWindTask
import me.shadowalzazel.mcodyssey.listeners.tasks.OverchargeTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin


object RangedListeners : Listener {

    private var entityAlchemyArtilleryAmmo = mutableMapOf<UUID, ItemStack?>()
    private var entityAlchemyArtilleryCounter = mutableMapOf<UUID, Int>()
    //
    private var galewindCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to shooting bows
    @EventHandler
    fun mainBowShotHandler(event: EntityShootBowEvent) {
        if (event.bow?.hasItemMeta() == false) { return }
        val someProjectile = event.projectile
        if (someProjectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW)) { return }
        val someShooter = event.entity
        // TODO: Make priority for some enchants

        // Loop for all enchants
        for (enchant in event.bow!!.enchantments) {
            when (enchant.key) {
                OdysseyEnchantments.ALCHEMY_ARTILLERY -> {
                    event.projectile = alchemyArtilleryEnchantmentShoot(someProjectile, someShooter, enchant.value) ?: event.projectile
                }
                OdysseyEnchantments.BOLA_SHOT -> {
                    bolaShotEnchantmentShoot(someProjectile, enchant.value)
                }
                OdysseyEnchantments.BURST_BARRAGE -> {
                    burstBarrageEnchantmentShoot(someProjectile, someShooter, enchant.value)
                }
                OdysseyEnchantments.CHAIN_REACTION -> {
                    chainReactionEnchantmentShoot(someProjectile, enchant.value)
                }
                OdysseyEnchantments.CLUSTER_SHOT -> {
                    clusterShotEnchantmentShoot(someProjectile, enchant.value)
                }
                OdysseyEnchantments.LUCKY_DRAW -> {
                    event.setConsumeItem(!luckyDrawEnchantmentShoot(enchant.value))
                }
                OdysseyEnchantments.GALE_WIND -> {
                    if (cooldownManager(someShooter, "Gale Wind", galewindCooldown, 3.0)) {
                        galeWindEnchantmentShoot(someShooter, enchant.value)
                    }
                }
                OdysseyEnchantments.OVERCHARGE -> {
                    overchargeEnchantmentShoot(someShooter, someProjectile)
                }
                OdysseyEnchantments.PERPETUAL_PROJECTILE -> {
                    perpetualProjectileEnchantmentShoot(someProjectile, enchant.value)
                }
                OdysseyEnchantments.RICOCHET -> {
                    ricochetEnchantmentShoot(someProjectile, enchant.value)
                }
                OdysseyEnchantments.SHARPSHOOTER -> {
                    sharpshooterEnchantmentShoot(someProjectile, enchant.value)
                }
                OdysseyEnchantments.SOUL_REND -> {
                    soulRendEnchantmentShoot(someProjectile, enchant.value)
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
        val someProjectile = event.damager as Projectile
        if (someProjectile.shooter == null) { return }
        val someShooter: LivingEntity = someProjectile.shooter as LivingEntity
        val someHitEntity: LivingEntity = event.entity as LivingEntity

        // Loop over arrow projectile tags
        for (projectileTag in someProjectile.scoreboardTags) {
            when (projectileTag) {
                EntityTags.OVERCHARGE_ARROW -> {
                    event.damage += overchargeEnchantmentHit(someProjectile)
                }
                EntityTags.RICOCHET_ARROW -> {
                    event.damage += ricochetEnchantmentEntityHit(someProjectile)
                }
                EntityTags.SHARPSHOOTER_ARROW -> {
                    event.damage += sharpshooterEnchantmentHit(someProjectile)
                }
                EntityTags.SOUL_REND_ARROW -> {
                    soulRendEnchantmentHit(someShooter, someProjectile, someHitEntity)
                }
            }
        }
    }


    // Main function for enchantments relating to projectile hits
    @EventHandler
    fun mainProjectileHitHandler(event: ProjectileHitEvent) {
        if (event.entity.shooter !is LivingEntity) { return }
        val someShooter: LivingEntity = event.entity.shooter as LivingEntity
        val someProjectile: Projectile = event.entity

        if (event.hitEntity is LivingEntity) {
            val someHitEntity: LivingEntity = event.hitEntity as LivingEntity
            for (projectileTag in someProjectile.scoreboardTags) {
                when (projectileTag) {
                    EntityTags.BOLA_SHOT_ARROW -> {
                        bolaShotEnchantmentHit(someProjectile, someHitEntity)
                    }
                    EntityTags.CHAIN_REACTION_ARROW -> {
                        chainReactionEnchantmentHit(someProjectile, someHitEntity)
                    }
                    EntityTags.CLUSTER_SHOT_ARROW -> {
                        clusterShotEnchantmentHit(someProjectile, someHitEntity)
                    }
                }
            }
        }
        else if (event.hitBlock != null) {
            if (someProjectile.scoreboardTags.contains(EntityTags.RICOCHET_ARROW)) {
                ricochetEnchantmentBlockHit(someProjectile, event.hitBlockFace!!.direction)
            }
        }
    }

    // Main function for enchantments relating to loading crossbows
    @EventHandler
    fun crossbowLoadingHandler(event: EntityLoadCrossbowEvent) {
        val someEntity = event.entity
        if (event.crossbow?.hasItemMeta() == true) {
            val someCrossbow = event.crossbow!!
            for (enchant in someCrossbow.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ALCHEMY_ARTILLERY -> {
                        event.isCancelled = alchemyArtilleryEnchantmentLoad(someEntity, someCrossbow)
                    }
                }
            }
        }
    }

    // Main function for readying arrows
    @EventHandler
    fun bowReadyHandler(event: PlayerReadyArrowEvent) {
        // Only works in survival
        if (event.bow.hasItemMeta()) {
            val someBow = event.bow
            val somePlayer = event.player
            for (enchant in someBow.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.OVERCHARGE -> {
                        overchargeEnchantmentLoad(somePlayer, someBow, enchant.value)
                    }
                }
            }
        }
    }

    // Helper Function for item drop
    @EventHandler
    fun itemDropHandler(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.hasItemMeta()) {
            if (event.itemDrop.itemStack.enchantments.containsKey(OdysseyEnchantments.SOUL_REND)) {
                event.isCancelled = soulRendEnchantmentActivate(event.player)
                // TODO: Weird Activation
            }
        }
    }

    // Helper function for cooldown
    private fun cooldownManager(eventHitter: LivingEntity, someMessage: String, someCooldownMap: MutableMap<UUID, Long>, cooldownTimer: Double): Boolean {
        if (!someCooldownMap.containsKey(eventHitter.uniqueId)) {
            someCooldownMap[eventHitter.uniqueId] = 0L
        }
        // Cooldown Timer
        val timeElapsed: Long = System.currentTimeMillis() - someCooldownMap[eventHitter.uniqueId]!!
        return if (timeElapsed > cooldownTimer * 1000) {
            someCooldownMap[eventHitter.uniqueId] = System.currentTimeMillis()
            true
        } else {
            eventHitter.sendActionBar(
                Component.text(
                    "$someMessage on Cooldown (Time Remaining: ${cooldownTimer - ((timeElapsed / 1) * 0.001)}s)",
                    TextColor.color(155, 155, 155)
                )
            )
            false
        }

    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // ------------------------------- ALCHEMY_ARTILLERY ------------------------------------

    private fun alchemyArtilleryEnchantmentLoad(eventEntity: LivingEntity, eventCrossbow: ItemStack): Boolean {
        if (eventEntity.equipment!!.itemInOffHand.type == Material.SPLASH_POTION || eventEntity.equipment!!.itemInOffHand.type == Material.LINGERING_POTION) {
            val someOffHandPotion = eventEntity.equipment!!.itemInOffHand
            // TODO: Move data to item tag!!!!!!!!
            return if ("Alchemy_Artillery_Loaded" !in eventEntity.scoreboardTags) {
                eventEntity.scoreboardTags.add("Alchemy_Artillery_Loaded")
                // Check if potion
                entityAlchemyArtilleryAmmo[eventEntity.uniqueId] = someOffHandPotion
                // Check counter
                val multiCounter = if (eventCrossbow.itemMeta.hasEnchant(Enchantment.MULTISHOT)) 3 else 1
                entityAlchemyArtilleryCounter[eventEntity.uniqueId] = multiCounter
                // Remove off-hand potion
                eventEntity.equipment!!.setItemInOffHand(ItemStack(Material.AIR, 1))
                false
            } else {
                true
            }
        }
        return false
    }

    private fun alchemyArtilleryEnchantmentShoot(eventProjectile: Entity, eventShooter: LivingEntity, enchantmentStrength: Int): ThrownPotion? {
        var removeTag = false
        var someThrownPotion: ThrownPotion? = null
        if ("Alchemy_Artillery_Loaded" in eventShooter.scoreboardTags) {
            val someCount = entityAlchemyArtilleryCounter[eventShooter.uniqueId]
            if (someCount!! >= 1) {
                // Spawn potion with item
                someThrownPotion = (eventShooter.world.spawnEntity(eventProjectile.location, EntityType.SPLASH_POTION) as ThrownPotion).also {
                    it.item = entityAlchemyArtilleryAmmo[eventShooter.uniqueId] ?: ItemStack(Material.SPLASH_POTION, 1)
                    it.velocity = eventProjectile.velocity.clone().multiply((enchantmentStrength * 0.2) + 0.1)
                    it.shooter = eventShooter
                }
                entityAlchemyArtilleryCounter[eventShooter.uniqueId] = someCount - 1
                if (entityAlchemyArtilleryCounter[eventShooter.uniqueId] == 0) removeTag = true
            }
            if (removeTag) {
                eventShooter.scoreboardTags.remove("Alchemy_Artillery_Loaded")
                entityAlchemyArtilleryAmmo[eventShooter.uniqueId] = null
            }
        }
        return someThrownPotion

    }

    // ------------------------------- BOLA_SHOT ------------------------------------
    private fun bolaShotEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag(EntityTags.BOLA_SHOT_ARROW)
        eventProjectile.addScoreboardTag(EntityTags.BOLA_SHOT_MODIFIER + enchantmentStrength)
    }

    private fun bolaShotEnchantmentHit(eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        // Loop
        for (x in 1..3) {
            if (eventProjectile.scoreboardTags.contains(EntityTags.BOLA_SHOT_MODIFIER + x)) {
                if (eventHitEntity.location.block.type == Material.AIR) {
                    eventHitEntity.location.block.type = Material.COBWEB
                    eventHitEntity.addPotionEffects(listOf(
                        PotionEffect(PotionEffectType.SLOW, 20 * (4 + x), 0),
                        PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * (4 + x), 0)))
                }
                break
            }
        }
    }

    // ------------------------------- BURST_BARRAGE ------------------------------------
    private fun burstBarrageEnchantmentShoot(eventProjectile: Entity, eventShooter: LivingEntity, enchantmentStrength: Int) {
        if (!eventShooter.scoreboardTags.contains(EntityTags.IS_BURST_BARRAGING) && !eventProjectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW) && eventProjectile is Projectile)  {
            eventShooter.addScoreboardTag(EntityTags.IS_BURST_BARRAGING)
            val initialVelocity = eventProjectile.velocity.clone()
            BurstBarrageTask(eventShooter, enchantmentStrength, initialVelocity, eventProjectile).runTaskTimer(Odyssey.instance, 3, 3)
        }

    }

    // ------------------------------- CHAIN_REACTION ------------------------------------
    private fun chainReactionEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        with(eventProjectile) {
            addScoreboardTag(EntityTags.CHAIN_REACTION_ARROW)
            addScoreboardTag(EntityTags.CHAIN_REACTION_MODIFIER + enchantmentStrength)
        }
    }

    private fun chainReactionEnchantmentHit(eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        val closeEntities = eventHitEntity.location.getNearbyLivingEntities(10.0).filter { it != eventProjectile.shooter!! }
        for (x in 1..6) {
            if (eventProjectile.scoreboardTags.contains(EntityTags.CHAIN_REACTION_MODIFIER + (x - 1))) { break }
            // Spawn projectiles
            if (x <= closeEntities.size) {
                val chainVelocity = closeEntities.elementAt(x - 1).location.clone().subtract(eventHitEntity.location.add(0.0, 0.5, 0.0)).toVector().normalize().multiply(1.5)
                eventProjectile.world.spawnEntity(eventProjectile.location, eventProjectile.type).also {
                    it.velocity = chainVelocity
                }
            }
            // TODO: Maybe shoot back!? both directions
        }
    }

    // ------------------------------- CLUSTER_SHOT ------------------------------------
    private fun clusterShotEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag(EntityTags.CLUSTER_SHOT_ARROW)
        eventProjectile.addScoreboardTag(EntityTags.CLUSTER_SHOT_MODIFIER + enchantmentStrength)
        if (eventProjectile is Arrow) { eventProjectile.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED }
    }

    private fun clusterShotEnchantmentHit(eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        var modifier = 0
        for (x in 1..5) {
            if (eventProjectile.scoreboardTags.contains(EntityTags.CHAIN_REACTION_MODIFIER + x)) {
                modifier = x
                break
            }
        }

        // TODO: Maybe Async each new arrow creation
        for (x in 1..(10 + (modifier * 5))) {
            // Math
            val angle = Math.random() * Math.PI * 2
            val coordinates: Pair<Double, Double> = Pair(cos(angle) * (5..12).random() * 0.1, sin(angle) * (5..12).random() * 0.1)
            // Make Random radii?
            val someVelocity = eventProjectile.location.clone().add(coordinates.first, 7.6, coordinates.second).subtract(eventProjectile.location).toVector().normalize().multiply(1.0)
            eventHitEntity.world.spawnEntity(eventHitEntity.location.clone().add(0.0, 0.5, 0.0), eventProjectile.type).also {
                if (it is Arrow) {
                    it.basePotionData = (eventProjectile as Arrow).basePotionData
                    it.isPersistent = false
                    it.fireTicks = eventProjectile.fireTicks
                    it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                else if (it is ThrownPotion) {
                    it.item = (eventProjectile as ThrownPotion).item
                }
                // Projectile
                if (it is Projectile) {
                    it.shooter = eventProjectile.shooter
                    it.velocity = someVelocity
                    for (tag in eventProjectile.scoreboardTags) {
                        if (tag != EntityTags.CLUSTER_SHOT_ARROW) { it.scoreboardTags.add(tag) }
                    }
                }
            }
        }
    }

    // ------------------------------- GALE_WIND ------------------------------------
    private fun galeWindEnchantmentShoot(eventShooter: LivingEntity, enchantmentStrength: Int) {
        eventShooter.world.playSound(eventShooter.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 2.5F, 1.5F)
        GaleWindTask(eventShooter, enchantmentStrength).runTaskLater(Odyssey.instance, 6)
    }

    // ------------------------------- LUCKY_DRAW ------------------------------------
    private fun luckyDrawEnchantmentShoot(enchantmentStrength: Int): Boolean {
        return (enchantmentStrength * 10) + 7 > (0..100).random()
    }

    // ------------------------------- OVERCHARGE ------------------------------------
    private fun overchargeEnchantmentLoad(eventPlayer: Player, eventBow: ItemStack, enchantmentStrength: Int) {
        // TODO: Move Overcharge cooldown to bow
        if (eventPlayer.scoreboardTags.contains("Bow_Overcharge_Cooldown")) {
            eventPlayer.scoreboardTags.remove("Bow_Overcharge_Cooldown")
            return
        }

        if (!eventPlayer.scoreboardTags.contains("Bow_Overcharging")) {
            eventPlayer.scoreboardTags.add("Bow_Overcharging")
            eventPlayer.scoreboardTags.add("Bow_Overcharge_Modifier_0")
            val overchargeTask = OverchargeTask(eventPlayer, eventBow, enchantmentStrength)
            overchargeTask.runTaskTimer(Odyssey.instance, (20 * 2) + 10, 20 * 2)
        }
    }

    private fun overchargeEnchantmentShoot(eventShooter: LivingEntity, eventProjectile: Entity) {
        with(eventShooter.scoreboardTags) {
            for (x in 1..5) {
                if (contains("Bow_Overcharge_Modifier_$x") && contains("Bow_Overcharging")) {
                    remove("Bow_Overcharge_Modifier_$x")
                    remove("Bow_Overcharging")
                    add("Bow_Overcharge_Cooldown")
                    eventProjectile.velocity.multiply(1 + (x * 0.2))
                    eventProjectile.scoreboardTags.add(EntityTags.OVERCHARGE_MODIFIER + x)
                    break
                }
            }
        }
        eventProjectile.scoreboardTags.add("Overcharge_Arrow")
    }

    private fun overchargeEnchantmentHit(eventProjectile: Projectile): Double {
        for (x in 1..5) {
            if (eventProjectile.scoreboardTags.contains(EntityTags.OVERCHARGE_MODIFIER + x)) {
                return x * 3.0
            }
        }
        return 0.0
    }

    // ------------------------------- PERPETUAL_PROJECTILE ------------------------------------
    private fun perpetualProjectileEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.run {
            addScoreboardTag(EntityTags.PERPETUAL_ARROW)
            addScoreboardTag(EntityTags.PERPETUAL_MODIFIER + enchantmentStrength)
            setGravity(false)
            isPersistent = false
        }
    }

    // ------------------------------- RICOCHET ------------------------------------
    private fun ricochetEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        with(eventProjectile) {
            addScoreboardTag(EntityTags.RICOCHET_ARROW)
            addScoreboardTag(EntityTags.RICOCHET_BOUNCE + 0)
            addScoreboardTag(EntityTags.RICOCHET_MODIFIER + enchantmentStrength)
        }
    }

    // RICOCHET enchantment function regarding entity hit
    private fun ricochetEnchantmentEntityHit(eventProjectile: Projectile): Double {
        for (x in 1..5) {
            if (eventProjectile.scoreboardTags.contains(EntityTags.RICOCHET_BOUNCE + x)) {
                return x * 2.0
            }
        }
        return 0.0
    }


    // RICOCHET enchantment function regarding hit
    private fun ricochetEnchantmentBlockHit(eventProjectile: Entity, eventNormalVector: Vector) {
        var bounce = 0
        var modifier = 0
        with(eventProjectile.scoreboardTags) {
            for (x in 1..4) {
                if (contains(EntityTags.RICOCHET_BOUNCE + x)) { bounce = x }
                if (contains(EntityTags.RICOCHET_MODIFIER + x)) {
                    modifier = x
                    break
                }
            }
            if (bounce < modifier) {
                remove(EntityTags.RICOCHET_BOUNCE + bounce)
                add(EntityTags.RICOCHET_BOUNCE + (bounce + 1))
            }
            else {
                return
            }
        }
        // Spawn New Arrow
        eventProjectile.world.spawnEntity(eventProjectile.location.clone(), eventProjectile.type).also {
            if (it is Arrow) {
                it.basePotionData = it.basePotionData
                it.isPersistent = false
                it.fireTicks = eventProjectile.fireTicks
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            else if (it is ThrownPotion) {
                it.item = (eventProjectile as ThrownPotion).item
            }
            // Projectile
            if (it is Projectile) {
                it.scoreboardTags.addAll(eventProjectile.scoreboardTags)
                it.shooter = (eventProjectile as Projectile).shooter
                // Math
                val normal = eventNormalVector.clone().normalize()
                val input = eventProjectile.velocity.clone().normalize()
                it.velocity = input.subtract(normal.multiply(input.dot(normal) * 2.0)).normalize().multiply(max(eventProjectile.velocity.length() - 0.3, 0.0))
            }
        }
        eventProjectile.remove()
    }

    // ------------------------------- SHARPSHOOTER ------------------------------------
    private fun sharpshooterEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        with(eventProjectile) {
            if (velocity.length() >= 2.94) {
                addScoreboardTag(EntityTags.SHARPSHOOTER_ARROW)
                addScoreboardTag(EntityTags.SHARPSHOOTER_MODIFIER + enchantmentStrength)
            }
        }
    }

    private fun sharpshooterEnchantmentHit(eventProjectile: Entity): Double {
        for (x in 1..3) {
            if (eventProjectile.scoreboardTags.contains(EntityTags.SHARPSHOOTER_MODIFIER + x)) {
                return x + 1.0
            }
        }
        return 0.0
    }

    // ------------------------------- SOUL_REND ------------------------------------
    private fun soulRendEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        with(eventProjectile) {
            addScoreboardTag(EntityTags.SOUL_REND_ARROW)
            addScoreboardTag(EntityTags.SOUL_REND_MODIFIER + enchantmentStrength)
        }
    }

    private fun soulRendEnchantmentHit(eventShooter: LivingEntity, eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        val shooterRendID: String = EntityTags.SOUL_RENDED_BY + eventShooter.uniqueId
        if (shooterRendID !in eventHitEntity.scoreboardTags) {
            eventHitEntity.scoreboardTags.add(shooterRendID)
            for (x in 1..3) {
                if (eventProjectile.scoreboardTags.contains(EntityTags.SOUL_REND_MODIFIER + x)) {
                    eventHitEntity.scoreboardTags.add(EntityTags.SOUL_REND_MODIFIER + x)
                    break
                }
            }
            eventHitEntity.world.spawnParticle(Particle.SCULK_SOUL, eventHitEntity.location, 25, 0.15, 0.15, 0.15)
        }
    }

    private fun soulRendEnchantmentActivate(eventPlayer: Player): Boolean {
        var soulRendMultiplier = 0
        eventPlayer.location.getNearbyLivingEntities(32.0).forEach {
            if (it.scoreboardTags.contains(EntityTags.SOUL_RENDED_BY + eventPlayer.uniqueId)) {
                // Soul Rend Modifier Multiplier
                for (x in 1..3) {
                    if (it.scoreboardTags.contains(EntityTags.SOUL_REND_MODIFIER + x)) {
                        it.scoreboardTags.remove(EntityTags.SOUL_REND_MODIFIER + x)
                        soulRendMultiplier = x
                        break
                    }
                }
                // Damage
                val soulRendDamage = (it.arrowsInBody * soulRendMultiplier) + 2.0
                it.scoreboardTags.remove(EntityTags.SOUL_RENDED_BY + eventPlayer.uniqueId)
                it.damage(soulRendDamage, eventPlayer)
                it.world.spawnParticle(Particle.SOUL, it.location, 25, 0.05, 0.35, 0.05)
                it.world.spawnParticle(Particle.SCULK_SOUL, it.location, 25, 0.25, 0.35, 0.25)
            }
        }
        return soulRendMultiplier != 0
    }

}