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
                        alchemyArtilleryEnchantmentShoot(event, someShooter, someBow)
                    }
                    OdysseyEnchantments.BURST_BARRAGE -> {
                        burstBarrageEnchantment(someProjectile, someBow, someShooter)
                    }
                    OdysseyEnchantments.CHAIN_REACTION -> {
                        chainReactionEnchantmentShoot(someProjectile, someBow)
                    }
                    OdysseyEnchantments.LUCKY_DRAW -> {
                        luckyDrawEnchantment(event, someBow)
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
                            chainReactionEnchantmentHit(someShooter, someProjectile, someHitEntity)
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
                        alchemyArtilleryEnchantmentLoad(event, someEntity, someCrossbow)
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

                    }
                }
            }
        }
    }



    /*----------------------------------------------------------------------------------*/

    // ALCHEMY_ARTILLERY enchantment function regarding loading
    private fun alchemyArtilleryEnchantmentLoad(event: EntityLoadCrossbowEvent, eventEntity: LivingEntity, eventCrossbow: ItemStack) {
        if (eventEntity.equipment!!.itemInOffHand.type == Material.SPLASH_POTION || eventEntity.equipment!!.itemInOffHand.type == Material.LINGERING_POTION) {
            val someOffHandPotion = eventEntity.equipment!!.itemInOffHand
            if ("Alchemy_Artillery_Loaded" !in eventEntity.scoreboardTags) {
                eventEntity.scoreboardTags.add("Alchemy_Artillery_Loaded")
                if (!entityAlchemyArtilleryAmmo.containsKey(eventEntity.uniqueId)) { entityAlchemyArtilleryAmmo[eventEntity.uniqueId] = someOffHandPotion } else { entityAlchemyArtilleryAmmo[eventEntity.uniqueId] = someOffHandPotion }
                val multiCounter = if (eventCrossbow.itemMeta.hasEnchant(Enchantment.MULTISHOT)) 3 else 1
                if (!entityAlchemyArtilleryCounter.containsKey(eventEntity.uniqueId)) { entityAlchemyArtilleryCounter[eventEntity.uniqueId] = multiCounter } else { entityAlchemyArtilleryCounter[eventEntity.uniqueId] = multiCounter }
                eventEntity.equipment!!.setItemInOffHand(ItemStack(Material.AIR, 1))
                println("Loaded")
            }
            else {
                event.isCancelled = true
                println("Full!")
            }
        }
    }

    // ALCHEMY_ARTILLERY enchantment function regarding shooting
    private fun alchemyArtilleryEnchantmentShoot(event: EntityShootBowEvent, eventShooter: LivingEntity, eventBow: ItemStack) {
        // Check enchantment Strength
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.ALCHEMY_ARTILLERY)
        //
        var removeTag = false
        if ("Alchemy_Artillery_Loaded" in eventShooter.scoreboardTags) {
            val someCount = entityAlchemyArtilleryCounter[eventShooter.uniqueId]
            if (someCount!! >= 1) {
                val loadedPotionItem: ItemStack = entityAlchemyArtilleryAmmo[eventShooter.uniqueId]!!
                val someThrownPotion: ThrownPotion = eventShooter.world.spawnEntity(event.projectile.location, EntityType.SPLASH_POTION) as ThrownPotion
                someThrownPotion.item = loadedPotionItem
                val newVelocity = event.projectile.velocity.clone()
                newVelocity.multiply((enchantmentStrength * 0.2) + 0.1)
                someThrownPotion.velocity = newVelocity
                someThrownPotion.shooter = eventShooter
                event.projectile.remove()
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
    private fun burstBarrageEnchantment(eventProjectile: Entity, eventBow: ItemStack, eventShooter: LivingEntity) {
        // Check enchantment Strength
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.BURST_BARRAGE)
        //
        if (!eventShooter.scoreboardTags.contains("Burst_Shooting"))  {
            eventShooter.addScoreboardTag("Burst_Shooting")
            val initialVelocity = eventProjectile.velocity.clone()
            val burstBarrageTask = BurstBarrageTask(eventShooter, enchantmentStrength, initialVelocity, eventProjectile) // Every 0.25 secs
            burstBarrageTask.runTaskTimer(MinecraftOdyssey.instance, 5, 5)
        }

    }

    // CHAIN_REACTION enchantment function regarding shooting
    private fun chainReactionEnchantmentShoot(eventProjectile: Entity, eventBow: ItemStack) {
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.CHAIN_REACTION)
        eventProjectile.addScoreboardTag("Chain_Reaction_Arrow")
        eventProjectile.addScoreboardTag("Chain_Reaction_Modifier_$enchantmentStrength")
    }

    // CHAIN_REACTION enchantment function
    private fun chainReactionEnchantmentHit(eventShooter: LivingEntity, eventProjectile: Projectile, eventHitEntity: LivingEntity) {
        val baseVelocity = eventProjectile.velocity.clone()
        val baseUnitVector = baseVelocity.clone().normalize()
        baseUnitVector.y = 0.0
        val baseLocation = eventProjectile.location.clone()
        var amount: Int = 0
        for (x in 1..5) {
            if (eventProjectile.scoreboardTags.contains("Chain_Reaction_Modifier_$x")) {
                amount = x
                break
            }
        }
        // remove tag after

        for (p in 1..amount) {
            val someRotation = (360.0 / amount) * p
            val newProjectile = eventProjectile.world.spawnEntity(baseLocation, eventProjectile.type)
            // copy item data later
            val newVelocity = baseVelocity.clone().multiply(baseVelocity.length() - 0.5).rotateAroundY(someRotation)
            newProjectile.velocity = newVelocity
        }

    }

    // CHAIN_REACTION enchantment function regarding shooting
    private fun ricochetEnchantmentShoot(eventProjectile: Entity, eventBow: ItemStack) {
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.CHAIN_REACTION)
        eventProjectile.addScoreboardTag("Ricochet_Arrow")
        eventProjectile.addScoreboardTag("Chain_Reaction_Modifier_$enchantmentStrength")
    }


    // LUCKY_DRAW enchantment function
    private fun luckyDrawEnchantment(event: EntityShootBowEvent, eventBow: ItemStack) {
        val luckyFactor = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.LUCKY_DRAW)
        if ((0..100).random() <= (luckyFactor * 10) + 5) { event.setConsumeItem(false) }
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