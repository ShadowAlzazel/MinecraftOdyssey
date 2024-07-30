package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class OdysseyFood(
    itemName: String,
    overrideMaterial: Material,
    customName: String,
    private val saturation: Float,
    private val nutrition: Int,
    private val eatTime: Float,
    customModel: Int? = null,
    lore: List<Component>? = null)
    : OdysseyItem(
    itemName,
    overrideMaterial,
    customName,
    customModel,
    lore
) {

    fun createFoodStack(amount: Int = 1): ItemStack {
        val item = super.newItemStack(amount)
        val foodComponent = item.itemMeta.food
        foodComponent.saturation = this.saturation
        foodComponent.nutrition = this.nutrition
        foodComponent.eatSeconds = eatTime
        val meta = item.itemMeta
        meta.setFood(foodComponent)
        meta.setMaxStackSize(64)
        item.itemMeta = meta
        return item
    }


}