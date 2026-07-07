package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.entity.LookAnchor
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.effects.EffectsManager
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.SpeedySpursTask
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.Vibration
import org.bukkit.Vibration.Destination.EntityDestination
import org.bukkit.attribute.Attribute
import org.bukkit.block.data.type.Leaves
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.absoluteValue

@Suppress("UnstableApiUsage")
object ArmorListeners : Listener, EnchantmentManager, EffectsManager {

    // Everything any armor enchant might need .
    private data class ArmorEnchantContext(
        val event: EntityDamageByEntityEvent,
        val holder: LivingEntity,    // the wearer whose enchant is firing
        val opponent: LivingEntity,  // the other party in the hit
        val level: Int,
        val mods: DamageMods,
        val bonusImmunityTime: MutableList<Int>,
    )

    /**
     *  Flat Damage: raw base added before percents.
     *  Percent Modifier: summed, applied as (1 + percent).
     *  Post Percent: multiplied last.
     * */
    private class DamageMods {
        var flat: Float = 0.0f         // raw base added before percent
        var percent: Float = 0.0f      // summed, applied as (1 + percent)
        var postPercent: Float = 1.0f  // multiplied last (e.g. magic)
    }

    private val armorDefenseEnchantmentsMap: Map<String, (ArmorEnchantContext) -> Unit> = mapOf(
        // --- percent reductions (subtract from percent) ---
        "antibonk"   to { c -> c.mods.percent -= antibonkEnchantment(c.event.isCritical, c.level) },
        "relentless" to { c -> c.mods.percent -= relentlessEnchantment(c.holder, c.level) },
        "blurcise"   to { c -> c.mods.percent -= blurciseEnchantment(c.holder, c.level) },
        "brawler"    to { c -> c.mods.percent -= brawlerArmorEnchantment(c.holder, c.level) },

        // --- percent increases (add to percent) ---
        "reckless"   to { c -> c.mods.percent += recklessDefenderEnchantment(c.level) },

        // --- flat (base damage changes) ---
        "root_boots" to { c -> c.mods.flat -= rootBootsDefenseHandler(c.holder, c.level).toFloat() },

        // --- side effects only, untouched ---
        "illumineye"       to { c -> illumineyeEnchantment(c.opponent, c.holder, c.level) },
        "opticalization"   to { c -> opticalizationHitEnchantment(c.holder, c.opponent, c.level) },
        "sslither_ssight"  to { c -> sslitherSsightEnchantment(c.opponent, c.holder, c.level) },
        "veiled_in_shadow" to { c -> veiledInShadowEnchantment(c.holder, c.level, c.bonusImmunityTime) },
        "black_rose"       to { c -> stxRoseHitEnchantment(c.opponent, c.level) },
        "ignore_pain"      to { c -> ignorePainEnchantment(c.holder, c.level) },
        "molten_core"      to { c -> moltenCoreEnchantment(c.holder, c.opponent, c.level) },
        "untouchable"      to { c -> untouchableEnchantment(c.holder, c.bonusImmunityTime) },
        "cowardice"        to { c -> cowardiceEnchantment(c.opponent, c.holder, c.level) },
        "sporeful"         to { c -> sporefulEnchantment(c.holder, c.level) },
        "squidify"         to { c -> squidifyEnchantment(c.holder, c.level) },
    )

    private val armorAttackEnchantmentsMap: Map<String, (ArmorEnchantContext) -> Unit> = mapOf(
        "vigor"            to { c -> c.mods.percent += vigorEnchantment(c.holder, c.level) },
        "reckless"         to { c -> c.mods.percent += recklessAttackerEnchantment(c.level) },

        "mandiblemania"    to { c -> mandiblemaniaEnchantment(c.holder, c.level) },
        "opticalization"   to { c -> opticalizationHitEnchantment(c.opponent, c.holder, c.level) },
        "illumineye"       to { c -> illumineyeEnchantment(c.opponent, c.holder, c.level) },
        "static_socks"     to { c -> staticSocksAttackEnchantment(c.holder, c.opponent, c.level)}
    )


