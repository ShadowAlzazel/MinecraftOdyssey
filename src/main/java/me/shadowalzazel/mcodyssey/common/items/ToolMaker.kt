package me.shadowalzazel.mcodyssey.common.items

import io.papermc.paper.datacomponent.DataComponentBuilder
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BlocksAttacks
import io.papermc.paper.datacomponent.item.SwingAnimation
import io.papermc.paper.registry.keys.DamageTypeKeys
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.ToolComponentHelper
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.damage.DamageType
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface ToolMaker : AttributeManager, DataTagManager, ToolComponentHelper {

    fun createToolStack(material: ToolMaterial, type: ToolType, amount: Int = 1): ItemStack {
        val otherTools = listOf(ToolType.SHURIKEN)
        val minecraftItemKey = if (type in otherTools) {
            type.vanillaBase // Get item key from tool
        } else {
            "${material.vanillaBase}_${type.vanillaBase}"
        }
        val minecraftMaterial = Material.matchMaterial(minecraftItemKey) ?: return ItemStack(Material.AIR)
        val itemStack = ItemStack(minecraftMaterial, amount).apply {
            // Create Variables
            val itemName = "${material.nameId}_${type.toolName}"
            val upperName = "${material.customName} ${type.fullName}"
            val customName = Component.text(upperName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            val damage = material.attackDamage + type.baseDamage
            val maxDurability = material.maxDurability
            var speed = type.baseSpeed
            val bonusRange = type.bonusRange
            val itemModel = createOdysseyKey(itemName)
            // Iridium and titanium have different speeds
            if (material == ToolMaterial.IRIDIUM) {
                speed *= 0.9
            }
            else if (material == ToolMaterial.TITANIUM || material == ToolMaterial.ANODIZED_TITANIUM) {
                speed *= 1.1
            }
            // ADD COMPONENTS
            if (maxDurability != null) this.setData(DataComponentTypes.MAX_DAMAGE, maxDurability)
            // Tools with mining ToolComponent
            val mineableTags = getMiningTags(type.toolName)
            if (mineableTags != null) {
                val newToolComponent = newToolComponent(material.nameId, type.toolName)
                if (newToolComponent != null) {
                    this.resetData(DataComponentTypes.TOOL)
                    this.setData(DataComponentTypes.TOOL, newToolComponent)
                }
            }
            // Set item names and models
            this.setData(DataComponentTypes.ITEM_MODEL, itemModel)
            this.setData(DataComponentTypes.CUSTOM_NAME, customName)
            this.setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
            // Set type and material identifiers
            this.setStringTag("item", itemName) // ItemKey
            this.setStringTag(ItemDataTags.TOOL_TYPE, type.toolName)
            this.setStringTag(ItemDataTags.MATERIAL_TYPE, material.nameId)
            // Assign Base Combat Attributes
            this.addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            this.setNewAttackSpeedAttribute(speed)
            if (bonusRange != null) this.addEntityRangeAttribute(bonusRange, AttributeTags.ITEM_BASE_ENTITY_RANGE)
            // Assign Special Combat Components
            /*
            val damageType = DamageType.PLAYER_ATTACK
            this.setData(DataComponentTypes.DAMAGE_TYPE, damageType)
            val swingAnimation = SwingAnimation.swingAnimation()
                .type(SwingAnimation.Animation.STAB)
                .duration(12) // In Ticks
                .build()
            this.setData(DataComponentTypes.SWING_ANIMATION, swingAnimation)
            val blocking = BlocksAttacks.blocksAttacks()
                .disableCooldownScale(1F)
                .damageReductions()
                .blockSound()
                .bypassedBy(DamageType.MAGIC)

             */
            // Finish
        }
        return itemStack
    }

}