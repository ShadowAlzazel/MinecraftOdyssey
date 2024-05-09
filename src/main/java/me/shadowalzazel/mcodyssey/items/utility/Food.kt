package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.meta.components.FoodComponent

class Food(itemName: String, overrideMaterial: Material, customName: String, customModel: Int? = null, lore: List<Component>? = null) : OdysseyItem(
    itemName,
    overrideMaterial,
    customName,
    customModel,
    lore
) {

    fun createFoodStack() {
        val metaFood = super.newItemStack(1).itemMeta.food
    }


}