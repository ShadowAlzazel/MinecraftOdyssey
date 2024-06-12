package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import io.papermc.paper.entity.LookAnchor
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.effects.EffectsManager
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantmentsManager
import me.shadowalzazel.mcodyssey.tasks.enchantment_tasks.SpeedySpursTask
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
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import org.bukkit.inventory.meta.Damageable as Repairable

object ArmorListeners : Listener, EnchantmentsManager, EffectsManager {

    // Pollen
    private val pollenMaxHeadPlayers = mutableMapOf<UUID, Int>()
    private val pollenMaxChestPlayers = mutableMapOf<UUID, Int>()
    private val pollenMaxLegsPlayers = mutableMapOf<UUID, Int>()
    private val pollenMaxBootsPlayers = mutableMapOf<UUID, Int>()

    // Main function for enchantments relating to entity damage for armor
    @EventHandler
    fun mainArmorDefenderHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) { return }
        if (event.entity !is LivingEntity) { return }
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) { return }
        // Make thorns bug new enchant apply ranged effects
        val enemy = event.damager as LivingEntity
        val defender = event.entity as LivingEntity
        // Start of ifs
        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "antibonk" -> {
                        event.damage = antibonkEnchantment(event.isCritical, event.damage, enchant.value)
                    }
                    "beastly" -> {
                        event.damage -= beastlyEnchantment(defender, enchant.value)
                    }
                    "illumineye" -> {
                        illumineyeEnchantment(enemy, defender, enchant.value)
                    }
                    "mandiblemania" -> {
                        mandiblemaniaDefendEnchantment(defender, enemy, enchant.value)
                    }
                    "opticalization" -> {
                        opticalizationHitEnchantment(defender, enemy, enchant.value)
                    }
                    "sslither_ssight" -> {
                        sslitherSsightEnchantment(enemy, defender, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    "relentless" -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "black_rose" -> {
                        blackRoseEnchantment(enemy, enchant.value)
                    }
                    "beastly" -> {
                        event.damage -= beastlyEnchantment(defender, enchant.value)
                    }
                    "ignore_pain" -> {
                        ignorePainEnchantment(defender, enchant.value)
                    }
                    "molten_core" -> {
                        moltenCoreEnchantment(defender, enemy, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    "relentless" -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    "untouchable" -> {
                        untouchableEnchantment(defender)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                    "vengeful" -> {
                        vengefulEnchantment(enemy, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "beastly" -> {
                        event.damage -= beastlyEnchantment(defender, enchant.value)
                    }
                    "cowardice" -> {
                        cowardiceEnchantment(enemy, defender, enchant.value)
                    }
                    "blurcise" -> {
                        event.damage -= blurciseEnchantment(defender, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    "relentless" -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    "sporeful" -> {
                        sporefulEnchantment(defender, enchant.value)
                    }
                    "squidify" -> {
                        squidifyEnchantment(defender, enchant.value)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "beastly" -> {
                        event.damage -= beastlyEnchantment(defender, enchant.value)
                    }
                    "reckless" -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    "relentless" -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    "root_boots" -> {
                        event.damage -= rootBootsDefenseHandler(defender, enchant.value)
                    }
                    "veiled_in_shadow" -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        // Check
        if (event.damage < 0.0) {
            event.damage = 0.0
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
        // Start ifs
        if (attacker.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = attacker.equipment?.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "brawler" -> {
                        event.damage += brawlerEnchantment(attacker, enchant.value)
                    }
                    "mandiblemania" -> {
                        mandiblemaniaAttackEnchantment(attacker, enemy, enchant.value)
                    }
                    "opticalization" -> {
                        opticalizationHitEnchantment(enemy, attacker, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = attacker.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "brawler" -> {
                        event.damage += brawlerEnchantment(attacker, enchant.value)
                    }
                    "vigor" -> {
                        event.damage += viciousVigorEnchantment(attacker, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = attacker.equipment?.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "brawler" -> {
                        event.damage += brawlerEnchantment(attacker, enchant.value)
                    }
                    "impetus" -> {
                        event.damage += impetusEnchantment(attacker, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.boots?.hasItemMeta() == true) {
            val boots = attacker.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "brawler" -> {
                        event.damage += brawlerEnchantment(attacker, enchant.value)
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
        if (player.equipment.helmet?.hasItemMeta() == true) {
            val helmet = player.equipment.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "brewful_breath" -> {
                        brewfulBreathEnchantment(player, event.item, enchant.value)
                    }
                }
            }
        }
        if (player.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment.chestplate!!
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

    // Main function for enchantments relating to using items
    @EventHandler
    fun mainArmorUsageHandler(event: PlayerInteractEvent) {
        if (event.item == null) { return }
        if (event.item!!.type !in listOf(Material.SPYGLASS, Material.GOAT_HORN)) { return }
        val player = event.player
        val item = event.item!!
        if (player.equipment.helmet?.hasItemMeta() == true) {
            val helmet = player.equipment.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    // ALL REMOVED MOVED TO OWN
                }
            }
        }
        if (player.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key.getNameId()) {
                }
            }
        }

    }

    @EventHandler
    fun passiveRegenHandler(event: EntityRegainHealthEvent) {
        if (event.entity !is LivingEntity) return
        if (event.regainReason != EntityRegainHealthEvent.RegainReason.SATIATED) return
        // Armor Holder
        val defender = event.entity as LivingEntity
        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "chitin" -> {
                        chitinEnchantment(defender, helmet)
                    }
                    "reckless" -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "chitin" -> {
                        chitinEnchantment(defender, chestplate)
                    }
                    "reckless" -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "chitin" -> {
                        chitinEnchantment(defender, leggings)
                    }
                    "reckless" -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "chitin" -> {
                        chitinEnchantment(defender, boots)
                    }
                    "reckless" -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
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

    // Function for knockback effects
    @EventHandler
    fun knockBackHandler(event: EntityKnockbackByEntityEvent) {
        val defender = event.entity
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "root_boots" -> {
                        //TODO
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
        if (sneaker.equipment.helmet?.hasItemMeta() == true) {
            val helmet = sneaker.equipment.helmet!!
            for (enchant in helmet.enchantments) {
                when (enchant.key.getNameId()) {
                    "pollen_gard"-> {
                        pollenGuardSneakEnchantment(sneaker, enchant.value, pollenMaxHeadPlayers, event.isSneaking)
                    }
                    "sculk_sensitive" -> {
                        sculkSensitiveSneakEnchantment(sneaker, enchant.value, event.isSneaking)
                    }
                }
            }
        }
        if (sneaker.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = sneaker.equipment.chestplate!!
            for (enchant in chestplate.enchantments) {
                when (enchant.key.getNameId()) {
                    "pollen_gard" -> {
                        pollenGuardSneakEnchantment(sneaker, enchant.value, pollenMaxChestPlayers, event.isSneaking)
                    }
                }
            }
        }
        if (sneaker.equipment.leggings?.hasItemMeta() == true) {
            val leggings = sneaker.equipment.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "leap_frog" -> {
                        leapFrogSneakEnchantment(sneaker, event.isSneaking)
                    }
                    "pollen_gard" -> {
                        pollenGuardSneakEnchantment(sneaker, enchant.value, pollenMaxLegsPlayers, event.isSneaking)
                    }
                }
            }
        }
        if (sneaker.equipment.boots?.hasItemMeta() == true) {
            val boots = sneaker.equipment.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "pollen_gard" -> {
                        pollenGuardSneakEnchantment(sneaker, enchant.value, pollenMaxBootsPlayers, event.isSneaking)
                    }
                    "root_boots" -> {
                        rootBootsSneakEnchantment(sneaker, enchant.value, event.isSneaking)
                    }
                    "static_socks" -> {
                        staticSocksSneakEnchantment(sneaker, enchant.value)  // Trigger on one toggle only
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
        if (jumper.equipment.leggings?.hasItemMeta() == true) {
            val leggings = jumper.equipment.leggings!!
            for (enchant in leggings.enchantments) {
                when (enchant.key.getNameId()) {
                    "leap_frog" -> {
                        leapFrogEnchantment(jumper, enchant.value)
                    }
                }
            }
        }
        if (jumper.equipment.boots?.hasItemMeta() == true) {
            val boots = jumper.equipment.boots!!
            for (enchant in boots.enchantments) {
                when (enchant.key.getNameId()) {
                    "leap_frog" -> {
                    }
                }
            }
        }
    }

    private fun getRayTraceTarget(player: Player, maxRange: Int=100): Entity? {
        val result = player.rayTraceEntities(maxRange) ?: return null
        val target = result.hitEntity ?: return null
        val distance = player.eyeLocation.distance(target.location)
        if (maxRange < distance) { return null }
        return target
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun antibonkEnchantment(
        isCrit: Boolean,
        damage: Double,
        level: Int,
    ): Double {
        return if (isCrit) {
            maxOf(damage - (level * 2.5), 0.0)
        } else {
            damage
        }
    }
    private fun brawlerEnchantment(
        defender: LivingEntity,
        level: Int
    ): Double {
        if (defender.location.getNearbyLivingEntities(4.0).size >= 2 * level) {
            return level * 1.0
        }
        return 0.0
    }
    private fun blackRoseEnchantment(
        attacker: LivingEntity,
        level: Int
    ) {
        attacker.addPotionEffect(
            PotionEffect(
                PotionEffectType.WITHER,
                5 * 20 * level,
                1
            )
        )
    }
    private fun blurciseEnchantment(
        defender: LivingEntity,
        level: Int
    ): Double {
        if (defender.velocity.length() > 1.8) {
            return level * 1.0
        }
        return 0.0
    }
    private fun beastlyEnchantment(
        attacker: LivingEntity,
        level: Int
    ): Double {
        if (attacker.location.getNearbyLivingEntities(4.0).size >= 2 * level) {
            return level * 1.0
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
                color = potionMeta.color!!
            }
            radius = 0.5F + (0.5F * level)
            radiusPerTick = -0.05F
            reapplicationDelay = 20
            addScoreboardTag(EntityTags.BREATH_CLOUD)
            addScoreboardTag(EntityTags.BREATH_BY + player.uniqueId)
        }

        player.setCooldown(potion.type, 20 * 6)
    }
    private fun chitinEnchantment(
        defender: LivingEntity,
        armor: ItemStack
    ) {
        if (defender.health <= 0.0) return
        val armorMeta = armor.itemMeta
        if (armorMeta !is Repairable) return
        if (!armorMeta.hasDamage()) return
        armorMeta.damage -= 1
        armor.itemMeta = armorMeta
        return
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
    private fun dreadfulShriekEnchantment(
        player: Player,
        horn: ItemStack,
        level: Int
    ) {
        if (horn.type != Material.GOAT_HORN) { return }
        if (player.getCooldown(horn.type) != 0) { return }

        val enemies = player.getNearbyEntities(16.0, 6.0, 16.0).filter { it !is Player && it is LivingEntity }
        enemies.forEach {
            (it as LivingEntity).addPotionEffects(
                listOf(
                    PotionEffect(
                        PotionEffectType.WEAKNESS,
                        (2 + (level * 2)) * 20,
                        0
                    ),
                    PotionEffect(
                        PotionEffectType.SLOWNESS,
                        (2 + (level * 2)) * 20,
                        0
                    )
                )
            )
            it.world.spawnParticle(Particle.SCULK_CHARGE_POP, it.location, 15, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_WARDEN_ROAR, 7.5F, 1.5F)
        player.world.playSound(player.location, Sound.ENTITY_FOX_AGGRO, 7.5F, 2.5F)
        player.setCooldown(horn.type, 20 * 6)
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
            if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value < currentHealth + (1 + level)) {
                player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
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
        val angle = attacker.eyeLocation.direction.angle(defender.eyeLocation.direction)
        if (angle < 1.74533) return
        attacker.addPotionEffect(
            PotionEffect(PotionEffectType.GLOWING, (3 + (level * 2)) * 20, 0))

    }
    private fun illumineyeSpyglassEnchantment(
        player: Player,
        spyglass: ItemStack,
        level: Int) {
        if (spyglass.type != Material.SPYGLASS) { return }
        if (player.getCooldown(spyglass.type) != 0) { return }

        val target = getRayTraceTarget(player, 100) ?: return
        if (target is LivingEntity) {
            target.addPotionEffect(
                PotionEffect(
                    PotionEffectType.GLOWING,
                    (3 + (level * 2)) * 20,
                    0
                )
            )
        }
        player.setCooldown(spyglass.type, 20 * 2)
    }
    private fun impetusEnchantment(
        wearer: LivingEntity,
        level: Int
    ): Double {
        if (wearer.velocity.length() > 1.8) {
            return level * 1.0
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
        jumper: LivingEntity,
        level: Int
    ) {
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
            jumper.velocity = jumper.velocity.multiply(1.0 + (0.4 * level))
        }
        if (jumper.scoreboardTags.contains(EntityTags.LEAP_FROG_READY)) {
            jumper.velocity = jumper.velocity.multiply(1.0 + (0.4 * level))
            jumper.scoreboardTags.remove(EntityTags.LEAP_FROG_READY)
        }

    }
    private fun mandiblemaniaAttackEnchantment(
        attacker: LivingEntity,
        enemy: LivingEntity,
        level: Int
    ) {
       if (enemy.eyeLocation.y < attacker.eyeLocation.y) {
           enemy.noDamageTicks = maxOf(enemy.noDamageTicks - (2 * level), 0)
       }
    }

    private fun mandiblemaniaDefendEnchantment(
        defender: LivingEntity,
        enemy: LivingEntity,
        level: Int
    ) {
        if (enemy.eyeLocation.y < defender.eyeLocation.y) {
            enemy.noDamageTicks = maxOf(enemy.noDamageTicks - (2 * level), 0)
        }
    }
    private fun moltenCoreEnchantment(
        defender: LivingEntity,
        attacker: LivingEntity,
        level: Int
    ) {
        val location = defender.location.clone().toBlockLocation().apply {
            y -= 0.75
        }
        // Fire
        if (defender.fireTicks > 40) {
            attacker.fireTicks += ((level * 2) * 2) * 20
        } else {
            attacker.fireTicks += (level * 2) * 20
        }

        // Saturation
        if (defender !is Player) return
        val block = location.block
        val blockValid = (block.type in listOf(Material.LAVA, Material.MAGMA_BLOCK))
        if (blockValid) {
            defender.saturation = minOf(defender.saturation + (0.5F * level)  , 20.0F)
        }
    }
    private fun opticalizationSpyglassEnchantment(
        player: Player,
        spyglass: ItemStack) {

        if (spyglass.type != Material.SPYGLASS) { return }
        if (player.getCooldown(spyglass.type) != 0) { return }

        val target = getRayTraceTarget(player, 100) ?: return
        if (target is Mob) {
            target.lookAt(player)
            player.lookAt(target, LookAnchor.EYES, LookAnchor.EYES)
        } else if (target is Player) {
            target.lookAt(player, LookAnchor.EYES, LookAnchor.EYES)
            player.lookAt(target, LookAnchor.EYES, LookAnchor.EYES)
        }
        player.setCooldown(spyglass.type, 20 * 2)
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
        val maxStacks = ((pollenMaxHeadPlayers[defender.uniqueId] ?: 0) + (pollenMaxChestPlayers[defender.uniqueId] ?: 0)
                + (pollenMaxLegsPlayers[defender.uniqueId] ?: 0) + (pollenMaxBootsPlayers[defender.uniqueId] ?: 0))
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
        player.addOdysseyEffect(EffectTags.BARRIER, (4 + (level * 4)) * 20)
        player.setCooldown(potion.type, (2 + (level * 2)) * 20)
        // Particles
        with(player.world) {
            spawnParticle(Particle.ELECTRIC_SPARK, player.location, 35, 0.5, 0.5, 0.5)
            spawnParticle(Particle.COMPOSTER, player.location, 35, 0.5, 0.5, 0.5)
            playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
            playSound(player.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
            playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
        }
    }
    private fun ragingRoarEnchantment(
        player: Player,
        horn: ItemStack,
        level: Int
    ) {
        if (horn.type != Material.GOAT_HORN) { return }
        if (player.getCooldown(horn.type) != 0) { return }

        val enemies = player.getNearbyEntities(10.0 + (level * 5), 6.0, 10.0 + (level * 5)).filter { it !is Player && it is Creature }
        enemies.forEach {
            (it as Creature).lookAt(player)
            it.pathfinder.moveTo(player)
            it.target = player
            it.world.spawnParticle(Particle.FALLING_OBSIDIAN_TEAR, it.location, 15, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_RAVAGER_ROAR, 7.5F, 1.5F)
        player.setCooldown(horn.type, 20 * 6)
    }
    private fun recklessEnchantment(level: Int): Double {
        return level * 0.5
    }

    private fun recklessRegenEnchantment(level: Int): Double {
        return level * 1.0
    }
    private fun relentlessEnchantment(defender: LivingEntity, level: Int) {
        if (defender is HumanEntity) {
            defender.saturation = minOf(defender.saturation + (0.25F + (0.25F * level)), 20.0F)
        } else {
            defender.health += (0.5)
        }
    }
    private fun rootBootsKnockbackHandler(defender: LivingEntity, level: Int): Double {
        // Sentries
        if (defender.isSwimming) return 1.0
        if (defender.isRiptiding) return 1.0

        // blocks
        val rootBlocks = listOf(Material.DIRT, Material.ROOTED_DIRT, Material.COARSE_DIRT,
            Material.MANGROVE_ROOTS, Material.MUDDY_MANGROVE_ROOTS, Material.MUD,
            Material.FARMLAND, Material.GRASS_BLOCK, Material.MYCELIUM)

        val blockUnder = defender.location.clone().add(0.0, -1.0, 0.0).block
        val blockMultiplier = if (blockUnder.type in rootBlocks) 2.0 else 1.0
        var rootValue = (0.2 + (0.1 * level))

        //40 25 10
        if (!defender.scoreboardTags.contains(EntityTags.ROOT_BOOTS_ROOTED)) {
            rootValue -= 0.2
        }
        return maxOf((1.0 - (rootValue * blockMultiplier)), 0.0)
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
    private fun sslitherSsightSpyglassEnchantment(
        player: Player,
        spyglass: ItemStack,
        level: Int) {

        if (spyglass.type != Material.SPYGLASS) { return }
        if (player.getCooldown(spyglass.type) != 0) { return }

        val target = getRayTraceTarget(player, 100) ?: return
        if (target !is LivingEntity) { return }
        val angle = target.eyeLocation.direction.angle(player.eyeLocation.direction)
        //println(angle)
        if (angle < 1.74533) { return } // a > 100 deg

        target.addPotionEffect(
            PotionEffect(PotionEffectType.SLOWNESS, (level) * 10, 5))

        player.setCooldown(spyglass.type, 20 * 2)
    }
    private fun sslitherSsightEnchantment(
        attacker: LivingEntity,
        defender: LivingEntity,
        level: Int) {
        if (!attacker.hasLineOfSight(defender)) return
        if (!defender.hasLineOfSight(attacker)) return
        if (!defender.hasLineOfSight(attacker.eyeLocation)) return
        val angle = attacker.eyeLocation.direction.angle(defender.eyeLocation.direction)
        if (angle < 1.74533) return
        attacker.addPotionEffect(
            PotionEffect(PotionEffectType.SLOWNESS, (level) * 10, 5))

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
        defender: LivingEntity
    ) {
        if (defender.noDamageTicks < 20) {
            defender.noDamageTicks = 20
        } else {
            defender.noDamageTicks += 10 // Add 0.5 seconds more of immunity
        }
    }
    private fun veiledInShadowEnchantment(
        defender: LivingEntity,
        level: Int) {
        val shadowLevelBlock = 10 - defender.location.block.lightFromBlocks + level
        val shadowLevelSky = if (!defender.world.isDayTime) { 10 + level }
        else { 10 - defender.location.block.lightFromSky + level }
        defender.noDamageTicks += maxOf(minOf(shadowLevelBlock, shadowLevelSky), 0)
        // gain 15 ticks of immunity when in full darkness with lvl 5
    }
    private fun vengefulEnchantment(
        attacker: LivingEntity,
        level: Int) {
        attacker.addScoreboardTag(EntityTags.MARKED_FOR_VENGEANCE)
        attacker.addScoreboardTag(EntityTags.VENGEFUL_MODIFIER + level)
        // Add Particles Timer?
    }
    private fun viciousVigorEnchantment(
        attacker: LivingEntity,
        level: Int): Double {
        if (attacker.health <= 8.0) {
            return level * 1.0
        }
        return 0.0
    }
    private fun warCryEnchantment(
        player: Player,
        horn: ItemStack,
        level: Int
    ) {
        if (horn.type != Material.GOAT_HORN) { return }
        if (player.getCooldown(horn.type) != 0) { return }
        val isPet = { entity: Entity -> entity is Tameable && entity.owner == player }

        val allies = player.getNearbyEntities(16.0, 6.0, 16.0).filter { it is Player || isPet(it) }
        allies.forEach {
            it as LivingEntity
            it.addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.STRENGTH, (4 + (level * 2)) * 20, 0),
                    PotionEffect(PotionEffectType.SPEED, (4 + (level * 2)) * 20, 0)
                )
            )
            it.world.spawnParticle(Particle.NOTE, it.location, 5, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_RAVAGER_ROAR, 7.5F, 1.5F)
        player.setCooldown(horn.type, 20 * 6)
    }

}

