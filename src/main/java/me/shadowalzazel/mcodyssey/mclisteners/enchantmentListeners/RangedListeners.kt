package me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack


object RangedListeners : Listener {

    // ALCHEMY_ARTILLERY enchantment effects
    @EventHandler
    fun alchemyArtilleryEnchantment(event: EntityShootBowEvent) {
        val someEntity = event.entity
        if (someEntity.equipment!!.itemInMainHand.type == Material.CROSSBOW) {
            val someCrossbow = someEntity.equipment!!.itemInMainHand
            if (someCrossbow.hasItemMeta()) {
                if (someCrossbow.itemMeta.hasEnchant(OdysseyEnchantments.ALCHEMY_ARTILLERY)) {
                    if (someEntity.equipment!!.itemInOffHand.type == Material.SPLASH_POTION || someEntity.equipment!!.itemInOffHand.type == Material.LINGERING_POTION) {
                        //val offhandPotionItem: PotionMeta = someEntity.equipment!!.itemInOffHand.itemMeta as PotionMeta
                        val someThrownPotion: ThrownPotion = someEntity.world.spawnEntity(event.projectile.location, EntityType.SPLASH_POTION) as ThrownPotion
                        //someThrownPotion.potionMeta = offhandPotionMeta
                        someThrownPotion.item = someEntity.equipment!!.itemInOffHand
                        val newVelocity = event.projectile.velocity.clone()
                        newVelocity.multiply((someCrossbow.itemMeta.getEnchantLevel(OdysseyEnchantments.ALCHEMY_ARTILLERY) * 0.2) + 0.1)
                        someThrownPotion.velocity = newVelocity
                        someThrownPotion.shooter = someEntity
                        event.projectile.remove()
                        //bounce
                        someEntity.equipment!!.setItemInOffHand(ItemStack(Material.AIR, 1))

                        println("X")
                    }
                }
            }
        }
    }


    // LUCKY_DRAW enchantment effects
    @EventHandler
    fun luckyDrawEnchantment(event: EntityShootBowEvent) {
        val someEntity = event.entity
        if (someEntity.equipment!!.itemInMainHand.type == Material.BOW) {
            val someBow = someEntity.equipment!!.itemInMainHand
            if (someBow.hasItemMeta()) {
                if (someBow.itemMeta.hasEnchant(OdysseyEnchantments.LUCKY_DRAW)) {
                    val luckyFactor = someBow.itemMeta.getEnchantLevel(OdysseyEnchantments.LUCKY_DRAW)
                    if ((0..100).random() <= luckyFactor * 15) event.setConsumeItem(false)
                    println("L")

                }
            }
        }
    }


    // REND enchantment effects
    @EventHandler
    fun rendEnchantment(event: ProjectileHitEvent) {
        if (event.hitEntity != null) {
            if (event.hitEntity is LivingEntity) {
                val rendedEntity = event.hitEntity
                if (event.entity.shooter is LivingEntity) {
                    val someEntity = event.entity.shooter as LivingEntity
                    if (someEntity.equipment!!.itemInMainHand.type == Material.BOW || someEntity.equipment!!.itemInMainHand.type == Material.CROSSBOW) {
                        val someWeapon = someEntity.equipment!!.itemInMainHand
                        if (someWeapon.hasItemMeta()) {
                            if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.REND)) {
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
    @EventHandler
    fun rendEnchantmentActivation(event: PlayerSwapHandItemsEvent) {
        val somePlayer = event.player
        if (event.offHandItem!!.type == Material.BOW || event.offHandItem!!.type == Material.CROSSBOW) {
            val someWeapon = event.offHandItem!!
            if (someWeapon.hasItemMeta()) {
                if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.REND)) {
                    val nearbyEnemies = somePlayer.world.getNearbyLivingEntities(somePlayer.location, 25.0)
                    println(System.nanoTime())
                    val rendLevel = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.REND)

                    for (someEntity in nearbyEnemies) {
                        if ("Rended_${somePlayer.name}" in someEntity.scoreboardTags) {
                            val rendStacks = someEntity.arrowsInBody
                            someEntity.damage((rendStacks * rendLevel * 1) + 1.0)
                            someEntity.scoreboardTags.remove("Rended_${somePlayer.name}")
                            //Effects
                        }
                    }
                    println(System.nanoTime())
                }
            }
        }
    }






}