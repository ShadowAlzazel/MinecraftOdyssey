package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.math.round

@Suppress("UnstableApiUsage")
interface ItemToolTipManager : DataTagManager, EnchantabilityHandler {

    // Main Method called by other functions
    // ONLY updates the ToolTip Lore, does not do logic!
    fun ItemStack.updateToolTip(toggleDescriptions: Boolean = true, forceUpdate: Boolean = false): Boolean {
        // Set boolean fields
        val hasAttributes = this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS) != null
        val weaponType = getWeaponType()
        val isCustomWeapon = weaponType != null
        val enchantments = this.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        val storedEnchantments = this.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        val hasEnchants = enchantments != null && enchantments.isNotEmpty()
        val hasStoredEnchants = storedEnchantments != null && storedEnchantments.isNotEmpty()

        // Needs to have at least one of these
        if (!forceUpdate) {
            if (!(isCustomWeapon || hasEnchants || hasStoredEnchants)) return false
        }

        // Start the tool tips
        val oldLore = this.lore()
        val newToolTip = mutableListOf<Component>()

        // Item Vars
        var usedEnchantabilityPoints = 0
        val enchantabilityMax = getMaxEnchantabilityPoints()
        // Logic and ToolTip Vars
        var showDescriptions: Boolean = false
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


        // Get all enchantments
        usedEnchantabilityPoints = this.getUsedEnchantabilityPoints()

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

        // Custom weapon data and skills
        if (isCustomWeapon && showDescriptions && weaponType != null) {
            newToolTip.add(toolTipIndex++, smallToolTipSeperator) // Add Another Seperator
            newToolTip.add(toolTipIndex++, createWeaponHeader(weaponType))
            // Base Damage Type
            if (weaponType in WeaponMaps.PIERCING_WEAPONS) {
                newToolTip.add(toolTipIndex++,
                    darkGrayTextComponent("- Piercing Damage (Deals damage along a ray)"))
            } else if (weaponType in WeaponMaps.SLASHING_WEAPONS) {
                newToolTip.add(toolTipIndex++,
                    darkGrayTextComponent("- Slashing Damage (Can do AoE damage slashes)"))
            } else if (weaponType in WeaponMaps.BLUNT_WEAPONS) {
                newToolTip.add(toolTipIndex++,
                    darkGrayTextComponent("- Blunt Damage (Can do AoE damage smashes)"))
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
            if (weaponType in WeaponMaps.BONUS_CRIT_DAMAGE) {
                val critModifier = WeaponMaps.BONUS_CRIT_DAMAGE[weaponType]!! + 0.5F
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Critical Damage Modifier is ${"%.2f".format(1 + critModifier)}%"))
            }
            if (weaponType in WeaponMaps.DISABLE_SHIELDS) {
                val disableTime = WeaponMaps.DISABLE_SHIELDS[weaponType]!! / 20
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Can disable Blocking for ${disableTime} seconds"))
            }
            val canMineBlocks = this.getData(DataComponentTypes.TOOL) != null
            if (canMineBlocks) {
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent("- Also a tool that can mine blocks"))
            }

        }

