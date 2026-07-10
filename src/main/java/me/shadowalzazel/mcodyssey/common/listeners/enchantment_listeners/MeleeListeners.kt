package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.common.effects.EffectsManager
import me.shadowalzazel.mcodyssey.common.effects.StatusEffect
import me.shadowalzazel.mcodyssey.common.effects.StatusEffectManager.applyOdysseyEffect
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.*
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.pow

object MeleeListeners : Listener, EffectsManager, AttackHelper, EnchantmentManager, VectorParticles {

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── CONTEXTS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    // Everything any melee enchant might need
    private data class MeleeEnchantContext(
        val eventDamage: Double,      // the event final damage
        val attacker: LivingEntity,   // the one attacking
        val victim: LivingEntity,     // the one being hit
        val weapon: ItemStack,
        val attackPower: Double,
        val level: Int,
        val mods: DamageMods,
        val isCritical: Boolean
    )

    private class DamageMods {
        var flat: Float = 0.0f         // raw base added before percent
        var percent: Float = 0.0f      // summed, applied as (1 + percent)
        var postPercent: Float = 1.0f  // multiplied last (e.g. magic)
    }

    // --- damage modifiers: read base damage / state, mutate mods only ---
    private val meleeDamageEnchantmentsMap: Map<String, (MeleeEnchantContext) -> Unit> = mapOf(
        // --- percent increases (add to percent) ---
        "backstabber"      to { c -> c.mods.percent += backstabberEnchantment(c.attacker, c.victim, c.level) },
        "besiege"          to { c -> c.mods.percent += besiegeEnchantment(c.attacker, c.level) },
        "brutality_curse"  to { c -> c.mods.percent += brutalityCurseEnchantment(c.attacker, c.level) },
        "committed"        to { c -> c.mods.percent += committedEnchantment(c.victim, c.level) },
        "cull_the_weak"    to { c -> c.mods.percent += cullTheWeakEnchantment(c.victim, c.level) },
        "douse"            to { c -> c.mods.percent += douseEnchantment(c.victim, c.level) },
        "guarding_strike"  to { c -> c.mods.percent += guardingStrikeEnchantment(c.attacker, c.level) },
        "impetus"          to { c -> c.mods.percent += impetusEnchantment(c.attacker, c.level) },
        "illucidation"     to { c -> c.mods.percent += illucidationEnchantment(c.victim, c.level, c.isCritical) },
        "press_the_attack" to { c -> c.mods.percent += pressTheAttackEnchantment(c.victim, c.level) },
        "unyielding"       to { c -> c.mods.percent += unyieldingEnchantment(c.attacker, c.level) },
        "vital"            to { c -> c.mods.percent += vitalEnchantment(c.isCritical, c.level) },
        "void_strike"      to { c -> c.mods.percent += voidStrikeEnchantment(c.attacker, c.victim, c.level) },
        "vengeful"         to { c -> c.mods.percent += vengefulEnchantment(c.attacker, c.victim, c.level) },

        // --- flat (base damage changes) ---
        "life_force"       to { c -> c.mods.flat += lifeForceEnchantment(c.attacker, c.level) },

        // --- post-percent multipliers (subtract from the multiplier) ---
        "flame_edge"       to { c -> c.mods.postPercent -= flameEdgeEnchantment(c.attacker, c.victim, c.eventDamage, c.level) },
        "magic_aspect"     to { c -> c.mods.postPercent -= magicAspectEnchantment(c.attacker, c.victim, c.eventDamage, c.level) },
        "rupture"          to { c -> c.mods.postPercent -= ruptureEnchantment(c.attacker, c.victim, c.eventDamage, c.level) },
    )

