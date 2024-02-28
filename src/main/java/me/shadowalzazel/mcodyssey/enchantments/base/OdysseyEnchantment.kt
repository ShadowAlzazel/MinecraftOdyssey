package me.shadowalzazel.mcodyssey.enchantments.base

import io.papermc.paper.enchantments.EnchantmentRarity
import me.shadowalzazel.mcodyssey.Odyssey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

open class OdysseyEnchantment(
    namespace: String?,
    val enchantName: String,
    val maximumLevel: Int,
    val subtype: Subtype = Subtype.GILDED) :
    Enchantment(NamespacedKey(Odyssey.instance, namespace!!)) {

    internal val romanNum = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    override fun translationKey(): String {
        TODO("Not yet implemented")
    }

    // 1.20.4
    /*
    override fun getKey(): NamespacedKey {
        return NamespacedKey(Odyssey.instance, namespace!!)
    }

    override fun getMinModifiedCost(p0: Int): Int {
        return 1
    }

    override fun getMaxModifiedCost(p0: Int): Int {
        return 1
    }
     */

    @Deprecated("Deprecated in Java", ReplaceWith("enchantName"))
    override fun getName(): String {
        return enchantName
    }

    override fun getMaxLevel(): Int {
        return maximumLevel
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

    fun displayLore(level: Int): TextComponent {
        return Component.text("$enchantName ${romanNum[level]}", subtype.displayColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    override fun displayName(level: Int): Component {
        return Component.text("$enchantName ${romanNum[level]}", subtype.displayColor)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun getGrayComponentText(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(TextColor.color(170, 170, 170))
    }

    open fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        return listOf(
            getGrayComponentText(enchantName)
        )
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