package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Ballistics : OdysseyEnchantment(
    "ballistics",
    "Ballistics",
    4,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.CROSSBOW_ENCHANTABLE,
    ItemTags.CROSSBOW_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun checkBukkitConflict(other: Enchantment): Boolean {
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
        val text1 = "Crossbows do $amount1=[level] more damage."
        return listOf(
            getGrayComponentText(text1)
        )
    }
}