    private fun processArmor(
        holder: LivingEntity,
        opponent: LivingEntity,
        event: EntityDamageByEntityEvent,
        map: Map<String, (ArmorEnchantContext) -> Unit>,
        mods: DamageMods,
        bonusImmunityTime: MutableList<Int>,
    ) {
        val pieces = listOfNotNull(
            holder.equipment?.helmet,
            holder.equipment?.chestplate,
            holder.equipment?.leggings,
            holder.equipment?.boots,
        )
        for (piece in pieces) {
            if (!piece.hasItemMeta()) continue
            for ((enchant, level) in piece.enchantments) {
                val handler = map[enchant.getNameId()] ?: continue
                handler(ArmorEnchantContext(event, holder, opponent, level, mods, bonusImmunityTime))
            }
        }
    }

    /**
     * Walks the entity's armor (helmet → chestplate → leggings → boots) and yields
     *  every enchant id + level on pieces with meta. Deterministic slot order.
     *
     * */
    private inline fun LivingEntity.forEachArmorEnchant(action: (id: String, level: Int) -> Unit) {
        val eq = equipment ?: return
        for (piece in listOfNotNull(eq.helmet, eq.chestplate, eq.leggings, eq.boots)) {
            if (!piece.hasItemMeta()) continue
            for ((enchant, level) in piece.enchantments) {
                action(enchant.getNameId(), level)
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ────────────────────────────── ENCHANTMENTS ──────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    @EventHandler
    fun mainArmorHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) return
        if (event.entity !is LivingEntity) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return

        val attacker = event.damager as LivingEntity
        val defender = event.entity as LivingEntity
        val bonusImmunityTime = mutableListOf(0)
        defender.maximumNoDamageTicks = 20

        val atkMods = DamageMods()
        val defMods = DamageMods()

        val armor = listOfNotNull(
            defender.equipment?.helmet,
            defender.equipment?.chestplate,
            defender.equipment?.leggings,
            defender.equipment?.boots,
        )
        // Attacker's gear amplifies the outgoing hit...
        processArmor(attacker, defender, event, armorAttackEnchantmentsMap, atkMods, bonusImmunityTime)
        // ...then the defender's gear mitigates the incoming hit
        processArmor(defender, attacker, event, armorDefenseEnchantmentsMap, defMods, bonusImmunityTime)

        if (bonusImmunityTime[0] > 0) {
            val immunityTicks = bonusImmunityTime[0]
            defender.maximumNoDamageTicks = 20 + immunityTicks
            defender.noDamageTicks = 10 + immunityTicks
        }

        // Final Calculation Step
        // (Base Damage + Base Flat Modifiers) * (1 + Percentage Modifiers) * postPercent
        // attacker layer, then defender layer
        val outgoing = (event.damage + atkMods.flat) * (1 + atkMods.percent) * atkMods.postPercent
        event.damage = (outgoing + defMods.flat) * (1 + defMods.percent) * defMods.postPercent
        if (event.damage < 0.0) event.damage = 0.0
    }


    @EventHandler
    fun mainArmorProjectileHandler(event: ProjectileHitEvent) {
        val defender = event.entity as? LivingEntity ?: return
        defender.forEachArmorEnchant { id, level ->
            when (id) {
                "styx_rose" -> styxRoseProjectileEnchantment(event, defender, level)
            }
        }
    }


    // Main function for enchantments relating to specific damage
    @EventHandler
    fun mainArmorHitHandler(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.FALL) return
        val defender = event.entity as? LivingEntity ?: return
        defender.forEachArmorEnchant { id, level ->
            when (id) {
                "devastating_drop" -> devastatingDropEnchantment(defender, event.damage, level)
            }
        }
    }

