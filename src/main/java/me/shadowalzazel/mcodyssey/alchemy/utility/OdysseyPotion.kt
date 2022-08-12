package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect

//
open class OdysseyPotion(itemName: String, private val potionEffect: PotionEffect, private val potionColor: Color?) : OdysseyItem(itemName, Material.POTION) {

    override fun createItemStack(amount: Int): ItemStack {
        val somePotionStack = super.createItemStack(amount)
        val somePotionMeta = somePotionStack.itemMeta as PotionMeta
        somePotionMeta.addCustomEffect(potionEffect, true)
        if (potionColor != null) somePotionMeta.color = potionColor
        somePotionStack.itemMeta = somePotionMeta
        return somePotionStack
    }


}