
package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.enchantments.utility.GildedPower
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.resources.ModifiersUUIDs
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.UUID
import kotlin.math.max

object OdysseyGildingListeners : Listener {

    private var smithingCooldown: Long = 0
    private var gildingCooldown: Long = 0

    // Function that disables anvil gilding
    @EventHandler
    fun anvilHandler(event: PrepareAnvilEvent) {
        // BOTH ITEM SLOTS OCCUPIED
        if (event.inventory.firstItem != null && event.inventory.secondItem != null) {
            //BASE CASE!
            if (event.inventory.firstItem!!.type == Material.ENCHANTED_GOLDEN_APPLE && event.inventory.secondItem!!.type == Material.GOLD_INGOT) {
                event.result = ItemStack(Material.GOLDEN_HOE)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
            }
            // Check if items have meta
            else if (event.inventory.firstItem!!.hasItemMeta() || event.inventory.secondItem!!.hasItemMeta()) {
                println("Q")
                val firstItem = event.inventory.firstItem
                val secondItem = event.inventory.secondItem

                // Check if custom model and sharpness
                // DOES NOT SHOW SHARPNESS META!
                /*
                if (firstItem!!.itemMeta.hasCustomModelData()) {
                    println("X")
                    // Check if result has sharpness
                    if (event.result?.itemMeta?.hasEnchant(Enchantment.DAMAGE_ALL) == true) {
                        val eventResult = event.result!!
                        // Check sharpness values
                        val sharpnessValue = eventResult.itemMeta!!.enchants[Enchantment.DAMAGE_ALL]!!
                        if (sharpnessValue <= 5) {
                            var isOdysseyWeapon = false
                            var sharpnessModifier: AttributeModifier? = null
                            // Check modifiers
                            val attackAttribute = eventResult.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)
                            if (attackAttribute != null) {
                                for (modifier in attackAttribute) {
                                    if (modifier.uniqueId == ModifiersUUIDs.ATTACK_DAMAGE_UUID) {
                                        isOdysseyWeapon = true
                                    }
                                    else if (modifier.uniqueId == ModifiersUUIDs.SHARPNESS_DAMAGE_UUID) {
                                        sharpnessModifier = modifier
                                    }
                                }
                            }
                            // Change damage
                            if (isOdysseyWeapon) {
                                eventResult.itemMeta = eventResult.clone().itemMeta.also {
                                    val newDamageValue = ((sharpnessValue + 1) * 0.5)
                                    println(newDamageValue)
                                    val newSharpnessModifier = AttributeModifier(ModifiersUUIDs.SHARPNESS_DAMAGE_UUID, "sharpness.attack_damage", newDamageValue, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
                                    if (sharpnessModifier != null) {
                                        it.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, sharpnessModifier)
                                    }
                                    it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newSharpnessModifier)
                                }
                                event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                                println(eventResult.itemMeta.attributeModifiers)
                            }

                        }
                    }
                }

                 */

                // Checks for items with odyssey enchants
                if (firstItem?.itemMeta?.hasEnchants() == true || secondItem?.itemMeta?.hasEnchants() == true) {

                    // Check if Odyssey Enchants
                    for (enchant in firstItem?.enchantments?.keys!!) {
                        if (enchant in OdysseyEnchantments.enchantmentSet) {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        }
                    }
                    // Check if Odyssey Enchants
                    for (enchant in secondItem?.enchantments?.keys!!) {
                        if (enchant in OdysseyEnchantments.enchantmentSet) {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        }
                    }
                }
            }
        }
        else if (event.inventory.firstItem != null && event.inventory.secondItem == null) {
            val itemToRename = event.inventory.firstItem
            if ((event.inventory.renameText != null) && (itemToRename?.enchantments?.containsKey(OdysseyEnchantments.GILDED_POWER) == true))  {
                /*
                val newMeta: ItemMeta = itemToRename.itemMeta.clone()
                val newName: String? = event.inventory.renameText
                // Change content of text component
                if (newMeta.hasDisplayName()) {
                    val displayComponent: TextComponent = newMeta.displayName() as TextComponent
                    displayComponent.content(newName!!)
                    newMeta.displayName(displayComponent)
                }
                else {
                    val newComponent = Component.text(newName!!)
                    newMeta.displayName(newComponent)
                }
                val newResult = itemToRename.clone()
                itemToRename.itemMeta = newMeta
                event.result = newResult
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                 */
                event.result = ItemStack(Material.AIR, 1)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
            }
        }
        //

    }


