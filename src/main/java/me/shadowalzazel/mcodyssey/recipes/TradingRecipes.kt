package me.shadowalzazel.mcodyssey.recipes


import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.inventory.MerchantRecipe

object TradingRecipes {

    var tradeList = listOf(createHourglassTradeRecipe())

    // Register Recipes
    fun getRecipes(): List<MerchantRecipe> {
        return listOf(
            createHourglassTradeRecipe(),
        )
    }

    /* ---------------------------------------------------------------------------*/

    // TODO: MAKE RANDOM PULL!!!!

    private fun createHourglassTradeRecipe(): MerchantRecipe {
        val someResult = OdysseyItems.HOURGLASS_FROM_BABEL.createItemStack(1)
        val someTrade = MerchantRecipe(someResult, 1, 20, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(OdysseyItems.GEMMA_PRIMUS.createItemStack(15))
        }

        return someTrade
    }

}