
package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.items.OdysseyWeapons
import me.shadowalzazel.mcodyssey.items.misc.GildedBook
import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
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

object OdysseyGildingListeners : Listener {

    // Colors
    private val separatorColor = TextColor.color(170, 170, 170)
    private val experienceEnchantColor = TextColor.color(191, 255, 189)
    private val soulEnchantColor = TextColor.color(102, 255, 222)
    private val gildedEnchantColor = TextColor.color(255, 170, 0)
    private val statColor = TextColor.color(167, 125, 255)
    // Headers
    private val statHeader = Component.text("Weapon in Main Hand: " , separatorColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val loreSeparator = Component.text("----------------------" , separatorColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    // Damage
    private val baseDamage = Component.text("Base Damage: " , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val piercingDamage = Component.text("Piercing Bonus: " , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val bludgeoningDamage = Component.text("Bludgeoning Bonus: " , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val laceratingDamage = Component.text("Lacerating Bonus: " , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val cleavingDamage = Component.text("Cleaving Bonus: " , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    // Creation
    private val createdBySeparator = Component.text("Created By: " , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    // Slots
    private val emptyGildedSlot = Component.text("+ Empty Gilded Slot", gildedEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val emptyEnchantSlot = Component.text("+ Empty Enchantment Slot", separatorColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)


    // Get slots based on material
    private fun createSlotCounts(itemType: Material): Pair<Int, Int> {
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
                2 + (0..3).random() - gildedSlots
            }
            Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET -> {
                gildedSlots = 1
                3 + (0..3).random() - gildedSlots
            }
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE, Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET -> {
                gildedSlots = 1 + (0..1).random()
                3 + (0..4).random() - gildedSlots
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

    // Create new lore
    private fun createEnchantSlotsLore(enchantSlots: Int, gildedSlots: Int, enchantments: Map<Enchantment, Int>?): List<Component> {
        val fullLore = mutableListOf<Component>()
        val totalSlots = enchantSlots + gildedSlots
        // Slot Counter
        val slotComponent = Component.text("Enchantment Slots: [0/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        fullLore.add(slotComponent)
        fullLore.add(loreSeparator)

        // Add regular enchant slots and enchantments if applicable
        var counter = 0
        var usedGildedSlots = 0
        var usedEnchantSlots = 0
        if (enchantments != null) {
            // Count and add slots for enchantments
            val odysseyEnchants = mutableMapOf<OdysseyEnchantment, Int>()
            for (enchant in enchantments) {
                if (enchant.key !is OdysseyEnchantment) {
                    fullLore.add(enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                    usedEnchantSlots += 1
                }
                else {
                    odysseyEnchants[enchant.key as OdysseyEnchantment] = enchant.value
                }
                counter += 1
            }
            if (usedEnchantSlots < enchantSlots) { for (x in ((usedEnchantSlots + 1)..enchantSlots)) { fullLore.add(emptyEnchantSlot) } }
            for (enchant in odysseyEnchants) {
                fullLore.add(enchant.key.displayName(enchant.value).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                usedGildedSlots += 1
            }
            if (usedGildedSlots < gildedSlots) { for (x in ((usedGildedSlots + 1)..gildedSlots)) { fullLore.add(emptyGildedSlot) } }
        }
        // Add empty slots
        else {
            for (x in (1..enchantSlots)) { fullLore.add(emptyEnchantSlot) }
            for (x in (1..gildedSlots)) { fullLore.add(emptyGildedSlot) }
        }

        // Create info lore
        fullLore[0] = Component.text("Enchantment Slots: [$counter/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

        return fullLore
    }

    // Get Compatible sets
    private fun compatibleSet(itemType: Material): Set<OdysseyEnchantment> {
        when(itemType) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                return OdysseyEnchantments.meleeSet
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                return OdysseyEnchantments.armorSet
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                return OdysseyEnchantments.armorSet
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                return OdysseyEnchantments.armorSet
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET -> {
                return OdysseyEnchantments.armorSet
            }
            Material.BOW, Material.CROSSBOW -> {
                return OdysseyEnchantments.rangedSet
            }
            Material.ELYTRA, Material.SHIELD -> {
                return OdysseyEnchantments.miscSet
            }
            else -> {
                return OdysseyEnchantments.miscSet
            }
        }
    }

    // Rolls for gilded enchants
    private fun rollGilded(enchantingItem: ItemStack, gildedSlots: Int, tableEnchants: Map<Enchantment, Int>): MutableMap<Enchantment, Int> {
        val addedEnchants = tableEnchants.toMutableMap()
        val newGildedEnchants = mutableMapOf<Enchantment, Int>()
        for (x in 1..gildedSlots) {
            if (7 >= (1..10).random()) {
                val possibleEnchant = compatibleSet(enchantingItem.type).random()
                if (possibleEnchant in addedEnchants) { break }
                var conflict = false
                addedEnchants.keys.forEach { if (possibleEnchant.conflictsWith(it)) { conflict = true } }
                if (possibleEnchant.canEnchantItem(enchantingItem) && !conflict) {
                    val randomInt = (1..possibleEnchant.maxLevel).random()
                    addedEnchants[possibleEnchant] = randomInt
                    newGildedEnchants[possibleEnchant] = randomInt
                }
            }
        }
        return newGildedEnchants
    }

    // Main handler for grindstone
    @EventHandler
    fun odysseyGrindStoneHandler(event: PrepareResultEvent) {
        if (event.inventory is GrindstoneInventory) {
            with(event.inventory as GrindstoneInventory) {
                // Checks if upper or lower item has enchants
                if ((upperItem?.enchantments != null || lowerItem?.enchantments != null) && result != null) {
                    // TEMP
                    if (upperItem!!.containsEnchantment(OdysseyEnchantments.GILDED_POWER)) { upperItem!!.removeEnchantment(OdysseyEnchantments.GILDED_POWER) }
                    // TEMP

                    if (result!!.lore()?.contains(loreSeparator) == true) {
                        val itemEnchants = if (upperItem?.enchantments != null) { upperItem!!.enchantments } else { lowerItem!!.enchantments }
                        //val newResult = result!!.clone()
                        val newLore = result!!.clone().lore()?.also { lore ->
                            for (enchant in itemEnchants) {
                                if (enchant.key is OdysseyEnchantment) {
                                    val gildedLore = enchant.key.displayName(enchant.value).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                    val someIndex = lore.indexOf(gildedLore)
                                    lore[someIndex] = emptyGildedSlot
                                }
                                else {
                                    val enchantLore = enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                    val someIndex = lore.indexOf(enchantLore)
                                    lore[someIndex] = emptyEnchantSlot
                                }
                            }
                            val totalSlots = lore.count{ it == emptyEnchantSlot } + lore.count{ it == emptyGildedSlot }
                            val infoIndex = lore.indexOf(loreSeparator) - 1
                            lore[infoIndex] = Component.text("Enchantment Slots: [0/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                        }
                        event.result = result!!.clone().apply { lore(newLore) }
                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                    }
                }
            }
        }
    }

    // TODO: Reveal/Apply Slots on _ table
    // Bug where if added name before slots removes name

    // Main handler for enchant event
    @EventHandler
    fun odysseyEnchantmentHandler(event: EnchantItemEvent) {
        // TODO: Check if block state soul table for different effects
        with(event.inventory as EnchantingInventory) {
            // Add new enchantment lore and slots if not identified
            if (item!!.type != Material.BOOK) {
                if (item!!.lore()?.contains(loreSeparator) != true) {
                    // Create new slots
                    val someSlots = createSlotCounts(item!!.type)
                    // Remove excess enchants
                    val newEnchants = event.enchantsToAdd.also { enchants ->
                        var counter = 0
                        val enchantsToRemove = mutableListOf<Enchantment>()
                        enchants.keys.forEach { enchantKey ->
                            if (counter >= someSlots.first) { enchantsToRemove.add(enchantKey) }
                            counter += 1
                        }
                        for (removed in enchantsToRemove) { enchants.remove(removed) }
                        if (someSlots.second >= 1) { rollGilded(item!!, someSlots.second, enchants).forEach { gilded -> enchants[gilded.key] = gilded.value } }
                    }
                    // Create new lore and hide vanilla enchant display
                    item!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    item!!.lore(createEnchantSlotsLore(someSlots.first, someSlots.second, newEnchants))
                }
                // Adds enchants to slots
                else if (item!!.lore()?.contains(loreSeparator) == true) {
                    val itemLore = item!!.lore()!!
                    val enchantSlots = itemLore.count{ it == emptyEnchantSlot }
                    val gildedSlots = itemLore.count{ it == emptyGildedSlot }
                    println("Unmodified Enchants: ${event.enchantsToAdd}")
                    val newEnchants = event.enchantsToAdd.also { enchants ->
                        var counter = 0
                        val enchantsToRemove = mutableListOf<Enchantment>()
                        enchants.keys.forEach { enchantKey ->
                            if (counter >= gildedSlots) { enchantsToRemove.add(enchantKey) }
                            counter += 1
                        }
                        for (removed in enchantsToRemove) { enchants.remove(removed) }
                        if (gildedSlots >= 1) { rollGilded(item!!, gildedSlots, enchants).forEach { gilded -> enchants[gilded.key] = gilded.value } }
                    }
                    // Create new lore and hide vanilla enchant display
                    item!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    val newLore = (createEnchantSlotsLore(enchantSlots, gildedSlots, newEnchants).toMutableList()).also {
                        if (itemLore.contains(Component.text(""))) {
                            it.add(Component.text(""))
                            it.add(itemLore.last())
                        }
                    }
                    item!!.lore(newLore)
                    println("New Enchants: $newEnchants")
                }
            }
            // TODO: Make soul/infusion table later check structure
            else if (item!!.type == Material.BOOK) {
                if (item!!.itemMeta?.hasCustomModelData() == true) {
                    if (item!!.itemMeta!!.customModelData == ItemModels.ARCANE_BOOK) {
                        // TODO: Make level 1-3 each do common, rare, exotic arcane books
                        var randomTome: OdysseyItem? = null
                        val expScaleOffset = minOf(event.expLevelCost + (maxOf(30, event.enchanter.level) - 30), 95)
                        when ((0..100).random() - expScaleOffset) {
                            in -200..-80 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_AVARICE, OdysseyBooks.TOME_OF_EUPHONY).random()
                            }
                            in -81..5 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_EXPENDITURE, OdysseyBooks.TOME_OF_REPLICATION).random()
                            }
                            in 6..30 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_HARMONY, OdysseyBooks.TOME_OF_PROMOTION).random()
                            }
                            in 31..70 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_DISCHARGE, OdysseyBooks.TOME_OF_EMBRACE).random()
                            }
                            in 71..200 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_BANISHMENT).random()
                            }
                        }
                        item = randomTome?.createItemStack(1)
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
                        event.enchanter.level -= (3 + (event.expLevelCost / 10))
                        event.isCancelled = true
                    }
                }
            }
        }
    }


    // Main handler for anvil
    @EventHandler
    fun odysseyAnvilHandler(event: PrepareAnvilEvent) {
        with(event.inventory) {
            // Anvil Combination
            if (firstItem != null && secondItem != null) {
                if (firstItem!!.itemMeta?.hasEnchants() == true || secondItem!!.itemMeta?.hasEnchants() == true || secondItem!!.type == Material.ENCHANTED_BOOK) {
                    // Checks if it has gilded enchant
                    var itemOneGilded = false
                    val currentGildedEnchants: MutableMap<Enchantment, Int> = mutableMapOf()
                    // If first enchant has gilded enchants add to map
                    for (enchant in firstItem!!.enchantments) {
                        if (enchant.key is OdysseyEnchantment) {
                            itemOneGilded = true
                            currentGildedEnchants[enchant.key] = enchant.value
                        }
                    }
                    // Checks if item two has gilded enchants
                    var itemTwoGilded = false
                    for (enchantKey in secondItem!!.enchantments.keys) { if (enchantKey is OdysseyEnchantment) { itemTwoGilded = true } }
                    // If item two gilded cancel event
                    if (itemTwoGilded) {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach { viewer ->
                            if (viewer is Player) {
                                viewer.updateInventory()
                                viewer.sendActionBar(Component.text("Cannot add gilded enchants with the anvil!", TextColor.color(255, 255, 85)))
                            }
                        }
                        return
                    }
                    // Add if only one gilded item
                    if (firstItem!!.type != Material.ENCHANTED_BOOK) {
                        // Add new slots if first time
                        if (firstItem!!.lore()?.contains(loreSeparator) != true) {
                            // Create new slots
                            var enchantSlots = 2
                            var gildedSlots = 1
                            for (enchant in event.result!!.clone().enchantments) {
                                if (enchant.key in OdysseyEnchantments.registeredSet) { gildedSlots += 1 } else { enchantSlots += 1 }
                            }
                            // Create new lore add slots and hide vanilla enchant display
                            event.result!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                            event.result!!.lore(createEnchantSlotsLore(enchantSlots, gildedSlots, event.result!!.enchantments))
                            event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                        }
                        // Add book if slotted
                        else if (firstItem!!.lore()?.contains(loreSeparator) == true) {
                            // Checks if item has gilded enchant
                            if (itemOneGilded) { event.result!!.addUnsafeEnchantments(currentGildedEnchants) } // TODO: remove tome detect
                            //
                            val newLore = firstItem!!.clone().lore()!!.also { lore ->
                                // Index
                                val infoIndex = lore.indexOf(loreSeparator) - 1
                                // Manage Slots
                                val emptySlots = lore.count{ it == emptyEnchantSlot }
                                val emptyGildedSlots = lore.count{ it == emptyGildedSlot }
                                var usedGildedSlots = currentGildedEnchants.size
                                var usedSlots = firstItem!!.enchantments.size - usedGildedSlots
                                val gildedSlots = emptyGildedSlots + usedGildedSlots
                                val enchantSlots = emptySlots + usedSlots
                                val totalSlots = gildedSlots + enchantSlots
                                // Loop over all enchants to either add lore or add to removal enchants if over enchant slots
                                val enchantsToRemove = mutableListOf<Enchantment>()
                                event.result!!.enchantments.forEach {
                                    if (it.key !is OdysseyEnchantment) {
                                        val lowestLevel = if (it.key in firstItem!!.enchantments.keys) { firstItem!!.getEnchantmentLevel(it.key) } else { it.value }
                                        val enchantLore = it.key.displayName(lowestLevel).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        if (!lore.contains(enchantLore) && usedSlots < enchantSlots) {
                                            val emptySlotIndex = lore.indexOf(emptyEnchantSlot)
                                            lore[emptySlotIndex] = enchantLore
                                            usedSlots += 1
                                        }
                                        else if (lore.contains(enchantLore)) {
                                            val usedSlotIndex = lore.indexOf(enchantLore)
                                            val replacementLore = it.key.displayName(it.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                            lore[usedSlotIndex] = replacementLore
                                        }
                                        else {
                                            enchantsToRemove.add(it.value, it.key)
                                        }
                                    }
                                    else if (it.key is OdysseyEnchantment) {
                                        // Should not work but OK
                                        val gildedLore = it.key.displayName(it.value).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        if (!lore.contains(gildedLore) && usedGildedSlots < emptyGildedSlots) {
                                            val emptySlotIndex = lore.indexOf(emptyGildedSlot)
                                            lore[emptySlotIndex] = gildedLore
                                            usedGildedSlots += 1
                                        }
                                    }
                                }
                                // Do not create result if more
                                if (event.result!!.enchantments.size > totalSlots) {
                                    event.result = ItemStack(Material.AIR, 1)
                                    event.viewers.forEach { viewer ->
                                        if (viewer is Player) {
                                            viewer.updateInventory()
                                            viewer.sendActionBar(Component.text("There are not enough slots to combine the items!", TextColor.color(255, 255, 85)))
                                        }
                                    }
                                    return
                                }
                                // Remove excess enchants if any
                                enchantsToRemove.forEach {
                                    event.result!!.enchantments.remove(it)
                                }
                                lore[infoIndex] = Component.text("Enchantment Slots: [${usedSlots + usedGildedSlots}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                            }
                            event.result!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                            event.result!!.lore(newLore)
                        }
                    }
                }
            }
            // TODO: Fix
            else if (firstItem != null && secondItem == null) {
                if (renameText != null) {
                    if (firstItem!!.lore()?.contains(loreSeparator) == true) {
                        val renamedItem = firstItem!!.clone().also {
                            it.itemMeta.displayName(Component.text(renameText!!))
                            val someMeta = it.itemMeta
                            someMeta.displayName(Component.text(renameText!!))
                            it.itemMeta = someMeta
                        }
                        event.result = renamedItem
                    }
                }
            }
        }
    }


    // Main Handler that handles Odyssey Smithing: Needs corresponding smithing recipe
    @EventHandler
    fun odysseySmithingHandler(event: PrepareSmithingEvent) {
        // TODO: Rose Gold Armor upgrades
        // TODO: Smithing table for attributes and modifiers
        // With event inventory
        with(event.inventory) {
            if (inputEquipment != null && inputMineral != null) {
                // -----------------------------------------------CUSTOM NON-NETHERITE--------------------------------------------------
                // Checks if an odyssey weapon is going to be upgraded to cancel it
                if (inputMineral!!.type == Material.NETHERITE_INGOT) {
                    if (inputEquipment!!.itemMeta?.hasCustomModelData() == true) {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                    }
                }
                // -----------------------------------------------SOUL-STEEL--------------------------------------------------
                // TODO: Detect Iron sword not any sword
                else if (inputMineral!!.type == Material.IRON_INGOT && inputEquipment!!.type == Material.IRON_SWORD) {
                    if (inputEquipment!!.itemMeta?.hasCustomModelData() == true && inputMineral == OdysseyItems.SOUL_STEEL_INGOT.createItemStack(inputMineral!!.amount)) {
                        val oldMeta = inputEquipment!!.itemMeta
                        when (oldMeta.customModelData) {
                            ItemModels.IRON_KATANA -> {
                                event.result = OdysseyWeapons.SOUL_STEEL_KATANA.createItemStack(1).also {
                                   // it.itemMeta = inputEquipment!!.itemMeta
                                    it.lore(oldMeta.lore())
                                    for (enchant in oldMeta.enchants) { it.addEnchantment(enchant.key, enchant.value) }
                                    for (flag in oldMeta.itemFlags) { it.addItemFlags(flag) }
                                    it.itemMeta.displayName(oldMeta.displayName())
                                }
                            }
                        }
                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                    }
                }

                // -----------------------------------------------LEGACY BOOK ACTIVATION--------------------------------------------------
                // Activate model for book
                else if (inputEquipment!!.type == Material.ENCHANTED_BOOK && inputMineral!!.type == Material.GOLD_INGOT) {
                    var gildedEnchant: OdysseyEnchantment? = null
                    var gildedLevel = 0
                    for (enchant in inputEquipment!!.enchantments) {
                        if (enchant.key is OdysseyEnchantment) {
                            gildedEnchant = enchant.key as OdysseyEnchantment
                            gildedLevel = enchant.value
                            break
                        }
                    }
                    if (gildedEnchant != null) {
                        event.result = OdysseyBooks.GILDED_BOOK.createGildedBook(gildedEnchant, gildedLevel)
                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                    }
                }
                // -----------------------------------------------BRANDING WITH AMETHYST--------------------------------------------------
                // Custom Branding for items not books
                else if (inputEquipment!!.type != Material.ENCHANTED_BOOK && inputMineral!!.type == Material.AMETHYST_SHARD) {
                    val namedMeta = inputEquipment!!.itemMeta.clone().also {
                        // Create an empty component and add viewers names if null, and if not branded
                        val forgerLore = mutableListOf(Component.text(""))
                        for (viewer in event.viewers) { forgerLore.add(Component.text("Created by ${viewer.name}", TextColor.color(141, 109, 209), TextDecoration.ITALIC)) }
                        if (it.lore() == null) { it.lore(forgerLore as List<Component>?) }
                        else if (!it.lore()!!.contains(Component.text(""))) { it.lore(it.lore()!! + forgerLore) }
                    }
                    event.result = inputEquipment!!.clone().apply { itemMeta = namedMeta }
                    event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                }
                // -----------------------------------------------ADDING BOOKS TOGETHER--------------------------------------------------
                // TODO: Make this runic table check later
                else if (inputEquipment!!.type == Material.ENCHANTED_BOOK && inputMineral!!.type == Material.ENCHANTED_BOOK) {
                    // Checks custom model book
                    if (inputMineral!!.itemMeta?.hasCustomModelData() == true && event.viewers.size == 1) {
                        when (inputMineral!!.itemMeta!!.customModelData) {
                            // Gilded Book
                            ItemModels.GILDED_BOOK -> {
                                if (inputEquipment!!.itemMeta.hasEnchants() && inputMineral!!.itemMeta.hasEnchants()) {
                                    val firstBookEnchants = inputEquipment!!.enchantments
                                    val secondBookEnchants = inputMineral!!.enchantments
                                    for (enchantKey in firstBookEnchants.keys) {
                                        if (secondBookEnchants.containsKey(enchantKey) && enchantKey is OdysseyEnchantment) {
                                            if (firstBookEnchants[enchantKey]!! < enchantKey.maxLevel && secondBookEnchants[enchantKey]!! < enchantKey.maxLevel) {
                                                val maxLevel = max(firstBookEnchants[enchantKey]!!, secondBookEnchants[enchantKey]!!)
                                                event.result = GildedBook.createGildedBook(enchantKey, maxLevel + 1)
                                                event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                            }
                                        }
                                    }
                                }
                            }
                            // Tome of Promotion
                            ItemModels.TOME_OF_PROMOTION -> {
                                // Checks book meta and enchant
                                val randomEnchant: Pair<Enchantment, Int>? = if (inputEquipment!!.itemMeta.hasEnchants()) {
                                    inputEquipment!!.enchantments.toList().random() }
                                else if ((inputEquipment!!.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
                                    (inputEquipment!!.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().random() }
                                else {
                                    null
                                }
                                if (randomEnchant != null) {
                                    if (randomEnchant.first.maxLevel > randomEnchant.second) {
                                        event.result = inputEquipment!!.clone().apply {
                                            if (randomEnchant.first is OdysseyEnchantment) {
                                                val oldLore = randomEnchant.first.displayName(randomEnchant.second).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                val someIndex = lore()!!.indexOf(oldLore)
                                                val newLore = lore()!!.also { it[someIndex] = randomEnchant.first.displayName(randomEnchant.second + 1).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
                                                lore(newLore)
                                                removeEnchantment(randomEnchant.first)
                                                addEnchantment(randomEnchant.first, randomEnchant.second + 1)
                                            }
                                            else {
                                                val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                                                newMeta.removeStoredEnchant(randomEnchant.first)
                                                newMeta.addStoredEnchant(randomEnchant.first, randomEnchant.second + 1, false)
                                                itemMeta = newMeta
                                            }
                                        }
                                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                    }
                                }
                            }
                            // Tome of Replication
                            ItemModels.TOME_OF_REPLICATION -> {
                                if (inputEquipment!!.itemMeta.hasEnchants() || (inputEquipment!!.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
                                    inputMineral = inputEquipment!!.clone()
                                }
                            }
                        }
                    }
                }
                // --------------------------------------------------ADDING BOOKS TO ITEMS---------------------------------------------------------
                // TODO: Make this runic table check later
                else if (inputEquipment!!.type != Material.ENCHANTED_BOOK && inputMineral!!.type == Material.ENCHANTED_BOOK) {
                    // Checks for books and tomes
                    if (inputMineral!!.itemMeta?.hasCustomModelData() == true) {
                        when (inputMineral!!.itemMeta!!.customModelData) {
                            // Gilded book
                            ItemModels.GILDED_BOOK -> {
                                // Gilded books can only have 1 enchant !
                                var gildedEnchant: OdysseyEnchantment? = null
                                var gildedValue = 0
                                val bookEnchants = inputMineral!!.enchantments
                                var upgradableEnchant = false
                                var oldValue = 0
                                // Checks if odyssey enchant has no conflicts or max level
                                for (enchant in bookEnchants) {
                                    val enchantKey = enchant.key
                                    if (enchantKey is OdysseyEnchantment && enchantKey.canEnchantItem(inputEquipment!!)) {
                                        // Checks if equipment has gilded enchant
                                        inputEquipment!!.enchantments.let { equipmentEnchants ->
                                            if (enchantKey in equipmentEnchants.keys) {
                                                val proposedLevel = if (bookEnchants[enchantKey]!! == equipmentEnchants[enchantKey]!!) {
                                                    bookEnchants[enchantKey]!! + 1
                                                } else {
                                                    maxOf(bookEnchants[enchantKey]!!, equipmentEnchants[enchantKey]!!)
                                                }
                                                if (proposedLevel <= enchantKey.maxLevel) {
                                                    upgradableEnchant = true
                                                    oldValue = equipmentEnchants[enchantKey]!!
                                                    gildedEnchant = enchantKey
                                                    gildedValue = proposedLevel
                                                }
                                            }
                                            else {
                                                gildedEnchant = enchantKey
                                                gildedValue = enchant.value
                                            }
                                            // Checks for conflict
                                            for (itemEnchantKey in equipmentEnchants.keys) {
                                                if (enchantKey.conflictsWith(itemEnchantKey)) {
                                                    gildedEnchant = null
                                                    break
                                                }
                                            }
                                        }
                                    }
                                }
                                // Checks if gilded enchant passed tests
                                if (gildedEnchant != null) {
                                    if (inputEquipment!!.lore()?.contains(loreSeparator) == true) {
                                        // Copy and modify lore components
                                        val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                            // If same enchant
                                            if (upgradableEnchant) {
                                                val gildedLore = gildedEnchant!!.displayName(oldValue).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                val someIndex = lore.indexOf(gildedLore)
                                                lore[someIndex] = gildedEnchant!!.displayName(gildedValue).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                            }
                                            // New application
                                            else {
                                                val emptyGildedSlots = lore.count{ it == emptyGildedSlot }
                                                if (emptyGildedSlots > 0) {
                                                    val slotIndex = lore.indexOf(emptyGildedSlot)
                                                    lore[slotIndex] = gildedEnchant!!.displayName(gildedValue).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                }
                                            }
                                            // Change info
                                            val infoIndex = lore.indexOf(loreSeparator) - 1
                                            val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size + 1
                                            lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size + 1}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        }
                                        event.result = inputEquipment!!.clone().apply {
                                            lore(newLore)
                                            if (upgradableEnchant) { removeEnchantment(gildedEnchant!!) }
                                            addUnsafeEnchantment(gildedEnchant!!, gildedValue)
                                        }
                                        println(event.result!!.enchantments)
                                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                    }
                                    // Set to air if no slots activated
                                    else {
                                        event.result = ItemStack(Material.AIR, 1)
                                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                    }
                                }
                            }
                            // Tome of Embrace
                            ItemModels.TOME_OF_EMBRACE -> {
                                if (inputEquipment!!.lore()?.contains(loreSeparator) == true) {
                                    val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                        // Change info
                                        val infoIndex = lore.indexOf(loreSeparator) - 1
                                        val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size
                                        lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size}/${totalSlots + 1}]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        // Add Slot
                                        var nonOdysseyCount = 0
                                        for (enchant in inputEquipment!!.enchantments) { if (enchant !is OdysseyEnchantment) {nonOdysseyCount += 1 } }
                                        val newSlotIndex = infoIndex + 1 + 1
                                        lore.add(newSlotIndex, emptyEnchantSlot)
                                    }
                                    event.result = inputEquipment!!.clone().apply {
                                        lore(newLore)
                                    }
                                }
                            }
                            // Tome of Banishment
                            // TODO: Fix
                            ItemModels.TOME_OF_BANISHMENT -> {
                                if (inputEquipment!!.lore()?.contains(loreSeparator) == true) {
                                    var extraEnchant: Enchantment? = null
                                    val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                        // Change info
                                        val infoIndex = lore.indexOf(loreSeparator) - 1
                                        val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size
                                        //
                                        val newTotal = totalSlots - 1
                                        if (newTotal < inputEquipment!!.enchantments.size) {
                                            val randomEnchant = inputEquipment!!.enchantments.toList().random()
                                            val odysseyEnchanted = randomEnchant.first is OdysseyEnchantment
                                            // Checks book meta and enchant
                                            val randomLore = if (odysseyEnchanted) {
                                                randomEnchant.first.displayName(randomEnchant.second).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
                                            else {
                                                randomEnchant.first.displayName(randomEnchant.second).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                            }
                                            lore.remove(randomLore)
                                            extraEnchant = randomEnchant.first
                                            lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size - 1}/$newTotal]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        }
                                        else {
                                            val banishedIndex = if (lore.count { it == emptyEnchantSlot } >= 1) { lore.indexOf(emptyEnchantSlot) } else { lore.indexOf(emptyGildedSlot) }
                                            lore.removeAt(banishedIndex)
                                            lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size}/$newTotal]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        }
                                    }
                                    event.result = inputEquipment!!.clone().apply {
                                        lore(newLore)
                                        if (extraEnchant != null) { removeEnchantment(extraEnchant!!) }
                                    }
                                }
                            }
                            // Tome of Discharge
                            ItemModels.TOME_OF_DISCHARGE -> {
                                if (inputEquipment!!.lore()?.contains(loreSeparator) == true && inputEquipment!!.itemMeta?.hasEnchants() == true)  {
                                    // Gets random enchant and replaces with an empty slot
                                    val randomEnchant = inputEquipment!!.enchantments.toList().random()
                                    val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                        // Change info
                                        val infoIndex = lore.indexOf(loreSeparator) - 1
                                        val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size
                                        lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size - 1}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        // Change slot
                                        val odysseyEnchanted = randomEnchant.first is OdysseyEnchantment
                                        val randomLore = if (odysseyEnchanted) {
                                            randomEnchant.first.displayName(randomEnchant.second).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
                                        else {
                                            randomEnchant.first.displayName(randomEnchant.second).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        }

                                        val someIndex = lore.indexOf(randomLore)
                                        lore[someIndex] = if (odysseyEnchanted) { emptyGildedSlot } else { emptyEnchantSlot }
                                    }
                                    event.result = inputEquipment!!.clone().apply {
                                        lore(newLore)
                                        removeEnchantment(randomEnchant.first)
                                    }
                                    event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                }
                            }
                            // Tome of Promotion
                            ItemModels.TOME_OF_EUPHONY -> {
                                if (inputEquipment!!.lore()?.contains(loreSeparator) == true && inputEquipment!!.itemMeta?.hasEnchants() == true)  {
                                    // Check if level 0
                                    var euphonicLevels = true // Has zero levels
                                    event.viewers.forEach { viewer ->
                                        if (viewer is Player) {
                                            if (viewer.level != 0) { euphonicLevels = false }
                                            viewer.updateInventory()
                                            viewer.sendActionBar(Component.text("You Need Zero Levels to use this tome!", TextColor.color(255, 255, 85)))
                                        }
                                    }
                                    if (!euphonicLevels) {
                                        return
                                    }

                                    // Checks book meta and enchant
                                    var randomEnchant: Pair<Enchantment, Int>? = null
                                    if (inputEquipment!!.itemMeta.hasEnchants()) {
                                        randomEnchant = inputEquipment!!.enchantments.toList().random()
                                    }
                                    else if ((inputEquipment!!.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
                                        randomEnchant = (inputEquipment!!.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().random()
                                    }
                                    // Check if Not Gilded and not 1 and has empty gilded slot
                                    if (randomEnchant != null && inputEquipment!!.lore()!!.contains(emptyGildedSlot) && randomEnchant.first !is OdysseyEnchantment) {
                                        // Get range
                                        if (randomEnchant.second in (2..(randomEnchant.first.maxLevel * 2))) {
                                            val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                                // Change info
                                                val infoIndex = lore.indexOf(loreSeparator) - 1
                                                val totalSlots = lore.count{ it == emptyGildedSlot || it == emptyEnchantSlot} + inputEquipment!!.enchantments.size - 1
                                                lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                // Change slot
                                                val randomLore = randomEnchant!!.first.displayName(randomEnchant!!.second).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                val someIndex = lore.indexOf(randomLore)
                                                lore[someIndex] = randomEnchant!!.first.displayName(randomEnchant!!.second + 1).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                // TODO: Change Colors
                                                lore.remove(emptyGildedSlot)
                                            }
                                            event.result = inputEquipment!!.clone().apply {
                                                lore(newLore)
                                                removeEnchantment(randomEnchant!!.first)
                                                addUnsafeEnchantment(randomEnchant!!.first, randomEnchant!!.second + 1)
                                            }
                                            event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                        }
                                    }
                                }
                            }
                            // Tome of Promotion
                            ItemModels.TOME_OF_PROMOTION -> {
                                if (inputEquipment!!.lore()?.contains(loreSeparator) == true && inputEquipment!!.itemMeta?.hasEnchants() == true)  {
                                    // Gets random enchant and tries to promote it
                                    // Checks book meta and enchant
                                    var randomEnchant: Pair<Enchantment, Int>? = null
                                    if (inputEquipment!!.itemMeta.hasEnchants()) {
                                        randomEnchant = inputEquipment!!.enchantments.toList().random()
                                    }
                                    else if ((inputEquipment!!.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
                                        randomEnchant = (inputEquipment!!.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().random()
                                    }

                                    if (randomEnchant != null) {
                                        if (randomEnchant.first.maxLevel > randomEnchant.second) {
                                            val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                                // Change info
                                                val infoIndex = lore.indexOf(loreSeparator) - 1
                                                val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size
                                                lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                // Change slot
                                                val odysseyEnchanted = randomEnchant.first is OdysseyEnchantment
                                                val randomLore = if (odysseyEnchanted) {
                                                    randomEnchant.first.displayName(randomEnchant.second).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
                                                else {
                                                    randomEnchant.first.displayName(randomEnchant.second).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                }

                                                val someIndex = lore.indexOf(randomLore)
                                                lore[someIndex] = if (odysseyEnchanted) { randomEnchant.first.displayName(randomEnchant.second + 1).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
                                                else {
                                                    randomEnchant.first.displayName(randomEnchant.second + 1).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                }
                                            }
                                            event.result = inputEquipment!!.clone().apply {
                                                lore(newLore)
                                                removeEnchantment(randomEnchant.first)
                                                addEnchantment(randomEnchant.first, randomEnchant.second + 1)
                                            }
                                            event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                        }
                                    }
                                }
                            }

                            // Tome of Harmony
                            ItemModels.TOME_OF_HARMONY -> {
                                val newMeta = inputEquipment!!.clone().itemMeta.also {
                                    if (it is Repairable) { it.repairCost = 1 }
                                }
                                event.result = inputEquipment!!.clone().apply { itemMeta = newMeta }
                            }
                            // Tome Of Expenditure
                            // TODO: Fix, does not work for multiple gilded
                            ItemModels.TOME_OF_EXPENDITURE -> {
                                if (inputEquipment!!.itemMeta?.hasEnchants() == true) {
                                    val randomEnchant = inputEquipment!!.enchantments.toList().random()

                                    // Creates a book based on gilded or not
                                    event.result = if (randomEnchant.first is OdysseyEnchantment) { OdysseyBooks.GILDED_BOOK.createGildedBook(randomEnchant.first as OdysseyEnchantment, randomEnchant.second) }
                                    else {
                                        ItemStack(Material.ENCHANTED_BOOK, 1).clone().apply {
                                            val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                                            newMeta.removeStoredEnchant(randomEnchant.first)
                                            val limitLevel = minOf(randomEnchant.first.maxLevel, randomEnchant.second)
                                            newMeta.addStoredEnchant(randomEnchant.first, limitLevel, false)
                                            itemMeta = newMeta
                                        }
                                    }
                                    event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                }
                            }
                            // Tome Of Avarice
                            ItemModels.TOME_OF_AVARICE -> {
                                if (inputEquipment!!.itemMeta?.hasEnchants() == true && inputEquipment!!.lore()?.contains(loreSeparator) == true) {
                                    // Check if level 30
                                    var avariceLevels = true // Has zero levels
                                    event.viewers.forEach { viewer ->
                                        if (viewer is Player) {
                                            if (viewer.level < 30) { avariceLevels = false }
                                            viewer.updateInventory()
                                            viewer.sendActionBar(Component.text("You Need Thirty Levels to use this tome!", TextColor.color(255, 255, 85)))
                                        }
                                    }
                                    if (!avariceLevels) {
                                        return
                                    }
                                    // Get Slots
                                    val gildedEnchants = inputEquipment!!.enchantments.keys.count { it is OdysseyEnchantment }
                                    val gildedSlots = inputEquipment!!.lore()!!.count{ it == emptyGildedSlot }
                                    // If 2 or less gilded slots + enchants and 5 enchants
                                    if (gildedEnchants + gildedSlots <= 1 && inputEquipment!!.enchantments.size - gildedEnchants >= 5 ) {
                                        val enchantList = mutableMapOf<Enchantment, Int>()
                                        var gildedEnchant: Pair<Enchantment, Int>? = null
                                        // Get all enchants up to 5 randomly
                                        val randomEnchantMap = inputEquipment!!.enchantments.toList().shuffled().toMap().toMutableMap()

                                        for (enchant in randomEnchantMap) {
                                            if (enchant.key !in enchantList && enchant.key !is OdysseyEnchantment && enchantList.size < 5) {
                                                enchantList[enchant.key] = enchant.value
                                            }
                                            else if (enchant.key is OdysseyEnchantment) {
                                                gildedEnchant = Pair(enchant.key, enchant.value)
                                            }
                                        }

                                        val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                            // Change info
                                            val infoIndex = lore.indexOf(loreSeparator) - 1
                                            val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size - 5 + 1
                                            lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                            // Change slot
                                            enchantList.forEach { (u, v) ->
                                                val someLore = u.displayName(v).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                                lore.remove(someLore)
                                            }

                                            if (gildedEnchant != null) {
                                                val someIndex = lore.indexOf(gildedEnchant!!.first.displayName(gildedEnchant!!.second).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                                                lore.add(someIndex, emptyGildedSlot)
                                            }
                                            else {
                                                // TODO: FIX
                                                val newIndex = infoIndex + 2 + totalSlots - 1 - 1
                                                lore[newIndex] = emptyGildedSlot
                                                lore.add(newIndex, emptyGildedSlot)
                                            }
                                        }
                                        event.result = inputEquipment!!.clone().apply {
                                            for (x in enchantList) { removeEnchantment(x.key) }
                                            lore(newLore)
                                        }
                                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                    }
                                }
                            }
                        }
                        // TAKE ENCHANTS TO REPAIR ITEM? SLOTS?
                        // UP TO 2
                        // MAYBE CONSUME ALL SLOTS FOR GILDED?
                    }
                }
            }
        }

    }
}