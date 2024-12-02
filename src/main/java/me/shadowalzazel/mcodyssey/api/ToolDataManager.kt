package me.shadowalzazel.mcodyssey.api

import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ToolDataManager : DataTagManager{

    fun getToolType(item: ItemStack): String? {
        return item.getStringTag(ItemDataTags.TOOL_TYPE) ?: getToolTypeFromMaterial(item.type)
    }

    fun getToolMaterial(item: ItemStack): String? {
        return item.getStringTag(ItemDataTags.MATERIAL_TYPE)
    }

    private fun getToolTypeFromMaterial(material: Material): String? {
        return when(material) {
            Material.WOODEN_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD,
            Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD -> {
                "sword"
            }
            Material.WOODEN_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE -> {
                "pickaxe"
            }
            Material.WOODEN_AXE, Material.GOLDEN_AXE, Material.STONE_AXE,
            Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE -> {
                "axe"
            }
            Material.WOODEN_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL,
            Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL -> {
                "shovel"
            }
            Material.WOODEN_HOE, Material.GOLDEN_HOE, Material.STONE_HOE,
            Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE -> {
                "hoe"
            }
            else -> {
                null
            }
        }
    }

    fun toolIsIron(material: Material): Boolean {
        return when(material) {
            Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE -> true
            else -> false
        }
    }

    fun toolIsDiamond(material: Material): Boolean {
        return when(material) {
            Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE -> true
            else -> false
        }
    }


}