    // Main Function that handles Odyssey Smithing make it call on certain gilding table, smithing will be for weapons smiths
    @Suppress("DEPRECATION")
    @EventHandler
    fun odysseySmithingHandler(event: PrepareSmithingEvent) {
        if (event.inventory.inputEquipment != null && event.inventory.inputMineral != null) {
            val timeElapsed = System.currentTimeMillis() - smithingCooldown

            //BASE CASE!
            if (event.inventory.inputEquipment!!.type == Material.ENCHANTED_GOLDEN_APPLE && event.inventory.inputMineral!!.type == Material.GOLD_INGOT) {
                event.result = ItemStack(Material.GOLDEN_HOE)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }
            // TEMP!!!
            // CUSTOM_ATTRIBUTES
            else if (event.inventory.inputMineral!!.type == Material.COMMAND_BLOCK) {
                val someItem = event.inventory.inputEquipment!!.clone()
                if (someItem.hasItemMeta()) {
                    if (someItem.itemMeta.hasEnchant(OdysseyEnchantments.GILDED_POWER)) {
                        val gildedPower = someItem.getEnchantmentLevel(OdysseyEnchantments.GILDED_POWER)
                        // 3
                        // TEMP 1000
                        if (gildedPower < 1000) {
                            val newItemMeta = someItem.itemMeta
                            newItemMeta.removeEnchant(GildedPower)
                            newItemMeta.addEnchant(OdysseyEnchantments.GILDED_POWER, gildedPower + 1, true)
                            val genericAttackDamageUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A22DB5CF")
                            //val someNewAttackStat = AttributeModifier(genericAttackDamageUUID, "generic.attack_damage", 1.0, AttributeModifier.Operation.ADD_NUMBER)
                            //val oldAttack = newItemMeta.
                            if (newItemMeta.hasAttributeModifiers()) {
                                val oldAttackAttribute = newItemMeta.attributeModifiers!![Attribute.GENERIC_ATTACK_DAMAGE]
                                println(oldAttackAttribute)
                                //FIX !!!!!!!!!!!!!!

                            }

                            val someNewAttackStat = AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 100.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
                            newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, someNewAttackStat)
                            //newItemMeta.addAttributeModifier()
                            println(newItemMeta.attributeModifiers)
                            someItem.itemMeta = newItemMeta
                            event.result = someItem
                            println("${event.result}")
                        }
                    }
                }
            }
            // ADDING GILDED POWER
            else if (event.inventory.inputMineral!!.type == Material.BEDROCK) {
                val someItem = event.inventory.inputEquipment!!.clone()
                if (someItem.hasItemMeta()) {
                    if (someItem.itemMeta.hasEnchant(OdysseyEnchantments.GILDED_POWER)) {
                        val gildedPower = someItem.getEnchantmentLevel(OdysseyEnchantments.GILDED_POWER)
                        if (gildedPower < 3) {
                            val newItemMeta = someItem.itemMeta
                            newItemMeta.removeEnchant(GildedPower)
                            newItemMeta.addEnchant(OdysseyEnchantments.GILDED_POWER, gildedPower + 1, true)
                            someItem.itemMeta = newItemMeta
                            event.result = someItem
                            println("${event.result}")
                        }
                    }
                }
            }
            // NAMING
            else if (event.inventory.inputMineral!!.type == Material.AMETHYST_SHARD) {
                val namedEquipment = event.inventory.inputEquipment!!.clone()
                val forgerName = mutableListOf("")
                val newItemMeta = namedEquipment.itemMeta
                for (viewer in event.viewers) {
                    if (viewer is Player) {
                        forgerName.add("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Created by ${viewer.name}")
                        if (timeElapsed > 20) {
                            smithingCooldown = System.currentTimeMillis()
                            viewer.sendMessage("This is a permanent engraving. Once engraved this item can no longer be modified!")
                        }
                    }
                }
                if (namedEquipment.lore != null) {
                    if (namedEquipment.lore!!.contains("")) {
                        return
                    }
                    val newLore = namedEquipment.lore!! + forgerName
                    newItemMeta.lore = newLore
                    namedEquipment.itemMeta = newItemMeta
                }
                else {
                    newItemMeta.lore = forgerName
                    namedEquipment.itemMeta = newItemMeta
                }
                event.result = namedEquipment
                event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
            }
            // GILDED SMITHING
            else if (event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK && event.inventory.inputEquipment!!.type != Material.ENCHANTED_BOOK) {
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
            // GILDED BOOK COMBINE
            else if (event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK && event.inventory.inputEquipment!!.type == Material.ENCHANTED_BOOK) {
                val gildedBook1 = event.inventory.inputMineral
                val gildedBook2 = event.inventory.inputEquipment
                var gildedLevel = 0
                var someEnchant: Enchantment? = null
                var canCombine = false
                if (gildedBook1!!.hasItemMeta() && gildedBook2!!.hasItemMeta()) {
                    for (enchant in gildedBook1.enchantments.keys) {
                        if (enchant in OdysseyEnchantments.enchantmentSet && enchant != OdysseyEnchantments.GILDED_POWER) {
                            if (gildedBook2.itemMeta.hasEnchant(enchant)) {
                                gildedLevel = gildedBook1.getEnchantmentLevel(enchant)
                                // Combine only same level and not max level
                                if (gildedBook2.getEnchantmentLevel(enchant) == gildedLevel && gildedLevel < enchant.maxLevel) {
                                    canCombine = true
                                    someEnchant = enchant
                                    break
                                }
                            }
                        }
                    }
                    // Check if similar enchant
                    if (canCombine) {
                        val newBook = OdysseyItems.GILDED_BOOK.createGildedBook(someEnchant!!, gildedLevel + 1)
                        event.result = newBook
                        event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                    }
                    // Check
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