package me.shadowalzazel.mcodyssey.common.effects

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

/** A single attribute change. `amount` is a function of the effect level/amplifier. */
data class AttributeChange(
    val attribute: Attribute,
    val operation: AttributeModifier.Operation,
    val amount: (level: Double) -> Double
)
