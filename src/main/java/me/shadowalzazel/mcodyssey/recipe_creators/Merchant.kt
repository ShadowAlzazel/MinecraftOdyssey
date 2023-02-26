package me.shadowalzazel.mcodyssey.recipe_creators


import me.shadowalzazel.mcodyssey.items.Miscellaneous
import org.bukkit.inventory.MerchantRecipe

class Merchant {

    // Register Recipes
    fun getRecipes(): List<MerchantRecipe> {
        return listOf(
            createHourglassTradeRecipe(),
        )
    }

    /* ---------------------------------------------------------------------------*/

    // TODO: MAKE RANDOM PULL!!!!

    fun createHourglassTradeRecipe(): MerchantRecipe {
        val someResult = Miscellaneous.HOURGLASS_FROM_BABEL.createItemStack(1)
        val someTrade = MerchantRecipe(someResult, 1, 20, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(Miscellaneous.PRIMO_GEM.createItemStack(15))
        }

        return someTrade
    }

}