package me.shadowalzazel.mcodyssey.datagen.items

import me.shadowalzazel.mcodyssey.util.NamedKeys
import me.shadowalzazel.mcodyssey.util.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.common.items.custom.*
import me.shadowalzazel.mcodyssey.util.AttributeManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType

interface ItemCreator : EnchantabilityHandler, AttributeManager {

    fun OdysseyItem.createArcaneBookStack(enchantment: Enchantment, level: Int = 1) : ItemStack {
        if (itemName != "arcane_book") return ItemStack(Material.AIR)
        val newBook = this.newItemStack(1)
        newBook.itemMeta = (newBook.itemMeta as EnchantmentStorageMeta).also {
            // Set lore -> description and enchantability Cost
            val pointCost = enchantment.enchantabilityCost(level)
            val costToolTip = createEnchantLoreComponent(enchantment, level, pointCost)
            val descriptionToolTip = enchantment.getDescriptionTooltip(level)
            val fullLore = listOf(costToolTip) + descriptionToolTip
            it.lore(fullLore)
            // Set name
            val bookName = Component.text(this.customName + ": ")
            val fullName = bookName.append(enchantment.displayName(level).color(bookName.color())).color(bookName.color())
            it.displayName(fullName.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            it.addStoredEnchant(enchantment, level, true)
            it.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        }
        return newBook
    }



}