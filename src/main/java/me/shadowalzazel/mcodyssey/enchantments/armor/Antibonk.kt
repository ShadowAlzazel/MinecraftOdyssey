package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Antibonk : OdysseyEnchantment(
    "antibonk",
    "Antibonk",
    3,
    10,
    constantCost(5),
    dynamicCost(5, 10),
    3,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.HEAD)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount = 2.5 * inputLevel
        val text = "Reduce critical hit damage by $amount=[2.5 x level]."
        return listOf(
            getGrayComponentText(text)
        )
    }

}