    @EventHandler
    fun mainArmorConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        player.forEachArmorEnchant { id, level ->
            when (id) {
                "brewful_breath" -> brewfulBreathEnchantment(player, event.item, level)
                //"mandiblemania"  -> mandiblemaniaEnchantment(player, level)
                "fairy_fare"  -> fairyFareEnchantment(player, event.item, level)
                "potion_barrier" -> potionBarrierEnchantment(player, event.item, level)
            }
        }
    }

    @EventHandler
    fun passiveRegenHandler(event: EntityRegainHealthEvent) {
        val defender = event.entity as? LivingEntity ?: return
        val originalAmount = event.amount
        defender.forEachArmorEnchant { id, level ->
            when (id) {
                "revitalize" -> event.amount += revitalizeEnchant(originalAmount, level)
            }
        }
    }

    @EventHandler
    fun miscArmorVehicleHandler(event: VehicleEnterEvent) {
        val rider = event.entered as? LivingEntity ?: return
        val mount = event.vehicle as? LivingEntity ?: return
        rider.forEachArmorEnchant { id, level ->
            when (id) {
                "speedy_spurs" -> speedySpursEnchantment(rider, mount, level)
            }
        }
    }

    @EventHandler
    fun sneakHandler(event: PlayerToggleSneakEvent) {
        val sneaker = event.player
        sneaker.forEachArmorEnchant { id, level ->
            when (id) {
                "sculk_sensitive" -> sculkSensitiveSneakEnchantment(sneaker, level, event.isSneaking)
                "leap_frog"       -> leapFrogSneakEnchantment(sneaker, event.isSneaking)
                "root_boots"      -> rootBootsSneakEnchantment(sneaker, level, event.isSneaking)
                "static_socks"    -> staticSocksSneakEnchantment(sneaker, level)
                "cloud_strider"   -> cloudStriderEnchantment(sneaker, level)
            }
        }
    }

    @EventHandler
    fun jumpHandler(event: PlayerJumpEvent) {
        val jumper = event.player
        jumper.forEachArmorEnchant { id, level ->
            when (id) {
                "leap_frog"     -> leapFrogEnchantment(event, level)
                "cloud_strider" -> cloudStriderEnchantment(jumper, level)
            }
        }
    }

    // UNUSED — also note there's no @EventHandler on this, so it never fires as written.
    fun playerInputHandler(event: PlayerInputEvent) {
        val player = event.player
        player.forEachArmorEnchant { id, level ->
            when (id) {
                "cloud_strider" -> cloudStriderEnchantment(player, level)
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── EFFECTS ─────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private fun antibonkEnchantment(
        isCrit: Boolean,
        level: Int,
    ): Float {
        return if (isCrit) {
            (level * 0.1F)
        } else {
            0.0F
        }
    }

    private fun revitalizeEnchant(
        amount: Double,
        level: Int,
    ): Double {
        return amount * (level * 0.05)
    }


    private fun brawlerArmorEnchantment(
        defender: LivingEntity,
        level: Int
    ) : Float {
        // If surrounded
        return if (defender.location.getNearbyLivingEntities(4.0).size >= 4) { // 3 + self
            (0.03F * level)
        } else {
            0.0F
        }
    }

    private fun stxRoseHitEnchantment(
        attacker: LivingEntity,
        level: Int
    ) {
        attacker.addPotionEffect(
            PotionEffect(
                PotionEffectType.WITHER,
                (4 * level) * 20,
                1
            )
        )
    }

    private fun styxRoseProjectileEnchantment(
        event: ProjectileHitEvent,
        defender: LivingEntity,
        level: Int
    ) {
        val maxHealth = defender.getAttribute(Attribute.MAX_HEALTH)?.value ?: return
        val currentHealth = defender.health
        val percentHealth = currentHealth / maxHealth
        if (percentHealth < (0.2 * level)) {
            event.isCancelled = true
        }
    }

    private fun blurciseEnchantment(
        defender: LivingEntity,
        level: Int,
    ): Float {
        val isMoving = defender.velocity.length() > 0.105 || defender.forwardsMovement.absoluteValue > 0.04
        if (isMoving) {
            return (level * 0.08).toFloat()
        }
        return 0.0F
    }

    private fun brewfulBreathEnchantment(
        player: Player,
        potion: ItemStack,
        level: Int
    ) {
        if (potion.type != Material.POTION) { return }
        if (potion.itemMeta !is PotionMeta) { return }
        // Lower Potency
        val potency = 0.15
        val potionMeta = (potion.itemMeta as PotionMeta).also {
            val newEffects = mutableListOf<PotionEffect>()
            for (x in it.customEffects) {
                newEffects.add(PotionEffect(x.type, (x.duration * potency).toInt(), x.amplifier, x.isAmbient))
                it.removeCustomEffect(x.type)
            }
            for (y in newEffects) {
                it.addCustomEffect(y, true)
            }
        }

        val cloud = player.world.spawnEntity(
            player.location,
            EntityType.AREA_EFFECT_CLOUD,
            CreatureSpawnEvent.SpawnReason.CUSTOM
        ) as AreaEffectCloud
        cloud.apply {
            basePotionType = potionMeta.basePotionType
            duration = 5 * 20 // 5 seconds
            durationOnUse = 5 //
            radiusOnUse = 0.05F
            if (potionMeta.color != null) {
                setColor(potionMeta.color!!)
            }
            radius = 0.5F + (0.5F * level)
            radiusPerTick = -0.05F
            reapplicationDelay = 20
            addScoreboardTag(EntityTags.BREATH_CLOUD)
            addScoreboardTag(EntityTags.BREATH_BY + player.uniqueId)
        }

        player.setCooldown(potion.type, 20 * 6)
    }

    private fun cowardiceEnchantment(
        attacker: LivingEntity,
        defender: LivingEntity,
        enchantmentStrength: Int
    ) {
        defender.addPotionEffect(
            PotionEffect(
                PotionEffectType.SPEED,
                6 * 20,
                enchantmentStrength
            )
        )
        // Movement Math
        if (attacker.location.distance(defender.location) <= 5.0) {
            defender.location.clone().subtract(attacker.location).toVector().normalize().multiply(1.6)
        }
    }

    private fun cloudStriderEnchantment(jumper: Player, level: Int): Boolean {
        val cloudJumpSpeed = 0.75
        val maxJumps = level.coerceAtLeast(0)
        if (maxJumps == 0) return false

        val currentJumps = jumper.getIntTag(EntityTags.CLOUD_STRIDER_JUMPS) ?: 0

        // On the ground: reset the counter, but only write the tag if it changed.
        if (jumper.isOnGround) {
            if (currentJumps != 0) {
                jumper.setIntTag(EntityTags.CLOUD_STRIDER_JUMPS, 0)
            }
            return false
        }

        // In the air: jump only if jumps remain.
        if (currentJumps >= maxJumps) return false

        jumper.velocity = jumper.velocity.setY(0.0).add(Vector(0.0, cloudJumpSpeed, 0.0))
        jumper.setIntTag(EntityTags.CLOUD_STRIDER_JUMPS, currentJumps + 1)
        return true
    }

    private fun devastatingDropEnchantment(
        dropper: LivingEntity,
        receivedDamage: Double,
        level: Int
    ) {
        if (!dropper.isSneaking) return // Has to be sneaking
        dropper.location.getNearbyLivingEntities(4.0).forEach {
            if (it != dropper) {
                it.damage(receivedDamage * (0.25 * level))
            }
        }
    }

    private fun fairyFareEnchantment(
        player: Player,
        food: ItemStack,
        level: Int): Double {
        val fairyFareCooldown = 20 * 2 // In Ticks
        if (level <= 0) return 0.0

        // Non-food items have no FOOD component -> bail.
        val nutrition = food.getData(DataComponentTypes.FOOD)?.nutrition() ?: return 0.0
        if (nutrition <= 0) return 0.0

        // Rate-limit. Note: this now applies to *every* food (see below).
        if (player.getCooldown(food.type) > 0) return 0.0

        val maxHealth = player.getAttribute(Attribute.MAX_HEALTH)?.value ?: return 0.0

        // (level × 10)% of nutrition — e.g. nutrition 8 at level 5 -> 4.0 health.
        val healAmount = nutrition * (level * 0.25F)
        val newHealth = (player.health + healAmount).coerceAtMost(maxHealth)
        val healed = newHealth - player.health
        if (healed <= 0.0) return 0.0  // already at full health

        //player.health = newHealth
        player.heal(healAmount.toDouble(), EntityRegainHealthEvent.RegainReason.REGEN)
        player.setCooldown(food.type, fairyFareCooldown)

        with(player.world) {
            spawnParticle(Particle.HAPPY_VILLAGER, player.location, 15, 0.5, 0.5, 0.5)
            spawnParticle(Particle.COMPOSTER, player.location, 15, 0.5, 0.5, 0.5)
            playSound(player.location, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 0.5f, 0.5f)
        }
        return healed
    }


    private fun ignorePainEnchantment(
        defender: LivingEntity,
        level: Int) {

        defender.noDamageTicks = 11 - (level * 2)
        // Effects
        if (!defender.hasPotionEffect(PotionEffectType.ABSORPTION)) {
            defender.addPotionEffect(
                PotionEffect(
                    PotionEffectType.ABSORPTION,
                    (5 - level) * 20,
                    0
                )
            )
        }
    }

    private fun illumineyeEnchantment(
        attacker: LivingEntity,
        defender: LivingEntity,
        level: Int) {
        if (!attacker.hasLineOfSight(defender)) return
        if (!defender.hasLineOfSight(attacker)) return
        attacker.addPotionEffect(
            PotionEffect(PotionEffectType.GLOWING, (3 + (level * 2)) * 20, 0))
    }


    private fun leapFrogSneakEnchantment(
        defender: LivingEntity,
        isSneak: Boolean
    ) {
        if (isSneak) {
            if (!defender.scoreboardTags.contains(EntityTags.LEAP_FROG_READY)) {
                defender.scoreboardTags.add(EntityTags.LEAP_FROG_READY)
            }
        } else {
            if (defender.scoreboardTags.contains(EntityTags.LEAP_FROG_READY)) {
                defender.scoreboardTags.remove(EntityTags.LEAP_FROG_READY)
            }
        }
    }

    private fun leapFrogEnchantment(
        event: PlayerJumpEvent,
        level: Int
    ) {
        val jumper = event.player
        val location = jumper.location.clone().toBlockLocation().apply {
            y -= 0.75
        }
        val block = location.block
        val jumpBlocks = listOf(
            Material.LILY_PAD,
            Material.BIG_DRIPLEAF,
            Material.MUD,
            Material.MUDDY_MANGROVE_ROOTS,
        )
        val isWaterLeaf = block.blockData is Leaves && (block.blockData as Leaves).isWaterlogged
        val validBlock = jumper.location.block.type in jumpBlocks || isWaterLeaf

        if (validBlock) {
            jumper.velocity = jumper.velocity.multiply(1.0 + (1.0 * level))
        } // Tag not working?
        if (jumper.scoreboardTags.contains(EntityTags.LEAP_FROG_READY) || jumper.isSneaking) {
            //jumper.velocity = jumper.velocity.no.multiply(1.0 + (2.5 * level))
            jumper.velocity.add(Vector(0.0, (1.0 * level), 0.0))
            jumper.scoreboardTags.remove(EntityTags.LEAP_FROG_READY)
        }

    }

    private fun mandiblemaniaEnchantment(
        entity: LivingEntity,
        level: Int
    ) {
        if (entity is Player) {
            entity.saturation += (entity.saturation * (1 + (level * 0.02))).toFloat()
        }
    }

    private fun moltenCoreEnchantment(
        defender: LivingEntity,
        attacker: LivingEntity,
        level: Int
    ) {
        // Fire
        if (defender.fireTicks > 20 && defender.fireTicks <= (20 * 20)) {
            attacker.fireTicks += ((level * 4) * 20) * 2
        } else {
            attacker.fireTicks += (level * 4) * 20
        }
    }

    private fun opticalizationHitEnchantment(
        defender: LivingEntity,
        attacker: LivingEntity,
        level: Int) {
        if (defender.location.distance(attacker.location) > (level * 2.0)) return
        // Used for both attack and defense
        when (defender) {
            is Mob -> {
                defender.lookAt(attacker)
            }
            is Player -> {
                defender.lookAt(attacker, LookAnchor.EYES, LookAnchor.EYES)
            }
        }
        when (attacker) {
            is Mob -> {
                attacker.lookAt(defender)
            }
            is Player -> {
                //attacker.lookAt(defender, LookAnchor.EYES, LookAnchor.EYES)
            }
        }

    }

    private fun pollenGuardSneakEnchantment(
        defender: LivingEntity,
        level: Int,
        dict: MutableMap<UUID, Int>,
        sneaking: Boolean
    ) {
        // Sentries
        if (!sneaking) return
        if (defender.isSwimming) return
        if (defender.isRiptiding) return

        // blocks
        val flowers = listOf(Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID,
            Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY,
            Material.ORANGE_TULIP, Material.PINK_TULIP, Material.WHITE_TULIP, Material.OXEYE_DAISY,
            Material.LILAC, Material.SUNFLOWER, Material.ROSE_BUSH, Material.PEONY, Material.TORCHFLOWER, Material.WITHER_ROSE,
            Material.PINK_PETALS, Material.PITCHER_POD
        )

        val block = defender.location.block
        if (block.type !in flowers) return

        var pollenStacks = defender.getIntTag(EntityTags.POLLEN_GUARD_STACKS)
        if (pollenStacks == null) {
            defender.setIntTag(EntityTags.POLLEN_GUARD_STACKS, 0)
            pollenStacks = 0
        }
        // Set item max
        dict[defender.uniqueId] = level
        val maxStacks = 0
        if (pollenStacks < maxStacks) {
            pollenStacks += 1
            defender.setIntTag(EntityTags.POLLEN_GUARD_STACKS, pollenStacks)
        }
    }

    private fun potionBarrierEnchantment(
        player: Player,
        potion: ItemStack,
        level: Int
    ) {
        if (potion.type != Material.POTION) { return }
        if (!potion.hasItemMeta()) { return }

        // Effects
        //player.addOdysseyEffect(EffectTags.BARRIER, (4 + (level * 4)) * 20)
        player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 10 * 20, 0))
        //val itemCooldown = (maxOf(1, 8 - (level * 2))) * 20
        player.setCooldown(potion.type, (2 + (level * 2)) * 20)
        // Particles
        with(player.world) {
            spawnParticle(Particle.DAMAGE_INDICATOR, player.location, 15, 0.5, 0.5, 0.5)
            playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
            playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
        }
    }

    private fun recklessAttackerEnchantment(
        level: Int
    ): Float {
        return (level * 0.05).toFloat()
    }

    private fun recklessDefenderEnchantment(
        level: Int
    ): Float {
        return (level * 0.05).toFloat()
    }


    private fun relentlessEnchantment(
        defender: LivingEntity,
        level: Int): Float {
        val maxHealth = defender.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        val currentHealthPercent = defender.health / maxHealth
        if (currentHealthPercent <= 0.4) {
            return (level * 0.05).toFloat()
        }
        return 0.0F
    }

    private fun rootBootsDefenseHandler(defender: LivingEntity, level: Int): Double {
        // Sentries
        if (defender.isSwimming) return 1.0
        if (defender.isRiptiding) return 1.0

        // blocks
        val rootBlocks = listOf(Material.DIRT, Material.ROOTED_DIRT, Material.COARSE_DIRT,
            Material.MANGROVE_ROOTS, Material.MUDDY_MANGROVE_ROOTS, Material.MUD,
            Material.FARMLAND, Material.GRASS_BLOCK, Material.MYCELIUM)

        val blockUnder = defender.location.clone().add(0.0, -1.0, 0.0).block
        val blockValue = if (blockUnder.type in rootBlocks) { level.toDouble() } else { 0.0 }
        val shiftValue = if (defender.scoreboardTags.contains(EntityTags.ROOT_BOOTS_ROOTED)) { level.toDouble() } else { 0.0 }

        return blockValue + shiftValue
    }

    private fun rootBootsSneakEnchantment(
        defender: LivingEntity,
        level: Int,
        sneaking: Boolean
    ) {
        // Sentries
        if (defender.isSwimming) return
        if (defender.isRiptiding) return

        if (sneaking) {
            defender.scoreboardTags.add(EntityTags.ROOT_BOOTS_ROOTED)
            defender.world.playSound(defender.location, Sound.BLOCK_MANGROVE_ROOTS_PLACE, 2.5F, 1.5F)
        }
        else {
            defender.scoreboardTags.remove(EntityTags.ROOT_BOOTS_ROOTED)
        }
    }

    private fun sculkSensitiveSneakEnchantment(
        sneaker: LivingEntity,
        level: Int,
        sneaking: Boolean
    ) {
        if (!sneaking) return
        val nearby = sneaker.world.getNearbyLivingEntities(sneaker.location, 5.0 + (level * 5))
            .filter { it != sneaker }
            .filter { it !is ArmorStand }
        nearby.forEach {
            val destination = EntityDestination(sneaker)
            val distance = it.location.distance(sneaker.location)
            val vibration = Vibration(destination, 7 + (0.3 * distance).toInt())
            it.world.spawnParticle(Particle.VIBRATION, it.location, 2, vibration)
        }
    }

    private fun speedySpursEnchantment(rider: LivingEntity, mount: LivingEntity, level: Int) {
        val someSpeedySpursTask = SpeedySpursTask(rider, mount, level)
        someSpeedySpursTask.runTaskTimer(Odyssey.instance, 0, 10 * 20)
    }

    private fun sporefulEnchantment(defender: LivingEntity, level: Int) {
        // List effects
        defender.world.getNearbyLivingEntities(defender.location, level.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.POISON, ((level * 2) + 2) * 20, 0),
                            PotionEffect(PotionEffectType.NAUSEA, ((level * 2) + 2) * 20, 0),
                        )
                    )
                }
            }
        // Particles
        with(defender.world) {
            spawnParticle(Particle.FALLING_SPORE_BLOSSOM, defender.location, 45, 1.0, 0.5, 1.0)
        }
    }

    private fun squidifyEnchantment(defender: LivingEntity, level: Int) {
        defender.world.getNearbyLivingEntities(defender.location, level.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.BLINDNESS, (1 + level) * 20, 1),
                            PotionEffect(PotionEffectType.SLOWNESS, (1 + level) * 20, 0)
                        )
                    )
                    if (it is Creature) {
                        it.pathfinder.stopPathfinding()
                        it.target = null
                    }
                }
            }
        // Particles
        with(defender.world) {
            spawnParticle(Particle.SQUID_INK, defender.location, 85, 0.75, 0.5, 0.75)
            spawnParticle(Particle.LARGE_SMOKE, defender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    private fun sslitherSsightEnchantment(
        attacker: LivingEntity,
        defender: LivingEntity,
        level: Int) {
        if (!attacker.hasLineOfSight(defender)) return
        if (!defender.hasLineOfSight(attacker)) return
        if (!defender.hasLineOfSight(attacker.eyeLocation)) return
        //val angle = attacker.eyeLocation.direction.angle(defender.eyeLocation.direction)
        //if (angle < 1.74533) return
        // Looking more than 90-deg (1.57-rads) away from attacker
        // parallel angles mean looking same direction -> behind
        val inFrontOfTarget = attacker.eyeLocation.direction.angle(defender.eyeLocation.direction) > 1.5708
        if (!inFrontOfTarget) return
        attacker.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, (level) * 10, 6))
    }

    private fun staticSocksAttackEnchantment(
        attacker: LivingEntity,
        victim: LivingEntity,
        level: Int
    ) {
        if (!attacker.scoreboardTags.contains(EntityTags.STATIC_SOCKS_CHARGING)) return
        val charge = attacker.getIntTag(EntityTags.STATIC_SOCKS_CHARGE)!!
        // Remove Tags and do Sound and Visual effects
        attacker.removeTag(EntityTags.STATIC_SOCKS_CHARGE)
        attacker.removeScoreboardTag(EntityTags.STATIC_SOCKS_CHARGING)
        attacker.world.spawnParticle(Particle.ELECTRIC_SPARK, attacker.location, charge * 3, 0.7, 0.5, 0.7)
        attacker.world.playSound(attacker.location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.5F, 4.5F)
        // Damage
        val damageSource = DamageSource.builder(DamageType.LIGHTNING_BOLT).build()
        victim.damage(level * 1.0, damageSource)
    }

    private fun staticSocksSneakEnchantment(
        defender: LivingEntity,
        level: Int
    ) {
        if (defender.scoreboardTags.contains(EntityTags.STATIC_SOCKS_CHARGING)) {
            val charge = defender.getIntTag(EntityTags.STATIC_SOCKS_CHARGE)!!
            if (charge < 2 * level) {
                defender.setIntTag(EntityTags.STATIC_SOCKS_CHARGE, charge + 1)
            }
            defender.world.spawnParticle(Particle.ELECTRIC_SPARK, defender.location, 5 + (charge * 1), 0.4, 0.3, 0.4)
        } else {
            defender.addScoreboardTag(EntityTags.STATIC_SOCKS_CHARGING)
            defender.setIntTag(EntityTags.STATIC_SOCKS_CHARGE, 0)
        }
    }

    private fun untouchableEnchantment(
        defender: LivingEntity,
        bonusImmunityTime: MutableList<Int>
    ) {
        bonusImmunityTime[0] += 10
    }

    private fun veiledInShadowEnchantment(
        defender: LivingEntity,
        level: Int,
        bonusImmunityTime: MutableList<Int>) {
        val location = defender.location
        val shadowLevelBlock = 10 + level - defender.location.block.lightFromBlocks
        val shadowLevelSky = 10 + level - defender.location.block.lightFromSky

        val lightLevel = if (location.world == Odyssey.instance.overworld) {
            if (location.world.isDayTime) {
                minOf(shadowLevelSky, shadowLevelBlock) // Get the lowest light level
            } else {
                shadowLevelBlock
            }
        } else {
            shadowLevelBlock
        }

        val shadowTicks = maxOf(lightLevel, 0)
        // gain at most 15 ticks of extra immunity when in full darkness with lvl 5
        if (shadowTicks > 0) {
            bonusImmunityTime[0] += shadowTicks
        }
    }

    private fun vigorEnchantment(
        attacker: LivingEntity,
        level: Int): Float {
        val maxHealth = attacker.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        val currentHealthPercent = attacker.health / maxHealth
        if (currentHealthPercent >= 0.60) {
            return (level * 0.04).toFloat()
        }
        return 0.0F
    }

}

