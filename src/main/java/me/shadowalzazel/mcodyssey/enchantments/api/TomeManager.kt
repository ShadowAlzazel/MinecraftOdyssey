package me.shadowalzazel.mcodyssey.enchantments.api

import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
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
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
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
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
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
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
        val isBook = item.type == Material.ENCHANTED_BOOK || item.type == Material.BOOK
        if (!hasStoredEnchants && !isBook) {
            viewers.forEach { it.sendBarMessage("This tome can only be used on enchanted books with enchants!") }
            return null
        }
        return item.clone()
    }

    fun tomeOfImitationOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
        val isBook = item.type == Material.ENCHANTED_BOOK || item.type == Material.BOOK
        if (!hasStoredEnchants && isBook) {
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
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
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
            totalLevels += enchant.second // TODO!!
            viewers.forEach { if (it is Player) it.giveExp(totalLevels * 100) } //IDk?
        }
        return ItemStack(Material.BOOK, 1)
    }

    // Adds all enchants at no cost
    fun tomeOfPolymerizationOnItem(book: ItemStack, item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendBarMessage("This item needs to be slotted to use this tome.") }
            return null
        }
        // Check book is enchantment storage
        val bookMeta = book.itemMeta
        val hasStoredEnchants = bookMeta is EnchantmentStorageMeta && bookMeta.storedEnchants.isNotEmpty()
        if (!hasStoredEnchants) {
            viewers.forEach { it.sendBarMessage("This tome needs to have at least one enchantment stored!") }
            return null
        }
        bookMeta as EnchantmentStorageMeta
        // Get Item Enchants
        val itemIsBook = item.itemMeta is EnchantmentStorageMeta
        val itemEnchants = if (itemIsBook) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            storedMeta.enchants.toMutableMap()
        } else {
            item.enchantments.toMutableMap()
        }
        // Add to new meta
        var newEnchantCount = 0
        val oldEnchantments = itemEnchants.keys
        val currentCount = oldEnchantments.size
        for (enchant in bookMeta.storedEnchants) {
            if (enchant.key !in oldEnchantments) newEnchantCount += 1 // Add
            itemEnchants[enchant.key] = enchant.value // DOES NOT COMBINE MAX
        }
        // Can not pass max!
        if (item.getEnchantSlots() < newEnchantCount + currentCount) {
            viewers.forEach { it.sendBarMessage("This item does not have enough slots to add all enchantments!") }
            return null
        }

        item.addEnchantments(itemEnchants.toMap())
        item.updateSlotLore()
        return item
    }

    fun tomeOfBanishmentOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendBarMessage("This item needs to be slotted to use this tome.") }
            return null
        }
        // Add Slot
        return item.apply {
            removeEnchantSlot()
            updateSlotLore()
        }
    }


}