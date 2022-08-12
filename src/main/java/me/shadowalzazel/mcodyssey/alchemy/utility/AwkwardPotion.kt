package me.shadowalzazel.mcodyssey.alchemy.utility

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

object AwkwardPotion {

    fun createAwkwardPotion(): ItemStack {
        val potionItem = ItemStack(Material.POTION, 1)
        val somePotionMeta = potionItem.itemMeta as PotionMeta
        somePotionMeta.basePotionData = PotionData(PotionType.AWKWARD)
        potionItem.itemMeta = somePotionMeta
        return potionItem
    }

}