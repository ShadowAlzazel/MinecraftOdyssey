package me.shadowalzazel.mcodyssey.attributes

import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.UUID

interface AttributeManager {

    fun ItemStack.addGenericAttribute(

    ) {

    }

    fun ItemStack.addAttackDamageAttribute(
        damage: Double, name: String,
        id: UUID=AttributeIDs.ITEM_ATTACK_DAMAGE_UUID,
        slot: EquipmentSlot=EquipmentSlot.HAND) {
        val damageModifier = AttributeModifier(id, name, damage,
            AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier)
        }
    }

    fun ItemStack.addAttackSpeedAttribute(
        speed: Double, name: String,
        id: UUID=AttributeIDs.ITEM_ATTACK_SPEED_UUID,
        slot: EquipmentSlot=EquipmentSlot.HAND) {
        val resetModifier = AttributeModifier(AttributeIDs.ITEM_ATTACK_SPEED_RESET_UUID, AttributeTags.ITEM_BASE_ATTACK_SPEED, -4.0,
            AttributeModifier.Operation.ADD_NUMBER, slot)
        val speedModifier = AttributeModifier(id, AttributeTags.ITEM_BASE_ATTACK_SPEED, speed,
            AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, resetModifier)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier)
        }
    }

    fun ItemStack.addArmorAttribute(
        armor: Double, name: String,
        id: UUID=AttributeIDs.ITEM_ARMOR_UUID,
        slot: EquipmentSlot) {
        val armorModifier = AttributeModifier(id, name, armor,
            AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier)
        }
    }

}