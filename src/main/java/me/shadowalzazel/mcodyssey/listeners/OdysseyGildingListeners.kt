
package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.enchantments.utility.GildedPower
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.misc.GildedBook
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
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
import org.bukkit.inventory.meta.ItemMeta
import kotlin.math.max

object OdysseyGildingListeners : Listener {

    private var smithingCooldown: Long = 0
    private var gildingCooldown: Long = 0
    // Colors
    private val separatorColor = TextColor.color(170, 170, 170)
    private val experienceEnchantColor = TextColor.color(191, 255, 189)
    private val soulEnchantColor = TextColor.color(102, 255, 222)
    private val gildedEnchantColor = TextColor.color(255, 170, 0)
    // Numbers
    private val romanNumeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")
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
                2 + (0..1).random()
            }
            Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE,
            Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET -> {
                gildedSlots = (0..1).random()
                2 + (0..2).random()
            }
            Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET -> {
                gildedSlots = 1
                3 + (0..2).random()
            }
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE, Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET -> {
                gildedSlots = 1 + (0..1).random()
                3 + (0..3).random()
            }
            Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                3 + (0..2).random()
            }
            Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE,
            Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET -> {
                gildedSlots = 1 + (0..2).random()
                4 + (0..2).random()
            }
            Material.ELYTRA, Material.SHIELD, Material.FISHING_ROD -> {
                gildedSlots = 1 + (0..1).random()
                3
            }
            Material.BOW, Material.CROSSBOW, Material.TRIDENT -> {
                gildedSlots = 1 + (0..1).random()
                4 + (0..2).random()
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
        if (enchantments != null) {
            var counter = 0
            for (enchant in enchantments.entries) {
                //fullLore.add(Component.text("${enchant.key.name}  ${romanNumeralList[enchant.value]}", soulEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                fullLore.add(enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                counter += 1
                println(enchant)
            }
            if (counter < enchantSlots) {
                for (x in ((counter + 1)..enchantSlots)) { fullLore.add(emptyEnchantSlot) }
            }
            fullLore[0] = Component.text("Enchantment Slots: [$counter/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        else {
            for (x in (1..enchantSlots)) { fullLore.add(emptyEnchantSlot) }
        }

        // Add gilded slots
        if (gildedSlots > 0) {
            for (x in (1..gildedSlots)) {
                fullLore.add(emptyGildedSlot)
            }
        }
        return fullLore
    }


    // FIX FOR BOTTOM
    @EventHandler
    fun odysseyGrindStoneHandler(event: PrepareResultEvent) {
        if (event.inventory is GrindstoneInventory) {
            with(event.inventory as GrindstoneInventory) {
                if (upperItem?.enchantments != null && result != null) {
                    val newResult = result!!.clone()
                    val newLore = newResult.lore()?.also { lore ->
                        for (enchant in upperItem!!.enchantments) {
                            val enchantLore = enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                            val someIndex = lore.indexOf(enchantLore)
                            if (enchant.key is OdysseyEnchantment) {
                                lore[someIndex] = emptyGildedSlot
                            }
                            else {
                                lore[someIndex] = emptyEnchantSlot
                            }
                        }
                        val totalSlots = lore.count{ it == emptyEnchantSlot } + lore.count{ it == emptyGildedSlot }
                        val countIndex = lore.indexOf(loreSeparator) - 1
                        lore[countIndex] = Component.text("Enchantment Slots: [0/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                    }
                    newResult.lore(newLore)
                    result = newResult
                    event.result = newResult
                    for (viewer in event.viewers) { (viewer as Player).updateInventory() }
                }
            }
        }
    }

    // TODO: Reveal/Apply Slots on _ table
    // Bug where if added name before slots removes name

    @EventHandler
    fun odysseyEnchantmentHandler(event: EnchantItemEvent) {
        // TODO: Check if block state soul table for different effects
        with(event.inventory as EnchantingInventory) {
            // Add new enchantment lore and slots if not identified
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
                // Create new lore
                item!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                item!!.lore(createEnchantSlotsLore(enchantSlots, gildedSlots, newEnchants))
                //println(item!!.itemFlags)
                // Hide enchants
            }
            // Adds enchants to slots
            else if (item!!.lore()?.contains(loreSeparator) == true) {
                val newLore = item!!.lore()!!.also { lore ->
                    val enchantSlots = lore.count{ it == emptyEnchantSlot }
                    val totalSlots = enchantSlots + lore.count{ it == emptyGildedSlot }
                    val countIndex = lore.indexOf(loreSeparator) - 1

                    // Loop over all enchants to either add lore or add to removal enchants if over enchant slots
                    val enchantsToRemove = mutableListOf<Enchantment>()
                    var usedSlots = 0
                    for (enchant in event.enchantsToAdd) {
                        if (usedSlots < enchantSlots) {
                            if (enchant.key !is OdysseyEnchantment) {
                                lore[countIndex + usedSlots + 1] = enchant.key.displayName(enchant.value).color(experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                            }
                            usedSlots += 1
                        }
                        else {
                            enchantsToRemove.add(enchant.key)
                        }
                    }
                    for (removed in enchantsToRemove) { event.enchantsToAdd.remove(removed) }
                    lore[countIndex] = Component.text("Enchantment Slots: [$usedSlots/$totalSlots]", experienceEnchantColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                }
                item!!.lore(newLore)
            }
            println(event.enchantsToAdd)
        }

        //TODO: Make if grinded to remove enchants but not slots
        // can re enchant with slots

    }





    // Function that
    @EventHandler
    fun odysseyAnvilHandler(event: PrepareAnvilEvent) {
        with(event.inventory) {
            // Can not anvil combine odyssey enchants
            if (firstItem != null && secondItem != null) {
                if (event.result?.itemMeta?.hasEnchants() == true) {
                    for (enchantKey in event.result!!.enchantments.keys) {
                        if (enchantKey in OdysseyEnchantments.enchantmentSet) {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach { if (it is Player) { it.updateInventory() } }
                        }
                    }
                }

            }
            // Cannot rename gilded enchanted item
            else if (firstItem != null && secondItem == null) {
                if ((renameText != null) && firstItem!!.enchantments.containsKey(OdysseyEnchantments.GILDED_POWER)) {
                    event.result = ItemStack(Material.AIR, 1)
                    for (viewer in event.viewers) { (viewer as Player).updateInventory() }
                    event.viewers.forEach { if (it is Player) { it.updateInventory() } }
                }
            }

        }
    }


    // Main Function that handles Odyssey Smithing make it call on certain gilding table
    @EventHandler
    fun odysseySmithingHandler(event: PrepareSmithingEvent) {
        // With event inventory
        // TODO: Rose Gold Armor
        with(event.inventory) {
            if (inputEquipment != null && inputMineral != null) {

                // Checks if an odyssey weapon is going to be upgraded to cancel it
                if (inputMineral!!.type == Material.NETHERITE_INGOT) {
                    if (inputEquipment!!.itemMeta?.hasCustomModelData() == true) {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach { if (it is Player) { it.updateInventory() } }
                    }
                }
                // Custom Branding for items
                else if (inputEquipment!!.type != Material.ENCHANTED_BOOK && inputMineral!!.type == Material.AMETHYST_SHARD) {
                    val namedMeta = inputEquipment!!.itemMeta.clone().also {
                        // Create an empty component and add viewers names if null, and if not branded
                        val forgerLore = mutableListOf(Component.text(""))
                        for (viewer in event.viewers) { forgerLore.add(Component.text("Created by ${viewer.name}", TextColor.color(141, 109, 209), TextDecoration.ITALIC)) }
                        if (it.lore() == null) { it.lore(forgerLore as List<Component>?) }
                        else if (!it.lore()!!.contains(Component.text(""))) { it.lore(it.lore()!! + forgerLore) }
                    }
                    event.result = inputEquipment!!.clone().apply { itemMeta = namedMeta }
                    event.viewers.forEach { if (it is Player) { it.updateInventory() } }
                }
                // Gilded Book Combine
                // TODO: Make this runic table check later
                else if (inputEquipment!!.type == Material.ENCHANTED_BOOK && inputMineral!!.type == Material.ENCHANTED_BOOK) {
                    if (inputEquipment!!.itemMeta.hasEnchants() && inputMineral!!.itemMeta.hasEnchants()) {
                        val firstBookEnchants = inputEquipment!!.enchantments
                        val secondBookEnchants = inputMineral!!.enchantments
                        for (enchantKey in firstBookEnchants.keys) {
                            if (secondBookEnchants.containsKey(enchantKey) && enchantKey is OdysseyEnchantment) {
                                if (firstBookEnchants[enchantKey]!! < enchantKey.maxLevel && secondBookEnchants[enchantKey]!! < enchantKey.maxLevel) {
                                    val maxLevel = max(firstBookEnchants[enchantKey]!!, secondBookEnchants[enchantKey]!!)
                                    event.result = GildedBook.createGildedBook(enchantKey, maxLevel + 1)
                                    event.viewers.forEach { if (it is Player) { it.updateInventory() } }
                                }
                            }
                        }
                    }
                }
                // TODO: Smithing table for attributes and modifiers


            }
        }







        if (event.inventory.inputEquipment != null && event.inventory.inputMineral != null) {
            val timeElapsed = System.currentTimeMillis() - smithingCooldown

            // GILDED SMITHING
            if (event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK && event.inventory.inputEquipment!!.type != Material.ENCHANTED_BOOK) {
                // BOOK CAN NEVER HAVE GILDED POWER
                // Create items
                val gildedEquipment = event.inventory.inputEquipment!!.clone()
                val gildedEnchantmentBook = event.inventory.inputMineral
                var sameGildedEnchant = false
                // Check
                if (gildedEnchantmentBook != null) {
                    // Checking if it has odyssey enchant
                    var gildedBookEnchants = gildedEnchantmentBook.itemMeta.enchants
                    var hasOdysseyEnchant = false
                    var someBookGildedEnchant: Enchantment? = null
                    for (someEnchant in gildedBookEnchants.keys) {
                        // Check gilded power
                        if (someEnchant in OdysseyEnchantments.enchantmentSet) {
                            hasOdysseyEnchant = true
                            someBookGildedEnchant = someEnchant
                        }
                        // Check if it cannot be applied
                        if (!(someEnchant.canEnchantItem(gildedEquipment))) {
                            return
                        }
                    }
                    // Numbers
                    val romanNumeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")
                    // Checks for enchantment counter
                    if (gildedEnchantmentBook.itemMeta.hasLore() && hasOdysseyEnchant) {
                        var odysseyEnchantCounter = 0
                        var gildedPower = 0
                        // Check if it has odyssey enchant in equipment
                        val equipmentEnchantments = gildedEquipment.itemMeta.enchants
                        for (someEnchant in equipmentEnchantments.keys) {
                            // Check conflicting enchant
                            if (someBookGildedEnchant!!.conflictsWith(someEnchant)) {
                                return
                            }
                            // Count gilded power
                            if (someEnchant in OdysseyEnchantments.enchantmentSet) {
                                if (someEnchant != OdysseyEnchantments.GILDED_POWER) {
                                    // Only 1 gilded enchant on book
                                    if (someEnchant == someBookGildedEnchant) {
                                        sameGildedEnchant = true
                                        println(someEnchant)
                                    }
                                    else {
                                        odysseyEnchantCounter += 1
                                    }
                                }
                            }
                        }
                        // Check gilded power
                        if (gildedEquipment.itemMeta.hasEnchant(OdysseyEnchantments.GILDED_POWER)) {
                            gildedPower = gildedEquipment.itemMeta.getEnchantLevel(OdysseyEnchantments.GILDED_POWER)
                        }
                        // Create new meta for same enchant book + equipment
                        var combinedBookEnchantMeta: ItemMeta? = null
                        if (sameGildedEnchant) {
                            val equipmentEnchantLevel = gildedEquipment.getEnchantmentLevel(someBookGildedEnchant!!)
                            val bookEnchantLevel = gildedEnchantmentBook.getEnchantmentLevel(someBookGildedEnchant)

                            if (equipmentEnchantLevel > bookEnchantLevel) {
                                return
                            }
                            val newLevel: Int = if (bookEnchantLevel == equipmentEnchantLevel) bookEnchantLevel + 1 else max(equipmentEnchantLevel, bookEnchantLevel)
                            // Check Max Level
                            if (newLevel <= someBookGildedEnchant.maxLevel) {
                                val oldBookEnchant = "${ChatColor.GOLD}${someBookGildedEnchant.name} ${romanNumeralList[bookEnchantLevel]}"
                                val newLevelEnchant = "${ChatColor.GOLD}${someBookGildedEnchant.name} ${romanNumeralList[newLevel]}"

                                // Enchantments
                                val copiedEnchants = gildedBookEnchants.toMutableMap()
                                copiedEnchants[someBookGildedEnchant] = newLevel
                                gildedBookEnchants = copiedEnchants
                                // Book Lore
                                val someBookMeta = gildedEnchantmentBook.itemMeta.clone()
                                val newBookLore = someBookMeta.lore!!
                                newBookLore.remove(oldBookEnchant)
                                newBookLore.add(0, newLevelEnchant)
                                someBookMeta.lore = newBookLore
                                combinedBookEnchantMeta = someBookMeta
                                combinedBookEnchantMeta.lore = newBookLore
                            }
                            else {
                                return
                            }
                        }
                        // Check if power allows new enchants
                        if (gildedPower > odysseyEnchantCounter || gildedPower == 0) {
                            // Viewer warning
                            for (viewer in event.viewers) {
                                if (viewer is Player) {
                                    if (timeElapsed > 20) {
                                        gildingCooldown = System.currentTimeMillis()
                                        viewer.sendMessage("This is a permanent gilded enchantment!")
                                    }
                                }
                            }
                            // Adds Lore
                            val gildedEquipmentMeta = gildedEquipment.itemMeta.clone()
                            // Check if item has lore
                            if (gildedEquipmentMeta.hasLore()) {
                                // Check if same
                                if (sameGildedEnchant) {
                                    val newLore = combinedBookEnchantMeta!!.lore
                                    // Remove old lore
                                    val equipmentEnchantLevel = gildedEquipment.getEnchantmentLevel(someBookGildedEnchant!!)
                                    val equipmentEnchantString = "${ChatColor.GOLD}${someBookGildedEnchant.name} ${romanNumeralList[equipmentEnchantLevel]}"
                                    val gildedEquipmentLore = gildedEquipmentMeta.lore!!
                                    gildedEquipmentLore.remove(equipmentEnchantString)
                                    // Add new lore
                                    newLore!!.addAll(gildedEquipmentLore)
                                    gildedEquipmentMeta.lore = newLore
                                    gildedEquipment.itemMeta = gildedEquipmentMeta
                                }
                                else {
                                    val newLore = gildedEnchantmentBook.itemMeta.lore!!
                                    newLore.addAll(gildedEquipmentMeta.lore!!)
                                    gildedEquipmentMeta.lore = newLore
                                    gildedEquipment.itemMeta = gildedEquipmentMeta
                                }
                            }
                            // Copy lore
                            else {
                                val newLore = gildedEnchantmentBook.itemMeta.lore!!
                                gildedEquipmentMeta.lore = newLore
                                gildedEquipment.itemMeta = gildedEquipmentMeta
                            }
                            // Add Enchantments
                            gildedEquipment.addUnsafeEnchantments(gildedBookEnchants)
                            if (gildedEquipment.itemMeta.hasEnchant(GildedPower)) {
                                gildedEquipment.removeEnchantment(GildedPower)
                            }
                            gildedEquipment.addEnchantment(GildedPower, odysseyEnchantCounter + 1)
                            // Create new item
                            event.result = gildedEquipment
                            event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                            println("${event.result}")
                        }
                        // Check Failed
                        else {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                            println("${event.result}")
                        }
                    }
                    // Check Failed
                    else {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        println("${event.result}")
                    }
                }
            }
        }
    }


}