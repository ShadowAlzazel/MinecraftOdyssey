package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

//
open class OdysseyPotion(itemName: String, private val potionEffects: List<PotionEffect>, private val potionColor: Color?) : OdysseyItem(itemName, Material.POTION) {

    override fun createItemStack(amount: Int): ItemStack {
        val somePotionStack = super.createItemStack(amount)
        val somePotionMeta = somePotionStack.itemMeta as PotionMeta
        if (potionEffects.isNotEmpty()) {
            for (effect in potionEffects) {
                somePotionMeta.addCustomEffect(effect, true)
            }
        }
        else {
            somePotionMeta.basePotionData = PotionData(PotionType.UNCRAFTABLE)
        }
        if (potionColor != null) somePotionMeta.color = potionColor
        somePotionStack.itemMeta = somePotionMeta
        return somePotionStack
    }


}