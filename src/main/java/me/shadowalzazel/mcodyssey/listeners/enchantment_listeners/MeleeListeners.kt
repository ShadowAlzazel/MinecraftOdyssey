package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.effects.EffectsManager
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.deprecated.EnchantmentDataManager
import me.shadowalzazel.mcodyssey.tasks.enchantment_tasks.ArcaneCellTask
import me.shadowalzazel.mcodyssey.tasks.enchantment_tasks.FrogFrightTask
import me.shadowalzazel.mcodyssey.tasks.enchantment_tasks.FrostyFuseTask
import me.shadowalzazel.mcodyssey.tasks.enchantment_tasks.GravitySingularityTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.log2

object MeleeListeners : Listener, EffectsManager, EnchantmentDataManager {

    // Internal cool downs for enchantments
    private var arcaneCellCooldown = mutableMapOf<UUID, Long>()
    private var backstabberCooldown = mutableMapOf<UUID, Long>()
    private var buzzyBeesCooldown = mutableMapOf<UUID, Long>()
    //private var decayingTouchCooldown = mutableMapOf<UUID, Long>()
    private var explodingCooldown = mutableMapOf<UUID, Long>()
    private var frostyFuseCooldown = mutableMapOf<UUID, Long>()
    private var gravityWellCooldown = mutableMapOf<UUID, Long>()
    private var guardingStrikeCooldown = mutableMapOf<UUID, Long>()
    private var hemorrhageCooldown = mutableMapOf<UUID, Long>()
    private var voidStrikeCooldown = mutableMapOf<UUID, Long>()
    private var whirlwindCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to entity damage
    @EventHandler
    fun mainMeleeDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) { return }
        if (event.entity !is LivingEntity) { return }
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) { return }
        val attacker = event.damager as LivingEntity
        if (attacker.equipment?.itemInMainHand?.hasItemMeta() == false) { return }
        val victim = event.entity as LivingEntity
        val weapon = attacker.equipment!!.itemInMainHand
        val power = if (attacker is Player) { attacker.attackCooldown.toDouble() } else { 1.0 }
        // Loop for all enchants
        for (enchant in weapon.getOdysseyEnchantments()) {
            when (enchant.key) {
                OdysseyEnchantments.ARCANE_CELL -> {
                    if (cooldownManager(attacker, "Arcane Cell", arcaneCellCooldown, 5.25)) {
                        arcaneCellEnchantment(victim, enchant.value)
                    }
                }
                OdysseyEnchantments.ASPHYXIATE -> {
                    event.damage += asphyxiateEnchantment(victim, enchant.value) * power
                }
                OdysseyEnchantments.BACKSTABBER -> {
                    if (cooldownManager(attacker, "Backstabber", backstabberCooldown, 3.25)) {
                        event.damage += backstabberEnchantment(attacker, victim, enchant.value) * power
                    }
                }
                OdysseyEnchantments.BANE_OF_THE_ILLAGER -> {
                    event.damage += baneOfTheIllagerEnchantment(victim, enchant.value) * power
                }
                OdysseyEnchantments.BANE_OF_THE_SEA -> {
                    event.damage += baneOfTheSeaEnchantment(attacker, victim, enchant.value)  * power
                }
                OdysseyEnchantments.BANE_OF_THE_SWINE -> {
                    event.damage += baneOfTheSwineEnchantment(victim, enchant.value) * power
                }
                OdysseyEnchantments.BLITZ_SHIFT -> {
                    blitzSwitchEnchantment(attacker, victim, enchant.value)
                }
                OdysseyEnchantments.BUDDING -> {
                    buddingEnchantment(victim, enchant.value) // MORE STACKS -> MORE INSTANCES
                }
                OdysseyEnchantments.BUZZY_BEES -> {
                    if (cooldownManager(attacker, "Buzzy Bees", buzzyBeesCooldown, 4.25)) {
                        buzzyBeesEnchantment(victim, enchant.value)
                    }
                }
                OdysseyEnchantments.COMMITTED -> {
                    event.damage += committedEnchantment(victim, enchant.value) * power
                }
                OdysseyEnchantments.CULL_THE_WEAK -> {
                    event.damage += cullTheWeakEnchantment(victim, enchant.value)  * power
                }
                OdysseyEnchantments.DECAYING_TOUCH -> {
                    decayingTouchEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.DOUSE -> {
                    event.damage += douseEnchantment(victim, enchant.value) * power
                }
                OdysseyEnchantments.ECHO -> {
                    echoEnchantment(attacker, victim, enchant.value)
                }
                OdysseyEnchantments.FREEZING_ASPECT -> {
                    freezingAspectEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.FROG_FRIGHT -> {
                    frogFrightEnchantment(attacker, victim, enchant.value)
                }
                OdysseyEnchantments.FROSTY_FUSE -> {
                    if (cooldownManager(attacker, "Frosty Fuse", frostyFuseCooldown, 5.0)) {
                        frostyFuseEnchantment(victim, enchant.value)
                    }
                }
                OdysseyEnchantments.GRAVITY_WELL -> {
                    if (cooldownManager(attacker, "Gravity Well", gravityWellCooldown, 8.0)) {
                        gravityWellEnchantment(attacker, victim, enchant.value)
                    }
                }
                OdysseyEnchantments.GUARDING_STRIKE -> {
                    if (cooldownManager(attacker, "Guarding Strike", guardingStrikeCooldown, 4.0)) {
                        guardingStrikeEnchantment(attacker, enchant.value)
                    }
                }
                OdysseyEnchantments.HEMORRHAGE -> {
                    if (cooldownManager(attacker, "Hemorrhage", hemorrhageCooldown, 3.0)) {
                        hemorrhageEnchantment(victim, enchant.value) // MORE STACKS -> MORE DAMAGE PER INSTANCE
                    }
                }
                OdysseyEnchantments.ILLUCIDATION -> {
                    event.damage += illucidationEnchantment(victim, enchant.value, event.isCritical) * power
                }
                OdysseyEnchantments.RUPTURING_STRIKE -> {
                    event.damage -= rupturingStrikeEnchantment(attacker, victim, event.damage, enchant.value)
                }
                OdysseyEnchantments.TAR_N_DIP -> {
                    tarNDipEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.VITAL -> {
                    event.damage += vitalEnchantment(event.isCritical, enchant.value)
                }
                OdysseyEnchantments.VOID_STRIKE -> {
                    if (cooldownManager(attacker, "Void Strike", voidStrikeCooldown, 0.45)) {
                        event.damage += voidStrikeEnchantment(attacker, victim, enchant.value)
                    }
                }
                OdysseyEnchantments.WHIRLWIND -> {
                    if (cooldownManager(attacker, "Whirlwind", whirlwindCooldown, 3.0)) {
                        whirlwindEnchantment(attacker, victim, event.damage, enchant.value)
                    }
                }
            }
        }
    }

    // Main function for enchantments relating to entity deaths
    @EventHandler
    fun mainMeleeDeathHandler(event: EntityDeathEvent) {
        if (event.entity.killer !is LivingEntity) { return }
        val killer = event.entity.killer as LivingEntity
        if (killer.equipment?.itemInMainHand?.hasItemMeta() == false) { return }
        val victim: LivingEntity = event.entity
        val weapon = killer.equipment?.itemInMainHand ?: return
        // Loop for all enchants
        for (enchant in weapon.getOdysseyEnchantments()) {
            when (enchant.key) {
                OdysseyEnchantments.EXPLODING -> {
                    if (cooldownManager(killer, "Exploding", explodingCooldown, 1.25)) {
                        explodingEnchantment(victim, enchant.value)
                    }
                }
                OdysseyEnchantments.FEARFUL_FINISHER -> {
                    fearfulFinisherEnchantment(victim, enchant.value)
                }
            }
        }
    }

    @EventHandler
    fun entityKnockBackHandler(event: EntityKnockbackByEntityEvent) {
        if (event.hitBy !is LivingEntity) return
        val attacker = event.hitBy as LivingEntity
        val hitWeapon = attacker.equipment?.itemInMainHand ?: return
        // Loop
        for (enchant in hitWeapon.getOdysseyEnchantments()) {
            when (enchant.key) {
                OdysseyEnchantments.GUST -> {
                    event.acceleration = gustEnchantment(event.acceleration, enchant.value)
                }
            }
        }
    }

    // Helper function for cooldown
    private fun cooldownManager(attacker: LivingEntity, message: String, cooldownMap: MutableMap<UUID, Long>, timer: Double): Boolean {
        if (!cooldownMap.containsKey(attacker.uniqueId)) {
            cooldownMap[attacker.uniqueId] = 0L
        }
        // Cooldown Timer
        val timeElapsed: Long = System.currentTimeMillis() - cooldownMap[attacker.uniqueId]!!
        return if (timeElapsed > timer * 1000) {
            cooldownMap[attacker.uniqueId] = System.currentTimeMillis()
            true
        } else {
            val cooldownTime = timer - ((timeElapsed / 1) * 0.001)
            val actionMessage = String.format("%s on Cooldown (Time Remaining: %.2f s)", message, cooldownTime)
            attacker.sendActionBar(
                Component.text(
                    actionMessage,
                    TextColor.color(155, 155, 155)
                )
            )
            false
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // -------------------------- ASPHYXIATE -------------------------------
    private fun asphyxiateEnchantment(
        victim: LivingEntity,
        level: Int
    ): Double {
        victim.remainingAir -= 20 * ((level * 2) + 3)
        if (victim.remainingAir < 20) return 3.0
        return 0.0
    }

    // ------------------------------- ARCANE_CELL ------------------------------------
    private fun arcaneCellEnchantment(victim: LivingEntity, level: Int) {
        if (victim.scoreboardTags.contains(EffectTags.ARCANE_JAILED)) return
        // Run
        with(victim) {
            addScoreboardTag(EffectTags.ARCANE_JAILED)
            world.spawnParticle(Particle.WITCH, location, 5, 0.2, 0.2, 0.2)
            val task = ArcaneCellTask(victim, location, level, (2 + (2 * level)) * 20)
            task.runTaskTimer(Odyssey.instance, 5, 5)
        }
    }
    // ------------------------------- BACKSTABBER ------------------------------------
    private fun backstabberEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ): Int {
        val victimTarget = victim.getTargetEntity(4)
        val isInvisible = attacker.isInvisible || attacker.hasPotionEffect(PotionEffectType.INVISIBILITY)
        // Looking more than 90-deg (1.57-rads) away from attacker
        val isNotTarget = victimTarget != attacker || victim.eyeLocation.direction.angle(attacker.eyeLocation.direction) > 1.5708

        if (isInvisible || isNotTarget) {
            // Particles and sounds
            with(victim.world) {
                spawnParticle(Particle.CRIT, victim.location, 25, 0.5, 0.5, 0.5)
                spawnParticle(Particle.WARPED_SPORE, victim.location, 25, 0.5, 0.5, 0.5)
                spawnParticle(Particle.ELECTRIC_SPARK, victim.location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT, victim.location, 15, 0.5, 0.5, 0.5)
                playSound(victim.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
            }
            // Damage
            return (3 + (level * 3))
        }
        return 0
    }
    // ------------------------------- BANE_OF_THE_ILLAGER ------------------------------------
    private fun baneOfTheIllagerEnchantment(
        victim: LivingEntity,
        level: Int
    ): Double {
        if (victim is Raider) {
            return level.toDouble() * 2.5
        }
        return 0.0
    }
    // ------------------------------- BANE_OF_THE_SEA ------------------------------------
    private fun baneOfTheSeaEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ): Double {
        if (victim.isInWaterOrRainOrBubbleColumn || victim is WaterMob || victim.isSwimming || attacker.isInWaterOrRainOrBubbleColumn) {
            return level.toDouble() * 2.0
        }
        return 0.0
    }
    // ------------------------------- BANE_OF_THE_SWINE ------------------------------------
    private fun baneOfTheSwineEnchantment(
        victim: LivingEntity,
        level: Int
    ): Double {
        if (victim is PiglinAbstract || victim is Pig || victim is Hoglin) {
            return level.toDouble() * 2.5
        }
        return 0.0
    }
    // ------------------------------- BLITZ_SWITCH -------------------------
    private fun blitzSwitchEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ) {
        val attackerLocation = attacker.location.clone()
        val victimLocation = victim.location.clone()
        val distance = attackerLocation.distance(victimLocation)

        attacker.teleport(victimLocation)
        if (attacker is HumanEntity) {
            val log2Amount = maxOf(log2(distance).toInt() - 1, 0)
            attacker.foodLevel = maxOf(attacker.foodLevel - log2Amount, 0)
        }
        victim.teleport(attackerLocation)
    }
    // Other enchant ideas
    // MAYBE DO NEXT TIME YOU SNEAK???? OR TOGGLE WITH
    // or do dmg based on distance?

    // ------------------------------- BUDDING ------------------------------------
    private fun buddingEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        with(victim) {
            addOdysseyEffect(EffectTags.BUDDING, 12 * 20, level * 1)
            world.playSound(location, Sound.BLOCK_BIG_DRIPLEAF_TILT_UP, 2.5F, 0.9F)
        }
    }

    // ------------------------------- BUZZY_BEES ------------------------------------
    private fun buzzyBeesEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Spawn Bee if not low
        with(victim) {
            if (!isDead) {
                (world.spawnEntity(victim.location.clone().add((-50..50).random() * 0.1, (30..80).random() * 0.1, (-50..50).random() * 0.1), EntityType.BEE) as Bee).also {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.SPEED, 10 * 20, level - 1),
                            PotionEffect(PotionEffectType.STRENGTH, 10 * 20, level - 1),
                            PotionEffect(PotionEffectType.HASTE, 10 * 20, 0),
                            PotionEffect(PotionEffectType.ABSORPTION, 10 * 20, level - 1)
                        )
                    )
                    it.target = this@with
                }
            }
            val seconds = (3 * level) + 3
            victim.addOdysseyEffect(EffectTags.HONEYED, seconds * 20, level)
            world.playSound(location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
        }
    }
    // ------------------------------- COMMITTED ------------------------------------
    private fun committedEnchantment(victim: LivingEntity, level: Int): Int {
        val maxHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        return if (victim.health < maxHealth * 0.4) {
            level + 1
        } else {
            0
        }
    }
    // ------------------------------- CULL_THE_WEAK ------------------------------------
    private fun cullTheWeakEnchantment(victim: LivingEntity, level: Int): Double {
        val hasSlowness = victim.hasPotionEffect(PotionEffectType.SLOWNESS)
        val hasWeakness = victim.hasPotionEffect(PotionEffectType.WEAKNESS)
        val hasFatigue = victim.hasPotionEffect(PotionEffectType.MINING_FATIGUE)

        return if (hasSlowness || hasWeakness || hasFatigue) {
            var damage = 0.0
            if (hasSlowness) { damage += 1.0 }
            if (hasWeakness) { damage += 1.0 }
            if (hasFatigue) { damage += 1.0 }
            level * (1.0 + damage)
        } else {
            0.0
        }
    }
    // ------------------------------- DECAYING_TOUCH ------------------------------------
    private fun decayingTouchEnchantment(victim: LivingEntity, level: Int) {
       victim.addPotionEffect(PotionEffect(PotionEffectType.WITHER, (20 * (2 + (level * 2))), 0))
    }
    // ------------------------------- DOUSE ------------------------------------
    private fun douseEnchantment(victim: LivingEntity, level: Int): Double {
        victim.fireTicks = 0
        if (victim is Blaze || victim is MagmaCube) {
            return (level * 2.5) + 2.5
        }
        return 0.0
    }
    // ------------------------------- ECHO ------------------------------------
    private fun echoEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        // Prevent recursive call
        if (victim.scoreboardTags.contains(EntityTags.ECHO_STRUCK)) {
            victim.scoreboardTags.remove(EntityTags.ECHO_STRUCK)
            return
        }
        // Prevent Spam
        if (attacker is Player && attacker.attackCooldown < 0.99) return
        if ((0..100).random() < level * 20) {
            if (!victim.isDead) {
                // Swing
                victim.addScoreboardTag(EntityTags.ECHO_STRUCK)
                attacker.attack(victim)
                // Particles
                with(victim.world) {
                    spawnParticle(
                        Particle.SWEEP_ATTACK,
                        victim.location.clone().add(0.0, 0.25, 0.0),
                        3,
                        0.05,
                        0.05,
                        0.05
                    )
                    playSound(victim.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.7F)
                }
            }
        }
    }
    // ------------------------------- EXPLODING ------------------------------------
    private fun explodingEnchantment(victim: LivingEntity, level: Int) {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA, Color.ORANGE)
        with(victim.world) {
            // Particles
            spawnParticle(Particle.FLASH, victim.location, 2, 0.2, 0.2, 0.2)
            spawnParticle(Particle.LAVA, victim.location, 25, 1.5, 1.0, 1.5)
            // Fireball
            (spawnEntity(victim.location, EntityType.FIREBALL) as Fireball).also {
                it.setIsIncendiary(false)
                it.yield = 0.0F
                it.direction = Vector(0.0, -3.0, 0.0)
            }
            // Firework
            (spawnEntity(victim.location, EntityType.FIREWORK_ROCKET) as Firework).also {
                val newMeta = it.fireworkMeta
                newMeta.power = level * 30
                newMeta.addEffect(
                    FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(randomColors.random())
                        .withFade(randomColors.random()).trail(true).flicker(true).build()
                )
                it.fireworkMeta = newMeta
                it.velocity = Vector(0.0, -3.0, 0.0)
            }
        }
    }
    // ------------------------------- FEARFUL_FINISHER ------------------------------------
    private fun fearfulFinisherEnchantment(victim: LivingEntity, level: Int) {
        val killer = victim.killer ?: return
        val vector = killer.eyeLocation.direction.clone().normalize()
        vector.y = 0.0
        vector.normalize().multiply((2 * level) + 2)
        val newLocation = victim.location.clone().add(vector).toHighestLocation(HeightMap.MOTION_BLOCKING_NO_LEAVES)
        with(victim) {
            world.playSound(location, Sound.ENTITY_VEX_CHARGE, 2.5F, 0.5F)
            world.spawnParticle(Particle.WITCH, newLocation, 35, 0.05, 0.5, 0.05)
        }

        victim.getNearbyEntities(3.5, 2.0, 3.5).filterIsInstance<Creature>().forEach {
            it.world.spawnParticle(Particle.WITCH, it.location, 10, 0.05, 0.5, 0.05)
            it.pathfinder.stopPathfinding()
            it.pathfinder.moveTo(newLocation)
        }

    }

    // ------------------------------- FREEZING_ASPECT ------------------------------------
    private fun freezingAspectEnchantment(victim: LivingEntity, level: Int) {
        with(victim) {
            if (freezeTicks <= 50) {
                addOdysseyEffect(EffectTags.FREEZING, 8 * 20, level * 1)
                world.spawnParticle(Particle.SNOWFLAKE, this@with.location, 30, 0.25, 0.5, 0.25)
            }
        }
    }
    // ------------------------------- FROG_FRIGHT ------------------------------------
    private fun frogFrightEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        victim.also {
            it.velocity.multiply(0.9)
            val tongueLashVector = attacker.location.clone().subtract(it.location).toVector().normalize().multiply(1.1 + (level * 0.1))
            val frogFrightTask = FrogFrightTask(victim, tongueLashVector.multiply(-1.0))
            frogFrightTask.runTaskLater(Odyssey.instance, 9)
            it.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 3, 1))
            it.damage(1.0)
        }

        // Particles
        with(victim.world) {
            spawnParticle(Particle.COMPOSTER, victim.location, 15, 0.25, 0.25, 0.25)
            playSound(victim.location, Sound.ENTITY_FROG_EAT, 2.5F, 0.7F)
        }

    }
    // ------------------------------- FROSTY_FUSE ------------------------------------
    private fun frostyFuseEnchantment(victim: LivingEntity, enchantmentStrength: Int) {
        // Victim Effects
        with(victim) {
            if (!scoreboardTags.contains(EffectTags.FROSTY_FUSED)) {
                addScoreboardTag(EffectTags.FROSTY_FUSED)
                val task = FrostyFuseTask(victim, enchantmentStrength * 1, 5 * 4)
                task.runTaskTimer(Odyssey.instance, 5, 5)
            }
        }

    }
    // ------------------------------- GRAVITY_WELL ------------------------------------
    private fun gravityWellEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ) {
        // Victim Effects
        with(victim) {
            // Particles
            world.playSound(location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.75F, 0.3F)
            world.spawnParticle(Particle.WHITE_ASH, location, 55, 1.0, 0.5, 1.0)
            // Task
            addScoreboardTag(EffectTags.GRAVITY_WELLED)
            val modifier = (level * 1) + 1
            val maxCount = ((level * 2) + 2) * 2
            val task = GravitySingularityTask(victim, attacker, modifier, maxCount)
            task.runTaskTimer(Odyssey.instance, 0, 10)
        }

    }
    // ------------------------------- GUARDING_STRIKE ------------------------------------
    private fun guardingStrikeEnchantment(
        attacker: LivingEntity,
        level: Int) {
        with(attacker) {
            // TODO: Add proper effect that adds raw armor
            val isCrouching = attacker is Player && attacker.isSneaking
            if (velocity.length() < 0.2 || isCrouching) {
                addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, (3 + (level * 2)) * 20, 0))
            }
            // Particles and Sounds
            world.spawnParticle(Particle.ENCHANTED_HIT, location, 35, 1.0, 0.5, 1.0)
            world.spawnParticle(Particle.ELECTRIC_SPARK, location, 35, 1.0, 0.5, 1.0)
            world.playSound(location, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.5F, 0.5F)
            world.playSound(location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
        }
    }
    // ------------------------------- GUST ------------------------------------
    private fun gustEnchantment(
        vector: Vector,
        level: Int
    ): Vector {
        val mag = vector.length()
        val upVector = vector.normalize().clone()
        val newVector = upVector.setX(0.0).setY(1.0).setZ(0.0).multiply(mag * level)
        return newVector
    }
    // ------------------------------- HEMORRHAGE ------------------------------------
    private fun hemorrhageEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        victim.addOdysseyEffect(EffectTags.HEMORRHAGING, 9, level)
        with(victim.world) {
            playSound(victim.location, Sound.BLOCK_NETHER_SPROUTS_PLACE, 2.5F, 0.9F)
            spawnParticle(Particle.CRIT, victim.location, 35, 1.0, 0.5, 1.0)
        }
    }
    // ------------------------------- ILLUCIDATION ------------------------------------
    private fun illucidationEnchantment(victim: LivingEntity, level: Int, isCrit: Boolean): Int {
        var illucidationDamage = 0
        if (victim.isGlowing) {
            illucidationDamage += (level + 1)
            if (isCrit) {
                illucidationDamage * 2
                victim.isGlowing = false
                if (victim.hasPotionEffect(PotionEffectType.GLOWING)) {
                    victim.removePotionEffect(PotionEffectType.GLOWING)
                }
            }
            with(victim.world) {
                playSound(victim.location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 2.5F, 1.5F)
                spawnParticle(Particle.ELECTRIC_SPARK, victim.location, 55, 1.0, 0.5, 1.0)
            }
        }
        return illucidationDamage
    }
    // ------------------------------- RUPTURING_STRIKE ------------------------------------
    private fun rupturingStrikeEnchantment(attacker: LivingEntity, victim: LivingEntity, damage: Double, level: Int): Double {
        if (victim.isDead) return 0.0
        val fullCharge = if ((attacker is Player) && attacker.attackCooldown > 0.99) { true } else attacker !is Player
        // Prevent Spam
        if (!fullCharge) return 0.0

        var rupturingDamage = 0.0
        with(victim) {
            if (scoreboardTags.contains(EffectTags.FULLY_RUPTURED)) {
                removeScoreboardTag(EffectTags.FULLY_RUPTURED)
                if (damage + 2 > level) {
                    rupturingDamage += level
                    health -= minOf(health, rupturingDamage)
                }
                world.playSound(victim.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 2.5F, 1.7F)
                world.spawnParticle(Particle.CRIT, victim.location, 25, 1.0, 0.5, 1.0)
                val blockData = Material.QUARTZ_BRICKS.createBlockData()
                world.spawnParticle(Particle.BLOCK, victim.location, 25, 0.95, 0.8, 0.95, blockData)
            }
            else if (scoreboardTags.contains(EffectTags.PARTLY_RUPTURED)) {
                removeScoreboardTag(EffectTags.PARTLY_RUPTURED)
                addScoreboardTag(EffectTags.FULLY_RUPTURED)
            }
            else {
                addScoreboardTag(EffectTags.PARTLY_RUPTURED)
            }
        }
        return rupturingDamage
    }
    // ------------------------------- TAR_N_DIP ------------------------------------
    private fun tarNDipEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        with(victim) {
            addOdysseyEffect(EffectTags.TARRED, 10 * 20, level * 1)
            world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 2.5F, 1.5F)
        }
    }
    // ------------------------------- DOUSE ------------------------------------
    private fun vitalEnchantment(isCrit: Boolean, level: Int): Double {
        if (isCrit) {
            return 1.0 * level
        }
        return 0.0
    }
    // ------------------------------- VOID_STRIKE ------------------------------------
    private fun voidStrikeEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ): Double {
        // Skip if not matching
        val voidStruckBy = victim.getIntTag(EntityTags.VOID_STRUCK_BY)
        if (voidStruckBy == null) {
            victim.setIntTag(EntityTags.VOID_STRUCK_BY, attacker.entityId)
        }
        else if (voidStruckBy != attacker.entityId) {
            return 0.0
        }
        // Get modifier and set voidDamage
        val modifier = victim.getIntTag(EntityTags.VOID_STRIKE_MODIFIER) ?: 0
        victim.setIntTag(EntityTags.VOID_STRIKE_MODIFIER, modifier + 1)
        val voidDamage = modifier * level
        // Reset void strike modifier
        if (modifier > 13) {
            victim.setIntTag(EntityTags.VOID_STRIKE_MODIFIER, 0)
            victim.removeTag(EntityTags.VOID_STRUCK_BY)
        }
        // Particles and Sounds
        with(victim.world) {
            val location = victim.location
            spawnParticle(Particle.PORTAL, location, (modifier + 1) * 8, 1.15, 0.85, 1.15)
            spawnParticle(Particle.WAX_OFF, location, (modifier + 1) * 2, 1.0, 0.75, 1.0)
            spawnParticle(Particle.WITCH, location, (modifier + 1) * 5, 1.0, 0.75, 1.0)
            playSound(location, Sound.BLOCK_BEACON_DEACTIVATE, 1.5F, 0.5F)
            playSound(location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.7F, 0.2F)
            playSound(location, Sound.ENTITY_ENDER_EYE_DEATH, 3.5F, 0.4F)
        }

        // Damage
        return voidDamage * 1.0
    }
    // ------------------------------- WHIRLWIND ------------------------------------
    private fun whirlwindEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int
    ) {
        // Entity list
        val nearbyEntities = attacker.world.getNearbyLivingEntities(attacker.location, 1.5).filter {
            it != attacker && it != victim
        }
        // Particles
        with(attacker.world) {
            spawnParticle(Particle.EXPLOSION, attacker.location, 10, 1.25, 0.75, 1.25)
            playSound(attacker.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
            playSound(attacker.location, Sound.ITEM_SHIELD_BLOCK, 1.2F, 0.6F)
            playSound(attacker.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.2F, 0.6F)
        }
        // Damage Calculation
        val whirlDamage = damage * ((level * 3) * 0.1)
        nearbyEntities.forEach {
            it.damage(whirlDamage, attacker)
            it.velocity = it.location.clone().subtract(attacker.location).toVector().normalize()
                .multiply(0.8 + (0.15 * level))
        }
    }

}