    // --- side effects only, untouched ---
    private val meleeEffectEnchantmentsMap: Map<String, (MeleeEnchantContext) -> Unit> = mapOf(
        "aerosion_aspect"  to { c -> aerosionAspectEnchantment(c.victim, c.level) },
        "arcane_cell"      to { c -> arcaneCellEnchantment(c.victim, c.level) },
        "asphyxiate"       to { c -> asphyxiateEnchantment(c.victim, c.level) },
        "budding"          to { c -> buddingEnchantment(c.victim, c.level) },          // MORE STACKS -> MORE INSTANCES
        "buzzy_bees"       to { c -> buzzyBeesEnchantment(c.victim, c.level) },
        "cleave"           to { c -> cleaveEnchantment(c.victim, c.level) },
        "conflagrate"      to { c -> conflagrateEnchantment(c.attacker, c.victim, c.level, c.eventDamage) },
        "decay"            to { c -> decayEnchantment(c.victim, c.level) },
        "dematerialize"    to { c -> dematerializeEnchantment(c.attacker, c.victim, c.eventDamage, c.level) }, //TODO: Move
        "echo"             to { c -> echoEnchantment(c.attacker, c.victim, c.level) },
        "execution"        to { c -> executionEnchantment(c.attacker, c.victim, c.level) },
        "chain_lightning"  to { c -> chainLightningEnchantment(c.attacker, c.victim, c.eventDamage, c.level) },
        "freezing_aspect"  to { c -> freezingAspectEnchantment(c.victim, c.level) },
        "frog_fright"      to { c -> frogFrightEnchantment(c.attacker, c.victim, c.level) },
        "frosty_fuse"      to { c -> frostyFuseEnchantment(c.victim, c.level) },
        "gravity_well"     to { c -> gravityWellEnchantment(c.attacker, c.victim, c.level) },
        "hemorrhage"       to { c -> hemorrhageEnchantment(c.victim, c.eventDamage, c.level) },        // MORE STACKS -> MORE DAMAGE PER INSTANCE
        "invocative"       to { c -> invocativeEnchantment(c.attacker, c.victim, c.eventDamage, c.level) },
        "miscalibrate"     to { c -> miscalibrateEnchantment(c.victim, c.level) },
        "pestilence"       to { c -> pestilenceEnchantment(c.attacker, c.victim, c.level) },
        "swap"             to { c -> swapEnchantment(c.attacker, c.victim, c.level) },
        "whirlwind"        to { c -> whirlwindEnchantment(c.attacker, c.victim, c.eventDamage, c.level) },
    )

    // event.damage is calculated when this function is run, so it runs again after the mods are applied
    private fun processWeapon(
        event: EntityDamageByEntityEvent,
        attacker: LivingEntity,
        victim: LivingEntity,
        weapon: ItemStack,
        attackPower: Double,
        map: Map<String, (MeleeEnchantContext) -> Unit>,
        mods: DamageMods,
    ) {
        for ((enchant, level) in weapon.enchantments) {
            val handler = map[enchant.getNameId()] ?: continue
            handler(MeleeEnchantContext(
                event.damage,
                attacker,
                victim,
                weapon,
                attackPower,
                level,
                mods,
                event.isCancelled
            ))
        }
    }

    private val recallTargets: MutableMap<UUID, LivingEntity> = mutableMapOf()
    private val invocativePreviousDamage: MutableMap<UUID, Double> = mutableMapOf()
    private val invocativeLastTarget: MutableMap<UUID, UUID> = mutableMapOf()
    private val vengefulTargets: MutableMap<UUID, UUID> = mutableMapOf()
    private val ptaTasks = mutableMapOf<UUID, BukkitTask>()
    private val voidDotStates = mutableMapOf<UUID, VoidDotState>()

