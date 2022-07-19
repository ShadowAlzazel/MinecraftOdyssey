package me.shadowalzazel.mcodyssey.enchantments

import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class BaneOfTheSwine(key: NamespacedKey) : Enchantment(key) {

    override fun translationKey(): String {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return "BaneOfTheSwine"
    }

    override fun getMaxLevel(): Int {
        return 5
    }

    override fun getStartLevel(): Int {
        return 1
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.WEAPON
    }

    override fun isTreasure(): Boolean {
        return true
    }

    override fun isCursed(): Boolean {
        return false
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            DAMAGE_ARTHROPODS, DAMAGE_ALL, DAMAGE_UNDEAD -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return false
    }

    override fun displayName(level: Int): Component {
        TODO("Not yet implemented")
    }

    override fun isTradeable(): Boolean {
        return false
    }

    override fun isDiscoverable(): Boolean {
        return false
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0.0F
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf(EquipmentSlot.HAND)
    }
}