package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.common.enchantments.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.ToolMaker
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.trims.TrimMaterials
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Creeper
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.pow

interface MobMaker: AttributeManager, EnchantabilityHandler, ToolMaker {

    fun getScaledDifficulty(entity: Entity): Double {
        // Find the XY distance from zero
        val distanceFromZero = entity.location.clone().let {
            it.toBlockLocation()
            it.y = 0.0
            it.distance(it.clone().zero()).absoluteValue
        }
        // Formula
        val scaleDist = 1.0 / 10000.0
        return (scaleDist * distanceFromZero) + (scaleDist * distanceFromZero).pow(2)
    }

    fun createArmoredMob(
        mob: LivingEntity,
        newWeapon: Boolean = false,
        weaponMaterial: ToolMaterial = ToolMaterial.IRON,
        armorMaterial: String? = null,
        trim: ArmorTrim? = null,
        enchantedArmor: Boolean = false)
       {
        // Weapon
        val weaponList = listOf(
            ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.CUTLASS, ToolType.CLAYMORE, ToolType.RAPIER, // 1 Hand
            ToolType.POLEAXE, ToolType.LONGAXE, ToolType.GLAIVE,
            ToolType.WARHAMMER,  ToolType.SCYTHE, ToolType.SPEAR, ToolType.HALBERD,
            ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM) // Double
        val weaponType = weaponList.random()
        // Override with new weapon
        val mainHand: ItemStack = if (newWeapon) {
            createToolStack(weaponMaterial, weaponType)
        } else {
            mob.equipment?.itemInMainHand ?: createToolStack(weaponMaterial, weaponType)
        }
        val weapon = mainHand.enchantWithLevels(20, false, Random())
        mob.apply {
            val armorType = armorMaterial ?: weaponMaterial.namePre
            createTrimmedArmor(this, trim, armorType, enchantedArmor)
            equipment?.also {
                it.setItemInMainHand(weapon)
                it.itemInMainHandDropChance = 0.15F // Change to difficulty
                val dualWieldTypes = listOf(ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM)
                if (weaponType in dualWieldTypes) {
                    it.setItemInOffHand(weapon.clone())
                    it.itemInOffHandDropChance = 0.15F // Change to difficulty
                }
                it.helmetDropChance = 0.0F
                it.chestplateDropChance = 0.0F
                it.leggingsDropChance = 0.0F
                it.bootsDropChance = 0.0F
            }
        }
    }

