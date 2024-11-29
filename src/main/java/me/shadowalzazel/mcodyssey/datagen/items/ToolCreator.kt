package me.shadowalzazel.mcodyssey.datagen.items

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.DataKeys
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.util.ToolComponentHelper
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.DataTagManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Suppress("UnstableApiUsage")
class ToolCreator : AttributeManager, DataTagManager, ToolComponentHelper {

    fun createToolStack(material: ToolMaterial, type: ToolType, amount: Int = 1): ItemStack {
        val otherTools = listOf(ToolType.SHURIKEN)
        val minecraftItemKey = if (type in otherTools) {
            type.itemOverrideSuf // Get item key from tool
        } else {
            "${material.itemOverridePre}_${type.itemOverrideSuf}"
        }
        val minecraftMaterial = Material.matchMaterial(minecraftItemKey) ?: return ItemStack(Material.AIR)
        val itemStack = ItemStack(minecraftMaterial, amount).apply {
            // Create Variables
            val itemName = "${material.itemName}_${type.itemName}"
            val upperName = "${material.customName} ${type.customName}"
            val customName = Component.text(upperName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            val damage = material.attackDamage + type.baseDamage
            val maxDurability = material.maxDurability
            var speed = type.baseSpeed
            val bonusRange = type.bonusRange
            val itemModel = DataKeys.newKey(itemName)
            // Iridium and titanium have different speeds
            if (material == ToolMaterial.IRIDIUM) {
                speed *= 0.9
            }
            else if (material == ToolMaterial.TITANIUM || material == ToolMaterial.ANODIZED_TITANIUM) {
                speed *= 1.1
            }
            // Tools with mining ToolComponent
            val mineableTags = getMiningTags(type.itemName)
            if (mineableTags != null) {
                val newToolComponent = newToolComponent(material.itemName, type.itemName)
                if (newToolComponent != null) {
                    this.resetData(DataComponentTypes.TOOL)
                    this.setData(DataComponentTypes.TOOL, newToolComponent)
                }
            }
            // Set the Component Data
            if (maxDurability != null) this.setData(DataComponentTypes.MAX_DAMAGE, maxDurability)
            this.setData(DataComponentTypes.ITEM_MODEL, itemModel)
            this.setData(DataComponentTypes.CUSTOM_NAME, customName)
            this.setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
            //meta.itemName(Component.text(itemName))
            // Set Custom Data
            val meta = this.itemMeta
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName)
            this.itemMeta = meta
            this.addStringTag(ItemDataTags.TOOL_TYPE, type.itemName)
            this.addStringTag(ItemDataTags.MATERIAL_TYPE, material.itemName)
            // Assign Base attributes
            this.addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            this.setNewAttackSpeedAttribute(speed)
            if (bonusRange != null) this.addEntityRangeAttribute(bonusRange, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            // Finish
        }
        return itemStack
    }

}