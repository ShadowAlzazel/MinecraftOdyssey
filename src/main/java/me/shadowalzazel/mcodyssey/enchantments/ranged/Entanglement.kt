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

object Entanglement : OdysseyEnchantment(
    "entanglement",
    "Entanglement",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.BOW_ENCHANTABLE,
    ItemTags.BOW_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.BOW, Material.CROSSBOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = inputLevel
        val text1 = "On projectile hit, the target is tagged. If another target is tagged "
        val text2 = "and within 10 blocks, they are pulled together and damaged for $amount1=[level]."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}