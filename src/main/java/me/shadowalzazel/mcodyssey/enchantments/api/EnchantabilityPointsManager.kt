package me.shadowalzazel.mcodyssey.enchantments.api

import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getStringTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.hasTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.removeTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.setIntTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

interface EnchantabilityPointsManager : EnchantmentsManager, EnchantmentExtender {

    // !!!!!!!!!!!!!!!!
    // GILDED ENCHANTS ARE SLOTTED/IRREMOVABLE BUT DO NOT TAKE UP EVs??

    fun getEnchantabilityCost(enchant: Pair<Enchantment, Int>, override: Int? = null): Int {
        val level = override ?: enchant.second
        return enchant.first.enchantabilityCost(level)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Extension Helper Functions
    fun ItemStack.getMaxEnchantabilityPoints(): Int {
        return getIntTag(ItemDataTags.ENCHANTABILITY_POINTS) ?: getEnchantabilityDefault(this)
    }

    fun ItemStack.setMaxEnchantabilityPoints(amount: Int) {
        setIntTag(ItemDataTags.ENCHANTABILITY_POINTS, amount)
    }

    fun ItemStack.getUsedEnchantabilityPoints(): Int {
        var usedEnchantabilityPoints = 0
        for (enchant in this.enchantments) {
            usedEnchantabilityPoints += enchant.key.enchantabilityCost(enchant.value)
        }
        return usedEnchantabilityPoints
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Mains
    fun ItemStack.updateEnchantabilityPointsLore(
        newEnchants: MutableMap<Enchantment, Int>? = null,
        removedEnchants: MutableMap<Enchantment, Int>? = null,
        resetLore: Boolean = true,
        toggleToolTip: Boolean = false
    ) {
        val newLore = itemMeta.lore() ?: mutableListOf()
        val updatedEnchantments = newEnchants ?: this.enchantments
        // Temp Lore Index Holders
        if (!newLore.contains(loreSeperator)) {
            newLore.add(0, Component.text("Header Holder"))
            newLore.add(1, loreSeperator)
        }
        // Tool tip
        val hasToolTip = this.hasTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
        if (toggleToolTip) {
            if (hasToolTip) {
                this.removeTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            } else {
                this.addTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            }
        }
        // get gilded [WIP]

        // Remove designated to be removed
        if (removedEnchants != null) {
            for (r in removedEnchants) {
                val enchantment = r.key
                val level = r.value
                val pointCost = enchantment.enchantabilityCost(level)
                newLore.remove(createEnchantLoreComponent(enchantment, level, pointCost))
            }
        }
        // Remove between
        if (resetLore || removedEnchants != null) {
            val startIndex = newLore.indexOf(loreSeperator)
            var endIndex = newLore.indexOf(loreFooter)
            if (endIndex == -1) {
                newLore.add(startIndex + 1, loreFooter)
                endIndex = newLore.indexOf(loreFooter)
            }
            // Loop through range and set to empty
            val emptyContent = startIndex + 1 == endIndex
            if (!emptyContent) {
                for (i in (startIndex + 1..< endIndex)) {
                    if (!newLore.indices.contains(i)) {
                        newLore.add(i, emptyEnchantSlot)
                    }
                    else {
                        newLore[i] = emptyEnchantSlot
                    }
                }
            }
            newLore.removeAll{ it == emptyEnchantSlot }
        }
        val seperatorIndex = newLore.indexOf(loreSeperator)
        // Enchantability Points and Tool Tips
        var counter = 1
        var enchantmentCount = 0
        val maxEnchantabilityPoints = getMaxEnchantabilityPoints()
        var usedEnchantabilityPoints = 0
        updatedEnchantments.forEach {
            val enchantment = it.key
            val level = it.value
            enchantmentCount += 1
            val pointCost = enchantment.enchantabilityCost(level)
            usedEnchantabilityPoints += pointCost
            // Add Lore components
            val enchantIndex = seperatorIndex + counter
            if (!newLore.indices.contains(enchantIndex)) {
                newLore.add(enchantIndex, emptyEnchantSlot)
            }
            newLore[enchantIndex] = createEnchantLoreComponent(enchantment, level, pointCost)
            counter += 1
            if (toggleToolTip && !hasToolTip) {
                val description = enchantment.getDescriptionTooltip(level)
                newLore.addAll(seperatorIndex + counter, description)
                counter += description.size
            }
        }
        // Engraving - Move To Bottom
        if (hasTag(ItemDataTags.IS_ENGRAVED)) {
            val engraver = getStringTag(ItemDataTags.ENGRAVED_BY)!!
            val engraving = Component.text("Created by $engraver", SlotColors.AMETHYST.color, TextDecoration.ITALIC)
            newLore.remove(engraving)
            newLore.add(engraving)
        }
        // Update Glint
        val newMeta = itemMeta.also {
            if (enchantmentCount == 0) {
                it.setEnchantmentGlintOverride(null)
            }
            else {
                it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                it.setEnchantmentGlintOverride(true)
            }
        }
        // Header, Footer
        newLore[seperatorIndex - 1] = createEnchantHeader(usedEnchantabilityPoints, maxEnchantabilityPoints)
        newLore.add(seperatorIndex + counter, loreFooter)
        newMeta.lore(newLore)
        itemMeta = newMeta
    }

    // Create the lore component for item
    fun createEnchantLoreComponent(enchantment: Enchantment, level: Int, pointCost: Int): Component {
        val color = if (enchantment.isCursed) {
            SlotColors.CURSED.color
        } else {
            SlotColors.GRAY.color
        }
        return enchantment
            .displayName(level).append(Component.text(" [$pointCost]").color(SlotColors.ENCHANT.color))
            .color(color)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Components
    val loreSeperator: TextComponent
        get() = Component.text("-----------*-----------", SlotColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val loreFooter: TextComponent
        get() = Component.text("                       ", SlotColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyGildedSlot: TextComponent
        get() = Component.text("+ Empty Gilded Slot", SlotColors.GILDED.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyEnchantSlot: TextComponent
        get() = Component.text("+ Empty Enchant Slot", SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    fun createEnchantHeader(used: Int = 0, total: Int = 0): TextComponent {
        return Component.text("Enchantability Points: [$used/$total]", SlotColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Fail Message
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = SlotColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Point Default
    fun getEnchantabilityDefault(item: ItemStack): Int {
        // do: ALL variations
        // Make map of material type i.e. gold
        // And tool/armor type

        val enchantabilityPoints = when(item.type) {
            Material.DIAMOND_BOOTS -> 45
            Material.DIAMOND_SWORD -> 40
            Material.BOW -> 35
            Material.CROSSBOW -> 30
            else -> 30
        }
        return enchantabilityPoints
    }

}