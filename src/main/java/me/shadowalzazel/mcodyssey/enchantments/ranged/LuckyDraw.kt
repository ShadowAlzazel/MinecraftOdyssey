package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.INFINITY
import org.bukkit.inventory.ItemStack

object LuckyDraw : OdysseyEnchantment(
    "lucky_draw",
    "Lucky Draw",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.BOW_ENCHANTABLE,
    ItemTags.BOW_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            INFINITY -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.BOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 7 + (10 * inputLevel)
        val text1 = "$amount1=[7 + (level x 10)]% chance to not consume ammo."
        return listOf(
            getGrayComponentText(text1)
        )
    }


}