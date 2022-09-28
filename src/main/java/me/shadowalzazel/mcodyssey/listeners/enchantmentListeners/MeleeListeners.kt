package me.shadowalzazel.mcodyssey.listeners.enchantmentListeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.effects.OdysseyEffectFunctions
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.tasks.ArcaneCellTask
import me.shadowalzazel.mcodyssey.listeners.tasks.FrogFrightTask
import me.shadowalzazel.mcodyssey.listeners.tasks.FrostyFuseTask
import me.shadowalzazel.mcodyssey.listeners.tasks.GravityWellTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
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
    private var frostyFuseCooldown = mutableMapOf<UUID, Long>()
    private var gravityWellCooldown = mutableMapOf<UUID, Long>()
    private var guardingStrikeCooldown = mutableMapOf<UUID, Long>()
    private var hemorrhageCooldown = mutableMapOf<UUID, Long>()
    private var voidStrikeCooldown = mutableMapOf<UUID, Long>()
    private var whirlwindCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to entity damage
    @EventHandler
    fun mainMeleeDamageHandler(event: EntityDamageByEntityEvent) {
        // Check if event damager and damaged is living entity
        if (event.damager is LivingEntity && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val someDamager = event.damager as LivingEntity
            val someVictim = event.entity as LivingEntity
            // Make thorns bug new enchant apply ranged effects !!!!!
            // Check if active item has lore
            if (someDamager.equipment?.itemInMainHand?.hasItemMeta() == true) {
                val someWeapon = someDamager.equipment?.itemInMainHand
                // Loop for all enchants
                for (enchant in someWeapon!!.enchantments) {
                    // When match
                    when (enchant.key) {
                        OdysseyEnchantments.ARCANE_CELL -> {
                            arcaneCellEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.BACKSTABBER -> {
                            if (!backstabberCooldown.containsKey(someDamager.uniqueId)) {
                                backstabberCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - backstabberCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 3.0 * 1000) {
                                backstabberCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                event.damage += backstabberEnchantment(someDamager, someVictim, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Backstabber on Cooldown (Time Remaining: ${6.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                        OdysseyEnchantments.BANE_OF_THE_ILLAGER -> {
                            event.damage += baneOfTheIllagerEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.BANE_OF_THE_SEA -> {
                            event.damage += baneOfTheSeaEnchantment(someDamager, someVictim, enchant.value)
                        }
                        OdysseyEnchantments.BANE_OF_THE_SWINE -> {
                            event.damage += baneOfTheSwineEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.BUZZY_BEES -> {
                            if (!buzzyBeesCooldown.containsKey(someDamager.uniqueId)) {
                                buzzyBeesCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - buzzyBeesCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 4.5 * 1000) {
                                buzzyBeesCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                buzzyBeesEnchantment(someVictim, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Buzzy Bees on Cooldown (Time Remaining: ${4.5 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                        OdysseyEnchantments.COMMITTED -> {
                            event.damage += committedEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.DECAYING_TOUCH -> {
                            if (!decayingTouchCooldown.containsKey(someDamager.uniqueId)) {
                                decayingTouchCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - decayingTouchCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 3.0 * 1000) {
                                decayingTouchCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                decayingTouchEnchantment(someVictim, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Decaying Touch on Cooldown (Time Remaining: ${3.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                        OdysseyEnchantments.DOUSE -> {
                            douseEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.ECHO -> {
                            echoEnchantment(someDamager, someVictim, enchant.value)
                        }
                        OdysseyEnchantments.FREEZING_ASPECT -> {
                            freezingAspectEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.FROG_FRIGHT -> {
                            frogFrightEnchantment(someDamager, someVictim, enchant.value)
                        }
                        OdysseyEnchantments.FROSTY_FUSE -> {
                            if (!frostyFuseCooldown.containsKey(someDamager.uniqueId)) {
                                frostyFuseCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - frostyFuseCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 5.0 * 1000) {
                                frostyFuseCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                frostyFuseEnchantment(someVictim, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Frosty Fuse on Cooldown (Time Remaining: ${5.0 - ((timeElapsed / 1) * 0.001)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                            frostyFuseEnchantment(someVictim, enchant.value)
                        }
                        OdysseyEnchantments.GRAVITY_WELL -> {
                            if (!gravityWellCooldown.containsKey(someDamager.uniqueId)) {
                                gravityWellCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - gravityWellCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 8.0 * 1000) {
                                gravityWellCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                gravityWellEnchantment(someDamager, someVictim, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Gravity Well on Cooldown (Time Remaining: ${8.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                        OdysseyEnchantments.GUARDING_STRIKE -> {
                            if (!guardingStrikeCooldown.containsKey(someDamager.uniqueId)) {
                                guardingStrikeCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - guardingStrikeCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 4.0 * 1000) {
                                guardingStrikeCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                guardingStrikeEnchantment(someDamager, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Guarding Strike on Cooldown (Time Remaining: ${4.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                        OdysseyEnchantments.HEMORRHAGE -> {
                            if (!hemorrhageCooldown.containsKey(someDamager.uniqueId)) {
                                hemorrhageCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - hemorrhageCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 3.0 * 1000) {
                                hemorrhageCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                hemorrhageEnchantment(someVictim, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Hemorrhage on Cooldown (Time Remaining: ${3.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                        OdysseyEnchantments.ILLUCIDATION -> {
                            event.damage += illucidationEnchantment(someVictim, enchant.value, event.isCritical)
                        }
                        OdysseyEnchantments.VOID_STRIKE -> {
                            if (!voidStrikeCooldown.containsKey(someDamager.uniqueId)) {
                                voidStrikeCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - voidStrikeCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 0.5 * 1000) {
                                voidStrikeCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                event.damage += voidStrikeEnchantment(someDamager, someVictim, enchant.value)
                            }
                        }
                        OdysseyEnchantments.WHIRLWIND -> {
                            if (!whirlwindCooldown.containsKey(someDamager.uniqueId)) {
                                whirlwindCooldown[someDamager.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - whirlwindCooldown[someDamager.uniqueId]!!
                            if (timeElapsed > 3.0 * 1000) {
                                whirlwindCooldown[someDamager.uniqueId] = System.currentTimeMillis()
                                whirlwindEnchantment(someDamager, someVictim, event.damage, enchant.value)
                            } else {
                                someDamager.sendActionBar(
                                    Component.text(
                                        "Whirlwind on Cooldown (Time Remaining: ${3.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Main function for enchantments relating to entity deaths
    @EventHandler
    fun mainMeleeDeathHandler(event: EntityDeathEvent) {
        // Check if event killer and entity are living entity
        if (event.entity.killer is LivingEntity) { // Make thorns bug new enchant apply ranged effects
            val someKiller = event.entity.killer as LivingEntity
            val someVictim: LivingEntity = event.entity
            // Check if active item has lore
            if (someKiller.equipment?.itemInMainHand?.hasItemMeta() == true) {
                val someWeapon = someKiller.equipment?.itemInMainHand
                // Loop for all enchants
                for (enchant in someWeapon!!.enchantments) {
                    // When Match
                    when (enchant.key) {
                        OdysseyEnchantments.EXPLODING -> {
                            if (!explodingCooldown.containsKey(someKiller.uniqueId)) {
                                explodingCooldown[someKiller.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - explodingCooldown[someKiller.uniqueId]!!
                            if (timeElapsed > 1.25 * 1000) {
                                explodingCooldown[someKiller.uniqueId] = System.currentTimeMillis()
                                explodingEnchantment(someVictim, enchant.value)
                            }
                        }
                        OdysseyEnchantments.GUARDING_STRIKE -> {
                            if (!guardingStrikeCooldown.containsKey(someKiller.uniqueId)) {
                                guardingStrikeCooldown[someKiller.uniqueId] = 0L
                            }
                            val timeElapsed: Long =
                                System.currentTimeMillis() - guardingStrikeCooldown[someKiller.uniqueId]!!
                            if (timeElapsed > 4.0 * 1000) {
                                guardingStrikeCooldown[someKiller.uniqueId] = System.currentTimeMillis()
                                guardingStrikeEnchantment(someKiller, enchant.value)
                            } else {
                                someKiller.sendActionBar(
                                    Component.text(
                                        "Guarding Strike on Cooldown (Time Remaining: ${4.0 - (timeElapsed / 1000)}s)",
                                        TextColor.color(255, 255, 85)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------*/

    // ARCANE_CELL Enchantment Function
    private fun arcaneCellEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Effects
        with(eventVictim) {
            if (!scoreboardTags.contains("Arcane_Jailed")) {
                addPotionEffects(
                    listOf(
                        PotionEffect(PotionEffectType.SLOW, (20 * ((enchantmentStrength * 2) + 2)), 4),
                        PotionEffect(PotionEffectType.SLOW_DIGGING, (20 * ((enchantmentStrength * 2) + 2)), 3)
                    )
                )
                addScoreboardTag("Arcane_Jailed")
                val arcaneCellTask = ArcaneCellTask(eventVictim, location, (2 + (enchantmentStrength * 2)) * 4)
                arcaneCellTask.runTaskTimer(MinecraftOdyssey.instance, 5, 5)
            }
        }

    }

    // BACKSTABBER Enchantment Function
    private fun backstabberEnchantment(
        eventDamager: LivingEntity,
        eventVictim: LivingEntity,
        enchantmentStrength: Int
    ): Int {
        // Check if not target
        val victimTarget = eventVictim.getTargetEntity(10)
        if (victimTarget != eventDamager || eventDamager.isInvisible || eventDamager.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            // Particles and sounds
            with(eventVictim.world) {
                spawnParticle(Particle.CRIT_MAGIC, eventVictim.location, 25, 0.5, 0.5, 0.5)
                spawnParticle(Particle.WARPED_SPORE, eventVictim.location, 25, 0.5, 0.5, 0.5)
                spawnParticle(Particle.ELECTRIC_SPARK, eventVictim.location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT, eventVictim.location, 15, 0.5, 0.5, 0.5)
                playSound(eventVictim.location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
            }
            // Damage
            return (3 + (enchantmentStrength * 3))
        }
        return 0
    }

    // BANE_OF_THE_ILLAGER Enchantment Function
    private fun baneOfTheIllagerEnchantment(
        eventVictim: LivingEntity,
        enchantmentStrength: Int
    ): Double {
        if (eventVictim is Raider) {
            return enchantmentStrength.toDouble() * 2.5
        }
        return 0.0
    }

    // BANE_OF_THE_SEA Enchantment Function
    private fun baneOfTheSeaEnchantment(
        eventDamager: LivingEntity,
        eventVictim: LivingEntity,
        enchantmentStrength: Int
    ): Double  {
        if (eventVictim.isInWaterOrRainOrBubbleColumn || eventVictim is WaterMob || eventVictim.isSwimming || eventDamager.isInWaterOrRainOrBubbleColumn) {
            return enchantmentStrength.toDouble() * 2.0
        }
        return 0.0
    }

    // BANE_OF_THE_SWINE Enchantment Function
    private fun baneOfTheSwineEnchantment(
        eventVictim: LivingEntity,
        enchantmentStrength: Int
    ): Double  {
        if (eventVictim is PiglinAbstract || eventVictim is Pig || eventVictim is Hoglin) {
            return enchantmentStrength.toDouble() * 2.5
        }
        return 0.0
    }

    // BUZZY_BEES Enchantment Function
    private fun buzzyBeesEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Spawn Bee if not low
        with(eventVictim) {
            if (!isDead) {
                // Bee
                (world.spawnEntity(eventVictim.location.clone().add((-50..50).random() * 0.1, (30..80).random() * 0.1, (-50..50).random() * 0.1), EntityType.BEE) as Bee).also {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.SPEED, 10 * 20, enchantmentStrength - 1),
                            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, enchantmentStrength - 1),
                            PotionEffect(PotionEffectType.FAST_DIGGING, 10 * 20, 0),
                            PotionEffect(PotionEffectType.ABSORPTION, 10 * 20, enchantmentStrength - 1)
                        )
                    )
                    it.target = this
                }
            }
            OdysseyEffectFunctions.honeyedEffect(mutableListOf(this), ((3 * enchantmentStrength) + 3) * 2)
            world.playSound(location, Sound.BLOCK_HONEY_BLOCK_FALL, 2.5F, 0.9F)
        }
    }

    // COMMITTED Enchantment Function
    private fun committedEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int): Int {
        return if (eventVictim.health < eventVictim.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value * 0.4) {
            enchantmentStrength + 1
        } else {
            0
        }
    }


    // DECAYING_TOUCH Enchantment Function
    private fun decayingTouchEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Victim
        with(eventVictim) {
            OdysseyEffectFunctions.decayingEffect(mutableListOf(this), 10, enchantmentStrength * 1)
            world.playSound(location, Sound.BLOCK_BIG_DRIPLEAF_TILT_UP, 2.5F, 0.9F)
        }
    }

    // DOUSE Enchantment Function
    private fun douseEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Victim
        with(eventVictim) {
            OdysseyEffectFunctions.dousedEffect(mutableListOf(this), 10, enchantmentStrength * 1)
            world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 2.5F, 1.5F)
        }
    }

    // ECHO Enchantment Function
    private fun echoEnchantment(eventDamager: LivingEntity, eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Prevent recursive call
        if (eventVictim.scoreboardTags.contains("Echo_Struck")) {
            eventVictim.scoreboardTags.remove("Echo_Struck")
            return
        }
        if ((0..100).random() < enchantmentStrength * 20) {
            if (!eventVictim.isDead) {
                // Swing
                eventDamager.swingOffHand()
                eventVictim.addScoreboardTag("Echo_Struck")
                eventDamager.attack(eventVictim)
                // Particles
                with(eventVictim.world) {
                    spawnParticle(
                        Particle.SWEEP_ATTACK,
                        eventVictim.location.clone().add(0.0, 0.25, 0.0),
                        3,
                        0.05,
                        0.05,
                        0.05
                    )
                    playSound(eventVictim.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.7F)
                }
            }
        }
    }

    // EXPLODING Enchantment Function
    private fun explodingEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA, Color.ORANGE)
        val someLocation = eventVictim.location
        with(eventVictim.world) {
            // Particles
            spawnParticle(Particle.FLASH, someLocation, 2, 0.2, 0.2, 0.2)
            spawnParticle(Particle.LAVA, someLocation, 25, 1.5, 1.0, 1.5)
            // Fireball
            (spawnEntity(someLocation, EntityType.FIREBALL) as Fireball).also {
                it.setIsIncendiary(false)
                it.yield = 0.0F
                it.direction = Vector(0.0, -3.0, 0.0)
            }
            // Firework
            (spawnEntity(someLocation, EntityType.FIREWORK) as Firework).also {
                val newMeta = it.fireworkMeta
                newMeta.power = enchantmentStrength * 30
                newMeta.addEffect(
                    FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(randomColors.random())
                        .withFade(randomColors.random()).trail(true).flicker(true).build()
                )
                it.fireworkMeta = newMeta
                it.velocity = Vector(0.0, -3.0, 0.0)
            }
        }
    }

    // FREEZING_ASPECT Enchantment Function
    private fun freezingAspectEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Victim Effects
        with(eventVictim) {
            if (freezeTicks <= 50) {
                OdysseyEffectFunctions.freezingEffect(mutableListOf(this), 8, enchantmentStrength * 1)
                world.spawnParticle(Particle.SNOWFLAKE, this.location, 25, 1.0, 0.5, 1.0)
            }
        }
    }

    // FROG_FRIGHT Enchantment Function
    private fun frogFrightEnchantment(eventDamager: LivingEntity, eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Victim Effects
        eventVictim.also {
            it.velocity.multiply(0.9)
            val tongueLashVector = eventDamager.location.clone().subtract(it.location).toVector().normalize().multiply(1.1 + (enchantmentStrength * 0.1))
            val frogFrightTask = FrogFrightTask(eventVictim, tongueLashVector)
            frogFrightTask.runTaskLater(MinecraftOdyssey.instance, 14)
            it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 3, 1))
            it.damage(1.0)
        }

        // Particles
        with(eventVictim.world) {
            spawnParticle(Particle.TOWN_AURA, eventVictim.location, 15, 0.25, 0.25, 0.25)
            playSound(eventVictim.location, Sound.ENTITY_FROG_EAT, 2.5F, 0.7F)
        }

    }

    // FROSTY_FUSE Enchantment Function
    private fun frostyFuseEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Victim Effects
        with(eventVictim) {
            if (!scoreboardTags.contains("Frosted_Fuse")) {
                addScoreboardTag("Frosted_Fuse")
                val frostedFuseTask = FrostyFuseTask(eventVictim, enchantmentStrength * 1, 5 * 4)
                frostedFuseTask.runTaskTimer(MinecraftOdyssey.instance, 5, 5)
            }
        }

    }

    // GRAVITY_WELL Enchantment Function
    private fun gravityWellEnchantment(
        eventDamager: LivingEntity,
        eventVictim: LivingEntity,
        enchantmentStrength: Int
    ) {
        // Victim Effects
        with(eventVictim) {
            // Particles
            world.playSound(location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.75F, 0.3F)
            world.spawnParticle(Particle.WHITE_ASH, location, 55, 1.0, 0.5, 1.0)
            // Task
            addScoreboardTag("Gravity_Well")
            val gravityWellTask =
                GravityWellTask(eventVictim, eventDamager, enchantmentStrength * 1, ((enchantmentStrength * 2) + 2) * 2)
            gravityWellTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10)
        }

    }

    // GUARDING_STRIKE Enchantment Function
    private fun guardingStrikeEnchantment(eventHitter: LivingEntity, enchantmentStrength: Int) {
        // Effects
        with(eventHitter) {
            if (velocity.length() < 0.05) {
                addPotionEffect(
                    PotionEffect(
                        PotionEffectType.DAMAGE_RESISTANCE,
                        (2 + (enchantmentStrength * 3)) * 20,
                        0
                    )
                )
            }
            // Particles and Sounds
            world.spawnParticle(Particle.SCRAPE, location, 35, 1.0, 0.5, 1.0)
            world.spawnParticle(Particle.ELECTRIC_SPARK, location, 35, 1.0, 0.5, 1.0)
            world.playSound(location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
            world.playSound(location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
        }
    }

    // HEMORRHAGE Enchantment Function
    private fun hemorrhageEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int) {
        // Victim
        OdysseyEffectFunctions.hemorrhagingEffect(mutableListOf(eventVictim), enchantmentStrength * 1)
        with(eventVictim.world) {
            playSound(eventVictim.location, Sound.BLOCK_NETHER_SPROUTS_PLACE, 2.5F, 0.9F)
            spawnParticle(Particle.CRIT, eventVictim.location, 35, 1.0, 0.5, 1.0)
        }
    }

    // ILLUCIDATION Enchantment Function
    private fun illucidationEnchantment(eventVictim: LivingEntity, enchantmentStrength: Int, isCrit: Boolean): Int {
        // Victim
        var illucidationDamage = 0
        if (eventVictim.isGlowing) {
            illucidationDamage += (enchantmentStrength + 1)
            if (isCrit) {
                illucidationDamage * 2
                eventVictim.isGlowing = false
                if (eventVictim.hasPotionEffect(PotionEffectType.GLOWING)) {
                    eventVictim.removePotionEffect(PotionEffectType.GLOWING)
                }
            }
            with(eventVictim.world) {
                playSound(eventVictim.location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 2.5F, 1.5F)
                spawnParticle(Particle.ELECTRIC_SPARK, eventVictim.location, 55, 1.0, 0.5, 1.0)
                // Glow
            }
        }
        return illucidationDamage
    }


    // VOID_STRIKE Enchantment Function
    private fun voidStrikeEnchantment(
        eventDamager: LivingEntity,
        eventVictim: LivingEntity,
        enchantmentStrength: Int
    ): Int {
        // Variables
        var voidDamage = 0
        var voidTouched = false
        var voidConflict = false
        // Values
        val victimScoreboardTags = eventVictim.scoreboardTags
        val voidStrikeIDs = voidStrikeCooldown.keys.toList()
        val voidDamagerID: UUID = eventDamager.uniqueId

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
                    if (tag.length > 10) {
                        // Change Void Struck tag
                        val voidStruckTagPrefix = tag.subSequence(0..10).toString()
                        if (voidStruckTagPrefix == "Void_Struck") {
                            val tagLastIndex = tag.lastIndex
                            voidStruckValue = tag[tagLastIndex].toString().toInt()
                            break
                        }
                    }
                }
                // Void Struck value to damage and update tag
                if (voidStruckValue != null) {
                    if (voidStruckValue == 9) {
                        eventVictim.scoreboardTags.remove("Void_Struck_${voidStruckValue}")
                        eventVictim.scoreboardTags.add("Void_Struck_0")
                    } else {
                        eventVictim.scoreboardTags.remove("Void_Struck_${voidStruckValue}")
                        eventVictim.scoreboardTags.add("Void_Struck_${voidStruckValue + 1}")
                    }
                    voidDamage = (enchantmentStrength * (voidStruckValue + 1))
                }
            }
            // Add tags if not voided
            else {
                (eventVictim.scoreboardTags).also {
                    it.add("Void_Touched")
                    it.add("Void_Touched_By_${voidDamagerID}")
                    it.add("Void_Struck_0")
                }
            }
            // Particles and Sounds
            with(eventVictim.world) {
                val someLocation = eventVictim.location
                spawnParticle(Particle.PORTAL, someLocation, 85, 1.15, 0.85, 1.15)
                spawnParticle(Particle.WAX_OFF, someLocation, 45, 1.0, 0.75, 1.0)
                spawnParticle(Particle.SPELL_WITCH, someLocation, 50, 1.0, 0.75, 1.0)
                playSound(someLocation, Sound.BLOCK_BEACON_DEACTIVATE, 1.5F, 0.5F)
                playSound(someLocation, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.7F, 0.2F)
                playSound(someLocation, Sound.ENTITY_ENDER_EYE_DEATH, 3.5F, 0.4F)
            }
        }
        // Damage
        return voidDamage
    }

    // WHIRLWIND Enchantment Function
    private fun whirlwindEnchantment(
        eventDamager: LivingEntity,
        eventVictim: LivingEntity,
        eventDamage: Double,
        enchantmentStrength: Int
    ) {
        // Entity list
        val entitiesNearby =
            eventDamager.world.getNearbyLivingEntities(eventDamager.location, 1.5).filter { it != eventDamager }

        // Particles
        with(eventDamager.world) {
            spawnParticle(Particle.EXPLOSION_LARGE, eventDamager.location, 10, 1.25, 0.75, 1.25)
            playSound(eventDamager.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.7F)
            playSound(eventDamager.location, Sound.ITEM_SHIELD_BLOCK, 1.2F, 0.6F)
            playSound(eventDamager.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.2F, 0.6F)
            playSound(eventDamager.location, Sound.ITEM_TRIDENT_RIPTIDE_2, 1.2F, 1.2F)
        }

        // Damage Calculation
        val whirlDamage = eventDamage * ((enchantmentStrength * 3) * 0.1)
        //
        entitiesNearby.forEach {
            if (it != eventVictim) {
                it.damage(whirlDamage, eventDamager)
            }
            it.velocity = it.location.clone().subtract(eventDamager.location).toVector().normalize()
                .multiply(0.75 + (0.1 * enchantmentStrength))
        }
    }

}