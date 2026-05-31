package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface ItemToolTipManager : DataTagManager, EnchantabilityHandler {

    // Main Method called by other functions
    // ONLY updates the ToolTip Lore, does not do logic!
    fun ItemStack.updateToolTip(toggleDescriptions: Boolean = true) {

        // Start the tool tips
        val oldLore = this.lore()
        val newToolTip = mutableListOf<Component>()

        // Item Vars
        var usedEnchantabilityPoints = 0
        val enchantabilityMax = 35
        // Logic and ToolTip Vars
        var hasEnchantToolTip: Boolean = false
        var hasDescriptionToolTip: Boolean = false
        var showDescriptions: Boolean = false
        var isCustomWeapon: Boolean = false
        var toolTipIndex = 0
        var startingIndex = 0


        // Local Helper Functions
        fun addEnchantToolTip(
            enchantment: MutableMap.MutableEntry<Enchantment, Int>
        ) {
            val level = enchantment.value
            val enchant = enchantment.key
            val pointCost = enchant.enchantabilityCost(level)
            //usedEnchantabilityPoints += pointCost
            // Create the component
            val enchantComponent = createEnchantComponent(
                enchant,
                level,
                pointCost
            )
            newToolTip.add(enchantComponent)
            toolTipIndex++
            // Add all lines of description to the tool tip if Show is On
            if (showDescriptions) {
                val enchantDescription = enchant.getDescription(level)
                for (x in enchantDescription) {
                    newToolTip.add(toolTipIndex, x)
                    toolTipIndex++
                }
            }
        }

        val weaponType = getWeaponType()
        isCustomWeapon = weaponType != null

        // Get all enchantments
        val enchantments = this.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        val storedEnchantments = this.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        val hasEnchants = enchantments != null && enchantments.isNotEmpty()
        val hasStoredEnchants = storedEnchantments != null && storedEnchantments.isNotEmpty()
        usedEnchantabilityPoints = this.getUsedEnchantabilityPoints()

        // Get all tool tip tags, add tags if not present.
        hasEnchantToolTip = this.hasTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
        if (!hasEnchantToolTip) {
            this.addTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
            hasEnchantToolTip = true
        }
        hasDescriptionToolTip = this.hasTag(ItemDataTags.SHOW_TOOL_TIP_DESCRIPTIONS)
        if (!hasDescriptionToolTip) {
            this.setBoolTag(ItemDataTags.SHOW_TOOL_TIP_DESCRIPTIONS, true)
            hasDescriptionToolTip = true
        }

        // Check Toggles
        showDescriptions = this.getBoolTag(ItemDataTags.SHOW_TOOL_TIP_DESCRIPTIONS) ?: false
        if (toggleDescriptions) {
            showDescriptions = !showDescriptions
            this.setBoolTag(ItemDataTags.SHOW_TOOL_TIP_DESCRIPTIONS, showDescriptions)
        }

        // If has lore, check for old seperator
        if (oldLore != null) {
            // Check if custom tool tip exists
            if (oldLore.contains(toolTipSeperator)) {
                // Starting index is one above the seperator
                startingIndex = oldLore.indexOf(toolTipSeperator) - 1
            }
        }
        startingIndex = maxOf(startingIndex, 0) // Check to make sure lowest index is 0

        // CREATE THE TOOL TIP, DO ALL LOGIC BEFORE THIS

        // Create the New Tool Tip
        newToolTip.add(0, createEnchantHeader(usedEnchantabilityPoints, enchantabilityMax))
        newToolTip.add(1, toolTipSeperator)

        // Loop Through all enchantments and add to tool tip
        toolTipIndex = 2
        if (enchantments != null && hasEnchants) {
            for (enchantment in enchantments) {
                addEnchantToolTip(enchantment)
            }
            // Add the item flag
            this.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        } else if (storedEnchantments != null && hasStoredEnchants) {
            for (enchantment in storedEnchantments) {
                addEnchantToolTip(enchantment)
            }
            // Add the item flag
            this.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        }

        // Add an Empty Slot if there is points available
        if (usedEnchantabilityPoints < enchantabilityMax) {
            //newToolTip.add(toolTipIndex++, emptyEnchantSlot)
        }

        // Weapon data and attributes
        if (isCustomWeapon && showDescriptions && weaponType != null) {
            newToolTip.add(toolTipIndex++, Component.text(""))
            newToolTip.add(toolTipIndex++, createWeaponHeader(weaponType))
            // Base Damage Type
            if (weaponType in WeaponMaps.PIERCING_WEAPONS) {
                newToolTip.add(toolTipIndex++,
                    darkGrayTextComponent("- Piercing Damage (Can do AoE damage along a ray)"))
            } else if (weaponType in WeaponMaps.SLASHING_WEAPONS) {
                newToolTip.add(toolTipIndex++,
                    darkGrayTextComponent("- Slashing Damage (Can do AoE slashes)"))
            } else if (weaponType in WeaponMaps.BLUNT_WEAPONS) {
                newToolTip.add(toolTipIndex++,
                    darkGrayTextComponent("- Blunt Damage (Can do AoE from impact)"))
            }

            // Active Skills
            if (weaponType in WeaponMaps.CAN_PARRY) {
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Parry (Hold to mitigate 50% incoming melee damage)"))
            }
            if (weaponType in WeaponMaps.DUAL_WIELDABLE) {
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Dual Wieldable (Can attack from Off-Hand)"))
            }
            if (weaponType in WeaponMaps.THROWABLE) {
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Throwable (Can throw weapon as projectile)"))
            }
            if (weaponType in WeaponMaps.CAN_KINETIC_CHARGE) {
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Chargeable (Hold weapon to do a charge attack)"))
            }
            // Passives

            // Attributes
        }

        // add the footer
        //newToolTip.add(toolTipIndex, toolTipFooter)

        // Method to inject and replace the old Odyssey Tool Tip, to keep other flavor text
        // Delete old ToolTip within the header and footer and add the new one
        if (oldLore != null && !oldLore.contains(toolTipSeperator)) { //TEMP CHECK!!!!!!
            var end = oldLore.indexOf(toolTipFooter)
            if (end == -1) end = oldLore.size + 1
            val oldLoreList = oldLore.subList(startingIndex, end) // Removes list elements between two indexes
            // Add new lore if oldLoreContains anything
            if (oldLoreList.isNotEmpty()) {
                for (x in oldLoreList) {
                    newToolTip.add(x)
                }
            }
        }

        // Set the New ToolTip
        this.lore(newToolTip)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Helper Functions
    private fun ItemStack.getWeaponType(): String? {
       return this.getStringTag(ItemDataTags.TOOL_TYPE)
    }


    /*-----------------------------------------------------------------------------------------------*/
    // Constant Components

    private val toolTipSeperator: TextComponent
        get() = Component.text("-----------*-----------", CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    private val toolTipFooter: TextComponent
        get() = Component.text("                       ", CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    private val emptyGildedSlot: TextComponent
        get() = Component.text("+ Empty Gilded Slot", CustomColors.GILDED.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    private val emptyEnchantSlot: TextComponent
        get() = Component.text("+ Empty Enchant Slot", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    /*-----------------------------------------------------------------------------------------------*/
    // Variable Components

    private fun darkGrayTextComponent(text: String): TextComponent {
        return Component.text(text, CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    private fun grayTextComponent(text: String): TextComponent {
        return Component.text(text, CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    private fun createEnchantHeader(used: Int, total: Int = 35): TextComponent {
        return Component.text("Enchantability Points: ", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .append(Component.text("[$used/$total]", CustomColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
    }

    private fun createWeaponHeader(text: String): TextComponent {
        return Component.text("Weapon Attributes: ", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .append(Component.text("[${text.replaceFirstChar { it.titlecase() }}]", CustomColors.BLUE.color))
    }

    private fun createEnchantComponent(enchantment: Enchantment, level: Int, pointCost: Int): Component {
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



}