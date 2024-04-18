package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Untouchable : OdysseyEnchantment(
    "untouchable",
    "Untouchable",
    1,
    Rarity.RARE,
    EnchantmentCategory.ARMOR_CHEST,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.IGNORE_PAIN.toBukkit() -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val text1 = "Increase invulnerable time to 1 second"
        return listOf(
            getGrayComponentText(text1)
        )
    }

}