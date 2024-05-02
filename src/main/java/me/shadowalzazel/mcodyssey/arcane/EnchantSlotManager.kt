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
        newEnchants: MutableMap<EnchantContainer, Int>? = null,
        resetLore: Boolean = false
    ) {
        val newLore = itemMeta.lore() ?: mutableListOf()
        val headerHolder = Component.text("Header Holder")
        if (!newLore.contains(slotSeperator)) {
            newLore.add(headerHolder)
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
        // Get Enchantment Containers
        val minecraftEnchantContainers = newEnchants?.filter { it.key.isBukkit } ?: createBukkitEnchantContainerMap(enchantments)
        val odysseyEnchantments = getOdysseyEnchantments()
        val odysseyEnchantContainers = newEnchants?.filter { it.key.isOdyssey } ?: createOdysseyEnchantContainerMap(odysseyEnchantments)
        val gildedEnchantKey = getGildedEnchantKey()
        // Add Enchants Over empty slots
        var enchantmentCount = 0
        minecraftEnchantContainers.forEach {
            val enchant = it.key.bukkitEnchant!!
            val color = if (enchant.isCursed) {
                SlotColors.CURSED.color
            } else {
                SlotColors.ENCHANT.color
            }
            enchantmentCount += 1
            newLore[sepIndex + enchantmentCount] = enchant
                .displayName(it.value)
                .color(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        // Separate to get subtypes
        odysseyEnchantContainers.forEach {
            val enchant = it.key.odysseyEnchant!!
            val color = if (enchant.isCurse) {
                SlotColors.CURSED.color
            } else {
                SlotColors.ENCHANT.color
            }
            enchantmentCount += 1
            newLore[sepIndex + enchantmentCount] = enchant
                .displayName(it.value)
                .color(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        // Get gilded
        var gildedCount = 0
        if (gildedEnchantKey != null) {
            // Set lore on if odyssey or bukkit
            if (gildedEnchantKey.isBukkit) {
                val enchant = gildedEnchantKey.bukkitEnchant!!
                val level = enchantments[enchant]!!
                newLore[sepIndex + enchantSlots] = enchant
                    .displayName(level)
                    .color(SlotColors.GILDED.color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                enchantments[gildedEnchantKey.bukkitEnchant]
            }
            else if (gildedEnchantKey.isOdyssey) {
                val enchant = gildedEnchantKey.odysseyEnchant!!
                val level = odysseyEnchantments[enchant]!!
                newLore[sepIndex + enchantSlots] = enchant
                    .displayName(level)
                    .color(SlotColors.GILDED.color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            gildedCount += 1
        }
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
        addItemFlags(ItemFlag.HIDE_ENCHANTS)
        lore(newLore)
    }

    // Create Base New Slots
    fun ItemStack.createNewEnchantSlots() {
        var enchantSlots = 2
        enchantSlots += enchantments.size
        val odysseyEnchantments = this.getOdysseyEnchantments()
        enchantSlots += odysseyEnchantments.size
        setPairSlots(Pair(enchantSlots, 0))
        val bukkitContainers = createBukkitEnchantContainerMap(enchantments)
        val odysseyContainers = createOdysseyEnchantContainerMap(odysseyEnchantments)
        val enchantContainers = bukkitContainers + odysseyContainers
        updateSlotLore(enchantContainers.toMutableMap())
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
    fun LivingEntity.sendBarMessage(reason: String, color: TextColor = SlotColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}