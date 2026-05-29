package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface ItemToolTipManager : DataTagManager {

    // Main Method called by other functions
    fun ItemStack.updateToolTip() {
        val oldLore = this.lore()
        val newToolTip = mutableSetOf<Component>()

        // Get all enchantments
        val enchantments = this.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        val hasEnchants = enchantments != null && enchantments.isNotEmpty()

        // Get all current tool tips
        val hasEnchantToolTip = this.hasTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
        if (!hasEnchantToolTip) {
            this.addTag(ItemDataTags.HAS_ENCHANT_TOOL_TIP)
        }


        // Check if enchant lore exists



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

    fun createEnchantHeader(used: Int = 0, total: Int = 25): TextComponent {
        return Component.text("Enchantability Points: ", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE).append(
            Component.text("[$used/$total]", CustomColors.ENCHANT.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        )
    }





}