package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemArmorTrim
import io.papermc.paper.registry.set.RegistryKeySet
import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.common.items.ToolMaker
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.MobData
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import java.util.*

@Suppress("UnstableApiUsage")
interface LootEquipmentCreator : ToolMaker, EnchantabilityHandler {

    // Equipment Randomizer
    fun EquipmentRandomizer.newWeapon(randomParts: Boolean = true): ItemStack {
        return if (randomParts) {
            generateCompositeWeapon(listOf(toolType), listOf(toolMaterial), MobData.ALL_PARTS)
        } else {
            generateCompositeWeapon(listOf(toolType), listOf(toolMaterial), listOf(toolPattern))
        }
    }

    fun EquipmentRandomizer.newTrimmedArmor(): List<ItemStack> {
        return generateTrimmedArmorSet(armorMaterial, listOf(armorTrim.material), listOf(armorTrim.pattern))
    }


    fun enchantItemsWithTagSet(items: List<ItemStack>, keySet: RegistryKeySet<Enchantment>, level: Int=25) {
        items.forEach {
            val copy = it.enchantWithLevels(minOf(30, level), keySet, Random())
            it.setData(DataComponentTypes.ENCHANTMENTS, copy.getData(DataComponentTypes.ENCHANTMENTS)!!)
            it.updateEnchantPoints()
        }
    }

    fun enchantItemsRandomly(items: List<ItemStack>, level: Int=25) {
        items.forEach {
            val copy = it.enchantWithLevels(minOf(30, level), false, Random())
            it.setData(DataComponentTypes.ENCHANTMENTS, copy.getData(DataComponentTypes.ENCHANTMENTS)!!)
            it.updateEnchantPoints()
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    // Generator functions generate a random item based on a list of custom options

    // Returns a full set of trimmed armor
    fun generateTrimmedArmorSet(material: String, trimMaterials: List<TrimMaterial>, trimPatterns: List<TrimPattern>): List<ItemStack> {
        val vanillaMaterials = listOf("diamond", "netherite", "iron", "chainmail", "golden", "stone", "wooden")
        val armorSet = if (material in vanillaMaterials) createVanillaArmorSet(material) else createDataArmorSet(material)
        val randomTrim = generateRandomArmorTrim(trimMaterials, trimPatterns)
        val armorTrim = ItemArmorTrim.itemArmorTrim(randomTrim)
        armorSet.forEach { it.setData(DataComponentTypes.TRIM, armorTrim) }
        return armorSet
    }

    // Return a composite custom weapon
    fun generateCompositeWeapon(weaponTypes: List<ToolType>, toolMaterials: List<ToolMaterial>, partSet: List<String>): ItemStack {
        val weapon = generateNewRandomWeapon(weaponTypes, toolMaterials)
        weapon.apply {
            val weaponType = getStringTag(ItemDataTags.TOOL_TYPE)!!
            setData(DataComponentTypes.CUSTOM_MODEL_DATA, generateRandomWeaponParts(partSet, weaponType))
        }
        return weapon
    }

    private fun generateNewRandomWeapon(weaponTypes: List<ToolType>, toolMaterials: List<ToolMaterial>): ItemStack {
        return createToolStack(toolMaterials.random(), weaponTypes.random())
    }

    private fun generateRandomWeaponParts(patterns: List<String>, toolType: String): CustomModelData.Builder {
        val pat = { name: String -> "${patterns.random()}_${name}"}
        val parts = mutableListOf(toolType, pat("blade"), pat("handle"), pat("hilt"), pat("pommel"), "no_trim")
        val customData = CustomModelData.customModelData().addStrings(parts)
        return customData
    }

    private fun generateRandomArmorTrim(materials: List<TrimMaterial>, patterns: List<TrimPattern>): ArmorTrim {
        return ArmorTrim(materials.random(), patterns.random())
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Creator functions accept non lists to create items

    private fun createWeaponParts(pattern: String, toolType: String): CustomModelData.Builder {
        val pat = { name: String -> "${pattern}_${name}"}
        val parts = mutableListOf(toolType, pat("blade"), pat("handle"), pat("hilt"), pat("pommel"), "no_trim")
        val customData = CustomModelData.customModelData().addStrings(parts)
        return customData
    }

    private fun createDataArmorSet(material: String): List<ItemStack> {
        val lootItem = { name: String -> LootTableManager.createItemStackFromLoot("${material}_${name}")}
        val dataItems = listOf(lootItem("helmet"), lootItem("chestplate"), lootItem("leggings"), lootItem("boots"))
        return dataItems
    }

    private fun createVanillaArmorSet(material: String): List<ItemStack> {
        val matItem = { name: String -> ItemStack(Material.matchMaterial("${material}_${name}")!!)}
        val dataItems = listOf(matItem("helmet"), matItem("chestplate"), matItem("leggings"), matItem("boots"))
        return dataItems
    }

    /*-----------------------------------------------------------------------------------------------*/

    fun LivingEntity.setArmorDropChances(chance: Float) {
        this.equipment?.also {
            it.helmetDropChance = chance
            it.chestplateDropChance = chance
            it.leggingsDropChance = chance
            it.bootsDropChance = chance
        }
    }

    fun LivingEntity.setArmor(list: List<ItemStack>) {
        // Apply
        this.equipment?.also {
            it.helmet = list[0]
            it.chestplate = list[1]
            it.leggings = list[2]
            it.boots = list[3]
        }
    }

}