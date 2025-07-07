package me.shadowalzazel.mcodyssey.common.smithing

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.api.EquipmentDataManager
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
internal interface ArmorUpgrading : EquipmentDataManager, AttributeManager, AdvancementManager {

    fun armorSmithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        val inputMaterial = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment ?: return
        if (getItemMaterialType(equipment) in SmithingMaps.NOT_UPGRADEABLE) return
        // Switch case
        when (inputMaterial.type) {
            Material.NETHERITE_INGOT -> return
            Material.IRON_INGOT -> customUpgrading(event)
            else -> {}
        }
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
        // Upgrade item
        val upgradedItem = armorUpgrader(item, upgradeMaterial)
        when (upgradeMaterial) {
            "iridium" -> {
                // MODIFY VALUES
                rewardAdvancement(event.viewers, "odyssey/smith_iridium")
            }
            "mithril" -> {
                rewardAdvancement(event.viewers, "odyssey/smith_mithril")
            }
            "soul_steel" -> {
                rewardAdvancement(event.viewers, "odyssey/smith_soul_steel")
            }
            "titanium", "anodized_titanium" -> {
                rewardAdvancement(event.viewers, "odyssey/smith_titanium")
            }
        }
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
    private fun armorUpgrader(item: ItemStack, upgradeMaterial: String): ItemStack {
        val armorType = getItemBaseToolName(item.type)!!
        val itemName = "${upgradeMaterial}_${armorType}"
        item.setStringTag(ItemDataTags.MATERIAL_TYPE, upgradeMaterial)
        // Copy Attributes
        val dataItem = Item.DataItem(itemName).newItemStack(1)
        item.copyAttributes(dataItem, false) // Glyphs are reset !!! Temp
        // Transfer components
        val transferable = listOf(DataComponentTypes.EQUIPPABLE, DataComponentTypes.ITEM_MODEL,
            DataComponentTypes.ITEM_NAME, DataComponentTypes.MAX_DAMAGE, DataComponentTypes.CUSTOM_NAME)
        transferComponents(item, dataItem, transferable)

        return item
    }

    /*
    private fun getArmorDurability(upgradeMaterial: String, armorType: String): Int? {
        val armorMaterialValue = SmithingMaps.ARMOR_DURABILITY_MAP[upgradeMaterial] ?: return null
        val baseDurability = mapOf("helmet" to 165, "chestplate" to 240, "leggings" to 225, "boots" to 195)
        return (baseDurability[armorType]!! * armorMaterialValue).toInt()
    }
     */

}