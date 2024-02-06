package me.shadowalzazel.mcodyssey.rune_writing

import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.items.Runesherds
import me.shadowalzazel.mcodyssey.rune_writing.base.OdysseyRunesherd
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack

interface RunesherdManager : AttributeManager {

    /* ---------------------------------------- */
    // TAGS

    fun ItemStack.addRunesherdTag() {
        addTag(ItemTags.IS_RUNESHERD)
    }

    fun ItemStack.hasRunesherdTag(): Boolean {
        return hasTag(ItemTags.IS_RUNESHERD)
    }

    fun ItemStack.addRuneAugmentTag() {
        addTag(ItemTags.HAS_RUNE_AUGMENT)
    }

    fun ItemStack.hasRuneAugmentTag(): Boolean {
        return hasTag(ItemTags.HAS_RUNE_AUGMENT)
    }

    fun ItemStack.addRunewareTag() {
        addTag(ItemTags.IS_RUNEWARE)
    }

    fun ItemStack.hasRunewareTag(): Boolean {
        return hasTag(ItemTags.IS_RUNEWARE)
    }

    fun ItemStack.setRuneAugmentCount(amount: Int) {
        addIntTag(ItemTags.RUNEWARE_AUGMENT_COUNT, amount)
    }

    fun ItemStack.getRuneAugmentCount(): Int {
        return getIntTag(ItemTags.RUNEWARE_AUGMENT_COUNT) ?: 0
    }

    // Add Attribute modifier to new runesherd
    fun ItemStack.addModifierToRunesherd(runesherd: OdysseyRunesherd) {
        if (runesherd.attribute == null) return
        if (runesherd.affectedEquipment == null) return
        if (runesherd.affectedEquipment.isEmpty()) return
        // Values for attribute manager
        val value = runesherd.value
        val attributeName = "odyssey." + runesherd.name + "_modifier"
        // When
        when(runesherd.attribute) { // FIX FOR ALL SLOTS!!!!!
            Attribute.GENERIC_ATTACK_DAMAGE -> {
                addAttackDamageAttribute(value, attributeName, AttributeIDs.RUNESHERD_ATTACK_DAMAGE_UUID)
            }
            Attribute.GENERIC_ARMOR -> {
                addArmorAttribute(value, attributeName, AttributeIDs.RUNESHERD_ARMOR_UUID)
            }
            else -> {

            }
        }
    }


    // Add runesherd to item
    fun addRunesherdToSmithingItem(runesherd: ItemStack, equipment: ItemStack): ItemStack? {
        // Basic Checks
        if (!runesherd.hasRunesherdTag()) return null
        if (!runesherd.hasOdysseyTag()) return null
        // Runeware can have up to 3 runesherd augments
        val isRuneware = equipment.hasRunewareTag()
        if (equipment.hasRuneAugmentTag() && !isRuneware) return null
        // More Checks
        if (!runesherd.itemMeta.hasAttributeModifiers()) return null
        // Find compatible runesherd
        val runesherdName = runesherd.getOdysseyTag() ?: return null
        val attributeName = "odyssey." + runesherdName + "_modifier"
        val attributeType: Attribute = findRunesherdAttribute(runesherdName) ?: return null
        val attributeMap = runesherd.itemMeta.attributeModifiers?.get(attributeType) ?: return null
        val runesherdModifier = attributeMap.find { it.name == attributeName } ?: return null
        // Can not stack same runesherd
        if (equipment.itemMeta.attributeModifiers != null && !isRuneware) {
            val itemAttributes = equipment.itemMeta.attributeModifiers!![attributeType]
            if (itemAttributes.contains(runesherdModifier)) return null
        }
        // Get values for new modifier
        var runesherdValue = runesherdModifier.amount
        val attributeID = runesherdModifier.uniqueId
        //val attributeSlots = runesherdAttribute.slot
        // TODO: fix for all slots

        // Check if runeware has
        if (isRuneware) {
            val runeCount = equipment.getRuneAugmentCount()
            if (runeCount >= 3) return null // CAN ONLY ADD 3
            // Find and remove attribute
            val equipmentMap = equipment.itemMeta.attributeModifiers?.get(attributeType)
            val matchingRune = equipmentMap?.find { it.name == attributeName }
            if (matchingRune != null) {
                // If found remove but add amount to current sherd value then Add in when
                runesherdValue += matchingRune.amount
                equipment.itemMeta = equipment.itemMeta.also {
                    it.removeAttributeModifier(attributeType, matchingRune)
                }
            }
            equipment.setRuneAugmentCount(runeCount + 1)
        }

        // Match
        when(attributeType) {
            Attribute.GENERIC_ATTACK_DAMAGE -> {
                equipment.addAttackDamageAttribute(runesherdValue, attributeName, attributeID)
            }
            Attribute.GENERIC_ARMOR -> {
                equipment.addArmorAttribute(runesherdValue, attributeName, attributeID)
            }
            else -> {

            }
        }
        // Add augmented tag if not yet
        if (!equipment.hasRuneAugmentTag()) {
            equipment.addRuneAugmentTag()
        }
        return equipment
    }

    // Find Attribute
    private fun findRunesherdAttribute(name: String): Attribute? {
        return when (name) {
            Runesherds.ASSAULT_RUNESHERD.name -> {
                Attribute.GENERIC_ATTACK_DAMAGE
            }
            Runesherds.GUARD_RUNESHERD.name -> {
                Attribute.GENERIC_ARMOR
            }
            else -> {
                null
            }
        }
    }


    // MAYBE FOR MAKING HIGHER QUALITY RUNES
    // use pots
    // since sherds are in pottery
    // have a pot and the rune sherd

    // USE NETHER BRICK FOR RARER TYPES


    // WHEN ADDING NEW SHERDS MAKE SURE TO INCULDE ATTRIBUTES IN WHEN

}

// To make a better world