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
        if (event.hitEntity == null) return
        val defender = event.hitEntity ?: return
        if (defender !is LivingEntity) return
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "styx_rose" -> {
                        styxRoseProjectileEnchantment(event, defender, enchant.value)
                    }
                }
            }
        }
    }


    // Main function for enchantments relating to specific damage
    @EventHandler
    fun mainArmorHitHandler(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            val defender = event.entity as LivingEntity
            if (defender.equipment?.boots?.hasItemMeta() == true) {
                val boots = defender.equipment?.boots!!
                for (enchant in boots.enchantments) {
                    when (enchant.key.getNameId()) {
                        "devastating_drop" -> {
                            devastatingDrop(defender, event.damage, enchant.value)
                        }
                    }
                }
            }
        }
        // DODGE AN ATTACK WITH (IDK)
        // If CAN SEE AND WITHIN 3 BLOCKS DODGE MELEE
        // IF LINE OF SIGHT projectile
        // DODGE
        // COOLDOWN
        // IF SOMETHING
        // IDK
        // LIGHTNING REFLEXES -> dodge attacks if within range?
        // CLOSE COMBAT SPECIALIST -> less damage if close?
            /*
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            //
        }
             */
    }
    // Add if eat rotten or raw heal + other

    // Main function for enchantments relating to consuming items
    @EventHandler
    fun mainArmorConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        if (player.equipment!!.helmet?.hasItemMeta() == true) {
            val helmet = player.equipment!!.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "brewful_breath" -> {
                        brewfulBreathEnchantment(player, event.item, enchant.value)
                    }
                    "mandiblemania" -> {
                        mandiblemaniaEnchantment(player, enchant.value)
                    }
                }
            }
        }
        if (player.equipment!!.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment!!.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "fruitful_fare" -> {
                        fruitfulFareEnchantment(player, event.item, enchant.value)
                    }
                    "potion_barrier" -> {
                        potionBarrierEnchantment(player, event.item, enchant.value)
                    }
                }
            }
        }

    }

    @EventHandler
    fun passiveRegenHandler(event: EntityRegainHealthEvent) {
        if (event.entity !is LivingEntity) return
        // Armor Holder
        val defender = event.entity as LivingEntity
        val originalAmount = event.amount
        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "revitalize" -> {
                        event.amount += revitalizeEnchant(originalAmount, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "revitalize" -> {
                        event.amount += revitalizeEnchant(originalAmount, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "revitalize" -> {
                        event.amount += revitalizeEnchant(originalAmount, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "revitalize" -> {
                        event.amount += revitalizeEnchant(originalAmount, enchant.value)
                    }
                }
            }
        }
    }

    // Function regarding vehicles and armor
    @EventHandler
    fun miscArmorVehicleHandler(event: VehicleEnterEvent) { // Move Horse Armor Enchant Here
        if (event.entered is LivingEntity && event.vehicle is LivingEntity) {
            val rider = event.entered as LivingEntity
            val mount = event.vehicle as LivingEntity
            if (rider.equipment?.boots?.hasItemMeta() == true) {
                val boots = rider.equipment?.boots!!
                for (enchant in boots.enchantments) {
                    when (enchant.key.getNameId()) {
                        "speedy_spurs" -> {
                            speedySpursEnchantment(rider, mount, enchant.value)
                        }
                    }
                }
            }
        }
    }

    // UNUSED
    fun playerInputHandler(event: PlayerInputEvent) {
        val player = event.player
        // Start of ifs
        if (player.equipment!!.boots?.hasItemMeta() == true) {
            val boots = player.equipment!!.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "cloud_strider" -> {
                        cloudStriderEnchantment(player, enchant.value)
                    }
                }
            }
        }
    }

    // Function for sneaking
    @EventHandler
    fun sneakHandler(event: PlayerToggleSneakEvent) {
        val sneaker = event.player
        // Start of ifs
        if (sneaker.equipment!!.helmet?.hasItemMeta() == true) {
            val helmet = sneaker.equipment!!.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "sculk_sensitive" -> {
                        sculkSensitiveSneakEnchantment(sneaker, enchant.value, event.isSneaking)
                    }
                }
            }
        }
        if (sneaker.equipment!!.chestplate?.hasItemMeta() == true) {
            val chestplate = sneaker.equipment!!.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    // Empty
                }
            }
        }
        if (sneaker.equipment!!.leggings?.hasItemMeta() == true) {
            val leggings = sneaker.equipment!!.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "leap_frog" -> {
                        leapFrogSneakEnchantment(sneaker, event.isSneaking)
                    }
                }
            }
        }
        if (sneaker.equipment!!.boots?.hasItemMeta() == true) {
            val boots = sneaker.equipment!!.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "root_boots" -> {
                        rootBootsSneakEnchantment(sneaker, enchant.value, event.isSneaking)
                    }
                    "static_socks" -> {
                        staticSocksSneakEnchantment(sneaker, enchant.value)  // Trigger on one toggle only
                    }
                    "cloud_strider" -> {
                        cloudStriderEnchantment(sneaker, enchant.value)
                    }
                }
            }
        }
        // After many charges -> when hit -> do static discharge
        // Set timer -> if it has not moved -> add is rooted
    }

    // Function for jumping
    @EventHandler
    fun jumpHandler(event: PlayerJumpEvent) {
        val jumper = event.player
        val equipment = jumper.equipment ?: return
        // Leggings
        val leggingEnchants = equipment.leggings?.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        if (leggingEnchants != null) {
            for (enchant in leggingEnchants) {
                when (enchant.key.getNameId()) {
                    "leap_frog" -> {
                        leapFrogEnchantment(event, enchant.value)
                    }
                }
            }
        }
        // Boots
        val bootEnchants = equipment.boots?.getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()
        if (bootEnchants != null) {
            for (enchant in bootEnchants) {
                when (enchant.key.getNameId()) {
                    "cloud_strider" -> {
                        cloudStriderEnchantment(jumper, enchant.value)
                    }
                }
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

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
        return amount * (level * 0.1)
    }

    /*
    private fun beastlyArmorEnchantment(
        defender: LivingEntity,
        enemy: LivingEntity,
        damage: Double,
        level: Int
    ): Double {
        if (defender != enemy) {
            return damage * (level * 0.05)
        }
        return 0.0
    }

     */

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
        if (defender.velocity.length() > 0.08) {
            return (level * 0.1).toFloat()
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

    private fun cloudStriderEnchantment(
        jumper: Player,
        level: Int
    ) {
        // Starting variables
        val maxJumps = level
        val jumpSpeed = 0.75

        println("Detected ${jumper} is trying to jump")
        // If player is on ground, reset jump count
        var currentCloudJumps = jumper.getIntTag(EntityTags.CLOUD_STRIDER_JUMPS) ?: 0
        if (jumper.isOnGround) {
            currentCloudJumps = 0
            jumper.setIntTag(EntityTags.CLOUD_STRIDER_JUMPS, currentCloudJumps)
        }
        // Else if not max jumps, do cloud stride jump
        else if (currentCloudJumps < maxJumps) {
            currentCloudJumps += 1
            jumper.velocity = jumper.velocity.setY(0.0).add(Vector(0.0, jumpSpeed, 0.0)) // Add Y
            jumper.setIntTag(EntityTags.CLOUD_STRIDER_JUMPS, currentCloudJumps)
        }
    }

    private fun devastatingDrop(
        dropper: LivingEntity,
        receivedDamage: Double,
        enchantmentStrength: Int
    ) {
        dropper.location.getNearbyLivingEntities(4.0).forEach {
            if (it != dropper) {
                it.damage(receivedDamage * (0.4 * enchantmentStrength))
            }
        }
    }

    private fun fruitfulFareEnchantment(
        player: Player,
        food: ItemStack,
        level: Int
    ) {
        val fareList = listOf(
            Material.MELON_SLICE,
            Material.APPLE,
            Material.GOLDEN_APPLE,
            Material.GLOW_BERRIES,
            Material.SWEET_BERRIES,
            Material.CHORUS_FRUIT,
            Material.ENCHANTED_GOLDEN_APPLE,
            Material.RABBIT_STEW
        )
        if (player.getCooldown(food.type) != 0) { return }
        if (food.type in fareList) {
            // Check Health
            val currentHealth = player.health
            if (player.getAttribute(Attribute.MAX_HEALTH)!!.value < currentHealth + (1 + level)) {
                player.health = player.getAttribute(Attribute.MAX_HEALTH)!!.value
            } else {
                player.health += (1 + level)
            }
            player.setCooldown(food.type, 20 * 3)
            // Particles
            with(player.world) {
                spawnParticle(Particle.HEART, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.HAPPY_VILLAGER, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, player.location, 35, 0.5, 0.5, 0.5)
                playSound(player.location, Sound.ENTITY_STRIDER_HAPPY, 1.5F, 0.5F)
                playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.5F, 0.5F)
                playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
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
                            PotionEffect(PotionEffectType.BLINDNESS, ((level * 2) + 2) * 20, 1),
                            PotionEffect(PotionEffectType.SLOWNESS, ((level * 2) + 2) * 20, 0)
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

