package me.shadowalzazel.mcodyssey.arcane

import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getStringTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.hasTag
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantmentDataManager
import me.shadowalzazel.mcodyssey.enchantments.util.EnchantContainer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

internal interface EnchantSlotManager : EnchantmentDataManager {

    fun ItemStack.isSlotted(): Boolean {
        return hasTag(ItemDataTags.IS_SLOTTED)
    }

    fun ItemStack.getEnchantSlots(): Int {
        return getIntTag(ItemDataTags.ENCHANT_SLOTS) ?: 0
    }

    fun ItemStack.getGildedSlots(): Int {
        return getIntTag(ItemDataTags.GILDED_SLOTS) ?: 0
    }

    fun ItemStack.getGildedEnchantKey(): EnchantContainer? {
        val name = getStringTag(ItemDataTags.GILDED_ENCHANT) ?: return null
        if (name == "null") return null
        return createEnchantContainer(name)
    }

    // Returns A Pair of (ENCHANT, GILDED) slots
    fun ItemStack.getPairSlots(): Pair<Int, Int> {
        return Pair(getEnchantSlots(), getGildedSlots())
    }

    // Not Restricted
    fun ItemStack.setPairSlots(slots: Pair<Int, Int>) {
        addTag(ItemDataTags.IS_SLOTTED)
        setIntTag(ItemDataTags.ENCHANT_SLOTS, slots.first)
        setIntTag(ItemDataTags.GILDED_SLOTS, slots.second)
    }

    fun ItemStack.addEnchantSlot() {
        val count = getEnchantSlots()
        setIntTag(ItemDataTags.ENCHANT_SLOTS, count + 1)
    }

    fun ItemStack.addGildedSlot(override: Boolean = false) {
        val count = getGildedSlots()
        if (count >= 1 && !override) {
            return
        }
        setIntTag(ItemDataTags.GILDED_SLOTS, count + 1)
    }

    fun ItemStack.removeEnchantSlot() {
        val count = getEnchantSlots()
        setIntTag(ItemDataTags.ENCHANT_SLOTS, maxOf(1, count - 1))
    }

    fun ItemStack.removeGildedSlot() {
        val count = getGildedSlots()
        setIntTag(ItemDataTags.GILDED_SLOTS, maxOf(0, count - 1))
    }

    // Usage for ONLY display not logic
    fun ItemStack.updateSlotLore(
        newEnchants: MutableMap<Enchantment, Int>? = null,
        resetLore: Boolean = false
    ) {
        val newLore = itemMeta.lore() ?: mutableListOf()
        if (!newLore.contains(slotSeperator)) {
            newLore.add(Component.text("Header Holder"))
            newLore.add(slotSeperator)
        }
        val sepIndex = newLore.indexOf(slotSeperator)
        val enchantSlots = getEnchantSlots()
        val gildedSlots = getGildedSlots()
        /*
        println("SEP INDEX: $sepIndex")
        println("SIZE: ${newLore.size}")
        println("SLOTS: $slots")
        println("F: ${sepIndex + slots.second + slots.first}")
         */
        // Loop add Empty Slots first
        for (e in 1..enchantSlots) {
            val i = e + sepIndex
            if (!newLore.indices.contains(i)) {
                newLore.add(i, emptyEnchantSlot)
            } else {
                newLore[i] = emptyEnchantSlot
            }
        }
        for (g in 1..gildedSlots) {
            val i = sepIndex + enchantSlots + g
            if (!newLore.indices.contains(i)) {
                newLore.add(i, emptyGildedSlot)
            } else {
                newLore[i] = emptyGildedSlot
            }
        }
        // Get Enchantment Maps
        val nonOdysseyEnchants = newEnchants?.filter { !it.key.isOdysseyEnchant() } ?: enchantments.filter { !it.key.isOdysseyEnchant() }
        val odysseyEnchants = newEnchants?.filter { it.key.isOdysseyEnchant() } ?: enchantments.filter { it.key.isOdysseyEnchant() }
        val gildedEnchantKey = getGildedEnchantKey()
        // Add Enchants Over empty slots
        var enchantmentCount = 0
        nonOdysseyEnchants.forEach {
            val color = if (it.key.isCursed) {
                SlotColors.CURSED.color
            } else {
                SlotColors.ENCHANT.color
            }
            enchantmentCount += 1
            newLore[sepIndex + enchantmentCount] = it.key
                .displayName(it.value)
                .color(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        // Separate to get subtypes
        odysseyEnchants.forEach {
            val color = if (it.key.isCursed) {
                SlotColors.CURSED.color
            } else {
                SlotColors.ENCHANT.color
            }
            enchantmentCount += 1
            newLore[sepIndex + enchantmentCount] = it.key
                .displayName(it.value)
                .color(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        // Get gilded
        var gildedCount = 0
        if (gildedEnchantKey != null) {
            val level = newEnchants?.get(gildedEnchantKey) ?: 1
            gildedCount += 1
            newLore[sepIndex + enchantmentCount] = gildedEnchantKey
                .displayName(level)
                .color(SlotColors.GILDED.color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        /*
        odysseyEnchants.forEach {
            gildedCount += 1
            newLore[sepIndex + enchantSlots.first + gildedCount] = (it.key.convertToOdysseyEnchant())
                .getTextForLore(it.value)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
         */
        // Move Engraving To Bottom
        if (hasTag(ItemDataTags.IS_ENGRAVED)) {
            val engraver = getStringTag(ItemDataTags.ENGRAVED_BY)!!
            val engraving = Component.text("Created by $engraver", SlotColors.AMETHYST.color, TextDecoration.ITALIC)
            newLore.remove(engraving)
            newLore.add(engraving)
        }
        // Header
        newLore[sepIndex - 1] = enchantHeader(enchantmentCount + gildedCount, enchantSlots + gildedSlots)
        // New Lore
        lore(newLore)
    }

    // Create Base New Slots
    fun ItemStack.createNewEnchantSlots() {
        var enchantSlots = 2
        for (enchant in enchantments) {
            enchantSlots += 1
        }
        setPairSlots(Pair(enchantSlots, 0))
        addItemFlags(ItemFlag.HIDE_ENCHANTS)
        updateSlotLore(enchantments)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Components
    val slotSeperator: TextComponent
        get() = Component.text("----------------------", SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyGildedSlot: TextComponent
        get() = Component.text("+ Empty Gilded Slot", SlotColors.GILDED.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyEnchantSlot: TextComponent
        get() = Component.text("+ Empty Enchant Slot", SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    fun enchantHeader(used: Int = 0, total: Int = 0): TextComponent {
        return Component.text("Enchantments: [$used/$total]", SlotColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Fail Message
    fun LivingEntity.sendFailMessage(reason: String, color: TextColor = SlotColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}