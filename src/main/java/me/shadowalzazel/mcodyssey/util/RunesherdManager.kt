@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

internal interface RunesherdManager : AttributeManager, DataTagManager {

    // MAYBE FOR MAKING HIGHER QUALITY RUNES
    // use pots
    // since sherds are in pottery
    // have a pot and the rune sherd
    // USE NETHER BRICK FOR RARER TYPES
    // WHEN ADDING NEW SHERDS MAKE SURE TO include ATTRIBUTES IN WHEN
    /* ---------------------------------------- */
    // TAGS

    // TODO: Invert values

    fun ItemStack.addRunesherdTag() {
        addTag(ItemDataTags.IS_RUNESHERD)
    }

    fun ItemStack.hasRunesherdTag(): Boolean {
        return hasTag(ItemDataTags.IS_RUNESHERD)
    }

    fun ItemStack.addRuneIdentifier(name: String) {
        addStringTag(ItemDataTags.RUNE_IDENTIFIER, name)
    }

    fun ItemStack.getRuneIdentifier(): String? {
        val runeName = getStringTag(ItemDataTags.RUNE_IDENTIFIER)
        if (runeName != null) return runeName
        val itemIdentifier = getItemIdentifier()
        if (itemIdentifier != null) return itemIdentifier
        return null
    }

    fun ItemStack.addRuneAugmentTag() {
        addTag(ItemDataTags.HAS_RUNE_AUGMENT)
    }

    fun ItemStack.hasRuneAugmentTag(): Boolean {
        return hasTag(ItemDataTags.HAS_RUNE_AUGMENT)
    }

    fun ItemStack.addRunewareTag() {
        addTag(ItemDataTags.IS_RUNEWARE)
    }

    fun ItemStack.hasRunewareTag(): Boolean {
        return hasTag(ItemDataTags.IS_RUNEWARE)
    }

    fun ItemStack.setRuneAugmentCount(amount: Int) {
        setIntTag(ItemDataTags.RUNEWARE_AUGMENT_COUNT, amount)
    }

