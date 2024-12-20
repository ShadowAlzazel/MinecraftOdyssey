package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.MobData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.absoluteValue
import kotlin.math.pow

@Suppress("UnstableApiUsage")
interface MobMaker : LootEquipmentCreator {


    fun eliteMobCreator(event: CreatureSpawnEvent) {
        val mob = event.entity
        if (mob is Creeper) return
        if (mob.scoreboardTags.contains(EntityTags.HANDLED)) return
        // Surface Spawns for now -> fix for mob farms
        //if (mob.location.block.lightFromSky < 1) return
        val inEdge = mob.location.world == Odyssey.instance.edge
        // random roll
        val roll = (0..1000).random()
        val difficulty = getScaledDifficulty(mob)

        // Roll Elite - Base 2% + 1.5% for zombie/skeleton
        val elitePreferenceBonus = if (mob is Zombie && mob !is PigZombie || mob is Skeleton) 15 else 0
        var eliteSpawnChance = 20 + elitePreferenceBonus + (difficulty * 10) // 1% per difficulty
        if (inEdge) eliteSpawnChance += 10
        val rolledElite = (eliteSpawnChance > roll)
        if (!rolledElite) return
        // 2% For A Pack of elites
        // 0.2% For a Shiny Mob

        // Handle Spawning
        var shinySpawnChance = 2 + (difficulty * 10) + elitePreferenceBonus
        if (inEdge) shinySpawnChance += 5
        val rolledShiny = (shinySpawnChance > roll) // Check if shiny

        // Handle Shiny
        if (rolledShiny) {
            // Get dimension materials
            val materials = mutableListOf(ToolMaterial.DIAMOND)
            if (inEdge) materials.add(ToolMaterial.MITHRIL)
            if (mob.location.world.key.key == "overworld") materials.add(ToolMaterial.IRIDIUM)
            if (mob.location.world.key.key == "the_nether") materials.add(ToolMaterial.NETHERITE)
            // Get Equipment Randomizer
            val equipmentRandomizer = EquipmentRandomizer(
                materials,
                MobData.ALL_WEAPONS,
                MobData.ALL_PARTS,
                toStringList(materials),
                MobData.ELITE_ARMOR_TRIM_MATS,
                MobData.SHINY_ARMOR_TRIM_PATTERNS)
            // Create
            createShinyMob(mob, equipmentRandomizer)
        }
        // Handle Elite
        else {
            // Equipment Randomizer
            val materials = listOf(ToolMaterial.SILVER, ToolMaterial.TITANIUM, ToolMaterial.ANODIZED_TITANIUM,
                ToolMaterial.COPPER, ToolMaterial.IRON, ToolMaterial.GOLDEN)
            val equipmentRandomizer = EquipmentRandomizer(
                materials,
                MobData.ALL_WEAPONS,
                MobData.ALL_PARTS,
                toStringList(materials) + "chainmail",
                MobData.ELITE_ARMOR_TRIM_MATS,
                MobData.ELITE_ARMOR_TRIM_PATTERNS)

            // Create Mob Pack Data
            val mobs = mutableListOf(mob)
            val name = mob.name
            val prefix = MobData.DANGER_PREFIXES.random()
            val randomMax = (1..(difficulty * 1.25).toInt() + 2).random()

            // Create Pack based on amount
            repeat(randomMax) {
                val newMob = mob.world.spawnEntity(mob.location, mob.type, CreatureSpawnEvent.SpawnReason.CUSTOM) as LivingEntity
                mobs.add(newMob)
            }
            mobs.forEach {
                createRandomizedMob(it, equipmentRandomizer, enchanted=true, newWeapon=true)
                customizeName(it, "$prefix $name", CustomColors.CURSED.color)
            }
        }

    }

    private fun toStringList(matList: List<ToolMaterial>): List<String> {
        return matList.map { it.namePre }
    }

    /*-----------------------------------------------------------------------------------------------*/

    // Naming Mob
    private fun customizeName(mob: LivingEntity, name: String, color: TextColor) {
        val newName = Component.text(name).color(color)
        mob.customName(newName)
        mob.isCustomNameVisible = true
    }

