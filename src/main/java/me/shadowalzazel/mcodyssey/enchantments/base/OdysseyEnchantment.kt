package me.shadowalzazel.mcodyssey.enchantments.base

import io.papermc.paper.enchantments.EnchantmentRarity
import me.shadowalzazel.mcodyssey.Odyssey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

open class OdysseyEnchantment(namespace: String?, private val name: String, private val maxLevel: Int) :
    Enchantment(NamespacedKey(Odyssey.instance, namespace!!)) {

    private val romanNumeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

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
        return Component.text("$name ${romanNumeralList[level]}", TextColor.color(255, 170, 0))
    }

    fun enchantLore(level: Int): TextComponent {
        return Component.text("$name ${romanNumeralList[level]}", TextColor.color(255, 170, 0))
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