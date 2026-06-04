package me.shadowalzazel.mcodyssey.common.tasks

import me.shadowalzazel.mcodyssey.util.AttributeManager
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.scheduler.BukkitRunnable

class AttributeStatusEffect(
    val entity: LivingEntity,
    val attribute: Attribute,
    val name: String,
    private val slotGroup: EquipmentSlotGroup,
) : BukkitRunnable() , AttributeManager {

    var stopNow: Boolean = false
    var isRunning: Boolean = true
    val key: NamespacedKey = NamespacedKey("odyssey",name)


    // Every 0.2 Seconds, Runs to check if to apply this Attribute Or remove
    override fun run() {
        // Stop
        if (this.stopNow) {
            entity.removeAttributeModifier(key, attribute)
        }


    }




}