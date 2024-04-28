package me.shadowalzazel.mcodyssey.alchemy.utility

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

object ThickPotion {

    fun createThickPotion(): ItemStack {
        val potionItem = ItemStack(Material.POTION, 1)
        val meta = potionItem.itemMeta as PotionMeta
        meta.basePotionType = PotionType.THICK
        potionItem.itemMeta = meta
        return potionItem
    }

    // Change Event Brew Thick Potion -> apply custom model
}