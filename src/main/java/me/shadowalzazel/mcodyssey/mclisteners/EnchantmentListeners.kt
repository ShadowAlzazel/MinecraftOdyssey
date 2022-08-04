package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mclisteners.utility.DecayingTask
import me.shadowalzazel.mcodyssey.mclisteners.utility.FreezingTask
import me.shadowalzazel.mcodyssey.mclisteners.utility.HoneyedTask
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
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*

object EnchantmentListeners : Listener {

    // Internal cool downs for enchantments
    private var playersBackstabberCooldown = mutableMapOf<UUID, Long>()
    private var playersBuzzyBeesCooldown = mutableMapOf<UUID, Long>()
    private var playersDecayingTouchCooldown = mutableMapOf<UUID, Long>()
    private var playersExplodingCooldown = mutableMapOf<UUID, Long>()
    private var playersGuardingStrikeCooldown = mutableMapOf<UUID, Long>()
    private var playersPotionBarrierCooldown = mutableMapOf<UUID, Long>()
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

                                    val honeyedTask = HoneyedTask(honeyedEntity, honeyFactor)
                                    // Every 10 ticks -> 0.5 sec
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

                            if (timeElapsed > (3 - decayingTouchFactor) * 1000) {
                                playersDecayingTouchCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                decayingEntity.world.spawnParticle(Particle.SPORE_BLOSSOM_AIR , decayingEntity.location, 40, 0.25, 0.4, 0.25)
                                decayingEntity.world.spawnParticle(Particle.GLOW, decayingEntity.location, 20, 0.25, 0.4, 0.25)
                                decayingEntity.world.spawnParticle(Particle.GLOW_SQUID_INK, decayingEntity.location, 15, 0.25, 0.25, 0.25)
                                decayingEntity.world.spawnParticle(Particle.SNEEZE, decayingEntity.location, 40, 0.25, 0.25, 0.25)
                                decayingEntity.world.spawnParticle(Particle.SCRAPE, decayingEntity.location, 20, 0.5, 1.0, 0.5)

                                val decayEffect = PotionEffect(PotionEffectType.HUNGER, 10 , 0)
                                decayingEntity.addPotionEffect(decayEffect)

                                val decayingTask = DecayingTask(decayingEntity, decayingTouchFactor)
                                // Every 2 secs
                                decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20 * 2)


                            }
                        }
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
                            froggingEntity.velocity = froggingEntity.location.add(0.0, frogFrightFactor * 2.75, 0.0).subtract(froggingEntity.location).toVector().multiply(0.25)
                            somePlayer.world.spawnParticle(Particle.TOWN_AURA, somePlayer.location, 45, 1.0, 0.5, 1.0)
                            somePlayer.playSound(somePlayer.location, Sound.ENTITY_FROG_EAT, 5.5F, 0.5F)

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

                        if (timeElapsed > 10 * 1000) {
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


    // POTION_BARRIER enchantment effects
    @EventHandler
    fun potionBarrierEnchantment(event: PlayerItemConsumeEvent) {
        val somePlayer: Player = event.player
        if (somePlayer.inventory.chestplate != null) {
            if (somePlayer.inventory.chestplate!!.hasItemMeta()) {
                val someArmor = somePlayer.inventory.chestplate
                if (someArmor!!.itemMeta.hasEnchant(OdysseyEnchantments.POTION_BARRIER)) {
                    val potionBarrierFactor = someArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.POTION_BARRIER)
                    if (somePlayer.gameMode != GameMode.SPECTATOR) {
                        // Fix for water bottle
                        if (event.item.type == Material.POTION) {
                            if (!playersPotionBarrierCooldown.containsKey(somePlayer.uniqueId)) {
                                playersPotionBarrierCooldown[somePlayer.uniqueId] = 0L
                            }

                            val timeElapsed: Long = System.currentTimeMillis() - playersPotionBarrierCooldown[somePlayer.uniqueId]!!
                            if (timeElapsed > (15 - potionBarrierFactor) * 1000) {
                                playersPotionBarrierCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                // Effects
                                val potionBarrierEffect = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ((potionBarrierFactor * 2) + 3) * 20, 2)
                                somePlayer.addPotionEffect(potionBarrierEffect)
                                // Particles
                                somePlayer.world.spawnParticle(Particle.SCRAPE, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.world.spawnParticle(Particle.ELECTRIC_SPARK, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.world.spawnParticle(Particle.COMPOSTER, somePlayer.location, 35, 0.5, 0.5, 0.5)
                                somePlayer.playSound(somePlayer.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
                                somePlayer.playSound(somePlayer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)

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
                            val target = someEntity.getTargetEntity(15)
                            if (target != somePlayer) {
                                if (!playersWhirlwindCooldown.containsKey(somePlayer.uniqueId)) {
                                    playersWhirlwindCooldown[somePlayer.uniqueId] = 0L
                                }

                                val timeElapsed: Long =
                                    System.currentTimeMillis() - playersWhirlwindCooldown[somePlayer.uniqueId]!!

                                if (timeElapsed > 0.75 * 1000) {
                                    val whirlwindFactor = someWeapon.itemMeta.getEnchantLevel(OdysseyEnchantments.WHIRLWIND)
                                    playersWhirlwindCooldown[somePlayer.uniqueId] = System.currentTimeMillis()

                                    val entitiesNearPlayer = somePlayer.world.getNearbyLivingEntities(somePlayer.location, 1.25)
                                    entitiesNearPlayer.remove(somePlayer)
                                    entitiesNearPlayer.remove(someEntity)

                                    //Particles
                                    val xCord = somePlayer.location.x - 1.25
                                    val yCord = somePlayer.location.y + 1
                                    val zCord = somePlayer.location.z
                                    val someLocation = somePlayer.location

                                    for (n in 0..16) {
                                        val s = (2.5 / 16) * n
                                        if (n == 16 || n == 0) {
                                            //somePlayer.world.spawnParticle(Particle.SWEEP_ATTACK, xCord + s, yCord, zCord, 3)
                                            somePlayer.spawnParticle(Particle.SWEEP_ATTACK, someLocation.add(xCord + s, yCord, zCord), 1)
                                        }
                                        else {
                                            var q = s
                                            if (n > 8) {
                                                q -= (2.5 / 16) * (n - 8)
                                            }
                                            //somePlayer.world.spawnParticle(Particle.SWEEP_ATTACK, xCord + s, yCord, zCord + q, 3)
                                            //somePlayer.world.spawnParticle(Particle.SWEEP_ATTACK, xCord + s, yCord, zCord - q, 3)
                                            somePlayer.spawnParticle(Particle.SWEEP_ATTACK, someLocation.add(xCord + s, yCord, zCord + q), 1)
                                            somePlayer.spawnParticle(Particle.SWEEP_ATTACK, someLocation.add(xCord + s, yCord, zCord - q), 1)
                                        }
                                    }


                                    val someDamage = event.damage
                                    for (closeEntity in entitiesNearPlayer) {
                                        closeEntity.damage(someDamage * (0.2 + ((whirlwindFactor * 3) / 10)), somePlayer)
                                        //somePlayer.world.spawnParticle(Particle.SWEEP_ATTACK, closeEntity.location, 1, 0.05, 0.05, 0.05)
                                        somePlayer.playSound(closeEntity.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.5F, 0.7F)
                                        somePlayer.playSound(closeEntity.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 3.5F, 0.4F)
                                    }


                                }
                            }
                        }
                    }
                }
            }
        }
    }


}