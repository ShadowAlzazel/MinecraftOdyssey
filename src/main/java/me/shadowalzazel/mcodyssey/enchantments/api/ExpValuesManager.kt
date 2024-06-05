package me.shadowalzazel.mcodyssey.enchantments.api

import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getStringTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.hasTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

interface ExpValuesManager {

    // GILDED ENCHANTS ARE SLOTTED/IRREMOVABLE BUT DO NOT TAKE UP EVs

    fun ItemStack.getExperienceValues(): Int {
        return getIntTag(ItemDataTags.EXPERIENCE_VALUES) ?: 0
    }

    fun ItemStack.updateEVsLore(
        newEnchants: MutableMap<Enchantment, Int>? = null,
        resetLore: Boolean = false
    ) {
        val newLore = itemMeta.lore() ?: mutableListOf()
        val updatedEnchantments = newEnchants ?: this.enchantments
        // Temp Lore Index Holders
        if (!newLore.contains(slotSeperator)) {
            newLore.add(Component.text("Header Holder"))
            newLore.add(slotSeperator)
        }
        val seperatorIndex = newLore.indexOf(slotSeperator)
        for (u in 1..updatedEnchantments.size) {
            val i = u + seperatorIndex
            if (!newLore.indices.contains(i)) {
                newLore.add(i, emptyEnchantSlot)
            } else {
                newLore[i] = emptyEnchantSlot
            }
        }
        // Add Enchantment Lore
        var enchantmentCount = 0
        var evCost = 0
        updatedEnchantments.forEach {
            val enchant = it.key
            val color = if (enchant.isCursed) {
                SlotColors.CURSED.color
            } else {
                SlotColors.ENCHANT.color
            }
            enchantmentCount += 1
            evCost += enchant.anvilCost * it.value
            newLore[seperatorIndex + enchantmentCount] = enchant
                .displayName(it.value)
                .color(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        }
        // Engraving - Move To Bottom
        if (hasTag(ItemDataTags.IS_ENGRAVED)) {
            val engraver = getStringTag(ItemDataTags.ENGRAVED_BY)!!
            val engraving = Component.text("Created by $engraver", SlotColors.AMETHYST.color, TextDecoration.ITALIC)
            newLore.remove(engraving)
            newLore.add(engraving)
        }
        // Update Glint
        itemMeta = itemMeta.also {
            if (enchantmentCount == 0) {
                it.setEnchantmentGlintOverride(null)
            }
            else {
                it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                it.setEnchantmentGlintOverride(true)
            }
        }
        // Header and add Lore
        newLore[seperatorIndex - 1] = createEVsHeader(evCost, 40)
        lore(newLore)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Components
    val slotSeperator: TextComponent
        get() = Component.text("----------------------", SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyGildedSlot: TextComponent
        get() = Component.text("+ Empty Gilded Slot", SlotColors.GILDED.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyEnchantSlot: TextComponent
        get() = Component.text("+ Empty Enchant Slot", SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    fun createEVsHeader(used: Int = 0, total: Int = 0): TextComponent {
        return Component.text("Exp Values: [$used/$total]", SlotColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
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