package me.shadowalzazel.mcodyssey.items.creators

import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ToolCreator : AttributeManager {

    fun createToolStack(material: ToolMaterial, type: ToolType): ItemStack {
        val minecraftItemKey = "${material.itemOverridePre}_${type.itemOverrideSuf}"
        val minecraftItem = Material.matchMaterial(minecraftItemKey) ?: return ItemStack(Material.AIR)
        val itemStack = ItemStack(minecraftItem, 1).also {
            // Create Variables
            val model = (material.itemModelPre * 100) + (type.itemModelSuf)
            val itemName = "${material.itemName}_${type.itemName}"
            val customName = "${material.customName} ${type.customName}"
            val damage = material.materialDamage + type.baseDamage
            val speed = type.baseSpeed
            // Assign variables (meta)
            val meta = it.itemMeta
            meta.setCustomModelData(model)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            it.itemMeta = meta
            // Assign attributes
            it.addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            it.setNewAttackSpeedAttribute(speed, AttributeTags.ITEM_BASE_ATTACK_SPEED)
        }
        return itemStack
    }

}