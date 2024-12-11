package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.ItemComponentsManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface AttributeManager : ItemComponentsManager, RegistryTagManager {

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
    // Item Components
    fun ItemStack.copyAttributes(input: ItemStack, union: Boolean=true) {
        // Copy from input
        if (!union) {
            transferComponent(this, input, DataComponentTypes.ATTRIBUTE_MODIFIERS)
            return
        }
        // If union, transfer old attributes not found in new input
        else {
            val transferredAttributes = input.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()?.toList() ?: return
            val existingAttributes = this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()?.toList()
            // Builder for attributes
            val builder = ItemAttributeModifiers.itemAttributes()
            transferredAttributes.forEach { builder.addModifier(it.attribute(), it.modifier()) }
            // Loop through existing attributes to get the key and match to the Data Item
            if (existingAttributes != null) {
                for (modifier in existingAttributes) {
                    // If modifier key not in modifiers -> add to builder
                    val modifierKey = modifier.modifier().key
                    if (modifierKey.namespace == "minecraft") continue // Ignore default modifiers
                    val hasKey = transferredAttributes.any { it.modifier().key == modifierKey }
                    if (!hasKey) builder.addModifier(modifier.attribute(), modifier.modifier())
                    //else if (override) builder.addModifier(modifier.attribute(), modifier.modifier())
                }
            }
            this.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, builder)
        }
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
            it.addAttributeModifier(Attribute.ATTACK_SPEED, speedModifier)
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