package me.shadowalzazel.mcodyssey.common.smithing

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.api.ToolDataManager
import me.shadowalzazel.mcodyssey.util.DataKeys
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
import org.bukkit.inventory.meta.Damageable

@Suppress("UnstableApiUsage")
class ToolUpgrading : DataTagManager, ToolComponentHelper {

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
                AdvancementManager.rewardAdvancement(event.viewers, "odyssey/smith_iridium")
            }
            "mithril" -> {
                modifyDamage(upgradedItem, 2.0)
                AdvancementManager.rewardAdvancement(event.viewers, "odyssey/smith_mithril")
            }
            "soul_steel" -> {
                modifyDamage(upgradedItem, 1.0)
                AdvancementManager.rewardAdvancement(event.viewers, "odyssey/smith_soul_steel")
            }
            "titanium" -> {
                modifyDamage(upgradedItem, 1.0)
                modifyAttackSpeed(upgradedItem, 1.1)
                AdvancementManager.rewardAdvancement(event.viewers, "odyssey/smith_titanium")
            }
        }
        // Add ToolComponent
        val mineableTags = getTypeMineableTags(ToolDataManager.getToolType(upgradedItem) ?: "none")
        if (mineableTags != null) toolMiningComponentHandler(upgradedItem, upgradePath)
        // Finish
        event.result = upgradedItem
    }


    private fun toolUpgradePathHandler(item: ItemStack, upgradePath: String): ItemStack {
        val meta = item.itemMeta as Damageable
        val toolType = ToolDataManager.getToolType(item)
        meta.itemModel = DataKeys.newKey("${upgradePath}_${toolType}")
        meta.setMaxDamage(SmithingMaps.DURABILITY_MAP[upgradePath])
        item.itemMeta = meta
        return item
    }

    private fun toolMiningComponentHandler(item: ItemStack, upgradePath: String) {
        val meta = item.itemMeta
        meta.setTool(null)
        val mineableTags = getTypeMineableTags(ToolDataManager.getToolType(item)!!)!!
        val newToolComponent = createMiningToolComponent(item.itemMeta.tool, upgradePath, mineableTags)
        if (newToolComponent != null) {
            newToolComponent.damagePerBlock = 1
            meta.setTool(newToolComponent)
        }
        item.itemMeta = meta
    }


    @Suppress("UnstableApiUsage")
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

    @Suppress("UnstableApiUsage")
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