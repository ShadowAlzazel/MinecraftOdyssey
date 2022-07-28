package me.shadowalzazel.mcodyssey.bosses.theAmbassador

import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.odysseyUtility.*
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Illusioner
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
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
        OdysseyItems.PURE_ALLOY_COPPER, OdysseyItems.GALVANIZED_STEEL, OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS, OdysseyItems.HAWKING_ENTANGLED_UNIT)
    // Combat Mechanic
    private var takeDamageCooldown: Long = 0L
    var specialAttacksCooldown = mutableMapOf<String, Long>()
    // Quotes MOVE HERE
    private val randomMessageList = listOf("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Such Folly, Such Weakness!", "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not tolerate such incompetence",
        "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The damage you think your inflicting is minimal!", "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am just disappointed...",
        "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Just stop... Your attacks are worthless!")


    // Create Kinetic Weapon
    private fun createAmbassadorWeapon(): ItemStack {
        val kineticBlaster = ItemStack(Material.BOW, 1)

        // Add lore and name
        val kineticBlasterMeta: ItemMeta = kineticBlaster.itemMeta
        kineticBlasterMeta.setDisplayName("${ChatColor.LIGHT_PURPLE}Kinetic Blaster")
        val kineticBlasterLore = listOf("A weapon commissioned to shoot kinetic darts", "Designed by the current natives")
        kineticBlasterMeta.lore = kineticBlasterLore

        // Add Enchantments
        kineticBlasterMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true)
        kineticBlasterMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        kineticBlasterMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true)
        kineticBlasterMeta.addEnchant(Enchantment.DURABILITY, 3, true)

        // Create weapon
        kineticBlaster.itemMeta = kineticBlasterMeta
        return kineticBlaster
    }


    // Spawn boss near players
    private fun spawnBoss(odysseyWorld: World): Illusioner {
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
        return odysseyWorld.spawnEntity(spawningLocation, EntityType.ILLUSIONER) as Illusioner
    }


    // Create Ambassador Boss Entity
    fun createBoss(odysseyWorld: World) {
        val ambassadorEntity: Illusioner = spawnBoss(odysseyWorld)
        // 1200 tks = 60 sec
        // Add Potion Effects
        val voidFall = PotionEffect(PotionEffectType.SLOW_FALLING, 2400, 1)
        val voidGlow = PotionEffect(PotionEffectType.GLOWING, 2400, 1)
        val voidSolar = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 3)
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 235)
        val ankiRainEffects = listOf(enhancedHealth, voidFall, voidSolar, voidGlow)
        ambassadorEntity.addPotionEffects(ankiRainEffects)

        despawnTimer = System.currentTimeMillis()
        // Change Default Behaviour
        ambassadorEntity.customName = "${ChatColor.LIGHT_PURPLE}$bossName"
        ambassadorEntity.isCustomNameVisible = true
        ambassadorEntity.removeWhenFarAway = false
        ambassadorEntity.isCanJoinRaid = false
        ambassadorEntity.isAware = false
        ambassadorEntity.canPickupItems = true
        ambassadorEntity.health = 950.0

        // Add Item
        val ambassadorWeapon: ItemStack = createAmbassadorWeapon()
        ambassadorEntity.clearActiveItem()
        ambassadorEntity.equipment.setItemInMainHand(ambassadorWeapon)

        // Trails
        /*
        GlobalScope.launch {
            var looper = 1
            var someTimer = System.currentTimeMillis()
            while (looper < 30) {
                var timeElapsed = System.currentTimeMillis() - someTimer
                if (timeElapsed >= 30) {
                    someTimer = System.currentTimeMillis()
                    odysseyWorld.spawnEntity(bossEntity!!.location, EntityType.FIREWORK) as Firework
                    looper += 1
                }
            }
        }
        */
        // Change boss class
        bossEntity = ambassadorEntity
    }


    // Defeat Boss
    fun defeatedBoss(ambassadorEntity: Illusioner, vanquisher: Player) {
        val nearbyPlayers = ambassadorEntity.world.getNearbyPlayers(ambassadorEntity.location, 64.0)
        for (somePlayer in ambassadorEntity.world.players) {
            somePlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}The Ambassador has departed ungracefully!")
            somePlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}With ${ChatColor.GOLD}${vanquisher.name} ${ChatColor.RESET}${ChatColor.YELLOW}${ChatColor.ITALIC}taking the final blow!")
            somePlayer.playSound(somePlayer, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F)
            if (somePlayer in nearbyPlayers) {
                somePlayer.giveExp(2500)
            }
        }
    }

    fun departBoss() {
        for (somePlayer in bossEntity!!.world.players) {
            somePlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}The Ambassador has left...!")
            somePlayer.playSound(somePlayer, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F)
        }
    }


    // activate boss
    private fun activateBoss(someTarget: Entity) {
        bossEntity!!.isAware = true
        for (somePlayer in someTarget.world.players) {
            somePlayer.playSound(somePlayer, Sound.ENTITY_WITHER_SPAWN, 1.0F, 0.8F)
        }

    }


    // Create a new firework entity
    private fun createSuperFirework(targetPlayer: Player): Firework {
        // MAYBE SHOOT FROM SPAWNED AMBASSADOR
        val superFirework: Firework = targetPlayer.world.spawnEntity(bossEntity!!.location, EntityType.FIREWORK) as Firework
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA)
        val superFireworkMeta = superFirework.fireworkMeta

        // Add Effects and Power
        superFireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(randomColors.random()).withFade(randomColors.random()).trail(true).flicker(true).build())
        superFireworkMeta.power = 120
        // FIX VELOCITY
        superFirework.velocity = targetPlayer.location.add(0.0, 5.0, 0.0).subtract(targetPlayer.location).toVector().multiply(1)
        superFirework.ticksToDetonate = (1..3).random()
        superFirework.fireworkMeta = superFireworkMeta
        return superFirework
    }


    // Do Firework damage
    private fun doFireworkDamage(somePlayers: MutableCollection<Player>) {
        // Create sounds, particles, and do damage to nearby players
        for (somePlayer in somePlayers) {
            // IDK TO SPAWN AT PLAYER WITH 0 VEL OR WHAT
            createSuperFirework(somePlayer)
            somePlayer.damage(24.5)
            somePlayer.playSound(somePlayer.location, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
            somePlayer.playSound(somePlayer.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.5F, 0.8F)
            somePlayer.playSound(somePlayer.location, Sound.ENTITY_IRON_GOLEM_DEATH, 2.0F, 0.8F)
            somePlayer.world.spawnParticle(Particle.FLASH, somePlayer.location, 5, 1.0, 1.0, 1.0)
            somePlayer.world.spawnParticle(Particle.LAVA, somePlayer.location, 35, 1.5, 1.0, 1.5)
        }
    }


    // Firework Attack Call
    private fun fireworkAttack(targetPlayer: Player) {
        // Find players for splash
        val playersNearTarget = targetPlayer.world.getNearbyPlayers(targetPlayer.location, 4.5)
        doFireworkDamage(playersNearTarget)
    }


    // Spawn a dummy clone
    private fun spawnDummy() {
        // Find Location
        val currentLocation = bossEntity!!.location
        val randomXZLocation = (-5..5).random()
        currentLocation.x += randomXZLocation
        currentLocation.z += randomXZLocation
        currentLocation.y += 3

        // Create Dummy
        val ambassadorDummy = bossEntity!!.world.spawnEntity(currentLocation, EntityType.ILLUSIONER) as Illusioner
        ambassadorDummy.customName = "${ChatColor.LIGHT_PURPLE}$bossName"
        ambassadorDummy.isCustomNameVisible = true
        ambassadorDummy.isCanJoinRaid = false
        ambassadorDummy.isAware = true
        ambassadorDummy.lootTable = null
        ambassadorDummy.health = 25.0

        // Add Item
        val ambassadorWeapon: ItemStack = createAmbassadorWeapon()
        ambassadorDummy.clearActiveItem()
        ambassadorDummy.equipment.setItemInMainHand(ambassadorWeapon)

    }


    // Do Gravity Wave Damage
    private fun doGravityWaveDamage(somePlayers: MutableCollection<Player>, someWorld: World) {
        val gravityDistort = PotionEffect(PotionEffectType.SLOW_FALLING, 200, 0)
        val gravityShatter = PotionEffect(PotionEffectType.WEAKNESS, 100, 0)
        val gravityWaveEffects = listOf(gravityDistort, gravityShatter)

        for (somePlayer in somePlayers) {
            // Do damage, apply effects and teleport
            somePlayer.addPotionEffects(gravityWaveEffects)
            somePlayer.damage(18.5)
            val gravityAttract = somePlayer.location
            gravityAttract.add(0.0, 12.5, 0.0)
            //superFirework.velocity = targetPlayer.location.direction.subtract(bossEntity!!.location.direction)
            somePlayer.velocity = gravityAttract.subtract(somePlayer.location).toVector().multiply(0.2)

            // Apply sound and particles
            somePlayer.playSound(somePlayer, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.3F, 1.1F)
            somePlayer.playSound(somePlayer, Sound.ITEM_TRIDENT_THUNDER, 2.5F, 0.8F)
            somePlayer.playSound(somePlayer, Sound.BLOCK_ANVIL_LAND, 1.0F, 0.5F)
            somePlayer.playSound(somePlayer, Sound.BLOCK_ANVIL_BREAK, 1.0F, 0.8F)
            somePlayer.playSound(somePlayer, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0F, 1.0F)
            somePlayer.playSound(somePlayer, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.5F, 1.0F)
            someWorld.spawnParticle(Particle.DAMAGE_INDICATOR, somePlayer.location, 15, 1.5, 0.5, 1.5)
            someWorld.spawnParticle(Particle.CRIT, somePlayer.location, 15, 2.5, 0.5, 2.5)
            someWorld.spawnParticle(Particle.END_ROD, somePlayer.location, 15, 2.0, 1.0, 2.0)
            someWorld.spawnParticle(Particle.FLASH, somePlayer.location, 5, 1.0, 1.0, 1.0)
            someWorld.spawnParticle(Particle.EXPLOSION_NORMAL, somePlayer.location, 10, 2.0, 1.0, 2.0)
        }
    }


    // Gravity Wave Attack Call
    private fun gravityWaveAttack(targetPlayer: Entity) {
        val voidFall = PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1)
        // Find Players Near Attacker
        if (targetPlayer is Player) {
            val teleportHigh = targetPlayer.location
            teleportHigh.y += 14
            bossEntity!!.teleport(teleportHigh)
            bossEntity!!.addPotionEffect(voidFall)
        }
        val playersNearTarget = targetPlayer.world.getNearbyPlayers(targetPlayer.location, 9.5)
        doGravityWaveDamage(playersNearTarget, targetPlayer.world)
        // Spawn Dummies
        spawnDummy()
        spawnDummy()
    }

    // hijack
    private fun hijackAttack(ambassadorEntity: Illusioner) {
        var playersNearAmbassador = ambassadorEntity.world.getNearbyPlayers(ambassadorEntity.location, 24.5)
        val randomPlayer = playersNearAmbassador.random()

        if (playersNearAmbassador.size > 1) {
            playersNearAmbassador.remove(randomPlayer)
        }

        randomPlayer.teleport(ambassadorEntity.location)
        val hijackAttract = randomPlayer.location
        for (somePlayer in playersNearAmbassador) {
            // arrow
            val someArrow = ambassadorEntity.world.spawnEntity(hijackAttract.add(0.0, 2.5, 0.0), EntityType.ARROW)
            someArrow.velocity = somePlayer.location.subtract(someArrow.location).toVector().multiply(1.0)
            somePlayer.damage(11.0)
            somePlayer.attack(randomPlayer)

            //pull
            somePlayer.velocity = hijackAttract.subtract(somePlayer.location).toVector().multiply(0.20)

            //sounds and particles
            somePlayer.playSound(somePlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
            somePlayer.playSound(somePlayer, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
            somePlayer.playSound(somePlayer, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
            somePlayer.world.spawnParticle(Particle.DAMAGE_INDICATOR, somePlayer.location, 13, 1.5, 0.5, 1.5)
            somePlayer.world.spawnParticle(Particle.PORTAL, somePlayer.location, 32, 2.5, 0.5, 2.5)
            somePlayer.world.spawnParticle(Particle.END_ROD, somePlayer.location, 15, 2.0, 1.0, 2.0)
            somePlayer.spawnParticle(Particle.FLASH, somePlayer.location, 5, 1.0, 1.0, 1.0)

        }
        //val hijackedPlayer = playersNearAmbassador.random()
        //hijackedPlayer.setPassenger(ambassadorEntity)
    }


    // Pull player and do damage
    fun voidPullBackAttack(targetPlayer: Player) {
        val voidSlow = PotionEffect(PotionEffectType.SLOW, 200, 0)
        val voidRise = PotionEffect(PotionEffectType.LEVITATION, 200, 0)
        val voidPullEffects = listOf(voidRise, voidSlow)

        // Teleport and damage
        targetPlayer.teleport(bossEntity!!.location)
        targetPlayer.addPotionEffects(voidPullEffects)
        targetPlayer.damage(7.5)

        // Play sounds and particles
        targetPlayer.playSound(targetPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
        targetPlayer.playSound(targetPlayer, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
        targetPlayer.playSound(targetPlayer, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
        targetPlayer.world.spawnParticle(Particle.DAMAGE_INDICATOR, targetPlayer.location, 5, 1.5, 0.5, 1.5)
        targetPlayer.world.spawnParticle(Particle.VIBRATION, targetPlayer.location, 15, 2.5, 0.5, 2.5)
        targetPlayer.world.spawnParticle(Particle.END_ROD, targetPlayer.location, 15, 2.0, 1.0, 2.0)
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
            if (someDamager is Player) {
                when((0..6).random()) {
                    3, 4 -> {
                        gravityWaveAttack(someDamager)
                    }
                    0, 1, 2 -> {
                        fireworkAttack(someDamager)
                    }
                    5, 6 -> {
                        hijackAttack(bossEntity!!)
                    }

                }
            }
            else {
                hijackAttack(bossEntity!!)
                println("lsssss")
            }
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
            if (someDamager is Player) {
                attackToDamager(someDamager)
            }
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
                giftLikeness += 9
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}An unborn Unit-${ChatColor.MAGIC}092412X.${ChatColor.RESET}. I shall start its sentience activation cycle for you...")
                givingPlayer.inventory.addItem(OdysseyItems.ARTIFICIAL_STAR_UNIT.createItemStack(1))
            }
            Material.NETHERITE_INGOT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 6
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here take this special Unit-${ChatColor.MAGIC}092412X.")
                    givingPlayer.inventory.addItem(OdysseyItems.ARTIFICIAL_STAR_UNIT.createItemStack(1))
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
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 3))
            }
            Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT -> {
                giftLikeness += 1
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The tribute of raw materials express the loyalty and growth of this world...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
            }
            Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK -> {
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                giftLikeness += 4
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This much raw materials should garner good attention...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards mechanization...")
                    givingPlayer.inventory.addItem(OdysseyItems.KUGELBLITZ_CONTAINMENT_SILO.createItemStack(1))
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
                giftLikeness += 1
                if (appeasement < 45) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hopefully this culture is not so dull as other test-s... Never mind...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                }
                else {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}So far I can say I am enjoying this...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
            }
            Material.ENDER_CHEST -> {
                giftLikeness += 3
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Sub-Dimensional Storage. A step towards industrialization I see...And excellent presents!")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 3))
            }
            Material.ENCHANTED_GOLDEN_APPLE -> {
                giftLikeness += 50
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}How did you get this?... I did not hear that Vail planted Aether roots from Lupercal...")
                givingPlayer.sendMessage("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Do not alert the others... Here take this. Keep it safe, it will help you soon to come...")
                givingPlayer.inventory.addItem(OdysseyItems.RHO_ANNULUS_SCHEMATICS.createItemStack(1))
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
                val randomEnchantments = listOf(OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BANE_OF_THE_SWINE, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BUZZY_BEES, OdysseyEnchantments.GUARDING_STRIKE)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 5))
                if (appeasement >= 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}These are some relics I have collected from previous visits...")
                    val randomBook = OdysseyItems.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                    givingPlayer.inventory.addItem(randomBook)
                }
            }
            // Later add books
            Material.CONDUIT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The ocean is essential to this world's stability! Though, power does not give you an excuse to butcher.")
                giftLikeness += 55
                val seaBook = OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, 4)
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
                giftLikeness += 35
                val seaBook = OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, (1..3).random())
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(seaBook)

            }
            Material.BEACON -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 55
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Follow the path of doubt...and not blinding light")
                val randomEnchantments = listOf(OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BANE_OF_THE_SWINE, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BUZZY_BEES, OdysseyEnchantments.VOID_STRIKE)
                val randomBook = OdysseyItems.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
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
                    givingPlayer.inventory.addItem(OdysseyItems.KUGELBLITZ_CONTAINMENT_SILO.createItemStack(1))
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

