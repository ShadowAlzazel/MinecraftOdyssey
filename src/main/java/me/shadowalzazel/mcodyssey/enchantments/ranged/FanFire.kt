package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object FanFire : OdysseyEnchantment(
    "fan_fire",
    "Fan Fire",
    3,
    Rarity.UNCOMMON,
    EnchantmentCategory.CROSSBOW,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.CROSSBOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = inputLevel
        val text1 = "On projectile shoot, shoot $amount1=[level] extra projectiles"
        val text2 = "at the nearest enemies within line of sight. (Velocity reduced by 50%)."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }


}