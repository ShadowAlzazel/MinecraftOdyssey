package me.shadowalzazel.mcodyssey.arcane

import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.listeners.EnchantingListeners.addEnchantSlot
import me.shadowalzazel.mcodyssey.listeners.EnchantingListeners.isSlotted
import me.shadowalzazel.mcodyssey.listeners.EnchantingListeners.sendFailMessage
import me.shadowalzazel.mcodyssey.listeners.EnchantingListeners.updateSlotLore
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable

internal interface TomeManager : EnchantSlotManager {

    /*-----------------------------------------------------------------------------------------------*/

    fun tomeOfDischargeOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendFailMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        val meta = item.itemMeta
        if (!meta.hasEnchants() || meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()) {
            viewers.forEach { it.sendFailMessage("This item needs to be enchanted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        val enchantments = if (meta is EnchantmentStorageMeta) { meta.storedEnchants } else { meta.enchants }
        // Remove Enchantment
        val enchantToRemove = enchantments.toList().random()
        return item.apply {
            if (meta is EnchantmentStorageMeta) {
                meta.removeStoredEnchant(enchantToRemove.first)
            }
            else {
                meta.removeEnchant(enchantToRemove.first)
            }
            itemMeta = meta
            updateSlotLore()
        }
    }

    private fun tomeOfEmbraceOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendFailMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        // Add Slot
        return item.apply {
            addEnchantSlot()
            updateSlotLore()
        }
    }

    private fun tomeOfHarmonyOnItem(item: ItemStack): ItemStack {
        return item.apply {
            itemMeta = itemMeta.also {
                if (it is Repairable) it.repairCost = 1
            }
        }
    }

    private fun tomeOfReplicationOnBook(item: ItemStack): ItemStack {
        return if (item.itemMeta.hasEnchants() || (item.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            item.clone()
        } else {
            Miscellaneous.TOME_OF_REPLICATION.createItemStack(1)
        }
    }

}