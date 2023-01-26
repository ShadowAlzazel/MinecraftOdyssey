package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

open class OdysseyPotion(
    potionName: String,
    val potionDisplayName: Component,
    potionLore: List<Component>? = null,
    private val potionEffects: List<PotionEffect>,
    private val potionColor: Color?
    ) : OdysseyItem(potionName, Material.POTION, potionDisplayName, potionLore) {

    // Creates and Item Stack
    override fun createItemStack(amount: Int): ItemStack {
        val somePotionStack = super.createItemStack(amount)

        // Assign item meta
        somePotionStack.itemMeta = (somePotionStack.itemMeta as PotionMeta).also {
            // Check if list not empty for non-custom effects
            if (potionEffects.isNotEmpty()) {
                for (effect in potionEffects) { it.addCustomEffect(effect, true) }
            }
            // If custom effects then un-craft-able type
            else {
                it.basePotionData = PotionData(PotionType.UNCRAFTABLE)
            }
            // Potion color and name assigning
            if (potionColor != null) { it.color = potionColor }
            it.displayName(potionDisplayName)
        }

        return somePotionStack
    }


}