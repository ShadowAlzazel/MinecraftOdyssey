package me.shadowalzazel.mcodyssey.enchantments.base

import me.shadowalzazel.mcodyssey.Odyssey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Component as McComponent
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
    val translatableName: String,
    val maximumLevel: Int,
    val subtype: Subtype = Subtype.GILDED) :
    Enchantment(
        Rarity.COMMON,
        EnchantmentCategory.WEAPON,
        EquipmentSlot.entries.toTypedArray()
    ) {

    internal val romanNum = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    // Important Method to convert OdysseyEnchant To Enchantment[Bukkit]
    fun toBukkit(): org.bukkit.enchantments.Enchantment {
        val foundEnchant = EnchantRegistry.get(NamespacedKey.minecraft(name))
        return foundEnchant!!
    }

    init {
        descriptionId = translatableName
    }

    /* Minecraft NMS Methods */
    override fun getRarity(): Rarity = Rarity.COMMON
    override fun getMinLevel(): Int = 1
    override fun getMaxLevel(): Int = maximumLevel
    override fun getMinCost(level: Int): Int = 1
    override fun getMaxCost(level: Int): Int = 15
    override fun getDamageProtection(var0: Int, var1: DamageSource): Int = 0
    override fun getDamageBonus(var0: Int, var1: MobType): Float = 0f
    override fun checkCompatibility(other: Enchantment): Boolean { return this != other }
    override fun getOrCreateDescriptionId(): String {
        if (descriptionId == null) {
            descriptionId = Util.makeDescriptionId("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(this))
        }
        return descriptionId ?: translatableName
    }
    override fun getDescriptionId(): String = this.getOrCreateDescriptionId()
    override fun getFullname(level: Int): McComponent {
        val mutableComponent: MutableComponent = McComponent.literal(translatableName)
        if (this.isCurse) {
            mutableComponent.withStyle(ChatFormatting.RED)
        } else {
            mutableComponent.withStyle(ChatFormatting.GRAY)
        }

        if (level != 1 || this.maximumLevel != 1) {
            mutableComponent.append(CommonComponents.SPACE).append(McComponent.translatable("enchantment.level.$level"))
        }

        println("Mutable: $mutableComponent")
        println("Desc ID: $descriptionId")
        return mutableComponent
    }

    override fun canEnchant(itemStack: net.minecraft.world.item.ItemStack): Boolean {
        return category.canEnchant(itemStack.item)
    }
    override fun doPostAttack(var0: LivingEntity, var1: Entity, var2: Int) = Unit
    override fun doPostHurt(var0: LivingEntity, var1: Entity, var2: Int) = Unit
    override fun isTreasureOnly(): Boolean = false
    override fun isCurse(): Boolean = false
    override fun isTradeable(): Boolean = true
    override fun isDiscoverable(): Boolean = true


    /* Bukkit Methods */
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
        return Component.text("$translatableName ${romanNum[level]}", subtype.displayColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun displayName(level: Int): Component {
        return Component.text("$translatableName ${romanNum[level]}", subtype.displayColor)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun getGrayComponentText(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(TextColor.color(170, 170, 170))
    }

    fun getKey(): NamespacedKey {
        return NamespacedKey(Odyssey.instance, name)
    }

    // Get tool tip
    open fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        return listOf(
            getGrayComponentText(translatableName)
        )
    }

    // Get Conflicts
    open fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        return false
    }
}