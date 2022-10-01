package me.shadowalzazel.mcodyssey.listeners.enchantmentListeners

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
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
import java.util.*
import org.bukkit.util.Vector
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
        // Check if item has meta and exists
        if (event.bow?.hasItemMeta() == true) {
            //
            val someBow: ItemStack = event.bow!!
            val someShooter = event.entity
            val someProjectile = event.projectile
            //
            if (someProjectile.scoreboardTags.contains("Copied_Burst_Arrow")) {
                println("DETECTED COPIED ARROW!")
                return
            }
            // Loop for all enchants
            for (enchant in someBow.enchantments) {
                // When match
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
    }
    // Main function for enchantments relating to projectile damage
    @EventHandler
    fun mainProjectileDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (event.damager is Projectile) {
                val someProjectile = event.damager as Projectile
                if (someProjectile.shooter != null) {
                    val someShooter: LivingEntity = someProjectile.shooter as LivingEntity
                    if (event.entity is LivingEntity) {
                        val someHitEntity: LivingEntity = event.entity as LivingEntity
                        // make odyssey enchants work with each other
                        for (projectileTag in someProjectile.scoreboardTags) {
                            when (projectileTag) {
                                "Overcharge_Arrow" -> {
                                    event.damage += overchargeEnchantmentHit(someProjectile)
                                }
                                "Ricochet_Arrow" -> {
                                    event.damage += ricochetEnchantmentEntityHit(someProjectile)
                                }
                                "Sharpshooter_Arrow" -> {
                                    event.damage += sharpshooterEnchantmentHit(someProjectile)
                                }
                                "Soul_Rend_Arrow" -> {
                                    soulRendEnchantmentHit(someShooter, someProjectile, someHitEntity)
                                }
                            }
                        }
                    }
                }
            }
        }

    }



    // Main function for enchantments relating to projectile hits
    @EventHandler
    fun mainProjectileHitHandler(event: ProjectileHitEvent) {
        if (event.entity.shooter is LivingEntity) {
            val someShooter: LivingEntity = event.entity.shooter as LivingEntity
            val someProjectile: Projectile = event.entity
            if (event.hitEntity is LivingEntity) {
                val someHitEntity: LivingEntity = event.hitEntity as LivingEntity
                // make odyssey enchants work with each other
                for (projectileTag in someProjectile.scoreboardTags) {
                    when (projectileTag) {
                        "Bola_Shot_Arrow" -> {
                            bolaShotEnchantmentHit(someProjectile, someHitEntity)
                        }
                        "Chain_Reaction_Arrow" -> {
                            chainReactionEnchantmentHit(someProjectile, someHitEntity)
                        }
                        "Cluster_Shot_Arrow" -> {
                            clusterShotEnchantmentHit(someProjectile, someHitEntity)
                        }
                    }
                }
            }
            else if (event.hitBlock != null) {
                for (projectileTag in someProjectile.scoreboardTags) {
                    when (projectileTag) {
                        "Ricochet_Arrow" -> {
                            event.isCancelled = ricochetEnchantmentHit(someProjectile, event.hitBlockFace!!.direction)
                            break
                        }
                    }
                }

            }
            // For Ricochet hitting anything
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
                        println("Called: Main Bow Ready")
                        overchargeEnchantmentLoad(somePlayer, someBow, enchant.value)
                    }
                }
            }
        }
    }

    @EventHandler
    fun itemDropHandler(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.hasItemMeta()) {
            if (event.itemDrop.itemStack.enchantments.containsKey(OdysseyEnchantments.SOUL_REND)) {
                event.isCancelled = soulRendEnchantmentActivate(event.player)
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


    /*----------------------------------------------------------------------------------*/

    // ALCHEMY_ARTILLERY enchantment function regarding loading
    private fun alchemyArtilleryEnchantmentLoad(eventEntity: LivingEntity, eventCrossbow: ItemStack): Boolean {
        if (eventEntity.equipment!!.itemInOffHand.type == Material.SPLASH_POTION || eventEntity.equipment!!.itemInOffHand.type == Material.LINGERING_POTION) {
            val someOffHandPotion = eventEntity.equipment!!.itemInOffHand
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

    // ALCHEMY_ARTILLERY enchantment function regarding shooting
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

    // BOLA_SHOT enchantment function regarding shooting
    private fun bolaShotEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Bola_Shot_Arrow")
        eventProjectile.addScoreboardTag("Bola_Shot_Modifier_$enchantmentStrength")
    }

    // BOLA_SHOT enchantment function
    private fun bolaShotEnchantmentHit(eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        // Loop
        for (x in 1..3) {
            if (eventProjectile.scoreboardTags.contains("Bola_Shot_Modifier_$x")) {
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

    // BURST_BARRAGE enchantment function
    private fun burstBarrageEnchantmentShoot(eventProjectile: Entity, eventShooter: LivingEntity, enchantmentStrength: Int) {
        if (!eventShooter.scoreboardTags.contains("Burst_Shooting") && !eventProjectile.scoreboardTags.contains("Copied_Burst_Arrow"))  {
            eventShooter.addScoreboardTag("Burst_Shooting")
            val initialVelocity = eventProjectile.velocity.clone()
            val burstBarrageTask = BurstBarrageTask(eventShooter, enchantmentStrength, initialVelocity, eventProjectile) // Every 0.25 secs
            burstBarrageTask.runTaskTimer(MinecraftOdyssey.instance, 5, 5)
        }

    }

    // CHAIN_REACTION enchantment function regarding shooting
    private fun chainReactionEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Chain_Reaction_Arrow")
        eventProjectile.addScoreboardTag("Chain_Reaction_Modifier_$enchantmentStrength")
    }

    // CHAIN_REACTION enchantment function
    private fun chainReactionEnchantmentHit(eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        // Loop and check if modifier reached
        val closeEntities = eventHitEntity.location.getNearbyLivingEntities(10.0)
        for (x in 1..10) {
            if (eventProjectile.scoreboardTags.contains("Chain_Reaction_Modifier_$x")) { break }
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

    // CLUSTER_SHOT enchantment function regarding shooting
    private fun clusterShotEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Cluster_Shot_Arrow")
        eventProjectile.addScoreboardTag("Cluster_Shot_Modifier_$enchantmentStrength")
    }

    // CLUSTER_SHOT enchantment function
    private fun clusterShotEnchantmentHit(eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        // Loop
        for (x in 1..20) {
            if (eventProjectile.scoreboardTags.contains("Cluster_Shot_Modifier_${(x - 1) / 3}")) { break }
            // Math
            val someAngle = Math.random() * Math.PI * 2
            val coordinates: Pair<Double, Double> = Pair(cos(someAngle) * (5..12).random() * 0.1, sin(someAngle) * (5..12).random() * 0.1)
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
                    for (tag in eventProjectile.scoreboardTags) {
                        it.shooter = eventProjectile.shooter
                        it.scoreboardTags.add(tag)
                    }
                    it.velocity = someVelocity
                }
            }
        }
    }

    // GALE_WIND enchantment function
    private fun galeWindEnchantmentShoot(eventShooter: LivingEntity, enchantmentStrength: Int) {
        eventShooter.world.playSound(eventShooter.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 2.5F, 1.5F)
        val galeWindTask = GaleWindTask(eventShooter, enchantmentStrength)
        galeWindTask.runTaskLater(MinecraftOdyssey.instance, 6)
    }

    // LUCKY_DRAW enchantment function
    private fun luckyDrawEnchantmentShoot(enchantmentStrength: Int): Boolean {
        return (enchantmentStrength * 10) + 7 > (0..100).random()
    }

    // OVERCHARGE enchantment function
    private fun overchargeEnchantmentLoad(eventPlayer: Player, eventBow: ItemStack, enchantmentStrength: Int) {
        if (eventPlayer.scoreboardTags.contains("Bow_Overcharge_Cooldown")) {
            eventPlayer.scoreboardTags.remove("Bow_Overcharge_Cooldown")
            return
        }

        if (!eventPlayer.scoreboardTags.contains("Bow_Overcharging")) {
            eventPlayer.scoreboardTags.add("Bow_Overcharging")
            eventPlayer.scoreboardTags.add("Bow_Overcharge_Modifier_0")
            val overchargeTask = OverchargeTask(eventPlayer, eventBow, enchantmentStrength)
            overchargeTask.runTaskTimer(MinecraftOdyssey.instance, (20 * 2) + 10, 20 * 2)
        }
    }

    // OVERCHARGE enchantment function regarding shooting
    private fun overchargeEnchantmentShoot(eventShooter: LivingEntity, eventProjectile: Entity) {
        with(eventShooter.scoreboardTags) {
            for (x in 1..5) {
                if (contains("Bow_Overcharge_Modifier_$x") && contains("Bow_Overcharging")) {
                    remove("Bow_Overcharge_Modifier_$x")
                    remove("Bow_Overcharging")
                    add("Bow_Overcharge_Cooldown")
                    eventProjectile.velocity.multiply(1 + (x * 0.2))
                    eventProjectile.scoreboardTags.add("Arrow_Overcharge_Modifier_$x")
                    break
                }
            }
        }
        eventProjectile.scoreboardTags.add("Overcharge_Arrow")
    }

    // OVERCHARGE enchantment function regarding hit
    private fun overchargeEnchantmentHit(eventProjectile: Projectile): Double {
        for (x in 1..5) {
            if (eventProjectile.scoreboardTags.contains("Arrow_Overcharge_Modifier_$x")) {
                return x * 3.0
            }
        }
        return 0.0
    }

    // PERPETUAL_PROJECTILE enchantment function regarding shooting
    private fun perpetualProjectileEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.run {
            addScoreboardTag("Perpetual_Arrow")
            addScoreboardTag("Perpetual_Modifier_$enchantmentStrength")
            setGravity(false)
            isPersistent = false
        }
    }

    // RICOCHET enchantment function regarding shooting
    private fun ricochetEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Ricochet_Arrow")
        eventProjectile.addScoreboardTag("Ricochet_Bounced_0")
        eventProjectile.addScoreboardTag("Ricochet_Modifier_$enchantmentStrength")
    }

    // RICOCHET enchantment function regarding entity hit
    private fun ricochetEnchantmentEntityHit(eventProjectile: Projectile): Double {
        for (x in 1..5) {
            if (eventProjectile.scoreboardTags.contains("Ricochet_Bounced_$x")) {
                return x * 2.0
            }
        }
        return 0.0
    }


    // RICOCHET enchantment function regarding hit
    private fun ricochetEnchantmentHit(eventProjectile: Entity, eventNormalVector: Vector): Boolean {
        // Loop
        for (x in 1..4) {
            if (eventProjectile.scoreboardTags.contains("Ricochet_Modifier_$x")) {
                // Tags
                with(eventProjectile.scoreboardTags) {
                    remove("Ricochet_Modifier_$x")
                    for (z in 0..4) {
                        if (contains("Ricochet_Bounced_$z")) {
                            remove("Ricochet_Bounced_$z")
                            add("Ricochet_Bounced_${z + 1}")
                        }
                    }
                }
                // Spawn New Arrow
                eventProjectile.world.spawnEntity(eventProjectile.location.clone(), eventProjectile.type).also {
                    if (it is Arrow) {
                        it.basePotionData = it.basePotionData
                        it.isPersistent = false
                        it.fireTicks = eventProjectile.fireTicks
                    }
                    else if (it is ThrownPotion) {
                        it.item = (eventProjectile as ThrownPotion).item
                    }
                    // Projectile
                    if (it is Projectile) {
                        for (tag in eventProjectile.scoreboardTags) {
                            it.scoreboardTags.add(tag)
                        }
                        if (x != 1) { it.scoreboardTags.add("Ricochet_Modifier_${x - 1}") }
                        it.shooter = (eventProjectile as Projectile).shooter
                        // Math
                        val normal = eventNormalVector.clone().normalize()
                        val input = eventProjectile.velocity.clone().normalize()
                        it.velocity = input.subtract(normal.multiply(input.dot(normal) * 2.0)).normalize().multiply(max(eventProjectile.velocity.length() - 0.3, 0.0))
                    }
                }
                return true
            }
        }
        return false
    }

    // SHARPSHOOTER enchantment function regarding shooting
    private fun sharpshooterEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        if (eventProjectile.velocity.length() >= 2.94) {
            eventProjectile.addScoreboardTag("Sharpshooter_Arrow")
            eventProjectile.addScoreboardTag("Sharpshooter_Modifier_$enchantmentStrength")
        }
    }

    // SHARPSHOOTER enchantment function regarding hit
    private fun sharpshooterEnchantmentHit(eventProjectile: Entity): Double {
        // Loop
        println("Z")
        for (x in 1..3) {
            if (eventProjectile.scoreboardTags.contains("Sharpshooter_Modifier_$x")) {
                return x + 1.0
            }
        }
        return 0.0
    }

    // SOUL_REND enchantment function regarding shooting
    private fun soulRendEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Soul_Rend_Arrow")
        eventProjectile.addScoreboardTag("Soul_Rend_Modifier_$enchantmentStrength")
    }

    // SOUL_REND enchantment function
    private fun soulRendEnchantmentHit(eventShooter: LivingEntity, eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        // Add Rended
        if ("Rended_${eventShooter.uniqueId}" !in eventHitEntity.scoreboardTags) {
            eventHitEntity.scoreboardTags.add("Rended_${eventShooter.uniqueId}")
            for (x in 1..3) {
                if (eventProjectile.scoreboardTags.contains("Soul_Rend_Modifier_$x")) {
                    eventHitEntity.scoreboardTags.add("Soul_Rend_Modifier_$x")
                    break
                }
            }
            eventHitEntity.world.spawnParticle(Particle.SCULK_SOUL, eventHitEntity.location, 25, 0.15, 0.15, 0.15)
        }
    }

    // SOUL_REND enchantment activation
    private fun soulRendEnchantmentActivate(eventPlayer: Player): Boolean {
        var soulRendMultiplier = 0
        eventPlayer.location.getNearbyLivingEntities(32.0).forEach {
            if (it.scoreboardTags.contains("Rended_${eventPlayer.uniqueId}")) {
                // Ger soul rend multiplier
                for (x in 1..3) {
                    if (it.scoreboardTags.contains("Soul_Rend_Modifier_$x")) {
                        it.scoreboardTags.remove("Soul_Rend_Modifier_$x")
                        soulRendMultiplier = x
                        break
                    }
                }
                // Damage
                val soulRendDamage = (it.arrowsInBody * soulRendMultiplier) + soulRendMultiplier + 0.0
                it.scoreboardTags.remove("Rended_${eventPlayer.uniqueId}")
                it.damage(soulRendDamage, eventPlayer)
                it.world.spawnParticle(Particle.SOUL, it.location, 25, 0.05, 0.35, 0.05)
                it.world.spawnParticle(Particle.SCULK_SOUL, it.location, 25, 0.25, 0.35, 0.25)
            }
        }
        return soulRendMultiplier != 0
    }


}