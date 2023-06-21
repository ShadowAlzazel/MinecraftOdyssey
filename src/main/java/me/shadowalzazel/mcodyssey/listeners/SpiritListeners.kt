package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

object SpiritListeners {

    // TEMP
    fun OdysseyItem.createTemp(
        statAttribute: Attribute,
        statIdentifier: UUID,
        statAmount: Double = 0.0,
        statSlot: EquipmentSlot = EquipmentSlot.OFF_HAND,
        statOperation: AttributeModifier.Operation = AttributeModifier.Operation.ADD_NUMBER
    ): ItemStack {
        val attributeName = "odyssey.${name}_spirit"
        val newTotem = this.createItemStack(1)
        newTotem.itemMeta = newTotem.itemMeta.also {
            val spiritModifier = AttributeModifier(statIdentifier, attributeName, statAmount, statOperation, statSlot)
            it.addAttributeModifier(statAttribute, spiritModifier)
        }
        return newTotem
    }

}