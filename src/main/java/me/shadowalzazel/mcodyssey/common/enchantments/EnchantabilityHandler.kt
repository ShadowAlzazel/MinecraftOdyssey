package me.shadowalzazel.mcodyssey.common.enchantments

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface EnchantabilityHandler : EnchantmentManager, DescriptionManager, DataTagManager {

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
    fun ItemStack.updateItemPoints(
        newEnchants: MutableMap<Enchantment, Int>? = null,
        enchantsToRemove: MutableMap<Enchantment, Int>? = null,
        resetLore: Boolean = true,
        toggleToolTip: Boolean = false
    ) {
        val newLore = itemMeta.lore() ?: mutableListOf()
        // Enchantment assigner
        val updatedEnchantments = newEnchants ?: this.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        if (updatedEnchantments == null) return
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
        // Remove designated to be removed
        if (enchantsToRemove != null) {
            for (enchant in enchantsToRemove) {
                val pointCost = enchant.key.enchantabilityCost(enchant.value)
                newLore.remove(createEnchantLoreComponent(enchant.key, enchant.value, pointCost))
            }
        }
        // Remove all lore between
        if (resetLore || enchantsToRemove != null) {
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
                val description = enchantment.getDescription(level)
                newLore.addAll(seperatorIndex + counter, description)
                counter += description.size
            }
        }
        // Engraving - Move To Bottom
        if (hasTag(ItemDataTags.IS_ENGRAVED)) {
            val engraver = getStringTag(ItemDataTags.ENGRAVED_BY)!!
            val engraving = Component.text("Created by $engraver", CustomColors.AMETHYST.color, TextDecoration.ITALIC)
            newLore.remove(engraving)
            newLore.add(engraving)
        }
        // Header, Footer
        newLore[seperatorIndex - 1] = createEnchantHeader(usedEnchantabilityPoints, maxEnchantabilityPoints)
        newLore.add(seperatorIndex + counter, loreFooter)
        itemMeta = itemMeta.clone().also {
            it.lore(newLore)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    fun ItemStack.updateStoredEnchantmentPoints(
        toggleToolTip: Boolean = true,
        newEnchants: MutableMap<Enchantment, Int>? = null) {
        // Enchantments
        val updatedEnchantments = newEnchants ?: this.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        if (updatedEnchantments == null) return
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
                val description = enchantment.getDescription(level)
                newLore.addAll(startIndex + counter, description)
                counter += description.size
            }
        }
        if (updatedEnchantments.isNotEmpty()) {
            newLore.add(Component.text(""))
        }
        // Final Tool Tip
        if (toggleToolTip) {
            if (hasToolTip) {
                this.removeTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            } else {
                this.addTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            }
        }
        // New
        itemMeta = itemMeta.clone().also {
            it.lore(newLore)
            it.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        }
    }

    fun ItemStack.updateEnchantPoints(
        resetLore: Boolean = true,
        toggleToolTip: Boolean = true,
        newEnchants: MutableMap<Enchantment, Int>? = null) {
        if (this.type == Material.ENCHANTED_BOOK || this.type == Material.BOOK) {
            this.updateStoredEnchantmentPoints(toggleToolTip, newEnchants)
        } else {
            this.updateItemPoints(newEnchants, null, resetLore, toggleToolTip)
        }
    }


    // Create the lore component for item
    fun createEnchantLoreComponent(enchantment: Enchantment, level: Int, pointCost: Int): Component {
        val color = if (enchantment.isCursed) {
            CustomColors.CURSED.color
        }
        else if (enchantment.maxLevel < level) {
            CustomColors.SHINY.color
        }
        else {
            CustomColors.GRAY.color
        }
        return enchantment
            .displayName(level).color(color)
            .append(Component.text(" [$pointCost]").color(CustomColors.ENCHANT.color))
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Components
    val loreSeperator: TextComponent
        get() = Component.text("-----------*-----------", CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val loreFooter: TextComponent
        get() = Component.text("                       ", CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyGildedSlot: TextComponent
        get() = Component.text("+ Empty Gilded Slot", CustomColors.GILDED.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    val emptyEnchantSlot: TextComponent
        get() = Component.text("+ Empty Enchant Slot", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    fun createEnchantHeader(used: Int = 0, total: Int = 0): TextComponent {
        return Component.text("Enchantability Points: ", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE).append(
            Component.text("[$used/$total]", CustomColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Fail Message
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = CustomColors.ENCHANT.color) {
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
        val materialType = item.getStringTag(ItemDataTags.MATERIAL_TYPE)
        if (materialType == "mithril" || materialType == "crystal_alloy") {
            bonusPoints += 5
        }
        val extraPoints = item.getIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS) ?: 0
        bonusPoints += extraPoints

        return baseMaterialPoints + bonusPoints
    }

}