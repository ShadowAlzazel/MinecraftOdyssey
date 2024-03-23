package me.shadowalzazel.mcodyssey.arcane

import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.enchantments.EnchantRegistryManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.apache.commons.lang3.tuple.MutablePair
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

internal interface EnchantSlotManager : EnchantRegistryManager {

    fun ItemStack.isSlotted(): Boolean {
        return hasTag(ItemTags.IS_SLOTTED)
    }

    fun ItemStack.getEnchantSlots(): Int {
        return getIntTag(ItemTags.ENCHANT_SLOTS) ?: 0
    }

    fun ItemStack.getGildedSlots(): Int {
        return getIntTag(ItemTags.GILDED_SLOTS) ?: 0
    }

    fun ItemStack.getSlots(): Pair<Int, Int> {
        return Pair(getEnchantSlots(), getGildedSlots())
    }

    // Not Restricted
    fun ItemStack.setSlots(slots: Pair<Int, Int>) {
        addTag(ItemTags.IS_SLOTTED)
        setIntTag(ItemTags.ENCHANT_SLOTS, slots.first)
        setIntTag(ItemTags.GILDED_SLOTS, slots.second)
    }

    fun ItemStack.addEnchantSlot() {
        val count = getEnchantSlots()
        setIntTag(ItemTags.ENCHANT_SLOTS, count + 1)
    }

    fun ItemStack.addGildedSlot() {
        val count = getGildedSlots()
        if (count >= 3) {
            return
        }
        setIntTag(ItemTags.GILDED_SLOTS, count + 1)
    }

    fun ItemStack.removeEnchantSlot() {
        val count = getEnchantSlots()
        setIntTag(ItemTags.ENCHANT_SLOTS, maxOf(1, count - 1))
    }

    fun ItemStack.removeGildedSlot() {
        val count = getGildedSlots()
        setIntTag(ItemTags.GILDED_SLOTS, maxOf(0, count - 1))
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
        val slots = Pair(getEnchantSlots(), getGildedSlots())
        /*
        println("SEP INDEX: $sepIndex")
        println("SIZE: ${newLore.size}")
        println("SLOTS: $slots")
        println("F: ${sepIndex + slots.second + slots.first}")
         */
        // Loop add Empty Slots first
        for (e in 1..slots.first) {
            val i = e + sepIndex
            if (!newLore.indices.contains(i)) {
                newLore.add(i, emptyEnchantSlot)
            } else {
                newLore[i] = emptyEnchantSlot
            }
        }
        for (g in 1..slots.second) {
            val i = sepIndex + slots.first + g
            if (!newLore.indices.contains(i)) {
                newLore.add(i, emptyGildedSlot)
            } else {
                newLore[i] = emptyGildedSlot
            }
        }
        // Get Enchantment Maps
        val enchants = newEnchants?.filter { !it.key.isOdysseyEnchant() } ?: enchantments.filter { !it.key.isOdysseyEnchant() }
        val gildedEnchants = newEnchants?.filter { it.key.isOdysseyEnchant() } ?: enchantments.filter { it.key.isOdysseyEnchant() }
        // Add Enchants Over empty slots
        var enchantCount = 0
        enchants.forEach {
            val color = if (it.key.isCursed) {
                SlotColors.CURSED.color
            } else {
                SlotColors.ENCHANT.color
            }
            enchantCount += 1
            newLore[sepIndex + enchantCount] = it.key
                .displayName(it.value)
                .color(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        var gildedCount = 0
        gildedEnchants.forEach {
            gildedCount += 1
            newLore[sepIndex + slots.first + gildedCount] = (it.key.convertToOdysseyEnchant())
                .displayLore(it.value)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        // Move Engraving To Bottom
        if (hasTag(ItemTags.IS_ENGRAVED)) {
            val engraver = getStringTag(ItemTags.ENGRAVED_BY)!!
            val engraving = Component.text("Created by $engraver", SlotColors.AMETHYST.color, TextDecoration.ITALIC)
            newLore.remove(engraving)
            newLore.add(engraving)
        }
        // Header
        newLore[sepIndex - 1] = enchantHeader(enchantCount + gildedCount, slots.first + slots.second)
        // New Lore
        lore(newLore)
    }

    //
    fun ItemStack.createNewEnchantSlots() {
        val slots = MutablePair(2, 1)
        for (enchant in enchantments) {
            if (enchant.key.isOdysseyEnchant()) {
                slots.left += 1
            } else {
                slots.right += 1
            }
        }
        setSlots(slots.toPair())
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