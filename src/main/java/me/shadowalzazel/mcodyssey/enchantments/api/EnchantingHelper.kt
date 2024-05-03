package me.shadowalzazel.mcodyssey.enchantments.api

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface EnchantingHelper {

    // Checks
    fun getApplicableEnchantments(item: ItemStack, playerLevel: Int, allowTreasure: Boolean = false): Map<OdysseyEnchantment, Int> {
        val applicableMap = mutableMapOf<OdysseyEnchantment, Int>()
        val isBook = item.type == Material.BOOK
        for (enchant in OdysseyEnchantments.ALL_SET) {
            // Check if enable
            if (!enchant.isDiscoverable()) continue
            if (enchant.isTreasureOnly() && !allowTreasure) continue
            if (!enchant.canEnchantItem(item) && !isBook) continue
            for (x in enchant.maximumLevel downTo 1) {
                if (playerLevel >= enchant.properties.minCost.calculate(x)
                    && playerLevel <= enchant.properties.maxCost.calculate(x)) {
                    applicableMap[enchant] = x
                    break
                }
            }
        }
        return applicableMap
    }

    fun selectEnchantments(item: ItemStack, playerLevel: Int, allowTreasure: Boolean = false) {
        val enchantability = 1

    }



}