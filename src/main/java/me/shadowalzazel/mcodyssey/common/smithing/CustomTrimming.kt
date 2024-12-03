package me.shadowalzazel.mcodyssey.common.smithing

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemArmorTrim
import io.papermc.paper.datacomponent.item.ItemLore
import me.shadowalzazel.mcodyssey.api.MessageHandler.sendBarMessage
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim

@Suppress("UnstableApiUsage")
class CustomTrimming : DataTagManager {

    fun customTrimsHandler(event: PrepareSmithingEvent) {
        // Get IDs
        val trimMaterial = event.inventory.inputMineral ?: return
        val trimTemplate = event.inventory.inputTemplate ?: return
        val materialId = trimMaterial.getItemIdentifier() ?: trimMaterial.type.name.lowercase()
        val patternId = trimTemplate.getItemIdentifier() ?: trimMaterial.type.name.lowercase()
        // Item
        val equipment = event.inventory.inputEquipment ?: return
        // Match
        val item = equipment.clone()
        val finalMaterial = SmithingMaps.TRIM_MATERIAL_FROM_ITEM_MAP[materialId] ?: return
        val finalPattern = SmithingMaps.PATTERN_FROM_ITEM_MAP[patternId] ?: return
        val newTrim = ItemArmorTrim.itemArmorTrim(ArmorTrim(finalMaterial, finalPattern))
        item.setData(DataComponentTypes.TRIM, newTrim)
        // Finish
        event.result = item
    }

    fun customEngraving(event: PrepareSmithingEvent) {
        // Get Ids
        val equipment = event.inventory.inputEquipment ?: return
        val addition = event.inventory.inputMineral ?: return
        val template = event.inventory.inputTemplate ?: return
        // Checks
        val isEngraved = equipment.hasTag(ItemDataTags.IS_ENGRAVED)
        if (isEngraved) {
            event.viewers.forEach { it.sendBarMessage("This Item Is Already Engraved!") }
            event.result = ItemStack(Material.AIR)
            return
        }
        if (event.viewers.isEmpty()) return
        val engraver = event.viewers.first()
        // Apply
        val item = equipment.clone()
        item.setTag(ItemDataTags.IS_ENGRAVED)
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