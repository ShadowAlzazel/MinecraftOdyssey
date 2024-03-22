package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import me.shadowalzazel.mcodyssey.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.recipe_creators.merchant.ArcaneSales
import me.shadowalzazel.mcodyssey.trims.Trims
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.absoluteValue
import kotlin.math.pow

object SpawningListeners : Listener, AttributeManager, EnchantSlotManager {

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
                if (event.entity.location.block.lightFromSky < 1) return
                // Roll Gilded - Base 2%
                val mobEx = if (event.entity is Zombie || event.entity is Skeleton) 25 else 0
                val extraDif = getDifScale(event.entity) * 10
                val rollGilded = (20 + extraDif + mobEx > (0..1000).random())
                if (rollGilded) gildedMobCreator(event.entity)
            }
        }
    }
    // 2% For gilded mob
    // 10% For a pack of elites


    private fun gildedMobCreator(mob: LivingEntity) {
        val gildedEnchant = listOf(OdysseyEnchantments.MELEE_SET, OdysseyEnchantments.ARMOR_SET).random().random()
        val enchantContext = gildedEnchant.enchantName
        val conjunctions = listOf("no", "of", "de", "con", "imbued by", "keeper of", "the")
        val nameText = "${dangerPrefixes.random()} ${mob.name} ${conjunctions.random()} $enchantContext"
        val newName = Component.text(nameText).color(TextColor.color(255, 170, 0))
        // Equipment
        val weaponList = listOf(Weapons.DIAMOND_CLAYMORE, Weapons.DIAMOND_WARHAMMER, Weapons.DIAMOND_HALBERD,
            Weapons.DIAMOND_KATANA, Weapons.DIAMOND_LONG_AXE, Weapons.DIAMOND_SABER,
            Weapons.DIAMOND_SCYTHE, Weapons.DIAMOND_SPEAR, Weapons.DIAMOND_RAPIER)
        var mainHand = weaponList.random().createWeapon(0.5)

        val difficultyMod = getDifScale(mob)

        // Melee Enchant
        if (gildedEnchant in OdysseyEnchantments.MELEE_SET) {
            // Get compatible weapon
            var canEnchant = gildedEnchant.canEnchantItem(mainHand)
            while (!canEnchant) {
                mainHand = weaponList.random().createWeapon(0.5)
                canEnchant = gildedEnchant.canEnchantItem(mainHand)
            }
            mainHand.itemMeta = mainHand.itemMeta.also {
                it.addEnchant(gildedEnchant.toBukkit(), gildedEnchant.maximumLevel, true)
            }
        }

        // Main hand
        mainHand.itemMeta = mainHand.itemMeta.also {
            if (weaponCheckIfCanApply(Enchantment.DAMAGE_ALL, gildedEnchant, mainHand))  {
                it.addEnchant(Enchantment.DAMAGE_ALL, (3..5).random(), false)
            }
            if (weaponCheckIfCanApply(Enchantment.DURABILITY, gildedEnchant, mainHand))  {
                it.addEnchant(Enchantment.DURABILITY, (1..3).random(), false)
            }
            if (weaponCheckIfCanApply(Enchantment.KNOCKBACK, gildedEnchant, mainHand))  {
                it.addEnchant(Enchantment.KNOCKBACK, (1..2).random(), false)
            }
            // Bonus
            val bonusEnchant = listOf(Enchantment.FIRE_ASPECT, Enchantment.KNOCKBACK, Enchantment.LOOT_BONUS_BLOCKS,
            Enchantment.DAMAGE_UNDEAD, Enchantment.LOOT_BONUS_MOBS, Enchantment.SWEEPING_EDGE).random()
            if (weaponCheckIfCanApply(bonusEnchant, gildedEnchant, mainHand)) {
                it.addEnchant(bonusEnchant, (1..bonusEnchant.maxLevel).random(), false)
            }
        }
        mainHand.createNewEnchantSlots()

        // Armor
        val helmet = createGildedArmor(Material.GOLDEN_HELMET, gildedEnchant)
        val chestplate = createGildedArmor(Material.GOLDEN_CHESTPLATE, gildedEnchant)
        val leggings = createGildedArmor(Material.GOLDEN_LEGGINGS, gildedEnchant)
        val boots = createGildedArmor(Material.GOLDEN_BOOTS, gildedEnchant)

        // Apply
        mob.apply {
            // Options
            customName(newName)
            isCustomNameVisible = true
            canPickupItems = true
            // Add Items
            equipment?.also {
                it.setItemInMainHand(mainHand)
                it.helmet = helmet
                it.chestplate = chestplate
                it.leggings = leggings
                it.boots = boots
                it.itemInMainHandDropChance = 0.86F // Change to difficulty
                it.helmetDropChance = 0.3F
                it.chestplateDropChance = 0.3F
                it.leggingsDropChance = 0.3F
                it.bootsDropChance = 0.3F
            }
            addHealthAttribute(35 + (20.0 * difficultyMod), id = AttributeIDs.ODYSSEY_GILDED_MOB_HEALTH_UUID)
            health += 35 + (20.0 * difficultyMod)
            addAttackAttribute(3 + (3 * difficultyMod), id = AttributeIDs.ODYSSEY_GILDED_MOB_ATTACK_UUID)
            addArmorAttribute(4 + (2 * difficultyMod), id = AttributeIDs.ODYSSEY_GILDED_MOB_ARMOR_UUID)
            addSpeedAttribute(0.012 + (0.012 * difficultyMod), id = AttributeIDs.ODYSSEY_GILDED_MOB_SPEED_UUID)
            addScoreboardTag(EntityTags.GILDED_MOB)
            if (mob.location.block.lightFromSky > 5) {
                isPersistent = true
            }
            // Potion
            if (this !is Creeper) {
                addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 999999, 1))
                addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0))
                addPotionEffect(PotionEffect(PotionEffectType.SPEED, 999999, 0))
                addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1))
            }
        }

        println("Spawned Gilded Mob at: ${mob.location}")
    }

    private fun weaponCheckIfCanApply(enchantment: Enchantment, gilded: OdysseyEnchantment, item: ItemStack): Boolean {
        if (gilded.conflictsWith(enchantment)) return false
        if (!enchantment.canEnchantItem(item)) return false
        return true
    }

    private fun createGildedArmor(armorType: Material, gildedEnchant: OdysseyEnchantment): ItemStack {
        val armor = ItemStack(armorType, 1)
        // Trim
        val newTrim = ArmorTrim(
            listOf(TrimMaterial.DIAMOND, TrimMaterial.COPPER, TrimMaterial.EMERALD, TrimMaterial.AMETHYST, TrimMaterial.LAPIS,
                Trims.JADE, Trims.ALEXANDRITE, Trims.RUBY, Trims.KUNZITE, Trims.OBSIDIAN).random(),
            listOf(TrimPattern.WAYFINDER, TrimPattern.RAISER, TrimPattern.HOST, TrimPattern.SHAPER,
                TrimPattern.SENTRY, TrimPattern.DUNE, TrimPattern.WILD, TrimPattern.COAST).random()
        )
        // Apply
        armor.itemMeta = (armor.itemMeta as ArmorMeta).also {
            it.trim = newTrim
            it.addEnchant(Enchantment.DURABILITY, (1..3).random(), false)
            val prot = listOf(Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_ENVIRONMENTAL,
                Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_FIRE).random()
            it.addEnchant(prot, (1..prot.maxLevel).random(), false)
            if (gildedEnchant.canEnchantItem(armor)) {
                it.addEnchant(gildedEnchant.toBukkit(), gildedEnchant.maxLevel, false)
            }
        }
        armor.createNewEnchantSlots()
        return armor
    }


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
                //println("Brute: (${brute.uniqueId}). Tags: ${brute.scoreboardTags} ")
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
                // Trims
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
            // Attributes
            val healthModifier = AttributeModifier(
                AttributeIDs.ODYSSEY_MOB_HEALTH_UUID,
                "odyssey.mob_health",
                (rank * 3.0) + 4.0,
                AttributeModifier.Operation.ADD_NUMBER)
            getAttribute(Attribute.GENERIC_MAX_HEALTH)?.addModifier(healthModifier)
            health = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: health
            val damageModifier = AttributeModifier(
                AttributeIDs.ODYSSEY_MOB_DAMAGE_UUID,
                "odyssey.mob_damage",
                (rank * 2.0) + 1.0,
                AttributeModifier.Operation.ADD_NUMBER)
            getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.addModifier(damageModifier)
        }
    }


}