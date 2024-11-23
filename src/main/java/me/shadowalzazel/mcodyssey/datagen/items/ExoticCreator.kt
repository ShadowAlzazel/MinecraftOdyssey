package me.shadowalzazel.mcodyssey.datagen.items

import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.items.custom.Exotics
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

interface ExoticCreator : AttributeManager, DataTagManager {

    fun OdysseyItem.createExoticWeapon(): ItemStack {
        val item = this.newItemStack(1)
        // When Statement for specific
        when(this) {
            Exotics.KNIGHT_BREAKER -> { createKnightBreaker(item) }
            Exotics.SHOGUN_LIGHTNING -> { createShogunLightning(item) }
            Exotics.ABZU_BLADE -> { createAbzuBlade(item) }
            Exotics.EXCALIBUR -> { createExcalibur(item) }
            Exotics.FROST_FANG -> { createFrostFang(item) }
            Exotics.ELUCIDATOR -> { createElucidator(item) }
            else -> return item // Want to return basic if not found
        }
        // Set Exotic Meta
        item.addTag(ItemDataTags.IS_EXOTIC)
        val meta = item.itemMeta
        meta.setRarity(ItemRarity.EPIC)
        item.itemMeta = meta
        // Return item
        return item
    }

    // Durability is all prime numbers
    private fun ItemStack.setMaxDurability(value: Int) {
        this.itemMeta = (itemMeta as Damageable).also {
            it.setMaxDamage(value)
        }
    }

    private fun createKnightBreaker(item: ItemStack) {
        item.apply {
            addStringTag(ItemDataTags.TOOL_TYPE, "dagger")
            // ADD TAG, BONUS PEIRCE
            addAttackDamageAttribute(9.0, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            addEntityRangeAttribute(-0.1, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            setNewAttackSpeedAttribute(3.0)
            setMaxDurability(1009)
        }
    }

    private fun createShogunLightning(item: ItemStack) {
        item.apply {
            addStringTag(ItemDataTags.TOOL_TYPE, "katana")
            addAttackDamageAttribute(12.0, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            addEntityRangeAttribute(0.3, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            setNewAttackSpeedAttribute(1.7)
            setMaxDurability(2111)
        }
    }

    private fun createAbzuBlade(item: ItemStack) {
        item.apply {
            addStringTag(ItemDataTags.TOOL_TYPE, "claymore")
            addAttackDamageAttribute(21.0, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            addEntityRangeAttribute(0.7, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            setNewAttackSpeedAttribute(0.8)
            setMaxDurability(3109)
        }
    }

    private fun createExcalibur(item: ItemStack) {
        item.apply {
            addStringTag(ItemDataTags.TOOL_TYPE, "longsword")
            addStringTag(ItemDataTags.MATERIAL_TYPE, "mithril") // MAYBE
            addAttackDamageAttribute(15.0, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            addEntityRangeAttribute(0.3, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            setNewAttackSpeedAttribute(1.4)
            setMaxDurability(2357)
        }
    }

    private fun createFrostFang(item: ItemStack) {
        item.apply {
            addStringTag(ItemDataTags.TOOL_TYPE, "dagger")
            addAttackDamageAttribute(10.0, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            addEntityRangeAttribute(-0.1, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            setNewAttackSpeedAttribute(3.0)
            setMaxDurability(1019)
        }
    }

    private fun createElucidator(item: ItemStack) {
        item.apply {
            addStringTag(ItemDataTags.TOOL_TYPE, "longsword")
            addAttackDamageAttribute(14.0, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            addEntityRangeAttribute(0.3, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            setNewAttackSpeedAttribute(1.5)
            setMaxDurability(2797)
        }
    }

}