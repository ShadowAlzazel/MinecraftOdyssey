package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.Identifiers
import me.shadowalzazel.mcodyssey.tasks.PiglinRallyTask
import me.shadowalzazel.mcodyssey.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.recipe_creators.merchant.Sales
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.MusicInstrumentMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SpawningListeners : Listener {

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
                        setRecipe(recipeCount - 1, Sales.createArcaneBookTrade())
                    }
                }
            }
            else -> {
            }
        }
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


    @EventHandler
    private fun runningPig(event: EntityTargetLivingEntityEvent) {
        if (event.entity !is PiglinBrute) return
        if (event.entity.scoreboardTags.contains(EntityTags.RUNNING_PIG)) { event.isCancelled = true }
    }

    @EventHandler
    private fun piglinHelp(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        if (event.entity !is PiglinBrute) return
        val brute = event.entity as PiglinBrute
        if (brute.scoreboardTags.contains(EntityTags.STARTED_RALLYING)) return
        if (event.cause == DamageCause.PROJECTILE) return

        // RNG HERE
        val roll = (1 > (0..40).random())
        if (!roll) { return }

        brute.also {
            // FIND
            var far = 0.0
            var helper: PiglinBrute? = null
            it.getNearbyEntities(16.0, 6.0, 16.0).filterIsInstance<PiglinBrute>().forEach { piglin ->
                val distance = piglin.location.distance(brute.location)
                if (distance > far) {
                    far = distance
                    helper = piglin
                }
            }
            if (helper == null) return
            // SOUNDS
            it.world.playSound(it.location, Sound.ITEM_GOAT_HORN_SOUND_5, 5.5F, 0.95F)
            val horn = ItemStack(Material.GOAT_HORN)
            val musicMeta = horn.itemMeta as MusicInstrumentMeta
            musicMeta.instrument = MusicInstrument.CALL
            horn.itemMeta = musicMeta
            it.equipment.setItemInOffHand(horn)
            it.swingOffHand()
            // SCORE
            it.addScoreboardTag(EntityTags.STARTED_RALLYING)
            it.addScoreboardTag(EntityTags.RUNNING_PIG)
            // MOVEMENT
            val newLocation = helper!!.location.clone()
            it.target = null
            it.pathfinder.stopPathfinding()
            it.pathfinder.moveTo(newLocation, 1.2)
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 * 8, 0))
        }

        val rallyTask = PiglinRallyTask(brute)
        rallyTask.runTaskLater(Odyssey.instance, 20 * 8)
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
                Identifiers.ODYSSEY_MOB_HEALTH_UUID,
                "odyssey.mob_health",
                (rank * 3.0) + 4.0,
                AttributeModifier.Operation.ADD_NUMBER)
            getAttribute(Attribute.GENERIC_MAX_HEALTH)?.addModifier(healthModifier)
            health = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: health
            val damageModifier = AttributeModifier(
                Identifiers.ODYSSEY_MOB_DAMAGE_UUID,
                "odyssey.mob_damage",
                (rank * 2.0) + 1.0,
                AttributeModifier.Operation.ADD_NUMBER)
            getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.addModifier(damageModifier)
        }
    }

}