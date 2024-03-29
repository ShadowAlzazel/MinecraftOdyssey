package me.shadowalzazel.mcodyssey.rune_writing

import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyItemTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.items.Runesherds
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

internal interface RunesherdManager : AttributeManager {

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
        setIntTag(ItemTags.RUNEWARE_AUGMENT_COUNT, amount)
    }

    fun ItemStack.getRuneAugmentCount(): Int {
        return getIntTag(ItemTags.RUNEWARE_AUGMENT_COUNT) ?: 0
    }

    /*-----------------------------------------------------------------------------------------------*/

    // Find Attribute
    private fun findRunesherdAttribute(name: String): Attribute? {
        return when (name) {
            Runesherds.ASSAULT_RUNESHERD.name -> {
                Attribute.GENERIC_ATTACK_DAMAGE
            }
            Runesherds.GUARD_RUNESHERD.name -> {
                Attribute.GENERIC_ARMOR
            }
            Runesherds.FINESSE_RUNESHERD.name -> {
                Attribute.GENERIC_ATTACK_SPEED
            }
            Runesherds.SWIFT_RUNESHERD.name -> {
                Attribute.GENERIC_MOVEMENT_SPEED
            }
            Runesherds.VITALITY_RUNESHERD.name -> {
                Attribute.GENERIC_MAX_HEALTH
            }
            Runesherds.STEADFAST_RUNESHERD.name -> {
                Attribute.GENERIC_KNOCKBACK_RESISTANCE
            }
            Runesherds.FORCE_RUNESHERD.name -> {
                Attribute.GENERIC_ATTACK_KNOCKBACK
            }
            else -> {
                null
            }
        }
    }

    // Used to get UUID from attribute held by runesherds
    fun getIDForRunesherd(runesherdAttribute: Attribute): UUID {
        return when(runesherdAttribute) {
            Attribute.GENERIC_ATTACK_DAMAGE -> { AttributeIDs.RUNESHERD_ATTACK_DAMAGE_UUID }
            Attribute.GENERIC_ARMOR -> { AttributeIDs.RUNESHERD_ARMOR_UUID }
            Attribute.GENERIC_ATTACK_SPEED -> { AttributeIDs.RUNESHERD_ATTACK_SPEED_UUID }
            Attribute.GENERIC_MOVEMENT_SPEED -> { AttributeIDs.RUNESHERD_MOVEMENT_SPEED_UUID }
            Attribute.GENERIC_MAX_HEALTH -> { AttributeIDs.RUNESHERD_MAX_HEALTH_UUID }
            Attribute.GENERIC_KNOCKBACK_RESISTANCE -> { AttributeIDs.RUNESHERD_KNOCKBACK_RESISTANCE_UUID }
            Attribute.GENERIC_ATTACK_KNOCKBACK -> { AttributeIDs.ATTACK_KNOCKBACK }
            else -> {  UUID.randomUUID() }
        }
    }

    fun ItemStack.addRuneModifier(
        attribute: Attribute,
        value: Double,
        name: String,
        id: UUID,
        slot: EquipmentSlot
    ) {
        when(attribute) {
            Attribute.GENERIC_ATTACK_DAMAGE -> {
                addAttackDamageAttribute(value, name, id, slot)
            }
            Attribute.GENERIC_ARMOR -> {
                addArmorAttribute(value, name, id, slot)
            }
            Attribute.GENERIC_ATTACK_SPEED -> {
                addAttackSpeedAttribute(value, name, id, slot)
            }
            Attribute.GENERIC_MOVEMENT_SPEED -> {
                addMovementSpeedAttribute(value, name, id, slot)
            }
            Attribute.GENERIC_MAX_HEALTH -> {
                addMaxHealthAttribute(value, name,  id, slot)
            }
            Attribute.GENERIC_KNOCKBACK_RESISTANCE -> {
                addKnockbackResistanceAttribute(value, name, id, slot)
            }
            Attribute.GENERIC_ATTACK_KNOCKBACK -> {
                addAttackKnockbackAttribute(value, name, id, slot)
            }
            else -> {
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    // Add runesherd to item
    fun addRunesherdToSmithingItem(runesherd: ItemStack, item: ItemStack): ItemStack? {
        // Basic Checks
        if (!runesherd.hasRunesherdTag()) return null
        if (!runesherd.hasOdysseyItemTag()) return null
        // Runeware can have up to 3 runesherd augments
        val equipment = item.clone()
        val equipIsRuneware = equipment.hasRunewareTag()
        if (equipment.hasRuneAugmentTag() && !equipIsRuneware) return null
        // More Checks
        if (!runesherd.itemMeta.hasAttributeModifiers()) return null
        // Find compatible runesherd
        val runesherdName = runesherd.getOdysseyTag() ?: return null
        val attributeName = "odyssey." + runesherdName + "_modifier"
        val attributeType: Attribute = findRunesherdAttribute(runesherdName) ?: return null
        val attributeMap = runesherd.itemMeta.attributeModifiers?.get(attributeType) ?: return null
        val runesherdModifier = attributeMap.find { it.name == attributeName } ?: return null
        // Can not stack same runesherd
        if (equipment.itemMeta.attributeModifiers != null && !equipIsRuneware) {
            val itemAttributes = equipment.itemMeta.attributeModifiers!![attributeType]
            if (itemAttributes.contains(runesherdModifier)) return null
        }
        // Get values for new modifier
        var runesherdValue = runesherdModifier.amount
        var previousValue = 0.0
        //val attributeID = runesherdModifier.uniqueId
        val attributeID = UUID.randomUUID()
        // Check if runeware has
        if (equipIsRuneware) {
            val runeCount = equipment.getRuneAugmentCount()
            if (runeCount >= 3) return null // CAN ONLY ADD 3
            // Find and remove attribute
            val equipmentMap = equipment.itemMeta.attributeModifiers?.get(attributeType)
            val matchingRune = equipmentMap?.find { it.name == attributeName }
            if (matchingRune != null) {
                // If found remove but add amount to current sherd value then Add in when
                previousValue += matchingRune.amount
                equipment.itemMeta = equipment.itemMeta.also {
                    it.removeAttributeModifier(attributeType, matchingRune)
                }
            }
            equipment.setRuneAugmentCount(runeCount + 1)
        }
        // Check sherd slot type
        val armorList = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
        val runeSlot = runesherdModifier.slot ?: EquipmentSlot.OFF_HAND
        val runesIsForArmor = runeSlot in armorList
        val equipSlot = getMaterialEquipmentSlot(equipment.type)
        val equipIsArmor = equipSlot in armorList

        // Creating new base
        // For base item stats FOR BASE VALUES
        if (equipment.itemMeta.attributeModifiers == null || !equipment.itemMeta.hasAttributeModifiers()) {
            // Check if armor
            val randomUUID = UUID.randomUUID()
            if (equipIsArmor) {
                val baseValues = getBaseDataArmor(equipment.type) // Pair(armor, toughness)
                equipment.addArmorAttribute(baseValues.first, "generic.armor", randomUUID, equipSlot)
                // Add toughness if can
                if (baseValues.second > 0.0) {
                    equipment.addArmorToughnessAttribute(baseValues.second, "generic.armor_toughness", randomUUID, equipSlot)
                }
                // CHeck if netherite to also add knockback resistance
                if (baseValues.second >= 3.0) { // apparently 0.1 is 1
                    equipment.addKnockbackResistanceAttribute(0.1, "generic.knockback_resistance", randomUUID, equipSlot)
                }
            } else {
                val baseValues = getBaseDataTools(equipment.type) // Pair(damage, speed)
                if (baseValues.second != 0.0) {
                    equipment.addAttackDamageAttribute(baseValues.first, "generic.attack_damage", randomUUID)
                    equipment.setNewAttackSpeedAttribute(baseValues.second, "generic.attack_speed", randomUUID)
                }
            }
        }

        // If not matches, 75% efficient
        var matchingSlotMultiplier = if (runeSlot == equipSlot) {
            1.0
        }
        else if (equipIsRuneware) {
            1.0
        }
        else {
            0.75
        }
        val totalValue = (runesherdValue * matchingSlotMultiplier) + previousValue

        return equipment.apply {
            addRuneModifier(attributeType, totalValue, attributeName, attributeID, equipSlot)
            if (!equipment.hasRuneAugmentTag()) {
                equipment.addRuneAugmentTag()
            }
        }
    }

    private fun getMaterialEquipmentSlot(material: Material): EquipmentSlot {
        return when (material) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                EquipmentSlot.HAND
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                EquipmentSlot.LEGS
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.ELYTRA -> {
                EquipmentSlot.CHEST
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                EquipmentSlot.FEET
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET,
            Material.TURTLE_HELMET, Material.CARVED_PUMPKIN -> {
                EquipmentSlot.HEAD
            }
            Material.BOW, Material.CROSSBOW -> {
                EquipmentSlot.HAND
            }
            else -> {
                EquipmentSlot.OFF_HAND
            }
        }
    }

    // FOR ARMOR (Armor, Toughness)
    // FOR TOOLS (Damage, Attack speed)
    private fun getBaseDataArmor(material: Material): Pair<Double, Double> {
        return when (material) {
            Material.LEATHER_HELMET, Material.LEATHER_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS -> {
                Pair(1.0, 0.0)
            }
            Material.TURTLE_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.LEATHER_LEGGINGS, Material.IRON_BOOTS -> { // 2 armor
                Pair(2.0, 0.0)
            }
            Material.GOLDEN_LEGGINGS -> { // 3 Armor
                Pair(3.0, 0.0)
            }
            Material.CHAINMAIL_LEGGINGS -> { // 4 Armor
                Pair(4.0, 0.0)
            }
            Material.IRON_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE -> { // 5 armor
                Pair(5.0, 0.0)
            }
            Material.IRON_CHESTPLATE -> {
                Pair(6.0, 0.0)
            }
            Material.DIAMOND_BOOTS, Material.DIAMOND_HELMET, -> {
                Pair(3.0, 2.0)
            }
            Material.DIAMOND_LEGGINGS -> {
                Pair(6.0, 2.0)
            }
            Material.DIAMOND_CHESTPLATE -> {
                Pair(8.0, 2.0)
            }
            Material.NETHERITE_BOOTS, Material.NETHERITE_HELMET, -> {
                Pair(3.0, 3.0)
            }
            Material.NETHERITE_LEGGINGS -> {
                Pair(6.0, 3.0)
            }
            Material.NETHERITE_CHESTPLATE -> {
                Pair(8.0, 3.0)
            }
            else -> {
                Pair(0.0, 0.0)
            }
        }
    }

    private fun getBaseDataTools(material: Material): Pair<Double, Double> {
        return when (material) {
            // Swords
            Material.GOLDEN_SWORD, Material.WOODEN_SWORD -> {
                Pair(4.0, 1.6)
            }
            Material.STONE_SWORD -> {
                Pair(5.0, 1.6)
            }
            Material.IRON_SWORD -> {
                Pair(6.0, 1.6)
            }
            Material.DIAMOND_SWORD -> {
                Pair(7.0, 1.6)
            }
            Material.NETHERITE_SWORD -> {
                Pair(8.0, 1.6)
            }
            // Axes
            Material.WOODEN_AXE -> {
                Pair(7.0, 0.8)
            }
            Material.GOLDEN_AXE -> {
                Pair(7.0, 1.0)
            }
            Material.STONE_AXE -> {
                Pair(9.0, 0.8)
            }
            Material.IRON_AXE -> {
                Pair(9.0, 0.9)
            }
            Material.DIAMOND_AXE -> {
                Pair(9.0, 1.0)
            }
            Material.NETHERITE_AXE -> {
                Pair(10.0, 1.0)
            }
            // Hoes
            Material.GOLDEN_HOE, Material.WOODEN_HOE  -> {
                Pair(1.0, 1.0)
            }
            Material.STONE_HOE -> {
                Pair(1.0, 2.0)
            }
            Material.IRON_HOE -> {
                Pair(1.0, 3.0)
            }
            Material.DIAMOND_HOE -> {
                Pair(1.0, 4.0)
            }
            Material.NETHERITE_HOE -> {
                Pair(1.0, 4.0)
            }
            // pick
            Material.GOLDEN_PICKAXE, Material.WOODEN_PICKAXE  -> {
                Pair(2.0, 1.2)
            }
            Material.STONE_PICKAXE -> {
                Pair(3.0, 1.2)
            }
            Material.IRON_PICKAXE -> {
                Pair(4.0, 1.2)
            }
            Material.DIAMOND_PICKAXE -> {
                Pair(5.0, 1.2)
            }
            Material.NETHERITE_PICKAXE -> {
                Pair(6.0, 1.2)
            }
            // Shovel
            Material.GOLDEN_SHOVEL, Material.WOODEN_SHOVEL  -> {
                Pair(2.5, 1.0)
            }
            Material.STONE_SHOVEL -> {
                Pair(3.5, 1.0)
            }
            Material.IRON_SHOVEL -> {
                Pair(4.5, 1.0)
            }
            Material.DIAMOND_SHOVEL -> {
                Pair(5.5, 1.0)
            }
            Material.NETHERITE_SHOVEL -> {
                Pair(6.5, 1.0)
            }
            // Misc
            Material.TRIDENT -> {
                Pair(9.0, 1.1)
            }
            else -> {
                Pair(0.0, 0.0)
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