    fun createRandomizedMob(
        mob: LivingEntity,
        lootEquipmentHolder: EquipmentRandomizer,
        enchanted: Boolean = false,
        newWeapon: Boolean = false)
    {
        // Difficulty
        val difficulty = getScaledDifficulty(mob)

        // Methods to create customized equipment
        val mainHand: ItemStack = if (newWeapon) {
            lootEquipmentHolder.newWeapon()
        } else {
            mob.equipment?.itemInMainHand ?: lootEquipmentHolder.newWeapon()
        }
        val armorList = lootEquipmentHolder.newTrimmedArmor()
        val dualWielding = lootEquipmentHolder.toolType in listOf(ToolType.DAGGER, ToolType.CHAKRAM, ToolType.SICKLE, ToolType.CUTLASS)

        // Enchant
        if (enchanted) {
            if (!mainHand.hasData(DataComponentTypes.ENCHANTMENTS)) enchantItemsRandomly(listOf(mainHand), 10 + difficulty.toInt())
            enchantItemsRandomly(armorList, 10 + difficulty.toInt())
        }

        // Apply to Mob
        mob.apply {
            canPickupItems = true
            isPersistent = false
            addScoreboardTag(EntityTags.HANDLED)
            // Add Items
            setArmor(armorList)
            equipment?.also {
                it.setItemInMainHand(mainHand)
                it.itemInMainHandDropChance = (0.15F * (difficulty)).toFloat()  + 0.025F
                if (dualWielding) {
                    it.setItemInOffHand(mainHand.clone())
                    it.itemInOffHandDropChance = (0.05F * (difficulty)).toFloat()
                }
            }
            // Stats
            setEliteAttributes(difficulty, 0.25)
            setArmorDropChances(0.075F)
        }
    }

    fun createShinyMob(
        mob: LivingEntity,
        equipmentRandomizer: EquipmentRandomizer,
        newWeapon: Boolean = true)
    {
        if (mob is Creeper) return
        val shinyColor = CustomColors.SHINY.color
        // Difficulty
        val difficulty = getScaledDifficulty(mob)

        // Methods to create customized equipment
        val mainHand: ItemStack = if (newWeapon) {
            equipmentRandomizer.newWeapon()
        } else {
            mob.equipment?.itemInMainHand ?: equipmentRandomizer.newWeapon()
        }
        val armorList = equipmentRandomizer.newTrimmedArmor()
        val dualWielding = equipmentRandomizer.toolType in listOf(ToolType.DAGGER, ToolType.CHAKRAM, ToolType.SICKLE, ToolType.CUTLASS)


        // Create shiny enchant and enchant main weapon
        val shinyEnchant = OdysseyEnchantments.meleeSet.random()
        val checkedMax = if (shinyEnchant.maxLevel != 1) { shinyEnchant.maxLevel + 1 } else { 1 }
        val enchantTagSet = getTagFromRegistry(RegistryKey.ENCHANTMENT, "in_table/melee")
        enchantItemsWithTagSet(listOf(mainHand), enchantTagSet, 20 + difficulty.toInt())
        mainHand.apply {
            addShinyEnchant(shinyEnchant, checkedMax)
            addEnchantment(OdysseyEnchantments.O_SHINY, 1)
            updateEnchantabilityPoints()
        }
        enchantItemsRandomly(armorList, 25 + difficulty.toInt())

        // Naming Mob
        val enchantName = shinyEnchant.displayName(checkedMax).color(shinyColor)
        val eliteName = MobData.newName(mob.name)
        // Create new name component
        val newName = Component.text(eliteName).color(shinyColor).append(enchantName)

        // Apply to Mob
        mob.apply {
            // Options
            isPersistent = false
            customName(newName)
            addScoreboardTag(EntityTags.HANDLED)
            isCustomNameVisible = true
            canPickupItems = true
            // Add Items
            setArmor(armorList)
            equipment?.also {
                it.setItemInMainHand(mainHand)
                it.itemInMainHandDropChance = (0.15F * (difficulty)).toFloat() + 0.05F
                if (dualWielding) {
                    it.setItemInOffHand(mainHand.clone())
                    it.itemInOffHandDropChance = (0.05F * (difficulty)).toFloat()
                }
            }
            // Stats
            addScaleAttribute(0.2)
            setEliteAttributes(difficulty)
            setArmorDropChances(0.085F)
            // Persistent
            /*
            if (mob.location.block.lightFromSky > 5) {
                isPersistent = true
            }

             */
        }
    }


    private fun LivingEntity.setEliteAttributes(difficulty: Double = 1.0, modifier: Double = 1.0) {
        val value = difficulty * 1.0
        addHealthAttribute((20.0 + (10.0 * value)) * modifier, AttributeTags.ELITE_HEALTH)
        health += (20.0 + (10.0 * value)) * modifier
        addAttackAttribute((4 + (1.0 * value)) * modifier, AttributeTags.ELITE_ATTACK_DAMAGE)
        addArmorAttribute((2 + (1 * value)) * modifier, AttributeTags.ELITE_ARMOR)
        addSpeedAttribute((0.015 + (0.015 * value)) * modifier, AttributeTags.ELITE_SPEED)
        addStepAttribute(2.0, AttributeTags.ELITE_STEP_HEIGHT)
        addScoreboardTag(EntityTags.ELITE_MOB)
    }


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

}