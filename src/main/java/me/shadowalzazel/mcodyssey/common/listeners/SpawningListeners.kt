package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.trims.TrimMaterials
import me.shadowalzazel.mcodyssey.common.trims.TrimPatterns
import me.shadowalzazel.mcodyssey.util.EquipmentRandomBuilder
import me.shadowalzazel.mcodyssey.util.MobMaker
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.ItemToolTipManager
import me.shadowalzazel.mcodyssey.util.StructureHelper
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EliteMobsData
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.loot.Lootable
import java.util.*

@Suppress("UnstableApiUsage")
object SpawningListeners : Listener, MobMaker, StructureHelper, RegistryTagManager, ItemToolTipManager {

    @EventHandler(priority = EventPriority.LOW)
    fun mobNaturalSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) return
        if (event.entity !is Enemy) return
        if (event.entity is Guardian) return
        val mob = event.entity
        if (mob.scoreboardTags.contains(EntityTags.SPAWN_HANDLED)) return // Do not handle twice

        // Handle natural spawning inside Structures
        mobNaturalSpawnInStructure(event)

        // Handle Creating Elites
        spawnEliteMob(event)

        // Handle edge spawns
        val inEdge = mob.location.world == Odyssey.instance.edge
        if (inEdge) {
            mob.addAttackAttribute(3.0, AttributeTags.MOB_EDGE_ATTACK_BONUS)
            mob.setHealthAttribute(15.0, AttributeTags.MOB_EDGE_HEALTH_BONUS)
            mob.heal(15.0)
        }
    }

    // Handle structure spawns
    @EventHandler(priority = EventPriority.LOWEST)
    fun mobStructureSpawningHandler(event: CreatureSpawnEvent) {
        //println(event.spawnReason) // -> DEFAULT
        val mob = event.entity
        if (mob.scoreboardTags.contains(EntityTags.SPAWN_HANDLED)) return // Do not handle twice
        // Get from registry
        val boundedStructures = getBoundedStructures(mob) ?: return
        val structureRegistry = getPaperRegistry(RegistryKey.STRUCTURE)
        // Check structures
        for (structure in boundedStructures) {
            when (val name = structureRegistry.getKey(structure)?.key) {
                "shadow_chambers" -> {
                    shadowChamberSpawnerSpawning(event)
                }
                "terminal_grid" -> {
                    terminalGridSpawnerSpawning(event)
                }
                "hypercubic_chamber" -> {
                    hypercubicChamberSpawnerSpawning(event)
                }
                "sunken_library" -> {
                    sunkenLibrarySpawnerSpawning(event)
                }
                "line_mine" -> {
                    //shadowChamberSpawnerSpawning(event)
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun mobStructureInitialPopulateHandler(event: ChunkPopulateEvent) {
        // Skip non structure  chunk
        if (event.chunk.structures.isEmpty()) return
        // Get all structures
        val structures = event.chunk.structures
        if (structures.isEmpty()) return
        val structureRegistry = getPaperRegistry(RegistryKey.STRUCTURE)

        // Get mobs
        val mobs = event.chunk.entities.filterIsInstance<LivingEntity>()

        event.chunk.structures.forEach { s ->
            val name = structureRegistry.getKey(s.structure)?.key ?: return@forEach
            val entitiesInStructure = mobs.filter { entityInsideStructure(it, s.structure) }

            when (name) {
                "forbidden_castle" -> entitiesInStructure.filterIsInstance<PiglinBrute>().forEach {
                    if (!it.scoreboardTags.contains(EntityTags.CLONED)) {
                        clonePiglinBrute(it)
                        // 30% chance to spawn a third knight
                        if ((0..10).random() > 3 && it.scoreboardTags.contains("in.knight")) {
                            clonePiglinBrute(it)
                        }
                    }
                }
                "sanctum" -> entitiesInStructure.filterIsInstance<Illager>().forEach {
                    //scoreboardTags.contains("in.sanctum")
                    if (!it.scoreboardTags.contains(EntityTags.CLONED)) {
                        cloneIllagerSanctum(it)
                        // 30% chance to spawn a third vindicator
                        if ((0..10).random() > 3 && it is Vindicator) {
                            cloneIllagerSanctum(it)
                        }
                    }
                }
            }

        }
        // End structure populate
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun mobNaturalSpawnInStructure(event: CreatureSpawnEvent) {
        val mob = event.entity
        // Get from registry
        val boundedStructures = getBoundedStructures(mob) ?: return
        val structureRegistry = getPaperRegistry(RegistryKey.STRUCTURE)
        // Check structures
        for (structure in boundedStructures) {
            when (val name = structureRegistry.getKey(structure)?.key) {
                "mineshaft", "mineshaft_mesa" -> {
                    mineshaftSpawning(event)
                }
                "supershaft" -> {
                    supershaftSpawning(event)
                }
                "line_mine" -> {
                    lineMineSpawning(event)
                }
            }
        }
    }

    private fun mineshaftSpawning(event: CreatureSpawnEvent) {
        val mob = event.entity
        mob.addScoreboardTag(EntityTags.IN_MINESHAFT)
        if (mob is Lootable) {
            val roll = (0..100).random()
            if (75 > roll) { // 75% chance to change LootTable of mobs in mineshaft
                mob.lootTable = LootTableManager.getResourceLootTable("structure_spawns/mineshaft")
            }
        }
    }

    private fun supershaftSpawning(event: CreatureSpawnEvent) {
        val mob = event.entity
        mob.addScoreboardTag(EntityTags.IN_SUPERSHAFT)
        if (mob is Lootable) {
            val roll = (0..100).random()
            if (75 > roll) { // 75% chance to change LootTable of mobs in supershaft
                mob.lootTable = LootTableManager.getResourceLootTable("structure_spawns/supershaft")
            }
        }
    }

    private fun lineMineSpawning(event: CreatureSpawnEvent) {
        val mob = event.entity
        mob.addScoreboardTag(EntityTags.IN_LINE_MINE)
        if (mob is Lootable) {
            val roll = (0..100).random()
            if (75 > roll) { // 75% chance to change LootTable of mobs in supershaft
                mob.lootTable = LootTableManager.getResourceLootTable("structure_spawns/line_mine")
            }
        }
    }

    private fun shadowChamberSpawnerSpawning(event: CreatureSpawnEvent) {
        // Handle mob customization
        val mob = event.entity
        // Create shiny if trial elite
        if (mob.scoreboardTags.contains("odyssey.trial_elite")) {
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                listOf(ToolMaterial.MITHRIL),
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                null,
                EliteMobsData.ELITE_ARMOR_TRIM_MATS,
                EliteMobsData.SHINY_ARMOR_TRIM_PATTERNS)
            createShinyMob(mob, equipmentRandomBuilder, true)
            // Bonus Stats
            mob.addAttackAttribute(4.0, AttributeTags.MOB_ATTACK_DAMAGE)
        }

        // Giant Spider,
        if (mob.scoreboardTags.contains("odyssey.giant")) {
            mob.apply {
                setHealthAttribute(42.0, AttributeTags.MOB_HEALTH)
                heal(42.0)
                addReachAttribute(0.5,  AttributeTags.MOB_REACH)
                addAttackAttribute(10.0, AttributeTags.MOB_ATTACK_DAMAGE)
                addScaleAttribute(0.5, AttributeTags.MOB_SCALE)
            }
        }
        // Vanguard
        if (mob.scoreboardTags.contains("odyssey.vanguard")) {
            // Weapon
            val weapon = createToolStack(ToolMaterial.IRON, ToolType.POLEAXE)
            // Add Enchantments
            val enchantItem = ItemStack(Material.NETHERITE_SWORD).enchantWithLevels(30, false, Random())
            val swordEnchants = enchantItem.enchantments
            weapon.apply {
                addUnsafeEnchantment(Enchantment.BREACH, 4)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                for (enchant in swordEnchants) {
                    if (enchant.key.canEnchantItem(weapon)) {
                        addEnchantment(enchant.key, enchant.value)
                    }
                }
                updateToolTip()
            }
            val offHand = ItemStack(Material.SHIELD)

            // Get Equipment Randomizer
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                listOf(ToolMaterial.IRON),
                listOf(ToolType.GLAIVE),
                listOf("imperial", "fancy", "voyager"),
                null,
                listOf(TrimMaterial.GOLD),
                listOf(TrimPatterns.VOYAGER))

            // Apply Stats and weapon
            mob.apply {
                // Stats
                setHealthAttribute(15.0, AttributeTags.MOB_HEALTH)
                heal(15.0)
                addAttackAttribute(4.0, AttributeTags.MOB_ATTACK_DAMAGE)
                addScaleAttribute(0.1, AttributeTags.MOB_SCALE)
                // Equipment
                equipment?.also {
                    it.setItemInOffHand(offHand)
                    it.setItemInMainHand(weapon)
                    it.itemInMainHandDropChance = 0.05F // Change to difficulty
                }
                // Do Custom mob
                createArmoredMob(this, equipmentRandomBuilder, enchantWeapon = true, replaceOldWeapon = false)
            }
        }
        // Basic Mob
        val basicMob = mob.scoreboardTags.isEmpty()
        if (basicMob) {
            // Get Equipment Randomizer
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                listOf(ToolMaterial.DIAMOND),
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                listOf("chainmail"),
                listOf(TrimMaterials.OBSIDIAN),
                EliteMobsData.SHADOW_MOB_TRIM_PATTERNS)

            // Creator
            createArmoredMob(mob, equipmentRandomBuilder, enchantWeapon = true, replaceOldWeapon = true)
            // Stats
            mob.addAttackAttribute(4.0, AttributeTags.MOB_ATTACK_DAMAGE)
            mob.setHealthAttribute(10.0, AttributeTags.MOB_HEALTH)
            mob.addArmorAttribute(2.0, AttributeTags.MOB_ARMOR)
            mob.heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
        }
        // All Shadow Chamber Mobs
        mob.apply {
            // To stop handlers
            addScoreboardTag(EntityTags.SPAWN_HANDLED)
            addScoreboardTag(EntityTags.SHADOW_MOB)
            // Stats
            setHealthAttribute(10.0, AttributeTags.SHADOW_CHAMBERS_HEALTH_BONUS)
            heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            addAttackAttribute(2.0, AttributeTags.SHADOW_CHAMBERS_ATTACK_BONUS)
            addArmorAttribute(2.0, AttributeTags.SHADOW_CHAMBERS_ARMOR_BONUS)
            addSpeedAttribute(0.0325, AttributeTags.SHADOW_CHAMBERS_SPEED_BONUS)
            // Special Attributes
            addStepAttribute(0.5, AttributeTags.SHADOW_CHAMBERS_STEP_HEIGHT)
        }
        // Finish
        return
    }

    private fun terminalGridSpawnerSpawning(event: CreatureSpawnEvent) {
        // Handle mob customization
        val mob = event.entity
        if (mob is Creaking) {
            mob.setHealthAttribute(20.0, AttributeTags.MOB_HEALTH)
            mob.addSpeedAttribute(0.05, AttributeTags.MOB_MOVEMENT_SPEED)
            mob.heal(20.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            mob.addAttackAttribute(8.0, AttributeTags.MOB_ATTACK_DAMAGE)
        } else {
            // Get Equipment Randomizer
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                listOf(ToolMaterial.COPPER),
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                listOf("copper"),
                listOf(TrimMaterial.RESIN),
                listOf(TrimPatterns.VOYAGER))

            // Creator
            createArmoredMob(mob, equipmentRandomBuilder, enchantWeapon = true, replaceOldWeapon = true)
            // Stats
            mob.addAttackAttribute(1.0, AttributeTags.MOB_ATTACK_DAMAGE)
            mob.setHealthAttribute(10.0, AttributeTags.MOB_HEALTH)
            mob.heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
        }
        // All Mobs
        mob.apply {
            // handlers
            addScoreboardTag(EntityTags.SPAWN_HANDLED)
            // Stats
            setHealthAttribute(10.0, AttributeTags.TERMINAL_GRID_HEALTH_BONUS)
            heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            addAttackAttribute(1.0, AttributeTags.TERMINAL_GRID_ATTACK_BONUS)
            addSpeedAttribute(0.01, AttributeTags.TERMINAL_GRID_SPEED_BONUS)
            addStepAttribute(0.5, AttributeTags.TERMINAL_GRID_STEP_HEIGHT)
        }
        // Finish
        return
    }

    private fun hypercubicChamberSpawnerSpawning(event: CreatureSpawnEvent) {
        // Handle mob customization
        val mob = event.entity
        // Guard Skeletons and Wither Skeletons
        if (mob is Skeleton) {
            // Get Equipment Randomizer
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                listOf(ToolMaterial.IRON),
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                listOf("iron"),
                listOf(TrimMaterials.NEPTUNIAN, TrimMaterials.JOVIANITE),
                listOf(TrimPatterns.VOYAGER, TrimPattern.BOLT, TrimPattern.RAISER))

            // Creator
            createArmoredMob(mob, equipmentRandomBuilder, enchantWeapon = true, replaceOldWeapon = true)
            // Stats
            mob.addAttackAttribute(1.0, AttributeTags.MOB_ATTACK_DAMAGE)
            mob.setHealthAttribute(10.0, AttributeTags.MOB_HEALTH)
            mob.heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
        }
        // All Mobs
        mob.apply {
            // handlers
            addScoreboardTag(EntityTags.SPAWN_HANDLED)
            // Stats
            setHealthAttribute(10.0, AttributeTags.HYPERCUBIC_CHAMBER_HEALTH_BONUS)
            heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            addAttackAttribute(2.0, AttributeTags.HYPERCUBIC_CHAMBER_ATTACK_BONUS)
            addSpeedAttribute(0.02, AttributeTags.HYPERCUBIC_CHAMBER_SPEED_BONUS)
            addStepAttribute(0.5, AttributeTags.HYPERCUBIC_CHAMBER_STEP_HEIGHT)
        }
        // Finish
        return
    }

    private fun sunkenLibrarySpawnerSpawning(event: CreatureSpawnEvent) {
        // Handle mob customization
        val mob = event.entity
        if (mob is Skeleton || mob is WitherSkeleton) {
            // Get Equipment Randomizer
            val equipmentRandomBuilder = EquipmentRandomBuilder(
                listOf(ToolMaterial.SILVER, ToolMaterial.IRON),
                EliteMobsData.ALL_WEAPONS,
                EliteMobsData.ALL_PARTS,
                listOf("silver", "iron"),
                listOf(TrimMaterial.DIAMOND),
                listOf(TrimPatterns.VOYAGER, TrimPattern.BOLT, TrimPattern.RAISER))

            // Creator
            createArmoredMob(mob, equipmentRandomBuilder, enchantWeapon = true, replaceOldWeapon = true)
            // Stats
            mob.addAttackAttribute(2.0, AttributeTags.MOB_ATTACK_DAMAGE)
            mob.setHealthAttribute(10.0, AttributeTags.MOB_HEALTH)
            mob.addArmorAttribute(2.0, AttributeTags.MOB_ARMOR)
            mob.heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
        }
        // Creaking
        if (mob is Creaking) {
            mob.setHealthAttribute(40.0, AttributeTags.MOB_HEALTH)
            mob.heal(40.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            mob.addAttackAttribute(8.0, AttributeTags.MOB_ATTACK_DAMAGE)
        }
        // Spider
        if (mob.scoreboardTags.contains("odyssey.giant") && mob is Spider) {
            mob.apply {
                setHealthAttribute(40.0, AttributeTags.MOB_HEALTH)
                heal(40.0)
                addAttackAttribute(10.0, AttributeTags.MOB_ATTACK_DAMAGE)
                addScaleAttribute(0.7, AttributeTags.MOB_SCALE)
            }
        }

        // All Mobs
        mob.apply {
            // handlers
            addScoreboardTag(EntityTags.SPAWN_HANDLED)
            // Stats
            setHealthAttribute(10.0, AttributeTags.SUNKEN_LIBRARY_HEALTH_BONUS)
            heal(10.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
            addAttackAttribute(2.0, AttributeTags.SUNKEN_LIBRARY_ATTACK_BONUS)
            addArmorAttribute(2.0, AttributeTags.SUNKEN_LIBRARY_ARMOR_BONUS)
            addSpeedAttribute(0.02, AttributeTags.SUNKEN_LIBRARY_SPEED_BONUS)
            addStepAttribute(0.5, AttributeTags.SUNKEN_LIBRARY_STEP_HEIGHT)
        }
        // Finish
        return
    }


    /*-----------------------------------------------------------------------------------------------*/
    // INCENDIUM

    private fun cloneIllagerSanctum(illager: Illager) : Illager {
        return (illager.world.spawnEntity(
            illager.location,
            illager.type,
            CreatureSpawnEvent.SpawnReason.CUSTOM) as Illager).apply {
            // Set data
            isPersistent = true
            removeWhenFarAway = false
            scoreboardTags.addAll(illager.scoreboardTags)
            scoreboardTags.add(EntityTags.CLONED)
            // Copy equipment
            copyAndSetEquipment(this, illager)
        }
    }

    internal fun clonePiglinBrute(brute: PiglinBrute) : PiglinBrute {
        return (brute.world.spawnEntity(
            brute.location,
            brute.type,
            CreatureSpawnEvent.SpawnReason.CUSTOM) as PiglinBrute).apply {
            // Set Data
            isPersistent = true
            removeWhenFarAway = false
            scoreboardTags.addAll(brute.scoreboardTags)
            scoreboardTags.add(EntityTags.CLONED)
            server.scoreboardManager.mainScoreboard.getEntityTeam(brute)?.addEntity(this)
            customName(brute.customName())

            // Copy equipment
            copyAndSetEquipment(this, brute)

            // Roll for trim
            var promoted = true
            var rank = 0
            var trimMaterial: TrimMaterial? = null
            when((0..110).random()) {
                in 0..9 -> {
                    trimMaterial = TrimMaterial.NETHERITE
                    rank = 4
                }
                in 10..29 -> {
                    trimMaterial = TrimMaterial.DIAMOND
                    rank = 3
                }
                in 30..59 -> {
                    trimMaterial = TrimMaterial.GOLD
                    rank = 2
                }
                in 60..100 -> {
                    trimMaterial = TrimMaterial.COPPER
                    rank = 1
                }
                else -> promoted = false
            }
            if (promoted) {
                // TrimMaterials
                val newTrim = ArmorTrim(trimMaterial!!, TrimPattern.SNOUT)
                trimMobArmor(this, newTrim)
                // New stats based on rank
                val bonusHealth = (rank * 4.0)
                val bonusDamage = (rank * 1.0)
                setHealthAttribute(bonusHealth)
                addAttackAttribute(bonusDamage)
            }
            // Random Enchant
            val eMax = maxOf((rank * 5), 1) // Clamp from 1..x
            enchantMobWornArmorRandomly(this, 1..eMax)

        }
    }




}