package me.shadowalzazel.mcodyssey.common.smithing

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.api.EquipmentDataManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.ToolComponentHelper
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
internal interface ToolUpgrading : EquipmentDataManager, ToolComponentHelper, AdvancementManager {

    fun toolSmithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        val inputMaterial = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment ?: return
        if (getEquipmentMaterial(equipment) in SmithingMaps.NOT_UPGRADEABLE) return
        // Switch case
        when (inputMaterial.type) {
            Material.NETHERITE_INGOT -> netheriteUpgrading(event)
            Material.IRON_INGOT -> customUpgrading(event)
            else -> {}
        }
    }


    private fun netheriteUpgrading(event: PrepareSmithingEvent) {
        val equipment = event.inventory.inputEquipment ?: return
        if (equipment.getData(DataComponentTypes.ITEM_MODEL) == null) return
        val upgradeMaterial = "netherite"
        val itemType = event.result!!.type
        val upgradedItem = toolUpgrader(equipment.withType(itemType), upgradeMaterial)
        modifyDamage(upgradedItem, 1.0)
        event.result = upgradedItem
    }


    private fun customUpgrading(event: PrepareSmithingEvent) {
        event.result = ItemStack(Material.AIR)
        val equipment = event.inventory.inputEquipment ?: return
        val item = equipment.clone()
        // Get ids
        val inputMaterialId = event.inventory.inputMineral?.getItemIdentifier() ?: return
        val templateId = event.inventory.inputTemplate?.getItemIdentifier() ?: return
        // Cross-check
        if (SmithingMaps.GET_TEMPLATE_FROM_MATERIAL[inputMaterialId] != templateId) return
        // Get upgrade path from the inputMaterial
        val upgradeMaterial = SmithingMaps.GET_UPGRADE_PATH[inputMaterialId] ?: return
        if (!itemIsUpgradeable(item, upgradeMaterial)) return
        // Upgrade
        val upgradedItem = toolUpgrader(item, upgradeMaterial)
        when (upgradeMaterial) {
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
            "titanium", "anodized_titanium" -> {
                modifyDamage(upgradedItem, 1.0)
                modifyAttackSpeed(upgradedItem, 1.1)
                rewardAdvancement(event.viewers, "odyssey/smith_titanium")
            }
        }
        // Add ToolComponent
        val mineableTags = getMiningTags(getEquipmentType(upgradedItem) ?: "none")
        if (mineableTags != null) toolMiningUpgrader(upgradedItem, upgradeMaterial)
        // Finish
        event.result = upgradedItem
        event.inventory.result = upgradedItem
    }

    private fun itemIsUpgradeable(item: ItemStack, path: String): Boolean {
        if (path in listOf("iridium", "mithril") && !equipmentIsDiamond(item.type)) return false
        if (path in listOf("titanium", "anodized_titanium", "soul_steel") && !equipmentIsIron(item.type)) return false
        return true
    }

    // Main method to upgrade
    private fun toolUpgrader(item: ItemStack, upgradeMaterial: String): ItemStack {
        val toolType = getEquipmentType(item)!!
        val itemName = "${upgradeMaterial}_${toolType}"
        val newModel = createOdysseyKey(itemName)
        item.setStringTag(ItemDataTags.MATERIAL_TYPE, upgradeMaterial)
        item.setStringTag(ItemDataTags.TOOL_TYPE, toolType)
        item.setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
        item.setData(DataComponentTypes.ITEM_MODEL, newModel)
        val maxDamage = SmithingMaps.TOOL_MATERIALS[upgradeMaterial]?.maxDurability
        if (maxDamage != null) item.setData(DataComponentTypes.MAX_DAMAGE, maxDamage)
        return item
    }

    private fun toolMiningUpgrader(item: ItemStack, upgradeMaterial: String) {
        val toolType = getEquipmentType(item) ?: return
        val newToolComponent = newToolComponent(upgradeMaterial, toolType)
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