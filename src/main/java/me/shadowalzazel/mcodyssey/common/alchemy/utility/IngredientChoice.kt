package me.shadowalzazel.mcodyssey.common.alchemy.utility

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

sealed class IngredientChoice(open val amount: Int) {

    data class MaterialChoice(val material: Material, override val amount: Int) : IngredientChoice(amount)
    data class ItemNameChoice(val itemName: String, override val amount: Int) : IngredientChoice(amount)
    data class SpecificChoice(val itemStack: ItemStack, override val amount: Int) : IngredientChoice(amount)

}
