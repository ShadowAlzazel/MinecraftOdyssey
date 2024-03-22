package me.shadowalzazel.mcodyssey.enchantments.base

import me.shadowalzazel.mcodyssey.Odyssey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry.ENCHANTMENT as EnchantRegistry
import org.bukkit.inventory.ItemStack

open class OdysseyEnchantment(
    val name: String,
    val enchantName: String,
    val maximumLevel: Int,
    val subtype: Subtype = Subtype.GILDED) :
    Enchantment(
        Rarity.UNCOMMON,
        EnchantmentCategory.WEAPON,
        EquipmentSlot.entries.toTypedArray()
    ) {

    internal val romanNum = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    // Important Method to convert OdysseyEnchant To Enchantment[Bukkit]
    fun toBukkit(): org.bukkit.enchantments.Enchantment {
        val foundEnchant = EnchantRegistry.get(NamespacedKey.minecraft(name))
        return foundEnchant!!
    }


    /* Minecraft NMS Methods */
    override fun getRarity(): Rarity = Rarity.UNCOMMON
    override fun getMinLevel(): Int = 1
    override fun getMaxLevel(): Int = 1
    override fun getMinCost(var0: Int): Int = 0
    override fun getMaxCost(var0: Int): Int = 0
    override fun getDamageProtection(var0: Int, var1: DamageSource): Int = 0
    override fun getDamageBonus(var0: Int, var1: MobType): Float = 0f
    override fun checkCompatibility(var0: Enchantment): Boolean = false
    override fun getOrCreateDescriptionId(): String = ""
    override fun getDescriptionId(): String = ""
    override fun getFullname(var0: Int): net.minecraft.network.chat.Component = net.minecraft.network.chat.Component.empty()
    override fun canEnchant(var0: net.minecraft.world.item.ItemStack): Boolean = false
    override fun doPostAttack(var0: LivingEntity, var1: Entity, var2: Int) = Unit
    override fun doPostHurt(var0: LivingEntity, var1: Entity, var2: Int) = Unit
    override fun isTreasureOnly(): Boolean = false
    override fun isCurse(): Boolean = false
    override fun isTradeable(): Boolean = false
    override fun isDiscoverable(): Boolean = false


    open fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK -> {
                true
            }
            else -> {
                false
            }
        }
    }

    /* Odyssey Methods */
    fun displayLore(level: Int): TextComponent {
        return Component.text("$enchantName ${romanNum[level]}", subtype.displayColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun displayName(level: Int): Component {
        return Component.text("$enchantName ${romanNum[level]}", subtype.displayColor)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun getGrayComponentText(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(TextColor.color(170, 170, 170))
    }

    fun getKey(): NamespacedKey {
        return NamespacedKey(Odyssey.instance, name!!)
    }

    // Get tool tip
    open fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        return listOf(
            getGrayComponentText(enchantName)
        )
    }

    // Get Conflicts
    open fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        return false
    }
}