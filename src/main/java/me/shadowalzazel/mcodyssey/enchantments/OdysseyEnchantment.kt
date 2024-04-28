package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.base.Subtype
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import net.minecraft.network.chat.Component as chatComponentNMS
import net.minecraft.tags.ItemTags as NMSItemTags
import org.bukkit.Registry.ENCHANTMENT as BukkitEnchantRegistry
import org.bukkit.enchantments.Enchantment as BukkitEnchant

open class OdysseyEnchantment(
    val name: String,
    val translatableName: String,
    val maximumLevel: Int,
    rarity: Int = 10, // VERY - 1, RARE - 5, UNCOMMON - 10, COMMON - 20
    minCost: Enchantment.Cost = Enchantment.constantCost(10),
    maxCost: Enchantment.Cost = Enchantment.dynamicCost(10, 10),
    anvilCost: Int = 2,
    supportedItems: TagKey<Item> = NMSItemTags.WEAPON_ENCHANTABLE,
    primaryItems: TagKey<Item>  = NMSItemTags.WEAPON_ENCHANTABLE,
    equipmentSlots: Array<EquipmentSlot> = EquipmentSlot.entries.toTypedArray(),
    val subtype: Subtype = Subtype.NORMAL
) : Enchantment(definition(
            supportedItems,
            primaryItems,
            rarity, // Weight
            maximumLevel,
            minCost,
            maxCost,
            anvilCost,
            *equipmentSlots // Array to varargs
    )) {
    // ------------------------------------------------------------------------------------------------------------

    // Important Method to convert OdysseyEnchantment[NMS] To Enchantment[Bukkit]
    fun toBukkit(): org.bukkit.enchantments.Enchantment {
        return BukkitEnchantRegistry.get(NamespacedKey(Odyssey.instance, name))!!
    }

    /* Minecraft NMS Methods */
    //override fun getMinLevel(): Int = 1
    //override fun getMaxLevel(): Int = maximumLevel
    // fun getMinCost(level: Int): Int = 1 + level * 10
   // override fun getMaxCost(level: Int): Int = getMinCost(level) + 10
    override fun getDamageProtection(level: Int, source: DamageSource): Int = 0
    //override fun getDamageBonus(level: Int, entityType: EntityType?): Float = 0f
    override fun checkCompatibility(other: Enchantment): Boolean {
        val notSame = this != other
        return notSame
    }
    override fun getOrCreateDescriptionId(): String {
        println(descriptionId)
        if (descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(this))
            //descriptionId = "enchantment.minecraft.$name"
        }
        return descriptionId!!
    }
    override fun getDescriptionId(): String = this.getOrCreateDescriptionId()
    override fun getFullname(level: Int): chatComponentNMS {
        val mutableComponent: MutableComponent = chatComponentNMS.translatable(getDescriptionId())
        if (this.isCurse) {
            mutableComponent.withStyle(ChatFormatting.RED)
        } else {
            mutableComponent.withStyle(ChatFormatting.GRAY)
        }

        if (level != 1 || this.maximumLevel != 1) {
            mutableComponent.append(CommonComponents.SPACE).append(chatComponentNMS.translatable("enchantment.level.$level"))
        }

        println("Mutable: $mutableComponent")
        println("Desc ID: $descriptionId")
        return mutableComponent
    }

    override fun canEnchant(itemStack: net.minecraft.world.item.ItemStack): Boolean {
        return super.canEnchant(itemStack)
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
    // Get Conflicts
    open fun conflictsWith(other: BukkitEnchant): Boolean {
        return false
    }

    /* Odyssey Methods */
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

}