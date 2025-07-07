@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup

object PetListener : Listener, AttributeManager, DataTagManager {
    @EventHandler
    fun eatingPetFoods(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.WOLF) return
        if (event.player.inventory.itemInMainHand.type == Material.AIR) return
        if (event.rightClicked !is LivingEntity) return
        val mainHand = event.player.inventory.itemInMainHand
        val entity = event.rightClicked as LivingEntity
        // Make Permanent
        when (mainHand.getItemNameId()) {
            "dog_spinach" -> {
                entity.setAttributeModifier(
                    2.0,
                    "odyssey.item.dog_spinach.attack_damage",
                    Attribute.ATTACK_DAMAGE,
                    slotGroup = EquipmentSlotGroup.ANY
                )
            }
            "dog_sizzle_crisp" -> {
                entity.setAttributeModifier(
                    0.1,
                    "odyssey.item.dog_sizzle_crisp.scale",
                    Attribute.BURNING_TIME,
                    slotGroup = EquipmentSlotGroup.ANY
                )
            }
            "dog_milk_bone" -> {
                entity.setHealthAttribute(10.0)
                entity.setAttributeModifier(
                    0.8,
                    "odyssey.item.milk_bone.scale",
                    Attribute.SCALE,
                    slotGroup = EquipmentSlotGroup.ANY
                )
            }
        }
    }
}