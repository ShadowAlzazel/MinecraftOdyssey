package me.shadowalzazel.mcodyssey.listeners.enchantmentListeners

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import java.util.*


object RangedListeners : Listener {

    private var entityAlchemyArtilleryAmmo = mutableMapOf<UUID, ItemStack?>()
    private var entityAlchemyArtilleryCounter = mutableMapOf<UUID, Int>()


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
                    OdysseyEnchantments.LUCKY_DRAW -> {
                        luckyDrawEnchantment(event, someBow)
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
                for (projectileTag in event.entity.scoreboardTags) {
                    when (projectileTag) {
                        "Soul_Rend_Arrow" -> {
                            soulRendEnchantmentHit(someShooter, someProjectile, someHitEntity)
                        }
                    }
                }
            }
            // For Ricochet hitting anything
        }
    }


    /*----------------------------------------------------------------------------------*/

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

    // ALCHEMY_ARTILLERY enchantment effects
    @EventHandler
    fun alchemyArtilleryEnchantmentLoad(event: EntityLoadCrossbowEvent) {
        val someEntity = event.entity
        if (someEntity.equipment!!.itemInMainHand.type == Material.CROSSBOW) {
            val someCrossbow = someEntity.equipment!!.itemInMainHand
            if (someCrossbow.hasItemMeta()) {
                if (someCrossbow.itemMeta.hasEnchant(OdysseyEnchantments.ALCHEMY_ARTILLERY)) {
                    if (someEntity.equipment!!.itemInOffHand.type == Material.SPLASH_POTION || someEntity.equipment!!.itemInOffHand.type == Material.LINGERING_POTION) {
                        val someOffHandPotion = someEntity.equipment!!.itemInOffHand
                        if ("Alchemy_Artillery_Loaded" !in someEntity.scoreboardTags) {
                            someEntity.scoreboardTags.add("Alchemy_Artillery_Loaded")
                            if (!entityAlchemyArtilleryAmmo.containsKey(someEntity.uniqueId)) entityAlchemyArtilleryAmmo[someEntity.uniqueId] = someOffHandPotion else entityAlchemyArtilleryAmmo[someEntity.uniqueId] = someOffHandPotion
                            val multiCounter = if (someCrossbow.itemMeta.hasEnchant(Enchantment.MULTISHOT)) 3 else 1
                            if (!entityAlchemyArtilleryCounter.containsKey(someEntity.uniqueId)) entityAlchemyArtilleryCounter[someEntity.uniqueId] = multiCounter else entityAlchemyArtilleryCounter[someEntity.uniqueId] = multiCounter
                            someEntity.equipment!!.setItemInOffHand(ItemStack(Material.AIR, 1))
                            println("Loaded")
                        }
                        else {
                            event.isCancelled = true
                            println("Full!")
                        }
                    }
                }
            }
        }
    }


    // LUCKY_DRAW enchantment function
    private fun luckyDrawEnchantment(event: EntityShootBowEvent, eventBow: ItemStack) {
        val luckyFactor = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.LUCKY_DRAW)
        if ((0..100).random() <= (luckyFactor * 10) + 5) { event.setConsumeItem(false) }
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

    // SOUL_REND enchantment function regarding shooting
    private fun soulRendEnchantmentShoot(eventProjectile: Entity, eventBow: ItemStack) {
        val enchantmentStrength = eventBow.itemMeta.getEnchantLevel(OdysseyEnchantments.SOUL_REND)
        eventProjectile.addScoreboardTag("Soul_Rend_Arrow")
        eventProjectile.addScoreboardTag("Soul_Rend_Modifier_$enchantmentStrength")
    }


    // REND enchantment effects

    fun rendEnchantment(event: ProjectileHitEvent) {
        if (event.hitEntity != null) {
            if (event.hitEntity is LivingEntity) {
                val rendedEntity = event.hitEntity
                if (event.entity.shooter is LivingEntity) {
                    val someEntity = event.entity.shooter as LivingEntity
                    if (someEntity.equipment!!.itemInMainHand.type == Material.BOW || someEntity.equipment!!.itemInMainHand.type == Material.CROSSBOW) {
                        val someWeapon = someEntity.equipment!!.itemInMainHand
                        if (someWeapon.hasItemMeta()) {
                            if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.SOUL_REND)) {
                                if ("Rended_${someEntity.name}" !in rendedEntity!!.scoreboardTags) {
                                    rendedEntity.scoreboardTags.add("Rended_${someEntity.name}")
                                    //Effects
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // REND enchantment effects

    fun rendEnchantmentActivation(event: PlayerSwapHandItemsEvent) {
        val somePlayer = event.player
        if (event.offHandItem!!.type == Material.BOW || event.offHandItem!!.type == Material.CROSSBOW) {
            val someWeapon = event.offHandItem!!
            if (someWeapon.hasItemMeta()) {
                if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.SOUL_REND)) {
                    val nearbyEnemies = somePlayer.world.getNearbyLivingEntities(somePlayer.location, 25.0)
                    //println(System.nanoTime())
                    val rendLevel = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.SOUL_REND)

                    for (someEntity in nearbyEnemies) {
                        if ("Rended_${somePlayer.name}" in someEntity.scoreboardTags) {
                            val rendStacks = someEntity.arrowsInBody
                            someEntity.damage((rendStacks * rendLevel * 1) + 1.0)
                            someEntity.world.spawnParticle(Particle.SOUL, someEntity.location, 25, 0.05, 0.35, 0.05)
                            someEntity.world.spawnParticle(Particle.SCULK_SOUL, someEntity.location, 25, 0.25, 0.35, 0.25)
                            println(rendStacks)
                            someEntity.scoreboardTags.remove("Rended_${somePlayer.name}")
                            //Effects
                        }
                    }
                    //println(System.nanoTime())
                }
            }
        }
    }






}