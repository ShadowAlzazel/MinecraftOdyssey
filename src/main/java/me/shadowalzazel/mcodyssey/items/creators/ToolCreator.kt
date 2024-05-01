package me.shadowalzazel.mcodyssey.items.creators

import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.addStringTag
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

class ToolCreator : AttributeManager {

    private val otherTools = listOf(ToolType.SHURIKEN)

    fun createToolStack(material: ToolMaterial, type: ToolType, amount: Int = 1): ItemStack {
        val minecraftItemKey = if (type in otherTools) {
            type.itemOverrideSuf // Get item key from tool
        } else {
            "${material.itemOverridePre}_${type.itemOverrideSuf}"
        }
        val minecraftItem = Material.matchMaterial(minecraftItemKey) ?: return ItemStack(Material.AIR)
        val itemStack = ItemStack(minecraftItem, amount).also {
            // Create Variables
            val model = (material.itemModelPre * 100) + (type.itemModelSuf)
            val itemName = "${material.itemName}_${type.itemName}"
            val customName = "${material.customName} ${type.customName}"
            val damage = material.attackDamage + type.baseDamage
            val maxDurability = material.maxDurability
            var speed = type.baseSpeed
            // Iridium and titanium have different speeds
            if (material == ToolMaterial.IRIDIUM) {
                speed *= 0.9
            }
            else if (material == ToolMaterial.TITANIUM || material == ToolMaterial.ANDONIZED_TITANIUM) {
                speed *= 1.1
            }
            // Assign variables (meta/components)
            val meta = it.itemMeta
            // Tools with custom durability
            if (maxDurability != null && meta is Damageable) {
                meta.setMaxDamage(maxDurability)
            }
            meta.setItemName(itemName)
            meta.setCustomModelData(model)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            it.itemMeta = meta
            it.addStringTag(ItemDataTags.WEAPON_TYPE, type.itemName)
            it.addStringTag(ItemDataTags.MATERIAL_TYPE, material.itemName)
            // Assign attributes
            it.addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            it.setNewAttackSpeedAttribute(speed, AttributeTags.ITEM_BASE_ATTACK_SPEED)
        }
        return itemStack
    }

}