package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.effects.OdysseyEffectsHandler
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.tasks.ArcaneCellTask
import me.shadowalzazel.mcodyssey.tasks.FrogFrightTask
import me.shadowalzazel.mcodyssey.tasks.FrostyFuseTask
import me.shadowalzazel.mcodyssey.tasks.GravityWellTask
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

object MeleeListeners : Listener {

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

        // Loop for all enchants
        for (enchant in weapon.enchantments) {
            // When match
            when (enchant.key) {
                OdysseyEnchantments.ARCANE_CELL -> {
                    if (cooldownManager(attacker, "Arcane Cell", arcaneCellCooldown, 5.25)) {
                        arcaneCellEnchantment(victim, enchant.value)
                    }
                }
                OdysseyEnchantments.ASPHYXIATING_ASSAULT -> {
                    event.damage += asphyxiatingAssaultEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.BACKSTABBER -> {
                    if (cooldownManager(attacker, "Backstabber", backstabberCooldown, 3.25)) {
                        event.damage += backstabberEnchantment(attacker, victim, enchant.value)
                    }
                }
                OdysseyEnchantments.BANE_OF_THE_ILLAGER -> {
                    event.damage += baneOfTheIllagerEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.BANE_OF_THE_SEA -> {
                    event.damage += baneOfTheSeaEnchantment(attacker, victim, enchant.value)
                }
                OdysseyEnchantments.BANE_OF_THE_SWINE -> {
                    event.damage += baneOfTheSwineEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.BUZZY_BEES -> {
                    if (cooldownManager(attacker, "Buzzy Bees", buzzyBeesCooldown, 4.25)) {
                        buzzyBeesEnchantment(victim, enchant.value)
                    }
                }
                OdysseyEnchantments.COMMITTED -> {
                    event.damage += committedEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.CULL_THE_WEAK -> {
                    event.damage += cullTheWeakEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.DECAYING_TOUCH -> {
                    decayingTouchEnchantment(victim, enchant.value)
                }
                OdysseyEnchantments.DOUSE -> {
                    douseEnchantment(victim, enchant.value)
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
                    event.damage += illucidationEnchantment(victim, enchant.value, event.isCritical)
                }
                OdysseyEnchantments.RUPTURING_STRIKE -> {
                    event.damage -= rupturingStrikeEnchantment(attacker, victim, event.damage, enchant.value)
                }
                OdysseyEnchantments.SPORING_ROT -> {
                    sporingRotEnchantment(victim, enchant.value) // MORE STACKS -> MORE INSTANCES
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
        val weapon = killer.equipment?.itemInMainHand

        // Loop for all enchants
        for (enchant in weapon!!.enchantments) {
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

    // -------------------------- ASPHYXIATING_ASSAULT -------------------------------
    private fun asphyxiatingAssaultEnchantment(
        victim: LivingEntity,
        level: Int
    ): Double {
        victim.remainingAir -= 20 * ((level * 2) + 3)
        if (victim.remainingAir < 20) return 3.0
        return 0.0
    }

    // ------------------------------- ARCANE_CELL ------------------------------------
    private fun arcaneCellEnchantment(victim: LivingEntity, level: Int) {
        with(victim) {
            if (!scoreboardTags.contains(EffectTags.ARCANE_JAILED)) {
                addPotionEffects(
                    listOf(
                        PotionEffect(PotionEffectType.SLOW, (20 * ((level * 2) + 2)), 4),
                        PotionEffect(PotionEffectType.SLOW_DIGGING, (20 * ((level * 2) + 2)), 3)
                    )
                )
                addScoreboardTag(EffectTags.ARCANE_JAILED)
                // TODO: Make circle Particles and Sound
                world.spawnParticle(Particle.SPELL_WITCH, location, 35, 1.0, 0.5, 1.0)
                world.playSound(location, Sound.ENTITY_VEX_CHARGE, 1.5F, 0.5F)
                ArcaneCellTask(victim, location, (2 + (level * 2)) * 4).runTaskTimer(Odyssey.instance, 5, 5)
            }
        }

    }
    // ------------------------------- BACKSTABBER ------------------------------------
    private fun backstabberEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        enchantmentStrength: Int
    ): Int {
        val victimTarget = victim.getTargetEntity(4)
        val isInvisible = attacker.isInvisible || attacker.hasPotionEffect(PotionEffectType.INVISIBILITY)
        // Looking more than 90-deg (1.57-rads) away from attacker
        val isNotTarget = victimTarget != attacker || victim.eyeLocation.direction.angle(attacker.eyeLocation.direction) > 1.5708

        if (isInvisible || isNotTarget) {
            // Particles and sounds
            with(victim.world) {
                spawnParticle(Particle.CRIT_MAGIC, victim.location, 25, 0.5, 0.5, 0.5)
                spawnParticle(Particle.WARPED_SPORE, victim.location, 25, 0.5, 0.5, 0.5)
                spawnParticle(Particle.ELECTRIC_SPARK, victim.location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT, victim.location, 15, 0.5, 0.5, 0.5)
                playSound(victim.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
            }
            // Damage
            return (3 + (enchantmentStrength * 3))
        }
        return 0
    }
    // ------------------------------- BANE_OF_THE_ILLAGER ------------------------------------
    private fun baneOfTheIllagerEnchantment(
        victim: LivingEntity,
        enchantmentStrength: Int
    ): Double {
        if (victim is Raider) {
            return enchantmentStrength.toDouble() * 2.5
        }
        return 0.0
    }
    // ------------------------------- BANE_OF_THE_SEA ------------------------------------
    private fun baneOfTheSeaEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        enchantmentStrength: Int
    ): Double {
        if (victim.isInWaterOrRainOrBubbleColumn || victim is WaterMob || victim.isSwimming || attacker.isInWaterOrRainOrBubbleColumn) {
            return enchantmentStrength.toDouble() * 2.0
        }
        return 0.0
    }
    // ------------------------------- BANE_OF_THE_SWINE ------------------------------------
    private fun baneOfTheSwineEnchantment(
        victim: LivingEntity,
        enchantmentStrength: Int
    ): Double {
        if (victim is PiglinAbstract || victim is Pig || victim is Hoglin) {
            return enchantmentStrength.toDouble() * 2.5
        }
        return 0.0
    }
    // ------------------------------- BUZZY_BEES ------------------------------------
    private fun buzzyBeesEnchantment(victim: LivingEntity, enchantmentStrength: Int) {
        // Spawn Bee if not low
        with(victim) {
            if (!isDead) {
                (world.spawnEntity(victim.location.clone().add((-50..50).random() * 0.1, (30..80).random() * 0.1, (-50..50).random() * 0.1), EntityType.BEE) as Bee).also {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.SPEED, 10 * 20, enchantmentStrength - 1),
                            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, enchantmentStrength - 1),
                            PotionEffect(PotionEffectType.FAST_DIGGING, 10 * 20, 0),
                            PotionEffect(PotionEffectType.ABSORPTION, 10 * 20, enchantmentStrength - 1)
                        )
                    )
                    it.target = this@with
                }
            }
            OdysseyEffectsHandler.honeyedEffect(mutableListOf(this@with), ((3 * enchantmentStrength) + 3) * 2)
            world.playSound(location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
        }
    }
    // ------------------------------- COMMITTED ------------------------------------
    private fun committedEnchantment(victim: LivingEntity, enchantmentStrength: Int): Int {
        return if (victim.health < victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value * 0.4) {
            enchantmentStrength + 1
        } else {
            0
        }
    }
    // ------------------------------- CULL_THE_WEAK ------------------------------------
    private fun cullTheWeakEnchantment(victim: LivingEntity, enchantmentStrength: Int): Double {
        val hasSlowness = victim.hasPotionEffect(PotionEffectType.SLOW)
        val hasWeakness = victim.hasPotionEffect(PotionEffectType.WEAKNESS)
        val hasFatigue = victim.hasPotionEffect(PotionEffectType.SLOW_DIGGING)

        return if (hasSlowness || hasWeakness || hasFatigue) {
            var damage = 0.0
            if (hasSlowness) { damage += 1.0 }
            if (hasWeakness) { damage += 1.0 }
            if (hasFatigue) { damage += 1.0 }
            enchantmentStrength * (1.0 + damage)
        } else {
            0.0
        }
    }
    // ------------------------------- DECAYING_TOUCH ------------------------------------
    private fun decayingTouchEnchantment(victim: LivingEntity, level: Int) {
       victim.addPotionEffect(PotionEffect(PotionEffectType.WITHER, (20 * ((level * 2))), 0))
    }
    // ------------------------------- DOUSE ------------------------------------
    private fun douseEnchantment(victim: LivingEntity, level: Int) {
        with(victim) {
            OdysseyEffectsHandler.dousedEffect(mutableListOf(this@with), 10, level * 1)
            world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 2.5F, 1.5F)
        }
    }
    // ------------------------------- ECHO ------------------------------------
    private fun echoEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        // Prevent recursive call
        if (victim.scoreboardTags.contains(EntityTags.ECHO_STRUCK)) {
            victim.scoreboardTags.remove(EntityTags.ECHO_STRUCK)
            return
        }
        if ((0..100).random() < level * 20) {
            if (!victim.isDead) {
                // Swing
                attacker.swingOffHand()
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
            (spawnEntity(victim.location, EntityType.FIREWORK) as Firework).also {
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
        val vector = victim.killer!!.eyeLocation.direction.clone().normalize()
        vector.y = 0.0
        vector.normalize().multiply((2 * level) + 2)
        val newLocation = victim.location.clone().add(vector).toHighestLocation(HeightMap.MOTION_BLOCKING_NO_LEAVES)
        with(victim) {
            world.playSound(location, Sound.ENTITY_VEX_CHARGE, 2.5F, 0.5F)
            world.spawnParticle(Particle.SPELL_WITCH, newLocation, 35, 0.05, 0.5, 0.05)
        }

        victim.getNearbyEntities(3.5, 2.0, 3.5).filterIsInstance<Creature>().forEach {
            it.world.spawnParticle(Particle.SPELL_WITCH, it.location, 10, 0.05, 0.5, 0.05)
            it.pathfinder.stopPathfinding()
            it.pathfinder.moveTo(newLocation)
        }

    }

    // ------------------------------- FREEZING_ASPECT ------------------------------------
    private fun freezingAspectEnchantment(victim: LivingEntity, level: Int) {
        with(victim) {
            if (freezeTicks <= 50) {
                OdysseyEffectsHandler.freezingEffect(mutableListOf(this@with), 8, level * 1)
                world.spawnParticle(Particle.SNOWFLAKE, this@with.location, 25, 1.0, 0.5, 1.0)
            }
        }
    }
    // ------------------------------- FROG_FRIGHT ------------------------------------
    private fun frogFrightEnchantment(attacker: LivingEntity, victim: LivingEntity, enchantmentStrength: Int) {
        victim.also {
            it.velocity.multiply(0.9)
            val tongueLashVector = attacker.location.clone().subtract(it.location).toVector().normalize().multiply(1.1 + (enchantmentStrength * 0.1))
            val frogFrightTask = FrogFrightTask(victim, tongueLashVector.multiply(-1.0))
            frogFrightTask.runTaskLater(Odyssey.instance, 9)
            it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 3, 1))
            it.damage(1.0)
        }

        // Particles
        with(victim.world) {
            spawnParticle(Particle.TOWN_AURA, victim.location, 15, 0.25, 0.25, 0.25)
            playSound(victim.location, Sound.ENTITY_FROG_EAT, 2.5F, 0.7F)
        }

    }
    // ------------------------------- FROSTY_FUSE ------------------------------------
    private fun frostyFuseEnchantment(victim: LivingEntity, enchantmentStrength: Int) {
        // Victim Effects
        with(victim) {
            if (!scoreboardTags.contains(EffectTags.FROSTY_FUSED)) {
                addScoreboardTag(EffectTags.FROSTY_FUSED)
                val frostedFuseTask = FrostyFuseTask(victim, enchantmentStrength * 1, 5 * 4)
                frostedFuseTask.runTaskTimer(Odyssey.instance, 5, 5)
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
            val gravityWellTask =
                GravityWellTask(victim, attacker, (level * 1) + 1, ((level * 2) + 2) * 2)
            gravityWellTask.runTaskTimer(Odyssey.instance, 0, 10)
        }

    }
    // ------------------------------- GUARDING_STRIKE ------------------------------------
    private fun guardingStrikeEnchantment(attacker: LivingEntity, enchantmentStrength: Int) {
        with(attacker) {
            // TODO: Add proper effect that adds raw armor
            val isCrouching = attacker is Player && attacker.isSneaking
            if (velocity.length() < 0.2 || isCrouching) {
                addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (3 + (enchantmentStrength * 2)) * 20, 0))
            }
            // Particles and Sounds
            world.spawnParticle(Particle.SUSPENDED, location, 35, 1.0, 0.5, 1.0)
            world.spawnParticle(Particle.ELECTRIC_SPARK, location, 35, 1.0, 0.5, 1.0)
            world.playSound(location, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.5F, 0.5F)
            world.playSound(location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
        }
    }
    // ------------------------------- HEMORRHAGE ------------------------------------
    private fun hemorrhageEnchantment(victim: LivingEntity, enchantmentStrength: Int) {
        OdysseyEffectsHandler.hemorrhagingEffect(mutableListOf(victim), enchantmentStrength * 1)
        with(victim.world) {
            playSound(victim.location, Sound.BLOCK_NETHER_SPROUTS_PLACE, 2.5F, 0.9F)
            spawnParticle(Particle.CRIT, victim.location, 35, 1.0, 0.5, 1.0)
        }
    }
    // ------------------------------- ILLUCIDATION ------------------------------------
    private fun illucidationEnchantment(victim: LivingEntity, enchantmentStrength: Int, isCrit: Boolean): Int {
        var illucidationDamage = 0
        if (victim.isGlowing) {
            illucidationDamage += (enchantmentStrength + 1)
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
    private fun rupturingStrikeEnchantment(attacker: LivingEntity, victim: LivingEntity, damage: Double, enchantmentStrength: Int): Double {
        val fullCharge = if ((attacker is Player) && attacker.attackCooldown > 0.99) { true } else attacker !is Player
        // Prevent Spam
        if (!fullCharge) return 0.0

        var rupturingDamage = 0.0
        with(victim) {
            if (scoreboardTags.contains(EffectTags.FULLY_RUPTURED)) {
                scoreboardTags.remove(EffectTags.FULLY_RUPTURED)
                if (damage + 2 > enchantmentStrength) {
                    rupturingDamage += enchantmentStrength
                    health -= minOf(health, rupturingDamage)
                }
                world.playSound(victim.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 2.5F, 1.7F)
                world.spawnParticle(Particle.CRIT, victim.location, 25, 1.0, 0.5, 1.0)
                world.spawnParticle(Particle.BLOCK_CRACK, victim.location, 25, 0.95, 0.8, 0.95, Material.QUARTZ_BRICKS.createBlockData())
            }
            else if (scoreboardTags.contains(EffectTags.PARTLY_RUPTURED)) {
                scoreboardTags.remove(EffectTags.PARTLY_RUPTURED)
                scoreboardTags.add(EffectTags.FULLY_RUPTURED)
            } else {
                scoreboardTags.add(EffectTags.PARTLY_RUPTURED)
            }
            damage
        }
        return rupturingDamage
    }
    // ------------------------------- DECAYING_TOUCH ------------------------------------
    private fun sporingRotEnchantment(victim: LivingEntity, level: Int) {
        with(victim) {
            OdysseyEffectsHandler.rottingEffect(mutableListOf(this@with), 12, level * 1)
            world.playSound(location, Sound.BLOCK_BIG_DRIPLEAF_TILT_UP, 2.5F, 0.9F)
        }
    }
    // ------------------------------- VOID_STRIKE ------------------------------------
    // TODO: REDO
    private fun voidStrikeEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        enchantmentStrength: Int
    ): Int {
        // Variables
        var voidDamage = 0
        var voidTouched = false
        var voidConflict = false
        // Values
        val victimScoreboardTags = victim.scoreboardTags
        val voidStrikeIDs = voidStrikeCooldown.keys.toList()
        val voidDamagerID: UUID = attacker.uniqueId

        // Initial Conflict Check
        if ("Void_Touched" in victimScoreboardTags) {
            voidTouched = true
            for (tag in victimScoreboardTags) {
                for (someID in voidStrikeIDs) {
                    if (tag == "Void_Touched_By_${someID}" && tag != "Void_Touched_By_${voidDamagerID}") {
                        voidConflict = true
                        break
                    }
                }
                if (voidConflict) {
                    break
                }
            }
        }
        // Remove all void related tags if conflict
        if (voidConflict) {
            val tagsToRemove = mutableListOf<String>()
            for (tag in victimScoreboardTags) {
                val voidTouchedTagPrefix = tag.subSequence(0..11).toString()
                val voidStruckTagPrefix = tag.subSequence(0..10).toString()
                if ((voidTouchedTagPrefix == "Void_Touched") || (voidStruckTagPrefix == "Void_Struck")) {
                    tagsToRemove.add(tag)
                }
            }
            for (tag in tagsToRemove) {
                victim.scoreboardTags.remove(tag)
            }
        }

        if (!voidConflict) {
            // Check if already voided
            if (voidTouched) {
                var voidStruckValue: Int? = null
                for (tag in victimScoreboardTags) {
                    if (tag.length > 10) {
                        // Change Void Struck tag
                        val voidStruckTagPrefix = tag.subSequence(0..10).toString()
                        if (voidStruckTagPrefix == "Void_Struck") {
                            val tagLastIndex = tag.lastIndex
                            voidStruckValue = tag[tagLastIndex].toString().toInt()
                            break
                        }
                    }
                }
                // Void Struck value to damage and update tag
                if (voidStruckValue != null) {
                    if (voidStruckValue == 9) {
                        victim.scoreboardTags.remove("Void_Struck_${voidStruckValue}")
                        victim.scoreboardTags.add("Void_Struck_0")
                    } else {
                        victim.scoreboardTags.remove("Void_Struck_${voidStruckValue}")
                        victim.scoreboardTags.add("Void_Struck_${voidStruckValue + 1}")
                    }
                    voidDamage = (enchantmentStrength * (voidStruckValue + 1))
                }
            }
            // Add tags if not voided
            else {
                (victim.scoreboardTags).also {
                    it.add("Void_Touched")
                    it.add("Void_Touched_By_${voidDamagerID}")
                    it.add("Void_Struck_0")
                }
            }
            // Particles and Sounds
            with(victim.world) {
                val someLocation = victim.location
                spawnParticle(Particle.PORTAL, someLocation, 85, 1.15, 0.85, 1.15)
                spawnParticle(Particle.WAX_OFF, someLocation, 45, 1.0, 0.75, 1.0)
                spawnParticle(Particle.SPELL_WITCH, someLocation, 50, 1.0, 0.75, 1.0)
                playSound(someLocation, Sound.BLOCK_BEACON_DEACTIVATE, 1.5F, 0.5F)
                playSound(someLocation, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.7F, 0.2F)
                playSound(someLocation, Sound.ENTITY_ENDER_EYE_DEATH, 3.5F, 0.4F)
            }
        }
        // Damage
        return voidDamage
    }
    // ------------------------------- WHIRLWIND ------------------------------------
    private fun whirlwindEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        enchantmentStrength: Int
    ) {
        // Entity list
        val nearbyEntities =
            attacker.world.getNearbyLivingEntities(attacker.location, 1.5).filter { it != attacker && it != victim }
        // Particles
        with(attacker.world) {
            spawnParticle(Particle.EXPLOSION_LARGE, attacker.location, 10, 1.25, 0.75, 1.25)
            playSound(attacker.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
            playSound(attacker.location, Sound.ITEM_SHIELD_BLOCK, 1.2F, 0.6F)
            playSound(attacker.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.2F, 0.6F)
            playSound(attacker.location, Sound.ITEM_TRIDENT_RIPTIDE_2, 1.2F, 1.2F)
        }
        // Damage Calculation
        val whirlDamage = damage * ((enchantmentStrength * 3) * 0.1)
        nearbyEntities.forEach {
            it.damage(whirlDamage, attacker)
            it.velocity = it.location.clone().subtract(attacker.location).toVector().normalize()
                .multiply(0.8 + (0.15 * enchantmentStrength))
        }
    }

}