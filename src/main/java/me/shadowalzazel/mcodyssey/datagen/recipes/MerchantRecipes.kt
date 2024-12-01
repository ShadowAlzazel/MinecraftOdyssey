package me.shadowalzazel.mcodyssey.datagen.recipes


import me.shadowalzazel.mcodyssey.common.items.custom.Miscellaneous
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class MerchantRecipes {

    fun getRecipes(): List<MerchantRecipe> {
        return listOf(

        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    fun createHourglassTradeRecipe(): MerchantRecipe {
        val result = ItemStack(Material.HONEYCOMB)
        val trade = MerchantRecipe(result, 1, 20, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(Miscellaneous.PRIMO_GEM.newItemStack(15))
        }
        return trade
    }

}