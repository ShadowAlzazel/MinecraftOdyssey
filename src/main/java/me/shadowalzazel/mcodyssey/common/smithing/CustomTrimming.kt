@file:Suppress("removal")

package me.shadowalzazel.mcodyssey.common.smithing

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemArmorTrim
import io.papermc.paper.datacomponent.item.ItemLore
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.util.MessageHandler.sendBarMessage
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim

@Suppress("UnstableApiUsage")
internal interface CustomTrimming : DataTagManager, RegistryTagManager {

    // BIOME SPECIFIC SETS !!

    fun customTrimsHandler(event: PrepareSmithingEvent) {
        // Get IDs
        val trimMaterial = event.inventory.inputMineral ?: return
        val trimTemplate = event.inventory.inputTemplate ?: return
        val materialId = trimMaterial.getItemNameId()
        val patternId = trimTemplate.getItemNameId()
        //println("TRIM ID: $patternId, MATERIAL ID: $materialId")
        // Item
        val equipment = event.inventory.inputEquipment ?: return
        val item = equipment.clone()
        // Find Armor or Weapon Trim
        val toolType = item.getStringTag(ItemDataTags.TOOL_TYPE)
        val isWeapon = toolType != null
        val finalPattern = if (isWeapon) {
            SmithingMaps.WEAPON_TRIM_PATTERNS_FROM_ITEM[patternId]
        } else {
            SmithingMaps.ARMOR_TRIM_PATTERNS_FROM_ITEM[patternId]
        }
        if (finalPattern == null) return
        val finalMaterial = SmithingMaps.TRIM_MATERIAL_FROM_ITEM_MAP[materialId] ?: return
        val newTrim = ItemArmorTrim.itemArmorTrim(ArmorTrim(finalMaterial, finalPattern))
        item.setData(DataComponentTypes.TRIM, newTrim)
        // Check if weapon to add model data
        if (isWeapon) {
            val trimKey = getPaperRegistry(RegistryKey.TRIM_PATTERN).getKey(finalPattern)!!
            val trimName = "${trimKey.key}_trim"
            item.modifyToolCustomModel(5, trimName)
        }
        // Finish
        event.result = item
        event.inventory.result = item
    }

    fun customPartUpgrading(event: PrepareSmithingEvent) {
        // Get IDs
        val partItem = event.inventory.inputMineral ?: return
        val patternItem = event.inventory.inputTemplate ?: return
        val partId = partItem.getItemNameId()
        val patternId = patternItem.getItemNameId()
        // Item
        val equipment = event.inventory.inputEquipment ?: return
        val item = equipment.clone()
        // Get Values
        val finalPattern = SmithingMaps.PATTERN_FROM_ITEM[patternId] ?: return
        val finalPart = SmithingMaps.PART_FROM_ITEM[partId] ?: return
        val customPart = "${finalPattern}_${finalPart}"
        val index = SmithingMaps.MODEL_DATA_MAP[finalPart] ?: return
        // Get weapon type or return,
        item.modifyToolCustomModel(index, customPart)
        // Finish
        event.result = item
        event.inventory.result = item
    }

    private fun ItemStack.modifyToolCustomModel(index: Int, text: String) {
        val toolType = this.getStringTag(ItemDataTags.TOOL_TYPE) ?: "generic"
        val oldModelData = this.getData(DataComponentTypes.CUSTOM_MODEL_DATA)
        val weaponParts = oldModelData?.strings()?.toMutableList() ?: mutableListOf(toolType, "blade", "handle", "hilt", "pommel", "no_trim")
        // Set
        weaponParts[index] = text
        val customData = CustomModelData.customModelData().addStrings(weaponParts)
        if (oldModelData != null) { // Copy from ol
            customData.addFlags(oldModelData.flags())
            customData.addFloats(oldModelData.floats())
            customData.addColors(oldModelData.colors())
        }
        this.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customData)
    }


    fun customEngraving(event: PrepareSmithingEvent) {
        // Get Ids
        val equipment = event.inventory.inputEquipment ?: return
        //val addition = event.inventory.inputMineral ?: return
        //val template = event.inventory.inputTemplate ?: return
        // Checks
        val isEngraved = equipment.hasTag(ItemDataTags.IS_ENGRAVED)
        if (isEngraved) {
            event.viewers.forEach { it.sendBarMessage("This Item Is Already Engraved!", CustomColors.GRAY.color) }
            event.result = ItemStack(Material.AIR)
            return
        }
        if (event.viewers.isEmpty()) return
        val engraver = event.viewers.first()
        // Apply
        val item = equipment.clone()
        item.addTag(ItemDataTags.IS_ENGRAVED)
        item.setStringTag(ItemDataTags.ENGRAVED_BY, engraver.name)
        val pretext = "Created"
        val engraving = Component.text("$pretext by ${engraver.name}", CustomColors.DARK_GRAY.color, TextDecoration.ITALIC)
        val oldLore = item.getData(DataComponentTypes.LORE)
        val newLore = if (oldLore != null) ItemLore.lore(oldLore.lines()) else ItemLore.lore(mutableListOf(engraving))
        item.setData(DataComponentTypes.LORE, newLore)
        // Finish
        event.result = item
    }

}