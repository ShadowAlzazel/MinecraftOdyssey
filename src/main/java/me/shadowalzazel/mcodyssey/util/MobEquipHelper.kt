package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.trims.TrimMaterials
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import java.util.*

interface MobEquipHelper {

    fun createTrimmedArmor(entity: LivingEntity, trim: ArmorTrim, type: String, randomEnchants: Boolean = true) {
        // Create armors from type
        val helmet = ItemStack(Material.IRON_HELMET)
        val chestplate = ItemStack(Material.IRON_CHESTPLATE)
        val leggings = ItemStack(Material.IRON_LEGGINGS)
        val boots = ItemStack(Material.IRON_BOOTS)
        val armorList = listOf(helmet, chestplate, leggings, boots)
        // Enchant Armors
        if (randomEnchants) {
            val randomSeed = Random()
            for (armor in armorList) {
                armor.enchantWithLevels(30, false, randomSeed)
            }
        }
        // Trim
        for (armor in armorList) {
            val meta = armor.itemMeta as ArmorMeta
            meta.trim = trim
            armor.itemMeta = meta
        }

        // Apply
        entity.equipment?.also {
            it.helmet = helmet
            it.chestplate = chestplate
            it.leggings = leggings
            it.boots = boots
        }

    }

    fun createRandomArmorTrim(materials: List<TrimMaterial>? = null, patterns: List<TrimPattern>? = null): ArmorTrim {
        val mats = materials ?: listOf(TrimMaterial.AMETHYST, TrimMaterial.COPPER, TrimMaterial.DIAMOND,
            TrimMaterials.JADE, TrimMaterials.OBSIDIAN, TrimMaterials.TITANIUM)
        val pats = patterns ?: listOf(TrimPattern.WAYFINDER, TrimPattern.RAISER, TrimPattern.HOST, TrimPattern.SHAPER,
            TrimPattern.SENTRY, TrimPattern.DUNE, TrimPattern.WILD, TrimPattern.COAST)
        return ArmorTrim(mats.random(), pats.random())
    }


}