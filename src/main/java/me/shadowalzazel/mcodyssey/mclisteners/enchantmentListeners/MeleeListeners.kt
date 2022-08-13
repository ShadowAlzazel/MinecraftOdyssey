package me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.tasks.*
import org.bukkit.*
import org.bukkit.entity.Bee
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Firework
import org.bukkit.entity.Hoglin
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Pig
import org.bukkit.entity.Piglin
import org.bukkit.entity.PiglinAbstract
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Raider
import org.bukkit.entity.Vex
import org.bukkit.entity.WaterMob
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*


//REFACTOR INTO MULTIPLE LISTENERS!!!

object MeleeListeners : Listener {

    // Internal cool downs for enchantments
    private var playersBackstabberCooldown = mutableMapOf<UUID, Long>()
    private var playersBuzzyBeesCooldown = mutableMapOf<UUID, Long>()
    private var playersDecayingTouchCooldown = mutableMapOf<UUID, Long>()
    private var playersExplodingCooldown = mutableMapOf<UUID, Long>()
    private var playersGravityWellCooldown = mutableMapOf<UUID, Long>()
    private var playersGuardingStrikeCooldown = mutableMapOf<UUID, Long>()
    private var playersHemorrhageCooldown = mutableMapOf<UUID, Long>()
    private var playersVoidStrikeCooldown = mutableMapOf<UUID, Long>()
    private var playersWhirlwindCooldown = mutableMapOf<UUID, Long>()

