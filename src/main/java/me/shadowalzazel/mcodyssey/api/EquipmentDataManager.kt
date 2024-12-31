package me.shadowalzazel.mcodyssey.api

import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface EquipmentDataManager : DataTagManager {

    fun getEquipmentType(item: ItemStack): String? {
        return item.getStringTag(ItemDataTags.TOOL_TYPE) ?: getDefaultType(item.type)
    }

    fun getEquipmentMaterial(item: ItemStack): String? {
        return item.getStringTag(ItemDataTags.MATERIAL_TYPE)
    }

    fun getDefaultType(material: Material): String? {
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
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                "leggings"
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                "chestplate"
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS,
            Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                "boots"
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET,
            Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET -> {
                "helmet"
            }
            else -> {
                null
            }
        }
    }

    fun itemIsArmor(material: Material): Boolean {
        return getDefaultType(material) in listOf("helmet", "chestplate", "leggings", "boots")
    }

    fun equipmentIsIron(material: Material): Boolean {
        return when(material) {
            Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS-> true
            else -> false
        }
    }

    fun equipmentIsDiamond(material: Material): Boolean {
        return when(material) {
            Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS-> true
            else -> false
        }
    }




}