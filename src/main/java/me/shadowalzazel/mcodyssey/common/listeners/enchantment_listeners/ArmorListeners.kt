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

    // Main function for enchantments relating to entity damage for armor
    @EventHandler
    fun mainArmorDefenderHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) return
        if (event.entity !is LivingEntity) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return
        // Make thorns bug new enchant apply ranged effects
        val enemy = event.damager as LivingEntity
        val defender = event.entity as LivingEntity
        val originalAmount = event.damage
        // RESET DAMAGE TICKS
        val bonusImmunityTime = mutableListOf(0) // Reference
        defender.maximumNoDamageTicks = 20
        // Start of ifs
        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "antibonk" -> {
                        event.damage -= antibonkEnchantment(event.isCritical, originalAmount, enchant.value)
                    }
                    "beastly" -> {
                        event.damage -= beastlyArmorEnchantment(defender, enemy, originalAmount, enchant.value)
                    }
                    "brawler" -> {
                        event.damage -= brawlerArmorEnchantment(defender, originalAmount, enchant.value)
                    }
                    "illumineye" -> {
                        illumineyeEnchantment(enemy, defender, enchant.value)
                    }
                    "opticalization" -> {
                        opticalizationHitEnchantment(defender, enemy, enchant.value)
                    }
                    "sslither_ssight" -> {
                        sslitherSsightEnchantment(enemy, defender, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessDefenderEnchantment(originalAmount, enchant.value)
                    }
                    "relentless" -> {
                        event.damage -= relentlessEnchantment(defender, enchant.value, originalAmount)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value, bonusImmunityTime)
                    }
                }
            }
        }
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "black_rose" -> {
                        stxRoseHitEnchantment(enemy, enchant.value)
                    }
                    "beastly" -> {
                        event.damage -= beastlyArmorEnchantment(defender, enemy, originalAmount, enchant.value)
                    }
                    "brawler" -> {
                        event.damage -= brawlerArmorEnchantment(defender, originalAmount, enchant.value)
                    }
                    "ignore_pain" -> {
                        ignorePainEnchantment(defender, enchant.value)
                    }
                    "molten_core" -> {
                        moltenCoreEnchantment(defender, enemy, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessDefenderEnchantment(originalAmount, enchant.value)
                    }
                    "relentless" -> {
                        event.damage -= relentlessEnchantment(defender, enchant.value, originalAmount)
                    }
                    "untouchable" -> {
                        untouchableEnchantment(defender, bonusImmunityTime)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value, bonusImmunityTime)
                    }
                }
            }
        }
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "cowardice" -> {
                        cowardiceEnchantment(enemy, defender, enchant.value)
                    }
                    "beastly" -> {
                        event.damage -= beastlyArmorEnchantment(defender, enemy, originalAmount, enchant.value)
                    }
                    "brawler" -> {
                        event.damage -= brawlerArmorEnchantment(defender, originalAmount, enchant.value)
                    }
                    "blurcise" -> {
                        event.damage -= blurciseEnchantment(defender, enchant.value, originalAmount)
                    }
                    "reckless" -> {
                        event.damage += recklessDefenderEnchantment(originalAmount, enchant.value)
                    }
                    "relentless" -> {
                        event.damage -= relentlessEnchantment(defender, enchant.value, originalAmount)
                    }
                    "sporeful" -> {
                        sporefulEnchantment(defender, enchant.value)
                    }
                    "squidify" -> {
                        squidifyEnchantment(defender, enchant.value)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value, bonusImmunityTime)
                    }
                }
            }
        }
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "beastly" -> {
                        event.damage -= beastlyArmorEnchantment(defender, enemy, originalAmount, enchant.value)
                    }
                    "brawler" -> {
                        event.damage -= brawlerArmorEnchantment(defender, originalAmount, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessDefenderEnchantment(originalAmount, enchant.value)
                    }
                    "relentless" -> {
                        event.damage -= relentlessEnchantment(defender, enchant.value, originalAmount)
                    }
                    "root_boots" -> {
                        event.damage -= rootBootsDefenseHandler(defender, enchant.value)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value, bonusImmunityTime)
                    }
                }
            }
        }
        // Edit Damage Ticks
        if (bonusImmunityTime[0] > 0) {
            val immunityTicks = bonusImmunityTime[0]
            defender.maximumNoDamageTicks = 20 + immunityTicks
            defender.noDamageTicks = 10 + immunityTicks
        }
        /*
        println("Immunity")
        println(defender.maximumNoDamageTicks)
        println(defender.noDamageTicks)

         */

        // Check
        if (event.damage < 0.0) {
            event.damage = 0.0
        }
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

    // Main function for enchantments relating to entity damage for armor
    @EventHandler
    fun mainArmorAttackHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) return
        if (event.entity !is LivingEntity) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return
        val attacker = event.damager as LivingEntity
        val enemy = event.entity as LivingEntity
        val originalAmount = event.damage
        // Start ifs
        if (attacker.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = attacker.equipment?.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "vigor" -> {
                        event.damage += vigorEnchantment(attacker, enchant.value, originalAmount)
                    }
                    "mandiblemania" -> {
                        mandiblemaniaEnchantment(attacker, enchant.value)
                    }
                    "opticalization" -> {
                        opticalizationHitEnchantment(enemy, attacker, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessAttackerEnchantment(originalAmount, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = attacker.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "vigor" -> {
                        event.damage += vigorEnchantment(attacker, enchant.value, originalAmount)
                    }
                    "reckless" -> {
                        event.damage += recklessAttackerEnchantment(originalAmount, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = attacker.equipment?.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "vigor" -> {
                        event.damage += vigorEnchantment(attacker, enchant.value, originalAmount)
                    }
                    "impetus" -> {
                        event.damage += impetusEnchantment(attacker, enchant.value, originalAmount)
                    }
                    "reckless" -> {
                        event.damage += recklessAttackerEnchantment(originalAmount, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.boots?.hasItemMeta() == true) {
            val boots = attacker.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "vigor" -> {
                        event.damage += vigorEnchantment(attacker, enchant.value, originalAmount)
                    }
                    "reckless" -> {
                        event.damage += recklessAttackerEnchantment(originalAmount, enchant.value)
                    }
                    "static_socks" -> {
                        event.damage += staticSocksAttackEnchantment(attacker, enchant.value)
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
        damage: Double,
        level: Int,
    ): Double {
        return if (isCrit) {
            damage * (level * 0.1)
        } else {
            0.0
        }
    }

    private fun revitalizeEnchant(
        amount: Double,
        level: Int,
    ): Double {
        return amount * (level * 0.1)
    }

    private fun beastlyArmorEnchantment(
        defender: LivingEntity,
        enemy: LivingEntity,
        damage: Double,
        level: Int
    ): Double {
        if (defender != enemy) { // 3 + self
            return damage * (level * 0.2)
        }
        return 0.0
    }

    private fun brawlerArmorEnchantment(
        defender: LivingEntity,
        damage: Double,
        level: Int
    ): Double {
        if (defender.location.getNearbyLivingEntities(6.0).size >= 4) { // 3 + self
            return damage * (level * 0.5)
        }
        return 0.0
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
        amount: Double,
    ): Double {
        if (defender.velocity.length() > 0.5) {
            return amount * (level * 0.1)
        }
        return 0.0
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
        if (!defender.hasLineOfSight(attacker.eyeLocation)) return
        // val angle = attacker.eyeLocation.direction.angle(defender.eyeLocation.direction)
        //if (angle < 1.74533) return
        attacker.addPotionEffect(
            PotionEffect(PotionEffectType.GLOWING, (3 + (level * 2)) * 20, 0))
    }

    private fun impetusEnchantment(
        wearer: LivingEntity,
        level: Int,
        amount: Double
    ): Double {
        if (wearer.velocity.length() > 0.5) {
            return amount * (0.05 + (level * 0.05))
        }
        return 0.0
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
        damage: Double,
        level: Int
    ): Double {
        return damage * (level * 0.05)
    }

    private fun recklessDefenderEnchantment(
        damage: Double,
        level: Int
    ): Double {
        return damage * (level * 0.05)
    }


    private fun relentlessEnchantment(
        defender: LivingEntity,
        level: Int,
        amount: Double): Double {
        val maxHealth = defender.getAttribute(Attribute.MAX_HEALTH)?.value ?: return 0.0
        val currentHealthPercent = defender.health / maxHealth
        if (currentHealthPercent <= 0.4) {
            return amount * (level * 0.05)
        }
        return 0.0
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
        val nearby = sneaker.world.getNearbyLivingEntities(sneaker.location, 5.0 + (level * 5)).filter { it != sneaker }
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
        level: Int
    ): Double {
        if (!attacker.scoreboardTags.contains(EntityTags.STATIC_SOCKS_CHARGING)) return 0.0
        val charge = attacker.getIntTag(EntityTags.STATIC_SOCKS_CHARGE)!!
        // Remove
        attacker.removeTag(EntityTags.STATIC_SOCKS_CHARGE)
        attacker.removeScoreboardTag(EntityTags.STATIC_SOCKS_CHARGING)
        attacker.world.spawnParticle(Particle.ELECTRIC_SPARK, attacker.location, charge * 3, 0.7, 0.5, 0.7)
        attacker.world.playSound(attacker.location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.5F, 4.5F)
        return minOf((charge / 2.0), level * 1.0)
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
        /*
        if (defender.noDamageTicks < 20) {
            defender.noDamageTicks = 20 // Add more immunity time
        } else {
            defender.noDamageTicks += 10 // Add more immunity time
        }
        defender.noDamageTicks += 10 // Add more immunity time
         */
        //defender.noDamageTicks += 10
        //defender.maximumNoDamageTicks = defender.noDamageTicks
        bonusImmunityTime[0] += 10
    }

    private fun veiledInShadowEnchantment(
        defender: LivingEntity,
        level: Int,
        bonusImmunityTime: MutableList<Int>) {
        val shadowLevelBlock = 10 + level - defender.location.block.lightFromBlocks
        val shadowLevelSky = if (!defender.world.isDayTime) { 10 + level }
        else { 10 + level - defender.location.block.lightFromSky}
        val shadowTicks = maxOf(minOf(shadowLevelBlock, shadowLevelSky), 0)
        // gain at most 15 ticks of extra immunity when in full darkness with lvl 5
        if (shadowTicks > 0) {
            //defender.noDamageTicks += shadowTicks
            //defender.maximumNoDamageTicks = defender.noDamageTicks
            bonusImmunityTime[0] += shadowTicks
        }
    }

    private fun vigorEnchantment(
        attacker: LivingEntity,
        level: Int,
        amount: Double): Double {
        val maxHealth = attacker.getAttribute(Attribute.MAX_HEALTH)?.value ?: return 0.0
        val currentHealthPercent = attacker.health / maxHealth
        if (currentHealthPercent >= 0.40) {
            return amount * (level * 0.5)
        }
        return 0.0
    }

}

