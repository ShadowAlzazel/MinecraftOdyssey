package me.shadowalzazel.mcodyssey.enchantments.utility

import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack


// REFACTOR LATER
open class OdysseyEnchantment(key: NamespacedKey, name: String) : Enchantment(key) {

    val odysseyName: String = name

    override fun translationKey(): String {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return odysseyName
    }

    override fun getMaxLevel(): Int {
        TODO("Not yet implemented")
    }

    override fun getStartLevel(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemTarget(): EnchantmentTarget {
        TODO("Not yet implemented")
    }

    override fun isTreasure(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCursed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        TODO("Not yet implemented")
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        TODO("Not yet implemented")
    }

    override fun displayName(level: Int): Component {
        TODO("Not yet implemented")
    }

    override fun isTradeable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isDiscoverable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRarity(): EnchantmentRarity {
        TODO("Not yet implemented")
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        TODO("Not yet implemented")
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        TODO("Not yet implemented")
    }
}