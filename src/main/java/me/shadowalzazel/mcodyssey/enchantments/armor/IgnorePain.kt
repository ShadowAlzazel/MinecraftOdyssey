package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object IgnorePain : OdysseyEnchantment(
    "ignore_pain",
    "Ignore Pain",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun checkOdysseyConflict(other: OdysseyEnchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.UNTOUCHABLE -> {
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
        val amount1 = 11 - (inputLevel * 2)
        val amount2 = 5 - inputLevel
        val text1 = "Decrease Invulnerable time when hit to $amount1=[11 - (level x 2)] ticks,"
        val text2 = "but gain absorption for $amount2=[5 - level] seconds."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}