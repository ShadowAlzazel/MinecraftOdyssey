package me.shadowalzazel.mcodyssey.bosses.theAmbassador

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.constants.ModifiersUUIDs
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.*
import net.kyori.adventure.text.Component
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
    var despawnTimer: Long = 1
    // Trading Mechanic
    var patience: Double = 55.0
    var appeasement: Double = 0.0
    private var playerLikeness = mutableMapOf<UUID, Double>()
    private var playersGiftCooldown = mutableMapOf<UUID, Long>()
    // Gifts
    private val itemLootTable = listOf(OdysseyItems.REFINED_IOJOVIAN_EMERALDS, OdysseyItems.NEUTRONIUM_BARK_SCRAPS, OdysseyItems.PURE_ALLOY_GOLD, OdysseyItems.PAPERS_OF_ARCUS, OdysseyItems.POLYMORPHIC_GLUE,
        OdysseyItems.PURE_ALLOY_COPPER, OdysseyItems.GALVANIZED_STEEL, OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS)
    // Combat Mechanic
    private var takeDamageCooldown: Long = 0L
    var specialAttacksCooldown = mutableMapOf<String, Long>()
    // Quotes MOVE HERE
    private val randomMessageList = listOf("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Such Folly, Such Weakness!", "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not tolerate such incompetence",
        "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The damage you think your inflicting is minimal!", "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am just disappointed...",
        "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Just stop... Your attacks are worthless!")


    // Spawn entity
    private fun spawnBossEntity(spawningLocation: Location): Illusioner {
        val newAmbassadorEntity: Illusioner = (spawningLocation.world.spawnEntity(spawningLocation, EntityType.ILLUSIONER) as Illusioner).apply {
            // Potion Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 1),
                PotionEffect(PotionEffectType.GLOWING, 20 * 300, 1),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 3),
                PotionEffect(PotionEffectType.SPEED, 99999, 2)))

            // Change Default Behaviour
            customName(Component.text("The Ambassador", TextColor.color(255, 85, 255)))
            isCustomNameVisible = true
            removeWhenFarAway = false
            isCanJoinRaid = false
            isAware = false
            canPickupItems = true

            // Health
            val mobHealth = AttributeModifier(ModifiersUUIDs.ODYSSEY_BOSS_HEALTH_UUID, "odyssey_mob_health", 930.0, AttributeModifier.Operation.ADD_NUMBER)
            val healthAttribute = getAttribute(Attribute.GENERIC_MAX_HEALTH)
            healthAttribute!!.addModifier(mobHealth)
            health = 950.0

            // Add Kinetic Blaster
            clearActiveItem()
            equipment.setItemInMainHand(OdysseyWeapons.KINETIC_BLASTER.createItemStack(1))
        }
        return newAmbassadorEntity
    }

    // Create Ambassador Boss in instance
    fun createBoss(odysseyWorld: World) {
        despawnTimer = System.currentTimeMillis()
        //
        val worldPlayers = odysseyWorld.players
        val spawningPlayer = worldPlayers.random()
        for (somePlayer in worldPlayers) {
            somePlayer.sendMessage("${ChatColor.GOLD}${ChatColor.MAGIC}[Vail]${ChatColor.RESET}${ChatColor.YELLOW} My Ambassador has arrived!")
            somePlayer.playSound(somePlayer.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.5F, 0.9F)
            somePlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am descending upon ${spawningPlayer.name}'s land..")
        }
        spawningPlayer.sendMessage("${ChatColor.RESET}${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Be prepared for my arrival... ${spawningPlayer.name}")
        val spawningLocation = spawningPlayer.location
        spawningLocation.x += (-28..28).random()
        spawningLocation.z += (-28..28).random()
        spawningLocation.y = 300.0
        println("The Ambassador has arrived at ${spawningLocation.x}, ${spawningLocation.z}")


        val ambassadorEntity: Illusioner = spawnBossEntity(spawningLocation)
        // Change boss class
        bossEntity = ambassadorEntity
    }


    // Defeat Boss
    fun defeatedBoss(ambassadorEntity: Illusioner, vanquisher: Player) {
        if (ambassadorEntity == bossEntity) {
            // Spawn loot near vanquisher
            vanquisher.world.dropItem(vanquisher.location, (OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, 1)))
            vanquisher.giveExpLevels(10)
            // Nearby player get xp and text
            ambassadorEntity.world.getNearbyPlayers(ambassadorEntity.location, 64.0).forEach {
                it.sendMessage(Component.text("The Ambassador has departed ungracefully!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
                it.sendMessage(Component.text("With", TextColor.color(255, 255, 85), TextDecoration.ITALIC)
                    .append(Component.text(vanquisher.name).color(TextColor.color(255, 170, 0)))
                    .append(Component.text("taking the final blow!").color(TextColor.color(255, 255, 85))).decorate(TextDecoration.ITALIC))
                it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F)
                it.giveExp(3550)
            }
        }
    }

    // Despawn boss
    fun departBoss() {
        bossEntity!!.world.players.forEach {
            it.sendMessage(Component.text("The Ambassador has left...!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
            it.playSound(it, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F)
        }
    }

    // activate boss
    private fun activateBoss(someTarget: Entity) {
        bossEntity!!.world.getNearbyPlayers(bossEntity!!.location, 64.0).forEach {
            //it.sendMessage(Component.text("The Ambassador has left...!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F)
        }
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
            equipment.setItemInMainHand(OdysseyWeapons.KINETIC_BLASTER.createItemStack(1))
        }
    }

    // Create a new firework entity
    private fun createSuperFirework(targetLocation: Location): Firework {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA)
        val superFirework: Firework = (bossEntity!!.world.spawnEntity(targetLocation, EntityType.FIREWORK) as Firework).apply {
            fireworkMeta.also {
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
            velocity = targetLocation.clone().add(0.0, -1.618, 0.0).subtract(targetLocation).toVector()
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

    }



    // Spawn a vortex that launches players
    private fun gravityLaunchAttack(targetLocation: Location) {
        val gravityWaveEffects = listOf(
            PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10, 0),
            PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 0))

        targetLocation.getNearbyPlayers(7.5).forEach {
            it.addPotionEffects(gravityWaveEffects)
            it.damage(15.5, bossEntity!!)
            it.velocity.setX(0.0).setY(1.0).setZ(0.0).multiply(1.0)

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
        someTarget.teleport(bossEntity!!.location)
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
            }
        }
        // Hijack
        someTarget.addPassenger(bossEntity!!)
        val hijackTask = AmbassadorHijackTasks(bossEntity!!)
        hijackTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10)

    }


    // Pull player and do damage
    fun voidPullBackAttack(targetPlayer: Player) {
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


    private fun attackPatterns(ambassadorEntity: Illusioner) {

    }


    // Attack damager
    private fun attackToDamager(someDamager: Entity) {
        // Quotes
        val timeElapsed: Long = System.currentTimeMillis() - takeDamageCooldown
        if (timeElapsed >= 6000) {
            val randomAttackQuote = randomMessageList.random()
            takeDamageCooldown = System.currentTimeMillis()
            for (somePlayer in bossEntity!!.world.getNearbyPlayers(bossEntity!!.location, 17.5)) {
                somePlayer.sendMessage(randomAttackQuote)
            }
            /*
            if (someDamager is Player) {
                when((0..6).random()) {
                    3, 4 -> {
                        gravityWaveAttack(someDamager)
                    }
                    0, 1, 2 -> {
                        skyBombardAttack(someDamager)
                    }
                    5, 6 -> {
                        hijackAttack(bossEntity!!)
                    }

                }
            }
            else {
                when((0..2).random()) {
                    0, 1 -> {
                        hijackAttack(bossEntity!!)
                    }
                    2 -> {
                        gravityWaveAttack(someDamager)
                    }
                }
            }

             */
        }
    }


    // Check damage source and current stats
    fun detectDamage(someDamager: Entity, someDamage: Double) {
        // Quotes MOVE LATER
        val criticalMoodQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${someDamager.name} wants to endanger you all!"
        val patiencePatienceQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${someDamager.name} is testing my patience!"
        val disrespectQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${someDamager.name} has shown me extreme disrespect!"
        val disappointedQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${someDamager.name}... I expected more from you..."
        val dislikeQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${someDamager.name}... Does your honor not mean anything?!"
        val firstBadContactQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${someDamager.name}, that is not an appropriate way to introduce yourself, though what is expected from such a lowlife..."
        val activationQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Due to ${someDamager.name}'s insolence, You all shall be taught some respect!"
        val whoActivationQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Time to teach these lowlifes basic manners..."
        val patienceBadMoodQuote = "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Who is doing that?!"

        // Check if Patience
        if (patience > 0) {
            var messageQuote: String?

            // Messages if player damage
            if (someDamager is Player) {
                println("${someDamager.name} hit player")
                // Add likeness and bad first contact
                if (!playerLikeness.containsKey(someDamager.uniqueId)) {
                    playerLikeness[someDamager.uniqueId] = 0.0
                    messageQuote = firstBadContactQuote
                }
                // Check if likability is high
                else {
                    messageQuote = if (playerLikeness[someDamager.uniqueId]!! >= 65.0) {
                        disappointedQuote
                    } else if (playerLikeness[someDamager.uniqueId]!! >= 25.0) {
                        dislikeQuote
                    } else {
                        patiencePatienceQuote
                    }
                    playerLikeness[someDamager.uniqueId] = playerLikeness[someDamager.uniqueId]!! - someDamage
                }
                // Change his mood
                appeasement -= (someDamage + 2.0)
                patience -= someDamage
                // Check if critical activation
                if (patience <= 0) {
                    // Check if activated
                    messageQuote = activationQuote
                    activateBoss(someDamager)
                }
                else if (patience <= 10) {
                    // Check if near criticality
                    messageQuote = criticalMoodQuote
                }
                else if (appeasement <= -5) {
                    // Check if low appeasement
                    messageQuote = disrespectQuote
                }
            }
            // Entity damages
            else {
                messageQuote = patienceBadMoodQuote
                // Change his mood
                appeasement -= (someDamage + 2.0)
                patience -= someDamage

                // Check if critical activation
                if (patience <= 0) {
                    messageQuote = whoActivationQuote
                    activateBoss(someDamager)
                }
            }
            // Send Messages to all
            for (somePlayer in someDamager.world.players) {
                somePlayer.sendMessage(messageQuote)
            }
        }
        else {
            attackToDamager(someDamager)
        }
    }

    // Appeasement Mechanic
    fun appeasementCheck(somePlayer: Player, someItem: Item) {
        // Check if players in gift cooldown map
        if (!playersGiftCooldown.containsKey(somePlayer.uniqueId)) {
            playersGiftCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
            calculateGiftTable(somePlayer, someItem)
        }
        else {
            val timeElapsed: Long = System.currentTimeMillis() - playersGiftCooldown[somePlayer.uniqueId]!!
            // Gift Cooldown
            if (timeElapsed >= 5000) {
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
            givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am sorry. I can not receive any more pleasantries from you.")
            playerLikeness[givingPlayer.uniqueId]!! + 1
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
        when (giftedMaterial) {
            Material.NETHER_STAR -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}An unborn Unit-${ChatColor.MAGIC}092412X.${ChatColor.RESET}. I shall start its sentience activation cycle for you...")
                givingPlayer.inventory.addItem(OdysseyItems.DORMANT_SENTIENT_STAR.createItemStack(1))
                val gravityBook = OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, (1..3).random())
                givingPlayer.inventory.addItem(gravityBook)
            }
            Material.NETHERITE_INGOT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 6
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here take this special Unit-${ChatColor.MAGIC}092412X.")
                    givingPlayer.inventory.addItem(OdysseyItems.DORMANT_SENTIENT_STAR.createItemStack(1))
                }
                else {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here are some quantum-entangled vacuums repurposed as storage " +
                            "that might help you as well as some gifts")
                    givingPlayer.inventory.addItem(OdysseyItems.HAWKING_ENTANGLED_UNIT.createItemStack(likenessReward + 2))
                    givingPlayer.inventory.addItem(OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(likenessReward * 4))
                    givingPlayer.inventory.addItem(OdysseyItems.REFINED_IOJOVIAN_EMERALDS.createItemStack(likenessReward * 4))
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
                    givingPlayer.inventory.addItem(OdysseyItems.KUGELBLITZ_CONTAINMENT_UNIT.createItemStack(1))
                }
            }
            Material.ACACIA_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG,
            Material.ACACIA_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.DARK_OAK_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD -> {
                giftLikeness += 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Wood is always a commodity that should be accepted!")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards industrialization...")
                    givingPlayer.inventory.addItem(OdysseyItems.POLYMORPHIC_GLUE.createItemStack(6))
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
                    val randomBook = OdysseyBooks.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
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
                givingPlayer.inventory.addItem(OdysseyItems.BABEL_ANNULUS_SCHEMATICS.createItemStack(1))
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
                    givingPlayer.inventory.addItem(OdysseyItems.IDESCINE_SAPLING.createItemStack(1))
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
                    val randomBook = OdysseyBooks.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                    givingPlayer.inventory.addItem(randomBook)
                }
            }
            // Later add books
            Material.CONDUIT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The ocean is essential to this world's stability! Though, power does not give you an excuse to butcher.")
                giftLikeness += 45
                val seaBook = OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, 4)
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
                val seaBook = OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, (1..3).random())
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(seaBook)

            }
            Material.BEACON -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Follow the path of doubt...and not blinding light")
                val randomEnchantments = listOf(OdysseyEnchantments.WARP_JUMP, OdysseyEnchantments.ECHO, OdysseyEnchantments.VOID_STRIKE, OdysseyEnchantments.SOUL_REND, OdysseyEnchantments.BACKSTABBER)
                val randomBook = OdysseyBooks.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
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
                    givingPlayer.inventory.addItem(OdysseyItems.KUGELBLITZ_CONTAINMENT_UNIT.createItemStack(1))
                }
            }
            else -> {
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Well...Let me appraise this for you...")
                giftAccepted = false

            }
        }
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

