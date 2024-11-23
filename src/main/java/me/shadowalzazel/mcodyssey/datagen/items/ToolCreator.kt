package me.shadowalzazel.mcodyssey.datagen.items

import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.DataKeys
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.util.ToolMiningManager
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.DataTagManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

@Suppress("UnstableApiUsage")
class ToolCreator : AttributeManager, DataTagManager, ToolMiningManager {

    private val otherTools = listOf(ToolType.SHURIKEN)

    fun createToolStack(material: ToolMaterial, type: ToolType, amount: Int = 1): ItemStack {
        val minecraftItemKey = if (type in otherTools) {
            type.itemOverrideSuf // Get item key from tool
        } else {
            "${material.itemOverridePre}_${type.itemOverrideSuf}"
        }
        val minecraftMaterial = Material.matchMaterial(minecraftItemKey) ?: return ItemStack(Material.AIR)
        val itemStack = ItemStack(minecraftMaterial, amount).apply {
            // Create Variables
            val itemName = "${material.itemName}_${type.itemName}"
            val customName = "${material.customName} ${type.customName}"
            val damage = material.attackDamage + type.baseDamage
            val maxDurability = material.maxDurability
            var speed = type.baseSpeed
            val bonusRange = type.bonusRange
            // Iridium and titanium have different speeds
            if (material == ToolMaterial.IRIDIUM) {
                speed *= 0.9
            }
            else if (material == ToolMaterial.TITANIUM || material == ToolMaterial.ANODIZED_TITANIUM) {
                speed *= 1.1
            }
            //println("Setting [$itemName] properties.")
            // Assign variables (meta/components)
            val meta = this.itemMeta
            // Tools with custom durability
            if (maxDurability != null && meta is Damageable) {
                meta.setMaxDamage(maxDurability)
            }
            // Tools with mining ToolComponent
            val mineableTags = getTypeMineableTags(type.itemName)
            if (mineableTags != null) {
                val newToolComponent = createMiningToolComponent(meta.tool, material.itemName, mineableTags)
                if (newToolComponent != null) {
                    newToolComponent.damagePerBlock = 1
                    meta.setTool(newToolComponent)
                }
            }
            // Display
            meta.itemModel = DataKeys.newKey(itemName)
            //meta.setCustomModelData(model)
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // item identifiers
            meta.itemName(Component.text(itemName))
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName)
            // Set meta
            this.itemMeta = meta
            this.addStringTag(ItemDataTags.TOOL_TYPE, type.itemName)
            this.addStringTag(ItemDataTags.MATERIAL_TYPE, material.itemName)
            // Assign Base attributes
            this.addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            if (bonusRange != null) {
                this.addEntityRangeAttribute(bonusRange, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            }
            this.setNewAttackSpeedAttribute(speed)
        }
        return itemStack
    }

}