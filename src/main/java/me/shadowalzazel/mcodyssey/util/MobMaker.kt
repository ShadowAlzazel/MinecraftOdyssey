package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemArmorTrim
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.constants.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import java.util.Random
import kotlin.math.absoluteValue

@Suppress("UnstableApiUsage")
interface MobMaker : EquipmentGenerator {

    fun getDistanceDifficulty(entity: Entity): Double {
        val distanceScale = 3000.0 // TODO: Get from plugin yml
        // Find the XZ distance from zero
        val distanceFromZero = entity.location.clone().let {
            it.toBlockLocation()
            it.y = 0.0
            it.distance(it.clone().zero()).absoluteValue
        }
        // Every 3000 blocks add 1 to the difficulty. Starting from 6000
        return (distanceFromZero - (distanceScale / 2)).coerceAtLeast(0.0) / distanceScale
    }


    fun mobEliteHandler(event: CreatureSpawnEvent) {
        val mob = event.entity
        if (mob is Creeper) return
        if (mob.scoreboardTags.contains(EntityTags.SPAWN_HANDLED)) return
        // Surface Spawns for now -> fix for mob farms
        //if (mob.location.block.lightFromSky < 1) return
        val inEdge = mob.location.world == Odyssey.instance.edge
        // random roll
        val roll = (0..1000).random()
        val difficulty = getDistanceDifficulty(mob)

        // Roll Elite - Base 1% + 1.5% for zombie/skeleton
        val elitePreferenceBonus = if (mob is Zombie && mob !is PigZombie || mob is Skeleton) 15 else 0
        var eliteSpawnChance = 10 + elitePreferenceBonus + (difficulty * 0.5) // 0.5% per difficulty
        if (inEdge) eliteSpawnChance += 10 // Bonus 1% if in Edge
        val rolledElite = (eliteSpawnChance > roll)
        if (!rolledElite) return

        // Each elite has a 20% to be a Shiny + 5% if in edge
        val shinyRoll = (0..100).random()
        var shinySpawnChance = 20
        if (inEdge) shinySpawnChance += 5
        val rolledShiny = (shinySpawnChance > shinyRoll) // Check if shiny

        // Handle Shiny
        if (rolledShiny) {
            // Get dimension materials
            val materials = mutableListOf(ToolMaterial.DIAMOND)
            if (inEdge) materials.add(ToolMaterial.MITHRIL)
            if (mob.location.world.key.key == "overworld") materials.add(ToolMaterial.IRIDIUM)
            if (mob.location.world.key.key == "the_nether") materials.add(ToolMaterial.NETHERITE)
            // Make Equipment Builder
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                materials,
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                toStringList(materials),
                EliteMobsData.ELITE_ARMOR_TRIM_MATS,
                EliteMobsData.SHINY_ARMOR_TRIM_PATTERNS)
            // Create
            createShinyMob(mob, equipmentRandomBuilder, true)
        }
        // Handle Elite
        else {
            // Get materials, then add per dimension
            val materials = mutableListOf(ToolMaterial.SILVER, ToolMaterial.TITANIUM, ToolMaterial.ANODIZED_TITANIUM, ToolMaterial.IRON)
            if (mob.location.world.key.key == "overworld") materials.add(ToolMaterial.COPPER)
            if (mob.location.world.key.key == "the_nether") materials.add(ToolMaterial.GOLDEN)
            if (mob.location.world.key.key == "edge") materials.add(ToolMaterial.CRYSTAL_ALLOY)

            // Equipment Randomizer
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                materials,
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                toStringList(materials) + "chainmail",
                EliteMobsData.ELITE_ARMOR_TRIM_MATS,
                EliteMobsData.ELITE_ARMOR_TRIM_PATTERNS)

            // Create Mob Pack Data
            val mobs = mutableListOf(mob)
            val name = mob.name
            val prefix = EliteMobsData.DANGER_PREFIXES.random()
            // Random pack size 0.5 * difficulty
            val packSize = (1..(difficulty * 0.5).toInt() + 2).random()

