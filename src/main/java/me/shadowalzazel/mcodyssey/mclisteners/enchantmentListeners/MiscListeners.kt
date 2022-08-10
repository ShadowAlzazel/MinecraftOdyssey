package me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.EquipmentSlot

object MiscListeners : Listener {

    // MIRROR_FORCE enchantment effects
    @EventHandler
    fun mirrorForceEnchantment(event: ProjectileHitEvent) {
        if (event.hitEntity != null) {
            if (event.hitEntity is LivingEntity) {
                val blockingEntity = event.hitEntity as LivingEntity
                if (blockingEntity.equipment != null ) {
                    if (blockingEntity.equipment!!.itemInOffHand.hasItemMeta() ) {
                        val someShield = blockingEntity.equipment!!.itemInOffHand
                        if (someShield.itemMeta.hasEnchant(OdysseyEnchantments.MIRROR_FORCE)) {
                            if (blockingEntity.isHandRaised) {
                                if (blockingEntity.handRaised == EquipmentSlot.OFF_HAND) {
                                    println("Blocked!")
                                    //cooldown is part of the factor

                                    //event.entity.velocity = event.entity.location.subtract(blockingEntity.location).toVector()
                                    event.entity.velocity.multiply(-2.0)
                                    //direction


                                }
                            }
                        }
                    }
                }
            }
        }
    }

}