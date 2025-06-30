package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.util.AttributeManager
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

// SPEEDY_SPURS TASK
@Suppress("UnstableApiUsage")
class SpeedySpursTask(
    private val player: LivingEntity,
    private val mount: LivingEntity,
    private val modifier: Int) : BukkitRunnable(), AttributeManager
{

    private val FLYING_SPEED_ID = "odyssey.enchantment.speedy_spurs.flight_speed"
    private val MOVEMENT_SPEED_ID = "odyssey.enchantment.speedy_spurs.movement_speed"

    override fun run() {
        // Checks if mount is player to add effects
        if (player !in mount.passengers) {
            removeFlyingSpeed()
            removeMovementSpeed()
            this.cancel()
            return
        }
        //val speedEffect = PotionEffect(PotionEffectType.SPEED, 7 * 20 , modifier - 1)
        //mount.addPotionEffect(speedEffect)
        val baseSpeed = mount.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue ?: 0.1125
        addMovementSpeed(baseSpeed * 0.25) // Multiply base speed by intervals of 25%
        // Add flying speed
        addFlyingSpeed()
    }

    private fun addMovementSpeed(baseValue: Double) {
        mount.setAttributeModifier(
            baseValue * modifier,
            MOVEMENT_SPEED_ID,
            Attribute.MOVEMENT_SPEED,
            slotGroup = EquipmentSlotGroup.ANY
        )
    }

    private fun removeMovementSpeed() {
        mount.removeAttributeModifier(
            MOVEMENT_SPEED_ID,
            Attribute.MOVEMENT_SPEED
        )
    }


    private fun addFlyingSpeed() {
        mount.setAttributeModifier(
            0.02 * modifier,
            FLYING_SPEED_ID,
            Attribute.FLYING_SPEED,
            slotGroup = EquipmentSlotGroup.ANY
        )
    }


    private fun removeFlyingSpeed() {
        mount.removeAttributeModifier(
            FLYING_SPEED_ID,
            Attribute.FLYING_SPEED
        )
    }


}