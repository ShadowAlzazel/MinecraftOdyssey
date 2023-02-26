package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.bosses.base.OdysseyBoss
import me.shadowalzazel.mcodyssey.constants.Identifiers
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.listeners.tasks.GravityWellTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

// Experimental


// DO COROUTINES LATER !!!


class AmbassadorBoss : OdysseyBoss("The Ambassador", "Illusioner") {

    // Boss Spawning Logic
    var bossEntity: Illusioner? = null
    private var despawnTimer: Long = 1
    // Trading Mechanic
    private var patience: Double = 55.0
    private var appeasement: Double = 0.0
    private var playersGiftCooldown = mutableMapOf<UUID, Long>()
    private var playerLikeness = mutableMapOf<UUID, Double>()
    //
    internal var angered: Boolean = false
    // Gifts
    /*
    private val itemLootTable = listOf(Miscellaneous.REFINED_IOJOVIAN_EMERALDS, Miscellaneous.PURE_ALLOY_GOLD, Miscellaneous.POLYMORPHIC_GLUE,
        Miscellaneous.PURE_ALLOY_COPPER, Miscellaneous.GALVANIZED_STEEL, Miscellaneous.REFINED_NEPTUNIAN_DIAMONDS)
     */
    private val itemLootTable = listOf(
        Material.GOLD_BLOCK,
        Material.SLIME_BLOCK,
        Material.WAXED_COPPER_BLOCK,
        Material.IRON_BLOCK,
        Material.DIAMOND,
        Material.EMERALD,
    )

    // Combat Mechanic
    private var takeDamageCooldown: Long = 0L

    // MESSAGES
    private val ambassadorPrefix = Component.text("[The Ambassador] ", TextColor.color(255, 85, 255))

    private val patienceMessages = listOf(
        Component.text(" threatens your safety...", TextColor.color(255, 255, 255)),
        Component.text(" is testing my patience!", TextColor.color(255, 255, 255)),
        Component.text("... The lack of intelligence in this waste is appearance", TextColor.color(255, 255, 255)),
        Component.text("... I expected more from you...", TextColor.color(255, 255, 255)),
        Component.text("... Does your honor not amount to anything?!", TextColor.color(255, 255, 255)),
        Component.text(", that is not an appropriate way to introduce yourself, though what is expected from such a lowlife...", TextColor.color(255, 255, 255)),
        Component.text("'s insolence has prompted me to teach some basic fundamentals of respect.", TextColor.color(255, 255, 255)),
        Component.text("... Time to teach these lowlifes basic manners...", TextColor.color(255, 255, 255)),
        Component.text("Who is doing that?!", TextColor.color(255, 255, 255))
    )

    private val damagedMessages = listOf(
        Component.text("Such Folly... Such Weakness...", TextColor.color(255, 255, 255)),
        Component.text("Any attacks are futile...", TextColor.color(255, 255, 255)),
        Component.text("The damage you think your inflicting is minimal.", TextColor.color(255, 255, 255)),
        Component.text("I am just disappointed...", TextColor.color(255, 255, 255)),
        Component.text("Just stop... Your attacks are worthless.", TextColor.color(255, 255, 255))
    )

    // Create Ambassador Boss in plugin
    fun createBoss(odysseyWorld: World) {
        despawnTimer = System.currentTimeMillis()
        // Spawning
        val spawningPlayer = odysseyWorld.players.random()
        val spawningLocation = spawningPlayer.location.clone().add((-28..28).random().toDouble(), 0.0, (-28..28).random().toDouble())
        spawningLocation.y = 300.0
        // Messages
        with(Odyssey.instance.server) {
            broadcast(vailPrefix.append(Component.text("My Ambassador has arrived!", TextColor.color(255, 255, 85))))
            broadcast(ambassadorPrefix.append(Component.text("I am descending upon ${spawningPlayer.name}'s land...", TextColor.color(255, 255, 255))))
            logger.info("The Ambassador has arrived at x:${spawningLocation.x}, y:${spawningLocation.z}")
        }
        spawningLocation.world.players.forEach { it.playSound(it.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.5F, 0.9F) }

        spawningPlayer.sendMessage(ambassadorPrefix.append(Component.text("Be prepared for my arrival ${spawningPlayer.name}...", TextColor.color(85, 85, 85))))

        bossActive = true
        bossEntity = spawnBossEntity(spawningLocation)
        val departTimer = AmbassadorDepartTask()
        departTimer.runTaskLater(Odyssey.instance, 20 * 60 * 60 * 1)
    }

