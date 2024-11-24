package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.common.effects.EffectsManager
import me.shadowalzazel.mcodyssey.common.items.custom.Miscellaneous.getNameId
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.*
import me.shadowalzazel.mcodyssey.util.AttackHelper
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.UUID
import kotlin.math.log2
import kotlin.math.pow

object MeleeListeners : Listener, EffectsManager, AttackHelper {

    private val recallTargets: MutableMap<UUID, LivingEntity> = mutableMapOf()

    // Main function for enchantments relating to entity damage
    @Suppress("UnstableApiUsage")
    @EventHandler
    fun mainMeleeDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.entity !is LivingEntity) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        val attacker = if (event.damager is LivingEntity) {
            event.damager as LivingEntity
        } else {
            event.damageSource.causingEntity
        }
        if (event.damage <= 0.0) return // Prevent going through shields
        if (attacker !is LivingEntity) return
        if (attacker.equipment?.itemInMainHand?.hasItemMeta() == false) return
        val victim = event.entity as LivingEntity
        val weapon = attacker.equipment!!.itemInMainHand
        val power = if (attacker is Player) { attacker.attackCooldown.toDouble() } else { 1.0 }
        // Loop for all enchants
        for (enchant in weapon.enchantments) {
            when (enchant.key.getNameId()) {
                "arcane_cell" -> {
                    arcaneCellEnchantment(victim, enchant.value)
                }
                "asphyxiate" -> {
                    event.damage += asphyxiateEnchantment(victim, enchant.value) * power
                }
                "backstabber" -> {
                    event.damage += backstabberEnchantment(attacker, victim, enchant.value) * power
                }
                "budding" -> {
                    buddingEnchantment(victim, enchant.value) // MORE STACKS -> MORE INSTANCES
                }
                "buzzy_bees" -> {
                    buzzyBeesEnchantment(victim, enchant.value)
                }
                "cleave" -> {
                    cleaveEnchantment(victim, enchant.value)
                }
                "committed" -> {
                    event.damage += committedEnchantment(victim, enchant.value) * power
                }
                "conflagrate" -> {
                    conflagrateEnchantment(attacker, victim, enchant.value, event.damage)
                }
                "cull_the_weak" -> {
                    event.damage += cullTheWeakEnchantment(victim, enchant.value)  * power
                }
                "decay" -> {
                    decayEnchantment(victim, enchant.value)
                }
                "douse" -> {
                    event.damage += douseEnchantment(victim, enchant.value) * power
                }
                "echo" -> {
                    echoEnchantment(attacker, victim, enchant.value)
                }
                "freezing_aspect" -> {
                    freezingAspectEnchantment(victim, enchant.value)
                }
                "frog_fright" -> {
                    frogFrightEnchantment(attacker, victim, enchant.value)
                }
                "frosty_fuse" -> {
                    frostyFuseEnchantment(victim, enchant.value)
                }
                "gravity_well" -> {
                    gravityWellEnchantment(attacker, victim, enchant.value)
                }
                "guarding_strike" -> {
                    guardingStrikeEnchantment(attacker, enchant.value)
                }
                "hemorrhage" -> {
                    hemorrhageEnchantment(victim, enchant.value) // MORE STACKS -> MORE DAMAGE PER INSTANCE
                }
                "illucidation" -> {
                    event.damage += illucidationEnchantment(victim, enchant.value, event.isCritical) * power
                }
                "invocative" -> {
                    invocativeEnchantment(attacker, victim, event.damage, enchant.value)
                }
                "magic_touch" -> {
                    event.damage -= magicTouchEnchantment(attacker, victim, event.damage, enchant.value)
                }
                "pestilence" -> {
                    pestilenceEnchantment(attacker, victim, enchant.value)
                }
                "rupture" -> {
                    ruptureEnchantment(attacker, victim, event.damage, enchant.value)
                }
                "swap" -> {
                    swapEnchantment(attacker, victim, enchant.value)
                }
                "vital" -> {
                    event.damage += vitalEnchantment(event.isCritical, enchant.value) * power
                }
                "void_strike" -> {
                    event.damage += voidStrikeEnchantment(attacker, victim, enchant.value) * power
                }
                "whirlwind" -> {
                    whirlwindEnchantment(attacker, victim, event.damage, enchant.value)
                }
            }
        }
        // Check
        if (event.damage < 0.0) {
            event.damage = 0.0
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
        for (enchant in weapon.enchantments) {
            when (enchant.key.getNameId()) {
                "exploding" -> {
                    explodingEnchantment(killer, victim, enchant.value)
                }
                "fearful_finisher" -> {
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
        for (enchant in hitWeapon.enchantments) {
            when (enchant.key.getNameId()) {
                "gust" -> {
                    event.knockback = gustEnchantment(event.knockback, enchant.value)
                }
            }
        }
    }

    @EventHandler
    fun mainBLockDropHandler(event: BlockDropItemEvent) {
        if (event.items.isEmpty()) return
        val player = event.player
        if (!player.inventory.itemInMainHand.hasItemMeta()) return
        if (!player.inventory.itemInMainHand.itemMeta.hasEnchants()) return
        val hand = player.inventory.itemInMainHand
        // Loop
        for (enchant in hand.enchantments) {
            when (enchant.key.getNameId()) {
                "pluck_pocket" -> {
                    pluckPocketEnchantment(event)
                }
            }
        }
    }

    @EventHandler
    fun mainBlockBreakHandler(event: BlockBreakEvent) {
        if (!event.isDropItems) return
        val player = event.player
        if (!player.inventory.itemInMainHand.hasItemMeta()) return
        if (!player.inventory.itemInMainHand.itemMeta.hasEnchants()) return
        val hand = player.inventory.itemInMainHand
        // Loop
        for (enchant in hand.enchantments) {
            when (enchant.key.getNameId()) {
                "metabolic" -> {
                    metabolicEnchantment(event, enchant.value)
                }
            }
        }

    }

    private fun metabolicEnchantment(event: BlockBreakEvent, level: Int) {
        if (level < (0..9).random()) return

        val player = event.player
        if (player.foodLevel < 20) {
            player.foodLevel = minOf(player.foodLevel + (0..1).random(), 20)
        }
        else {
            player.saturation = minOf(player.saturation + 0.5F, 20F)
        }
    }

    private fun pluckPocketEnchantment(event: BlockDropItemEvent) {
        val items = event.items
        val player = event.player
        for (drop in items) {
            val overflow = player.inventory.addItem(drop.itemStack.clone())
            // Empty -> Success
            if (overflow.isEmpty()) {
                drop.remove()
            }
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    private fun asphyxiateEnchantment(
        victim: LivingEntity,
        level: Int
    ): Double {
        victim.remainingAir -= 20 * (level * 2)
        victim.world.spawnParticle(Particle.BUBBLE_POP, victim.location, 10, 0.25, 0.25, 0.25)
        victim.velocity = victim.velocity.multiply(0.0)
        if (victim.remainingAir < 20) return level * 1.0
        return 0.0
    }

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

    private fun backstabberEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ): Int {
        val victimTarget = victim.getTargetEntity(4)
        val isInvisible = attacker.isInvisible || attacker.hasPotionEffect(PotionEffectType.INVISIBILITY)
        // Looking more than 90-deg (1.57-rads) away from attacker
        // parallel angles mean looking same direction -> behind
        val behindTarget = attacker.eyeLocation.direction.angle(victim.eyeLocation.direction) < 1.5708
        if ((isInvisible && victimTarget != attacker) || behindTarget) {
            // Particles and sounds
            with(victim.world) {
                spawnParticle(Particle.CRIMSON_SPORE, victim.location, 15, 0.25, 0.25, 0.25)
                playSound(victim.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
            }
            // Damage
            return (2 + (level * 2))
        }
        return 0
    }

    private fun conflagrateEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int,
        damage: Double,
    ) {
        val extraDamage = damage * (level * 0.2)
        val task = ConflagrateTask(victim, extraDamage)
        task.runTaskLater(Odyssey.instance, 30)
    }

    private fun swapEnchantment(
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
    private fun buddingEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        with(victim) {
            addOdysseyEffect(EffectTags.BUDDING, 12 * 20, level * 1)
            world.playSound(location, Sound.BLOCK_BIG_DRIPLEAF_TILT_UP, 2.5F, 0.9F)
        }
    }

    private fun buzzyBeesEnchantment(
        victim: LivingEntity,
        level: Int) {
        if (victim.health < 4) return
        if (victim.scoreboardTags.contains(EffectTags.HONEYED)) return
        // Spawn Bee if not low
        with(victim) {
            if (!isDead) {
                (world.spawnEntity(victim.location.clone().add((-50..50).random() * 0.1, (30..80).random() * 0.1, (-50..50).random() * 0.1), EntityType.BEE) as Bee).also {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.SPEED, 10 * 20, level - 1),
                            PotionEffect(PotionEffectType.STRENGTH, 10 * 20, level - 1),
                            PotionEffect(PotionEffectType.HASTE, 10 * 20, 0)
                        )
                    )
                    it.target = this@with
                    it.isPersistent = false
                }
            }
            victim.addOdysseyEffect(EffectTags.HONEYED, 6 * 20, level)
            world.playSound(location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
        }
    }

    private fun cleaveEnchantment(victim: LivingEntity, level: Int) {
        val equipment = victim.equipment ?: return
        if (equipment.chestplate.hasItemMeta()) {
            equipment.chestplate.damage(level, victim)
        }
        if (equipment.leggings.hasItemMeta()) {
            equipment.leggings.damage(level, victim)
        }
        if (equipment.helmet.hasItemMeta()) {
            equipment.helmet.damage(level, victim)
        }
        if (equipment.chestplate.hasItemMeta()) {
            equipment.chestplate.damage(level, victim)
        }
        victim.shieldBlockingDelay += level * 20
    }

    private fun committedEnchantment(victim: LivingEntity, level: Int): Int {
        val maxHealth = victim.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        return if (victim.health < maxHealth * 0.4) {
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, victim.location, 10, 0.25, 0.25, 0.25)
            level + 1
        } else {
            0
        }
    }

    private fun cullTheWeakEnchantment(victim: LivingEntity, level: Int): Double {
        val hasSlowness = victim.hasPotionEffect(PotionEffectType.SLOWNESS)
        val hasWeakness = victim.hasPotionEffect(PotionEffectType.WEAKNESS)
        val hasFatigue = victim.hasPotionEffect(PotionEffectType.MINING_FATIGUE)
        return if (hasSlowness || hasWeakness || hasFatigue) {
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, victim.location, 10, 0.25, 0.25, 0.25)
            var damage = 0.0
            if (hasSlowness) { damage += 1.0 }
            if (hasWeakness) { damage += 1.0 }
            if (hasFatigue) { damage += 1.0 }
            level * (damage)
        } else {
            0.0
        }
    }

    private fun decayEnchantment(victim: LivingEntity, level: Int) {
       victim.addPotionEffect(PotionEffect(PotionEffectType.WITHER, (20 * (level * 4)), 0))
    }

    private fun douseEnchantment(victim: LivingEntity, level: Int): Double {
        if (victim is Blaze || victim is MagmaCube || victim is Enderman || victim.fireTicks > 0) {
            victim.fireTicks = 0
            return (level * 2.5) + 2.5
        }
        victim.fireTicks = 0
        return 0.0
    }

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

    @Suppress("UnstableApiUsage")
    private fun explodingEnchantment(killer: LivingEntity, victim: LivingEntity, level: Int) {
        val location = victim.location
        // Effects
        location.world.spawnParticle(Particle.EXPLOSION, location, 1, 0.0, 0.04, 0.0)
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 1.2F)
        // Damage
        val radius = level * 1.0
        for (entity in location.getNearbyLivingEntities(radius)) {
            if (entity == killer) return
            // distance square
            val distance = entity.location.distance(location)
            val power = (maxOf(radius - distance, 0.1 * level)).pow(2.0) + (maxOf(radius - distance, 0.1 * level)).times(1) + (radius / 2)
            val damageSource = DamageSource.builder(DamageType.EXPLOSION).build()
            entity.damage(power + level, damageSource)
        }
    }

    private fun fearfulFinisherEnchantment(victim: LivingEntity, level: Int) {
        val killer = victim.killer ?: return
        val vector = killer.eyeLocation.direction.clone().normalize()
        vector.y = 0.0
        vector.normalize().multiply(level * 4)
        val newLocation = victim.location.clone().add(vector).toHighestLocation(HeightMap.MOTION_BLOCKING_NO_LEAVES)
        with(victim) {
            world.playSound(location, Sound.ENTITY_VEX_CHARGE, 2.5F, 0.5F)
            world.spawnParticle(Particle.WITCH, newLocation, 35, 0.05, 0.5, 0.05)
        }
        victim.getNearbyEntities(3.5, 2.0, 3.5).filterIsInstance<Creature>().forEach {
            it.noDamageTicks -= (level * 0.1).toInt()
            it.world.spawnParticle(Particle.WITCH, it.location, 10, 0.05, 0.5, 0.05)
            it.pathfinder.stopPathfinding()
            it.pathfinder.moveTo(newLocation)
        }
    }

    private fun freezingAspectEnchantment(victim: LivingEntity, level: Int) {
        with(victim) {
            if (freezeTicks <= 50) {
                addOdysseyEffect(EffectTags.FREEZING, (level * 4) * 20, level)
                world.spawnParticle(Particle.SNOWFLAKE, this@with.location, 5, 0.05, 0.05, 0.05)
            }
        }
    }

    private fun frogFrightEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        victim.also {
            it.velocity.multiply(0.9)
            val pullVector = attacker.location.clone().subtract(it.location).toVector().normalize().multiply(1.1 + (level * 0.1))
            val frogFrightTask = FrogFrightTask(victim, pullVector.multiply(-1.0), level)
            frogFrightTask.runTaskLater(Odyssey.instance, 9)
            it.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 5, 1))
        }

        // Particles
        with(victim.world) {
            spawnParticle(Particle.COMPOSTER, victim.location, 15, 0.25, 0.25, 0.25)
            playSound(victim.location, Sound.ENTITY_FROG_EAT, 2.5F, 0.7F)
        }

    }

    private fun frostyFuseEnchantment(victim: LivingEntity, level: Int) {
        // Victim Effects
        with(victim) {
            if (!scoreboardTags.contains(EffectTags.FROSTY_FUSED)) {
                addScoreboardTag(EffectTags.FROSTY_FUSED)
                val task = FrostyFuseTask(victim, level * 1, 5 * 4)
                task.runTaskTimer(Odyssey.instance, 5, 5)
            }
        }

    }

    private fun gravityWellEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ) {
        // Particles
        victim.world.playSound(victim.location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.75F, 0.3F)
        victim.world.spawnParticle(Particle.WHITE_ASH, victim.location, 55, 1.0, 0.5, 1.0)
        // Task
        victim.addScoreboardTag(EffectTags.GRAVITY_WELLED)
        val modifier = (level * 1) + 1
        val maxCount = ((level * 2) + 1) * 2
        val task = GravitySingularityTask(victim, attacker, modifier, maxCount)
        task.runTaskTimer(Odyssey.instance, 0, 10)
    }

    private fun guardingStrikeEnchantment(
        attacker: LivingEntity,
        level: Int) {
        with(attacker) {
            val isCrouching = attacker is Player && attacker.isSneaking
            if (velocity.length() < 0.3 || isCrouching) {
                addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, (level * 5) * 20, 0))
                // Particles and Sounds
                world.spawnParticle(Particle.ENCHANTED_HIT, location, 35, 1.0, 0.5, 1.0)
                world.playSound(location, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.5F, 0.5F)
                world.playSound(location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
            }
        }
    }

    private fun gustEnchantment(
        vector: Vector,
        level: Int
    ): Vector {
        val mag = vector.length()
        val upVector = vector.normalize().clone()
        val newVector = upVector.setX(0.0).setY(1.0).setZ(0.0).multiply(mag * level)
        return newVector
    }

    private fun hemorrhageEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        victim.addOdysseyEffect(EffectTags.HEMORRHAGING, 9 * 20, level)
        with(victim.world) {
            playSound(victim.location, Sound.BLOCK_NETHER_SPROUTS_PLACE, 2.5F, 0.9F)
            spawnParticle(Particle.CRIT, victim.location, 35, 1.0, 0.5, 1.0)
        }
    }

    private fun illucidationEnchantment(victim: LivingEntity, level: Int, isCrit: Boolean): Int {
        var illucidationDamage = 0
        if (victim.isGlowing) {
            illucidationDamage += (level * 1)
            if (isCrit) {
                illucidationDamage * 2
                victim.isGlowing = false
                if (victim.hasPotionEffect(PotionEffectType.GLOWING)) {
                    victim.removePotionEffect(PotionEffectType.GLOWING)
                }
            }
            with(victim.world) {
                playSound(victim.location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 2.5F, 1.5F)
                spawnParticle(Particle.END_ROD, victim.location, 55, 1.0, 0.5, 1.0)
            }
        }
        return illucidationDamage
    }

    @Suppress("UnstableApiUsage")
    private fun magicTouchEnchantment(attacker: LivingEntity, victim: LivingEntity, damage: Double, level: Int): Double {
        val potency = 0.1 * level
        val damageSource = DamageSource.builder(DamageType.MAGIC).build()
        val magicDamage = damage * potency
        victim.damage(magicDamage, damageSource)
        victim.world.spawnParticle(Particle.WITCH, victim.location, 10, 0.25, 0.25, 0.25)
        return magicDamage
    }

    // PLAGUE BRINGER
    @Suppress("UnstableApiUsage")
    private fun plagueBringerEnchantment(victim: LivingEntity, level: Int) {
        if (!victim.hasPotionEffect(PotionEffectType.POISON)) return
        val potionEffect = victim.getPotionEffect(PotionEffectType.POISON) ?: return
        val location = victim.location
        val color = Color.fromRGB(135, 163, 99)
        val particle = Particle.ENTITY_EFFECT
        // Particles
        location.world.spawnParticle(particle, location, 10, 0.02, 0.1, 0.02, color)
        // Damage
        val amplifier = potionEffect.amplifier
        val efficiency = level * 0.2
        val timeInTicks = (potionEffect.duration * efficiency).toInt()
        for (entity in location.getNearbyLivingEntities(3.5)) {
            // Apply poison to non poisoned enemies
            if (!entity.hasPotionEffect(PotionEffectType.POISON)) {
                entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, timeInTicks, amplifier))
            } else {
                val damageSource = DamageSource.builder(DamageType.MAGIC).build()
                val damage = ((timeInTicks / 20) * (0.1 * (amplifier + 1))) * (efficiency)
                entity.damage(damage, damageSource)
            }
        }
    }

    private fun pestilenceEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        if (victim.activePotionEffects.isEmpty()) return
        val potionEffects = victim.activePotionEffects
        val location = victim.location
        val particle = Particle.ENTITY_EFFECT
        val potency = level * 0.2
        val spreadingEffects = mutableListOf<PotionEffect>()
        // Per Effect
        for (effect in potionEffects) {
            val color = effect.type.color
            location.world.spawnParticle(particle, location, 13, 0.03, 0.1, 0.03, color)
            val amplifier = effect.amplifier
            val timeInTicks = (effect.duration * potency).toInt()
            spreadingEffects.add(PotionEffect(effect.type, timeInTicks, amplifier))
        }
        // All Nearby Entities
        val nearbyEntities = location.getNearbyLivingEntities(4.0).filter { it != attacker}
        for (entity in nearbyEntities) {
            entity.addPotionEffects(spreadingEffects)
        }
    }

    @Suppress("UnstableApiUsage")
    private fun invocativeEnchantment(attacker: LivingEntity, victim: LivingEntity, damage: Double, level: Int) {
        val lastTarget = recallTargets[attacker.uniqueId]
        if (lastTarget == null) {
            recallTargets[attacker.uniqueId] = victim
            return
        }
        else if (lastTarget != victim) {
            val source = createPlayerDamageSource(attacker)
            recallTargets[attacker.uniqueId] = victim // Prevent recursion
            val recallDamage = damage * (level * 0.1)
            victim.damage(recallDamage, source)
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, victim.location, 25, 1.0, 0.5, 1.0)
        }
        else if (lastTarget == victim) {
            return
        }
    }

    private fun ruptureEnchantment(attacker: LivingEntity, victim: LivingEntity, damage: Double, level: Int): Double {
        if (victim.isDead) return 0.0
        val fullCharge = if ((attacker is Player) && attacker.attackCooldown > 0.99) { true } else attacker !is Player
        // Prevent Spam
        if (!fullCharge) return 0.0
        var rupturingDamage = 0.0
        with(victim) {
            if (scoreboardTags.contains(EffectTags.FULLY_RUPTURED)) {
                removeScoreboardTag(EffectTags.FULLY_RUPTURED)
                if (damage > level) {
                    rupturingDamage += level
                    health -= minOf(health, rupturingDamage)
                }
                world.playSound(victim.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 2.5F, 1.7F)
                val blockData = Material.TUFF_BRICKS.createBlockData()
                world.spawnParticle(Particle.BLOCK, victim.location, 15, 0.45, 0.8, 0.45, blockData)
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

    private fun vitalEnchantment(isCrit: Boolean, level: Int): Double {
        if (isCrit) {
            return 1.0 * level
        }
        return 0.0
    }

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
            victim.setIntTag(EntityTags.VOID_STRUCK_BY, attacker.entityId)
            return 0.0
        }
        // Get modifier and set voidDamage
        val modifier = victim.getIntTag(EntityTags.VOID_STRIKE_MODIFIER) ?: 0
        victim.setIntTag(EntityTags.VOID_STRIKE_MODIFIER, modifier + 1)
        val voidDamage = modifier * level
        // Reset void strike modifier
        if (modifier > 10) {
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
            spawnParticle(Particle.GUST, attacker.location, 10, 0.04, 0.04, 0.04)
            playSound(attacker.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
            playSound(attacker.location, Sound.ENTITY_WIND_CHARGE_THROW, 1.2F, 0.6F)
        }
        // Damage Calculation
        val whirlDamage = damage * (level * 0.3)
        nearbyEntities.forEach {
            it.damage(whirlDamage)
            val speed = 0.8 + (0.2 * level)
            val direction = it.location.clone().subtract(attacker.location).toVector().normalize()
            it.velocity = direction.multiply(speed)
        }
    }
    /*-----------------------------------------------------------------------------------------------*/
}