    private data class VoidDotState(
        val task: BukkitTask,
        val perTick: Double,
        var ticksRemaining: Int
    )


    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── HANDLERS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.HIGH)
    fun mainMeleeDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.entity !is LivingEntity) return
        // Caused by entity attack
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return

        val attacker = if (event.damager is LivingEntity) {
            event.damager as LivingEntity
        } else {
            event.damageSource.causingEntity
        }

        // Do item and power checks
        if (event.damage <= 0.0) return // Prevent going through shields
        if (attacker !is LivingEntity) return
        if (attacker.equipment?.itemInMainHand?.hasItemMeta() == false) return

        val victim = event.entity as LivingEntity
        val weapon = attacker.equipment!!.itemInMainHand
        val attackPower = if (attacker is Player) attacker.attackCooldown.toDouble() else 1.0

        // Set tags like vengeful (for now, Players)
        if (event.entity is Player) {
            vengefulTargets[event.entity.uniqueId] = event.damager.uniqueId
        }

        val mods = DamageMods()

        // (Base Damage + Base Flat Modifiers) * (1 + Percentage Modifiers) * postPercent
        // Damage modifiers take priority and are all gathered first...
        processWeapon(event, attacker, victim, weapon, attackPower, meleeDamageEnchantmentsMap, mods)

        // Damage Calculation Step
        // This changes the event damage context after the modifiers fired
        event.damage = (event.damage + mods.flat) * (1 + mods.percent) * mods.postPercent
        if (event.damage < 0.0) event.damage = 0.0

        // ...then side effect enchantments fire against the modified damage
        processWeapon(event, attacker, victim, weapon, attackPower, meleeEffectEnchantmentsMap, mods)
        // Finish
    }


    // TODO!!
    // Main function for shields/blocks
    //@EventHandler
    fun mainMeleeBlockHandler(event: EntityDamageByEntityEvent) {
        val attacker = if (event.damager is LivingEntity) {
            event.damager as LivingEntity
        } else {
            event.damageSource.causingEntity
        }
        val directEntity = event.damageSource.directEntity
        // Check if defender is alive
        val defender = event.entity
        if (event.entity !is LivingEntity) return
        defender as LivingEntity
        val mainHand = defender.equipment?.itemInMainHand ?: return


        // Effect Enchantments are next
        for (enchant in mainHand.enchantments) {
            when (enchant.key.getNameId()) {
                "mirror_force" -> {
                    mirrorForceEnchantment(defender, directEntity, enchant.value)
                }
            }
        }


    }


    // Main function for enchantments relating to entity deaths
    @EventHandler
    fun mainMeleeDeathHandler(event: EntityDeathEvent) {
        if (event.entity.killer !is LivingEntity) return
        val killer = event.entity.killer as LivingEntity
        if (killer.equipment?.itemInMainHand?.hasItemMeta() == false) return
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
                "plunder" -> {
                    plunderEnchantment(event)
                }
                "midas_curse" -> {
                    midasCurseEnchantment(event)
                }
            }
        }
    }

    // Main function for enchantments relating to knockbacks
    @EventHandler
    fun entityKnockBackHandler(event: EntityKnockbackByEntityEvent) {
        if (event.hitBy !is LivingEntity) return
        val attacker = event.hitBy as LivingEntity
        val hitWeapon = attacker.equipment?.itemInMainHand ?: return
        val originalKnockback = event.knockback.clone()
        // Loop
        for (enchant in hitWeapon.enchantments) {
            when (enchant.key.getNameId()) {
                "gust" -> {
                    event.knockback = gustEnchantment(originalKnockback, enchant.value)
                }
                "thunderous" -> {
                    thunderousEnchantment(attacker, event.entity, originalKnockback, enchant.value)
                }
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ─────────────────────────────── ENCHANTMENTS ─────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private fun aerosionAspectEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        with(victim.world) {
            playSound(victim.location, Sound.ENTITY_WIND_CHARGE_THROW, 2.5F, 0.9F)
            spawnParticle(Particle.SMALL_GUST, victim.location, 25, 1.0, 0.5, 1.0)
        }
        // Set Effect
        victim.applyOdysseyEffect(StatusEffect.AEROSION, 20 * 6, level * 1.0)
    }

    private fun tempestSplitterEnchantment(
        event: EntityDamageByEntityEvent,
        level: Int) {
        val victim = event.entity
        if (victim !is LivingEntity) return
        // Effects
        with(victim.world) {
            playSound(victim.location, Sound.BLOCK_HEAVY_CORE_BREAK, 2.5F, 0.9F)
        }
        // Set Stacks
        val stacks = victim.getIntTag(EntityTags.AEROSION_STACKS) ?: 0

        // Damage
        if (event.isCritical) {
            val critDamageAmp = (0.5 * level) * stacks
            event.damage *= (1.0 + (critDamageAmp))
            victim.setIntTag(EntityTags.AEROSION_STACKS, 0)
        } else {
            val damageAmp = (0.2 * level) * stacks
            event.damage *= (1.0 + (damageAmp))
        }
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


    private fun asphyxiateEnchantment(
        victim: LivingEntity,
        level: Int
    ) {
        victim.remainingAir -= 20 * (level * 2)
        victim.world.spawnParticle(Particle.BUBBLE_POP, victim.location, 14, 0.25, 0.25, 0.25)
        victim.velocity = victim.velocity.multiply(0.0) // Also reset their speed
        if (victim.remainingAir < 20) {
            val damageSource = DamageSource.builder(DamageType.DROWN).build()
            val drownDamage = level * 1.0
            victim.damage(drownDamage, damageSource)
        }
    }

    private fun backstabberEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ): Float {
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
            return level * 0.2F
        }
        return 0.0F

    }

    private fun besiegeEnchantment(
        attacker: LivingEntity,
        level: Int): Float {

        val isCrouching = attacker is Player && attacker.isSneaking
        val isNotMoving = attacker.velocity.length() < 0.095 || attacker.forwardsMovement.absoluteValue < 0.01
        if (isCrouching || isNotMoving) {
            // Effects
            attacker.world.playSound(attacker.location, Sound.ITEM_SPEAR_ATTACK, 0.7F, 0.7F)
            val blockData = Material.DEEPSLATE_BRICKS.createBlockData()
            attacker.world.spawnParticle(Particle.BLOCK, attacker.location, 20, 0.45, 0.5, 0.35, blockData)
            // Return damage
            return 0.10F * level
        }
        return 0.0F
    }

    private fun brutalityCurseEnchantment(
        attacker: LivingEntity,
        level: Int,
    ) : Float {
        val damageSource = DamageSource.builder(DamageType.GENERIC).build()
        val feedbackDamage = level * 1.0
        attacker.damage(feedbackDamage, damageSource)
        return 0.1F * level
    }

    // Other enchant ideas
    // MAYBE DO NEXT TIME YOU SNEAK???? OR TOGGLE WITH
    // or do dmg based on distance?
    private fun buddingEnchantment(
        victim: LivingEntity,
        level: Int) {
        // Effects
        with(victim) {
            addOdysseyEffect(EffectTags.ROTTING, 12 * 20, level * 1)
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
                            PotionEffect(PotionEffectType.STRENGTH, 10 * 20, level - 1)
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

    private fun chainLightningEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int): Double {
        val maxTargets = 2 + level
        val chainDamage = damage * 0.20
        val chainRadius = 5.0

        // Build the chain: start from the initial victim, bounce to nearby entities
        val chainTargets = mutableListOf<LivingEntity>()
        chainTargets.add(victim)

        var lastTarget: LivingEntity = victim
        repeat(maxTargets - 1) {
            val nextTarget = lastTarget.world
                .getNearbyLivingEntities(lastTarget.location, chainRadius)
                .filter { entity ->
                    entity != attacker && entity !is ArmorStand && entity !in chainTargets
                }
                .minByOrNull { it.location.distanceSquared(lastTarget.location) }
                ?: return@repeat

            chainTargets.add(nextTarget)
            lastTarget = nextTarget
        }

        // Deal electric (lightning) damage to all chained targets except the first
        // (the first victim already received the base hit)
        val damageSource = DamageSource.builder(DamageType.LIGHTNING_BOLT).build()
        for (i in 1 until chainTargets.size) {
            chainTargets[i].damage(chainDamage, damageSource)
        }

        // Draw particle lightning lines between each chained entity pair
        for (i in 0 until chainTargets.size - 1) {
            val from = chainTargets[i].eyeLocation
            val to = chainTargets[i + 1].eyeLocation
            spawnZigZagLine(Particle.ELECTRIC_SPARK, from, to)
        }
        //attacker.location.world.playSound(attacker.location, Sound.LIGH, 3.5F, 0.4F)

        return chainDamage
    }

    private fun cleaveEnchantment(victim: LivingEntity, level: Int) {
        //victim.shieldBlockingDelay += level * 20
    }

    private const val VOID_DOT_TICKS = 2 * 10  // Number of runs (20 / 5 = 4) * 10
    private const val VOID_DOT_PERIOD = 10L // Run every 10 ticks -> 0.5s

    /**
     * Dematerialize: applies a Void damage-over-time equal to
     * (5 + level × 5)% of the triggering attack's damage, spread across
     * 20 ticks (every 0.5s for 10s).
     *
     * Re-hitting the same target refreshes the 5s window and folds the new
     * hit's pool into whatever damage hasn't been dealt yet.
     */
    private fun dematerializeEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int
    ) {
        if (level <= 0) return

        val conversionPct = (level * 0.02)
        val newDotDamage = damage * conversionPct
        if (newDotDamage <= 0.0) return

        val id = victim.uniqueId

        // Carry over the undealt portion of any running DoT, then cancel it.
        val carryOver = voidDotStates.remove(id)?.let { existing ->
            existing.task.cancel()
            existing.perTick * existing.ticksRemaining
        } ?: 0.0

        val damagePerTick = (carryOver / VOID_DOT_TICKS) + newDotDamage

        // VOID for now
        val damageSource = DamageSource.builder(DamageType.OUT_OF_WORLD).build()

        val task = object : BukkitRunnable() {
            override fun run() {
                val state = voidDotStates[id]
                if (state == null || victim.isDead || !victim.isValid) {
                    voidDotStates.remove(id)
                    cancel()
                    return
                }

                victim.damage(state.perTick, damageSource)
                with(victim.world) {
                    spawnParticle(Particle.REVERSE_PORTAL, victim.location, 12, 0.4, 0.6, 0.4, 0.02)
                }

                state.ticksRemaining -= 1
                if (state.ticksRemaining <= 0) {
                    voidDotStates.remove(id)
                    cancel()
                }
            }
        }.runTaskTimer(Odyssey.instance, VOID_DOT_PERIOD, VOID_DOT_PERIOD)

        voidDotStates[id] = VoidDotState(task, damagePerTick, VOID_DOT_TICKS)
    }


    private fun executionEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        val maxHealth = victim.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        val threshHold = maxHealth * (0.03 * level)
        if (victim.health < threshHold) {
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, victim.location, 10, 0.25, 0.25, 0.25)
            // Execute
            victim.health = 0.0
            val damageSource = DamageSource.builder(DamageType.GENERIC_KILL)
                .withCausingEntity(attacker)
                .withDirectEntity(attacker)
                .withDamageLocation(victim.location)
                .build()
            victim.kill(damageSource)
            victim.world.spawnParticle(
                Particle.RAID_OMEN,
                victim.location.clone().add(0.0, 0.5, 0.0),
                20,
                0.05,
                0.05,
                0.05
            )
        }
    }

    private fun committedEnchantment(victim: LivingEntity, level: Int): Float {
        val maxHealth = victim.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        return if (victim.health < maxHealth * 0.4) {
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, victim.location, 15, 0.25, 0.25, 0.25)
            level * 0.15F
        } else {
            0.0F
        }
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


    private fun cullTheWeakEnchantment(victim: LivingEntity, level: Int): Float {
        var modifier = 0.0F
        val hasSlowness = victim.hasPotionEffect(PotionEffectType.SLOWNESS)
        val hasWeakness = victim.hasPotionEffect(PotionEffectType.WEAKNESS)
        val hasFatigue = victim.hasPotionEffect(PotionEffectType.MINING_FATIGUE)
        val hasNausea = victim.hasPotionEffect(PotionEffectType.NAUSEA)

        if (hasSlowness || hasWeakness || hasFatigue || hasNausea) {
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, victim.location, 10, 0.25, 0.25, 0.25)
            if (hasSlowness) modifier += (0.1F * level)
            if (hasWeakness) modifier += (0.1F * level)
            if (hasFatigue) modifier += (0.1F * level)
            if (hasNausea) modifier += (0.1F * level)
        }
        return modifier
    }

    private fun decayEnchantment(victim: LivingEntity, level: Int) {
       victim.addPotionEffect(PotionEffect(PotionEffectType.WITHER, (20 * (level * 4)), 0))
    }

    private fun douseEnchantment(victim: LivingEntity, level: Int): Float {
        if (victim.fireTicks > 0 || victim is Blaze || victim is MagmaCube || victim is Enderman) {
            victim.fireTicks = 0
            return 0.2F * level
        }
        victim.fireTicks = 0
        return 0.0F
    }

    private fun echoEnchantment(attacker: LivingEntity, victim: LivingEntity, level: Int) {
        // Prevent recursive call
        if (victim.scoreboardTags.contains(EntityTags.ECHO_STRUCK)) {
            victim.scoreboardTags.remove(EntityTags.ECHO_STRUCK)
            return
        }
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
                        22,
                        0.05,
                        0.05,
                        0.05
                    )
                    playSound(victim.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.7F)
                }
            }
        }
    }

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

    private fun flameEdgeEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int): Float {
        val modifier = 0.06F * level
        val damageSource = DamageSource.builder(DamageType.ON_FIRE).build()
        val fireDamage = damage * modifier
        victim.damage(fireDamage, damageSource)
        victim.world.spawnParticle(Particle.FLAME, victim.location, 10, 0.02, 0.02, 0.02)

        return modifier
    }


    private fun freezingAspectEnchantment(victim: LivingEntity, level: Int) {
        victim.addOdysseyEffect(EffectTags.FREEZING, (level * 4) * 20, 1)
        if (victim.freezeTicks <= 20 * ((4 * level) + 2)) {
            victim.world.spawnParticle(Particle.SNOWFLAKE, victim.location, 5, 0.05, 0.05, 0.05)
            victim.freezeTicks += 40
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
        level: Int) : Float {

        // ADD TAG on block
        // -> add scheduler that removes tag
        // if still has tag, then damage

        attacker.world.playSound(attacker.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)

        if (!attacker.scoreboardTags.contains(EntityTags.GUARDING_STRIKE_READY)) return 0.0F
        return 0.1F * level
    }

    private fun gustEnchantment(
        vector: Vector,
        level: Int
    ): Vector {
        val magnitude = vector.length() * (0.5 * level)
        val oldVector = vector.normalize().clone()
        val newVector = oldVector.setX(0.0).setY(1.0).setZ(0.0).normalize().multiply(magnitude)

        return newVector
    }

    private fun hemorrhageEnchantment(
        victim: LivingEntity,
        damage: Double,
        level: Int) {
        // VFX
        victim.world.playSound(victim.location, Sound.BLOCK_NETHER_SPROUTS_PLACE, 2.5F, 0.9F)
        // Calculate Damage
        val hemorrhageDamage = damage * 0.03 * level
        victim.applyOdysseyEffect(StatusEffect.HEMORRHAGE, 8 * 20, hemorrhageDamage)
    }

    private fun illucidationEnchantment(
        victim: LivingEntity,
        level: Int,
        isCrit: Boolean): Float {
        var damageBonus = 0.0F
        val isGlowing = victim.hasPotionEffect(PotionEffectType.GLOWING)

        if (victim.isGlowing) {
            damageBonus += (level * 0.15F)
            victim.world.playSound(victim.location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 2.5F, 1.5F)
            victim.world.spawnParticle(Particle.END_ROD, victim.location, 25, 1.0, 0.5, 1.0)
        }

        if (isCrit && isGlowing) {
            victim.removePotionEffect(PotionEffectType.GLOWING)
            damageBonus *= 2.0F
        }
        return damageBonus
    }


    private fun invocativeEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int, ) {
        // Invocative Vars
        val lastTargetId = invocativeLastTarget[attacker.uniqueId]
        // Set the lastTarget to this current victim
        if (lastTargetId == null) {
            invocativeLastTarget[attacker.uniqueId] = victim.uniqueId
            return
        }
        else {
            invocativeLastTarget[attacker.uniqueId] = victim.uniqueId
        }

        // If new target is entity run new damage on last victim
        val lastVictim = attacker.world.getEntity(lastTargetId)
        if (lastVictim != victim && lastVictim != null) {
            lastVictim.world.spawnParticle(Particle.ENCHANTED_HIT, lastVictim.location, 20, 0.02, 0.5, 0.02)
            // Damage last living target
            if (lastVictim is LivingEntity && !lastVictim.isDead) {
                val damageSource = DamageSource.builder(DamageType.MAGIC).build()
                val voidDamage = damage * 0.15F * level
                lastVictim.damage(voidDamage, damageSource)
            }
        }
    }

    private fun impetusEnchantment(
        attacker: LivingEntity,
        level: Int): Float {

        val isMoving = attacker.velocity.length() > 0.105 || attacker.forwardsMovement.absoluteValue > 0.04
        if (isMoving) {
            // Effects
            attacker.world.playSound(attacker.location, Sound.BLOCK_COPPER_PLACE, 0.7F, 0.7F)
            val blockData = Material.CYAN_STAINED_GLASS.createBlockData()
            attacker.world.spawnParticle(Particle.BLOCK, attacker.location, 15, 0.45, 0.8, 0.35, blockData)
            // Return damage
            return 0.1F * level
        }
        return 0.0F
    }


    private fun lifeForceEnchantment(
        attacker: LivingEntity,
        level: Int
    ): Float {
        val maxHealth = attacker.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        val isBelowHalf = attacker.health / maxHealth < 0.5
        val flatDamage = maxHealth * (0.05F * level)
        if (isBelowHalf) return (flatDamage * 2.0F).toFloat()
        return (flatDamage).toFloat()
    }

    private fun mirrorForceEnchantment(
        defender: LivingEntity,
        projectile: Entity?,
        level: Int
    ) {
        if (projectile == null) return
        // Vectors
        val currentVelocity = projectile.velocity
        val currentSpeed = projectile.velocity.length()
        if (currentSpeed <= 0.0) return // Ignore divide 0
        //println("Projectile Velocity: $currentVelocity")
        //println("Projectile Speed: $currentSpeed")

        val hasShield = defender.equipment?.itemInMainHand?.type == Material.SHIELD
        val isBlocking = if (defender is Player) defender.isBlocking && hasShield else hasShield
        // Run has shield
        if (projectile is Projectile && isBlocking) {
            // get shield normal
            val shieldNormal = defender.location.direction.normalize()

            // Reflect across
            val dot = currentVelocity.dot(shieldNormal)
            val reflectedVelocity = currentVelocity.clone()
                .subtract(shieldNormal.multiply(2.0 * dot))
                .normalize()

            val newSpeed = currentSpeed * (level * 0.4F)
            projectile.velocity = reflectedVelocity.multiply(newSpeed)
            projectile.shooter = defender

            //println("New Velocity: $newVelocity")
            //println("New Speed: $newSpeed")
        }
    }

    private fun magicAspectEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int): Float {
        val modifier = 0.05F * level
        val damageSource = DamageSource.builder(DamageType.MAGIC).build()
        val magicDamage = damage * modifier
        victim.damage(magicDamage, damageSource)
        victim.world.spawnParticle(Particle.WITCH, victim.location, 10, 0.25, 0.25, 0.25)
        return modifier
    }

    private fun midasCurseEnchantment(event: EntityDeathEvent) {
        val items = event.drops
        val victim = event.entity
        if (victim is HumanEntity) return // Ignore players
        if (items.isEmpty()) return

        for (x in 0..<items.size) {
            val item = items[x]
            val newDrop = ItemStack(Material.GOLD_NUGGET, item.amount)
            items[x] = newDrop
        }

    }


    private fun miscalibrateEnchantment(victim: LivingEntity, level: Int) {
        victim.maximumNoDamageTicks
        victim.noDamageTicks = 10 - level
        victim.world.spawnParticle(Particle.ENCHANTED_HIT, victim.location, 10, 0.25, 0.25, 0.25)
    }

    // PLAGUE BRINGER
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


    private fun pressTheAttackEnchantment(
        victim: LivingEntity,
        level: Int): Float {
        val bonusMultiplier = 0.15F * level
        val blockData = Material.GOLD_BLOCK.createBlockData()

        with(victim) {
            // Cancel any existing expiry task and reschedule — resets the 20s window on each hit
            ptaTasks[uniqueId]?.cancel()
            ptaTasks[uniqueId] = Odyssey.instance.server.scheduler.runTaskLater(
                Odyssey.instance,
                Runnable {
                    removeScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_1)
                    removeScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_2)
                    ptaTasks.remove(uniqueId)
                },
                400L // 20 seconds = 400 ticks
            )

            // Remove if dead
            if (this.isDead || this.health <= 0.0) {
                ptaTasks[uniqueId]?.cancel()
                ptaTasks.remove(uniqueId)
                removeScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_1)
                removeScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_2)
            }

            return when {
                // 3rd hit — full bonus, then reset all tags
                scoreboardTags.contains(EffectTags.PRESS_THE_ATTACK_HIT_2) -> {
                    // Remove task tracking
                    removeScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_2)
                    ptaTasks[uniqueId]?.cancel()
                    ptaTasks.remove(uniqueId)

                    // Effects
                    //world.playSound(location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 0.8F)
                    world.spawnParticle(Particle.BLOCK, location, 25, 0.45, 0.8, 0.35, blockData)

                    bonusMultiplier
                }
                // 2nd hit — bonus damage, advance to hit 3
                scoreboardTags.contains(EffectTags.PRESS_THE_ATTACK_HIT_1) -> {
                    //world.playSound(location, Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 1.0F)
                    world.spawnParticle(Particle.BLOCK, location, 15, 0.45, 0.8, 0.35, blockData)

                    removeScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_1)
                    addScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_2)
                    bonusMultiplier
                }
                // 1st hit — bonus damage, start tracking
                else -> {
                    //world.playSound(location, Sound.ENTITY_PLAYER_ATTACK_WEAK, 1.0F, 1.2F)
                    world.spawnParticle(Particle.BLOCK, location, 10, 0.45, 0.8, 0.35, blockData)

                    addScoreboardTag(EffectTags.PRESS_THE_ATTACK_HIT_1)
                    bonusMultiplier
                }
            }
        }
    }


    private fun plunderEnchantment(event: EntityDeathEvent) {
        val items = event.drops
        val killer = event.entity.killer as LivingEntity
        val dropsToRemove = mutableListOf<ItemStack>()
        // Loop per drop then add to killer inventory
        for (drop in items) {
            if (killer is Player) {
                val overflow = killer.inventory.addItem(drop.clone())
                // Empty -> Success
                if (overflow.isEmpty()) dropsToRemove.add(drop)
            }
            else {
                // Do Nothing
            }
        }
        for (plunderedDrop in dropsToRemove) {
            items.remove(plunderedDrop)
        }

    }

    // Old Invocative
    private fun recallEnchantment(attacker: LivingEntity, victim: LivingEntity, damage: Double, level: Int) {
        val lastTarget = recallTargets[attacker.uniqueId]
        if (lastTarget == null) {
            recallTargets[attacker.uniqueId] = victim
            return
        }
        else if (lastTarget != victim) {
            val source = createEntityDamageSource(attacker, null, DamageType.PLAYER_ATTACK)
            recallTargets[attacker.uniqueId] = victim // Prevent recursion
            val recallDamage = damage * (level * 0.1)
            victim.damage(recallDamage, source)
            victim.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, victim.location, 25, 1.0, 0.5, 1.0)
        }
        else if (lastTarget == victim) {
            return
        }
    }


    private fun ruptureEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        damage: Double,
        level: Int): Float {
        if (victim.isDead) return 0.0F
        // Prevent spam

        val rupturingModifier = 0.1F * level
        with(victim) {
            if (scoreboardTags.contains(EffectTags.FULLY_RUPTURED)) {
                removeScoreboardTag(EffectTags.FULLY_RUPTURED)
                if (damage > level) {
                    // True Damage bypasses the damage layer
                    val trueDamage = damage * rupturingModifier
                    health -= minOf(health, trueDamage)
                }
                world.playSound(victim.location, Sound.ITEM_SPEAR_HIT, 1.2F, 1.3F)
                val blockData = Material.IRON_BLOCK.createBlockData()
                world.spawnParticle(Particle.BLOCK, victim.location, 15, 0.45, 0.8, 0.35, blockData)
                return rupturingModifier
            }
            else if (scoreboardTags.contains(EffectTags.PARTLY_RUPTURED)) {
                removeScoreboardTag(EffectTags.PARTLY_RUPTURED)
                addScoreboardTag(EffectTags.FULLY_RUPTURED)
            }
            else {
                addScoreboardTag(EffectTags.PARTLY_RUPTURED)
            }
        }
        return 0.0F
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
        victim.teleport(attackerLocation)
    }

    private fun thunderousEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        knockback: Vector,
        level: Int
    ) {
        val newKnockback = knockback.clone().normalize().multiply(level * 0.4)
        val entitiesInCone = getEntitiesBehindConeFromAttack(
            attacker = attacker,
            target = victim, // or any vector
            halfAngleDeg = 45.0, // 90deg total cone
            length = 4.0
        )
        entitiesInCone.forEach {
            it.velocity = newKnockback.clone()
        }
        if (entitiesInCone.isNotEmpty()) {
            attacker.world.playSound(victim.location, Sound.BLOCK_HEAVY_CORE_BREAK, 0.5F, 0.3F)
        }

    }

    private fun unyieldingEnchantment(attacker: LivingEntity, level: Int): Float {
        val maxHealth = attacker.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        return if (attacker.health < maxHealth * 0.4) {
            attacker.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, attacker.location, 15, 0.25, 0.25, 0.25)
            level * 0.15F
        } else {
            0.0F
        }
    }

    private fun vitalEnchantment(isCrit: Boolean, level: Int): Float {
        if (isCrit) {
            return 0.1F * level
        }
        return 0.0F
    }

    private fun vengefulEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ) : Float {
        val attackerId = attacker.entityId

        victim.setIntTag(EntityTags.VENGEFUL_MARK, attackerId)

        return 0.0F
    }

    private fun voidStrikeEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ): Float {
        // Skip if not matching
        val voidStruckBy = victim.getIntTag(EntityTags.VOID_STRUCK_BY)
        if (voidStruckBy == null) {
            victim.setIntTag(EntityTags.VOID_STRUCK_BY, attacker.entityId)
        }
        else if (voidStruckBy != attacker.entityId) {
            victim.setIntTag(EntityTags.VOID_STRUCK_BY, attacker.entityId)
            return 0.0F
        }
        // Get void stacks
        val stacks = victim.getIntTag(EntityTags.VOID_STRIKE_STACKS) ?: 0
        victim.setIntTag(EntityTags.VOID_STRIKE_STACKS, stacks + 1)
        val voidDamageModifier = stacks * (level * 0.1F)
        // Reset void strike modifier
        if (stacks > 10) {
            victim.setIntTag(EntityTags.VOID_STRIKE_STACKS, 0)
            victim.removeTag(EntityTags.VOID_STRUCK_BY)
        }
        // Particles and Sounds
        with(victim.world) {
            val location = victim.location
            spawnParticle(Particle.PORTAL, location, (stacks + 1) * 8, 1.15, 0.85, 1.15)
            //spawnParticle(Particle.WAX_OFF, location, (modifier + 1) * 2, 1.0, 0.75, 1.0)
            spawnParticle(Particle.WITCH, location, (stacks + 1) * 5, 1.0, 0.75, 1.0)
            playSound(location, Sound.BLOCK_BEACON_DEACTIVATE, 1.5F, 0.5F)
            //playSound(location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.7F, 0.2F)
            playSound(location, Sound.ENTITY_ENDER_EYE_DEATH, 3.5F, 0.4F)
        }
        // Damage
        return voidDamageModifier
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
            spawnParticle(Particle.GUST, attacker.location, 20, 0.04, 0.04, 0.04)
            //playSound(attacker.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
            playSound(attacker.location, Sound.ENTITY_WIND_CHARGE_THROW, 1.2F, 0.6F)
        }

        // Damage Calculation
        val damageSource = DamageSource.builder(DamageType.WIND_CHARGE).build()
        val whirlDamage = damage * (level * 0.4)
        nearbyEntities.forEach {
            it.damage(whirlDamage, damageSource)
            val speed = 0.8 + (0.2 * level)
            val direction = it.location.clone().subtract(attacker.location).toVector().normalize()
            it.velocity = direction.multiply(speed)
        }
    }

}