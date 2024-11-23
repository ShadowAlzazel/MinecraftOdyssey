package me.shadowalzazel.mcodyssey.common.alchemy.utility

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

object AwkwardPotion {
    fun createAwkwardPotion(): ItemStack {
        val potionItem = ItemStack(Material.POTION, 1)
        val meta = potionItem.itemMeta as PotionMeta
        meta.basePotionType = PotionType.AWKWARD
        potionItem.itemMeta = meta
        return potionItem
    }
}