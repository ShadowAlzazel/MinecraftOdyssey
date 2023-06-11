package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.apache.commons.lang3.tuple.MutablePair
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object EnchantingListeners: Listener {

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
    fun enchantingItemHandler(event: EnchantItemEvent) {
        val isSlotted = event.item.lore()?.contains(slotSeperator) == true

        when (event.item.type) {
            Material.BOOK -> {

            }
            else -> {
                if (isSlotted) {
                    itemSlotted(event.item, event.enchantsToAdd)
                }
                else {
                    itemNotSlotted(event.item, event.enchantsToAdd)
                }
            }
        }
    }

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


    private fun enchantingSwordHandler(eventItem: ItemStack) {

    }

    private fun enchantingBookHandler(eventItem: ItemStack) {
        when(eventItem.itemMeta.customModelData) {
            ItemModels.VOLUME_OF_TOOLS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_AXES -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_AXE, 1)) }
            }
            ItemModels.VOLUME_OF_SWORDS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_POLE_ARMS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_HOE, 1)) }
            }
            ItemModels.VOLUME_OF_SPEARS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SHOVEL, 1)) }
            }
            ItemModels.VOLUME_OF_CLUBS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_PICKAXE, 1)) }
            }
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun rollNewGilded(eventItem: ItemStack, slots: Int, eventEnchants: MutableMap<Enchantment, Int>) {
        for (x in 1..slots) {
            val passedRoll = 65 >= (1..100).random()
            if (!passedRoll) { continue }
            val randomGilded = getCompatibleSet(eventItem.type).random()
            if (randomGilded in eventEnchants.keys) { continue }
            val conflict = eventEnchants.keys.any { randomGilded.conflictsWith(it) }
            if (conflict) { continue }
            if (!randomGilded.canEnchantItem(eventItem)) { continue }
            // Passed All conditions
            eventEnchants.also {
                it[randomGilded] = (1..randomGilded.maximumLevel).random()
            }
        }
    }

    private fun itemSlotted(eventItem: ItemStack, eventEnchants: MutableMap<Enchantment, Int>) {
        // Item Slots
        val itemSlots = Pair(eventItem.lore()!!.count{ it == emptyEnchantSlot }, eventItem.lore()!!.count{ it == emptyGildedSlot })
        // Remove excess enchants
        var counter = 0
        val enchantsToRemove = mutableListOf<Enchantment>()
        eventEnchants.keys.forEach { enchantment ->
            if (counter >= itemSlots.first) { enchantsToRemove.add(enchantment) }
            counter += 1
        }
        enchantsToRemove.forEach { eventEnchants.remove(it) }
        // Roll Gilded Enchants
        if (itemSlots.second >= 1) { rollNewGilded(eventItem, itemSlots.second, eventEnchants) }
        eventItem.lore(addNewEnchantsLore(eventItem, itemSlots, eventEnchants))
    }

    private fun itemNotSlotted(eventItem: ItemStack, eventEnchants: MutableMap<Enchantment, Int>) {
        // New Slots
        val newSlots = getMaterialSlots(eventItem.type)
        // Remove excess enchants
        var counter = 0
        val enchantsToRemove = mutableListOf<Enchantment>()
        eventEnchants.keys.forEach { enchantment ->
            if (counter >= newSlots.first) { enchantsToRemove.add(enchantment) }
            counter += 1
        }
        enchantsToRemove.forEach { eventEnchants.remove(it) }
        // Roll Gilded Enchants
        if (newSlots.second >= 1) { rollNewGilded(eventItem, newSlots.second, eventEnchants) }
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
        val enchantmentCount = enchants.keys.count { it is Enchantment && it !is OdysseyEnchantment }
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
                2 + (0..2).random() - gildedSlots
            }
            Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE,
            Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET -> {
                gildedSlots = (0..1).random()
                2 + (0..2).random() - gildedSlots
            }
            Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET -> {
                gildedSlots = 1
                3 + (0..2).random() - gildedSlots
            }
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE, Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET -> {
                gildedSlots = 1 + (0..1).random()
                3 + (0..3).random() - gildedSlots
            }
            Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                4 + (0..2).random() - gildedSlots
            }
            Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE,
            Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                4 + (0..4).random() - gildedSlots
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