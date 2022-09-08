
package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import me.shadowalzazel.mcodyssey.items.misc.GildedBook
import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.assets.CustomModels
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
    // Components
    private val loreSeparator = Component.text("----------------------" , separatorColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val emptyGildedSlot = Component.text("+ Empty Gilded Slot", gildedEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    private val emptyEnchantSlot = Component.text("+ Empty Enchantment Slot", separatorColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)


    // Get slots based on material
    private fun createSlotCounts(itemType: Material): List<Int> {
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
            Material.ELYTRA, Material.SHIELD, Material.FISHING_ROD -> {
                gildedSlots = 1 + (0..1).random()
                4
            }
            Material.BOW, Material.CROSSBOW, Material.TRIDENT -> {
                gildedSlots = 1 + (0..1).random()
                4 + (0..2).random() - gildedSlots
            }
            else -> {
                2
            }
        }
        println(listOf(enchantSlots, gildedSlots))
        return listOf(enchantSlots, gildedSlots)
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
                    val slotList = createSlotCounts(item!!.type)
                    val enchantSlots = slotList[0]
                    val gildedSlots = slotList[1]
                    // Remove excess enchants
                    val newEnchants = event.enchantsToAdd.also {
                        var counter = 0
                        val enchantsToRemove = mutableListOf<Enchantment>()
                        for (enchant in it) {
                            if (counter >= enchantSlots) { enchantsToRemove.add(enchant.key) }
                            counter += 1
                        }
                        for (removed in enchantsToRemove) { it.remove(removed) }
                    }
                    if ((25 + (gildedSlots * 25) >= (0..100).random()) && MinecraftOdyssey.instance.ambassadorDefeated && gildedSlots > 1) {
                        val possibleEnchant = OdysseyEnchantments.meleeSet.random()
                        var conflict = false
                        for (newKey in newEnchants.keys) { if (possibleEnchant.conflictsWith(newKey)) { conflict = true } }
                        if (possibleEnchant.canEnchantItem(item!!) && !conflict) {
                            newEnchants[possibleEnchant] = (1..possibleEnchant.maxLevel).random()
                        }
                    }
                    // Create new lore and hide vanilla enchant display
                    item!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    item!!.lore(createEnchantSlotsLore(enchantSlots, gildedSlots, newEnchants))
                }
                // Adds enchants to slots
                else if (item!!.lore()?.contains(loreSeparator) == true) {
                    val newLore = item!!.lore()!!.also { lore ->
                        val enchantSlots = lore.count{ it == emptyEnchantSlot }
                        val gildedSlots = lore.count{ it == emptyGildedSlot }
                        val totalSlots = enchantSlots + gildedSlots
                        val infoIndex = lore.indexOf(loreSeparator) - 1

                        // Loop over all enchants to either add lore or add to removal enchants if over enchant slots
                        val enchantsToRemove = mutableListOf<Enchantment>()
                        val enchantsToAdd = event.enchantsToAdd
                        var counter = 0
                        var usedSlots = 0
                        // For non odyssey enchants
                        for (enchant in enchantsToAdd) {
                            if (usedSlots < enchantSlots) {
                                if (enchant.key !is OdysseyEnchantment && lore[infoIndex + 2 + counter] == emptyEnchantSlot) {
                                    lore[infoIndex + 2 + counter] = enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                }
                                usedSlots += 1
                            }
                            else {
                                enchantsToRemove.add(enchant.key)
                            }
                            counter += 1
                        }
                        for (removed in enchantsToRemove) { event.enchantsToAdd.remove(removed) }
                        // Do a rng check for gilded enchant if empty slot
                        if (gildedSlots > 1) {
                            if ((25 + (gildedSlots * 25) >= (0..100).random()) && MinecraftOdyssey.instance.ambassadorDefeated) {
                                // TODO: Separate Sets per item
                                val possibleEnchant = OdysseyEnchantments.meleeSet.random()
                                var conflict = false
                                for (newKey in enchantsToAdd.keys) { if (possibleEnchant.conflictsWith(newKey)) { conflict = true } }
                                if (possibleEnchant.canEnchantItem(item!!) && !conflict) {
                                    val randomValue =  (1..possibleEnchant.maxLevel).random()
                                    enchantsToAdd[possibleEnchant] = randomValue
                                    val emptyGildedIndex = lore.indexOf(emptyGildedSlot)
                                    lore[emptyGildedIndex] = possibleEnchant.displayName(randomValue).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                    usedSlots += 1
                                }
                            }
                        }
                        lore[infoIndex] = Component.text("Enchantment Slots: [$usedSlots/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                    }
                    item!!.lore(newLore)
                }
            }
            // TODO: Make soul/infusion table later check structure
            else if (item!!.type == Material.BOOK) {
                if (item!!.itemMeta?.hasCustomModelData() == true) {
                    if (item!!.itemMeta!!.customModelData == CustomModels.ARCANE_BOOK) {
                        // TODO: Make level 1-3 each do common, rare, exotic arcane books
                        event.isCancelled = true
                        var randomTome: OdysseyItem? = null
                        when ((0..100).random() - event.expLevelCost) {
                            in -30..15 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_EXPENDITURE, OdysseyBooks.TOME_OF_REPLICATION).random()
                            }
                            in 16..40 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_HARMONY, OdysseyBooks.TOME_OF_PROMOTION).random()
                            }
                            in 41..71 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_BANISHMENT, OdysseyBooks.TOME_OF_EMBRACE).random()
                            }
                            in 71..100 -> {
                                randomTome = listOf(OdysseyBooks.TOME_OF_DISCHARGE).random()
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
                if (firstItem!!.itemMeta?.hasEnchants() == true || secondItem!!.itemMeta?.hasEnchants() == true) {
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
                    for (enchantKey in secondItem!!.enchantments.keys) { if (enchantKey in OdysseyEnchantments.enchantmentSet) { itemTwoGilded = true } }
                    // If item two gilded cancel event
                    if (itemTwoGilded) {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
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
                                if (enchant.key in OdysseyEnchantments.enchantmentSet) { gildedSlots += 1 } else { enchantSlots += 1 }
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
                                // Empty Slots
                                val emptySlots = lore.count{ it == emptyEnchantSlot }
                                // Check slots in first item
                                var occupiedSlots = 0
                                var gildedSlots = 0
                                for (enchant in firstItem!!.enchantments) { if (enchant.key is OdysseyEnchantment) { gildedSlots += 1 } else { occupiedSlots += 1 }}
                                val totalSlots = emptySlots + lore.count{ it == emptyGildedSlot } + occupiedSlots + gildedSlots
                                // Loop over all enchants to either add lore or add to removal enchants if over enchant slots
                                val enchantsToRemove = mutableListOf<Enchantment>()
                                var counter = 0
                                var usedSlots = 0
                                for (enchant in event.result!!.enchantments) {
                                    if (enchant.key !is OdysseyEnchantment) {
                                        if (usedSlots < emptySlots + occupiedSlots) {
                                            usedSlots += 1
                                            lore[infoIndex + 2 + counter] = enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        }
                                        else {
                                            enchantsToRemove.add(enchant.key)
                                        }
                                        counter += 1
                                    }
                                }
                                for (removed in enchantsToRemove) { event.result!!.enchantments.remove(removed) }
                                lore[infoIndex] = Component.text("Enchantment Slots: [${usedSlots + gildedSlots}/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                // Checks if item exceeded slots
                                println(event.result!!.enchantments)
                                println(counter)
                                println(occupiedSlots + emptySlots)
                                if (counter > occupiedSlots + emptySlots) {
                                    event.result = ItemStack(Material.AIR, 1)
                                    event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                    return
                                }
                            }
                            event.result!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                            event.result!!.lore(newLore)
                        }
                    }
                }
            }
            // TODO: Fix
            else if (firstItem != null) {
                if (renameText != null) {
                    for (enchant in firstItem!!.enchantments.keys) {
                        if (enchant in OdysseyEnchantments.enchantmentSet) {
                            // TEMP
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                        }
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
                    if (inputMineral!!.itemMeta?.hasCustomModelData() == true) {
                        when (inputMineral!!.itemMeta!!.customModelData) {
                            // Gilded Book
                            CustomModels.GILDED_BOOK -> {
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
                            CustomModels.TOME_OF_PROMOTION -> {
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
                            CustomModels.TOME_OF_REPLICATION -> {
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
                            CustomModels.GILDED_BOOK -> {
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
                            CustomModels.TOME_OF_EMBRACE -> {
                                if (inputEquipment!!.lore()?.contains(loreSeparator) == true) {
                                    val newLore = inputEquipment!!.clone().lore()!!.also { lore ->
                                        // Change info
                                        val infoIndex = lore.indexOf(loreSeparator) - 1
                                        val totalSlots = lore.count{ it == emptyGildedSlot } + lore.count{ it == emptyEnchantSlot } + inputEquipment!!.enchantments.size
                                        lore[infoIndex] = Component.text("Enchantment Slots: [${inputEquipment!!.enchantments.size}/${totalSlots + 1}]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                        // Add Slot
                                        var nonOdysseyCount = 0
                                        for (enchant in inputEquipment!!.enchantments) { if (enchant !is OdysseyEnchantment) {nonOdysseyCount += 1 } }
                                        val newSlotIndex = infoIndex + 3 + nonOdysseyCount
                                        lore.add(newSlotIndex, emptyEnchantSlot)
                                    }
                                    event.result = inputEquipment!!.clone().apply {
                                        lore(newLore)
                                    }
                                }
                            }
                            // Tome of Banishment
                            // TODO: Fix
                            CustomModels.TOME_OF_BANISHMENT -> {
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
                            CustomModels.TOME_OF_DISCHARGE -> {
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
                            CustomModels.TOME_OF_PROMOTION -> {
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
                            CustomModels.TOME_OF_HARMONY -> {
                                val newMeta = inputEquipment!!.clone().itemMeta.also {
                                    if (it is Repairable) { it.repairCost = 10 }
                                }
                                event.result = inputEquipment!!.clone().apply { itemMeta = newMeta }
                            }
                            // Tome Of Infusion
                            CustomModels.TOME_OF_EXPENDITURE -> {
                                if (inputEquipment!!.itemMeta?.hasEnchants() == true) {
                                    val randomEnchant = inputEquipment!!.enchantments.toList().random()

                                    // Creates a book based on gilded or not
                                    event.result = if (randomEnchant.first is OdysseyEnchantment) { OdysseyBooks.GILDED_BOOK.createGildedBook(randomEnchant.first as OdysseyEnchantment, randomEnchant.second) }
                                    else {
                                        ItemStack(Material.ENCHANTED_BOOK, 1).clone().apply {
                                            val newMeta = itemMeta.clone() as EnchantmentStorageMeta
                                            newMeta.removeStoredEnchant(randomEnchant.first)
                                            newMeta.addStoredEnchant(randomEnchant.first, randomEnchant.second, false)
                                            itemMeta = newMeta
                                        }
                                    }
                                    event.viewers.forEach { viewer -> if (viewer is Player) { viewer.updateInventory() } }
                                }
                            }
                        }
                        //
                    }
                }
            }
        }

    }
}