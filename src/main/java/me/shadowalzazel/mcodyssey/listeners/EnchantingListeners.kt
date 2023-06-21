package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.Runic
import me.shadowalzazel.mcodyssey.items.Runic.createEnchantedBook
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.listeners.unused.OdysseyArcaneSlotListeners
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.apache.commons.lang3.tuple.MutablePair
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
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


//TODO:
//TODO:
// TODO: ADDD UPDATE SLOTS FUNCTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//TODO:


object EnchantingListeners : Listener {

    // Colors
    private val GRAY_COLOR = TextColor.color(170, 170, 170)
    private val ENCHANT_COLOR = TextColor.color(191, 255, 189)
    private val GILDED_COLOR = TextColor.color(255, 170, 0)
    // Text
    private val slotSeperator = Component.text("----------------------" , GRAY_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val emptyGildedSlot = Component.text("+ Empty Gilded Slot", GILDED_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val emptyEnchantSlot = Component.text("+ Empty Enchantment Slot", GRAY_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    // TODO: Add Sharpness to hoes, shovels

    @EventHandler
    fun grindstoneHandler(event: PrepareResultEvent) {
        if (event.inventory !is GrindstoneInventory) { return }
        val inventory: GrindstoneInventory = event.inventory as GrindstoneInventory
        if (inventory.result == null) { return }
        if (inventory.upperItem?.enchantments == null && inventory.lowerItem?.enchantments == null) { return }
        if (inventory.result!!.lore()?.contains(slotSeperator) == false) { return }
        // Sentry Done
        val itemEnchants = if (inventory.upperItem?.enchantments != null) { inventory.upperItem!!.enchantments } else { inventory.lowerItem!!.enchantments }
        event.result = inventory.result!!.clone().apply { lore(removeEnchantsLore(inventory.result!!, itemEnchants)) }
        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
    }

    @EventHandler
    fun enchantingItemHandler(event: EnchantItemEvent) {
        val isSlotted = event.item.lore()?.contains(slotSeperator) == true

        when (event.item.type) {
            Material.BOOK -> {
                val isCustom = event.item.itemMeta.hasCustomModelData()
                if (isCustom) {
                    enchantingBookHandler(event)
                }
            }
            else -> {
                if (isSlotted) {
                    itemSlotted(event)
                }
                else {
                    itemNotSlotted(event)
                }
            }
        }
    }

    private fun enchantingBookHandler(event: EnchantItemEvent) {
        when(event.item.itemMeta.customModelData) {
            ItemModels.VOLUME_OF_TOOLS -> {
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
        val expScaleOffset = minOf(1 + (maxOf(30, event.enchanter.level) - 30), 95)
        when ((0..100).random() - expScaleOffset) {
            in -200..-80 -> {
                randomTome = listOf(Runic.TOME_OF_AVARICE, Runic.TOME_OF_EUPHONY).random()
            }
            in -81..-20 -> {
                randomTome = listOf(Runic.TOME_OF_EXPENDITURE, Runic.TOME_OF_REPLICATION).random()
            }
            in 21..50 -> {
                randomTome = listOf(Runic.TOME_OF_HARMONY, Runic.TOME_OF_PROMOTION).random()
            }
            in 51..80 -> {
                randomTome = listOf(Runic.TOME_OF_DISCHARGE, Runic.TOME_OF_EMBRACE).random()
            }
            in 81..200 -> {
                randomTome = listOf(Runic.TOME_OF_BANISHMENT).random()
            }
            else -> {
                randomTome = listOf(Runic.TOME_OF_DISCHARGE, Runic.TOME_OF_EMBRACE).random()
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
        (event.inventory as EnchantingInventory).item = randomTome.createItemStack(1)
        event.enchanter.level -= maxOf(event.enchanter.level / 10, event.levelHint / 1)
        event.isCancelled = true
    }

    @EventHandler
    fun anvilHandler(event: PrepareAnvilEvent) {
        if (event.inventory.firstItem == null) { return }
        if (event.inventory.secondItem == null) { return }
        val first = event.inventory.firstItem!!
        val second = event.inventory.secondItem!!
        if (event.result == null) {
            println("PROBLEM 1")
            // TRY
            // Adding Gilded Book
            if (!second.itemMeta.hasCustomModelData()) { return }
            if (second.itemMeta.customModelData == ItemModels.GILDED_BOOK) {
                println("BOOK")
                event.result = anvilSecondGildedBook(first, second)
            }
            return
        }
        if (first.type == Material.ENCHANTED_BOOK) { return }
        // Results
        val result = event.result!!
        val slotted = first.lore()?.contains(slotSeperator) == true
        // Enchants
        val firstHasGilded = first.enchantments.any { it.key is OdysseyEnchantment }
        val secondHasGilded = second.enchantments.any { it.key is OdysseyEnchantment }
        val secondIsBook = second.type == Material.ENCHANTED_BOOK
        // Prevent Combining Gilded
        if (firstHasGilded && (secondHasGilded && !secondIsBook)) {
            event.result = ItemStack(Material.AIR, 1)
            event.viewers.forEach { viewer ->
                if (viewer is Player) {
                    viewer.sendActionBar(Component.text("Cannot combine Gilded Enchants on two equipments", TextColor.color(255, 255, 85)))
                }
            }
        }
        // Add Slots
        if (!slotted && !secondHasGilded) { // TODO: MOVE
            // Create new slots
            var enchantSlots = 2
            var gildedSlots = 1
            for (enchant in result.enchantments) {
                if (enchant.key in OdysseyEnchantments.REGISTERED_SET) { gildedSlots += 1 } else { enchantSlots += 1 }
            }
            event.result!!.lore(createSlotsLore(result, Pair(enchantSlots, gildedSlots), result.enchantments))
            event.result!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        // Adding Regular Book
        // COMBINING
        if (slotted && !secondHasGilded) {
            val combineResult = anvilCombine(first, second, result)
            event.result = combineResult
            if (combineResult.type == Material.AIR) {
                event.viewers.forEach { viewer ->
                    if (viewer is Player) {
                        viewer.updateInventory()
                        viewer.sendActionBar(Component.text("There are not enough slots to combine the items!", TextColor.color(255, 255, 85)))
                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    fun enchantSmithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return
        if (event.inventory.inputMineral == null) return
        if (event.inventory.inputEquipment == null) return
        if (event.inventory.inputTemplate == null) return
        val mineral = event.inventory.inputMineral!!
        val equipment = event.inventory.inputEquipment!!
        val template = event.inventory.inputTemplate!!
        // Avoid Conflict with other smithing
        // val oldResult = event.result
        if (recipe.result.type != Material.ENCHANTED_BOOK) { return }
        if (event.result?.type == Material.ENCHANTED_BOOK) {
            event.result = ItemStack(Material.AIR)
        }
        // Check for model
        if (!template.itemMeta.hasCustomModelData()) return
        // Vars
        val hasCrystals = mineral.type == Material.PRISMARINE_CRYSTALS
        val hasGold = mineral.type == Material.GOLD_NUGGET
        val hasEquipment = equipment.type != Material.ENCHANTED_BOOK && equipment.type != Material.BOOK
        val hasBook = equipment.type == Material.ENCHANTED_BOOK


        if (hasCrystals && hasEquipment) {
            event.result = when(template.itemMeta.customModelData) {
                ItemModels.TOME_OF_BANISHMENT -> {
                    tomeOfBanishmentToItem(equipment)
                }
                ItemModels.TOME_OF_DISCHARGE -> {
                    tomeOfDischargeToItem(equipment)
                }
                ItemModels.TOME_OF_EMBRACE -> {
                    tomeOfEmbraceToItem(equipment)
                }
                ItemModels.TOME_OF_EXPENDITURE -> {
                    tomeOfExpenditureToItem(equipment)
                }
                ItemModels.TOME_OF_HARMONY -> {
                    tomeOfHarmonyToItem(equipment)
                }
                ItemModels.TOME_OF_PROMOTION -> {
                    tomeOfPromotionToItem(equipment)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
                // TODO: Tome Of Avarice and Euphony
            }
        }
        else if (hasGold && hasEquipment) {
            event.result = when(template.itemMeta.customModelData) {
                ItemModels.GILDED_BOOK -> {
                    gildedBookToItem(equipment, template)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
            }
        }
        else if (hasCrystals && hasBook) {
            event.result = when(template.itemMeta.customModelData) {
                ItemModels.TOME_OF_PROMOTION -> {
                    tomeOfPromotionToBook(equipment)
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
        else if (hasGold && hasBook) {
            event.result = when(template.itemMeta.customModelData) {
                ItemModels.GILDED_BOOK -> {
                    combiningGildedToBook(template, equipment)
                }
                else -> {
                    ItemStack(Material.AIR)
                }
            }
        }

        //event.inventory.result = recipe.result
        //event.result = recipe.result
        //event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }

    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun anvilAddBook(equipment: ItemStack, book: ItemStack, eventResult: ItemStack) : ItemStack {
        val newItem = eventResult.clone()
        // Lore
        val newLore = newItem.lore()!!
        val emptySlots = newLore.count{ it == emptyEnchantSlot }
        val emptyGildedSlots = newLore.count{ it == emptyGildedSlot }
        // Checks
        val hasSpace = emptySlots > 0
        val canEnchant = book.enchantments.keys.all { it.canEnchantItem(equipment) }

        // Add Odyssey Enchants
        equipment.enchantments.forEach {
            if (it.key is OdysseyEnchantment && !newItem.containsEnchantment(it.key)) {
                newItem.addUnsafeEnchantment(it.key, it.value)
            }
        }
        return ItemStack(Material.AIR)
    }

    private fun anvilSecondGildedBook(equipment: ItemStack, book: ItemStack) : ItemStack {
        // Gilded Book has 1 enchant
        val isOdysseyEnchant = book.enchantments.any { it.key is OdysseyEnchantment }
        val canEnchantItem = book.enchantments.any { it.key.canEnchantItem(equipment) }
        val itemHasEnchant = book.enchantments.any { equipment.containsEnchantment(it.key) }
        val conflictsWithItem = book.enchantments.any {
            var found = false
            for (enchant in equipment.enchantments.keys) {
                if (it.key.conflictsWith(enchant)) {
                    found = true
                    break
                }
            }
            found
        }

        if (conflictsWithItem) return ItemStack(Material.AIR)
        if (!canEnchantItem) return ItemStack(Material.AIR)
        if (!isOdysseyEnchant) return ItemStack(Material.AIR)
        val emptyGildedSlots = equipment.lore()!!.count { it == emptyGildedSlot }
        if (emptyGildedSlots <= 0 && !itemHasEnchant) return ItemStack(Material.AIR)
        // New
        val bookEnchant = book.enchantments.entries.first { it.key is OdysseyEnchantment }
        val newItem = equipment.clone()
        val newLore = newItem.lore()!!
        // Upgrade
        if (itemHasEnchant) {
            val equipmentLevel = equipment.getEnchantmentLevel(bookEnchant.key)
            val notMaxLevel = equipmentLevel < bookEnchant.key.maxLevel
            val canUpgrade = equipmentLevel <= bookEnchant.value
            if (!(canUpgrade && notMaxLevel)) return ItemStack(Material.AIR)
            // Continue
            val newMax = max(equipmentLevel + 1, bookEnchant.value)
            // Lore
            val gildedLore = (bookEnchant.key as OdysseyEnchantment).displayLore(equipmentLevel)
            val gildedIndex = newLore.indexOf(gildedLore)
            newLore[gildedIndex] = (bookEnchant.key as OdysseyEnchantment).displayLore(newMax)
            // New
            newItem.apply {
                lore(newLore)
                removeEnchantment(bookEnchant.key)
                addUnsafeEnchantment(bookEnchant.key, newMax)
            }

            return newItem
        }
        // Add
        else {
            val slotIndex = newLore.indexOf(emptyGildedSlot)
            newLore[slotIndex] = (bookEnchant.key as OdysseyEnchantment).displayLore(bookEnchant.value)
            // New
            newItem.apply {
                lore(newLore)
                addUnsafeEnchantment(bookEnchant.key, bookEnchant.value)
            }

            return newItem
        }
    }

    private fun anvilCombine(equipment: ItemStack, secondEquipment: ItemStack, eventResult: ItemStack): ItemStack {
        val currentGildedEnchants: MutableMap<Enchantment, Int> = mutableMapOf()
        // If first enchant has gilded enchants add to map
        for (enchant in equipment.enchantments) {
            if (enchant.key is OdysseyEnchantment) {
                currentGildedEnchants[enchant.key] = enchant.value
            }
        }

        var newItem = eventResult.clone()
        val newLore = equipment.clone().lore()!!

        // Index
        val infoIndex = newLore.indexOf(slotSeperator) - 1
        // Manage Slots
        val emptySlots = newLore.count{ it == emptyEnchantSlot }
        val emptyGildedSlots = newLore.count{ it == emptyGildedSlot }
        var usedGildedSlots = currentGildedEnchants.size
        var usedSlots = equipment.enchantments.size - usedGildedSlots
        val gildedSlots = emptyGildedSlots + usedGildedSlots
        val enchantSlots = emptySlots + usedSlots
        val totalSlots = gildedSlots + enchantSlots
        // Loop over all enchants to either add lore or add to removal enchants if over enchant slots
        val enchantsToRemove = mutableListOf<Enchantment>()
        eventResult.enchantments.forEach {
            if (it.key !is OdysseyEnchantment) {
                val lowestLevel = if (it.key in equipment.enchantments.keys) { equipment.getEnchantmentLevel(it.key) } else { it.value }
                val enchantLore = it.key
                    .displayName(lowestLevel)
                    .color(ENCHANT_COLOR)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                if (!newLore.contains(enchantLore) && usedSlots < enchantSlots) {
                    val emptySlotIndex = newLore.indexOf(emptyEnchantSlot)
                    newLore[emptySlotIndex] = enchantLore
                    usedSlots += 1
                }
                else if (newLore.contains(enchantLore)) {
                    val usedSlotIndex = newLore.indexOf(enchantLore)
                    val replacementLore = it.key
                        .displayName(it.value)
                        .color(ENCHANT_COLOR)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                    newLore[usedSlotIndex] = replacementLore
                }
                else {
                    enchantsToRemove.add(it.value, it.key)
                }
            }
            else if (it.key is OdysseyEnchantment) {
                // Should not work but OK
                val gildedLore = it.key.displayName(it.value).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                if (!newLore.contains(gildedLore) && usedGildedSlots < emptyGildedSlots) {
                    val emptySlotIndex = newLore.indexOf(emptyGildedSlot)
                    newLore[emptySlotIndex] = gildedLore
                    usedGildedSlots += 1
                }
            }
        }
        // Do not create result if more
        if (eventResult.enchantments.size > totalSlots) {
            newItem = ItemStack(Material.AIR)
            return newItem
        }
        // Remove excess enchants if any
        enchantsToRemove.forEach {
            newItem.enchantments.remove(it)
        }
        newLore[infoIndex] = Component.text("Enchantment Slots: [${usedSlots + usedGildedSlots}/$totalSlots]", ENCHANT_COLOR)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

        newItem.apply {
            lore(newLore)
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        return newItem
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun gildedBookToItem(eventItem: ItemStack, eventBook: ItemStack): ItemStack {
        return anvilSecondGildedBook(eventItem, eventBook)
    }

    private fun tomeOfEmbraceToItem(eventItem: ItemStack): ItemStack {
        if (eventItem.itemMeta.lore()?.contains(slotSeperator) == false) { return ItemStack(Material.AIR) }
        // Sentries
        val newItem = eventItem.clone()
        // Lore
        val newLore = newItem.lore()!!.also { lore ->
            // Change info
            val startIndex = lore.indexOf(slotSeperator) - 1
            val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + newItem.enchantments.size
            lore[startIndex] = Component.text("Enchantment Slots: [${newItem.enchantments.size}/${totalSlots + 1}]", ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            // Add Slot
            var nonOdysseyCount = 0
            for (enchant in newItem.enchantments) { if (enchant !is OdysseyEnchantment) { nonOdysseyCount += 1 } }
            val newSlotIndex = startIndex + 1 + nonOdysseyCount + lore.count{ it == emptyEnchantSlot }
            lore.add(newSlotIndex, emptyEnchantSlot)
        }

        newItem.apply {
            lore(newLore)
        }

        return newItem
    }

    private fun tomeOfExpenditureToItem(eventItem: ItemStack): ItemStack {
        if (!eventItem.itemMeta.hasEnchants()) { return ItemStack(Material.AIR) }
        // Sentries
        val randomEnchant = eventItem.clone().enchantments.toList().random()
        // Book
        val newItem = if (randomEnchant.first is OdysseyEnchantment) {
            Runic.GILDED_BOOK.createEnchantedBook(randomEnchant.first as OdysseyEnchantment, randomEnchant.second) }
        else {
            ItemStack(Material.ENCHANTED_BOOK, 1).apply {
                val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                newMeta.removeStoredEnchant(randomEnchant.first)
                val limitLevel = minOf(randomEnchant.first.maxLevel, randomEnchant.second)
                newMeta.addStoredEnchant(randomEnchant.first, limitLevel, false)
                itemMeta = newMeta
            }
        }

        return newItem
    }

    private fun tomeOfHarmonyToItem(eventItem: ItemStack): ItemStack {
        val newItem = eventItem.clone()
        newItem.apply {
            itemMeta = itemMeta.also {
                if (it is Repairable) { it.repairCost = 0 }
            }
        }
        return newItem
    }

    private fun tomeOfPromotionToItem(eventItem: ItemStack): ItemStack {
        if (eventItem.itemMeta.lore()?.contains(slotSeperator) == false) { return ItemStack(Material.AIR) }
        if (!eventItem.itemMeta.hasEnchants()) { return ItemStack(Material.AIR) }
        // Sentries
        val newItem = eventItem.clone()
        // Checks book meta and enchant
        var randomEnchant: Pair<Enchantment, Int>? = null
        if (newItem.itemMeta.hasEnchants()) {
            randomEnchant = newItem.enchantments.toList().random()
        }
        else if ((newItem.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            randomEnchant = (newItem.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().random()
        }
        // More Sentries
        if (randomEnchant == null) { return ItemStack(Material.AIR) }
        if (randomEnchant.first.maxLevel <= randomEnchant.second) { return ItemStack(Material.AIR) }
        // Lore
        val newLore = newItem.lore()!!.also { lore ->
            // Change info
            val startIndex = lore.indexOf(slotSeperator) - 1
            val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + newItem.enchantments.size
            lore[startIndex] = Component.text("Enchantment Slots: [${newItem.enchantments.size}/$totalSlots]", ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            // Change slot
            val isOdysseyEnchant = randomEnchant.first is OdysseyEnchantment
            val randomLore = if (isOdysseyEnchant) {
                randomEnchant.first
                    .displayName(randomEnchant.second)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
            else {
                randomEnchant.first
                    .displayName(randomEnchant.second)
                    .color(ENCHANT_COLOR)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            val randomIndex = lore.indexOf(randomLore)
            lore[randomIndex] = if (isOdysseyEnchant) {
                randomEnchant.first
                    .displayName(randomEnchant.second + 1)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
            else {
                randomEnchant.first
                    .displayName(randomEnchant.second + 1)
                    .color(ENCHANT_COLOR)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
        }
        newItem.apply {
            lore(newLore)
            removeEnchantment(randomEnchant.first)
            addEnchantment(randomEnchant.first, randomEnchant.second + 1)
        }

        return newItem
    }

    private fun tomeOfDischargeToItem(eventItem: ItemStack): ItemStack {
        if (eventItem.itemMeta.lore()?.contains(slotSeperator) == false) { return ItemStack(Material.AIR) }
        if (!eventItem.itemMeta.hasEnchants()) { return ItemStack(Material.AIR) }
        // Sentries
        val newItem = eventItem.clone()
        val enchantToRemove = eventItem.enchantments.toList().random()
        // Lore
        val newLore = newItem.lore()!!.also { lore ->
            // Change info
            val startIndex = lore.indexOf(slotSeperator) - 1
            val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + newItem.enchantments.size
            lore[startIndex] = Component.text("Enchantment Slots: [${newItem.enchantments.size - 1}/$totalSlots]", ENCHANT_COLOR)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            // Change slot
            val isOdysseyEnchant = enchantToRemove.first is OdysseyEnchantment
            // Find
            val foundLore = if (isOdysseyEnchant) {
                (enchantToRemove.first as OdysseyEnchantment)
                    .displayLore(enchantToRemove.second)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
            else {
                enchantToRemove.first
                    .displayName(enchantToRemove.second)
                    .color(ENCHANT_COLOR)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            // Change
            lore[lore.indexOf(foundLore)] = if (isOdysseyEnchant) {
                emptyGildedSlot
            } else {
                emptyEnchantSlot
            }
        }
        newItem.apply {
            lore(newLore)
            removeEnchantment(enchantToRemove.first)
        }

        return newItem
    }

    private fun tomeOfBanishmentToItem(eventItem: ItemStack): ItemStack {
        if (eventItem.itemMeta.lore()?.contains(slotSeperator) == false) { return ItemStack(Material.AIR) }
        // Sentries
        val newItem = eventItem.clone()
        var extraEnchant: Enchantment? = null
        // Lore
        val newLore = newItem.lore()!!.also { lore ->
            val startIndex = lore.indexOf(slotSeperator) - 1
            val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + newItem.enchantments.size
            val newTotal = totalSlots - 1
            if (newTotal < newItem.enchantments.size) {
                val randomEnchant = newItem.enchantments.toList().random()
                val isOdysseyEnchant = randomEnchant.first is OdysseyEnchantment
                // Checks book meta and enchant
                val foundLore = if (isOdysseyEnchant) {
                    (randomEnchant.first as OdysseyEnchantment)
                        .displayLore(randomEnchant.second)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
                else {
                    randomEnchant.first
                        .displayName(randomEnchant.second)
                        .color(ENCHANT_COLOR)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                }
                lore.remove(foundLore)
                extraEnchant = randomEnchant.first
                lore[startIndex] = Component.text("Enchantment Slots: [${newItem.enchantments.size - 1}/$newTotal]", ENCHANT_COLOR)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            else {
                val banishedIndex = if (lore.count { it == emptyEnchantSlot } >= 1) { lore.indexOf(emptyEnchantSlot) }
                else { lore.indexOf(emptyGildedSlot) }
                lore.removeAt(banishedIndex)
                lore[startIndex] = Component.text("Enchantment Slots: [${newItem.enchantments.size}/$newTotal]", ENCHANT_COLOR)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
        }
        newItem.apply {
            lore(newLore)
            if (extraEnchant != null) { removeEnchantment(extraEnchant!!) }
        }

        return newItem
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun combiningGildedToBook(first: ItemStack, second: ItemStack): ItemStack {
        val firstBookEnchants = first.enchantments
        val secondBookEnchants = second.enchantments
        for (enchantKey in firstBookEnchants.keys) {
            if (secondBookEnchants.containsKey(enchantKey) && enchantKey is OdysseyEnchantment) {
                if (firstBookEnchants[enchantKey]!! < enchantKey.maximumLevel && secondBookEnchants[enchantKey]!! < enchantKey.maximumLevel) {
                    val maxLevel = max(firstBookEnchants[enchantKey]!!, secondBookEnchants[enchantKey]!!)
                    return Runic.GILDED_BOOK.createEnchantedBook(enchantKey, maxLevel + 1)
                }
            }
        }
        return ItemStack(Material.AIR)
    }

    private fun tomeOfPromotionToBook(eventItem: ItemStack): ItemStack {
        // Checks book meta and enchant
        val randomEnchant: Pair<Enchantment, Int> = if (eventItem.itemMeta.hasEnchants()) {
            eventItem.enchantments.toList().random() }
        else if ((eventItem.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            (eventItem.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().random() }
        else {
            return ItemStack(Material.AIR)
        }
        // Sentries
        if (randomEnchant.first.maxLevel <= randomEnchant.second) { return ItemStack(Material.AIR) }
        //
        val newItem = eventItem.clone()
        newItem.apply {
            if (randomEnchant.first is OdysseyEnchantment) {
                val oldLore = randomEnchant.first.displayName(randomEnchant.second).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                val someIndex = lore()!!.indexOf(oldLore)
                val newLore = lore()!!.also {
                    it[someIndex] = randomEnchant.first
                        .displayName(randomEnchant.second + 1)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                }
                lore(newLore)
                removeEnchantment(randomEnchant.first)
                addEnchantment(randomEnchant.first, randomEnchant.second + 1)
            } else {
                val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                newMeta.removeStoredEnchant(randomEnchant.first)
                newMeta.addStoredEnchant(randomEnchant.first, randomEnchant.second + 1, false)
                itemMeta = newMeta
            }
        }

        return newItem
    }

    private fun tomeOfReplicationToBook(eventItem: ItemStack): ItemStack {
        return if (eventItem.itemMeta.hasEnchants() || (eventItem.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            eventItem.clone()
        } else {
            Runic.TOME_OF_REPLICATION.createItemStack(1)
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun rollNewGilded(eventItem: ItemStack, slots: Int, eventEnchants: MutableMap<Enchantment, Int>, cost: Int, levels: Int) {
        for (x in 1..minOf(slots, cost)) {
            val passedRoll = (10 + minOf(levels, 75)) >= (1..100).random()
            if (!passedRoll) { continue }
            val randomGilded = getCompatibleSet(eventItem.type).random()
            if (randomGilded in eventEnchants.keys) { continue }
            val conflict = eventEnchants.keys.any { randomGilded.conflictsWith(it) }
            if (conflict) { continue }
            if (!randomGilded.canEnchantItem(eventItem)) { continue }
            // Passed All conditions
            eventEnchants.also {
                it[randomGilded] = maxOf((1..minOf(randomGilded.maximumLevel, (levels / 10) + 1)).random() - (0..2).random(), 1)
            }
        }
    }

    private fun itemSlotted(event: EnchantItemEvent) {
        val eventItem = event.item
        val eventEnchants = event.enchantsToAdd
        // Item Slots
        val itemSlots = Pair(eventItem.lore()!!.count{ it == emptyEnchantSlot }, eventItem.lore()!!.count{ it == emptyGildedSlot })
        // Remove excess enchants
        var counter = 0
        val enchantsToRemove = mutableListOf<Enchantment>()
        // Keep Hint
        val hint = event.enchantmentHint
        counter += 1
        for (enchant in eventEnchants.keys) {
            if (enchant == hint) {
                continue
            }
            if (counter >= itemSlots.first) {
                enchantsToRemove.add(enchant)
                counter += 1
            }
        }
        enchantsToRemove.forEach { eventEnchants.remove(it) }
        if (itemSlots.first == 0) { enchantsToRemove.add(hint) }
        // Roll Gilded Enchants
        if (itemSlots.second >= 1) { rollNewGilded(eventItem, itemSlots.second, eventEnchants, event.expLevelCost, event.enchanter.level) }
        eventItem.lore(addNewEnchantsLore(eventItem, itemSlots, eventEnchants))
    }

    private fun itemNotSlotted(event: EnchantItemEvent) {
        val eventItem = event.item
        val eventEnchants = event.enchantsToAdd
        // New Slots
        val newSlots = getMaterialSlots(eventItem.type)
        // Remove excess enchants
        var counter = 0
        val enchantsToRemove = mutableListOf<Enchantment>()
        // Keep Hint
        val hint = event.enchantmentHint
        counter += 1
        for (enchant in eventEnchants.keys) {
            if (enchant == hint) {
                continue
            }
            if (counter >= newSlots.first) {
                enchantsToRemove.add(enchant)
                counter += 1
            }
        }
        enchantsToRemove.forEach { eventEnchants.remove(it) }
        // Roll Gilded Enchants
        if (newSlots.second >= 1) { rollNewGilded(eventItem, newSlots.second, eventEnchants, event.expLevelCost, event.enchanter.level) }
        // Lore
        eventItem.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        eventItem.lore(createSlotsLore(eventItem, newSlots, eventEnchants))
    }


    private fun getApplicableEnchants(item: ItemStack, type: Material): Map<Enchantment, Int> {
        // Special Parameters
        val applicableList = item.enchantments.filter { it.key.canEnchantItem(ItemStack(type, 1)) }
        val rerollList = item.enchantments.filter { !it.key.canEnchantItem(ItemStack(type, 1)) }

        return applicableList
    }

    // Change Slots Lore - for Enchanting Slotted Item
    private fun addNewEnchantsLore(item: ItemStack, slots: Pair<Int, Int>, enchants: MutableMap<Enchantment, Int>): MutableList<Component> {
        // Get Lore
        val changedLore = item.itemMeta.lore()!!
        val startIndex = changedLore.indexOf(slotSeperator) - 1
        // Lore Components
        val total = slots.first + slots.second
        val slotsUsed = MutablePair(0, 0)
        enchants.forEach {
            if (it.key !is OdysseyEnchantment) {
                changedLore[(startIndex + 2) + slotsUsed.left] = it.key.displayName(it.value).color(ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                slotsUsed.left += 1
            }
            else if (it.key is OdysseyEnchantment) {
                changedLore[(startIndex + 2) + slots.first + slotsUsed.right] = (it.key as OdysseyEnchantment).displayLore(it.value)
                slotsUsed.right += 1
            }
        }
        val used = slotsUsed.left + slotsUsed.right
        val slotHeader = Component.text("Enchantment Slots: [$used/$total]", ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        changedLore[startIndex] = slotHeader

        return changedLore
    }

    // Create Slots Lore - for Enchanting new item
    private fun createSlotsLore(item: ItemStack, slots: Pair<Int, Int>, enchants: MutableMap<Enchantment, Int>): MutableList<Component> {
        // Get Lore or create new
        val createdLore = item.itemMeta.lore() ?: mutableListOf()
        val startIndex = 0
        //var startIndex = if (createdLore.lastIndex == -1) { 0 } else { createdLore.lastIndex }
        val total = slots.first + slots.second
        val slotsUsed = MutablePair(0, 0)
        // Lore Components
        createdLore.add(startIndex, Component.text(""))
        createdLore.add(startIndex + 1, slotSeperator)
        for (x in 1..slots.first) { createdLore.add(emptyEnchantSlot) }
        for (x in 1..slots.second) { createdLore.add(emptyGildedSlot) }
        enchants.forEach {
            if (it.key !is OdysseyEnchantment) {
                createdLore[(startIndex + 2) + slotsUsed.left] = it.key.displayName(it.value).color(ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                slotsUsed.left += 1
            }
            else if (it.key is OdysseyEnchantment) {
                createdLore[(startIndex + 2) + slots.first + slotsUsed.right] = (it.key as OdysseyEnchantment).displayLore(it.value)
                slotsUsed.right += 1
            }
        }
        val used = slotsUsed.left + slotsUsed.right
        val slotHeader = Component.text("Enchantment Slots: [$used/$total]", ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        createdLore[0] = slotHeader

        return createdLore
    }

    // Create Empty Slots Lore
    private fun createEmptySlotsLore(item: ItemStack, slots: Pair<Int, Int>): MutableList<Component> {
        val createdLore = mutableListOf<Component>()
        val total = slots.first + slots.second
        val slotHeader = Component.text("Enchantment Slots: [0/$total]", ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        createdLore.add(0, slotHeader)
        createdLore.add(1, slotSeperator)
        for (c in 1..slots.first) { createdLore.add(emptyEnchantSlot) }
        for (c in 1..slots.second) { createdLore.add(emptyGildedSlot) }

        return createdLore
    }

    // Remove
    private fun removeEnchantsLore(item: ItemStack, enchants: MutableMap<Enchantment, Int>): MutableList<Component> {
        val changedLore = item.itemMeta.lore()!!
        val emptyEnchantSlots = changedLore.count { it == emptyEnchantSlot }
        val emptyGildedSlots = changedLore.count { it == emptyGildedSlot }
        val enchantmentCount = enchants.keys.count { it !is OdysseyEnchantment }
        val gildedCount = enchants.keys.count { it is OdysseyEnchantment }
        val total = gildedCount + enchantmentCount + emptyGildedSlots + emptyEnchantSlots
        // Counts
        val startIndex = changedLore.indexOf(slotSeperator) - 1
        val endRangeEnchant = startIndex + 1 + emptyEnchantSlots + enchantmentCount
        for (x in (startIndex + 2)..(endRangeEnchant)) {
            changedLore[x] = emptyEnchantSlot
        }
        for (y in (endRangeEnchant + 1)..(endRangeEnchant + gildedCount + emptyGildedSlots)) {
            changedLore[y] = emptyGildedSlot
        }

        val slotHeader = Component.text("Enchantment Slots: [0/$total]", ENCHANT_COLOR).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        changedLore[0] = slotHeader

        return changedLore
    }

    // Get the compatible enchants for items
    private fun getCompatibleSet(itemType: Material): Set<OdysseyEnchantment> {
        when(itemType) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                return OdysseyEnchantments.MELEE_SET
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                return OdysseyEnchantments.ARMOR_SET
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                return OdysseyEnchantments.ARMOR_SET
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                return OdysseyEnchantments.ARMOR_SET
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET -> {
                return OdysseyEnchantments.ARMOR_SET
            }
            Material.BOW, Material.CROSSBOW -> {
                return OdysseyEnchantments.RANGED_SET
            }
            Material.ELYTRA, Material.SHIELD -> {
                return OdysseyEnchantments.MISC_SET
            }
            else -> {
                return OdysseyEnchantments.MISC_SET
            }
        }
    }

    // Get slots based on material and return Enchant-Gilded Pair
    private fun getMaterialSlots(itemType: Material): Pair<Int, Int> {
        var gildedSlots = 0
        val enchantSlots = when(itemType) {
            Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_HOE,
            Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET -> {
                gildedSlots = (0..1).random()
                minOf(3 + (0..2).random() - gildedSlots, 2)
            }
            Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE,
            Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET -> {
                gildedSlots = (0..1).random()
                minOf(3 + (0..2).random() - gildedSlots, 3)
            }
            Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET -> {
                gildedSlots = 1
                minOf(4 + (0..2).random() - gildedSlots, 3)
            }
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE, Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET -> {
                gildedSlots = 1 + (0..1).random()
                minOf(3 + (0..3).random() - gildedSlots, 4)
            }
            Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                minOf(2 + (0..2).random() - gildedSlots, 2)
            }
            Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE,
            Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                minOf(4 + (0..4).random() - gildedSlots, 4)
            }
            Material.ELYTRA, Material.SHIELD, Material.FISHING_ROD, Material.TRIDENT -> {
                gildedSlots = 1 + (0..1).random()
                4
            }
            Material.BOW, Material.CROSSBOW -> {
                gildedSlots = 1 + (0..2).random()
                4 + (0..2).random() - gildedSlots
            }
            else -> {
                2
            }
        }
        return Pair(enchantSlots, gildedSlots)
    }


}