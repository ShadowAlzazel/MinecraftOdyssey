package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.common.smithing.CustomTrimming
import me.shadowalzazel.mcodyssey.common.smithing.SmithingMaps
import me.shadowalzazel.mcodyssey.common.smithing.ToolUpgrading
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.event.inventory.PrepareSmithingEvent

@Suppress("UnstableApiUsage")
object SmithingListeners : Listener, DataTagManager {

    private val TOOL_UPGRADING = ToolUpgrading()
    private val CUSTOM_TRIMMING = CustomTrimming()

    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        val result = event.result
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        // Get Ids
        val equipment = event.inventory.inputEquipment ?: return
        val addition = event.inventory.inputMineral ?: return
        val template = event.inventory.inputTemplate ?: return
        val equipmentId = equipment.getItemNameId()
        val additionId = addition.getItemNameId()
        val templateId = template.getItemNameId()
        // Custom Tools
        if (templateId in SmithingMaps.UPGRADE_TEMPLATES) {
            TOOL_UPGRADING.toolSmithingHandler(event)
        }
        // Trims
        else if (templateId in SmithingMaps.TRIM_TEMPLATES) {
            CUSTOM_TRIMMING.customTrimsHandler(event)
        }
        // Engraving
        else if (addition.type == Material.AMETHYST_SHARD && template.type == Material.PAPER) {
            CUSTOM_TRIMMING.customEngraving(event)
        }
    }


    @EventHandler
    fun furnaceFinishTemperItem(event: FurnaceSmeltEvent) {
        // Get matching vars
        if (event.block.type != Material.BLAST_FURNACE) return
        val input = event.source
        if (!input.hasItemMeta()) return
        val itemMeta = input.itemMeta
        if (!itemMeta.hasCustomModelData()) return
        if (input.getStringTag(ItemDataTags.MATERIAL_TYPE) != "titanium") return
        val result = input.clone()
        //upgradeModel(result, ItemModels.ANODIZED_TITANIUM_MATERIAL_PRE)
        event.result = result
    }


}
