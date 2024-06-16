package me.shadowalzazel.mcodyssey.recipes.merchant

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import me.shadowalzazel.mcodyssey.listeners.LootListeners.createArcaneBookStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

object ArcaneSales : ItemCreator {

    fun createLowTierTomeTrade(): MerchantRecipe {
        val tomes = listOf(Miscellaneous.TOME_OF_EMBRACE, Miscellaneous.TOME_OF_DISCHARGE, Miscellaneous.TOME_OF_BANISHMENT)
        return MerchantRecipe(tomes.random().newItemStack(1), 1, (2..5).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (23..32).random()))
        }
    }

    fun createMidTierTomeTrade(): MerchantRecipe {
        val tomes = listOf(Miscellaneous.TOME_OF_PROMOTION, Miscellaneous.TOME_OF_HARMONY)
        return MerchantRecipe(tomes.random().newItemStack(1), 1, (2..3).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (30..37).random()))
        }
    }

    fun createLowTierGildedEnchantTrade(): MerchantRecipe {
        val enchants = listOf(OdysseyEnchantments.EXPLODING, OdysseyEnchantments.ASPHYXIATE,
            OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BALLISTICS)
        return MerchantRecipe(Miscellaneous.ARCANE_BOOK.createArcaneBookStack(enchants.random(), 1), 1, 1, true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (26..36).random()))
        }
    }

    fun createArcaneBookTrade(): MerchantRecipe {
        val book = Miscellaneous.PRISMATIC_BOOK.newItemStack(1)
        return MerchantRecipe(book, 1, (5..9).random(), true).apply {
            setIgnoreDiscounts(true)
            addIngredient(ItemStack(Material.EMERALD, (13..21).random()))
        }
    }

}