package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.EnchantRegistryManager
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.tuple.MutablePair
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable
import kotlin.math.max

object EnchantingListeners : Listener, EnchantSlotManager, EnchantRegistryManager, ItemCreator {

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun grindstoneHandler(event: PrepareResultEvent) {
        if (event.inventory !is GrindstoneInventory) return
        val inventory: GrindstoneInventory = event.inventory as GrindstoneInventory
        if (inventory.result == null) return
        if (inventory.upperItem?.enchantments == null && inventory.lowerItem?.enchantments == null) return
        val result = inventory.result!!
        val slotted = result.isSlotted()
        if (!slotted) return
        // Sentries passed
        event.result = result.clone().also { it.updateSlotLore() }
    }

    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun anvilHandler(event: PrepareAnvilEvent) {
        // Handled natively by class
        // - Conflicts / Adding E-Book to slotted / Renaming
        if (event.inventory.firstItem == null) return
        val first = event.inventory.firstItem ?: return
        val firstIsSlotted = first.isSlotted()
        val viewers = event.viewers
        val second = event.inventory.secondItem
        // Create Variables to detect conditions
        val firstHasGildedEnchants = if (first.itemMeta is EnchantmentStorageMeta) {
            (first.itemMeta as EnchantmentStorageMeta).enchants.any { it.key.isOdysseyEnchant() }
        } else {
            first.itemMeta.enchants.any { it.key.isOdysseyEnchant() }
        }
        val secondHasGildedEnchants = if (second?.itemMeta is EnchantmentStorageMeta) {
            (second.itemMeta as EnchantmentStorageMeta).enchants.any { it.key.isOdysseyEnchant() }
        } else {
            second?.itemMeta?.enchants?.any { it.key.isOdysseyEnchant() }
        }
        val firstIsBook = first.type == Material.ENCHANTED_BOOK
        val firstIsGildedBook = firstHasGildedEnchants && firstIsBook
        val secondIsBook = second?.type == Material.ENCHANTED_BOOK
        val secondIsGildedBook = secondHasGildedEnchants == true && secondIsBook
        // Prevent Gilded Book + Enchanted Book [MAYBE make VOLUME tomes]
        if ((firstIsGildedBook && !secondIsGildedBook && secondIsBook) || (secondIsGildedBook && !firstIsGildedBook && firstIsBook)) {
            viewers.forEach { it.sendFailMessage("Can not combine Gilded Books and Enchanted Books!") }
            event.result = ItemStack(Material.AIR)
            return
        }
        // Skip Event if just two Enchanted Books
        if (!firstIsGildedBook && !secondIsGildedBook && (firstIsBook && secondIsBook)) {
            return
        }
        // Check Gilded Books
        if (firstIsGildedBook && secondIsGildedBook) {
            val firstStoredMeta = first.itemMeta as EnchantmentStorageMeta
            val secondStoredMeta = second!!.itemMeta as EnchantmentStorageMeta
            // Somehow the gilded book got more than 1 enchant
            if (firstStoredMeta.storedEnchants.size > 1) {
                viewers.forEach { it.sendFailMessage("The first Gilded Book has more than 1 enchantment!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            // Somehow the gilded book got more than 1 enchant
            if (secondStoredMeta.storedEnchants.size > 1) {
                viewers.forEach { it.sendFailMessage("The second Gilded Book has more than 1 enchantment!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            val matching = firstStoredMeta.storedEnchants.keys.first() == secondStoredMeta.storedEnchants.keys.first()
            // If not matching set to air
            if (!matching) {
                viewers.forEach { it.sendFailMessage("Can not combine Gilded Books with different enchantments!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            // IF matching update lore,
            else {
                val enchant = firstStoredMeta.storedEnchants.keys.first()
                event.result = event.result?.apply {
                    val meta = itemMeta as EnchantmentStorageMeta
                    val newLevel = meta.storedEnchants[enchant]!!
                    val changedLore = mutableListOf(
                        enchant.convertToOdysseyEnchant().getTextForLore(newLevel)) + Component.text("") +
                            enchant.convertToOdysseyEnchant().getDescriptionToolTip(newLevel)
                    /*
                     val changedLore = lore() ?: mutableListOf()
                    val oldLevel = minOf(firstStoredMeta.storedEnchants[enchant]!!, secondStoredMeta.storedEnchants[enchant]!!)
                    val index = changedLore.indexOf(enchant.convertToOdysseyEnchant().getTextForLore(oldLevel))
                    val newLevel = meta.storedEnchants[enchant]!!
                    changedLore[index] = enchant.convertToOdysseyEnchant().getTextForLore(newLevel)
                    changedLore[index + 1]
                     */
                    meta.lore(changedLore)
                    itemMeta = meta
                }
                return
            }
        }
        // EventResult Null Check
        if (event.result == null) return
        if (second == null) return
        // Assumes that NMS is present and transfer enchantments
        val result = event.result ?: return
        // Activating Slots First Time {WIP fix exploit of adding 3 gilded to a non slotted to get 4]
        if (!firstIsSlotted) {
            val slots = MutablePair(2, 1)
            for (enchant in event.result!!.enchantments) {
                if (!enchant.key.isOdysseyEnchant()) {
                    slots.left += 1
                } else {
                    slots.right += 1
                }
            }
            // Lore
            event.result = event.result!!.apply {
                setSlots(slots.toPair())
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
                updateSlotLore(event.result!!.enchantments)
            }
            return
        }
        // If slotted
        else {
            // Current gilded
            val currentGildedEnchants: Map<Enchantment, Int> = first.enchantments.filter { it.key.isOdysseyEnchant()}
            val resultEnchants = result.enchantments
            // Conflict Checker
            for (enchant in resultEnchants.keys) { // FIX!!!!!!!!!!
                var conflictingEnchant: Enchantment? = null
                val foundConflict = currentGildedEnchants.keys.any {
                    conflictingEnchant = it
                    enchant.conflictsWith(it) && it != enchant
                }
                // If found conflict set to air
                if (foundConflict) {
                    val enchantName = enchant.key.key
                    viewers.forEach { it.sendFailMessage("The enchantment [$enchantName] conflicts with the enchantment [${conflictingEnchant!!.key.key}]") }
                    event.result = ItemStack(Material.AIR)
                    return
                }
            }
            // Check slot sizes
            val gildedSlots = result.getGildedSlots()
            val gildedEnchants = result.enchantments.filter { it.key.isOdysseyEnchant() }
            if (gildedSlots < gildedEnchants.size) {
                viewers.forEach { it.sendFailMessage("There are not enough empty 'Gilded Slots' on the first item!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            val enchantSlots = result.getEnchantSlots()
            val nonGildedEnchants = result.enchantments.filter { it.key.isNotOdysseyEnchant() }
            if (enchantSlots < nonGildedEnchants.size) {
                viewers.forEach { it.sendFailMessage("There are not enough empty 'Enchant Slots' on the first item!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            event.result = event.result!!.apply {
                //val enchantsToAdd = first.enchantments.filter { it.key.isOdysseyEnchant() && it.key !in enchantments.keys }
                //addUnsafeEnchantments(enchantsToAdd)
                updateSlotLore()
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun enchantingTableHandler(event: EnchantItemEvent) {
        when (event.item.type) {
            Material.BOOK -> {
                if (event.item.itemMeta.hasCustomModelData()) {
                    enchantingBookHandler(event)
                }
            }
            else -> {
                enchantingItemHandler(event)
            }
        }
    }

    private fun enchantingBookHandler(event: EnchantItemEvent) {
        when (event.item.itemMeta.customModelData) {
            ItemModels.VOLUME_OF_FARM_TOOLS -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_AXES -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_AXE, 1)) }
            }
            ItemModels.VOLUME_OF_SWORDS -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_POLE_ARMS -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_HOE, 1)) }
            }
            ItemModels.VOLUME_OF_SPEARS -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SHOVEL, 1)) }
            }
            ItemModels.VOLUME_OF_CLUBS -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_PICKAXE, 1)) }
            }
            ItemModels.ARCANE_BOOK -> {
                enchantingTomeHandler(event)
            }
        }
    }

    private fun enchantingTomeHandler(event: EnchantItemEvent) {
        val randomTome: OdysseyItem
        val enchanterLevel = maxOf(1, event.enchanter.level)
        val tierCost: Int

        when ((0..40).random() + minOf(enchanterLevel, 100)) {
            in 0..10 -> {
                tierCost = 0
                randomTome = listOf(Miscellaneous.TOME_OF_BANISHMENT).random()
            }
            in 11..40 -> {
                tierCost = 1
                randomTome = listOf(Miscellaneous.TOME_OF_DISCHARGE, Miscellaneous.TOME_OF_EMBRACE).random()
            }
            in 41..70 -> {
                tierCost = 2
                randomTome = listOf(Miscellaneous.TOME_OF_PROMOTION, Miscellaneous.TOME_OF_IMITATION).random()
            }
            in 71..110 -> {
                tierCost = 3
                randomTome = listOf(Miscellaneous.TOME_OF_EXPENDITURE, Miscellaneous.TOME_OF_HARMONY).random()
            }
            in 111..200 -> {
                tierCost = 4
                randomTome = listOf(Miscellaneous.TOME_OF_AVARICE).random()
            }
            else -> {
                tierCost = 0
                randomTome = listOf(Miscellaneous.TOME_OF_BANISHMENT).random()
            }
        }

        // Particles and sounds
        with(event.enchantBlock.world) {
            val enchantLocation = event.enchantBlock.location.clone().toCenterLocation()
            spawnParticle(Particle.CRIT_MAGIC, enchantLocation, 125, 0.5, 0.5, 0.5)
            spawnParticle(Particle.VILLAGER_HAPPY, enchantLocation, 65, 0.5, 0.5, 0.5)
            spawnParticle(Particle.ELECTRIC_SPARK, enchantLocation, 65, 0.5, 0.5, 0.5)
            playSound(enchantLocation, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2.5F, 0.9F)
            playSound(enchantLocation, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2.5F, 1.3F)
            playSound(enchantLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.6F)
        }

        val advancement = event.enchanter.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/enchant_a_tome")!!)
        if (advancement != null) {
            event.enchanter.getAdvancementProgress(advancement).awardCriteria("requirement")
        }

        (event.inventory as EnchantingInventory).item = randomTome.createItemStack(1)
        event.enchanter.level -= minOf(tierCost + 1, event.enchanter.level)
        event.isCancelled = true
    }

    private fun enchantingItemHandler(event: EnchantItemEvent) {
        val item = event.item
        val newEnchants = event.enchantsToAdd
        // Get slots or create new slots
        val maxSlots = if (item.isSlotted()) { item.getSlots() } else { getMaterialSlotCount(item.type) }
        // New Enchant Slots
        if (!item.isSlotted()) {
            item.setSlots(maxSlots)
        }
        var usedEnchantSlots = 0
        var usedGildedSlots = 0
        // Get Hint
        val hint = event.enchantmentHint
        if (hint.isOdysseyEnchant()) { usedGildedSlots += 1 } else { usedEnchantSlots += 1 }
        // Remove excess enchants over slot limit
        val enchantsToRemove = mutableListOf<Enchantment>()
        for (newEnchant in newEnchants.keys) {
            if (newEnchant == hint) {
                continue
            }
            // Append to removal list
            if (newEnchant.isNotOdysseyEnchant()) {
                usedEnchantSlots += 1
                if (usedEnchantSlots > maxSlots.first) {
                    enchantsToRemove.add(newEnchant)
                }
            }
            else if (newEnchant.isOdysseyEnchant()) {
                usedGildedSlots += 1
                if (usedGildedSlots > maxSlots.second) {
                    enchantsToRemove.add(newEnchant)
                }
            }
        }
        enchantsToRemove.forEach { newEnchants.remove(it) }
        // Add Bonus Gilded
        //item.rollGildedEnchants(maxSlots.second, newEnchants, event.expLevelCost, event.enchanter.level)
        // Lore
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        item.updateSlotLore(newEnchants)
    }

    private fun ItemStack.rollGildedEnchants(gildedSlots: Int, newEnchants: MutableMap<Enchantment, Int>, lapisCost: Int, levels: Int) {
        for (x in 1..minOf(gildedSlots, lapisCost)) {
            val hasRolled = (10 + minOf(levels, 75)) >= (1..100).random()
            if (!hasRolled) continue
            val randomGilded = getMaterialEnchantSet(type).random()
            if (!randomGilded.canEnchantItem(this)) continue
            if (randomGilded.toBukkit() in newEnchants.keys) continue
            val hasConflict = newEnchants.keys.any { randomGilded.conflictsWith(it) }
            if (hasConflict) continue
            // Passed All conditions
            newEnchants.also {
                it[randomGilded.toBukkit() ] = maxOf(1, minOf(randomGilded.maximumLevel, (levels / 10)))
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // TODO: Polymerization -> Promotion and Replication Fixes
    @EventHandler(priority = EventPriority.HIGH)
    fun smithingEnchantHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return
        if (event.inventory.inputMineral == null) return
        if (event.inventory.inputEquipment == null) return
        if (event.inventory.inputTemplate == null) return
        val mineral = event.inventory.inputMineral!!
        val equipment = event.inventory.inputEquipment!!.clone()
        val template = event.inventory.inputTemplate!!
        // Avoid Conflict with other smithing, using (Enchanted Book)
        if (!template.itemMeta.hasCustomModelData()) return
        if (!equipment.hasItemMeta()) return
        if (recipe.result.type != Material.ENCHANTED_BOOK) return
        if (event.result?.type == Material.ENCHANTED_BOOK) {
            event.result = ItemStack(Material.AIR)
        }
        // Variables
        val hasCrystals = (mineral.type == Material.PRISMARINE_CRYSTALS)
        val hasGold = mineral.type == Material.GOLD_NUGGET
        val hasEquipment = equipment.type != Material.ENCHANTED_BOOK && equipment.type != Material.BOOK
        val hasBook = equipment.type == Material.ENCHANTED_BOOK

        // Legacy Check
        val isLegacy = !equipment.isSlotted() && equipment.itemMeta.hasLore() && equipment.lore()!!.contains(slotSeperator)
        if (isLegacy) {
            event.viewers.forEach { it.sendFailMessage("You need to reactivate this item. Combine with empty paper or gold nugget at the anvil!") }
            return
        }

        if (hasCrystals && hasEquipment) {
            event.result = when (template.itemMeta.customModelData) {
                ItemModels.TOME_OF_AVARICE -> {
                    tomeOfAvariceToEquipment(equipment, event.viewers)
                }
                ItemModels.TOME_OF_BANISHMENT -> {
                    tomeOfBanishmentToEquipment(equipment, event.viewers)
                }
                ItemModels.TOME_OF_DISCHARGE -> {
                    tomeOfDischargeToEquipment(equipment, event.viewers)
                }
                ItemModels.TOME_OF_EMBRACE -> {
                    tomeOfEmbraceToEquipment(equipment, event.viewers)
                }
                ItemModels.TOME_OF_EXPENDITURE -> {
                    tomeOfExpenditureToEquipment(equipment, event.viewers)
                }
                ItemModels.TOME_OF_HARMONY -> {
                    tomeOfHarmonyToEquipment(equipment)
                }
                ItemModels.TOME_OF_POLYMERIZATION -> {
                    tomeOfPolymerizationToEquipment(template, equipment, event.viewers)
                }
                ItemModels.TOME_OF_PROMOTION -> {
                    tomeOfPromotionToEquipment(equipment, event.viewers)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
            }
        }
        else if (hasCrystals && hasBook) {
            event.result = when (template.itemMeta.customModelData) {
                ItemModels.TOME_OF_PROMOTION -> {
                    tomeOfPromotionToBook(equipment, event.viewers)
                }
                ItemModels.TOME_OF_REPLICATION -> {
                    event.inventory.inputTemplate = tomeOfReplicationToBook(equipment)
                    event.inventory.inputMineral!!.subtract(1)
                    ItemStack(Material.AIR)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
            }
        }
        /*
        else if (hasGold && hasEquipment) {
            event.result = when (template.itemMeta.customModelData) {
                ItemModels.GILDED_BOOK -> {
                    gildedBookToEquipment(template, equipment, event.viewers)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
            }
        }
        else if (hasGold && hasBook) {
            event.result = when (template.itemMeta.customModelData) {
                ItemModels.GILDED_BOOK -> {
                    gildedBookToBook(template, equipment, event.viewers)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
            }
        }

         */
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun tomeOfAvariceToEquipment(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendFailMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        if (!item.itemMeta.hasEnchants()) {
            viewers.forEach { it.sendFailMessage("This item needs to be enchanted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        if (item.getGildedSlots() >= 3) {
            viewers.forEach { it.sendFailMessage("This item has the maximum of three gilded slots.") }
            return ItemStack(Material.AIR)
        }
        val enchantList = item.enchantments.filter { it.key.isNotOdysseyEnchant() }
        val enchantSize = enchantList.size
        if (enchantSize < 4) {
            viewers.forEach { it.sendFailMessage("This tome requires at least 4 enchants on the item to use.") }
            return ItemStack(Material.AIR)
        }
        // Add Slot remove enchants
        return item.apply {
            addGildedSlot()
            for (enchant in enchantList) { removeEnchantment(enchant.key) }
            updateSlotLore()
        }
    }

    private fun tomeOfBanishmentToEquipment(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendFailMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        val enchantSlotCount = item.getEnchantSlots()
        val gildedSlotCount = item.getGildedSlots()
        val newSlotCount = enchantSlotCount + gildedSlotCount - 1
        // Remove Excess Enchant
        if (newSlotCount < item.enchantments.size) {
            val enchantToRemove = item.enchantments.toList().random()
            item.apply {
                if (enchantToRemove.first.isOdysseyEnchant()) {
                    removeEnchantment(enchantToRemove.first)
                    removeGildedSlot()
                }
                else {
                    removeEnchantment(enchantToRemove.first)
                    removeEnchantSlot()
                }
                itemMeta = itemMeta.also {
                    it.removeEnchant(enchantToRemove.first)
                }
                updateSlotLore()
            }
        } else { // Remove Slot
            if (enchantSlotCount > 1) {
                item.removeEnchantSlot()
            }
            else if (gildedSlotCount > 1) {
                item.removeGildedSlot()
            }
            else {
                viewers.forEach { it.sendFailMessage("This item needs at least one slot to use this tome") }
                return ItemStack(Material.AIR)
            }
        }
        item.updateSlotLore()
        return item
    }

    private fun tomeOfDischargeToEquipment(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendFailMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        if (!item.itemMeta.hasEnchants()) {
            viewers.forEach { it.sendFailMessage("This item needs to be enchanted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        // Remove Enchantment
        val enchantToRemove = item.enchantments.toList().random()
        return item.apply {
            if (enchantToRemove.first.isOdysseyEnchant()) {
                removeEnchantment(enchantToRemove.first)
            }
            else {
                removeEnchantment(enchantToRemove.first)
            }
            itemMeta = itemMeta.also {
                it.removeEnchant(enchantToRemove.first)
            }
            updateSlotLore()
        }
    }

    private fun tomeOfEmbraceToEquipment(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
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

    private fun tomeOfExpenditureToEquipment(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.itemMeta.hasEnchants()) {
            viewers.forEach { it.sendFailMessage("This item needs to be enchanted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        // Create Book
        val extractedEnchant = item.enchantments.toList().random()
        val extractedIsGilded = extractedEnchant.first.isOdysseyEnchant()
        val book = if (extractedIsGilded) {
            Miscellaneous.GILDED_BOOK.createGildedBook(extractedEnchant.first.convertToOdysseyEnchant(), extractedEnchant.second)
        } else {
            ItemStack(Material.ENCHANTED_BOOK, 1).apply {
                val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                newMeta.removeStoredEnchant(extractedEnchant.first)
                val limitLevel = minOf(extractedEnchant.first.maxLevel, extractedEnchant.second)
                newMeta.addStoredEnchant(extractedEnchant.first, limitLevel, false)
                itemMeta = newMeta
            }
        }
        return book
    }

    private fun tomeOfHarmonyToEquipment(item: ItemStack): ItemStack {
        return item.apply {
            itemMeta = itemMeta.also { if (it is Repairable) it.repairCost = 1 }
        }
    }

    private fun tomeOfPolymerizationToEquipment(book: ItemStack, item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (item.itemMeta.hasEnchants()) {
            viewers.forEach { it.sendFailMessage("This item needs no enchants to be used.") }
            return ItemStack(Material.AIR)
        }
        if (!book.itemMeta.hasEnchants()) {
            viewers.forEach { it.sendFailMessage("This tome needs at least one enchant to be used") }
            return ItemStack(Material.AIR)
        }
        if (item.getGildedSlots() < book.enchantments.keys.count { it.isOdysseyEnchant() } ) {
            viewers.forEach { it.sendFailMessage("This tome has more gilded enchants than slots on the item") }
            return ItemStack(Material.AIR)
        }
        if (item.getEnchantSlots() < book.enchantments.keys.count { it.isNotOdysseyEnchant() } ) {
            viewers.forEach { it.sendFailMessage("This tome has more regular enchants than slots on the item") }
            return ItemStack(Material.AIR)
        }
        // Apply new
        return item.apply {
            // TODO: Make more robust
            addUnsafeEnchantments(book.enchantments)
            updateSlotLore()
        }
    }

    private fun tomeOfPromotionToEquipment(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted() && item.type != Material.ENCHANTED_BOOK) {
            viewers.forEach { it.sendFailMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        if (!item.itemMeta.hasEnchants()) {
            viewers.forEach { it.sendFailMessage("This item needs to be enchanted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        // Check book meta and enchant meta
        var promotedEnchant: Pair<Enchantment, Int>? = null
        if (item.itemMeta.hasEnchants()) {
            try {
                val notMaxList = item.enchantments.toList().shuffled().filter { it.second < it.first.maxLevel }
                promotedEnchant = notMaxList.random()
            }
            catch (except: NoSuchElementException) {
                viewers.forEach { it.sendFailMessage("The item enchantments are at the max level.") }
                return ItemStack(Material.AIR)
            }
            promotedEnchant = item.enchantments.toList().random()
        } else if ((item.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            promotedEnchant = (item.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().random()
        }
        // More Sentries
        if (promotedEnchant == null) return ItemStack(Material.AIR)
        if (promotedEnchant.first.maxLevel <= promotedEnchant.second) {
            val message = "${promotedEnchant.first.key} is at the max level (Max: ${promotedEnchant.first.maxLevel})."
            viewers.forEach { it.sendFailMessage(message) }
            return ItemStack(Material.AIR)
        }

        return item.apply {
            removeEnchantment(promotedEnchant.first)
            addEnchantment(promotedEnchant.first, promotedEnchant.second + 1)
            updateSlotLore()
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun tomeOfPromotionToBook(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        // Checks book meta and enchant item meta
        val promotedEnchant: Pair<Enchantment, Int> = if (item.itemMeta.hasEnchants()) {
            try {
                val notMaxList = item.enchantments.toList().shuffled().filter { it.second < it.first.maxLevel }
                notMaxList.random()
            }
            catch (except: NoSuchElementException) {
                viewers.forEach { it.sendFailMessage("The enchantment is at the max level.") }
                return ItemStack(Material.AIR)
            }

        } else if ((item.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            try {
                val notMaxList = (item.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().shuffled().filter { it.second < it.first.maxLevel }
                notMaxList.random()
            }
            catch (except: NoSuchElementException) {
                viewers.forEach { it.sendFailMessage("The enchantments are at the max level.") }
                return ItemStack(Material.AIR)
            }
        } else {
            return ItemStack(Material.AIR)
        }
        // Sentries
        if (promotedEnchant.first.maxLevel <= promotedEnchant.second) {
            viewers.forEach { it.sendFailMessage("${promotedEnchant.first.displayName(promotedEnchant.second)} is at the max level (Max: ${promotedEnchant.first.maxLevel}).") }
            return ItemStack(Material.AIR)
        }
        // Book
        val book = if (promotedEnchant.first.isOdysseyEnchant()) {
            Miscellaneous.GILDED_BOOK.createGildedBook(promotedEnchant.first.convertToOdysseyEnchant(), promotedEnchant.second + 1)
        } else {
            item.clone().apply {
                val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                newMeta.removeStoredEnchant(promotedEnchant.first)
                newMeta.addStoredEnchant(promotedEnchant.first, promotedEnchant.second + 1, false)
                itemMeta = newMeta
            }
        }
        return book
    }

    private fun tomeOfReplicationToBook(item: ItemStack): ItemStack {
        return if (item.itemMeta.hasEnchants() || (item.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            item.clone()
        } else {
            Miscellaneous.TOME_OF_REPLICATION.createItemStack(1)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*
    private fun gildedBookToEquipment(book: ItemStack, item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        // Gilded Book Sentries
        if (book.enchantments.size > 1) {
            viewers.forEach { it.sendFailMessage("The gilded book has more than one enchantment") }
            return ItemStack(Material.AIR)
        }
        val isOdysseyEnchant = book.enchantments.any { it.key is OdysseyEnchantment }
        if (!isOdysseyEnchant) {
            viewers.forEach { it.sendFailMessage("The gilded book does not have a gilded enchantment") }
            return ItemStack(Material.AIR)
        }
        val bookEnchant = book.enchantments.entries.first { it.key is OdysseyEnchantment }
        val canEnchantItem = bookEnchant.key.canEnchantItem(item)
        if (!canEnchantItem) {
            viewers.forEach { it.sendFailMessage("The enchantment ${bookEnchant.key.key} can not be applied to the item") }
            return ItemStack(Material.AIR)
        }
        var conflictingEnchant: Enchantment? = null
        val conflictsWithItem = item.enchantments.any {
            conflictingEnchant = it.key
            bookEnchant.key.conflictsWith(it.key)
        }
        if (conflictsWithItem)  {
            viewers.forEach { it.sendFailMessage("The enchantment ${bookEnchant.key.key} conflicts with the enchant ${conflictingEnchant!!.key}") }
            return ItemStack(Material.AIR)
        }
        val itemHasEnchant = item.enchantments.containsKey(bookEnchant.key)
        val gildedCount = item.enchantments.keys.count { it is OdysseyEnchantment }
        if (gildedCount >= item.getGildedSlots() && !itemHasEnchant) {
            viewers.forEach { it.sendFailMessage("There are no empty gilded slots on this item") }
            return ItemStack(Material.AIR)
        }
        // New Enchant
        if (!itemHasEnchant) {
            // Apply
            return item.apply {
                addUnsafeEnchantment(bookEnchant.key, bookEnchant.value)
                updateSlotLore()
            }
        }
        else {
            // Check if not max
            val equipmentLevel = item.enchantments[bookEnchant.key]!!
            val notMaxLevel = equipmentLevel < bookEnchant.key.maxLevel
            val canUpgrade = equipmentLevel <= bookEnchant.value
            if (!(canUpgrade && notMaxLevel)) {
                viewers.forEach { it.sendFailMessage("${bookEnchant.key.displayName(bookEnchant.value)} is at the max level (Max: ${bookEnchant.key.maxLevel}).") }
                return ItemStack(Material.AIR)
            }
            // Continue
            val newMax = max(equipmentLevel + 1, bookEnchant.value)
            return item.apply {
                removeEnchantment(bookEnchant.key)
                addUnsafeEnchantment(bookEnchant.key, newMax)
                updateSlotLore()
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun gildedBookToBook(first: ItemStack, second: ItemStack, viewers: List<HumanEntity>): ItemStack {
        // Gilded Book Sentries
        if (first.enchantments.size > 1 || second.enchantments.size > 1) {
            viewers.forEach { it.sendFailMessage("The gilded book has more than one enchantment") }
            return ItemStack(Material.AIR)
        }
        val areOdysseyEnchants = first.enchantments.any { it.key is OdysseyEnchantment } && second.enchantments.any { it.key is OdysseyEnchantment }
        if (!areOdysseyEnchants) {
            viewers.forEach { it.sendFailMessage("The gilded book does not have a gilded enchantment") }
            return ItemStack(Material.AIR)
        }
        val firstEnchant = first.enchantments.entries.first { it.key is OdysseyEnchantment }
        val secondEnchant = second.enchantments.entries.first { it.key is OdysseyEnchantment }
        if (firstEnchant.key != secondEnchant.key) {
            viewers.forEach { it.sendFailMessage("The gilded books are not the same enchantment") }
            return ItemStack(Material.AIR)
        }
        // Checks
        val notMaxLevel = firstEnchant.value < firstEnchant.key.maxLevel && secondEnchant.value < secondEnchant.key.maxLevel
        if (!notMaxLevel) {
            viewers.forEach { it.sendFailMessage("${firstEnchant.key.key} is at the max level (Max: ${firstEnchant.key.maxLevel}).") }
            return ItemStack(Material.AIR)
        }
        val oldMax = maxOf(firstEnchant.value, secondEnchant.value)
        val newLevel = minOf(firstEnchant.value, secondEnchant.value)
        if (newLevel < oldMax) {
            viewers.forEach { it.sendFailMessage("The gilded books do not create a higher level enchantment.") }
            return ItemStack(Material.AIR)
        }
        return Miscellaneous.GILDED_BOOK.createGildedBook(firstEnchant.key.convertToOdysseyEnchant(), newLevel + 1)
    }


     */
    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // Get the compatible enchants for items
    private fun getMaterialEnchantSet(itemType: Material): Set<OdysseyEnchantment> {
        val enchantList = when (itemType) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                OdysseyEnchantments.MELEE_SET
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                OdysseyEnchantments.ARMOR_SET
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                OdysseyEnchantments.ARMOR_SET
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                OdysseyEnchantments.ARMOR_SET
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET -> {
                OdysseyEnchantments.ARMOR_SET
            }
            Material.BOW, Material.CROSSBOW -> {
                OdysseyEnchantments.RANGED_SET
            }
            Material.ELYTRA, Material.SHIELD, Material.FISHING_ROD -> {
                OdysseyEnchantments.MISC_SET
            }
            else -> {
                OdysseyEnchantments.MISC_SET
            }
        }
        val enchantSet = enchantList.filter { it !in OdysseyEnchantments.EXOTIC_LIST }
        return enchantSet.toSet()
    }

    // Get slots based on material and return Enchant-Gilded Pair
    private fun getMaterialSlotCount(itemType: Material): Pair<Int, Int> {
        var gildedSlots = 0
        val enchantSlots = when (itemType) {
            Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_HOE,
            Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET -> {
                gildedSlots = (0..1).random()
                maxOf(3 + (0..2).random() - gildedSlots, 2)
            }
            Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE,
            Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET -> {
                gildedSlots = (0..1).random()
                maxOf(3 + (0..2).random() - gildedSlots, 3)
            }
            Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET -> {
                gildedSlots = 1
                maxOf(4 + (0..2).random() - gildedSlots, 3)
            }
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
            Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET -> {
                gildedSlots = 1 + (0..1).random()
                maxOf(3 + (0..3).random() - gildedSlots, 4)
            }
            Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET -> {
                gildedSlots = 2 + (0..1).random()
                maxOf(2 + (0..2).random() - gildedSlots, 2)
            }
            Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE,
            Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                maxOf(4 + (0..3).random() - gildedSlots, 4)
            }
            Material.ELYTRA, Material.SHIELD, Material.FISHING_ROD, Material.TRIDENT -> {
                gildedSlots = 1 + (0..1).random()
                (3..4).random()
            }
            Material.BOW, Material.CROSSBOW -> {
                gildedSlots = 1 + (0..1).random()
                4 + (0..2).random() - gildedSlots
            }
            else -> {
                2
            }
        }
        return Pair(enchantSlots, gildedSlots)
    }

}