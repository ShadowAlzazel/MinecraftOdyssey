package me.shadowalzazel.mcodyssey.rune_writing.base

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot

class OdysseyRunesherd(
    name: String,
    material: Material,
    displayName: Component? = null,
    lore: List<Component>? = null,
    customModel: Int? = null,
    internal val attribute: Attribute? = null,
    internal val value: Double = 0.0,
    internal val affectedEquipment: List<EquipmentSlot>? = null,
    internal val odysseyAttribute: Int? = null
) : OdysseyItem(
    name = name,
    material = material,
    displayName = displayName,
    lore = lore,
    customModel = customModel,
    weaponMaterial = null,
    weaponType = null,
    enchantments = null) {

}