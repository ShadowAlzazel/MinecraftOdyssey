package me.shadowalzazel.mcodyssey.common.enchantments

import io.papermc.paper.datacomponent.DataComponentType
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import kotlin.collections.component1
import kotlin.collections.component2

interface EnchantabilityHandler : EnchantmentManager, DescriptionManager, DataTagManager {

    /*-- Shared component helpers ------------------------------------------------------------*/
    fun Enchantment.enchantabilityCost(level: Int = 1): Int {
        return maxOf((this.anvilCost / 2), 1) * level
    }

    fun getEnchantabilityCost(enchant: Pair<Enchantment, Int>, override: Int? = null): Int {
        val level = override ?: enchant.second
        return enchant.first.enchantabilityCost(level)
    }

    // Extension Helper Functions
    fun ItemStack.getMaxEnchantabilityPoints(): Int {
        return itemEnchantabilityPoints(this)
    }

    fun ItemStack.setMaxEnchantabilityPoints(amount: Int) {
        setIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS, amount)
    }

    fun ItemStack.getUsedEnchantabilityPoints(): Int {
        var usedEnchantabilityPoints = 0

        if (this.enchantments.isNotEmpty()) {
            for (enchant in this.enchantments) {
                usedEnchantabilityPoints += enchant.key.enchantabilityCost(enchant.value)
            }
        }

        return usedEnchantabilityPoints
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Point Default
    private fun itemEnchantabilityPoints(item: ItemStack): Int {
        // And tool/armor type
        val baseMaterialPoints = when(item.type) {
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET,
            Material.GOLDEN_HELMET, Material.LEATHER_HELMET, Material.TURTLE_HELMET -> {
                35
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS,
            Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                35
            }
            Material.BOW -> {
                35
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                35
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.ELYTRA -> {
                35
            }
            else -> 35
        }
        var bonusPoints = 0
        val materialType = item.getStringTag(ItemDataTags.MATERIAL_TYPE)
        val isEnchantableMaterial = materialType == "mithril" || materialType == "crystal_alloy"
        val nameId = item.getItemNameId()
        val hasEnchantableName = nameId.contains("mithril") || nameId.contains("crystal_alloy")
        if (isEnchantableMaterial || hasEnchantableName) {
            bonusPoints += 5
        }
        // Exotics
        when(nameId) {
            "excalibur" -> bonusPoints += 5
            "frost_fang", "knight_breaker", "abzu_blade", "elucidator", "shogun_lightning" -> {
                bonusPoints += 5
            }
            "laplace_spear", "flaming_wolf_spear", "blade_broken_by_storms", "shogun_pulse", "firewood_staff" -> {
                bonusPoints += 0
            }
        }

        // Extra points
        val extraPoints = item.getIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS) ?: 0
        bonusPoints += extraPoints

        return baseMaterialPoints + bonusPoints
    }

}