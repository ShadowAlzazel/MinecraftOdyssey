package me.shadowalzazel.mcodyssey.recipe_creators.merchant

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Arcane
import me.shadowalzazel.mcodyssey.items.Arcane.createGildedBook
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

object ArcaneSales {

    fun createLowTierTomeTrade(): MerchantRecipe {
        val tomes = listOf(Arcane.TOME_OF_EMBRACE, Arcane.TOME_OF_DISCHARGE, Arcane.TOME_OF_BANISHMENT)
        return MerchantRecipe(tomes.random().createItemStack(1), 1, (2..5).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (23..32).random()))
        }
    }

    fun createMidTierTomeTrade(): MerchantRecipe {
        val tomes = listOf(Arcane.TOME_OF_PROMOTION, Arcane.TOME_OF_HARMONY)
        return MerchantRecipe(tomes.random().createItemStack(1), 1, (2..3).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (30..37).random()))
        }
    }

    fun createLowTierGildedEnchantTrade(): MerchantRecipe {
        val enchants = listOf(OdysseyEnchantments.EXPLODING, OdysseyEnchantments.ASPHYXIATING_ASSAULT,
            OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.HEAVY_BALLISTICS)
        return MerchantRecipe(Arcane.GILDED_BOOK.createGildedBook(enchants.random(), 1), 1, 1, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (26..36).random()))
        }
    }

    fun createArcaneBookTrade(): MerchantRecipe {
        val book = Arcane.ARCANE_BOOK.createItemStack(1)
        return MerchantRecipe(book, 1, (5..9).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (13..21).random()))
        }
    }

}