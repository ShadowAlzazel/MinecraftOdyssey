package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.items.creators.ToolCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.recipes.merchant.ArcaneSales
import me.shadowalzazel.mcodyssey.util.MobEquipHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.generator.structure.Structure
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.Random
import kotlin.math.absoluteValue
import kotlin.math.pow

object SpawningListeners : Listener, AttributeManager, MobEquipHelper, EnchantabilityHandler {

    private val dangerPrefixes = setOf(
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
        "Fatal")

    @EventHandler
    fun mobNaturalSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) { return }
        when(event.entity.type) {
            EntityType.WANDERING_TRADER -> {
                if (3 >= (1..10).random()) {
                    DubiousDealer.createMob(event.location.world, event.location)
                }
                else {
                    (event.entity as WanderingTrader).apply {
                        setRecipe(recipeCount - 1, ArcaneSales.createArcaneBookTrade())
                    }
                }
            }
            else -> {
                if (event.entity !is Enemy) return
                // Surface Spawns for now -> fix for mob farms
                if (event.entity is Guardian) return
                checkStructure(event.entity)
                if (event.entity.location.block.lightFromSky < 1) return
                // Roll Elite - Base 2%
                val mobEx = if (event.entity is Zombie || event.entity is Skeleton) 25 else 0
                val extraDif = getDifScale(event.entity) * 10
                val rollElite = (20 + extraDif + mobEx > (0..1000).random())
                if (rollElite) createEliteMob(event.entity)
            }
        }
    }

    private fun checkStructure(mob: LivingEntity) {
        val structures = mob.location.chunk.structures
        if (structures.isEmpty()) return
        val inside = structures.filter { mob.boundingBox.overlaps(it.boundingBox) }
        if (inside.isEmpty()) return
        // Check if inside
        for (struct in inside) {
            // Is Mesa
            if (struct.structure == Structure.MINESHAFT || struct.structure == Structure.MINESHAFT_MESA) {
                mob.addScoreboardTag(EntityTags.IN_MINESHAFT)
            }
        }
    }

    // 2% For gilded mob
    // 0.1% For a pack of elites
    private fun createEliteMob(mob: LivingEntity) {
        if (mob is Creeper) return
        // Difficulty
        val difficultyMod = getDifScale(mob)
        // Weapon
        val weaponTypes = listOf(ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.POLEAXE, ToolType.WARHAMMER)
        val materialType = ToolMaterial.IRON
        val mainHand: ItemStack = when(mob.type) {
            EntityType.SKELETON, EntityType.STRAY -> {
                mob.equipment?.itemInMainHand ?: ItemStack(Material.BOW)
            }
            EntityType.ZOMBIE, EntityType.HUSK -> {
                ToolCreator().createToolStack(materialType, weaponTypes.random())
            }
            else -> {
                ToolCreator().createToolStack(materialType, weaponTypes.random())
            }
        }
        // GET ENCHANTMENTS TO SWORD
        val greaterEnchant: Pair<Enchantment, Int>
        // Enchant randomly and get gilded
        val weapon = mainHand.enchantWithLevels(30, false, Random())
        weapon.apply {
            val newEnchant = this.enchantments.entries.random()
            greaterEnchant = Pair(newEnchant.key, newEnchant.value + 1)
            addGildedEnchant(greaterEnchant.first, greaterEnchant.second)
            updateEnchantabilityPointsLore()

        }
        // Naming
        val enchantName = greaterEnchant.first.displayName(greaterEnchant.second).color(TextColor.color(85, 255, 255))
        val conjunctions = listOf("with", "of", "master of", "joined with", "imbued by", "keeper of", "holder of")
        val nameText = "${dangerPrefixes.random()} ${mob.name} ${conjunctions.random()} "
        //val nameText = "${dangerPrefixes.random()} ${mob.name}"
        val newName = Component.text(nameText).color(TextColor.color(85, 255, 255)).append(enchantName)
        // Apply to Mob
        val trim = createRandomArmorTrim()
        mob.apply {
            // Options
            customName(newName)
            isCustomNameVisible = true
            canPickupItems = true
            // Add Items
            createTrimmedArmor(this, trim, "golden")
            equipment?.also {
                it.setItemInMainHand(weapon)
                it.itemInMainHandDropChance = 0.85F // Change to difficulty
                it.helmetDropChance = 0.3F
                it.chestplateDropChance = 0.3F
                it.leggingsDropChance = 0.3F
                it.bootsDropChance = 0.3F
            }
            addHealthAttribute(45 + (20.0 * difficultyMod), AttributeTags.ELITE_HEALTH)
            health += 45 + (20.0 * difficultyMod)
            addAttackAttribute(3 + (1 * difficultyMod), AttributeTags.ELITE_ATTACK_DAMAGE)
            addArmorAttribute(6 + (2 * difficultyMod), AttributeTags.ELITE_ARMOR)
            addSpeedAttribute(0.012 + (0.012 * difficultyMod), AttributeTags.ELITE_SPEED)
            addScaleAttribute(0.2)
            addScoreboardTag(EntityTags.ELITE_MOB)
            addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 20 * 3600, 0))
            // Persistent
            if (mob.location.block.lightFromSky > 5) {
                isPersistent = true
            }
        }
        println("Spawned Elite Mob at: ${mob.location}")
    }

    /*-----------------------------------------------------------------------------------------------*/
    private fun getDifScale(entity: Entity): Double {
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