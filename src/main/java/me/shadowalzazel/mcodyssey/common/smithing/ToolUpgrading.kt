package me.shadowalzazel.mcodyssey.common.smithing

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.api.ToolDataManager
import me.shadowalzazel.mcodyssey.util.NamedKeys
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.ToolComponentHelper
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class ToolUpgrading : DataTagManager, ToolComponentHelper, AdvancementManager {

    fun toolSmithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        val inputMaterial = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment ?: return
        if (ToolDataManager.getToolMaterial(equipment) in SmithingMaps.NOT_UPGRADEABLE) return
        // Switch case
        when (inputMaterial.type) {
            Material.NETHERITE_INGOT -> netheriteUpgrading(event)
            Material.IRON_INGOT -> customUpgrading(event)
            else -> {}
        }
    }


    private fun netheriteUpgrading(event: PrepareSmithingEvent) {
        val equipment = event.inventory.inputEquipment ?: return
        if (equipment.itemMeta?.hasItemModel() != true) return
        val upgradePath = "netherite"
        val upgradedItem = toolUpgradePathHandler(equipment.clone(), upgradePath)
        modifyDamage(upgradedItem, 1.0)
        event.result = upgradedItem
    }


    private fun customUpgrading(event: PrepareSmithingEvent) {
        event.result = ItemStack(Material.AIR)
        val equipment = event.inventory.inputEquipment ?: return
        val resultItem = equipment.clone()
        // Get ids
        val inputMaterialId = event.inventory.inputMineral?.getItemIdentifier() ?: return
        val templateId = event.inventory.inputTemplate?.getItemIdentifier() ?: return
        // Cross-check
        if (SmithingMaps.TEMPLATE_INPUT_MAP[templateId] != inputMaterialId) return
        // Get upgrade path from the inputMaterial
        val upgradePath = SmithingMaps.MATERIAL_UPGRADE_MAP[inputMaterialId] ?: return
        val upgradedItem = toolUpgradePathHandler(resultItem, upgradePath)
        when (upgradePath) {
            "iridium" -> {
                modifyDamage(upgradedItem, 1.0)
                modifyAttackSpeed(upgradedItem, 0.9)
                rewardAdvancement(event.viewers, "odyssey/smith_iridium")
            }
            "mithril" -> {
                modifyDamage(upgradedItem, 2.0)
                rewardAdvancement(event.viewers, "odyssey/smith_mithril")
            }
            "soul_steel" -> {
                modifyDamage(upgradedItem, 1.0)
                rewardAdvancement(event.viewers, "odyssey/smith_soul_steel")
            }
            "titanium" -> {
                modifyDamage(upgradedItem, 1.0)
                modifyAttackSpeed(upgradedItem, 1.1)
                rewardAdvancement(event.viewers, "odyssey/smith_titanium")
            }
        }
        // Add ToolComponent
        val mineableTags = getMiningTags(ToolDataManager.getToolType(upgradedItem) ?: "none")
        if (mineableTags != null) toolMiningComponentHandler(upgradedItem, upgradePath)
        // Finish
        event.result = upgradedItem
    }


    private fun toolUpgradePathHandler(item: ItemStack, upgradePath: String): ItemStack {
        val toolType = ToolDataManager.getToolType(item)
        val newModel = NamedKeys.newKey("${upgradePath}_${toolType}")
        item.setData(DataComponentTypes.ITEM_MODEL, newModel)
        val maxDamage = SmithingMaps.DURABILITY_MAP[upgradePath]
        if (maxDamage != null)  item.setData(DataComponentTypes.MAX_DAMAGE, maxDamage)
        return item
    }

    private fun toolMiningComponentHandler(item: ItemStack, upgradePath: String) {
        val toolType = ToolDataManager.getToolType(item) ?: return
        val newToolComponent = newToolComponent(upgradePath, toolType)
        if (newToolComponent != null) {
            item.resetData(DataComponentTypes.TOOL)
            item.setData(DataComponentTypes.TOOL, newToolComponent)
        }
    }


    private fun modifyDamage(item: ItemStack, bonus: Double = 1.0) {
        // Get Old Modifier
        val oldDamageModifier = item.itemMeta.getAttributeModifiers(Attribute.ATTACK_DAMAGE)?.first {
            it.name == AttributeTags.ITEM_BASE_ATTACK_DAMAGE
        } ?: return
        val newDamage = oldDamageModifier.amount + bonus
        val slots = EquipmentSlotGroup.MAINHAND
        val nameKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
        val newMeta = item.itemMeta
        // Set Meta Attribute
        newMeta.also {
            it.removeAttributeModifier(Attribute.ATTACK_DAMAGE, oldDamageModifier)
            val newModifier = AttributeModifier(nameKey, newDamage, AttributeModifier.Operation.ADD_NUMBER, slots)
            it.addAttributeModifier(Attribute.ATTACK_DAMAGE, newModifier)
        }
        item.itemMeta = newMeta
    }


    private fun modifyAttackSpeed(item: ItemStack, speed: Double = 1.0) {
        // Get Old Modifier
        val oldModifier = item.itemMeta.getAttributeModifiers(Attribute.ATTACK_SPEED)?.first {
            it.name == AttributeTags.ITEM_BASE_ATTACK_SPEED
        } ?: return
        val newSpeed = oldModifier.amount * speed
        val slots = EquipmentSlotGroup.MAINHAND
        val nameKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_SPEED)
        val newMeta = item.itemMeta
        // Set Meta Attribute
        newMeta.also {
            it.removeAttributeModifier(Attribute.ATTACK_SPEED, oldModifier)
            val newModifier = AttributeModifier(nameKey, newSpeed, AttributeModifier.Operation.ADD_NUMBER, slots)
            it.addAttributeModifier(Attribute.ATTACK_SPEED, newModifier)
        }
        item.itemMeta = newMeta
    }


}