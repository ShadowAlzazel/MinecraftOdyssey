package me.shadowalzazel.mcodyssey.listeners.enchantmentListeners

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.tasks.BurstBarrageTask
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


object RangedListeners : Listener {

    private var entityAlchemyArtilleryAmmo = mutableMapOf<UUID, ItemStack?>()
    private var entityAlchemyArtilleryCounter = mutableMapOf<UUID, Int>()
    //
    private var overchargeRampingTime = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to shooting bows
    @EventHandler
    fun mainBowShotHandler(event: EntityShootBowEvent) {
        // Check if item has meta and exists
        if (event.bow?.hasItemMeta() == true) {
            val someBow: ItemStack = event.bow!!
            val someShooter = event.entity
            val someProjectile = event.projectile
            // Loop for all enchants
            for (enchant in someBow.enchantments) {
                // When match
                when (enchant.key) {
                    OdysseyEnchantments.ALCHEMY_ARTILLERY -> {
                        alchemyArtilleryEnchantmentShoot(someProjectile, someShooter, enchant.value)
                    }
                    OdysseyEnchantments.BOLA_SHOT -> {

                    }
                    OdysseyEnchantments.BURST_BARRAGE -> {
                        burstBarrageEnchantment(someProjectile, someShooter, enchant.value)
                    }
                    OdysseyEnchantments.CHAIN_REACTION -> {
                        chainReactionEnchantmentShoot(someProjectile, enchant.value)
                    }
                    OdysseyEnchantments.LUCKY_DRAW -> {
                        event.setConsumeItem(!luckyDrawEnchantment(enchant.value))
                    }
                    OdysseyEnchantments.OVERCHARGE -> {

                    }
                    OdysseyEnchantments.SOUL_REND -> {
                        soulRendEnchantmentShoot(someProjectile, someBow)
                    }
                }
            }
        }
    }

