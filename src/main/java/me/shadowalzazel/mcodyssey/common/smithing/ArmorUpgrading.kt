package me.shadowalzazel.mcodyssey.common.smithing

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.api.EquipmentDataManager
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
internal interface ArmorUpgrading : DataTagManager, AttributeManager, AdvancementManager {

    fun armorSmithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        val inputMaterial = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment ?: return
        if (EquipmentDataManager.getEquipmentMaterial(equipment) in SmithingMaps.NOT_UPGRADEABLE) return
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
        if (SmithingMaps.TEMPLATE_INPUT_MAP[templateId] != inputMaterialId) return
        // Get upgrade path from the inputMaterial
        val upgradeMaterial = SmithingMaps.MATERIAL_UPGRADE_MAP[inputMaterialId] ?: return
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
            "titanium" -> {
                rewardAdvancement(event.viewers, "odyssey/smith_titanium")
            }
        }
        // Finish
        event.result = upgradedItem
        event.inventory.result = upgradedItem
    }

    // Main method to upgrade
    private fun armorUpgrader(item: ItemStack, upgradeMaterial: String): ItemStack {
        val armorType = EquipmentDataManager.getDefaultType(item.type)!!
        val itemName = "${upgradeMaterial}_${armorType}"
        item.setStringTag(ItemDataTags.MATERIAL_TYPE, upgradeMaterial)
        /*
        val newModel = createOdysseyKey(itemName)
        item.setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
        item.setData(DataComponentTypes.ITEM_MODEL, newModel)
        val maxDamage = getArmorDurability(upgradeMaterial, armorType)
        if (maxDamage != null) item.setData(DataComponentTypes.MAX_DAMAGE, maxDamage)
         */
        // Copy Attributes
        val dataItem = Item.DataItem(itemName).newItemStack(1)
        item.copyAttributes(dataItem, true)
        // Transfer components
        val transferable = listOf(DataComponentTypes.EQUIPPABLE, DataComponentTypes.ITEM_MODEL,
            DataComponentTypes.ITEM_NAME, DataComponentTypes.MAX_DAMAGE, DataComponentTypes.CUSTOM_NAME)
        transferComponents(item, dataItem, transferable)


        /*
       val templateAttributes = dataItem.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()
       val existingAttributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()
       // Loop through existing attributes to get the key and match to the Data Item
       if (templateAttributes != null && existingAttributes != null) {
           val builder = ItemAttributeModifiers.itemAttributes()
           for (modifier in existingAttributes) {
               // If modifier key not in DataItem modifiers
               val modifierKey = modifier.modifier().key
               val foundModifier = templateAttributes.find { it.modifier().key == modifierKey }
               if (foundModifier != null) {
                   builder.addModifier(foundModifier.attribute(), foundModifier.modifier())
               } else {
                   builder.addModifier(modifier.attribute(), modifier.modifier())
               }
           }
           item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, builder)
       }

        */
        return item
    }


    private fun getArmorDurability(upgradeMaterial: String, armorType: String): Int? {
        val armorMaterialValue = SmithingMaps.ARMOR_DURABILITY_MAP[upgradeMaterial] ?: return null
        val baseDurability = mapOf("helmet" to 165, "chestplate" to 240, "leggings" to 225, "boots" to 195)
        return (baseDurability[armorType]!! * armorMaterialValue).toInt()
    }


}