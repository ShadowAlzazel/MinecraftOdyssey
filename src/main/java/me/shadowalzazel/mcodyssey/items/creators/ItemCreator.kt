package me.shadowalzazel.mcodyssey.items.creators

import me.shadowalzazel.mcodyssey.arcane.SlotColors
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantmentDataManager
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType

interface ItemCreator : EnchantmentDataManager {


    fun OdysseyItem.createNewStack(amount: Int = 1): ItemStack {
        val itemStack = ItemStack(overrideMaterial, amount).also {
            // Set Variables
            val meta  = it.itemMeta
            meta.setCustomModelData(customModel)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // Optional Variables
            if (lore != null) {
                meta.lore(lore)
            }
            // Assemble meta
            it.itemMeta = meta
        }
        return itemStack
    }

    fun OdysseyItem.createArcaneBook(enchantment: OdysseyEnchantment, level: Int = 1) : ItemStack {
        if (itemName != "arcane_book") return ItemStack(Material.AIR)
        val newBook = this.createItemStack(1)
        newBook.itemMeta = (newBook.itemMeta as EnchantmentStorageMeta).also {
            // Set lore and description
            val loreName = enchantment.displayName(level)
            val newToolTip = enchantment.getDescriptionToolTip(level)
            val textLore = mutableListOf(loreName) + Component.text("") + newToolTip
            val bookName = it.displayName()!!.color(SlotColors.ARCANE.color)
            val fullName = bookName.append(loreName.color(SlotColors.ARCANE.color))
            it.displayName(fullName)
            it.lore(textLore)
        }
        return newBook
    }

    fun OdysseyItem.createTome() : ItemStack {
        val tome = this.createItemStack()
        val meta = tome.itemMeta
        meta.displayName(
            Component.text(customName, TextColor.color(255, 255, 85))
        )
        tome.itemMeta = meta
        return tome
    }

    fun OdysseyItem.createArmor(bonus: Double = 0.0): ItemStack {
        val armor = this.createItemStack(1)
        val meta = armor.itemMeta.also {
            val armorModifier = AttributeModifier(AttributeIDs.ARMOR_HELMET_UUID, "odyssey.armor_helmet", bonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
            it.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier)
        }
        armor.itemMeta = meta
        return armor
    }

    /*
    fun getByItemKey(text: String): Materials? {
        try {
            val mat = Materials.valueOf(text.uppercase(Locale.getDefault()))
            return mat
        }
        catch (exception: IllegalArgumentException) {
            return null
        }
    }

     */

    /* OLD ODYSSEY ITEM CLASS
    fun createItemStack(amount: Int): ItemStack {
        val itemStack = ItemStack(material, amount)
        // On item meta; Add lore, display name, custom model, damage stats, effects, color if applicable
        var enchantSlots = 0
        var gildedSlots = 0
        itemStack.itemMeta = (itemStack.itemMeta as ItemMeta).also {
            if (displayName != null) { it.displayName(displayName) }
            if (lore != null) { it.lore(lore) }
            if (customModel != null) { it.setCustomModelData(customModel) }
            if (enchantments != null) {
                for (enchant in enchantments) {
                    if (enchant.key.isOdysseyEnchant()) { gildedSlots += 1 } else { enchantSlots += 1 }
                    it.addEnchant(enchant.key, enchant.value, true)
                }
            }
            it.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, name)
        }
        if (enchantments != null) {
            itemStack.addTag(ItemDataTags.IS_SLOTTED)
            itemStack.setIntTag(ItemDataTags.ENCHANT_SLOTS, enchantSlots)
            itemStack.setIntTag(ItemDataTags.GILDED_SLOTS, gildedSlots)
            itemStack.updateSlotLore()
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return itemStack
    }
 */

}