package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.structures.StructureManager
import me.shadowalzazel.mcodyssey.util.MobCreationHelper
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.generator.structure.Structure
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SpawningListeners : Listener, MobCreationHelper, StructureManager {

    @EventHandler
    fun mobNaturalSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) return
        when(event.entity.type) {
            EntityType.WANDERING_TRADER -> {
                if (3 >= (1..10).random()) {
                    DubiousDealer.createMob(event.location.world, event.location)
                }
            }
            else -> {
                if (event.entity !is Enemy) return
                // Surface Spawns for now -> fix for mob farms
                if (event.entity is Guardian) return
                val mob =   event.entity
                structureTagHandler(mob)
                if (mob.location.block.lightFromSky < 1) return
                val inEdge = mob.location.world == Odyssey.instance.edge
                // Roll Elite - Base 2%
                val elitePreferenceBonus = if (mob is Zombie || mob is Skeleton) 25 else 0
                val extraDif = getScaledDifficulty(mob) * 10
                var eliteSpawnBonus = 20 + extraDif + elitePreferenceBonus
                if (inEdge) {
                    eliteSpawnBonus += 20
                }
                val rolledElite = (eliteSpawnBonus > (0..1000).random())
                // 2% For Elite Mob
                // 0.1% For a pack of shiny
                if (rolledElite) {
                    val materialInEdge = if (inEdge) ToolMaterial.DIAMOND else ToolMaterial.IRON
                    createShinyMob(mob, inEdge, materialInEdge)
                }
                else if (inEdge) {
                    mob.addAttackAttribute(2.0 + (0..2).random(), AttributeTags.MOB_EDGE_ATTACK_BONUS)
                    mob.addAttackAttribute(15.0, AttributeTags.MOB_EDGE_HEALTH_BONUS)
                    mob.heal(15.0)
                }
            }
        }
    }

    fun spawningStructureHandler(event: EntitySpawnEvent) {
        println("Location: [${event.location} Entity: [${event.entity}]")
        println(event.eventName)
        return
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun structureTagHandler(mob: LivingEntity) {
        val boundedStructures = getBoundedStructures(mob) ?: return
        // Check if inside
        for (struct in boundedStructures) {
            // Is Mesa
            if (struct == Structure.MINESHAFT || struct == Structure.MINESHAFT_MESA) {
                mob.addScoreboardTag(EntityTags.IN_MINESHAFT)
            }
        }
    }


    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun mobStructureSpawning(event: ChunkPopulateEvent) {
        val pigList = event.chunk.entities.filter { it.type == EntityType.PIGLIN_BRUTE }
        if (pigList.isNotEmpty()) {
            pigList.forEach { brute ->
                // Incendium
                if (!brute.scoreboardTags.contains("in.pyro") && !brute.scoreboardTags.contains(EntityTags.CLONED)) {
                    handlePiglinBruteSpawn(brute as PiglinBrute)
                    if (brute.scoreboardTags.contains("in.knight")) {
                        handlePiglinBruteSpawn(brute)
                    }
                }
            }
        }

        // Incendium
        val illagerList = event.chunk.entities.filter { it.scoreboardTags.contains("in.sanctum") && it is Illager }
        if(illagerList.isNotEmpty()) {
            illagerList.forEach { illager ->
                handleSanctumSpawn(illager as Illager)
                if (illager is Vindicator) {
                    handleSanctumSpawn(illager as Illager)
                }
            }
        }

    }

    // INCENDIUM
    private fun handleSanctumSpawn(illager: Illager) {
        (illager.world.spawnEntity(illager.location, illager.type, CreatureSpawnEvent.SpawnReason.CUSTOM) as Illager).apply {
            isPersistent = true
            removeWhenFarAway = false
            scoreboardTags.addAll(illager.scoreboardTags)
            scoreboardTags.add(EntityTags.CLONED)
            equipment.armorContents = illager.equipment.armorContents
            equipment.helmetDropChance = illager.equipment.helmetDropChance
            equipment.chestplateDropChance = illager.equipment.chestplateDropChance
            equipment.bootsDropChance = illager.equipment.bootsDropChance
            equipment.itemInMainHandDropChance = illager.equipment.itemInMainHandDropChance
            equipment.itemInOffHandDropChance = illager.equipment.itemInOffHandDropChance
            equipment.setItemInMainHand(illager.equipment.itemInMainHand)
            equipment.setItemInOffHand(illager.equipment.itemInOffHand)
        }
    }

    internal fun handlePiglinBruteSpawn(brute: PiglinBrute) : PiglinBrute {
        return (brute.world.spawnEntity(brute.location, brute.type, CreatureSpawnEvent.SpawnReason.CUSTOM) as PiglinBrute).apply {
            isPersistent = true
            removeWhenFarAway = false
            scoreboardTags.addAll(brute.scoreboardTags)
            scoreboardTags.add(EntityTags.CLONED)
            server.scoreboardManager.mainScoreboard.getEntityTeam(brute)?.addEntity(this)
            customName(brute.customName())
            equipment.armorContents = brute.equipment.armorContents
            equipment.helmetDropChance = brute.equipment.helmetDropChance
            equipment.chestplateDropChance = brute.equipment.chestplateDropChance
            equipment.bootsDropChance = brute.equipment.bootsDropChance
            equipment.itemInMainHandDropChance = brute.equipment.itemInMainHandDropChance
            equipment.itemInOffHandDropChance = brute.equipment.itemInOffHandDropChance
            equipment.setItemInMainHand(brute.equipment.itemInMainHand)
            equipment.setItemInOffHand(brute.equipment.itemInOffHand)
            // Roll for trim
            var promoted = true
            var rank = 2
            var trimMaterial: TrimMaterial? = null
            when((0..100).random()) {
                in 0..5 -> {
                    trimMaterial = TrimMaterial.NETHERITE
                    rank = 4
                }
                in 6..30 -> {
                    trimMaterial = TrimMaterial.GOLD
                    rank = 3
                }
                else -> {
                    promoted = false
                }
            }
            if (promoted) {
                // TrimMaterials
                val newTrim = ArmorTrim(trimMaterial!!, TrimPattern.SNOUT)
                if (equipment.helmet.itemMeta is ArmorMeta) {
                    val newMeta = (equipment.helmet.itemMeta as ArmorMeta)
                    newMeta.trim = newTrim
                }
                if (equipment.chestplate.itemMeta is ArmorMeta) {
                    val newMeta = (equipment.chestplate.itemMeta as ArmorMeta)
                    newMeta.trim = newTrim
                }
                if (equipment.leggings.itemMeta is ArmorMeta) {
                    val newMeta = (equipment.leggings.itemMeta as ArmorMeta)
                    newMeta.trim = newTrim
                }
                if (equipment.boots.itemMeta is ArmorMeta) {
                    val newMeta = (equipment.boots.itemMeta as ArmorMeta)
                    newMeta.trim = newTrim
                }
            }
            val bonusHealth = (rank * 3.0) + 4.0
            val bonusDamage = (rank * 2.0) + 1.0
            addHealthAttribute(bonusHealth)
            addAttackAttribute(bonusDamage)
        }
    }


}