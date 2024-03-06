package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import io.papermc.paper.entity.LookAnchor
import io.papermc.paper.world.MoonPhase
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.AlchemyListener.addOdysseyEffect
import me.shadowalzazel.mcodyssey.listeners.utility.MoonwardPhase
import me.shadowalzazel.mcodyssey.tasks.SpeedySpursTask
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
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
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import org.bukkit.inventory.meta.Damageable as Repairable
object ArmorListeners : Listener {

    private val moonwardPhasePlayers = mutableListOf<UUID>()

    // Main function for enchantments relating to entity damage for armor
    @EventHandler
    fun mainArmorDefenderHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) { return }
        if (event.entity !is LivingEntity) { return }
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) { return }

        // Make thorns bug new enchant apply ranged effects
        val enemy = event.damager as LivingEntity
        val defender = event.entity as LivingEntity
        // All Armors

        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ANTIBONK -> {
                        event.damage = antibonkEnchantment(event.isCritical, event.damage, enchant.value)
                    }
                    OdysseyEnchantments.ILLUMINEYE -> {
                        illumineyeEnchantment(enemy, defender, enchant.value)
                    }
                    OdysseyEnchantments.MANDIBLEMANIA -> {
                        mandiblemaniaDefendEnchantment(defender, enemy, enchant.value)
                    }
                    OdysseyEnchantments.OPTICALIZATION -> {
                        opticalizationHitEnchantment(defender, enemy, enchant.value)
                    }
                    OdysseyEnchantments.SSLITHER_SSIGHT -> {
                        sslitherSsightEnchantment(enemy, defender, enchant.value)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    OdysseyEnchantments.RELENTLESS -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.VEILED_IN_SHADOW -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.BLACK_ROSE -> {
                        blackRoseEnchantment(enemy, enchant.value)
                    }
                    OdysseyEnchantments.IGNORE_PAIN -> {
                        ignorePainEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.MOLTEN_CORE -> {
                        moltenCoreEnchantment(defender, enemy, enchant.value)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    OdysseyEnchantments.RELENTLESS -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.UNTOUCHABLE -> {
                        untouchableEnchantment(defender)
                    }
                    OdysseyEnchantments.VEILED_IN_SHADOW -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.VENGEFUL -> {
                        vengefulEnchantment(enemy, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings
            for (enchant in leggings!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COWARDICE -> {
                        cowardiceEnchantment(enemy, defender, enchant.value)
                    }
                    OdysseyEnchantments.BLURCISE -> {
                        event.damage -= blurciseEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    OdysseyEnchantments.RELENTLESS -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.SPOREFUL -> {
                        sporefulEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.SQUIDIFY -> {
                        squidifyEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.VEILED_IN_SHADOW -> {
                        veiledInShadowEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.RECKLESS -> {
                        event.damage += recklessEnchantment(enchant.value)
                    }
                    OdysseyEnchantments.RELENTLESS -> {
                        relentlessEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.VEILED_IN_SHADOW -> {
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

        if (attacker.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = attacker.equipment?.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.MANDIBLEMANIA -> {
                        mandiblemaniaAttackEnchantment(attacker, enemy, enchant.value)
                    }
                    OdysseyEnchantments.OPTICALIZATION -> {
                        opticalizationHitEnchantment(enemy, attacker, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = attacker.equipment?.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.BEASTLY_BRAWLER -> {
                        event.damage += beastlyBrawlerEnchantment(attacker, enchant.value)
                    }
                    OdysseyEnchantments.VICIOUS_VIGOR -> {
                        event.damage += viciousVigorEnchantment(attacker, enchant.value)
                    }
                }
            }
        }
        if (attacker.equipment?.boots?.hasItemMeta() == true) {
            val boots = attacker.equipment?.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.STATIC_SOCKS -> {
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
                val boots = defender.equipment?.boots
                for (enchant in boots!!.enchantments) {
                    when (enchant.key) {
                        OdysseyEnchantments.DEVASTATING_DROP -> {
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
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
        }

    }

    // Main function for enchantments relating to consuming items
    @EventHandler
    fun mainArmorConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        // Check if helmet item has lore
        if (player.equipment.helmet?.hasItemMeta() == true) {
            val helmet = player.equipment.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.BREWFUL_BREATH -> {
                        brewfulBreathEnchantment(player, event.item, enchant.value)
                    }
                }
            }
        }
        // Check if chestplate item has lore
        if (player.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.FRUITFUL_FARE -> {
                        fruitfulFareEnchantment(player, event.item, enchant.value)
                    }
                    // Add if eat rotten or raw heal + other
                    OdysseyEnchantments.POTION_BARRIER -> {
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
            val helmet = player.equipment.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.DREADFUL_SHRIEK -> {
                        dreadfulShriekEnchantment(player, item, enchant.value)
                    }
                    OdysseyEnchantments.ILLUMINEYE -> {
                        illumineyeSpyglassEnchantment(player, item, enchant.value)
                    }
                    OdysseyEnchantments.OPTICALIZATION -> {
                        opticalizationSpyglassEnchantment(player, item)
                    }
                    OdysseyEnchantments.RAGING_ROAR -> {
                        ragingRoarEnchantment(player, item, enchant.value)
                    }
                    OdysseyEnchantments.SSLITHER_SSIGHT -> {
                        sslitherSsightSpyglassEnchantment(player, item, enchant.value)
                    }
                    OdysseyEnchantments.WAR_CRY -> {
                        warCryEnchantment(player, item, enchant.value)
                    }
                }
            }
        }
        if (player.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
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
            val helmet = defender.equipment?.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        copperChitinEnchantment(defender, helmet)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        copperChitinEnchantment(defender, chestplate)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings
            for (enchant in leggings!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        copperChitinEnchantment(defender, leggings)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        copperChitinEnchantment(defender, boots)
                    }
                    OdysseyEnchantments.RECKLESS -> {
                        event.amount += recklessRegenEnchantment(enchant.value)
                    }
                }
            }
        }
    }

    @EventHandler
    fun itemDurabilityHandler(event: PlayerItemDamageEvent) {
        if (!event.item.hasItemMeta()) return
        // Copper Chitin

        if (event.item.enchantments.containsKey(OdysseyEnchantments.MOONWARD)) {
            moonwardEnchantment(event)
        }

    }

    // Function regarding vehicles and armor
    @EventHandler
    fun miscArmorVehicleHandler(event: VehicleEnterEvent) { // Move Horse Armor Enchant Here
        if (event.entered is LivingEntity && event.vehicle is LivingEntity) {
            val rider = event.entered as LivingEntity
            val mount = event.vehicle as LivingEntity
            // Check if boot item has lore
            if (rider.equipment?.boots?.hasItemMeta() == true) {
                val boots = rider.equipment?.boots
                // Loop for all enchants
                for (enchant in boots!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.SPEEDY_SPURS -> {
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
            val boots = defender.equipment?.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ROOT_BOOTS -> {
                        event.acceleration.multiply(rootBootsHitEnchantment(defender, enchant.value))
                    }
                }
            }
        }
    }

    // Function for sneaking
    @EventHandler
    fun sneakHandler(event: PlayerToggleSneakEvent) {
        val sneaker = event.player
        if (sneaker.equipment.leggings?.hasItemMeta() == true) {
            val leggings = sneaker.equipment.leggings
            for (enchant in leggings!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ROOT_BOOTS -> {
                        leapFrogSneakEnchantment(sneaker, event.isSneaking)
                    }
                }
            }
        }
        if (sneaker.equipment.boots?.hasItemMeta() == true) {
            val boots = sneaker.equipment.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ROOT_BOOTS -> {
                        rootBootsSneakEnchantment(sneaker, enchant.value, event.isSneaking)
                    }
                    OdysseyEnchantments.STATIC_SOCKS -> {
                        // Trigger on one toggle only
                        staticSocksSneakEnchantment(sneaker, enchant.value)
                    }
                }
            }
        }

        // After many charges -> when hit -> do static discharge

        // Set timer -> if has not moved -> add is rooted
    }

    // Function for jumping
    @EventHandler
    fun jumpHandler(event: PlayerJumpEvent) {
        val jumper = event.player
        if (jumper.equipment.leggings?.hasItemMeta() == true) {
            val leggings = jumper.equipment.leggings
            for (enchant in leggings!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.LEAP_FROG -> {
                        leapFrogEnchantment(jumper, enchant.value)
                    }
                }
            }
        }
        if (jumper.equipment.boots?.hasItemMeta() == true) {
            val boots = jumper.equipment.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.LEAP_FROG -> {

                    }
                }
            }
        }

    }

    private fun getRayTraceTarget(player: Player, maxRange: Int): Entity? {
        val result = player.rayTraceEntities(maxRange) ?: return null
        val target = result.hitEntity ?: return null
        val distance = player.eyeLocation.distance(target.location)
        if (maxRange < distance) { return null }
        return target
    }


    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    /*---------------------------------------------ANTIBONK------------------------------------------*/
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


    /*----------------------------------------BEASTLY_BRAWLER----------------------------------------*/
    private fun beastlyBrawlerEnchantment(
        defender: LivingEntity,
        level: Int
    ): Double {
        if (defender.location.getNearbyLivingEntities(4.0).size >= level) {
            return level * 2.0
        }
        return 0.0
    }

    // ------------------------------- BLACK_ROSE ------------------------------------
    private fun blackRoseEnchantment(
        attacker: LivingEntity,
        level: Int
    ) {
        attacker.addPotionEffect(
            PotionEffect(
                PotionEffectType.WITHER,
                5 * 20,
                level - 1
            )
        )
    }

    private fun blurciseEnchantment(
        defender: LivingEntity,
        level: Int
    ): Double {
        if (defender.velocity.length() > 2.0) {
            return level * 1.0
        }
        return 0.0
    }


    // ------------------------------- BREWFUL_BREATH ------------------------------------
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
            basePotionData = potionMeta.basePotionData
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

    // ------------------------------- COPPER_CHITIN ------------------------------------
    private fun copperChitinEnchantment(
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

    // ------------------------------- COWARDICE ------------------------------------
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

    // ------------------------------- DEVASTATING_DROP ------------------------------------
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

    // ------------------------------- DREADFUL_SHRIEK ------------------------------------
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
                        PotionEffectType.SLOW,
                        (2 + (level * 2)) * 20,
                        0
                    )
                )
            )
            it.world.spawnParticle(Particle.SUSPENDED, it.location, 15, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_WARDEN_ROAR, 7.5F, 1.5F)
        player.world.playSound(player.location, Sound.ENTITY_FOX_AGGRO, 7.5F, 2.5F)
        player.setCooldown(horn.type, 20 * 6)
    }


    // ------------------------------- FRUITFUL_FARE ------------------------------------
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
                spawnParticle(Particle.VILLAGER_HAPPY, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, player.location, 35, 0.5, 0.5, 0.5)
                playSound(player.location, Sound.ENTITY_STRIDER_HAPPY, 1.5F, 0.5F)
                playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.5F, 0.5F)
                playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
    }

    // ------------------------------- IGNORE_PAIN ------------------------------------
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

    // ------------------------------- ILLUMINEYE ------------------------------------
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

    // ------------------------------- LEAP_FROG ------------------------------------

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

    // ------------------------------ MANDIBLEMANIA ------------------------------------
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


    // ------------------------------- MOLTEN_CORE ------------------------------------
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

    // ------------------------------- MOONWARD ------------------------------------
    private fun moonwardEnchantment(event: PlayerItemDamageEvent) {
        if (!event.player.world.hasSkyLight()) return
        if (event.player.world.isDayTime) return
        if (event.player.world.moonPhase == MoonPhase.NEW_MOON) return
        if (event.player.location.block.lightFromSky < 8) return
        //
        event.damage = maxOf(event.damage - 1, 0)
        // TODO: TO EXPENSIVE TO RUN EVERY TIME MOVE LATER
        if (!moonwardPhasePlayers.contains(event.player.uniqueId)) {
            moonwardPhasePlayers.add(event.player.uniqueId)
            val moonwardPhase = MoonwardPhase(event.player)
            moonwardPhase.runTaskTimer(Odyssey.instance, 10, 20)
        }
    }


    // ------------------------------- OPTICALIZATION ------------------------------------
    @Suppress("UnstableApiUsage")
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


    @Suppress("UnstableApiUsage")
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

    // ------------------------------- POLLEN_GUARD ------------------------------------

    private fun pollenGuardSneakEnchantment(
        defender: LivingEntity,
        level: Int,
        sneaking: Boolean
    ) {
        // Sentries
        if (defender.isSwimming) return
        if (defender.isRiptiding) return

        // blocks
        val flowers = listOf(Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID,
            Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY,
            Material.ORANGE_TULIP, Material.PINK_TULIP, Material.WHITE_TULIP, Material.OXEYE_DAISY,
            Material.LILAC, Material.SUNFLOWER, Material.ROSE_BUSH, Material.PEONY, Material.TORCHFLOWER, Material.WITHER_ROSE,
            Material.PINK_PETALS
        )

        /*
        val blockUnder = defender.location.clone().add(0.0, -1.0, 0.0).block
        if (blockUnder.type !in flowers) return

        if (sneaking) {
            defender.scoreboardTags.add(EntityTags.ROOT_BOOTS_ROOTED)
            defender.world.playSound(defender.location, Sound.BLOCK_MANGROVE_ROOTS_PLACE, 2.5F, 1.5F)
            defender.noDamageTicks = 5
        }
        else {
            defender.scoreboardTags.remove(EntityTags.ROOT_BOOTS_ROOTED)
        }

        if (defender.scoreboardTags.contains(EntityTags.STATIC_SOCKS_CHARGING)) {
            val pollen = defender.getIntTag(EntityTags.STATIC_SOCKS_CHARGE)!!
            if (charge < 2 * level) {
                defender.setIntTag(EntityTags.STATIC_SOCKS_CHARGE, charge + 1)
            }
            defender.world.spawnParticle(Particle.ELECTRIC_SPARK, defender.location, 5 + (charge * 1), 0.4, 0.3, 0.4)
        } else {
            defender.addScoreboardTag(EntityTags.STATIC_SOCKS_CHARGING)
            defender.setIntTag(EntityTags.STATIC_SOCKS_CHARGE, 0)
        }

         */
    }

    // ------------------------------- POTION_BARRIER ------------------------------------
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

    // ------------------------------- RAGING_ROAR -------------------------------
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

    // ------------------------------- RECKLESS ------------------------------------
    private fun recklessEnchantment(level: Int): Double {
        return level * 0.5
    }

    private fun recklessRegenEnchantment(level: Int): Double {
        return level * 1.0
    }


    // ------------------------------- RELENTLESS ------------------------------------
    private fun relentlessEnchantment(defender: LivingEntity, level: Int) {
        if (defender is HumanEntity) {
            defender.saturation = minOf(defender.saturation + (0.25F + (0.25F * level)), 20.0F)
        } else {
            defender.health += (0.5)
        }
    }

    // ------------------------------- ROOT_BOOTS -------------------------------
    private fun rootBootsHitEnchantment(defender: LivingEntity, level: Int): Double {
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
            defender.noDamageTicks = 5
        }
        else {
            defender.scoreboardTags.remove(EntityTags.ROOT_BOOTS_ROOTED)
        }
    }


    // ------------------------------- SPEEDY_SPURS ------------------------------------
    private fun speedySpursEnchantment(rider: LivingEntity, mount: LivingEntity, enchantmentStrength: Int) {
        val someSpeedySpursTask = SpeedySpursTask(rider, mount, enchantmentStrength)
        someSpeedySpursTask.runTaskTimer(Odyssey.instance, 0, 10 * 20)
    }

    // ------------------------------- SPOREFUL ------------------------------------
    private fun sporefulEnchantment(defender: LivingEntity, enchantmentStrength: Int) {
        // List effects
        defender.world.getNearbyLivingEntities(defender.location, enchantmentStrength.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.POISON, ((enchantmentStrength * 2) + 2) * 20, 0),
                            PotionEffect(PotionEffectType.CONFUSION, ((enchantmentStrength * 2) + 2) * 20, 0),
                        )
                    )
                }
            }
        // Particles
        with(defender.world) {
            spawnParticle(Particle.FALLING_SPORE_BLOSSOM, defender.location, 45, 1.0, 0.5, 1.0)
        }
    }

    // ------------------------------- SQUIDIFY ------------------------------------
    private fun squidifyEnchantment(defender: LivingEntity, level: Int) {
        defender.world.getNearbyLivingEntities(defender.location, level.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.BLINDNESS, ((level * 2) + 2) * 20, 1),
                            PotionEffect(PotionEffectType.SLOW, ((level * 2) + 2) * 20, 0)
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
            spawnParticle(Particle.SMOKE_LARGE, defender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    // ------------------------------- SSLITHER_SSIGHT ------------------------------------
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
            PotionEffect(PotionEffectType.SLOW, (level) * 10, 5))

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
            PotionEffect(PotionEffectType.SLOW, (level) * 10, 5))

    }


    // ------------------------------- STATIC_SOCKS ------------------------------------

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

    // ------------------------------- UNTOUCHABLE ------------------------------------
    private fun untouchableEnchantment(
        defender: LivingEntity
    ) {
        if (defender.noDamageTicks < 20) {
            defender.noDamageTicks = 20
        } else {
            defender.noDamageTicks += 10 // Add 0.5 seconds more of immunity
        }
    }

    // ------------------------------ VEILED IN SHADOW ---------------------------------
    private fun veiledInShadowEnchantment(
        defender: LivingEntity,
        level: Int) {
        val shadowLevelBlock = 10 - defender.location.block.lightFromBlocks + level
        val shadowLevelSky = if (!defender.world.isDayTime) { 10 + level }
        else { 10 - defender.location.block.lightFromSky + level }
        defender.noDamageTicks += maxOf(minOf(shadowLevelBlock, shadowLevelSky), 0)
        // gain 15 ticks of immunity when in full darkness with lvl 5
    }

    // ---------------------------------- VENGEFUL --------------------------------------
    private fun vengefulEnchantment(
        attacker: LivingEntity,
        level: Int) {
        attacker.addScoreboardTag(EntityTags.MARKED_FOR_VENGEANCE)
        attacker.addScoreboardTag(EntityTags.VENGEFUL_MODIFIER + level)
        // Add Particles Timer?
    }

    // ------------------------------- VICIOUS VIGOR ------------------------------------
    private fun viciousVigorEnchantment(
        attacker: LivingEntity,
        level: Int): Double {
        if (attacker.health <= 8.0) {
            return level * 1.0
        }
        return 0.0
    }

    // ------------------------------- WAR_CRY -------------------------------
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
                    PotionEffect(PotionEffectType.INCREASE_DAMAGE, (4 + (level * 2)) * 20, 0),
                    PotionEffect(PotionEffectType.SPEED, (4 + (level * 2)) * 20, 0)
                )
            )
            it.world.spawnParticle(Particle.NOTE, it.location, 5, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_RAVAGER_ROAR, 7.5F, 1.5F)
        player.setCooldown(horn.type, 20 * 6)
    }

}

