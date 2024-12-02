package me.shadowalzazel.mcodyssey.datagen.recipes

import org.bukkit.inventory.MerchantRecipe

class MerchantRecipes {

    fun getRecipes(): List<MerchantRecipe> {
        return listOf(

        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    /*
 fun createHourglassTradeRecipe(): MerchantRecipe {
     val result = ItemStack(Material.HONEYCOMB)
     val trade = MerchantRecipe(result, 1, 20, true).apply {
         setIgnoreDiscounts(true)
         addIngredient(Miscellaneous.PRIMO_GEM.newItemStack(15))
     }
     return trade
 }

 fun lowTierTomeTrade(): MerchantRecipe {
     val tomes = listOf(Miscellaneous.TOME_OF_DISCHARGE, Miscellaneous.TOME_OF_PROMOTION)
     val item = tomes.random().newItemStack(1)
     return MerchantRecipe(item, 1, (2..4).random(), true).apply {
         setIgnoreDiscounts(true)
         addIngredient(ItemStack(Material.EMERALD, (23..32).random()))
     }
 }

 fun midTierTomeTrade(): MerchantRecipe {
     val tomes = listOf(Miscellaneous.TOME_OF_IMITATION, Miscellaneous.TOME_OF_EXPENDITURE)
     val item = tomes.random().newItemStack(1)
     return MerchantRecipe(item, 1, (2..3).random(), true).apply {
         setIgnoreDiscounts(true)
         addIngredient(ItemStack(Material.EMERALD, (30..37).random()))
     }
 }


 fun lowLevelArcaneBookTrade(): MerchantRecipe {
     val tempBook = ItemStack(Material.BOOK).enchantWithLevels(10, false, Random())
     val enchant = (tempBook.itemMeta as EnchantmentStorageMeta).storedEnchants.toList().first()
     val item = Miscellaneous.ARCANE_BOOK.createArcaneBookStack(enchant.first, enchant.second)
     return MerchantRecipe(item, 1, (2..3).random(), true).apply {
         setIgnoreDiscounts(true)
         addIngredient(ItemStack(Material.EMERALD, (26..36).random()))
     }
 }

  */

    /*
    fun prismaticBookTrade(): MerchantRecipe {
        val book = Miscellaneous.BLANK_TOME.newItemStack(1)
        return MerchantRecipe(book, 1, (5..9).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (13..21).random()))
        }
    }

     */

}