package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.creators.ToolCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.recipes.merchant.ArcaneSales
import me.shadowalzazel.mcodyssey.trims.TrimMaterials
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
import org.bukkit.generator.structure.Structure
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
                checkStructure(event.entity)
                if (event.entity.location.block.lightFromSky < 1) return
                // Roll Gilded - Base 2%
                val mobEx = if (event.entity is Zombie || event.entity is Skeleton) 25 else 0
                val extraDif = getDifScale(event.entity) * 10
                val rollGilded = (20 + extraDif + mobEx > (0..1000).random())
                if (rollGilded) gildedMobCreator(event.entity)
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
    // 10% For a pack of elites
    private fun gildedMobCreator(mob: LivingEntity) {
        // Weapon
        val weaponTypes = listOf(ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.POLEAXE, ToolType.WARHAMMER)
        val mainHand: ItemStack
        val gildedEnchant: OdysseyEnchantment
        // Get Gilded
        when(mob.type) {
            EntityType.SKELETON, EntityType.STRAY -> {
                gildedEnchant = OdysseyEnchantments.RANGED_SET.random()
                mainHand = mob.equipment?.itemInMainHand ?: ItemStack(Material.BOW)
            }
            EntityType.ZOMBIE, EntityType.HUSK -> {
                gildedEnchant = OdysseyEnchantments.MELEE_SET.random()
                mainHand = ToolCreator().createToolStack(ToolMaterial.GOLDEN, weaponTypes.random())
            }
            else -> {
                gildedEnchant = OdysseyEnchantments.ARMOR_SET.random()
                mainHand = ToolCreator().createToolStack(ToolMaterial.GOLDEN, weaponTypes.random())
            }
        }
        // Naming
        val enchantName = gildedEnchant.translatableName
        val conjunctions = listOf("no", "of", "de", "con", "imbued by", "keeper of", "the")
        val nameText = "${dangerPrefixes.random()} ${mob.name} ${conjunctions.random()} $enchantName"
        val newName = Component.text(nameText).color(TextColor.color(255, 170, 0))

        // Difficulty
        val difficultyMod = getDifScale(mob)
        /*
        mainHand.itemMeta = mainHand.itemMeta.also {
            it.addEnchant(gildedEnchant.toBukkit(), gildedEnchant.maximumLevel + 1, true)
        }
         */
        mainHand.addOdysseyEnchantment(gildedEnchant, gildedEnchant.maximumLevel + (1..2).random())
        // Main hand
        mainHand.itemMeta = mainHand.itemMeta.also {
            if (weaponCheckIfCanApply(Enchantment.SHARPNESS, gildedEnchant, mainHand))  {
                it.addEnchant(Enchantment.SHARPNESS, (3..5).random(), false)
            }
            if (weaponCheckIfCanApply(Enchantment.UNBREAKING, gildedEnchant, mainHand))  {
                it.addEnchant(Enchantment.UNBREAKING, (1..3).random(), false)
            }
            if (weaponCheckIfCanApply(Enchantment.KNOCKBACK, gildedEnchant, mainHand))  {
                it.addEnchant(Enchantment.KNOCKBACK, (1..2).random(), false)
            }
            // Bonus
            val bonusEnchant = listOf(Enchantment.FIRE_ASPECT, Enchantment.KNOCKBACK, Enchantment.LOOTING,
            Enchantment.SMITE, Enchantment.LOOTING, Enchantment.SWEEPING_EDGE).random()
            if (weaponCheckIfCanApply(bonusEnchant, gildedEnchant, mainHand)) {
                it.addEnchant(bonusEnchant, (1..bonusEnchant.maxLevel).random(), false)
            }
        }
        mainHand.createNewEnchantSlots()
        // Armor Trimming
        val helmet = createTrimmedArmor(Material.GOLDEN_HELMET)
        val chestplate = createTrimmedArmor(Material.GOLDEN_CHESTPLATE)
        val leggings = createTrimmedArmor(Material.GOLDEN_LEGGINGS)
        val boots = createTrimmedArmor(Material.GOLDEN_BOOTS)
        // Armor Enchanting
        enchantMobArmor(helmet, gildedEnchant)
        enchantMobArmor(chestplate, gildedEnchant)
        enchantMobArmor(leggings, gildedEnchant)
        enchantMobArmor(boots, gildedEnchant)
        // Apply to Mob
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
                addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 999999, 0))
                addPotionEffect(PotionEffect(PotionEffectType.SPEED, 999999, 0))
                addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 999999, 1))
            }
        }
        println("Spawned Gilded Mob at: ${mob.location}")
    }

    private fun weaponCheckIfCanApply(enchantment: Enchantment, gilded: OdysseyEnchantment, item: ItemStack): Boolean {
        if (gilded.checkBukkitConflict(enchantment)) return false
        if (!enchantment.canEnchantItem(item)) return false
        return true
    }

    private fun createTrimmedArmor(armorType: Material): ItemStack {
        val armor = ItemStack(armorType, 1)
        // Trim
        val newTrim = ArmorTrim(
            listOf(TrimMaterial.DIAMOND, TrimMaterial.COPPER, TrimMaterial.EMERALD, TrimMaterial.AMETHYST, TrimMaterial.LAPIS,
                TrimMaterials.JADE, TrimMaterials.ALEXANDRITE, TrimMaterials.RUBY, TrimMaterials.KUNZITE, TrimMaterials.OBSIDIAN).random(),
            listOf(TrimPattern.WAYFINDER, TrimPattern.RAISER, TrimPattern.HOST, TrimPattern.SHAPER,
                TrimPattern.SENTRY, TrimPattern.DUNE, TrimPattern.WILD, TrimPattern.COAST).random()
        )
        armor.itemMeta = (armor.itemMeta as ArmorMeta).also {
            it.trim = newTrim
        }
        return armor
    }

    private fun enchantMobArmor(armor: ItemStack, gildedEnchant: OdysseyEnchantment): ItemStack {
        // Apply
        if (gildedEnchant.canEnchantItem(armor)) {
            armor.addOdysseyEnchantment(gildedEnchant, gildedEnchant.maximumLevel + 1)
        }
        armor.itemMeta = (armor.itemMeta as ArmorMeta).also {
            it.addEnchant(Enchantment.UNBREAKING, (1..3).random(), false)
            val prot = listOf(Enchantment.PROJECTILE_PROTECTION, Enchantment.PROTECTION,
                Enchantment.BLAST_PROTECTION, Enchantment.FIRE_PROTECTION).random()
            it.addEnchant(prot, (1..prot.maxLevel).random(), false)
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