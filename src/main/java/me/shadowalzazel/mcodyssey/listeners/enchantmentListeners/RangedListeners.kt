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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
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
                        galeWindEnchantmentShoot(someShooter, enchant.value)
                    }
                    OdysseyEnchantments.OVERCHARGE -> {
                        println("Called: Main Bow Shot")
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
                        "Bola_Shot_Arrow" -> {
                            bolaShotEnchantmentHit(someProjectile, someHitEntity)
                        }
                        "Chain_Reaction_Arrow" -> {
                            chainReactionEnchantmentHit(someProjectile, someHitEntity)
                        }
                        "Cluster_Shot_Arrow" -> {
                            clusterShotEnchantmentHit(someProjectile, someHitEntity)
                        }
                        "Soul_Rend_Arrow" -> {
                            soulRendEnchantmentHit(someShooter, someProjectile, someHitEntity)
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
            for (enchant in someBow.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.OVERCHARGE -> {
                        println("Called: Main Bow Ready")
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
                val chainVelocity = closeEntities.elementAt(x - 1).location.clone().subtract(eventProjectile.location.add(0.0, -0.25, 0.0)).toVector().normalize().multiply(2.0)
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
                }
                else if (it is ThrownPotion) {
                    it.item = (eventProjectile as ThrownPotion).item
                }
                // Projectile
                if (it is Projectile) {
                    for (tag in eventProjectile.scoreboardTags) {
                        it.scoreboardTags.add(tag)
                    }
                    it.velocity = someVelocity
                }
            }
        }
    }

    // GALE_WIND enchantment function
    private fun galeWindEnchantmentShoot(eventShooter: LivingEntity, enchantmentStrength: Int) {
        println(eventShooter.velocity)
        println(eventShooter.velocity.length())
        val someVelocity = eventShooter.velocity.clone().normalize().setY(0.0).multiply((enchantmentStrength * 0.6) + 1.3)
        eventShooter.velocity = someVelocity
        println(someVelocity)
        println(someVelocity.length())
        // TODO: PARTICLES
    }

    // LUCKY_DRAW enchantment function
    private fun luckyDrawEnchantmentShoot(enchantmentStrength: Int): Boolean {
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

    // PERPETUAL_PROJECTILE enchantment function regarding shooting
    private fun perpetualProjectileEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.run {
            addScoreboardTag("Perpetual_Arrow")
            addScoreboardTag("Perpetual_Modifier_$enchantmentStrength")
            setGravity(false)
            isPersistent = false
        }
        // TODO: Maybe if hit will keep on going
    }

    // RICOCHET enchantment function regarding shooting
    private fun ricochetEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        eventProjectile.addScoreboardTag("Ricochet_Arrow")
        eventProjectile.addScoreboardTag("Ricochet_Modifier_$enchantmentStrength")
    }

    // RICOCHET enchantment function regarding shooting
    private fun sharpshooterEnchantmentShoot(eventProjectile: Entity, enchantmentStrength: Int) {
        if (eventProjectile.velocity.length() >= 2.94) {
            eventProjectile.addScoreboardTag("Sharpshooter_Arrow")
            eventProjectile.addScoreboardTag("Sharpshooter_Modifier_$enchantmentStrength")
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