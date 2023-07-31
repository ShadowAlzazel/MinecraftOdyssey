package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.tasks.SpeedySpursTask
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
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.inventory.meta.Damageable as Repairable
import java.util.*

object ArmorListeners : Listener {

    // Internal Cool downs
    private var fruitfulFareCooldown = mutableMapOf<UUID, Long>()
    private var potionBarrierCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to entity damage for armor
    @EventHandler
    fun mainArmorDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) {
            return
        }
        if (event.entity !is LivingEntity) {
            return
        }
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return
        }

        // Make thorns bug new enchant apply ranged effects
        val attacker = event.damager as LivingEntity
        val defender = event.entity as LivingEntity
        // All Armors
        var isChitin = false

        // --------------------------------------------------------------
        // Check if helmet item has lore
        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ANTIBONK -> {
                        event.damage = antibonkEnchantment(event.isCritical, event.damage, enchant.value)
                    }
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        if (copperChitinEnchantment(defender, helmet)) { isChitin = true }
                    }
                }
            }
        }
        // --------------------------------------------------------------
        // Check if chestplate item has lore
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.BEASTLY_BRAWLER -> {
                        beastlyBrawlerEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        if (copperChitinEnchantment(defender, chestplate)) { isChitin = true }
                    }
                    OdysseyEnchantments.VENGEFUL -> {
                        vengefulEnchantment(attacker, enchant.value)
                    }
                }
            }
        }
        // --------------------------------------------------------------
        // Check if legging item has lore
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings
            for (enchant in leggings!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        if (copperChitinEnchantment(defender, leggings)) { isChitin = true }
                    }
                    OdysseyEnchantments.COWARDICE -> {
                        cowardiceEnchantment(attacker, defender, enchant.value)
                    }
                    OdysseyEnchantments.SPOREFUL -> {
                        sporefulEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.SQUIDIFY -> {
                        squidifyEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        // --------------------------------------------------------------
        // Check if boot item has lore
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots
            for (enchant in boots!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.COPPER_CHITIN -> {
                        if (copperChitinEnchantment(defender, boots)) { isChitin = true }
                    }
                }
            }
        }
        // Chitin
        if (isChitin) {
            val copper = defender.equipment?.itemInOffHand
            if (copper?.type == Material.COPPER_INGOT) {
                copper.subtract(1)
            }
        }
    }

    // Main function for enchantments relating to consuming items
    @EventHandler
    fun mainArmorConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player

        // --------------------------------------------------------------
        // Check if helmet item has lore
        if (player.equipment.helmet?.hasItemMeta() == true) {
            val helmet = player.equipment.helmet
            for (enchant in helmet!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.BREWFUL_BREATH -> {
                        brewfulBreathEnchantment(player, event.item, enchant.value)
                    }
                    OdysseyEnchantments.DREADFUL_SHRIEK -> {
                        dreadfulShriekEnchantment(player, event.item, enchant.value)
                    }
                    OdysseyEnchantments.RAGING_ROAR -> {
                        ragingRoarEnchantment(player, event.item, enchant.value)
                        println("ROAR")
                    }
                    OdysseyEnchantments.WAR_CRY -> {
                        warCryEnchantment(player, event.item, enchant.value)
                    }
                }
            }
        }
        // --------------------------------------------------------------
        // Check if chestplate item has lore
        if (player.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment.chestplate
            for (enchant in chestplate!!.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.FRUITFUL_FARE -> {
                        if (!fruitfulFareCooldown.containsKey(player.uniqueId)) {
                            fruitfulFareCooldown[player.uniqueId] = 0L
                        }
                        val timeElapsed: Long =
                            System.currentTimeMillis() - fruitfulFareCooldown[player.uniqueId]!!
                        if (timeElapsed > 8 * 1000) {
                            fruitfulFareCooldown[player.uniqueId] = System.currentTimeMillis()
                            fruitfulFareEnchantment(player, event.item, enchant.value)
                        }
                    }
                    // Add if eat rotten or raw heal + other
                    OdysseyEnchantments.POTION_BARRIER -> {
                        if (!potionBarrierCooldown.containsKey(player.uniqueId)) {
                            potionBarrierCooldown[player.uniqueId] = 0L
                        }
                        val timeElapsed: Long =
                            System.currentTimeMillis() - potionBarrierCooldown[player.uniqueId]!!
                        if (timeElapsed > 12 * 1000) {
                            potionBarrierCooldown[player.uniqueId] = System.currentTimeMillis()
                            potionBarrierEnchantment(player, event.item, enchant.value)
                        }
                    }

                }
            }
        }

    }

    @EventHandler
    fun mainArmorHitHandler(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            val defender = event.entity as LivingEntity
            // --------------------------------------------------------------
            // Check if boot item has lore
            if (defender.equipment?.boots?.hasItemMeta() == true) {
                val someBoots = defender.equipment?.boots
                // Loop for all enchants
                for (enchant in someBoots!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.DEVASTATING_DROP -> {
                            devastatingDrop(defender, event.damage, enchant.value)
                        }
                    }
                }
            }
        }

        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            // DODGE AN ATTACK WITH UNTOUCHABLE
            // If CAN SEE AND WITHIN 3 BLOCKS DODGE MELEE
            // IF LINE OF SIGHT projectile
            // DODGE
            // COOLDOWN


            // IF SOMETHING
            // IDK


        }

    }

    // Some function regarding vehicles and armor
    @EventHandler
    fun miscArmorVehicleHandler(event: VehicleEnterEvent) { // Move Horse Armor Enchant Here
        if (event.entered is LivingEntity && event.vehicle is LivingEntity) {
            val rider = event.entered as LivingEntity
            val mount = event.vehicle as LivingEntity
            // --------------------------------------------------------------
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

    @EventHandler
    fun knockBackHandler(event: EntityKnockbackByEntityEvent) {
        val defender = event.entity
        // --------------------------------------------------------------
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

    @EventHandler
    fun sneakHandler(event: PlayerToggleSneakEvent) {
        // After many charges -> when hit -> do static discharge

        // Set timer -> if has not moved -> add is rooted
    }

    @EventHandler
    fun jumpHandler(event: PlayerJumpEvent) {
        val jumper = event.player
        // --------------------------------------------------------------
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

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // ------------------------------- ANTIBONK ------------------------------------
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


    // ------------------------------- BEASTLY_BRAWLER ------------------------------------
    // TODO: Add Temp Attribute Attack Damage instead of Potion
    private fun beastlyBrawlerEnchantment(
        defender: LivingEntity,
        level: Int
    ) {
        if (defender.location.getNearbyLivingEntities(4.0).size > 5) {
            defender.addPotionEffect(
                PotionEffect(
                    PotionEffectType.INCREASE_DAMAGE,
                    6 * 20,
                    level - 1
                )
            )
        }
    }

    // ------------------------------- BREWFUL_BREATH ------------------------------------
    private fun brewfulBreathEnchantment(
        player: Player,
        potion: ItemStack,
        level: Int
    ) {
        if (potion.type != Material.POTION) return
        if (potion.itemMeta !is PotionMeta) return
        if (!(potion.itemMeta as PotionMeta).hasCustomEffects()) return
        val potionMeta = potion.itemMeta as PotionMeta
        val cloud = player.world.spawnEntity(
            player.location,
            EntityType.AREA_EFFECT_CLOUD,
            CreatureSpawnEvent.SpawnReason.CUSTOM
        ) as AreaEffectCloud
        cloud.apply {
            addCustomEffect(potionMeta.customEffects.first(), true)
            duration = (level + 3) * 20
            radius = 2.5F
            durationOnUse = 5
            addScoreboardTag(EntityTags.BREATH_CLOUD)
            addScoreboardTag(EntityTags.BREATH_BY + player.uniqueId)
        }
        player.setCooldown(potion.type, 20 * 6)
    }

    // ------------------------------- COPPER_CHITIN ------------------------------------
    private fun copperChitinEnchantment(
        defender: LivingEntity,
        armor: ItemStack
    ): Boolean {
        if (defender.equipment?.itemInOffHand?.type != Material.COPPER_INGOT) { return false }
        val armorMeta = armor.itemMeta
        if (armorMeta !is Repairable) { return false }
        if (!armorMeta.hasDamage()) { return false }
        armorMeta.damage -= 1
        if (defender !is HumanEntity) { return false }
        defender.saturation = maxOf(defender.saturation + 0.5F, 20.0F)
        return true
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
        enchantmentStrength: Int
    ) {
        if (horn.type != Material.GOAT_HORN) return

        val enemies = player.getNearbyEntities(12.0, 6.0, 12.0).filter { it !is Player && it is LivingEntity }
        enemies.forEach {
            (it as LivingEntity).addPotionEffects(
                listOf(
                    PotionEffect(
                        PotionEffectType.WEAKNESS,
                        ((enchantmentStrength * 2) + 2) * 20,
                        0
                    ),
                    PotionEffect(
                        PotionEffectType.SLOW,
                        ((enchantmentStrength * 2) + 2) * 20,
                        0
                    )
                )
            )
            it.world.spawnParticle(Particle.SUSPENDED, it.location, 15, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 7.5F, 1.5F)
        player.setCooldown(horn.type, 20 * 6)
    }


    // ------------------------------- FRUITFUL_FARE ------------------------------------
    private fun fruitfulFareEnchantment(
        player: Player,
        food: ItemStack,
        enchantmentStrength: Int
    ) {
        // list of materials that can not be consumed
        val fareList = listOf(
            Material.MELON_SLICE,
            Material.APPLE,
            Material.GOLDEN_APPLE,
            Material.GLOW_BERRIES,
            Material.SWEET_BERRIES,
            Material.ENCHANTED_GOLDEN_APPLE,
            Material.RABBIT_STEW // TEMP Make separate list for models
        )
        if (food.type in fareList) { // Currently non-forgiving
            // Check Health
            val currentHealth = player.health
            if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value < currentHealth + (1 + enchantmentStrength)) {
                player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            } else {
                player.health += (1 + enchantmentStrength)
            }
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

    private fun leapFrogEnchantment(
        jumper: LivingEntity,
        level: Int
    ) {
        val location = jumper.location.clone().apply {
            y -= 0.05
        }
        val block = location.block
        val jumpBlocks = listOf(
            Material.LILY_PAD,
            Material.BIG_DRIPLEAF,
            Material.MUD,
            Material.MUDDY_MANGROVE_ROOTS,
        )
        val isWaterLeaf = block.blockData is Leaves && (block.blockData as Leaves).isWaterlogged

        if (jumper.location.block.type in jumpBlocks || isWaterLeaf) {
            jumper.velocity = jumper.velocity.multiply(1.0 + (0.4 * level))
        }
    }

    // ------------------------------- POTION_BARRIER ------------------------------------
    private fun potionBarrierEnchantment(
        player: Player, potion: ItemStack, level: Int
    ) {
        // Check if potion
        if (potion.type != Material.POTION) {
            return
        }
        if (!potion.itemMeta.hasCustomModelData() || potion.itemMeta.customModelData == ItemModels.SQUARE_BOTTLE) {
            // Effects
            player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE,
                    ((level * 2) + 2) * 20,
                    1
                )
            )
            // Particles
            with(player.world) {
                spawnParticle(Particle.SCRAPE, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.ELECTRIC_SPARK, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, player.location, 35, 0.5, 0.5, 0.5)
                playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
                playSound(player.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
                playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
    }

    // ------------------------------- RAGING_ROAR -------------------------------
    private fun ragingRoarEnchantment(
        player: Player,
        horn: ItemStack,
        level: Int
    ) {
        if (horn.type != Material.GOAT_HORN) return

        val enemies = player.getNearbyEntities(8.0 + (level * 4), 6.0, 8.0 + (level * 4)).filter { it !is Player && it is Creature }
        enemies.forEach {
            (it as Creature).lookAt(player)
            it.pathfinder.moveTo(player)
            it.target = player
            it.world.spawnParticle(Particle.FALLING_OBSIDIAN_TEAR, it.location, 15, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_RAVAGER_ROAR, 7.5F, 1.5F)
        player.setCooldown(horn.type, 20 * 6)
    }


    // ------------------------------- ROOT_BOOTS -------------------------------
    private fun rootBootsHitEnchantment(defender: LivingEntity, level: Int): Double {
        //40 25 10
        if (!defender.scoreboardTags.contains(EntityTags.IS_ROOTED)) {
            return 1.0
        }
        return (1.0 - (0.15 * (3 + level)))
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
    private fun squidifyEnchantment(defender: LivingEntity, enchantmentStrength: Int) {
        defender.world.getNearbyLivingEntities(defender.location, enchantmentStrength.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.BLINDNESS, ((enchantmentStrength * 2) + 2) * 20, 1),
                            PotionEffect(PotionEffectType.SLOW, (enchantmentStrength * 20) * 20, 2)
                        )
                    )
                }
            }
        // Particles
        with(defender.world) {
            spawnParticle(Particle.ASH, defender.location, 95, 1.5, 0.5, 1.5)
            spawnParticle(Particle.SPELL_MOB_AMBIENT, defender.location, 55, 0.75, 0.5, 0.75)
            spawnParticle(Particle.SQUID_INK, defender.location, 85, 0.75, 0.5, 0.75)
            spawnParticle(Particle.SMOKE_LARGE, defender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    // ------------------------------- VENGEFUL ------------------------------------
    private fun vengefulEnchantment(attacker: LivingEntity, enchantmentStrength: Int) {
        attacker.addScoreboardTag(EntityTags.MARKED_FOR_VENGEANCE)
        attacker.addScoreboardTag(EntityTags.VENGEFUL_MODIFIER + enchantmentStrength)
        // Add Particles Timer?
    }

    // ------------------------------- WAR_CRY -------------------------------
    private fun warCryEnchantment(
        player: Player,
        horn: ItemStack,
        level: Int
    ) {
        if (horn.type != Material.GOAT_HORN) return
        val isPet = { entity: Entity -> entity is Tameable && entity.owner == player }

        val allies = player.getNearbyEntities(8.0 + (level * 4), 6.0, 8.0 + (level * 4)).filter { it is Player || isPet(it) }
        allies.forEach {
            it as LivingEntity
            it.addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.INCREASE_DAMAGE, ((level * 2) + 2) * 20, 0),
                    PotionEffect(PotionEffectType.SPEED, (level * 20) * 20, 0)
                )
            )
            it.world.spawnParticle(Particle.NOTE, it.location, 5, 0.3, 0.5, 0.3)
        }
        player.world.playSound(player.location, Sound.ENTITY_RAVAGER_ROAR, 7.5F, 1.5F)
        player.setCooldown(horn.type, 20 * 6)
    }

}