    // Extra Damage Modifiers
    // 2.5
    @EventHandler
    fun modifierDamageIncrease(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val damagedEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    // Bane of the Illager
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.BANE_OF_THE_ILLAGER)) {
                        if (damagedEntity is Vex || damagedEntity is Raider) {
                            event.damage += (someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BANE_OF_THE_ILLAGER).toDouble() * 2.5)
                            println("${event.damage}")
                        }
                    }
                    // Bane of the Sea
                    else if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.BANE_OF_THE_SEA)) {
                        if (damagedEntity.isInWaterOrRainOrBubbleColumn || damagedEntity.isSwimming || damagedEntity is WaterMob) {
                            event.damage += (someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BANE_OF_THE_SWINE).toDouble() * 2.0)
                            println("${event.damage}")
                        }
                    }
                    // Bane of the Swine
                    else if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.BANE_OF_THE_SWINE)) {
                        if (damagedEntity is Piglin || damagedEntity is PiglinBrute || damagedEntity is Pig || damagedEntity is Hoglin || damagedEntity is PiglinAbstract) {
                            event.damage += (someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BANE_OF_THE_SWINE).toDouble() * 2.5)
                            println("${event.damage}")
                        }
                    }
                }
            }
        }
    }


    // BUZZY_BEES Enchantment Effects
    @EventHandler
    fun buzzyBeesEnchant(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            if (event.entity is LivingEntity) {
                val somePlayer: Player = event.damager as Player
                val honeyedEntity: LivingEntity = event.entity as LivingEntity
                if (honeyedEntity.type != EntityType.BEE) {
                    if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                        val someWeapon = somePlayer.inventory.itemInMainHand
                        if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.BUZZY_BEES)) {
                            if (somePlayer.gameMode != GameMode.SPECTATOR) {

                                if (!playersBuzzyBeesCooldown.containsKey(somePlayer.uniqueId)) {
                                    playersBuzzyBeesCooldown[somePlayer.uniqueId] = 0L
                                }

                                val timeElapsed: Long = System.currentTimeMillis() - playersBuzzyBeesCooldown[somePlayer.uniqueId]!!

                                val honeyFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BUZZY_BEES)
                                if (timeElapsed > (3.5 - honeyFactor) * 1000) {
                                    playersBuzzyBeesCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
                                    val honeySlow = PotionEffect(PotionEffectType.SLOW, ((3 * honeyFactor) + 3) * 20, 0)
                                    honeyedEntity.world.spawnParticle(Particle.DRIPPING_HONEY, honeyedEntity.location, 15, 1.0, 0.5, 1.0)
                                    honeyedEntity.world.spawnParticle(Particle.FALLING_HONEY, honeyedEntity.location, 20, 1.5, 0.5, 1.5)
                                    honeyedEntity.world.spawnParticle(Particle.LANDING_HONEY, honeyedEntity.location, 10, 1.0, 0.5, 1.0)
                                    honeyedEntity.addPotionEffect(honeySlow)
                                    somePlayer.playSound(somePlayer.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)

                                    // Every 10 ticks -> 0.5 sec
                                    honeyedEntity.addScoreboardTag("Decaying")
                                    val honeyedTask = HoneyedTask(honeyedEntity, honeyFactor)
                                    honeyedTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10)

                                    /*
                                    for (x in 1..honeyFactor) {
                                        val someBee: Bee = honeyedEntity.world.spawnEntity(honeyedEntity.location, EntityType.BEE) as Bee
                                        someBee.target = honeyedEntity
                                    }
                                    */
                                    if (honeyedEntity.health > event.finalDamage + 1) {
                                        val someBee: Bee = honeyedEntity.world.spawnEntity(somePlayer.location, EntityType.BEE) as Bee
                                        val honeySpeed = PotionEffect(PotionEffectType.SPEED, 10, (honeyFactor))
                                        val honeyStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, (honeyFactor))
                                        val honeyDexterity = PotionEffect(PotionEffectType.FAST_DIGGING, 10, (honeyFactor))
                                        val honeyConstitution = PotionEffect(PotionEffectType.ABSORPTION, 10, (honeyFactor + 2))
                                        val honeyRoids = listOf(honeyDexterity, honeySpeed, honeyStrength, honeyConstitution)
                                        someBee.addPotionEffects(honeyRoids)
                                        someBee.target = honeyedEntity
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // BACKSTABBER enchant
    @EventHandler
    fun backstabberEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val someEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.BACKSTABBER)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {
                            val target = someEntity.getTargetEntity(15)
                            if (target != somePlayer) {

                                if (!playersBackstabberCooldown.containsKey(somePlayer.uniqueId)) {
                                    playersBackstabberCooldown[somePlayer.uniqueId] = 0L
                                }

                                val timeElapsed: Long = System.currentTimeMillis() - playersBackstabberCooldown[somePlayer.uniqueId]!!

                                if (timeElapsed > 6.5 * 1000) {
                                    playersBackstabberCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
                                    val backstabberProwess = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.BACKSTABBER)
                                    val newDamage = 3 + (backstabberProwess * 3)
                                    someEntity.world.spawnParticle(Particle.CRIT_MAGIC, somePlayer.location, 45, 0.5, 0.5, 0.5)
                                    someEntity.world.spawnParticle(Particle.WARPED_SPORE, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                    someEntity.world.spawnParticle(Particle.SPELL_WITCH, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                    somePlayer.playSound(somePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1.5F, 0.9F)
                                    event.damage += newDamage
                                    println(event.damage)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // DECAYING_TOUCH Enchantment Effects
    @EventHandler
    fun decayingTouchEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val decayingEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.DECAYING_TOUCH)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {

                            if (!playersDecayingTouchCooldown.containsKey(somePlayer.uniqueId)) {
                                playersDecayingTouchCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - playersDecayingTouchCooldown[somePlayer.uniqueId]!!
                            val decayingTouchFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.DECAYING_TOUCH)

                            if (timeElapsed > (3.5 - decayingTouchFactor) * 1000) {
                                playersDecayingTouchCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                decayingEntity.world.spawnParticle(Particle.SPORE_BLOSSOM_AIR , decayingEntity.location, 40, 0.25, 0.4, 0.25)
                                decayingEntity.world.spawnParticle(Particle.GLOW, decayingEntity.location, 20, 0.25, 0.4, 0.25)
                                decayingEntity.world.spawnParticle(Particle.GLOW_SQUID_INK, decayingEntity.location, 15, 0.25, 0.25, 0.25)
                                decayingEntity.world.spawnParticle(Particle.SNEEZE, decayingEntity.location, 40, 0.25, 0.25, 0.25)
                                decayingEntity.world.spawnParticle(Particle.SCRAPE, decayingEntity.location, 20, 0.5, 1.0, 0.5)

                                val decayEffect = PotionEffect(PotionEffectType.HUNGER, 10 * 20 , 0)
                                decayingEntity.addPotionEffect(decayEffect)

                                val pop = listOf(decayEffect)
                                decayingEntity.addPotionEffects(pop)
                                val oww = listOf<PotionEffect>()
                                if (oww.isEmpty())

                                // Every 2 secs
                                decayingEntity.addScoreboardTag("Decaying")
                                val decayingTask = DecayingTask(decayingEntity, decayingTouchFactor)
                                decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20 * 2)


                            }
                        }
                    }
                }
            }
        }
    }


    // DOUSE Enchantment Effects
    @EventHandler
    fun douseEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val dousedEntity: LivingEntity = event.entity as LivingEntity
                var isDoused = false

                if ("Doused" in dousedEntity.scoreboardTags) {
                    isDoused = true
                }

                if (!isDoused) {
                    if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                        val someWeapon = somePlayer.inventory.itemInMainHand
                        if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.DOUSE)) {
                            if (somePlayer.gameMode != GameMode.SPECTATOR) {
                                val douseFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.DOUSE)
                                dousedEntity.scoreboardTags.add("Doused")
                                dousedEntity.scoreboardTags.add("Doused_Factor_$douseFactor")
                                val dousingTask = DousedTask(dousedEntity)
                                dousingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                return
                            }
                        }
                    }
                }


                if (dousedEntity.fireTicks > 0) {
                    var removeDouse = false
                    var douseFactorPower: Int? = null
                    var tagToRemove: String? = null
                    if (isDoused) {
                        for (x in 1..3) {
                            if ("Doused_Factor_$x" in dousedEntity.scoreboardTags) {
                                tagToRemove = "Doused_Factor_$x"
                                douseFactorPower = x
                                removeDouse = true
                            }
                        }
                    }
                    if (removeDouse) {
                        dousedEntity.scoreboardTags.remove("Doused")
                        dousedEntity.scoreboardTags.remove(tagToRemove!!)

                        // sounds


                        // do douse effects
                        dousedEntity.addScoreboardTag("Ignited")
                        dousedEntity.fireTicks = 20 * ((douseFactorPower!! * 4) + 4) + 1
                        val igniteDouseTask = DouseIgniteTask(dousedEntity, douseFactorPower)
                        igniteDouseTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)

                    }
                }
            }
        }
    }


    // EXPLODING enchantment effects
    @EventHandler
    fun explodingEnchant(event: EntityDeathEvent) {
        if (event.entity.killer is Player) {
            if (event.entity !is Player) {
                val somePlayer = event.entity.killer!!
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.EXPLODING)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {

                            if (!playersExplodingCooldown.containsKey(somePlayer.uniqueId)) {
                                playersExplodingCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - playersExplodingCooldown[somePlayer.uniqueId]!!

                            if (timeElapsed > 1 * 1000) {
                                playersExplodingCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
                                // Boom variables
                                val boomLocation = event.entity.location
                                val boomMagnitude = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.EXPLODING)

                                // Fireball
                                val boomExplosion: Fireball = somePlayer.world.spawnEntity(boomLocation, EntityType.FIREBALL) as Fireball
                                boomExplosion.setIsIncendiary(false)
                                boomExplosion.yield = 0.0F
                                boomExplosion.direction = Vector(0.0, -3.0, 0.0)

                                // Firework effect and color
                                val boomFirework: Firework = somePlayer.world.spawnEntity(boomLocation, EntityType.FIREWORK) as Firework
                                val boomFireworkMeta = boomFirework.fireworkMeta
                                val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA, Color.ORANGE)
                                boomFireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(randomColors.random()).withFade(randomColors.random()).trail(true).flicker(true).build())
                                boomFireworkMeta.power = boomMagnitude * 30
                                boomFirework.fireworkMeta = boomFireworkMeta
                                boomFirework.velocity = Vector(0.0, -3.0, 0.0)

                                // Particles
                                somePlayer.world.spawnParticle(Particle.FLASH, boomLocation, 5, 1.0, 1.0, 1.0)
                                somePlayer.world.spawnParticle(Particle.LAVA, boomLocation, 35, 1.5, 1.0, 1.5)
                            }
                        }
                    }
                }
            }
        }
    }


    // FREEZING_ASPECT enchantment effects
    @EventHandler
    fun freezingAspectEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val freezingEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.FREEZING_ASPECT)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {
                            // Fix for powder LATER
                            if (event.entity.freezeTicks <= 50) {
                                val freezeFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.FREEZING_ASPECT)

                                // Effects
                                val freezingSlow = PotionEffect(PotionEffectType.SLOW, (freezeFactor * 20 * 3) - 2, freezeFactor - 1)
                                freezingEntity.addPotionEffect(freezingSlow)

                                // Particles
                                somePlayer.world.spawnParticle(Particle.SNOWFLAKE, freezingEntity.location, 25, 1.0, 0.5, 1.0)
                                println("Applied Freeze Stuff")

                                //
                                freezingEntity.addScoreboardTag("Freezing")
                                val freezingTask = FreezingTask(freezingEntity, freezeFactor)
                                freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                            }

                        }
                    }
                }
            }
        }
    }


    // FROG_FRIGHT enchantment effects
    @EventHandler
    fun frogFrightEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val froggingEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.FROG_FRIGHT)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {
                            val frogFrightFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.FROG_FRIGHT)
                            // MATH
                            val closeEntityLocation = froggingEntity.location.clone().add(0.0, 1.75, 0.0)
                            val someDirectionVector = closeEntityLocation.clone().subtract(somePlayer.location.clone()).toVector()
                            val someUnitVector = someDirectionVector.clone().normalize()
                            val someNewVector = someUnitVector.clone().multiply(1.15 + (0.15 * frogFrightFactor))
                            froggingEntity.velocity = someNewVector

                            val someToungTask = FrogFrightTask(froggingEntity, someNewVector.clone())
                            someToungTask.runTaskTimer(MinecraftOdyssey.instance, 20, 1)

                            somePlayer.world.spawnParticle(Particle.TOWN_AURA, somePlayer.location, 65, 1.0, 0.5, 1.0)
                            somePlayer.playSound(somePlayer.location, Sound.ENTITY_FROG_EAT, 5.5F, 0.5F)

                        }
                    }
                }
            }
        }
    }


    // GRAVITY_WELL enchantment effects
    @EventHandler
    fun gravityWellEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val gravitatingEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.GRAVITY_WELL)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {

                            if (!playersGravityWellCooldown.containsKey(somePlayer.uniqueId)) {
                                playersGravityWellCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - playersGravityWellCooldown[somePlayer.uniqueId]!!

                            if (timeElapsed > 8 * 1000) {
                                playersGravityWellCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.5F, 0.2F)

                                val singularityPoint = event.entity.location
                                val gravityWellFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.GRAVITY_WELL)

                                // Every 10 ticks -> 0.5 sec
                                gravitatingEntity.addScoreboardTag("Gravity_Well")
                                val gravityWellTask = GravitationalAttract(gravitatingEntity, singularityPoint, gravityWellFactor, somePlayer)
                                gravityWellTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10)

                            }
                        }
                    }
                }
            }
        }
    }


    // GUARDING_STRIKE enchantment effects
    @EventHandler
    fun guardingStrikeEnchant(event: EntityDeathEvent) {
        if (event.entity.killer is Player) {
            val somePlayer: Player = event.entity.killer as Player
            if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                val someWeapon = somePlayer.inventory.itemInMainHand
                if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.GUARDING_STRIKE)) {
                    if (somePlayer.gameMode != GameMode.SPECTATOR) {

                        if (!playersGuardingStrikeCooldown.containsKey(somePlayer.uniqueId)) {
                            playersGuardingStrikeCooldown[somePlayer.uniqueId] = 0L
                        }

                        val timeElapsed: Long = System.currentTimeMillis() - playersGuardingStrikeCooldown[somePlayer.uniqueId]!!

                        if (timeElapsed > 8 * 1000) {
                            playersGuardingStrikeCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                            val guardingFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.GUARDING_STRIKE)
                            // Effects
                            val guardingPose = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (2 + (guardingFactor * 2)) * 20, 0)
                            somePlayer.addPotionEffect(guardingPose)
                            // Particles
                            somePlayer.world.spawnParticle(Particle.SCRAPE, somePlayer.location, 35, 1.0, 0.5, 1.0)
                            somePlayer.world.spawnParticle(Particle.ELECTRIC_SPARK, somePlayer.location, 35, 1.0, 0.5, 1.0)
                            somePlayer.playSound(somePlayer.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
                            somePlayer.playSound(somePlayer.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)

                            println("Did guarding strike")
                        }
                    }
                }
            }
        }
    }

    // HEMORRHAGE Enchantment Effects
    @EventHandler
    fun hemorrhageEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val hemorrhagingEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.HEMORRHAGE)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {

                            if (!playersHemorrhageCooldown.containsKey(somePlayer.uniqueId)) {
                                playersHemorrhageCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - playersHemorrhageCooldown[somePlayer.uniqueId]!!
                            val hemorrhageFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.HEMORRHAGE)

                            if (timeElapsed > (3.5 - hemorrhageFactor) * 1000) {
                                playersHemorrhageCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                // Every 1.5 secs
                                hemorrhagingEntity.addScoreboardTag("Hemorrhaging")
                                val hemorrhageTask = HemorrhageTask(hemorrhagingEntity, hemorrhageFactor)
                                hemorrhageTask.runTaskTimer(MinecraftOdyssey.instance, 0, 30)

                            }
                        }
                    }
                }
            }
        }
    }


    // VOID_STRIKE enchantment effects
    @EventHandler
    fun voidStrikeEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val voidTouchedEntity: LivingEntity = event.entity as LivingEntity
                // Add more monitors
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.VOID_STRIKE)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {

                            if (somePlayer.itemUseRemainingTime == 0) {

                                if (!playersVoidStrikeCooldown.containsKey(somePlayer.uniqueId)) {
                                    playersVoidStrikeCooldown[somePlayer.uniqueId] = 0L
                                }

                                val timeElapsed: Long = System.currentTimeMillis() - playersVoidStrikeCooldown[somePlayer.uniqueId]!!

                                if (timeElapsed > 0.75 * 1000) {
                                    playersVoidStrikeCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                    println("V")
                                    val someTags = voidTouchedEntity.scoreboardTags
                                    var voidDamage = 0
                                    var playerMatch = false
                                    var voidTouched = false
                                    var voidConflict = false

                                    val onlinePlayers = event.damager.world.players

                                    // tag variables
                                    var tagToRemove: String? = null
                                    val tagsToRemove = mutableListOf<String>()
                                    var tagToAdd: String? = null

                                    // Initial check
                                    for (tag in someTags) {
                                        for (unknownPlayer in onlinePlayers) {
                                            if (tag == "VoidTouched_${unknownPlayer.name}" && tag != "VoidTouched_${somePlayer.name}") {
                                                voidConflict = true
                                                break
                                            }
                                        }
                                        if (voidConflict) {
                                            break
                                        }
                                        // Check if player match
                                        if (tag == "VoidTouched_${somePlayer.name}") {
                                            playerMatch = true
                                            voidTouched = true
                                        }
                                        println(tag)
                                    }
                                    // Remove conflicts RESET
                                    if (voidConflict) {
                                        for (tag in someTags) {
                                            for (unknownPlayer in onlinePlayers) {
                                                if (tag == "VoidTouched_${unknownPlayer.name}") {
                                                    //voidTouchedEntity.removeScoreboardTag(tag)
                                                    tagsToRemove.add(tag)
                                                }
                                            }
                                        }
                                        for (someTag in tagsToRemove) {
                                            voidTouchedEntity.removeScoreboardTag(someTag)
                                        }
                                    }
                                    val voidTouchFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.VOID_STRIKE)

                                    // Run if no other voided
                                    if (!voidConflict && playerMatch) {
                                        println(someTags)
                                        for (tag in someTags) {
                                            // Max modifier
                                            if (tag == "VoidStruck_${somePlayer.name}_10") {
                                                //voidTouchedEntity.removeScoreboardTag("VoidStruck_${somePlayer.name}_10")
                                                tagToRemove = "VoidStruck_${somePlayer.name}_10"
                                                //voidTouchedEntity.addScoreboardTag("VoidStruck_${somePlayer.name}_0")
                                                tagToAdd = "VoidStruck_${somePlayer.name}_0"
                                                break
                                            }
                                            for (x in 0..9) {
                                                if (tag == "VoidStruck_${somePlayer.name}_$x") {
                                                    //voidTouchedEntity.removeScoreboardTag("VoidStruck_${somePlayer.name}_$x")
                                                    tagToRemove = "VoidStruck_${somePlayer.name}_$x"
                                                    val newX = x + 1
                                                    //voidTouchedEntity.addScoreboardTag("VoidStruck_${somePlayer.name}_$newX")
                                                    tagToAdd = "VoidStruck_${somePlayer.name}_$newX"
                                                    voidDamage = (voidTouchFactor * x) + voidTouchFactor
                                                    break
                                                }
                                            }
                                        }
                                        voidTouchedEntity.addScoreboardTag(tagToAdd!!)
                                        voidTouchedEntity.removeScoreboardTag(tagToRemove!!)

                                    }
                                    // Apply initial tags
                                    if (!voidTouched) {
                                        voidTouchedEntity.addScoreboardTag("VoidStruck_${somePlayer.name}_0")
                                        voidTouchedEntity.addScoreboardTag("VoidTouched_${somePlayer.name}")
                                    }
                                    // Check if player
                                    if (playerMatch) {
                                        somePlayer.world.spawnParticle(Particle.PORTAL, voidTouchedEntity.location, 85, 1.15, 0.85, 1.15)
                                        somePlayer.world.spawnParticle(Particle.WAX_OFF, voidTouchedEntity.location, 45, 1.0, 0.75, 1.0)
                                        somePlayer.world.spawnParticle(Particle.SPELL_WITCH, voidTouchedEntity.location, 50, 1.0, 0.75, 1.0)
                                        somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_DEACTIVATE, 1.5F, 0.5F)
                                        somePlayer.playSound(somePlayer.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.7F, 0.2F)
                                        somePlayer.playSound(somePlayer.location, Sound.ENTITY_ENDER_EYE_DEATH, 3.5F, 0.4F)
                                        event.damage += voidDamage
                                        println(event.damage)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //WHIRLWIND enchantment effects
    @EventHandler
    fun whirlwindEnchantment(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val somePlayer: Player = event.damager as Player
            if (event.entity is LivingEntity) {
                val someEntity: LivingEntity = event.entity as LivingEntity
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.WHIRLWIND)) {
                        if (somePlayer.gameMode != GameMode.SPECTATOR) {
                            if (someEntity.hasLineOfSight(somePlayer)) {
                                if (!playersWhirlwindCooldown.containsKey(somePlayer.uniqueId)) {
                                    playersWhirlwindCooldown[somePlayer.uniqueId] = 0L
                                }

                                val timeElapsed: Long =
                                    System.currentTimeMillis() - playersWhirlwindCooldown[somePlayer.uniqueId]!!

                                if (timeElapsed > 1.0 * 1000) {
                                    val whirlwindFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.WHIRLWIND)
                                    playersWhirlwindCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                    //1.25
                                    val entitiesNearPlayer = somePlayer.world.getNearbyLivingEntities(somePlayer.location, 1.375)
                                    entitiesNearPlayer.remove(somePlayer)

                                    //Particles
                                    val xCord = somePlayer.location.x - 1.25
                                    val yCord = somePlayer.location.y + 1
                                    val zCord = somePlayer.location.z
                                    //val someLocation = somePlayer.location

                                    for (n in 0..16) {
                                        val s = (2.5 / 16) * n
                                        if (n == 16 || n == 0) {
                                            somePlayer.world.spawnParticle(Particle.EXPLOSION_LARGE, xCord + s, yCord, zCord, 1)
                                        }
                                        else {
                                            var q = s
                                            if (n > 8) {
                                                q -= (2.5 / 16) * (n - 8)
                                            }
                                            somePlayer.world.spawnParticle(Particle.EXPLOSION_LARGE, xCord + s, yCord, zCord + q, 1)
                                            somePlayer.world.spawnParticle(Particle.EXPLOSION_LARGE, xCord + s, yCord, zCord - q, 1)
                                        }
                                    }

                                    val someDamage = event.damage
                                    val whirlwindPercentage = (0.3 + ((whirlwindFactor * 3) * 0.1))
                                    val whirlDamage = someDamage * whirlwindPercentage
                                    println("event: $someDamage, percent: $whirlwindPercentage, newDamage: $whirlDamage")

                                    val somePlayerLocation = somePlayer.location.clone()
                                    for (closeEntity in entitiesNearPlayer) {
                                        if (closeEntity != someEntity) closeEntity.damage(whirlDamage, somePlayer)
                                        // MATH
                                        val closeEntityLocation = closeEntity.location.clone().add(0.0, 1.5, 0.0)
                                        val someDirectionVector = closeEntityLocation.clone().subtract(somePlayerLocation.clone()).toVector()
                                        val someUnitVector = someDirectionVector.clone().normalize()
                                        val someNewVector = someUnitVector.clone().multiply(0.75 + (0.1 * whirlwindFactor))
                                        closeEntity.velocity = someNewVector
                                        //somePlayer.world.spawnParticle(Particle.SWEEP_ATTACK, closeEntity.location, 1, 0.05, 0.05, 0.05)
                                    }
                                    somePlayer.playSound(somePlayer.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
                                    somePlayer.playSound(somePlayer.location, Sound.ITEM_SHIELD_BLOCK, 1.2F, 0.6F)
                                    somePlayer.playSound(somePlayer.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.2F, 0.6F)
                                    somePlayer.playSound(somePlayer.location, Sound.ITEM_TRIDENT_RIPTIDE_2, 1.2F, 1.2F)


                                }
                            }
                        }
                    }
                }
            }
        }
    }


}