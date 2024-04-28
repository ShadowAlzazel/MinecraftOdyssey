package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object BolaShot : OdysseyEnchantment(
    "bola_shot",
    "Bola Shot",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.CROSSBOW_ENCHANTABLE,
    ItemTags.CROSSBOW_ENCHANTABLE,
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
        val amount1 = 3 + inputLevel
        val text1 = "Applies Slowness 1 on the target for $amount1=[3 + level] seconds"
        val text2 = "and places a cobweb at their location."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}