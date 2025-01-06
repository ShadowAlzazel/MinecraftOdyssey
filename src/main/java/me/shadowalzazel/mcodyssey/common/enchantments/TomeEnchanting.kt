package me.shadowalzazel.mcodyssey.common.enchantments

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable

@Suppress("UnstableApiUsage")
internal interface TomeEnchanting : EnchantabilityHandler, AdvancementManager {

    fun tomeOfDischargeOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val itemEnchants = item.getData(DataComponentTypes.ENCHANTMENTS)
        val storedEnchants = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)
        if (itemEnchants == null && storedEnchants == null) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Find an enchant to remove
        val enchantToRemove = storedEnchants?.enchantments()?.toList()?.random() ?: itemEnchants?.enchantments()?.toList()?.random()
        if (enchantToRemove == null) {
            viewers.forEach { it.sendBarMessage("This item needs to have enchantments to use this tome.") }
            return null
        }
        // Advancement
        if (enchantToRemove.first.isCursed) {
            rewardAdvancement(viewers, "odyssey:odyssey/discharge_a_curse")
        }
        // Remove enchantment
        if (itemEnchants != null) {
            val enchantmentMap = itemEnchants.enchantments().toMutableMap()
            enchantmentMap.remove(enchantToRemove.first)
            val newEnchantments = ItemEnchantments.itemEnchantments(enchantmentMap,false)
            item.setData(DataComponentTypes.ENCHANTMENTS, newEnchantments)
        }
        if (storedEnchants != null) {
            val enchantmentMap = storedEnchants.enchantments().toMutableMap()
            enchantmentMap.remove(enchantToRemove.first)
            val newEnchantments = ItemEnchantments.itemEnchantments(enchantmentMap,false)
            item.setData(DataComponentTypes.STORED_ENCHANTMENTS, newEnchantments)
        }

        item.updateEnchantPoints()
        return item
    }

    fun tomeOfHarmonyOnItem(item: ItemStack): ItemStack? {
        val meta = item.itemMeta
        if (meta !is Repairable) return null
        meta.repairCost = 1
        item.itemMeta = meta
        item.updateEnchantPoints()
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
            storedMeta.storedEnchants.toList().filter { it.second < it.first.maxLevel }
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
        if (potentialNewPoints > maxEnchantabilityPoints && !hasStoredEnchants) {
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
            // Set advancement from datapack
        } else {
            // Remove and re-add
            item.removeEnchantment(enchantToUpgrade.first)
            item.addEnchantment(enchantToUpgrade.first, checkedMaxLevel)
            item.updateEnchantPoints()
        }
        // Advancement
        if (checkedMaxLevel >= enchantToUpgrade.first.maxLevel) {
            viewers.forEach {
                if (it is Player) {
                    val advancement = it.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/use_promotion_tome")!!)
                    if (advancement != null) {
                        it.getAdvancementProgress(advancement).awardCriteria("requirement")
                    }
                }
            }
        }
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
        // Advancement
        if (randomEnchant.second >= randomEnchant.first.maxLevel) {
            viewers.forEach {
                if (it is Player) {
                    val advancement = it.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/use_imitation_tome")!!)
                    if (advancement != null) {
                        it.getAdvancementProgress(advancement).awardCriteria("requirement")
                    }
                }
            }
        }
        return imitatedBook
    }

    // Extracts an enchantment from an item, without destroying it
    fun tomeOfExtractionOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        // Sentries
        val storedEnchantments = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        val itemEnchantments = item.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        if (itemEnchantments.isNullOrEmpty() && storedEnchantments.isNullOrEmpty()) {
            viewers.forEach { it.sendBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        // Depends on what data type
        if (!itemEnchantments.isNullOrEmpty()) {
            // Get enchantment
            val extractedEnchant = itemEnchantments.toList().random()
            val checkMax = minOf(extractedEnchant.first.maxLevel, extractedEnchant.second)
            // New Book
            val extractedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
            val bookEnchantment = ItemEnchantments.itemEnchantments().add(extractedEnchant.first, checkMax)
            extractedBook.setData(DataComponentTypes.STORED_ENCHANTMENTS, bookEnchantment)
            // Return book
            extractedBook.updateEnchantPoints()
            return extractedBook
        }
        else if (!storedEnchantments.isNullOrEmpty()) {
            // Get enchantment
            val extractedEnchant = storedEnchantments.toList().random()
            val checkMax = minOf(extractedEnchant.first.maxLevel, extractedEnchant.second)
            // New Book
            val extractedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
            val bookEnchantment = ItemEnchantments.itemEnchantments().add(extractedEnchant.first, checkMax)
            extractedBook.setData(DataComponentTypes.STORED_ENCHANTMENTS, bookEnchantment)
            // Return book
            extractedBook.updateEnchantPoints()
            return extractedBook
        }
        return null
    }

    fun tomeOfExtractionPostEffect(item: ItemStack, book: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val storedEnchantments = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        val itemEnchantments = item.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        // Get Extracted Enchant
        val extractedEnchant = book.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()?.toList()?.first() ?: return null

        // Remove from old
        if (!itemEnchantments.isNullOrEmpty()) {
            val enchantmentMap = itemEnchantments.toMutableMap()
            enchantmentMap.remove(extractedEnchant.first)
            val newEnchantments = ItemEnchantments.itemEnchantments(enchantmentMap,false)
            item.setData(DataComponentTypes.ENCHANTMENTS, newEnchantments)
        }
        if (!storedEnchantments.isNullOrEmpty()) {
            val enchantmentMap = storedEnchantments.toMutableMap()
            enchantmentMap.remove(extractedEnchant.first)
            val newEnchantments = ItemEnchantments.itemEnchantments(enchantmentMap,false)
            item.setData(DataComponentTypes.STORED_ENCHANTMENTS, newEnchantments)
        }
        item.updateEnchantPoints()
        return item
    }

    fun tomeOfExpenditureOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        // Sentries
        val storedEnchantments = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        val itemEnchantments = item.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        if (itemEnchantments == null && storedEnchantments == null) {
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
        return extractedBook
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

    /*-----------------------------------------------------------------------------------------------*/
    // Fail Message
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = CustomColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}