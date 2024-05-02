package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

// Turns Junk into Gems
object OShiny : OdysseyEnchantment(
    "o_shiny",
    "O' Shiny",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    3,
    ItemTags.FISHING_ENCHANTABLE,
    ItemTags.FISHING_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {
    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.FISHING_ROD -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
