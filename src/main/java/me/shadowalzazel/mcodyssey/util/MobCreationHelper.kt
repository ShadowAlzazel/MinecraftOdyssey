package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.enchantments.api.SlotColors
import me.shadowalzazel.mcodyssey.items.creators.ToolCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.trims.TrimMaterials
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

interface MobCreationHelper: AttributeManager, EnchantabilityHandler {

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

    fun createShinyMob(mob: LivingEntity, enchantedArmor: Boolean, materialType: ToolMaterial = ToolMaterial.SILVER) {
        if (mob is Creeper) return
        val shinyColor = SlotColors.SHINY.color
        // Difficulty
        val difficultyMod = getScaledDifficulty(mob)
        // Weapon
        val weaponList = listOf(
            ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.CUTLASS, ToolType.CLAYMORE, // 1 Hand
            ToolType.POLEAXE, ToolType.LONGAXE,
            ToolType.WARHAMMER,  ToolType.SCYTHE,
            ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM) // Double
        val weaponType = weaponList.random()

        val mainHand: ItemStack = when(mob.type) {
            EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED -> {
                mob.equipment?.itemInMainHand ?: ItemStack(Material.BOW)
            }
            EntityType.ZOMBIE, EntityType.HUSK -> {
                ToolCreator().createToolStack(materialType, weaponType)
            }
            else -> {
                ToolCreator().createToolStack(materialType, weaponType)
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
            updateEnchantabilityPointsLore()
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
            createTrimmedArmor(this, trim, materialType.itemName, enchantedArmor)
            equipment?.also {
                it.setItemInMainHand(weapon)
                it.itemInMainHandDropChance = 0.5F // Change to difficulty
                val dualWieldTypes = listOf(ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM)
                if (weaponType in dualWieldTypes) {
                    it.setItemInOffHand(weapon.clone())
                    it.itemInOffHandDropChance = 0.5F // Change to difficulty
                }
                it.helmetDropChance = 0.3F
                it.chestplateDropChance = 0.3F
                it.leggingsDropChance = 0.3F
                it.bootsDropChance = 0.3F
            }
            addHealthAttribute(45 + (20.0 * difficultyMod), AttributeTags.ELITE_HEALTH)
            health += 45 + (20.0 * difficultyMod)
            addAttackAttribute(3 + (1 * difficultyMod), AttributeTags.ELITE_ATTACK_DAMAGE)
            addArmorAttribute(4 + (2 * difficultyMod), AttributeTags.ELITE_ARMOR)
            addSpeedAttribute(0.012 + (0.012 * difficultyMod), AttributeTags.ELITE_SPEED)
            addScaleAttribute(0.2)
            addScoreboardTag(EntityTags.ELITE_MOB)
            addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 20 * 3600, 0))
            // Persistent
            if (mob.location.block.lightFromSky > 5) {
                isPersistent = true
            }
        }
        println("Spawned Elite Shiny Mob at: ${mob.location}")
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

    fun createTrimmedArmor(entity: LivingEntity, trim: ArmorTrim, type: String, randomEnchants: Boolean = false) {
        // Create armors from type
        val helmet = if (type == "diamond") {
            ItemStack(Material.DIAMOND_HELMET)
        } else {
            ItemStack(Material.IRON_HELMET)
        }
        val chestplate = if (type == "diamond") {
            ItemStack(Material.DIAMOND_CHESTPLATE)
        } else {
            ItemStack(Material.IRON_CHESTPLATE)
        }
        val leggings = if (type == "diamond") {
            ItemStack(Material.DIAMOND_LEGGINGS)
        } else {
            ItemStack(Material.IRON_LEGGINGS)
        }
        val boots = if (type == "diamond") {
            ItemStack(Material.DIAMOND_BOOTS)
        } else {
            ItemStack(Material.IRON_BOOTS)
        }
        val armorList = listOf(helmet, chestplate, leggings, boots)
        // Enchant Armors
        if (randomEnchants) {
            val randomSeed = Random()
            for (armor in armorList) {
                val copy = armor.enchantWithLevels(30, false, randomSeed)
                armor.itemMeta = copy.itemMeta
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