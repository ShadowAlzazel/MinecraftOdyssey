package me.shadowalzazel.mcodyssey.recipes


import me.shadowalzazel.mcodyssey.items.Miscellaneous
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class Merchant {

    fun getRecipes(): List<MerchantRecipe> {
        return listOf(

        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    fun createHourglassTradeRecipe(): MerchantRecipe {
        val someResult = ItemStack(Material.HONEYCOMB)
        val someTrade = MerchantRecipe(someResult, 1, 20, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(Miscellaneous.PRIMO_GEM.newItemStack(15))
        }

        return someTrade
    }

}