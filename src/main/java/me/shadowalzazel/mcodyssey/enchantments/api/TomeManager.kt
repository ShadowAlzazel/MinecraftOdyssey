package me.shadowalzazel.mcodyssey.enchantments.api

import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable

internal interface TomeManager : EnchantSlotManager {

    // TODO!!! Not Gilded for Expenditure and Discharge

    fun tomeOfDischargeOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendBarMessage("This item needs to be slotted to use this tome.") }
            return null
        }
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()
        if (!meta.hasEnchants() && !hasStoredEnchants) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Remove Enchant
        if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            val enchantToRemove = storedMeta.enchants.toList().random()
            storedMeta.removeStoredEnchant(enchantToRemove.first)
            item.itemMeta = storedMeta
        } else {
            val enchantToRemove = item.enchantments.toList().random()
            item.removeEnchantment(enchantToRemove.first)
        }
        item.updateSlotLore()
        return item
    }

    fun tomeOfEmbraceOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendBarMessage("This item needs to be slotted to use this tome.") }
            return null
        }
        // Add Slot
        return item.apply {
            addEnchantSlot()
            updateSlotLore()
        }
    }

    fun tomeOfHarmonyOnItem(item: ItemStack): ItemStack? {
        val meta = item.itemMeta
        if (meta !is Repairable) return null
        meta.repairCost = 1
        item.itemMeta = meta
        item.updateSlotLore()
        return item
    }

    fun tomeOfPromotionOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()
        if (!meta.hasEnchants() && !hasStoredEnchants) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Get Enchant to Upgrade if available
        val availableEnchants = if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            storedMeta.enchants.toList().filter { it.second < it.first.maxLevel }
        } else {
            item.enchantments.toList().filter { it.second < it.first.maxLevel }
        }
        // Return if no available enchants
        if (availableEnchants.isEmpty()) {
            viewers.forEach { it.sendBarMessage("This item already has max level enchants!") }
            return null
        }
        // Upgrade Enchant
        val enchantToUpgrade = availableEnchants.random()
        if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            // Get max level
            val checkMax = minOf(enchantToUpgrade.first.maxLevel, enchantToUpgrade.second + 1)
            // Remove and re-add
            storedMeta.removeStoredEnchant(enchantToUpgrade.first)
            storedMeta.addStoredEnchant(enchantToUpgrade.first, checkMax, false)
            item.itemMeta = storedMeta
        } else {
            // Get max level
            val checkMax = minOf(enchantToUpgrade.first.maxLevel, enchantToUpgrade.second + 1)
            // Remove and re-add
            item.removeEnchantment(enchantToUpgrade.first)
            item.addEnchantment(enchantToUpgrade.first, checkMax)
        }
        item.updateSlotLore()
        return item
    }

    fun tomeOfReplicationOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()
        if (!hasStoredEnchants && item.type != Material.ENCHANTED_BOOK) {
            viewers.forEach { it.sendBarMessage("This tome can only be used on enchanted books with enchants!") }
            return null
        }
        return item.clone()
    }

    fun tomeOfImitationOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()
        if (!hasStoredEnchants && item.type != Material.ENCHANTED_BOOK) {
            viewers.forEach { it.sendBarMessage("This tome can only be used on enchanted books with enchants!") }
            return null
        }
        meta as EnchantmentStorageMeta
        val randomEnchant = meta.storedEnchants.toList().random()
        // Create new book
        val imitatedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        val newMeta = imitatedBook.itemMeta as EnchantmentStorageMeta
        newMeta.addStoredEnchant(randomEnchant.first, 1, false)
        imitatedBook.itemMeta = newMeta
        return imitatedBook
    }

    fun tomeOfExpenditureOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        if (!meta.hasEnchants()) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Get Extracted
        val extractedEnchant = item.enchantments.toList().random()
        val checkMax = minOf(extractedEnchant.first.maxLevel, extractedEnchant.second)
        // Create new book
        val extractedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        val newMeta = extractedBook.itemMeta as EnchantmentStorageMeta
        newMeta.addStoredEnchant(extractedEnchant.first, checkMax, false)
        extractedBook.itemMeta = newMeta
        return extractedBook // TODO !! Maybe switch to arcane
    }

    // Removes all enchants and gain XP! destroys item
    fun tomeOfAvariceOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()
        if (!meta.hasEnchants() && !hasStoredEnchants) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Get Enchant to Upgrade if available
        val allEnchants = if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            storedMeta.enchants.toList()
        } else {
            item.enchantments.toList()
        }
        var totalLevels = 0
        for (enchant in allEnchants) {
            totalLevels += enchant.second
        }
        return ItemStack(Material.BOOK, 1)
    }

}