    // Main function for enchantments relating to projectile hits
    @EventHandler
    fun mainProjectileHitHandler(event: ProjectileHitEvent) {
        if (event.entity.shooter is LivingEntity) {
            if (event.hitEntity is LivingEntity) {
                val someShooter: LivingEntity = event.entity.shooter as LivingEntity
                val someProjectile: Projectile = event.entity
                val someHitEntity: LivingEntity = event.hitEntity as LivingEntity
                // make odyssey enchants work with each other
                for (projectileTag in event.entity.scoreboardTags) {
                    when (projectileTag) {
                        "Soul_Rend_Arrow" -> {
                            soulRendEnchantmentHit(someShooter, someProjectile, someHitEntity)
                        }
                        "Chain_Reaction_Arrow" -> {
                            chainReactionEnchantmentHit(someProjectile, someHitEntity)
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
        if (event.bow.hasItemMeta()) {
            val someBow = event.bow
            for (enchant in someBow.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.OVERCHARGE -> {
                        println(event.bow)
                    }
                }
            }
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
    private fun alchemyArtilleryEnchantmentShoot(eventProjectile: Entity, eventShooter: LivingEntity, enchantmentStrength: Int) {
        var removeTag = false
        if ("Alchemy_Artillery_Loaded" in eventShooter.scoreboardTags) {
            val someCount = entityAlchemyArtilleryCounter[eventShooter.uniqueId]
            if (someCount!! >= 1) {
                // Spawn potion with item
                (eventShooter.world.spawnEntity(eventProjectile.location, EntityType.SPLASH_POTION) as ThrownPotion).also {
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

    }

    // BURST_BARRAGE enchantment function
    private fun burstBarrageEnchantment(eventProjectile: Entity, eventShooter: LivingEntity, enchantmentStrength: Int) {
        if (!eventShooter.scoreboardTags.contains("Burst_Shooting"))  {
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
                val chainVelocity = closeEntities.elementAt(x - 1).location.clone().subtract(eventProjectile.location.add(0.0, -0.25, 0.0)).toVector().normalize().multiply(2.0)
                eventProjectile.world.spawnEntity(eventProjectile.location, eventProjectile.type).also {
                    it.velocity = chainVelocity
                }
            }
            // TODO: Maybe shoot back!?

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
            if (eventProjectile.scoreboardTags.contains("Cluster_Shot_Modifier_${x / 3}")) { break }
            // Math
            val someAngle = Math.random() * Math.PI * 2
            val coordinates: Pair<Double, Double> = Pair(cos(someAngle) * 2.0, sin(someAngle) * 2.0)

            val someVelocity = eventProjectile.location.clone().add(coordinates.first, 10.0, coordinates.second).subtract(eventProjectile.location).toVector()
            eventProjectile.world.spawnEntity(eventProjectile.location, eventProjectile.type).also {
                it.velocity = someVelocity
            }

        }

        // TODO: Random Circle


    }

    // RICOCHET enchantment function regarding shooting
    private fun ricochetEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Ricochet_Arrow")
        eventProjectile.addScoreboardTag("Ricochet_Modifier_$enchantmentStrength")
    }


    // LUCKY_DRAW enchantment function
    private fun luckyDrawEnchantment(enchantmentStrength: Int): Boolean {
        return (enchantmentStrength * 10) + 7 > (0..100).random()
    }

    // OVERCHARGE enchantment function
    private fun overchargeEnchantmentLoad(event: PlayerReadyArrowEvent, eventBow: ItemStack, eventPlayer: Player) {
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.OVERCHARGE)
        //
        if (!overchargeRampingTime.containsKey(eventPlayer.uniqueId)) { overchargeRampingTime[eventPlayer.uniqueId] = 0L }
        val timeElapsed = System.currentTimeMillis() - overchargeRampingTime[eventPlayer.uniqueId]!!

        if (!eventPlayer.scoreboardTags.contains("Overcharge_1") && timeElapsed > 1.5) {

        }
        else {

        }

    }



    // SOUL_REND enchantment function regarding shooting
    private fun soulRendEnchantmentShoot(eventProjectile: Entity, eventBow: ItemStack) {
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.SOUL_REND)
        eventProjectile.addScoreboardTag("Soul_Rend_Arrow")
        eventProjectile.addScoreboardTag("Soul_Rend_Modifier_$enchantmentStrength")
    }

    // SOUL_REND enchantment function
    private fun soulRendEnchantmentHit(eventShooter: LivingEntity, eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        // Add Rended
        if ("Rended_${eventShooter.uniqueId}" !in eventHitEntity.scoreboardTags) {
            eventHitEntity.scoreboardTags.add("Rended_${eventShooter.uniqueId}")
            eventHitEntity.world.spawnParticle(Particle.SCULK_SOUL, eventHitEntity.location, 25, 0.15, 0.15, 0.15)
        }
        else {
            // Rend Damage
            var soulRendMultiplier: Int = 0
            for (x in 1..3) {
                if (eventProjectile.scoreboardTags.contains("Soul_Rend_Modifier_$x")) {
                    soulRendMultiplier = x
                    break
                }
            }
            // TODO: Fix
            if (eventShooter.equipment?.itemInMainHand?.containsEnchantment(OdysseyEnchantments.SOUL_REND) == false) {
                val soulRendDamage = (eventHitEntity.arrowsInBody * soulRendMultiplier) + soulRendMultiplier + 0.0
                eventHitEntity.scoreboardTags.remove("Rended_${eventShooter.uniqueId}")
                eventHitEntity.damage(soulRendDamage, eventShooter)
                eventHitEntity.world.spawnParticle(Particle.SOUL, eventHitEntity.location, 25, 0.05, 0.35, 0.05)
                eventHitEntity.world.spawnParticle(Particle.SCULK_SOUL, eventHitEntity.location, 25, 0.25, 0.35, 0.25)
            }
        }

    }


}