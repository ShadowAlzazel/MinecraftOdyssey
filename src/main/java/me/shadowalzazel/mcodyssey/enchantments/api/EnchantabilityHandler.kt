package me.shadowalzazel.mcodyssey.enchantments.api

import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.DataTagManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

interface EnchantabilityHandler : EnchantmentsManager, EnchantmentExtender, DataTagManager {

    /*-----------------------------------------------------------------------------------------------*/
    fun getEnchantabilityCost(enchant: Pair<Enchantment, Int>, override: Int? = null): Int {
        val level = override ?: enchant.second
        return enchant.first.enchantabilityCost(level)
    }

    // Extension Helper Functions
    fun ItemStack.getMaxEnchantabilityPoints(): Int {
        return itemEnchantabilityPoints(this)
    }

    fun ItemStack.setMaxEnchantabilityPoints(amount: Int) {
        setIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS, amount)
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
        // Enchantment assigner
        val updatedEnchantments: MutableMap<Enchantment, Int>
        if (newEnchants != null) {
            updatedEnchantments = newEnchants
        } else if (itemMeta is EnchantmentStorageMeta) {
            updatedEnchantments = (itemMeta as EnchantmentStorageMeta).storedEnchants
        } else {
            updatedEnchantments = this.enchantments
        }
        // Create Header if it does not exist
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
        // Gilded enchantments do not take up any Enchantability-points

        // Remove designated to be removed
        if (removedEnchants != null) {
            for (r in removedEnchants) {
                val enchantment = r.key
                val level = r.value
                val pointCost = enchantment.enchantabilityCost(level)
                newLore.remove(createEnchantLoreComponent(enchantment, level, pointCost))
            }
        }
        // Remove all lore between
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
            // Remove Spam
            val finalIndex = newLore.indexOf(loreFooter)
            newLore.removeAll { it == loreFooter }
            newLore.add(finalIndex, loreFooter)
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

    fun ItemStack.updateStoredEnchantabilityLore(
        toggleToolTip: Boolean = true) {
        // Checks
        val storedMeta = itemMeta
        if (storedMeta !is EnchantmentStorageMeta) return
        // Enchantments
        val updatedEnchantments: MutableMap<Enchantment, Int> = storedMeta.storedEnchants
        if (updatedEnchantments.isEmpty()) return
        // Tool tip
        val hasToolTip = this.hasTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
        // Points Lore
        val startIndex = 0
        var counter = 0
        var enchantmentCount = 0
        val newLore = mutableListOf<Component>()
        updatedEnchantments.forEach {
            val enchantment = it.key
            val level = it.value
            enchantmentCount += 1
            val pointCost = enchantment.enchantabilityCost(level)
            // Add Lore components
            val enchantIndex = counter + startIndex
            if (!newLore.indices.contains(enchantIndex)) {
                newLore.add(enchantIndex, emptyEnchantSlot)
            }
            newLore[enchantIndex] = createEnchantLoreComponent(enchantment, level, pointCost)
            counter += 1
            if (toggleToolTip && !hasToolTip) {
                val description = enchantment.getDescriptionTooltip(level)
                newLore.addAll(startIndex + counter, description)
                counter += description.size
            }
        }
        newLore.add(Component.text(""))
        storedMeta.lore(newLore)
        // Update Lore Flags
        storedMeta.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        itemMeta = storedMeta
        // Final Tool Tip
        if (toggleToolTip) {
            if (hasToolTip) {
                this.removeTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            } else {
                this.addTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            }
        }
    }

    fun ItemStack.updatePoints(
        resetLore: Boolean = true,
        toggleToolTip: Boolean = true) {
        if (this.type == Material.ENCHANTED_BOOK) {
            this.updateStoredEnchantabilityLore(toggleToolTip)
        }
        else {
            this.updateEnchantabilityPointsLore(
                null, null, resetLore, toggleToolTip
            )
        }
    }


    // Create the lore component for item
    fun createEnchantLoreComponent(enchantment: Enchantment, level: Int, pointCost: Int): Component {
        val color = if (enchantment.isCursed) {
            SlotColors.CURSED.color
        }
        else if (enchantment.maxLevel < level) {
            SlotColors.SHINY.color
        }
        else {
            SlotColors.GRAY.color
        }
        return enchantment
            .displayName(level).color(color)
            .append(Component.text(" [$pointCost]").color(SlotColors.ENCHANT.color))
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
        return Component.text("Enchantability Points: ", SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE).append(
            Component.text("[$used/$total]", SlotColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        )
    }
    //[$used/$total]

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
    fun itemEnchantabilityPoints(item: ItemStack): Int {
        // do: ALL variations
        // Make map of material type i.e. gold
        // And tool/armor type
        val baseMaterialPoints = when(item.type) {
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET,
            Material.GOLDEN_HELMET, Material.LEATHER_HELMET, Material.TURTLE_HELMET -> {
                35
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS,
            Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                40
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                35
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.ELYTRA -> {
                35
            }
            else -> 35
        }
        var bonusPoints = 0
        if (item.getStringTag(ItemDataTags.MATERIAL_TYPE) == "mithril") {
            bonusPoints += 5
        }
        val extraPoints = item.getIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS) ?: 0
        bonusPoints += extraPoints

        return baseMaterialPoints + bonusPoints
    }

}