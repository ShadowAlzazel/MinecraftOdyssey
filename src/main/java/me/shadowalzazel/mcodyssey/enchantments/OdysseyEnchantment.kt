package me.shadowalzazel.mcodyssey.enchantments

import io.papermc.paper.adventure.PaperAdventure
import me.shadowalzazel.mcodyssey.enchantments.util.Subtype
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantment.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.minecraft.network.chat.Component as chatComponent
import org.bukkit.enchantments.Enchantment as BukkitEnchant

open class OdysseyEnchantment(
    val name: String,
    val translatableName: String,
    val maximumLevel: Int,
    rarity: Int = 10, // VERY - 1, RARE - 5, UNCOMMON - 10, COMMON - 20
    minCost: Cost = constantCost(10),
    maxCost: Cost = dynamicCost(10, 10),
    anvilCost: Int = 2,
    supportedItems: TagKey<Item> = ItemTags.WEAPON_ENCHANTABLE,
    primaryItems: TagKey<Item>  = ItemTags.WEAPON_ENCHANTABLE,
    equipmentSlots: Array<EquipmentSlot> = EquipmentSlot.entries.toTypedArray(),
    val subtype: Subtype = Subtype.NORMAL
) {
    // ----------------------------------------------------------------------------------------
    val properties: EnchantmentDefinition = definition(
        supportedItems,
        primaryItems,
        rarity, // Weight
        maximumLevel,
        minCost,
        maxCost,
        anvilCost,
        *equipmentSlots // Array to varargs
    )
    val isCurse = subtype == Subtype.CURSE
    var descriptionId: String? = null

    /* Conflict Methods */
    fun conflictsWith(nmsEnchant: Enchantment): Boolean  {
        return checkNmsConflict(nmsEnchant)
    }
    fun conflictsWith(bukkitEnchant: org.bukkit.enchantments.Enchantment): Boolean {
        return checkBukkitConflict(bukkitEnchant)
    }
    fun conflictsWith(odysseyEnchant: OdysseyEnchantment): Boolean {
        return checkOdysseyConflict(odysseyEnchant)
    }
    open fun checkOdysseyConflict(other: OdysseyEnchantment) : Boolean {
        val notSame = this != other
        return notSame
    }

    /* NMS Methods */
    fun getOrCreateDescriptionId(): String {
        if (descriptionId == null) {
            val resource = ResourceLocation("odyssey", name)
            this.descriptionId = Util.makeDescriptionId("enchantment", resource)
        }
        return descriptionId!!
    }
    fun getFullId(): String = this.getOrCreateDescriptionId() //getDescriptionId
    fun getFullname(level: Int): chatComponent {
        val mutableComponent: MutableComponent = chatComponent.translatable(getFullId())
        if (isCurse) {
            mutableComponent.withStyle(ChatFormatting.RED)
        } else {
            mutableComponent.withStyle(ChatFormatting.GRAY)
        }
        if (level != 1 || this.maximumLevel != 1) {
            mutableComponent.append(CommonComponents.SPACE).append(chatComponent.translatable("enchantment.level.$level"))
        }
        return mutableComponent
    }
    fun displayName(level: Int): Component {
        return PaperAdventure.asAdventure(this.getFullname(level))
    }
    open fun getDamageProtection(level: Int, source: DamageSource): Int = 0
    open fun checkNmsConflict(other: Enchantment): Boolean {
        val notSame = this != other
        return notSame
    }
    open fun canEnchantStack(itemStack: net.minecraft.world.item.ItemStack): Boolean {
        return itemStack.`is`(properties.supportedItems)
    }
    open fun doPostAttack(user: LivingEntity, target: Entity, level: Int) = Unit
    open fun doPostHurt(user: LivingEntity, target: Entity, level: Int) = Unit
    open fun isTreasureOnly(): Boolean = false
    open fun isTradeable(): Boolean = false
    open fun isDiscoverable(): Boolean = false

    /* Old Bukkit Methods */
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
    open fun checkBukkitConflict(other: BukkitEnchant): Boolean {
        return false
    }

    /* Old Odyssey Methods */
    internal fun getGrayComponentText(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(TextColor.color(170, 170, 170))
    }
    // Get tool tip
    open fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        return listOf(
            getGrayComponentText("$translatableName[WIP] How this get here.")
        )
    }

}