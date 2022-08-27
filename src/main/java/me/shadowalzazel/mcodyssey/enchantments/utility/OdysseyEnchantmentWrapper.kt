package me.shadowalzazel.mcodyssey.enchantments.utility

import io.papermc.paper.enchantments.EnchantmentRarity
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

open class OdysseyEnchantmentWrapper(namespace: String?, private val name: String, private val maxLevel: Int) :
    Enchantment(NamespacedKey(MinecraftOdyssey.instance, namespace!!)) {

    override fun translationKey(): String {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getName(): String {
        return name
    }

    override fun getMaxLevel(): Int {
        return maxLevel
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
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun displayName(level: Int): Component {
        var displayComponent: TextComponent = Component.text("$name $level").color(NamedTextColor.GOLD)
        return displayComponent

    }

    override fun isTradeable(): Boolean {
        return false
    }

    override fun isDiscoverable(): Boolean {
        return false
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.UNCOMMON
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0.0F
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf(EquipmentSlot.HAND)
    }
}