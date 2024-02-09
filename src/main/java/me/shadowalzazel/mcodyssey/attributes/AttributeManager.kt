package me.shadowalzazel.mcodyssey.attributes

import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

interface AttributeManager {

    fun ItemStack.addGenericAttribute(

    ) {

    }

    fun ItemStack.addAttackDamageAttribute(
        damage: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ATTACK_DAMAGE_UUID,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        // TODO: FIX FOR ALL SLOTS
        // Add Attack Damage Attribute Modifier
        val damageModifier = AttributeModifier(id, name, damage, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier)
        }
    }

    fun ItemStack.addArmorAttribute(
        armor: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ARMOR_UUID,
        slot: EquipmentSlot = EquipmentSlot.CHEST)
    {
        val armorModifier = AttributeModifier(id, name, armor, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier)
        }
    }

    fun ItemStack.addAttackSpeedAttribute(
        speed: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ATTACK_SPEED_UUID,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        // TODO: FIX FOR ALL SLOTS
        // Add Attack Damage Attribute Modifier
        val damageModifier = AttributeModifier(id, name, speed, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, damageModifier)
        }
    }

    // Used when creating a custom weapon
    fun ItemStack.setNewAttackSpeedAttribute(
        speed: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ATTACK_SPEED_UUID,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        // Add A Base Speed
        val resetModifier = AttributeModifier(AttributeIDs.ITEM_ATTACK_SPEED_RESET_UUID, AttributeTags.ITEM_BASE_ATTACK_SPEED, -4.0,
            AttributeModifier.Operation.ADD_NUMBER, slot)
        val speedModifier = AttributeModifier(id, AttributeTags.ITEM_BASE_ATTACK_SPEED, speed,
            AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, resetModifier)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier)
        }
    }

    fun ItemStack.addMovementSpeedAttribute(
        speed: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_SPRINTING_SPEED,
        slot: EquipmentSlot = EquipmentSlot.FEET)
    {
        val armorModifier = AttributeModifier(id, name, speed, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, armorModifier)
        }
    }

    fun ItemStack.addMaxHealthAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.HEALTH_BOOST,
        slot: EquipmentSlot = EquipmentSlot.HEAD)
    {
        val newModifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, newModifier)
        }
    }

    fun ItemStack.addKnockbackResistanceAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.KNOCKBACK_RESISTANCE,
        slot: EquipmentSlot = EquipmentSlot.LEGS)
    {
        val newModifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, newModifier)
        }
    }

    fun ItemStack.addAttackKnockbackAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ATTACK_KNOCKBACK,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        val newModifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, newModifier)
        }
    }


}