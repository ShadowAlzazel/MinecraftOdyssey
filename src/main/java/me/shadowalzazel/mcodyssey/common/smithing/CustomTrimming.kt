package me.shadowalzazel.mcodyssey.common.smithing

import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim

class CustomTrimming : DataTagManager {

    fun customTrimsHandler(event: PrepareSmithingEvent) {
        // Get IDs
        val trimMaterial = event.inventory.inputMineral ?: return
        val trimTemplate = event.inventory.inputTemplate ?: return
        val materialId = trimMaterial.getItemIdentifier() ?: trimMaterial.type.name.lowercase()
        val patternId = trimTemplate.getItemIdentifier() ?: trimMaterial.type.name.lowercase()
        // Item
        val equipment = event.inventory.inputEquipment ?: return
        val meta = equipment.itemMeta as ArmorMeta
        // Match
        val finalMaterial = SmithingMaps.TRIM_MATERIAL_FROM_ITEM_MAP[materialId] ?: return
        val finalPattern = SmithingMaps.PATTERN_FROM_ITEM_MAP[patternId] ?: return
        meta.trim = ArmorTrim(finalMaterial, finalPattern)
        // Finish
        val result = equipment.clone()
        result.itemMeta = meta
        event.result = result

    }



}