    fun ItemStack.getRuneAugmentCount(): Int {
        return getIntTag(ItemDataTags.RUNEWARE_AUGMENT_COUNT) ?: 0
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Find Attribute
    fun findRunesherdAttribute(name: String): Attribute? {
        return when (name) {
            "assault_runesherd", "assault_rune" -> Attribute.ATTACK_DAMAGE
            "guard_runesherd", "guard_rune" -> Attribute.ARMOR
            "finesse_runesherd", "finesse_rune" -> Attribute.ATTACK_SPEED
            "swift_runesherd", "swift_rune" -> Attribute.MOVEMENT_SPEED
            "vitality_runesherd", "vitality_rune" -> Attribute.MAX_HEALTH
            "steadfast_runesherd", "steadfast_rune" -> Attribute.KNOCKBACK_RESISTANCE
            "force_runesherd", "force_rune" -> Attribute.ATTACK_KNOCKBACK
            "break_runesherd", "break_rune" -> Attribute.BLOCK_BREAK_SPEED
            "grasp_runesherd", "grasp_rune" -> Attribute.BLOCK_INTERACTION_RANGE
            "jump_runesherd", "jump_rune" -> Attribute.JUMP_STRENGTH
            "gravity_runesherd", "gravity_rune" -> Attribute.GRAVITY
            "range_runesherd", "range_rune" -> Attribute.ENTITY_INTERACTION_RANGE
            "size_runesherd", "size_rune" -> Attribute.SCALE
            else -> null
        }
    }

    // Used to get UUID from attribute held by runesherds
    fun getRuneAttributeName(attribute: Attribute): String {
        return when(attribute) {
            Attribute.ATTACK_DAMAGE ->  AttributeTags.RUNE_ATTACK_DAMAGE
            Attribute.ARMOR ->  AttributeTags.RUNE_ARMOR
            Attribute.ATTACK_SPEED ->  AttributeTags.RUNE_ATTACK_SPEED
            Attribute.MOVEMENT_SPEED ->  AttributeTags.RUNE_MOVEMENT_SPEED
            Attribute.MAX_HEALTH ->  AttributeTags.RUNE_BONUS_HEALTH
            Attribute.KNOCKBACK_RESISTANCE ->  AttributeTags.RUNE_KNOCKBACK_RESISTANCE
            Attribute.ATTACK_KNOCKBACK ->  AttributeTags.RUNE_ATTACK_KNOCKBACK
            Attribute.BLOCK_BREAK_SPEED ->  AttributeTags.RUNE_BLOCK_BREAK_SPEED
            Attribute.BLOCK_INTERACTION_RANGE ->  AttributeTags.RUNE_BLOCK_REACH
            Attribute.JUMP_STRENGTH ->  AttributeTags.RUNE_JUMP_STRENGTH
            Attribute.GRAVITY ->  AttributeTags.RUNE_GRAVITY
            Attribute.ENTITY_INTERACTION_RANGE ->  AttributeTags.RUNE_ENTITY_REACH
            Attribute.SCALE ->  AttributeTags.RUNE_SCALE
            Attribute.STEP_HEIGHT ->  AttributeTags.RUNE_STEP_HEIGHT
            Attribute.ARMOR_TOUGHNESS ->  AttributeTags.RUNE_ARMOR_TOUGHNESS
            else -> "rune.generic"
        }
    }

    fun ItemStack.addRuneModifier(
        attribute: Attribute,
        value: Double,
        name: String,
        slots: EquipmentSlotGroup
    ) {
        when(attribute) {
            Attribute.ATTACK_DAMAGE -> addAttackDamageAttribute(value, name, slots)
            Attribute.ARMOR -> addArmorAttribute(value, name, slots)
            Attribute.ATTACK_SPEED -> addAttackSpeedAttribute(value, name, slots)
            Attribute.MOVEMENT_SPEED -> addMovementSpeedAttribute(value, name, slots)
            Attribute.MAX_HEALTH -> addMaxHealthAttribute(value, name, slots)
            Attribute.KNOCKBACK_RESISTANCE -> addKnockbackResistanceAttribute(value, name, slots)
            Attribute.ATTACK_KNOCKBACK -> addAttackKnockbackAttribute(value, name, slots)
            else -> setGenericAttribute(value, name, attribute, slotGroup = slots)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Add runesherd to item
    fun addRunesherdToItemStack(runesherd: ItemStack, item: ItemStack): ItemStack? {
        // Basic Checks
        if (!runesherd.hasRunesherdTag()) return null
        // Runeware can have up to 3 runesherd augments
        val equipment = item.clone()
        val itemIsRuneware = equipment.hasRunewareTag()
        if (equipment.hasRuneAugmentTag() && !itemIsRuneware) return null
        if (!runesherd.itemMeta.hasAttributeModifiers()) return null
        val runeName = runesherd.getRuneIdentifier() ?: return null // ItemName
        // Get rune key
        val runeKey = AttributeTags.RUNESHERD_KEY
        val runeAttribute = findRunesherdAttribute(runeName) ?: return null
        val runesherdAttributeModifiers = runesherd.itemMeta.attributeModifiers?.get(runeAttribute) ?: return null
        val runesherdModifier = runesherdAttributeModifiers.find { it.name == runeKey } ?: return null
        val runeAttributeName = getRuneAttributeName(runeAttribute)
        val slotNameExtension = getMaterialSlotName(equipment.type) // Prohibits MULTI runeware!!???
        val fullAttributeName = "${runeAttributeName}.${slotNameExtension}"
        // Can not stack runesherd keys
        if (equipment.itemMeta.attributeModifiers != null && !itemIsRuneware) {
            val itemAttributes = equipment.itemMeta.attributeModifiers!![runeAttribute]
            if (itemAttributes.contains(runesherdModifier)) return null
        }
        // Get values for new modifier
        val runesherdValue = runesherdModifier.amount
        var previousValue = 0.0
        // Add to runeware more
        if (itemIsRuneware) {
            val runeCount = equipment.getRuneAugmentCount()
            if (runeCount >= 3) return null // CAN ONLY ADD 3
            // Find and remove attribute
            val equipmentModifiers = equipment.itemMeta.attributeModifiers?.get(runeAttribute)
            val matchingModifier = equipmentModifiers?.find { it.name == fullAttributeName }
            if (matchingModifier != null) {
                // If found remove but add amount to current sherd value then Add in when
                previousValue += matchingModifier.amount
                equipment.itemMeta = equipment.itemMeta.also {
                    it.removeAttributeModifier(runeAttribute, matchingModifier)
                }
            }
            equipment.setRuneAugmentCount(runeCount + 1)
        }
        // Check sherd slot type
        val itemGroups = getMaterialSlotGroups(equipment.type)
        val runeGroup = runesherdModifier.slotGroup
        val runeMatches = runeGroup in itemGroups
        val mainGroup = itemGroups[0] // First in list
        // Creating new base
        val itemIsArmor = itemGroups.contains(EquipmentSlotGroup.ARMOR)
        // For base item stats FOR BASE VALUES
        if (equipment.itemMeta.attributeModifiers == null || !equipment.itemMeta.hasAttributeModifiers()) {
            // Check if armor
            if (itemIsArmor) {
                val baseValues = getBaseDataArmor(equipment.type) // Pair(armor, toughness)
                equipment.addArmorAttribute(baseValues.first, "generic.armor.$slotNameExtension", mainGroup)
                // Add toughness if can
                if (baseValues.second > 0.0) {
                    equipment.addArmorToughnessAttribute(baseValues.second, "generic.armor_toughness.$slotNameExtension", mainGroup)
                }
                // CHeck if netherite to also add knockback resistance
                if (baseValues.second >= 3.0) { // apparently 0.1 is 1
                    equipment.addKnockbackResistanceAttribute(0.1, "generic.knockback_resistance.$slotNameExtension",  mainGroup)
                }
            } else {
                val baseValues = getBaseDataTools(equipment.type) // Pair(damage, speed)
                if (baseValues.second != 0.0) {
                    equipment.addAttackDamageAttribute(baseValues.first, "generic.attack_damage.$slotNameExtension")
                    equipment.setNewAttackSpeedAttribute(baseValues.second)
                }
            }
        }
        // If Matching
        val matchingSlotMultiplier = if (runeMatches) {
            1.0
        } else if (itemIsRuneware) {
            1.0
        } else {
            0.75
        }
        val totalValue = (runesherdValue * matchingSlotMultiplier) + previousValue
        // Get final group
        val finalGroup = if (itemIsRuneware) {
            EquipmentSlotGroup.HAND
        } else {
            mainGroup // Converts the runesherd to the armor (removing ANY) kind of nerfing it
        }
        // Apply
        return equipment.apply {
            addRuneModifier(runeAttribute, totalValue, fullAttributeName, finalGroup)
            if (!equipment.hasRuneAugmentTag()) {
                equipment.addRuneAugmentTag()
            }
        }
    }

    private fun getMaterialSlotGroups(material: Material): List<EquipmentSlotGroup> {
        val slotGroups = when (material) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
            Material.MACE, Material.TRIDENT, Material.FISHING_ROD -> {
                listOf(EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.HAND)
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                listOf(EquipmentSlotGroup.LEGS, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.BODY)
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.ELYTRA -> {
                listOf(EquipmentSlotGroup.CHEST, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.BODY)
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                listOf(EquipmentSlotGroup.FEET, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.BODY)
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET,
            Material.TURTLE_HELMET, Material.CARVED_PUMPKIN -> {
                listOf(EquipmentSlotGroup.HEAD, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.BODY)
            }
            Material.BOW, Material.CROSSBOW -> {
                listOf(EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.HAND)
            }
            else -> {
                listOf(EquipmentSlotGroup.OFFHAND)
            }
        }
        return slotGroups + listOf(EquipmentSlotGroup.ANY)
    }

    private fun getMaterialSlotName(material: Material): String {
        return when (material) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
            Material.MACE, Material.TRIDENT, Material.FISHING_ROD -> {
                "mainhand"
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                "legs"
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.ELYTRA -> {
                "chest"
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                "feet"
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET,
            Material.TURTLE_HELMET, Material.CARVED_PUMPKIN -> {
                "head"
            }
            Material.BOW, Material.CROSSBOW -> {
                "mainhand"
            }
            Material.BRICK, Material.CLAY_BALL -> {
                "any"
            }
            else -> {
                "offhand"
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
            Material.DIAMOND_BOOTS, Material.DIAMOND_HELMET -> {
                Pair(3.0, 2.0)
            }
            Material.DIAMOND_LEGGINGS -> {
                Pair(6.0, 2.0)
            }
            Material.DIAMOND_CHESTPLATE -> {
                Pair(8.0, 2.0)
            }
            Material.NETHERITE_BOOTS, Material.NETHERITE_HELMET -> {
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

}

// To make a better world