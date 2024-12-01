package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface AttributeManager {

    fun LivingEntity.setAttributeModifier(
        value: Double,
        name: String,
        attribute: Attribute,
        id: NamespacedKey? = null,
        slotGroup: EquipmentSlotGroup? = null)
    {
        val nameKey = id ?: NamespacedKey(Odyssey.instance, name)
        val slots = slotGroup ?: EquipmentSlotGroup.ANY
        val modifier = AttributeModifier(nameKey, value, AttributeModifier.Operation.ADD_NUMBER, slots)
        val mobAttribute = this.getAttribute(attribute) ?: return
        if (!mobAttribute.modifiers.contains(modifier)) {
            mobAttribute.addModifier(modifier)
        }
    }

    fun LivingEntity.addScaleAttribute(
        value: Double,
        name: String = AttributeTags.MOB_SCALE)
    {
        this.setAttributeModifier(value, name, Attribute.SCALE)
    }

     fun LivingEntity.addHealthAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_HEALTH_GENERIC)
    {
        this.setAttributeModifier(value, name, Attribute.MAX_HEALTH)
    }

    fun LivingEntity.addAttackAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_ATTACK_GENERIC)
    {
        this.setAttributeModifier(value, name, Attribute.ATTACK_DAMAGE)
    }

    fun LivingEntity.addArmorAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_ARMOR_GENERIC)
    {
        this.setAttributeModifier(value, name, Attribute.ARMOR)
    }

    fun LivingEntity.addSpeedAttribute(
        value: Double,
        name: String = AttributeTags.EXTRA_SPEED_GENERIC)
    {
        this.setAttributeModifier(value, name, Attribute.MOVEMENT_SPEED)
    }

    fun LivingEntity.addStepAttribute(
        value: Double,
        name: String = "generic.extra_step_height")
    {
        this.setAttributeModifier(value, name, Attribute.STEP_HEIGHT)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Items

    fun ItemStack.setGenericAttribute(
        value: Double,
        name: String,
        attribute: Attribute,
        id: NamespacedKey? = null,
        slotGroup: EquipmentSlotGroup? = null)
    {
        val nameKey = id ?: NamespacedKey(Odyssey.instance, name)
        val slots = slotGroup ?: EquipmentSlotGroup.ANY
        val newModifier = AttributeModifier(nameKey, value, AttributeModifier.Operation.ADD_NUMBER, slots)
        itemMeta = itemMeta.also { meta ->
            // Check if already has named modifier to remove
            val attributeModifiers = meta.getAttributeModifiers(attribute)
            if (attributeModifiers != null) {
                if (attributeModifiers.contains(newModifier)) {
                    meta.removeAttributeModifier(attribute, newModifier)
                }
            }
            meta.addAttributeModifier(attribute, newModifier)
        }
    }

    // Used when creating a custom weapon
    fun ItemStack.setNewAttackSpeedAttribute(
        speed: Double)
    {
        val resetKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_RESET_ATTACK_SPEED)
        val speedKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_SPEED)
        val slots = EquipmentSlotGroup.MAINHAND
        // Add A Base Speed
        val resetModifier = AttributeModifier(resetKey, -4.0, AttributeModifier.Operation.ADD_NUMBER, slots)
        val speedModifier = AttributeModifier(speedKey, speed, AttributeModifier.Operation.ADD_NUMBER, slots)
        itemMeta = itemMeta.also {
            it.addAttributeModifier(Attribute.ATTACK_SPEED, resetModifier)
            it.addAttributeModifier(Attribute.ATTACK_DAMAGE, speedModifier)
        }
    }

    fun ItemStack.addAttackDamageAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.MAINHAND)
    {
        this.setGenericAttribute(value, name, Attribute.ATTACK_DAMAGE, slotGroup = slots)
    }

    fun ItemStack.addAttackSpeedAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.MAINHAND)
    {
        this.setGenericAttribute(value, name, Attribute.ATTACK_SPEED, slotGroup = slots)
    }


    fun ItemStack.addEntityRangeAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.MAINHAND)
    {
        this.setGenericAttribute(value, name, Attribute.ENTITY_INTERACTION_RANGE, slotGroup = slots)
    }

    fun ItemStack.addArmorAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.MAINHAND)
    {
        this.setGenericAttribute(value, name, Attribute.ARMOR, slotGroup = slots)
    }

    fun ItemStack.addArmorToughnessAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.MAINHAND)
    {
        this.setGenericAttribute(value, name, Attribute.ARMOR_TOUGHNESS, slotGroup = slots)
    }

    fun ItemStack.addMovementSpeedAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.FEET)
    {
        this.setGenericAttribute(value, name, Attribute.MOVEMENT_SPEED, slotGroup = slots)
    }

    fun ItemStack.addMaxHealthAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.ARMOR)
    {
        this.setGenericAttribute(value, name, Attribute.MAX_HEALTH, slotGroup = slots)
    }

    fun ItemStack.addKnockbackResistanceAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.ARMOR)
    {
        this.setGenericAttribute(value, name, Attribute.KNOCKBACK_RESISTANCE, slotGroup = slots)
    }

    fun ItemStack.addAttackKnockbackAttribute(
        value: Double,
        name: String,
        slots: EquipmentSlotGroup = EquipmentSlotGroup.MAINHAND)
    {
        this.setGenericAttribute(value, name, Attribute.ATTACK_KNOCKBACK, slotGroup = slots)
    }

}