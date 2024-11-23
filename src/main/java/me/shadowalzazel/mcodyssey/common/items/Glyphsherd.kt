package me.shadowalzazel.mcodyssey.common.items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlotGroup

@Suppress("UnstableApiUsage")
class Glyphsherd(
    itemName: String,
    overrideMaterial: Material,
    customName: String,
    lore: List<Component>? = null,
    internal val attribute: Attribute? = null,
    internal val value: Double = 0.0,
    internal val slotGroup: EquipmentSlotGroup? = null
) : OdysseyItem(
    itemName = itemName,
    overrideMaterial = overrideMaterial,
    customName = customName,
    lore = lore)