            // Create Pack based on amount
            repeat(packSize) {
                val newMob = mob.world.spawnEntity(
                    mob.location,
                    mob.type,
                    CreatureSpawnEvent.SpawnReason.CUSTOM) as LivingEntity
                mobs.add(newMob)
            }
            // For Each mob create armor
            mobs.forEach {
                createArmoredMob(it, equipmentRandomBuilder, enchantWeapon = true, replaceOldWeapon = true)
                customizeName(it, "$prefix $name", CustomColors.BLUE.color)
                it.heal(30.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            }
        }

    }


    /*-----------------------------------------------------------------------------------------------*/

    // Naming Mob
    private fun customizeName(mob: LivingEntity, name: String, color: TextColor) {
        val newName = Component.text(name).color(color)
        mob.customName(newName)
        mob.isCustomNameVisible = true
    }

    fun createArmoredMob(
        mob: LivingEntity,
        equipmentRandomBuilder: EquipmentRandomBuilder,
        enchantWeapon: Boolean,
        replaceOldWeapon: Boolean)
    {
        // Difficulty 1 per 3000 blocks
        val difficulty = getDistanceDifficulty(mob)

        // Methods to create customized equipment
        val mainHand: ItemStack = if (replaceOldWeapon) {
            equipmentRandomBuilder.newWeapon()
        } else {
            mob.equipment?.itemInMainHand ?: equipmentRandomBuilder.newWeapon()
        }
        val armorList = equipmentRandomBuilder.newTrimmedArmor()
        val validDualWieldWeapons = equipmentRandomBuilder.toolType in listOf(
            ToolType.DAGGER, ToolType.CHAKRAM, ToolType.SICKLE, ToolType.CUTLASS)

        // Enchant weapon
        if (enchantWeapon) {
            if (!mainHand.hasData(DataComponentTypes.ENCHANTMENTS)) enchantItemsRandomly(listOf(mainHand), 10 + difficulty.toInt())
            enchantItemsRandomly(armorList, 10 + difficulty.toInt())
        }
        mainHand.setIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS, (1..5).random())

        // Apply to Mob
        mob.apply {
            canPickupItems = true
            isPersistent = false
            addScoreboardTag(EntityTags.SPAWN_HANDLED)
            // Add Items and Equipment
            setNewArmor(armorList)
            equipment?.also {
                it.setItemInMainHand(mainHand)
                it.itemInMainHandDropChance = 0.05F + (difficulty * 0.01F).toFloat()
                if (validDualWieldWeapons) {
                    it.setItemInOffHand(mainHand.clone())
                    it.itemInOffHandDropChance = 0.05F + (difficulty * 0.01F).toFloat()
                }
            }
            setArmorDropChances(0.05F)
            // Add Attributes and Stats
            val modifier = 1.0 * difficulty

            // 10 + 5 * difficulty
            setHealthAttribute(10.0 + (5.0 * modifier), AttributeTags.ELITE_HEALTH)
            // 1.0 + 1 * difficulty
            addAttackAttribute(1.0 + (modifier), AttributeTags.ELITE_ATTACK_DAMAGE)
            addArmorAttribute(2.0, AttributeTags.ELITE_ARMOR)
            addSpeedAttribute(0.01, AttributeTags.ELITE_SPEED)
        }
    }

    fun createShinyMob(
        mob: LivingEntity,
        equipmentRandomBuilder: EquipmentRandomBuilder,
        replaceOldWeapon: Boolean)
    {
        if (mob is Creeper) return // Always ignore creepers
        val shinyColor = CustomColors.SHINY.color
        // Difficulty 1 per 3000 blocks
        val difficulty = getDistanceDifficulty(mob)

        // Methods to create customized equipment
        val mainHand: ItemStack = if (replaceOldWeapon) {
            equipmentRandomBuilder.newWeapon()
        } else {
            mob.equipment?.itemInMainHand ?: equipmentRandomBuilder.newWeapon()
        }
        val armorList = equipmentRandomBuilder.newTrimmedArmor()
        val validDualWieldWeapons = equipmentRandomBuilder.toolType in listOf(
            ToolType.DAGGER, ToolType.CHAKRAM, ToolType.SICKLE, ToolType.CUTLASS)

        // Create shiny enchant and enchant main weapon
        val shinyEnchant = getCollectionFromKey(
            RegistryKey.ENCHANTMENT,
            "can_be_shiny",
            "odyssey").random()


        val shinyMax = if (shinyEnchant.maxLevel != 1) { shinyEnchant.maxLevel + 1 } else { 1 }

        // A list of enchantments inside a data tag
        val bonusEnchantTagSet = getTagFromRegistry(
            RegistryKey.ENCHANTMENT,
            "can_be_shiny",
            "odyssey")

        // Enchant Item With Tag List
        enchantItemsWithTagSet(listOf(mainHand), bonusEnchantTagSet, 20 + difficulty.toInt())
        mainHand.apply {
            addShinyEnchant(shinyEnchant, shinyMax)
            addEnchantment(OdysseyEnchantments.O_SHINY, 1)
            setIntTag(ItemDataTags.EXTRA_ENCHANTABILITY_POINTS, (2..5).random())
            updateToolTip()
        }
        // Enchant all armor
        enchantItemsRandomly(armorList, 20 + difficulty.toInt())

        // Naming Mob
        val enchantName = shinyEnchant.displayName(shinyMax).color(shinyColor)
        val dangerPrefixName = EliteMobsData.newName(mob.name)
        // Create new name component
        val mobShinyName = Component.text(dangerPrefixName).color(shinyColor).append(enchantName)

        // Apply to Mob
        mob.apply {
            // Options
            isPersistent = false
            customName(mobShinyName)
            addScoreboardTag(EntityTags.SPAWN_HANDLED)
            isCustomNameVisible = true
            canPickupItems = true
            // Add Items
            setNewArmor(armorList)
            equipment?.also {
                it.setItemInMainHand(mainHand)
                it.itemInMainHandDropChance = 0.03F + (difficulty * 0.01F).toFloat()
                if (validDualWieldWeapons) {
                    it.setItemInOffHand(mainHand.clone())
                    it.itemInOffHandDropChance = 0.02F + (difficulty * 0.01F).toFloat()
                }
            }
            setArmorDropChances(0.025F)
            // Get Stat values
            val modifier = 1.0 * difficulty

            // Stats and Attributes
            addScaleAttribute(0.2)
            // 20 + 10 * difficulty
            setHealthAttribute(20.0 + (10.0 * modifier), AttributeTags.ELITE_HEALTH)
            // 3.0 + 1 * difficulty
            addAttackAttribute(3.0 + (modifier), AttributeTags.ELITE_ATTACK_DAMAGE)
            addArmorAttribute(4.0, AttributeTags.ELITE_ARMOR)
            addSpeedAttribute(0.015, AttributeTags.ELITE_SPEED)

            // Set tags
            addScoreboardTag(EntityTags.ELITE_MOB)
            // set new health
            health += 20.0 + (10.0 * modifier)
        }
    }

    fun trimMobArmor(entity: LivingEntity, armorTrim: ArmorTrim) {
        val equipment = entity.equipment ?: return
        // Get Armor and add Trim
        val armor = getArmorItems(entity) ?: return
        armor.forEach { it.addDataTrim(armorTrim) }
        // Set new equipment
        entity.setNewArmor(armor)
    }


    fun enchantMobWornArmorRandomly(entity: LivingEntity, levelRange: IntRange) {
        val equipment = entity.equipment ?: return
        // Random levels
        val random = Random(Odyssey.instance.seed) //WORLD SEED
        // Get Armor and enchant
        val armor = getArmorItems(entity) ?: return
        for (x in 0..3) {
            // Enchant with levels returns a copy DOES NOT change original
            val item = armor[x]
            if (item.isEmpty) continue
            if (item.type == Material.AIR) continue
            // Ignore if already enchanted
            val hasEnchants = item.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()?.isNotEmpty() == true
            if (hasEnchants) continue
            // Enchant and copy
            val enchanted = item.enchantWithLevels(levelRange.random(), false, random)
            armor[x] = enchanted
        }
        entity.setNewArmor(armor)
    }

    /*
    * Copies the equipment and equipment chances from one mob to another
    */
    fun copyAndSetEquipment(entity: LivingEntity, provider: LivingEntity) {
        val providerEquipment = provider.equipment ?: return
        entity.equipment?.apply {
            armorContents = providerEquipment.armorContents
            setItemInMainHand(providerEquipment.itemInMainHand)
            setItemInOffHand(providerEquipment.itemInOffHand)
            // Copy Chances
            helmetDropChance = providerEquipment.helmetDropChance
            chestplateDropChance = providerEquipment.chestplateDropChance
            leggingsDropChance = providerEquipment.leggingsDropChance
            bootsDropChance = providerEquipment.bootsDropChance
            itemInMainHandDropChance = providerEquipment.itemInMainHandDropChance
            itemInOffHandDropChance = providerEquipment.itemInOffHandDropChance
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────

    private fun ItemStack.addDataTrim(armorTrim: ArmorTrim) {
        if (this.type != Material.AIR) {
            val newArmorTrim = ItemArmorTrim.itemArmorTrim(armorTrim)
            this.setData(DataComponentTypes.TRIM, newArmorTrim)
        }
    }


    private fun LivingEntity.setArmorDropChances(chance: Float) {
        this.equipment?.also {
            it.helmetDropChance = chance
            it.chestplateDropChance = chance
            it.leggingsDropChance = chance
            it.bootsDropChance = chance
        }
    }

    private fun LivingEntity.setNewArmor(list: List<ItemStack>) {
        // Apply
        this.equipment?.also {
            it.setHelmet(list[0])
            it.setChestplate(list[1])
            it.setLeggings(list[2])
            it.setBoots(list[3])
        }
    }

    private fun getArmorItems(entity: LivingEntity): MutableList<ItemStack>? {
        val equipment = entity.equipment ?: return null
        val armorContents = mutableListOf(
            equipment.helmet,
            equipment.chestplate,
            equipment.leggings,
            equipment.boots,
        )
        return armorContents
    }


    private fun toStringList(matList: List<ToolMaterial>): List<String> {
        return matList.map { it.nameId }
    }

}