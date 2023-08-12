package me.shadowalzazel.mcodyssey.recipe_creators.merchant

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Arcane
import me.shadowalzazel.mcodyssey.items.Arcane.createEnchantedBook
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

object Sales {

    fun createLowTierTomeTrade(): MerchantRecipe {
        val tomes = listOf(
            Arcane.TOME_OF_EMBRACE, Arcane.TOME_OF_DISCHARGE, Arcane.TOME_OF_PROMOTION, Arcane.TOME_OF_BANISHMENT, Arcane.TOME_OF_HARMONY)
        val someTrade = MerchantRecipe(tomes.random().createItemStack(1), 1, (2..5).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (23..32).random()))
        }

        return someTrade
    }

    fun createLowTierGildedEnchantTrade(): MerchantRecipe {
        val enchants = listOf(OdysseyEnchantments.EXPLODING, OdysseyEnchantments.FREEZING_ASPECT,
            OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BURST_BARRAGE)
        val someTrade = MerchantRecipe(Arcane.GILDED_BOOK.createEnchantedBook(enchants.random(), 1), 1, 1, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (26..36).random()))
        }

        return someTrade
    }

    fun createArcaneBookTrade(): MerchantRecipe {
        val book = Arcane.ARCANE_BOOK.createItemStack(1)
        val someTrade = MerchantRecipe(book, 1, (5..9).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (13..21).random()))
        }

        return someTrade
    }


}