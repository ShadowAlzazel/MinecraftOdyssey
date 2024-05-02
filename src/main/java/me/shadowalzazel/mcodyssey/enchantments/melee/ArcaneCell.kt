package me.shadowalzazel.mcodyssey.enchantments.melee

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

object ArcaneCell : OdysseyEnchantment(
    "arcane_cell",
    "Arcane Cell",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.WEAPON_ENCHANTABLE,
    ItemTags.WEAPON_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun checkOdysseyConflict(other: OdysseyEnchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.GRAVITY_WELL -> {
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
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount = 2 + (inputLevel * 2)
        val text1 = "Create a circular zone with a radius of 5 for $amount=[2 + (level x 2)] seconds."
        val text2 = "The target can not leave, and are teleported inside if they try."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}