    fun createShinyMob(
        mob: LivingEntity,
        enchantedArmor: Boolean,
        materialType: ToolMaterial = ToolMaterial.SILVER,
        newWeapon: Boolean = false) {
        if (mob is Creeper) return
        val shinyColor = CustomColors.SHINY.color
        // Difficulty
        val difficultyMod = getScaledDifficulty(mob)
        // Weapon
        val weaponList = listOf(
            ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.CUTLASS, ToolType.CLAYMORE, ToolType.RAPIER, // 1 Hand
            ToolType.POLEAXE, ToolType.LONGAXE, ToolType.GLAIVE,
            ToolType.WARHAMMER,  ToolType.SCYTHE, ToolType.SPEAR, ToolType.HALBERD,
            ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM) // Double
        val weaponType = weaponList.random()
        // Override with new weapon
        val mainHand: ItemStack = if (newWeapon) {
            createToolStack(materialType, weaponType)
        } else {
            when(mob.type) {
                EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED -> {
                    mob.equipment?.itemInMainHand ?: ItemStack(Material.BOW)
                }

                EntityType.ZOMBIE, EntityType.HUSK -> {
                    createToolStack(materialType, weaponType)
                }

                else -> {
                    createToolStack(materialType, weaponType)
                }
            }
        }
        // GET ENCHANTMENTS TO SWORD
        val greaterEnchant: Pair<Enchantment, Int>
        // Enchant randomly and get gilded
        val weapon = mainHand.enchantWithLevels(30, false, Random())
        // Prevent Empty Collection
        if (weapon.enchantments.isEmpty()) {
            return
        }
        weapon.apply {
            val newEnchant = this.enchantments.entries.random()
            val checkMax = if (newEnchant.key.maxLevel != 1) { newEnchant.value + 1 } else { 1 }
            greaterEnchant = Pair(newEnchant.key, checkMax)
            addShinyEnchant(greaterEnchant.first, greaterEnchant.second)
            val enchantment = OdysseyEnchantments.O_SHINY
            addEnchantment(enchantment, 1)
            updateEnchantabilityPoints()
        }
        // Naming
        val enchantName = greaterEnchant.first.displayName(greaterEnchant.second).color(shinyColor)
        val conjunctions = listOf("with", "of", "master of", "joined with", "imbued by", "keeper of", "holder of")
        val nameText = "${dangerPrefixes.random()} ${mob.name} ${conjunctions.random()} "
        //val nameText = "${dangerPrefixes.random()} ${mob.name}"
        val newName = Component.text(nameText).color(shinyColor).append(enchantName)
        // Apply to Mob
        val trim = createRandomArmorTrim()
        mob.apply {
            // Options
            customName(newName)
            isCustomNameVisible = true
            canPickupItems = true
            // Add Items
            createTrimmedArmor(this, trim, materialType.namePre, enchantedArmor)
            equipment?.also {
                it.setItemInMainHand(weapon)
                it.itemInMainHandDropChance = 0.35F // Change to difficulty
                val dualWieldTypes = listOf(ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM)
                if (weaponType in dualWieldTypes) {
                    it.setItemInOffHand(weapon.clone())
                    it.itemInOffHandDropChance = 0.15F // Change to difficulty
                }
                it.helmetDropChance = 0.075F
                it.chestplateDropChance = 0.075F
                it.leggingsDropChance = 0.075F
                it.bootsDropChance = 0.075F
            }
            addHealthAttribute(25.0 + (15.0 * difficultyMod), AttributeTags.ELITE_HEALTH)
            health += 25.0 + (15.0 * difficultyMod)
            addAttackAttribute(9 + (1.25 * difficultyMod), AttributeTags.ELITE_ATTACK_DAMAGE)
            addArmorAttribute(4 + (2 * difficultyMod), AttributeTags.ELITE_ARMOR)
            addSpeedAttribute(0.015 + (0.015 * difficultyMod), AttributeTags.ELITE_SPEED)
            addScaleAttribute(0.2)
            addStepAttribute(2.0, AttributeTags.ELITE_STEP_HEIGHT)
            addScoreboardTag(EntityTags.ELITE_MOB)
            addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 20 * 3600, 0))
            // Persistent
            if (mob.location.block.lightFromSky > 5) {
                isPersistent = true
            }
        }
        //println("Spawned Elite Shiny Mob at: ${mob.location}")
    }

    val dangerPrefixes: Set<String>
        get() = setOf(
            "Deadly",
            "Magnificent",
            "Terrorizing",
            "Classic",
            "Potent",
            "Dominant",
            "Forceful",
            "Mighty",
            "Great",
            "Cruel",
            "Dangerous",
            "Savage",
            "Lethal",
            "Fatal",
            "Whimsy",
            "Mythic",
            "Legendary",
            "Shiny",
            "Glistening",
            "Sparkling")

    fun createTrimmedArmor(entity: LivingEntity, trim: ArmorTrim?, type: String, enchantArmor: Boolean = false) {
        // Create armors from type
        val helmet = when (type) {
            "diamond" -> {
                ItemStack(Material.DIAMOND_HELMET)
            }
            "chainmail" -> {
                ItemStack(Material.CHAINMAIL_HELMET)
            }
            else -> {
                ItemStack(Material.IRON_HELMET)
            }
        }
        val chestplate = when (type) {
            "diamond" -> {
                ItemStack(Material.DIAMOND_CHESTPLATE)
            }
            "chainmail" -> {
                ItemStack(Material.CHAINMAIL_CHESTPLATE)
            }
            else -> {
                ItemStack(Material.IRON_CHESTPLATE)
            }
        }
        val leggings = when (type) {
            "diamond" -> {
                ItemStack(Material.DIAMOND_LEGGINGS)
            }
            "chainmail" -> {
                ItemStack(Material.CHAINMAIL_LEGGINGS)
            }
            else -> {
                ItemStack(Material.IRON_LEGGINGS)
            }
        }
        val boots = when (type) {
            "diamond" -> {
                ItemStack(Material.DIAMOND_BOOTS)
            }
            "chainmail" -> {
                ItemStack(Material.CHAINMAIL_BOOTS)
            }
            else -> {
                ItemStack(Material.IRON_BOOTS)
            }
        }
        val armorList = listOf(helmet, chestplate, leggings, boots)
        // Enchant Armors
        if (enchantArmor) {
            val randomSeed = Random()
            for (armor in armorList) {
                val copy = armor.enchantWithLevels(30, false, randomSeed)
                armor.itemMeta = copy.itemMeta
            }
        }
        // Trim
        if (trim != null) {
            for (armor in armorList) {
                val meta = armor.itemMeta as ArmorMeta
                meta.trim = trim
                armor.itemMeta = meta
            }
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