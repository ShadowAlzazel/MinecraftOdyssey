package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object SculkSensitive : OdysseyEnchantment(
    "sculk_sensitive",
    "Sculk Sensitive",
    3,
    1,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.HEAD)
) {

    override fun isTradeable(): Boolean = false
    override fun isDiscoverable(): Boolean = false

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 5 + (inputLevel * 5)
        val text1 = "On sneak, sense moving entities within a"
        val text2 = "$amount1=[5 + (level * 5)] block radius."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }

}