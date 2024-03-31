package me.shadowalzazel.mcodyssey.rune_writing.base

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot

class OdysseyRunesherd(
    itemName: String,
    overrideMaterial: Material,
    customName: String,
    customModel: Int? = null,
    lore: List<Component>? = null,
    internal val attribute: Attribute? = null,
    internal val value: Double = 0.0,
    internal val affectedEquipment: List<EquipmentSlot>? = null,
    internal val odysseyAttribute: Int? = null
) : OdysseyItem(
    itemName = itemName,
    overrideMaterial = overrideMaterial,
    customName = customName,
    lore = lore,
    customModel = customModel)