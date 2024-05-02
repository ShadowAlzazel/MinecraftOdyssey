package me.shadowalzazel.mcodyssey.attributes

import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

internal interface AttributeManager {

     fun LivingEntity.addHealthAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_HEALTH_GENERIC,
        id: UUID = AttributeIDs.EXTRA_HEALTH_GENERIC_UUID)
    {
        val modifier = AttributeModifier(
            id,
            name,
            value,
            AttributeModifier.Operation.ADD_NUMBER
        )
        val attribute = getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        if (!attribute.modifiers.contains(modifier)) {
            attribute.addModifier(modifier)
        }

    }

    fun LivingEntity.addAttackAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_ATTACK_GENERIC,
        id: UUID = AttributeIDs.EXTRA_ATTACK_GENERIC_UUID)
    {
        val modifier = AttributeModifier(
            id,
            name,
            value,
            AttributeModifier.Operation.ADD_NUMBER
        )
        val attribute = getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) ?: return
        if (!attribute.modifiers.contains(modifier)) {
            attribute.addModifier(modifier)
        }
    }

    fun LivingEntity.addArmorAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_ARMOR_GENERIC,
        id: UUID = AttributeIDs.EXTRA_ARMOR_GENERIC_UUID)
    {
        val modifier = AttributeModifier(
            id,
            name,
            value,
            AttributeModifier.Operation.ADD_NUMBER
        )
        val attribute = getAttribute(Attribute.GENERIC_ARMOR) ?: return
        if (!attribute.modifiers.contains(modifier)) {
            attribute.addModifier(modifier)
        }
    }

    fun LivingEntity.addSpeedAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_SPEED_GENERIC,
        id: UUID = AttributeIDs.EXTRA_SPEED_GENERIC_UUID)
    {
        val modifier = AttributeModifier(
            id,
            name,
            value,
            AttributeModifier.Operation.ADD_NUMBER
        )
        val attribute = getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) ?: return
        if (!attribute.modifiers.contains(modifier)) {
            attribute.addModifier(modifier)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Items

    fun ItemStack.addGenericAttribute(
        value: Double,
        name: String,
        id: UUID,
        slot: EquipmentSlot,
        attribute: Attribute)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(attribute, modifier)
        }
    }

    fun ItemStack.addEntityRangeAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ENTITY_RANGE_UUID,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.PLAYER_ENTITY_INTERACTION_RANGE, modifier)
        }
    }

    fun ItemStack.addAttackDamageAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ATTACK_DAMAGE_UUID,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        // Add Attack Damage Attribute Modifier
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier)
        }
    }

    fun ItemStack.addArmorAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ARMOR_UUID,
        slot: EquipmentSlot = EquipmentSlot.CHEST)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier)
        }
    }

    fun ItemStack.addArmorToughnessAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ARMOR_TOUGHNESS_UUID,
        slot: EquipmentSlot = EquipmentSlot.CHEST)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier)
        }
    }

    fun ItemStack.addAttackSpeedAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_ATTACK_SPEED_UUID,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        // TODO: FIX FOR ALL SLOTS
        // Add Attack Damage Attribute Modifier
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier)
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
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ITEM_SPRINTING_SPEED,
        slot: EquipmentSlot = EquipmentSlot.FEET)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier)
        }
    }

    fun ItemStack.addMaxHealthAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.HEALTH_BOOST,
        slot: EquipmentSlot = EquipmentSlot.HEAD)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier)
        }
    }

    fun ItemStack.addKnockbackResistanceAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.KNOCKBACK_RESISTANCE,
        slot: EquipmentSlot = EquipmentSlot.LEGS)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier)
        }
    }

    fun ItemStack.addAttackKnockbackAttribute(
        value: Double,
        name: String,
        id: UUID = AttributeIDs.ATTACK_KNOCKBACK,
        slot: EquipmentSlot = EquipmentSlot.HAND)
    {
        val modifier = AttributeModifier(id, name, value, AttributeModifier.Operation.ADD_NUMBER, slot)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, modifier)
        }
    }


}