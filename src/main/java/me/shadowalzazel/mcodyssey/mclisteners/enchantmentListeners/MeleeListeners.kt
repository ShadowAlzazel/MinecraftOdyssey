package me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.effects.*
import org.bukkit.*
import org.bukkit.entity.Bee
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Firework
import org.bukkit.entity.Hoglin
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Pig
import org.bukkit.entity.PiglinAbstract
import org.bukkit.entity.Raider
import org.bukkit.entity.WaterMob
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*

object MeleeListeners : Listener {

    // Internal cool downs for enchantments
    private var backstabberCooldown = mutableMapOf<UUID, Long>()
    private var buzzyBeesCooldown = mutableMapOf<UUID, Long>()
    private var decayingTouchCooldown = mutableMapOf<UUID, Long>()
    private var explodingCooldown = mutableMapOf<UUID, Long>()
    private var gravityWellCooldown = mutableMapOf<UUID, Long>()
    private var guardingStrikeCooldown = mutableMapOf<UUID, Long>()
    private var hemorrhageCooldown = mutableMapOf<UUID, Long>()
    private var voidStrikeCooldown = mutableMapOf<UUID, Long>()
    private var whirlwindCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to entity damage
    @EventHandler
    fun mainDamageHandler(event: EntityDamageByEntityEvent) {
        // Check if event damager and damaged is living entity
        if (event.damager is LivingEntity && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) { // Make thorns bug new enchant apply ranged effects
            val someDamager = event.damager as LivingEntity
            val someVictim = event.entity as LivingEntity
            // Check if active item has lore
            if (someDamager.equipment?.itemInMainHand?.hasItemMeta() == true)  {
                val someWeapon = someDamager.equipment?.itemInMainHand
                // Loop for all enchants
                for (enchant in someWeapon!!.enchantments) {
                    // Check if Gilded Enchantment
                    if (enchant.key in OdysseyEnchantments.enchantmentSet) { // IDK if this faster
                        // Check when
                        when (enchant.key) {
                            OdysseyEnchantments.BACKSTABBER -> {
                                if (!backstabberCooldown.containsKey(someDamager.uniqueId)) { backstabberCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - backstabberCooldown[someDamager.uniqueId]!!
                                if (timeElapsed > 6.0 * 1000) {
                                    backstabberCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    backstabberEnchantment(event, someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.BANE_OF_THE_ILLAGER -> {
                                baneOfTheIllagerEnchantment(event, someWeapon, someVictim)
                            }
                            OdysseyEnchantments.BANE_OF_THE_SEA -> {
                                baneOfTheSeaEnchantment(event, someWeapon, someVictim)
                            }
                            OdysseyEnchantments.BANE_OF_THE_SWINE -> {
                                baneOfTheSwineEnchantment(event, someWeapon, someVictim)
                            }
                            OdysseyEnchantments.BUZZY_BEES -> {
                                if (!buzzyBeesCooldown.containsKey(someDamager.uniqueId)) { buzzyBeesCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - buzzyBeesCooldown[someDamager.uniqueId]!!
                                if (timeElapsed > 2.5 * 1000) {
                                    buzzyBeesCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    buzzyBeesEnchantment(event, someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.DECAYING_TOUCH -> {
                                if (!decayingTouchCooldown.containsKey(someDamager.uniqueId)) { decayingTouchCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - decayingTouchCooldown[someDamager.uniqueId]!!
                                if (timeElapsed > 3.0 * 1000) {
                                    decayingTouchCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    decayingTouchEnchantment(someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.DOUSE -> {
                                douseEnchantment(someWeapon, someVictim)
                            }
                            OdysseyEnchantments.ECHO -> {
                                echoEnchantment(someWeapon, someDamager, someVictim)
                            }
                            OdysseyEnchantments.FREEZING_ASPECT -> {
                                freezingAspectEnchantment(someWeapon, someVictim)
                            }
                            OdysseyEnchantments.FROG_FRIGHT -> {
                                frogFrightEnchantment(event, someWeapon, someVictim)
                            }
                            OdysseyEnchantments.GRAVITY_WELL -> {
                                if (!gravityWellCooldown.containsKey(someDamager.uniqueId)) { gravityWellCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - gravityWellCooldown[someDamager.uniqueId]!!
                                if (timeElapsed > 8.0 * 1000) {
                                    gravityWellCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    gravityWellEnchantment(event, someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.HEMORRHAGE -> {
                                if (!hemorrhageCooldown.containsKey(someDamager.uniqueId)) { hemorrhageCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - hemorrhageCooldown[someDamager.uniqueId]!!
                                if (timeElapsed > 3.5 * 1000) {
                                    hemorrhageCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    hemorrhageEnchantment(someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.VOID_STRIKE -> {
                                if (!voidStrikeCooldown.containsKey(someDamager.uniqueId)) { voidStrikeCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - voidStrikeCooldown[someDamager.uniqueId]!!
                                //val attackSpeedAttribute = someDamager.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
                                if (timeElapsed > 0.75 * 1000) {
                                    voidStrikeCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    voidStrikeEnchantment(event, someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.WHIRLWIND -> {
                                if (!whirlwindCooldown.containsKey(someDamager.uniqueId)) { whirlwindCooldown[someDamager.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - whirlwindCooldown[someDamager.uniqueId]!!
                                if (timeElapsed > 3.5 * 1000) {
                                    whirlwindCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                    whirlwindEnchantment(event, someDamager, someVictim, someWeapon)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Main function for enchantments relating to entity deaths
    @EventHandler
    fun mainDeathHandler(event: EntityDeathEvent) {
        // Check if event killer and entity are living entity
        if (event.entity.killer is LivingEntity) { // Make thorns bug new enchant apply ranged effects
            val someKiller = event.entity.killer as LivingEntity
            val someVictim: LivingEntity = event.entity
            // Check if active item has lore
            if (someKiller.equipment?.itemInMainHand?.hasItemMeta() == true) {
                val someWeapon = someKiller.equipment?.itemInMainHand
                // Loop for all enchants
                for (enchant in someWeapon!!.enchantments) {
                    // Check if Gilded Enchantment
                    if (enchant.key in OdysseyEnchantments.enchantmentSet) { // IDK if this faster
                        // Check when
                        when (enchant.key) {
                            OdysseyEnchantments.EXPLODING -> {
                                if (!explodingCooldown.containsKey(someKiller.uniqueId)) { explodingCooldown[someKiller.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - explodingCooldown[someKiller.uniqueId]!!
                                if (timeElapsed > 1.25 * 1000) {
                                    explodingCooldown[someKiller.uniqueId] = System.currentTimeMillis()
                                    explodingEnchantment(event, someWeapon, someVictim)
                                }
                            }
                            OdysseyEnchantments.GUARDING_STRIKE -> {
                                if (!guardingStrikeCooldown.containsKey(someKiller.uniqueId)) { guardingStrikeCooldown[someKiller.uniqueId] = 0L }
                                val timeElapsed: Long = System.currentTimeMillis() - guardingStrikeCooldown[someKiller.uniqueId]!!
                                if (timeElapsed > 8 * 1000) {
                                    guardingStrikeCooldown[someKiller.uniqueId] = System.currentTimeMillis()
                                    guardingStrikeEnchantment(someWeapon, someVictim, someKiller)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------*/

    // BACKSTABBER Enchantment Function
    private fun backstabberEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        // Check enchantment Strength
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BACKSTABBER)
        // Check if not target
        val victimTarget = eventVictim.getTargetEntity(10)
        if (victimTarget != event.damager) {
            // Particles and sounds
            eventVictim.world.spawnParticle(Particle.CRIT_MAGIC, eventVictim.location, 95, 0.5, 0.5, 0.5)
            eventVictim.world.spawnParticle(Particle.WARPED_SPORE, eventVictim.location, 95, 0.5, 0.5, 0.5)
            eventVictim.world.spawnParticle(Particle.ELECTRIC_SPARK, eventVictim.location, 65, 0.5, 0.5, 0.5)
            eventVictim.world.spawnParticle(Particle.CRIT, eventVictim.location, 65, 0.5, 0.5, 0.5)
            eventVictim.world.playSound(eventVictim.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
            // Damage
            event.damage += (3 + (enchantmentStrength * 3))
        }
    }

    // BANE_OF_THE_ILLAGER Enchantment Function
    private fun baneOfTheIllagerEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        if (eventVictim is Raider) {
            event.damage += (damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BANE_OF_THE_ILLAGER).toDouble() * 2.5)
        }
    }

    // BANE_OF_THE_SEA Enchantment Function
    private fun baneOfTheSeaEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        if (eventVictim.isInWaterOrRainOrBubbleColumn || eventVictim is WaterMob || eventVictim.isSwimming || event.damager.isInWaterOrRainOrBubbleColumn) {
            event.damage += (damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BANE_OF_THE_SEA).toDouble() * 2.0)
        }
    }

    // BANE_OF_THE_SWINE Enchantment Function
    private fun baneOfTheSwineEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        if (eventVictim is PiglinAbstract || eventVictim is Pig || eventVictim is Hoglin) {
            event.damage += (damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BANE_OF_THE_SWINE).toDouble() * 2.5)
        }
    }

    // BUZZY_BEES Enchantment Function
    private fun buzzyBeesEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        // Check enchantment Strength
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BUZZY_BEES)

        // Effects
        val buzzyBeesSlow = PotionEffect(PotionEffectType.SLOW, ((3 * enchantmentStrength) + 3) * 20, 0)
        eventVictim.addPotionEffect(buzzyBeesSlow)
        eventVictim.addScoreboardTag("Honeyed")
        val honeyedTask = HoneyedTask(eventVictim, enchantmentStrength, ((3 * enchantmentStrength) + 3) * 2)
        honeyedTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10) // Every 10 ticks -> 0.5 sec

        // Particles and Sounds
        eventVictim.world.spawnParticle(Particle.DRIPPING_HONEY, eventVictim.location, 15, 1.0, 0.5, 1.0)
        eventVictim.world.spawnParticle(Particle.FALLING_HONEY, eventVictim.location, 20, 1.5, 0.5, 1.5)
        eventVictim.world.spawnParticle(Particle.LANDING_HONEY, eventVictim.location, 10, 1.0, 0.5, 1.0)
        event.damager.world.playSound(eventVictim.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)

        // Spawn Bee if not low
        if (eventVictim.health > event.finalDamage + 1) {
            val someBee: Bee = eventVictim.world.spawnEntity(eventVictim.location, EntityType.BEE) as Bee
            val beeSpeed = PotionEffect(PotionEffectType.SPEED, 10, (enchantmentStrength))
            val beeStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, (enchantmentStrength))
            val beeDexterity = PotionEffect(PotionEffectType.FAST_DIGGING, 10, (enchantmentStrength))
            val beeCarapace = PotionEffect(PotionEffectType.ABSORPTION, 10, (enchantmentStrength + 2))
            someBee.addPotionEffects(listOf(beeSpeed, beeStrength, beeDexterity, beeCarapace))
            someBee.target = eventVictim
        }

    }

    // DECAYING_TOUCH Enchantment Function
    private fun decayingTouchEnchantment(damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        // Check enchantment Strength
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.DECAYING_TOUCH)

        // Effects
        val decayEffect = PotionEffect(PotionEffectType.HUNGER, 10 * 20 , 0)
        eventVictim.addPotionEffect(decayEffect)
        eventVictim.addScoreboardTag("Decaying")
        val decayingTask = DecayingTask(eventVictim, enchantmentStrength, 5) // Every 2 secs
        decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20 * 2)

        // Particles and sounds
        eventVictim.world.spawnParticle(Particle.SPORE_BLOSSOM_AIR , eventVictim.location, 40, 0.25, 0.4, 0.25)
        eventVictim.world.spawnParticle(Particle.GLOW, eventVictim.location, 20, 0.25, 0.4, 0.25)
        eventVictim.world.spawnParticle(Particle.GLOW_SQUID_INK, eventVictim.location, 15, 0.25, 0.25, 0.25)
        eventVictim.world.spawnParticle(Particle.SNEEZE, eventVictim.location, 40, 0.25, 0.25, 0.25)
        eventVictim.world.spawnParticle(Particle.SCRAPE, eventVictim.location, 20, 0.5, 1.0, 0.5)

    }

    // DOUSE Enchantment Function
    private fun douseEnchantment(damagerWeapon: ItemStack, eventVictim: LivingEntity) { // Moved Ignition to ablaze effect
        // Check enchantment Strength
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.DOUSE)

        // Add Doused if not in tags
        if ("Doused" !in eventVictim.scoreboardTags) {
            eventVictim.scoreboardTags.add("Doused")
            eventVictim.scoreboardTags.add("Doused_Factor_$enchantmentStrength")
            // Effects
            val dousingTask = DousedTask(eventVictim, 10)
            dousingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)

        }

    }

    // ECHO Enchantment Function
    private fun echoEnchantment(damagerWeapon: ItemStack, eventDamager: LivingEntity, eventVictim: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.ECHO)
        if ((0..100).random() < enchantmentStrength * 20) {
            if (!eventVictim.isDead) {
                // Swing
                eventDamager.swingOffHand()
                // Particles
                eventVictim.world.spawnParticle(Particle.SWEEP_ATTACK, eventVictim.location, 3, 0.05, 0.05, 0.05)
            }
        }

    }

    // EXPLODING Enchantment Function
    private fun explodingEnchantment(event: EntityDeathEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        // Boom variables
        val deathLocation = event.entity.location.clone()
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.EXPLODING)

        // Fireball
        val boomExplosion: Fireball = eventVictim.world.spawnEntity(deathLocation, EntityType.FIREBALL) as Fireball
        boomExplosion.setIsIncendiary(false)
        boomExplosion.yield = 0.0F
        boomExplosion.direction = Vector(0.0, -3.0, 0.0)

        // Firework effect and color
        val boomFirework: Firework = deathLocation.world.spawnEntity(deathLocation, EntityType.FIREWORK) as Firework
        val boomFireworkMeta = boomFirework.fireworkMeta
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA, Color.ORANGE)
        boomFireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(randomColors.random()).withFade(randomColors.random()).trail(true).flicker(true).build())
        boomFireworkMeta.power = enchantmentStrength * 30
        boomFirework.fireworkMeta = boomFireworkMeta
        boomFirework.velocity = Vector(0.0, -3.0, 0.0)

        // Particles
        eventVictim.world.spawnParticle(Particle.FLASH, deathLocation, 5, 1.0, 1.0, 1.0)
        eventVictim.world.spawnParticle(Particle.LAVA, deathLocation, 35, 1.5, 1.0, 1.5)
    }

    // FREEZING_ASPECT Enchantment Function
    private fun freezingAspectEnchantment(damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.FREEZING_ASPECT)
        if (eventVictim.freezeTicks <= 50) {
            // Particles
            eventVictim.world.spawnParticle(Particle.SNOWFLAKE, eventVictim.location, 25, 1.0, 0.5, 1.0)

            // Effects
            val freezingSlow = PotionEffect(PotionEffectType.SLOW, (enchantmentStrength * 20 * 3) - 2, enchantmentStrength - 1)
            eventVictim.addPotionEffect(freezingSlow)
            eventVictim.addScoreboardTag("Freezing")
            val freezingTask = FreezingTask(eventVictim, enchantmentStrength, enchantmentStrength * 3)
            freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
        }
    }

    // FROG_FRIGHT Enchantment Function
    private fun frogFrightEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.FROG_FRIGHT)
        // Vector Math
        val closeEntityLocation = eventVictim.location.clone().add(0.0, 1.75, 0.0)
        val someDirectionVector = closeEntityLocation.clone().subtract(event.damager.location.clone()).toVector()
        val someUnitVector = someDirectionVector.clone().normalize()
        val someNewVector = someUnitVector.clone().multiply(1.15 + (0.15 * enchantmentStrength))
        eventVictim.velocity = someNewVector

        // Effects and Particles
        val someFroggingTask = FrogFrightTask(eventVictim, someNewVector.clone())
        someFroggingTask.runTaskTimer(MinecraftOdyssey.instance, 20, 1)

        eventVictim.world.spawnParticle(Particle.TOWN_AURA, eventVictim.location, 65, 1.0, 0.5, 1.0)
        eventVictim.world.playSound(eventVictim.location, Sound.ENTITY_FROG_EAT, 3.5F, 0.5F)

    }

    // GRAVITY_WELL Enchantment Function
    private fun gravityWellEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.GRAVITY_WELL)
        val singularityPoint = event.entity.location

        /* Particles and Sounds */
        eventVictim.world.playSound(singularityPoint, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.75F, 0.3F)
        eventVictim.world.spawnParticle(Particle.WHITE_ASH, singularityPoint, 105, 1.0, 0.5, 1.0)

        // Every 10 ticks -> 0.5 sec
        eventVictim.addScoreboardTag("Gravity_Well")
        val gravityWellTask = GravitationalAttract(eventVictim, event.damager as LivingEntity, enchantmentStrength * 1, (enchantmentStrength * 2) + 2)
        gravityWellTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10)

    }

    // GUARDING_STRIKE Enchantment Function
    private fun guardingStrikeEnchantment(damagerWeapon: ItemStack, eventVictim: LivingEntity, eventKiller: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.GUARDING_STRIKE)

        // Effects
        val guardingPose = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (2 + (enchantmentStrength * 2)) * 20, 0)
        eventKiller.addPotionEffect(guardingPose)
        // Particles and Sounds
        eventKiller.world.spawnParticle(Particle.SCRAPE, eventVictim.location, 35, 1.0, 0.5, 1.0)
        eventKiller.world.spawnParticle(Particle.ELECTRIC_SPARK, eventVictim.location, 35, 1.0, 0.5, 1.0)
        eventKiller.world.playSound(eventVictim.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
        eventKiller.world.playSound(eventVictim.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
    }

    // HEMORRHAGE Enchantment Function
    private fun hemorrhageEnchantment(damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.HEMORRHAGE)
        if (eventVictim.freezeTicks <= 50) {
            // Particles
            eventVictim.world.spawnParticle(Particle.CRIT, eventVictim.location, 45, 1.0, 0.5, 1.0)

            // Effects
            eventVictim.addScoreboardTag("Hemorrhaging")
            val hemorrhageTask = HemorrhageTask(eventVictim, enchantmentStrength)
            hemorrhageTask.runTaskTimer(MinecraftOdyssey.instance, 0, 30) // Every 1.5 secs
        }
    }

    // VOID_STRIKE Enchantment Function
    private fun voidStrikeEnchantment(event: EntityDamageByEntityEvent, damagerWeapon: ItemStack, eventVictim: LivingEntity) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.VOID_STRIKE)
        // Variables
        var voidDamage = 0
        var voidTouched = false
        var voidConflict = false
        // Values
        val victimScoreboardTags = eventVictim.scoreboardTags
        val voidStrikeIDs = voidStrikeCooldown.keys.toList()
        val voidDamagerID: UUID = event.damager.uniqueId

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
                eventVictim.scoreboardTags.remove(tag)
            }
        }

        if (!voidConflict) {
            // Check if already voided
            if (voidTouched) {
                var voidStruckValue: Int? = null
                for (tag in victimScoreboardTags) {
                    // Change Void Struck tag
                    val voidStruckTagPrefix = tag.subSequence(0..10).toString()
                    if (voidStruckTagPrefix == "Void_Struck") {
                        val tagLastIndex = tag.lastIndex
                        voidStruckValue = tag[tagLastIndex].toString().toInt()
                        break
                    }
                }
                // Void Struck value to damage and update tag
                if (voidStruckValue != null) {
                    if (voidStruckValue == 9) {
                        eventVictim.scoreboardTags.remove("Void_Struck_${voidStruckValue}")
                        eventVictim.scoreboardTags.add("Void_Struck_0")
                    }
                    else {
                        eventVictim.scoreboardTags.remove("Void_Struck_${voidStruckValue}")
                        eventVictim.scoreboardTags.add("Void_Struck_${voidStruckValue + 1}")
                    }
                    voidDamage = (enchantmentStrength * (voidStruckValue + 1))
                }
            }
            // Add tags if not voided
            else {
                eventVictim.scoreboardTags.add("Void_Touched")
                eventVictim.scoreboardTags.add("Void_Touched_By_${voidDamagerID}")
                eventVictim.scoreboardTags.add("Void_Struck_0")
            }

            // Particles and Sounds
            eventVictim.world.spawnParticle(Particle.PORTAL, eventVictim.location, 85, 1.15, 0.85, 1.15)
            eventVictim.world.spawnParticle(Particle.WAX_OFF, eventVictim.location, 45, 1.0, 0.75, 1.0)
            eventVictim.world.spawnParticle(Particle.SPELL_WITCH, eventVictim.location, 50, 1.0, 0.75, 1.0)
            eventVictim.world.playSound(eventVictim.location, Sound.BLOCK_BEACON_DEACTIVATE, 1.5F, 0.5F)
            eventVictim.world.playSound(eventVictim.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.7F, 0.2F)
            eventVictim.world.playSound(eventVictim.location, Sound.ENTITY_ENDER_EYE_DEATH, 3.5F, 0.4F)

            // Damage
            event.damage += voidDamage
        }
    }

    // WHIRLWIND Enchantment Function
    private fun whirlwindEnchantment(event: EntityDamageByEntityEvent, eventDamager: LivingEntity, eventVictim: LivingEntity, damagerWeapon: ItemStack) {
        val enchantmentStrength = damagerWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.WHIRLWIND)
        // Entity list
        val entitiesNearby = eventDamager.world.getNearbyLivingEntities(eventDamager.location, 1.5)
        entitiesNearby.remove(eventDamager)

        // Location
        val eventLocation = eventDamager.location

        // Particles and Sound
        /*
        val xCord = eventDamager.location.x - 1.25
        val yCord = eventDamager.location.y + 1
        val zCord = eventDamager.location.z
        for (n in 0..16) {
            val s = (2.5 / 16) * n
            if (n == 16 || n == 0) {
                eventDamager.world.spawnParticle(Particle.EXPLOSION_LARGE, xCord + s, yCord, zCord, 1)
            }
            else {
                var q = s
                if (n > 8) {
                    q -= (2.5 / 16) * (n - 8)
                }
                eventDamager.world.spawnParticle(Particle.EXPLOSION_LARGE, xCord + s, yCord, zCord + q, 1)
                eventDamager.world.spawnParticle(Particle.EXPLOSION_LARGE, xCord + s, yCord, zCord - q, 1)
            }
        }
         */
        eventDamager.world.spawnParticle(Particle.EXPLOSION_LARGE, eventLocation, 10, 1.25, 0.75, 1.25)
        eventDamager.world.playSound(eventLocation, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
        eventDamager.world.playSound(eventLocation, Sound.ITEM_SHIELD_BLOCK, 1.2F, 0.6F)
        eventDamager.world.playSound(eventLocation, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.2F, 0.6F)
        eventDamager.world.playSound(eventLocation, Sound.ITEM_TRIDENT_RIPTIDE_2, 1.2F, 1.2F)

        // Damage Calculation
        val someDamage = event.damage
        val whirlwindPercentage = (((enchantmentStrength * 3) * 0.1))
        val whirlDamage = someDamage * whirlwindPercentage

        for (closeEntity in entitiesNearby) {
            // Exclude event victim
            if (closeEntity != eventVictim) closeEntity.damage(whirlDamage, eventDamager)
            // Vector Math
            val closeEntityLocation = closeEntity.location.clone().add(0.0, 1.5, 0.0)
            val someDirectionVector = closeEntityLocation.clone().subtract(eventLocation.clone()).toVector()
            val someUnitVector = someDirectionVector.clone().normalize()
            val someNewVector = someUnitVector.clone().multiply(0.75 + (0.1 * enchantmentStrength))
            closeEntity.velocity = someNewVector
        }
    }

    fun todo() {
        TODO("Check if glowing then do more damage" +
                "Echo Enchant")
    }


}