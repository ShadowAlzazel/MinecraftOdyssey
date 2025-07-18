package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.common.arcane.RuneInscribing
import me.shadowalzazel.mcodyssey.common.smithing.ArmorUpgrading
import me.shadowalzazel.mcodyssey.common.smithing.CustomTrimming
import me.shadowalzazel.mcodyssey.common.smithing.SmithingMaps
import me.shadowalzazel.mcodyssey.common.smithing.ToolUpgrading
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.event.inventory.SmithItemEvent

object SmithingListeners : Listener, CustomTrimming, ToolUpgrading, ArmorUpgrading, RuneInscribing {

    //private val TOOL_UPGRADING = ToolUpgrading()
    //private val CUSTOM_TRIMMING = CustomTrimming()

    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        //val result = event.result
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        // Get Ids
        val equipment = event.inventory.inputEquipment ?: return
        val addition = event.inventory.inputMineral ?: return
        val template = event.inventory.inputTemplate ?: return
        //val equipmentId = equipment.getItemNameId()
        val additionId = addition.getItemNameId()
        val templateId = template.getItemNameId()
        // Custom Upgrades
        if (templateId in SmithingMaps.UPGRADE_TEMPLATES) {
            if (itemIsArmor(equipment.type)) armorSmithingHandler(event)
            else toolSmithingHandler(event)
        }
        // Trims
        else if (templateId in SmithingMaps.TRIM_TEMPLATES) {
            // Check if tool or weapon trim
            customTrimsHandler(event)
        }
        // Tool Parts
        else if (templateId.contains("part_pattern") && additionId.contains("part_upgrade")) {
            customPartUpgrading(event)
        }
        // Engraving
        else if (addition.type == Material.AMETHYST_SHARD && template.type == Material.PAPER) {
            customEngraving(event)
        }
        // Rune Inscribing
        else if (addition.type == Material.INK_SAC
            && template.type == Material.PAPER
            && equipment.type == Material.BUNDLE) {
            smithingRuneInscribing(event)
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun smithingDoneHandler(event: SmithItemEvent) {
        val smithingInventory = event.inventory
        val result = smithingInventory.result ?: return
        val template = smithingInventory.inputTemplate ?: return
        val mineral = smithingInventory.inputMineral ?: return
        val equipment = smithingInventory.inputEquipment ?: return
        val player = event.whoClicked

        if (equipment.getItemNameId() == "arcane_pen") {
            val overflow = player.inventory.addItem(equipment.clone())
            if (overflow.isNotEmpty()) {
                player.world.dropItem(player.location, overflow[0]!!)
            }
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