        // Hide Attribute Modifiers on simple Screen
        if (isCustomWeapon) {
            // Damage vars
            var flatAttackSpeed = 0.0
            var percentAttackSpeed = 0.0
            var flatAttackDamage = 0.0
            var percentAttackDamage = 0.0
            // Damage Calculations
            var attacksPerSecond = 0.0
            var damagePerSecond = 0.0
            var attackRange = 3.0

            val modifiers = this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
            if (modifiers != null) {
                for (x in modifiers.modifiers()) {
                    // Get Attack Speed
                    if (x.attribute() == Attribute.ATTACK_SPEED) {
                        // Skip Reset Speed but include base speed
                        if (x.modifier().key.key == AttributeTags.ITEM_RESET_ATTACK_SPEED) {
                            continue
                        } else {
                            //flatAttackSpeed += 4.0
                        }
                        // Get the modifiers
                        if (x.modifier().operation == AttributeModifier.Operation.ADD_NUMBER) {
                            flatAttackSpeed += x.modifier().amount
                        }
                        else if (x.modifier().operation == AttributeModifier.Operation.ADD_SCALAR) {
                            percentAttackSpeed += x.modifier().amount
                        }
                    }
                    // Get Attack Damage
                    if (x.attribute() == Attribute.ATTACK_DAMAGE) {
                        if (x.modifier().operation == AttributeModifier.Operation.ADD_NUMBER) {
                            flatAttackDamage += x.modifier().amount
                        }
                        else if (x.modifier().operation == AttributeModifier.Operation.ADD_SCALAR) {
                            percentAttackDamage += x.modifier().amount
                        }
                    }
                    // Attack Range Attribute
                    if (x.attribute() == Attribute.ENTITY_INTERACTION_RANGE) {
                        if (x.modifier().operation == AttributeModifier.Operation.ADD_NUMBER) {
                            attackRange += x.modifier().amount
                        }
                    }
                }
            }
            // Check enchantments that modify base stats.
            if (hasEnchants) {
                for (enchant in enchantments) {
                    when(enchant.key.getNameId()) {
                        "sharpness" -> {
                            flatAttackDamage += (0.5 * enchant.value + 0.5)
                        }
                        "agile" -> {
                            percentAttackSpeed += (0.05 * enchant.value)
                        }
                    }
                }

            }


            // Calculate Per Seconds
            val speedFinal = flatAttackSpeed * (1 + percentAttackSpeed)
            attacksPerSecond = (20 / (round(20 / speedFinal))) // APS = 20 / (round (20 / speed))
            val attackFinal = flatAttackDamage * (1 + percentAttackDamage)
            damagePerSecond = ((0.2 + 0.8) * flatAttackDamage) * attacksPerSecond

            newToolTip.add(toolTipIndex++,darkGrayTextComponent("")) // Add Another Seperator
            newToolTip.add(
                toolTipIndex++,
                grayTextComponent("When in Main Hand: "))
            newToolTip.add(
                toolTipIndex++,
                blueTextComponent("${"%.1f".format(damagePerSecond)} Damage Per Second"))
            newToolTip.add(
                toolTipIndex++,
                darkGrayTextComponent(" | ${"%.1f".format(attackFinal)} Damage per Hit"))
            newToolTip.add(
                toolTipIndex++,
                darkGrayTextComponent(" | ${"%.2f".format(attacksPerSecond)} Attacks per Second"))
            // Toggleable
            if (showDescriptions) {
                newToolTip.add(
                    toolTipIndex++,
                    darkGrayTextComponent(" | ${"%.2f".format(attackRange)} Attack Range"))
            }

            // Flags
            this.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }

        // add the footer
        //newToolTip.add(toolTipIndex, toolTipFooter)

        // Method to inject and replace the old Odyssey Tool Tip, to keep other flavor text
        // Delete old ToolTip within the header and footer and add the new one
        if (oldLore != null) { //TEMP CHECK!!!!!!
            var oldSeperatorIndex = oldLore.indexOf(toolTipSeperator)
            // If No Seperator
            if (oldSeperatorIndex == -1) {

            }
            // If Seperator
            else {

            }
        }

        // Set the New ToolTip
        this.lore(newToolTip)

        return true
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

    private val smallToolTipSeperator: TextComponent
        get() = Component.text("---------------*", CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

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

    private fun blueTextComponent(text: String): TextComponent {
        return Component.text(text, CustomColors.BLUE.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    private fun createEnchantHeader(used: Int, total: Int = 35): TextComponent {
        return Component.text("Enchantability Points: ", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .append(Component.text("[$used/$total]", CustomColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
    }

    private fun createWeaponHeader(text: String): TextComponent {
        return Component.text("Weapon Attributes: ", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .append(Component.text("[${text.toTitleCase()}]", CustomColors.BLUE.color))
    }

    private fun String.toTitleCase(): String {
        return split('_', '-', ' ')
            .filter { it.isNotEmpty() }
            .joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
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