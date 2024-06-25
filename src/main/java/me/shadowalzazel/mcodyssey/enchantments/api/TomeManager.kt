package me.shadowalzazel.mcodyssey.enchantments.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable

internal interface TomeManager : EnchantabilityHandler {

    fun tomeOfDischargeOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
        if (!meta.hasEnchants() && !hasStoredEnchants) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Remove Enchant
        val enchantToRemove: Pair<Enchantment, Int>
        if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            enchantToRemove = storedMeta.enchants.toList().random()
            storedMeta.removeStoredEnchant(enchantToRemove.first)
            item.itemMeta = storedMeta
        } else {
            enchantToRemove = item.enchantments.toList().random()
            item.removeEnchantment(enchantToRemove.first)
        }
        val removedEnchantsMap = mutableMapOf(enchantToRemove.first to enchantToRemove.second)
        item.updateEnchantabilityPointsLore(removedEnchants=removedEnchantsMap)
        return item
    }

    @Deprecated(message = "No more slots")
    fun tomeOfEmbraceOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        return null
    }

    fun tomeOfHarmonyOnItem(item: ItemStack): ItemStack? {
        val meta = item.itemMeta
        if (meta !is Repairable) return null
        meta.repairCost = 1
        item.itemMeta = meta
        item.updateEnchantabilityPointsLore()
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
        // Get Enchant
        val enchantToUpgrade = availableEnchants.random()
        // check over Enchantability point limit
        val checkedMaxLevel = minOf(enchantToUpgrade.first.maxLevel, enchantToUpgrade.second + 1)
        val maxEnchantabilityPoints = item.getMaxEnchantabilityPoints()
        val usedEnchantabilityPoints = item.getUsedEnchantabilityPoints()
        val potentialNewPoints = usedEnchantabilityPoints - getEnchantabilityCost(enchantToUpgrade) + getEnchantabilityCost(enchantToUpgrade, checkedMaxLevel)
        if (potentialNewPoints > maxEnchantabilityPoints) {
            viewers.forEach { it.sendBarMessage("The enchantment ${enchantToUpgrade.first.key} would be to expensive!") }
            return null
        }
        // Upgrade Enchant
        if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            // Remove and re-add
            storedMeta.removeStoredEnchant(enchantToUpgrade.first)
            storedMeta.addStoredEnchant(enchantToUpgrade.first, checkedMaxLevel, false)
            item.itemMeta = storedMeta
        } else {
            // Remove and re-add
            item.removeEnchantment(enchantToUpgrade.first)
            item.addEnchantment(enchantToUpgrade.first, checkedMaxLevel)
        }
        item.updateEnchantabilityPointsLore()
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
        newMeta.addStoredEnchant(randomEnchant.first, randomEnchant.second, false)
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
        return ItemStack(Material.BOOK, 1)
    }

    fun tomeOfAvaricePostEffect(item: ItemStack, viewers: List<HumanEntity>) {
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
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
            viewers.forEach {
                if (it is Player) {
                    it.giveExp(totalLevels * 50)
                    it.playSound(it.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5F, 0.9F)
                }
            }
        }
    }

    // Adds enchants from one item to another
    fun tomeOfPolymerizationOnItem(book: ItemStack, item: ItemStack, other: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        // Check book is enchantment storage
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isNotEmpty()
        // Get Enchant to Upgrade if available
        val itemEnchants = if (hasStoredEnchants) {
            val storedMeta = item.itemMeta as EnchantmentStorageMeta
            storedMeta.storedEnchants
        } else {
            item.enchantments
        }
        val otherEnchants = other.enchantments
        if (otherEnchants.isEmpty()) return null
        // Loop other
        for (enchant in otherEnchants) {
            val enchantment = enchant.key
            if (!enchantment.canEnchantItem(item)) continue
            // Conflict
            val hasConflict = itemEnchants.any { it.key.conflictsWith(enchantment) }
            if (hasConflict) continue
            // Is In List
            val otherLevel = minOf(enchantment.maxLevel, enchant.value)
            if (enchantment in itemEnchants.keys) {
                val itemLevel = itemEnchants[enchantment]!!
                itemEnchants[enchantment] = maxOf(otherLevel, itemLevel)
            } else {
                itemEnchants[enchantment] = otherLevel
            }
        }
        val result = item.clone()
        result.addEnchantments(itemEnchants)
        // Check max
        val maxEnchantabilityPoints = result.getMaxEnchantabilityPoints()
        val usedEnchantabilityPoints = result.getUsedEnchantabilityPoints()
        if (usedEnchantabilityPoints > maxEnchantabilityPoints) {
            viewers.forEach { it.sendBarMessage("There are too many enchantments to be applied!") }
            return null
        }
        return result
    }

    @Deprecated(message = "No more slots")
    fun tomeOfBanishmentOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        return null
    }


    /*-----------------------------------------------------------------------------------------------*/
    // Fail Message
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = SlotColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}