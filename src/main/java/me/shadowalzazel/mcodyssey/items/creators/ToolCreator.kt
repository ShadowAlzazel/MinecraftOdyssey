package me.shadowalzazel.mcodyssey.items.creators

import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.util.DataTagManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

class ToolCreator : AttributeManager, DataTagManager {

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
            val model = (material.itemModelPre * 100) + (type.itemModelSuf)
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
            // Display
            meta.setCustomModelData(model)
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // item identifiers
            meta.setItemName(itemName)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName)
            // Set meta
            this.itemMeta = meta
            this.addStringTag(ItemDataTags.WEAPON_TYPE, type.itemName)
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