    // Spawn entity
    private fun spawnBossEntity(spawningLocation: Location): Illusioner {
        val newAmbassadorEntity: Illusioner = (spawningLocation.world.spawnEntity(spawningLocation, EntityType.ILLUSIONER) as Illusioner).apply {
            // Potion Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 1),
                PotionEffect(PotionEffectType.GLOWING, 20 * 300, 1),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 3),
                PotionEffect(PotionEffectType.WATER_BREATHING, 99999, 3),
                PotionEffect(PotionEffectType.SPEED, 99999, 2)))

            // Change Default Behaviour
            customName(Component.text("The Ambassador", TextColor.color(255, 85, 255)))
            isCustomNameVisible = true
            removeWhenFarAway = false
            isCanJoinRaid = false
            isAware = false
            canPickupItems = true

            // Health
            val mobHealth = AttributeModifier(Identifiers.ODYSSEY_BOSS_HEALTH_UUID, "odyssey_mob_health", 930.0, AttributeModifier.Operation.ADD_NUMBER)
            val healthAttribute = getAttribute(Attribute.GENERIC_MAX_HEALTH)
            healthAttribute!!.addModifier(mobHealth)
            health = 950.0

            // Add Kinetic Blaster
            clearActiveItem()
            equipment.setItemInMainHand(Weapons.KINETIC_BLASTER.createItemStack(1))
        }
        return newAmbassadorEntity
    }

    // Defeat Boss
    internal fun defeatedBoss(ambassadorEntity: Illusioner, vanquisher: Player?) {
        if (ambassadorEntity == bossEntity) {
            if (vanquisher is Player) {
                // Spawn loot near vanquisher
                //vanquisher.world.dropItem(vanquisher.location, (Runic.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, 1)))
                vanquisher.world.dropItem(vanquisher.location, (Miscellaneous.PRIMO_GEM.createItemStack(15)))
                vanquisher.giveExpLevels(10)
            }
            val vanquisherName = vanquisher?.name ?: "An unknown Hero"
            // Nearby player get xp and text
            ambassadorEntity.world.getNearbyPlayers(ambassadorEntity.location, 64.0).forEach {
                it.sendMessage(Component.text("The Ambassador has departed ungracefully!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
                it.sendMessage(Component.text("With ", TextColor.color(255, 255, 85), TextDecoration.ITALIC)
                    .append(Component.text(vanquisherName).color(TextColor.color(255, 170, 0)))
                    .append(Component.text(" taking the final blow!").color(TextColor.color(255, 255, 85))).decorate(TextDecoration.ITALIC))
                it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F)
                it.giveExp(3550)
            }
            bossActive = false
            removeBoss = true
        }
    }

    // Despawn boss
    internal fun departBoss() {
        bossEntity!!.world.players.forEach {
            it.sendMessage(Component.text("The Ambassador has left...!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
            it.playSound(it, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F)
        }
        bossEntity!!.remove()
        bossActive = false
        removeBoss = true
    }

    // activate boss
    private fun activateBoss() {
        bossEntity!!.world.getNearbyPlayers(bossEntity!!.location, 64.0).forEach {
            //it.sendMessage(Component.text("The Ambassador has left...!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F)
        }
        bossEntity!!.isAware = true
        val attackCycle = AmbassadorAttackCycle(bossEntity!!)
        attackCycle.runTaskTimer(Odyssey.instance, 0, 20 * 10)
    }

    // Spawn a dummy clone
    private fun spawnDummy() {
        val randomLocation = bossEntity!!.location.clone().add((-8..8).random().toDouble(), 3.0, (-8..8).random().toDouble())
        (bossEntity!!.world.spawnEntity(randomLocation, EntityType.ILLUSIONER) as Illusioner).apply {
            customName(Component.text("The Ambassador", TextColor.color(255, 85, 255)))
            isCustomNameVisible = true
            isCanJoinRaid = false
            isAware = true
            lootTable = null
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.REGENERATION, 20 * 300, 0),
                PotionEffect(PotionEffectType.SLOW, 20 * 300, 1)))
            // Add Item
            clearActiveItem()
            equipment.setItemInMainHand(Weapons.KINETIC_BLASTER.createItemStack(1))
        }
    }

    // Create a new firework entity
    private fun createSuperFirework(targetLocation: Location): Firework {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA)
        val superFirework: Firework = (bossEntity!!.world.spawnEntity(targetLocation, EntityType.FIREWORK) as Firework).apply {
            fireworkMeta = fireworkMeta.clone().also {
                it.addEffect(FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(randomColors.random())
                    .withFade(randomColors.random())
                    .trail(true)
                    .flicker(true)
                    .build()
                )
            }
            fireworkMeta.power = 120
            ticksToDetonate = 20 * 2
            velocity = targetLocation.clone().add(0.0, -1.918, 0.0).subtract(targetLocation).toVector()
            addScoreboardTag("super_firework")
        }
        return superFirework
    }

    // Calls fireworks from the sky
    private fun skyBombardAttack(targetLocation: Location) {
        // Spawn 5 random fireworks
        repeat(5) {
            createSuperFirework(targetLocation.clone().add((-5..5).random().toDouble(), (35..45).random().toDouble(), (-5..5).random().toDouble()))
        }
    }

    // Falling Singularity that attracts
    private fun fallingSingularityAttack(targetLocation: Location) {
        // Spawn falling armor stand wearing singularity thingy
        val fallingSingularity: ArmorStand = (targetLocation.world.spawnEntity(targetLocation, EntityType.ARMOR_STAND) as ArmorStand).apply {
            isSilent = true
            isInvisible = true
            isInvulnerable = true
            isVisible = false
            addScoreboardTag("Gravity_Well")
            addScoreboardTag("Falling_Singularity")
            addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 0))
        }
        val gravityWellTask = GravityWellTask(fallingSingularity, bossEntity!!, 4, 30)
        gravityWellTask.runTaskTimer(Odyssey.instance, 0, 10)
        val standTask = AmbassadorSingularity(fallingSingularity)
        standTask.runTaskLater(Odyssey.instance, 33 * 10)
    }

    // Spawn a vortex that launches players
    private fun gravityLaunchAttack(targetLocation: Location) {
        // make effect/wave

        val gravityWaveEffects = listOf(
            PotionEffect(PotionEffectType.LEVITATION, 20 * 8, 0),
            PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 0))

        targetLocation.getNearbyPlayers(7.5).forEach {
            it.damage(15.5, bossEntity!!)
            //it.velocity.setX(0.0).setY(1.0).setZ(0.0).multiply(2.18)
            it.addPotionEffects(gravityWaveEffects)

            // Sounds and Effects
            it.playSound(it, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.3F, 1.1F)
            it.playSound(it, Sound.ITEM_TRIDENT_THUNDER, 2.5F, 0.8F)
            it.playSound(it, Sound.BLOCK_BEACON_POWER_SELECT, 1.5F, 0.3F)
            it.playSound(it, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0F, 1.0F)
            it.spawnParticle(Particle.DAMAGE_INDICATOR, it.location, 35, 1.5, 0.5, 1.5)
            it.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 55, 2.5, 0.5, 2.5)
            it.spawnParticle(Particle.END_ROD, it.location, 35, 2.0, 1.0, 2.0)
            it.spawnParticle(Particle.SPELL_WITCH, it.location, 55, 1.0, 1.0, 1.0)
            it.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 10, 2.0, 1.0, 2.0)
        }
    }

    // Hijack and Clone Attack
    private fun hijackAttack(someTarget: Player) {
        // Target is hijacked player
        with(someTarget) {
            teleport(bossEntity!!.location)
            addPassenger(bossEntity!!)
            playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
            playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
            playSound(this, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
        }
        someTarget.world.getNearbyPlayers(someTarget.location, 22.0).forEach {
            if (it != someTarget) {
                // Arrow and Vectors
                val someArrow = it.world.spawnEntity(it.location, EntityType.ARROW) as Arrow
                val arrowUnitVector = someTarget.location.subtract(someArrow.location).toVector().normalize()
                someArrow.velocity = arrowUnitVector.clone().multiply(3.14)
                it.eyeLocation.direction = arrowUnitVector

                // Sounds and Effects
                it.playSound(it, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
                it.playSound(it, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
                it.playSound(it, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
                it.spawnParticle(Particle.DAMAGE_INDICATOR, it.location, 43, 1.5, 0.5, 1.5)
                it.spawnParticle(Particle.PORTAL, it.location, 42, 2.5, 0.5, 2.5)
                it.spawnParticle(Particle.END_ROD, it.location, 35, 2.0, 1.0, 2.0)
                it.spawnParticle(Particle.SPELL_WITCH, it.location, 25, 1.0, 1.0, 1.0)
                it.swingMainHand()
                spawnDummy()
            }
        }
        // Hijack
        val hijackTask = AmbassadorHijackTasks(bossEntity!!)
        hijackTask.runTaskTimer(Odyssey.instance, 0, 10)

    }

    // Pull player and do damage
    internal fun voidPullBackAttack(targetPlayer: Player) {
        // Teleport and damage
        with(targetPlayer) {
            teleport(bossEntity!!.location)
            val voidPullEffects = listOf(
                PotionEffect(PotionEffectType.LEVITATION, 20 * 10, 1),
                PotionEffect(PotionEffectType.SLOW, 20 * 10, 2))
            addPotionEffects(voidPullEffects)
            damage(7.5)

            // Sounds and Effects
            playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
            playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
            playSound(this, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
            spawnParticle(Particle.DAMAGE_INDICATOR, this.location, 43, 1.5, 0.5, 1.5)
            spawnParticle(Particle.PORTAL, this.location, 42, 2.5, 0.5, 2.5)
            spawnParticle(Particle.END_ROD, this.location, 35, 2.0, 1.0, 2.0)
            spawnParticle(Particle.SPELL_WITCH, this.location, 25, 1.0, 1.0, 1.0)
        }
    }


    internal fun attackPatterns() {
        val nearbyPlayers = bossEntity!!.location.getNearbyPlayers(28.0)
        when((1..10).random()) {
            in 1..3 -> {
                if (nearbyPlayers.isNotEmpty()) { skyBombardAttack(nearbyPlayers.random().location) }
                else { skyBombardAttack(bossEntity!!.location.clone().add((-9..9).random().toDouble(), 0.0, (-9..9).random().toDouble())) }
                nearbyPlayers.forEach { it.sendMessage(ambassadorPrefix.append(Component.text("Take a gift from the heavens!", TextColor.color(255, 255, 255)))) }
            }
            in 4..5 -> {
                if (nearbyPlayers.isNotEmpty()) { gravityLaunchAttack(nearbyPlayers.random().location) }
                else { gravityLaunchAttack(bossEntity!!.location.clone().add((-9..9).random().toDouble(), 0.0, (-9..9).random().toDouble())) }
                nearbyPlayers.forEach { it.sendMessage(ambassadorPrefix.append(Component.text("You can not fathom this...", TextColor.color(255, 255, 255)))) }
            }
            in 6..7 -> {
                if (nearbyPlayers.isNotEmpty()) {
                    hijackAttack(nearbyPlayers.random())
                }
                nearbyPlayers.forEach { it.sendMessage(ambassadorPrefix.append(Component.text("It appears your friends are actually foes...", TextColor.color(255, 255, 255)))) }
            }
            in 8..10 -> {
                if (nearbyPlayers.isNotEmpty()) {
                    nearbyPlayers.forEach { fallingSingularityAttack(it.location.clone().add(0.0, 15.0, 0.0))  }
                }
                nearbyPlayers.forEach { it.sendMessage(ambassadorPrefix.append(Component.text("The points is that its super massive...", TextColor.color(255, 255, 255)))) }
            }
        }
    }


    // Attack damager
    private fun defensiveAttack(someDamager: Entity) {
        // Quotes
        val timeElapsed: Long = System.currentTimeMillis() - takeDamageCooldown
        if (timeElapsed >= 1000 * 10) {
            val nearbyPlayers = bossEntity!!.getNearbyEntities(32.0, 32.0, 32.0).filterIsInstance<Player>()
            nearbyPlayers.forEach { it.sendMessage(ambassadorPrefix.append(damagedMessages[(0..4).random()])) }
        }
    }

    // Check damage source and current stats
    internal fun damageHandler(someDamager: Entity, someDamage: Double) {
        // Check if Patience
        var messageQuote: TextComponent
        val nearbyPlayers = bossEntity!!.getNearbyEntities(32.0, 32.0, 32.0).filterIsInstance<Player>()
        if (!angered) {
            if (someDamager is Player) {
                // Add likeness and bad first contact
                if (!playerLikeness.containsKey(someDamager.uniqueId)) {
                    playerLikeness[someDamager.uniqueId] = 0.0
                    messageQuote = ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[5])
                } else {
                    // Check likability
                    messageQuote = if (playerLikeness[someDamager.uniqueId]!! >= 65.0) {
                        ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[4])
                    } else if (playerLikeness[someDamager.uniqueId]!! >= 25.0) {
                        ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[3])
                    } else {
                        ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[1])
                    }
                    playerLikeness[someDamager.uniqueId] = playerLikeness[someDamager.uniqueId]!! - someDamage
                }
                // Pre-activation messages
                nearbyPlayers.forEach { it.sendMessage(messageQuote) }

                // Change mood stats
                appeasement -= (someDamage + 2.0)
                patience -= someDamage
                // Check if critical activation, or low patience
                messageQuote = if (patience <= 0) {
                    ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[6])
                } else if (appeasement <= -5) {
                    ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[2])
                } else {
                    ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[0])
                }
            }
            else {
                // Change mood stats
                appeasement -= (someDamage + 2.0)
                patience -= someDamage
                messageQuote = if (patience <= 0) {
                    ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[7])
                } else {
                    ambassadorPrefix.append(someDamager.name().color(TextColor.color(255, 255, 255))).append(patienceMessages[8])
                }
            }
            // Damage Message
            nearbyPlayers.forEach { it.sendMessage(messageQuote) }
            if (patience <= 0) {
                angered = true
                activateBoss()
            }
        }
        else {
            defensiveAttack(someDamager)
        }
    }

    // Appeasement Mechanic
    internal fun appeasementCheck(somePlayer: Player, someItem: Item) {
        // Check if players in gift cooldown map
        if (!playersGiftCooldown.containsKey(somePlayer.uniqueId)) {
            playersGiftCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
            calculateGiftTable(somePlayer, someItem)
        }
        else {
            // Gift Cooldown
            val timeElapsed: Long = System.currentTimeMillis() - playersGiftCooldown[somePlayer.uniqueId]!!
            if (timeElapsed >= 1000 * 5) {
                playersGiftCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
                calculateGiftTable(somePlayer, someItem)
            }
        }
    }

    // random delay 2-5
    private fun calculateGiftTable(givingPlayer: Player, giftedItem: Item) {
        val giftedMaterial: Material = giftedItem.itemStack.type
        // Check if item in table
        var giftAccepted = true

        // Check player likeness
        if (!playerLikeness.containsKey(givingPlayer.uniqueId)) {
            playerLikeness[givingPlayer.uniqueId] = 0.0
        }

        // Check if likeness maxed
        if (playerLikeness[givingPlayer.uniqueId]!! >= 100) {
            givingPlayer.sendMessage(ambassadorPrefix.append(givingPlayer.name()).append(Component.text(", I am sorry. I can not receive any more pleasantries from you.", TextColor.color(255, 255, 255))))
            playerLikeness[givingPlayer.uniqueId]!! + 5
            return
        }


        // Check if special item make (MAKE SEPARATE FLAGS LATER)
        if ((giftedItem.itemStack.itemMeta.hasEnchants() && giftedMaterial != Material.ENCHANTED_BOOK) ) {
            givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not accept this.")
            appeasement -= 1
            return
        }

        var giftLikeness = 0
        val likenessReward = (playerLikeness[givingPlayer.uniqueId]!! / 25).toInt() + 1
        // Checks if gift in when, and deletes if accepted
        val someGift = itemLootTable.random()
        /*
        when (giftedMaterial) {
            Material.NETHER_STAR -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}An unborn Unit-${ChatColor.MAGIC}092412X.${ChatColor.RESET}. I shall start its sentience activation cycle for you...")
                givingPlayer.inventory.addItem(Miscellaneous.DORMANT_SENTIENT_STAR.createItemStack(1))
                val gravityBook = Runic.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, (1..3).random())
                givingPlayer.inventory.addItem(gravityBook)
            }
            Material.NETHERITE_INGOT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 6
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here take this special Unit-${ChatColor.MAGIC}092412X.")
                    givingPlayer.inventory.addItem(Miscellaneous.DORMANT_SENTIENT_STAR.createItemStack(1))
                }
                else {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here are some quantum-entangled vacuums repurposed as storage " +
                            "that might help you as well as some gifts")
                    givingPlayer.inventory.addItem(Miscellaneous.HAWKING_ENTANGLED_UNIT.createItemStack(likenessReward + 2))
                    givingPlayer.inventory.addItem(Miscellaneous.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(likenessReward * 4))
                    givingPlayer.inventory.addItem(Miscellaneous.REFINED_IOJOVIAN_EMERALDS.createItemStack(likenessReward * 4))
                }
            }
            Material.DIAMOND, Material.EMERALD, Material.AMETHYST_SHARD -> {
                giftLikeness -= 1
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}These gems are not that refined according to standards. But... here is something for such work.")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
            }
            Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT -> {
                giftLikeness += 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The tribute of raw materials express the loyalty and growth of this world...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
            }
            Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK -> {
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                giftLikeness += 4
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This much raw materials should garner good attention...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards mechanization...")
                    givingPlayer.inventory.addItem(Miscellaneous.KUGELBLITZ_CONTAINMENT_UNIT.createItemStack(1))
                }
            }
            Material.ACACIA_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG,
            Material.ACACIA_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.DARK_OAK_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD -> {
                giftLikeness += 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Wood is always a commodity that should be accepted!")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards industrialization...")
                    givingPlayer.inventory.addItem(Miscellaneous.POLYMORPHIC_GLUE.createItemStack(6))
                }
            }
            Material.ROTTEN_FLESH -> {
                giftLikeness -= 2
                patience -= 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}You think of me so low and stupid. I believe this world does not deserve anything close to respect...")
            }
            Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT -> {
                giftLikeness -= 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not accept something that crude...")
            }
            Material.SCULK, Material.SCULK_CATALYST, Material.SCULK_SENSOR, Material.SCULK_SHRIEKER, Material.SCULK_VEIN -> {
                giftLikeness -= 45
                patience -= 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The Sculk has corrupted this forsaken place... Its eradication is inevitable")
            }
            Material.ENCHANTED_BOOK ->  {
                giftLikeness += 4
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Enchanted Literature! Something quite interesting this test-site was made for...and a Good Read")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                if (appeasement < 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am pleased so far.. here have some extra gifts...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                }
                else if (appeasement >= 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Tokens for your prosperity and of the upcoming ${ChatColor.MAGIC}World Integration Procedure")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
            }
            Material.BOOK, Material.PAINTING -> {
                giftLikeness += 4
                if (appeasement < 55) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hopefully this culture is not so dull as other test-s... Never mind...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                }
                else {
                    val randomEnchantments = listOf(OdysseyEnchantments.SPOREFUL, OdysseyEnchantments.SQUIDIFY, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BUZZY_BEES, OdysseyEnchantments.FRUITFUL_FARE, OdysseyEnchantments.POTION_BARRIER)
                    val randomBook = Runic.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}So far I can say I am enjoying this...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                    givingPlayer.inventory.addItem(randomBook)
                }
            }
            Material.ELYTRA -> {
                giftLikeness += 3
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Sub-Dimensional Flying. A step towards industrialization I see...And excellent presents!")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
            }
            Material.ENCHANTED_GOLDEN_APPLE -> {
                giftLikeness += 50
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}How did you get this?... I did not hear that Vail planted Aether roots from Lupercal...")
                givingPlayer.sendMessage("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Do not alert the others... Here take this. Keep it safe, it will help you soon to come...")
                givingPlayer.inventory.addItem(Miscellaneous.BABEL_ANNULUS_SCHEMATICS.createItemStack(1))
            }
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP,
            Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER,
            Material.LILAC, Material.ROSE_BUSH, Material.PEONY -> {
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                giftLikeness += 2
                if (appeasement < 45) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}For an uncivilized world, flowers still bloom... Hopefully you do as well...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                }
                else if (appeasement >= 45) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This should grow your knowledge and strength in time...")
                    givingPlayer.inventory.addItem(Miscellaneous.IDESCINE_SAPLING.createItemStack(1))
                }
            }
            Material.GOAT_HORN -> {
                giftLikeness += 3
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Musical anomalies... Interesting...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                //Give sound based enchant if likeness high
            }
            Material.MUSIC_DISC_WARD, Material.MUSIC_DISC_11, Material.MUSIC_DISC_5, Material.MUSIC_DISC_13, Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_BLOCKS,
            Material.MUSIC_DISC_PIGSTEP, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_CAT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Primitive music... Something I find amusing...")
                val randomEnchantments = listOf(OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BANE_OF_THE_SWINE, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BUZZY_BEES,
                    OdysseyEnchantments.GUARDING_STRIKE, OdysseyEnchantments.FRUITFUL_FARE, OdysseyEnchantments.DOUSE, OdysseyEnchantments.POTION_BARRIER)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 5))
                if (appeasement >= 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}These are some relics I have collected from previous visits...")
                    val randomBook = Runic.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                    givingPlayer.inventory.addItem(randomBook)
                }
            }
            // Later add books
            Material.CONDUIT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The ocean is essential to this world's stability! Though, power does not give you an excuse to butcher.")
                giftLikeness += 45
                val seaBook = Runic.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, 4)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement >= 45) {
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
                givingPlayer.inventory.addItem(seaBook)
            }
            Material.TRIDENT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Do no be afraid of the sea or its monsters...")
                giftLikeness += 30
                val seaBook = Runic.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, (1..3).random())
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(seaBook)

            }
            Material.BEACON -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Follow the path of doubt...and not blinding light")
                val randomEnchantments = listOf(OdysseyEnchantments.VOID_JUMP, OdysseyEnchantments.ECHO, OdysseyEnchantments.VOID_STRIKE, OdysseyEnchantments.SOUL_REND, OdysseyEnchantments.BACKSTABBER)
                val randomBook = Runic.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement >= 45) {
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
                givingPlayer.inventory.addItem(randomBook)
            }
            Material.CHEST, Material.BARREL -> {
                giftLikeness += 1
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Nice and quaint boxes...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                if (appeasement >= 65) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This is what a present look like however...")
                    givingPlayer.inventory.addItem(Miscellaneous.KUGELBLITZ_CONTAINMENT_UNIT.createItemStack(1))
                }
            }
            else -> {
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Well...Let me appraise this for you...")
                giftAccepted = false

            }
        }


         */
        // DO GILDED ENCHANTMENTS
        if (giftAccepted) {
            giftedItem.remove()
            // Calculation for gift likeness
            appeasement += giftLikeness
            val newLikeness = playerLikeness[givingPlayer.uniqueId]!! + giftLikeness
            playerLikeness[givingPlayer.uniqueId] = newLikeness
            if (playerLikeness[givingPlayer.uniqueId]!! > 50.0) {
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Your gifts are enjoyable")
            }
        }
    }
}

