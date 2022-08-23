package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

//
open class OdysseyPotion(potionName: String, val potionDisplayName: TextComponent, potionLore: List<String>? = null, private val potionEffects: List<PotionEffect>, private val potionColor: Color?) :
    OdysseyItem(potionName, Material.POTION, potionDisplayName, potionLore) {

    // Creates and Item Stack
    override fun createItemStack(amount: Int): ItemStack {
        val somePotionStack = super.createItemStack(amount)
        val somePotionMeta = somePotionStack.itemMeta as PotionMeta
        // Check if list not empty for non-custom effects
        if (potionEffects.isNotEmpty()) {
            for (effect in potionEffects) {
                somePotionMeta.addCustomEffect(effect, true)
            }
        }
        // If custom effects then un-craft-able type
        else {
            somePotionMeta.basePotionData = PotionData(PotionType.UNCRAFTABLE)
        }
        // Potion color and meta assigning
        if (potionColor != null) somePotionMeta.color = potionColor
        somePotionStack.itemMeta = somePotionMeta
        return somePotionStack
    }


}