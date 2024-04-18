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
import org.bukkit.enchantments.Enchantment as BukkitEnchant

open class OdysseyEnchantment(
    val name: String,
    val translatableName: String,
    val maximumLevel: Int,
    rarity: Rarity = Rarity.UNCOMMON,
    category: EnchantmentCategory = EnchantmentCategory.WEAPON,
    equipmentSlots: Array<EquipmentSlot> = arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND),
    val subtype: Subtype = Subtype.NORMAL) :
    Enchantment(
        rarity,
        category,
        equipmentSlots
    ) {

    private val romanNum = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    // Important Method to convert OdysseyEnchant To Enchantment[Bukkit]
    fun toBukkit(): org.bukkit.enchantments.Enchantment {
        return EnchantRegistry.get(NamespacedKey(Odyssey.instance, name))!!
    }

    /* Minecraft NMS Methods */
    /*
    override fun getRarity(): Rarity {
        return super.getRarity()
    }
     */

    override fun getMinLevel(): Int = 1
    override fun getMaxLevel(): Int = maximumLevel
    override fun getMinCost(level: Int): Int = 1 + level * 10
    override fun getMaxCost(level: Int): Int = getMinCost(level) + 10
    override fun getDamageProtection(level: Int, source: DamageSource): Int = 0
    override fun getDamageBonus(level: Int, group: MobType): Float = 0f
    override fun checkCompatibility(other: Enchantment): Boolean { return this != other }
    override fun getOrCreateDescriptionId(): String {
        println(descriptionId)
        if (descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(this))
            //descriptionId = "enchantment.minecraft.$name"
        }
        return descriptionId!!
    }
    override fun getDescriptionId(): String = this.getOrCreateDescriptionId()
    override fun getFullname(level: Int): McComponent {
        val mutableComponent: MutableComponent = McComponent.translatable(getDescriptionId())
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
    override fun doPostAttack(user: LivingEntity, target: Entity, level: Int) = Unit
    override fun doPostHurt(user: LivingEntity, target: Entity, level: Int) = Unit
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
    fun getTextForLore(level: Int): TextComponent {
        return Component.text("$translatableName ${romanNum[level]}", subtype.displayColor)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun getTextComponent(level: Int): Component {
        return Component.text("$translatableName ${romanNum[level]}", subtype.displayColor)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    internal fun getGrayComponentText(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(TextColor.color(170, 170, 170))
    }

    // Get tool tip
    open fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        return listOf(
            getGrayComponentText(translatableName)
        )
    }

    // Get Conflicts
    open fun conflictsWith(other: BukkitEnchant): Boolean {
        return false
    }

}