package me.shadowalzazel.mcodyssey.spiritweaving.base

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import java.util.*

data class SpiritAttributeContainer(
    val statAttribute: Attribute,
    val statIdentifier: UUID,
    val statAmount: Double = 0.0,
    val statSlot: EquipmentSlot = EquipmentSlot.OFF_HAND,
    val statOperation: AttributeModifier.Operation = AttributeModifier.Operation.ADD_NUMBER
)



