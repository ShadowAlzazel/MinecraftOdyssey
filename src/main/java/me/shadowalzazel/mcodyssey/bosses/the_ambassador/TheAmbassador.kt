package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.bosses.base.OdysseyBoss
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.common.items.custom.Miscellaneous
import me.shadowalzazel.mcodyssey.common.listeners.PetListener.addArmorAttribute
import me.shadowalzazel.mcodyssey.common.listeners.PetListener.addHealthAttribute
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.GravitySingularityTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class TheAmbassador(location: Location) : OdysseyBoss(
    name = "The Ambassador",
    type = EntityType.ILLUSIONER
) {

    var illusioner: Illusioner
    init {
        entity = spawnBossEntity(location)
        illusioner = entity as Illusioner
    }

    internal val giftManager = GiftManager(this)

    // Boss Spawning Logic
    private var despawnTimer: Long = 1

    // Trading Mechanic
    internal var patience: Double = 55.0
    internal var appeasement: Double = 0.0
    internal var playersGiftCooldown = mutableMapOf<UUID, Long>()
    internal var playerLikeness = mutableMapOf<UUID, Double>()
    internal var isAngered: Boolean = false

    private val itemLootTable = listOf(
        Material.RAW_GOLD_BLOCK,
        Material.SLIME_BLOCK,
        Material.WEATHERED_COPPER,
        Material.WAXED_COPPER_BLOCK,
        Material.RAW_IRON_BLOCK,
        Material.GOLDEN_PICKAXE,
        Material.DIAMOND_PICKAXE,
    )

    /*-----------------------------------------------------------------------------------------------*/

    val AMBASSADOR_COLOR = TextColor.color(255, 85, 255)
    val WHITE_COLOR = TextColor.color(255, 255, 255)

    // Messages
    private val chatPrefix = Component.text("[The Ambassador] ", TextColor.color(255, 85, 255))
    /*
    private val damagedMessages = listOf(
        Component.text("Such Folly... Such Weakness...", TextColor.color(255, 255, 255)),
        Component.text("Any attacks are futile...", TextColor.color(255, 255, 255)),
        Component.text("The damage you think your inflicting is minimal.", TextColor.color(255, 255, 255)),
        Component.text("I am just disappointed...", TextColor.color(255, 255, 255)),
        Component.text("Just stop... Your attacks are worthless.", TextColor.color(255, 255, 255))
    )

     */

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/
    // Create

    // Create Ambassador Boss in plugin
    fun createEvent(world: World) {
        despawnTimer = System.currentTimeMillis()
        // Spawning
        val hostPlayer = world.players.random()
        val location = hostPlayer.location.clone().add((-28..28).random().toDouble(), 0.0, (-28..28).random().toDouble())
        location.y = 300.0
        // Messages
        with(Odyssey.instance.server) {
            //broadcast(vailPrefix.append(Component.text("My Ambassador has arrived!", TextColor.color(255, 255, 85))))
            broadcast(chatPrefix.append(Component.text("I am descending upon ${hostPlayer.name}'s land...", TextColor.color(255, 255, 255))))
            logger.info("The Ambassador has arrived at x:${location.x}, y:${location.z}")
        }
        location.world.players.forEach { it.playSound(it.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.5F, 0.9F) }

        hostPlayer.sendMessage(chatPrefix.append(Component.text("Be prepared for my arrival ${hostPlayer.name}...", TextColor.color(85, 85, 85))))

        isActive = true
        val departTimer = AmbassadorDepartTask()
        departTimer.runTaskLater(Odyssey.instance, 20 * 60 * 120 * 1) // 2hrs
    }

    // Spawn entity
    override fun spawnBossEntity(location: Location): Illusioner {
        val spawnLocation = location.add(0.0, 64.0, 0.0)
        return (location.world.spawnEntity(spawnLocation, EntityType.ILLUSIONER) as Illusioner).apply {
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 1),
                    PotionEffect(PotionEffectType.GLOWING, 20 * 300, 1),
                    PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 3),
                    PotionEffect(PotionEffectType.WATER_BREATHING, 99999, 3),
                    PotionEffect(PotionEffectType.SPEED, 99999, 2)
                )
            )
            // Change Default Behaviour
            customName(Component.text(this@TheAmbassador.name, AMBASSADOR_COLOR))
            isCustomNameVisible = true
            removeWhenFarAway = false
            isCanJoinRaid = false
            isAware = false
            canPickupItems = true
            equipment.helmet = ItemStack(Material.DIAMOND_HELMET)
            equipment.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
            equipment.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            equipment.boots = ItemStack(Material.DIAMOND_BOOTS)
            equipment.itemInMainHandDropChance = 100F
            // Health
            addHealthAttribute(930.0, "boss.health")
            addArmorAttribute(6.0, "boss.armor")
            health = 950.0
            // Add Kinetic Blaster
            clearActiveItem()
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    internal fun defeatedBoss(entity: Illusioner, vanquisher: Player?) {
        if (entity != this.illusioner) return
        if (vanquisher is Player) {
            //vanquisher.world.dropItem(vanquisher.location, (Arcane.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, 1)))
            vanquisher.world.dropItem(vanquisher.location, (Miscellaneous.PRIMO_GEM.newItemStack(15)))
            vanquisher.giveExpLevels(40)
        }
        val vanquisherName = vanquisher?.name ?: "An unknown Hero"
        // Nearby player get xp and text
        entity.world.getNearbyPlayers(entity.location, 64.0).forEach {
            it.sendMessage(Component.text("The Ambassador has departed ungracefully!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
            it.sendMessage(
                Component.text("With ", TextColor.color(255, 255, 85), TextDecoration.ITALIC)
                    .append(Component.text(vanquisherName).color(TextColor.color(255, 170, 0)))
                    .append(Component.text(" taking the final blow!").color(TextColor.color(255, 255, 85))).decorate(TextDecoration.ITALIC)
            )
            it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F)
            it.giveExp(3550)
        }
        isActive = false
        hasRemove = true
    }

    internal fun departBoss() {
        entity.world.players.forEach {
            it.sendMessage(Component.text("The Ambassador has left the realm...!", TextColor.color(255, 255, 85), TextDecoration.ITALIC))
            it.playSound(it, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F)
        }
        entity.remove()
        isActive = false
        hasRemove = true
    }

    private fun activateBoss() {
        entity.world.getNearbyPlayers(entity.location, 64.0).forEach {
            it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F)
        }
        (entity as Illusioner).isAware = true
        AmbassadorAttackCycle().runTaskTimer(Odyssey.instance, 0, 20 * 7)
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // Spawn a dummy clone
    private fun spawnHoloDummy(location: Location) {
        (entity.world.spawnEntity(location, EntityType.ILLUSIONER) as Illusioner).apply {
            customName(Component.text(this@TheAmbassador.name, AMBASSADOR_COLOR))
            isCustomNameVisible = true
            isCanJoinRaid = false
            isAware = true
            lootTable = null
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.REGENERATION, 20 * 300, 0),
                    PotionEffect(PotionEffectType.SLOWNESS, 20 * 300, 1)
                )
            )
            // Add Item
            clearActiveItem()
            equipment.itemInMainHandDropChance = 0F
        }
    }

    // Create a new firework entity
    private fun createSuperFirework(targetLocation: Location): Firework {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA)
        val superFirework: Firework = (entity.world.spawnEntity(targetLocation, EntityType.FIREWORK_ROCKET) as Firework).apply {
            fireworkMeta = fireworkMeta.clone().also {
                it.addEffect(
                    FireworkEffect.builder()
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(randomColors.random())
                        .withFade(randomColors.random())
                        .trail(true)
                        .flicker(true)
                        .build()
                )
            }
            fireworkMeta.power = 120
            ticksToDetonate = 40
            velocity = targetLocation.clone().add(0.0, -1.918, 0.0).subtract(targetLocation).toVector()
            addScoreboardTag(EntityTags.SUPER_FIREWORK)
        }
        return superFirework
    }

    // Calls fireworks from the sky
    private fun skyBombardAttack(targetLocation: Location) {
        // Spawn 7 random fireworks
        repeat(13) {
            createSuperFirework(targetLocation.clone().add((-7..7).random().toDouble(), (35..45).random().toDouble(), (-7..7).random().toDouble()))
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
            addScoreboardTag(EffectTags.GRAVITY_WELLED)
            addScoreboardTag(EntityTags.FALLING_SINGULARITY)
            addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 0))
        }
        GravitySingularityTask(fallingSingularity, illusioner, 10, 30).runTaskTimer(Odyssey.instance, 0, 10)
        RemoveSingularityStand(fallingSingularity).runTaskLater(Odyssey.instance, 33 * 10)
    }

    // Spawn a vortex that launches players
    private fun gravityLaunchAttack(targetLocation: Location) {
        targetLocation.getNearbyPlayers(7.5).forEach {
            it.damage(18.0, entity)
            it.addPotionEffects(listOf(
                PotionEffect(PotionEffectType.LEVITATION, 20 * 8, 0),
                PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 1),
                PotionEffect(PotionEffectType.BLINDNESS, 20, 0)
            ))
            // Sounds and Effects
            it.playSound(it, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.3F, 1.1F)
            it.playSound(it, Sound.ITEM_TRIDENT_THUNDER, 2.5F, 0.8F)
            it.playSound(it, Sound.BLOCK_BEACON_POWER_SELECT, 1.5F, 0.3F)
            it.playSound(it, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0F, 1.0F)
            it.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 95, 2.5, 0.5, 2.5)
            it.spawnParticle(Particle.END_ROD, it.location, 65, 2.0, 1.0, 2.0)
        }
    }

    // Hijack and Clone Attack
    private fun hijackAttack(target: Player) {
        // Target is hijacked player
        with(target) {
            teleport(entity.location)
            addPassenger(entity)
            damage(2.0, entity)
            playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
            playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
            playSound(this, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
        }
        target.world.getNearbyPlayers(target.location, 22.0).forEach {
            if (it != target) {
                // Arrow and Vectors
                val arrow = it.world.spawnEntity(it.location, EntityType.ARROW) as Arrow
                val unitVector = target.location.subtract(arrow.location).toVector().normalize()
                arrow.velocity = unitVector.clone().multiply(3.14)
                it.eyeLocation.direction = unitVector
                // Sounds and Effects
                it.playSound(it, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
                it.playSound(it, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
                it.playSound(it, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
                it.spawnParticle(Particle.DAMAGE_INDICATOR, it.location, 43, 1.5, 0.5, 1.5)
                it.spawnParticle(Particle.PORTAL, it.location, 42, 2.5, 0.5, 2.5)
                it.spawnParticle(Particle.END_ROD, it.location, 35, 2.0, 1.0, 2.0)
                it.spawnParticle(Particle.WITCH, it.location, 25, 1.0, 1.0, 1.0)
                it.swingMainHand()
                spawnHoloDummy(it.location)
            }
        }
        // Hijack
        AmbassadorHijackTasks(entity as Illusioner).runTaskTimer(Odyssey.instance, 0, 10)
    }

    // Pull player and do damage
    internal fun voidPullBackAttack(player: Player) {
        with(player) {
            teleport(entity.location)
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.LEVITATION, 20 * 10, 1),
                PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 2)
            ))
            damage(10.0, entity)
            // Sounds and Effects
            playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
            playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
            playSound(this, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
            spawnParticle(Particle.DAMAGE_INDICATOR, this.location, 43, 1.5, 0.5, 1.5)
            spawnParticle(Particle.PORTAL, this.location, 42, 2.5, 0.5, 2.5)
            spawnParticle(Particle.END_ROD, this.location, 35, 2.0, 1.0, 2.0)
            spawnParticle(Particle.WITCH, this.location, 25, 1.0, 1.0, 1.0)
            sendAmbassadorChat(AmbassadorMessage.ELYTRA_PULL_BACK_ATTACK)
        }
    }

    internal fun attackPatternHandler() {
        val players = entity.location.getNearbyPlayers(28.0)
        val playersNear = players.isNotEmpty()
        val attackLocation = if (playersNear)
        { players.random().location } else
        { entity.location.clone().add((-9..9).random().toDouble(), 0.0, (-9..9).random().toDouble()) }

        when ((1..10).random()) {
            in 1..3 -> {
                skyBombardAttack(attackLocation)
                players.forEach { it.sendAmbassadorChat(AmbassadorMessage.SKY_BOMBARD_ATTACK) }
            }
            in 4..5 -> {
                gravityLaunchAttack(attackLocation)
                players.forEach { it.sendAmbassadorChat(AmbassadorMessage.GRAVITY_LAUNCH_ATTACK) }
            }
            in 6..7 -> {
                if (playersNear) { hijackAttack(players.random()) }
                players.forEach { it.sendAmbassadorChat(AmbassadorMessage.HIJACK_ATTACK) }
            }
            in 8..10 -> {
                if (playersNear) { players.forEach { fallingSingularityAttack(it.location.clone().add(0.0, 15.0, 0.0)) } }
                players.forEach { it.sendAmbassadorChat(AmbassadorMessage.FALLING_SINGULARITY_ATTACK) }
            }
        }
    }

    // Check damage source and current stats
    internal fun takeDamageHandler(damager: Entity, damage: Double) {
        // Announcement
        val players = illusioner.getNearbyEntities(32.0, 32.0, 32.0).filterIsInstance<Player>()

        // Check if Patience
        if (isAngered) return
        // Not Player
        if (damager !is Player) {
            appeasement -= (damage + 2.0)
            patience -= damage
            // Messages
            if (patience <= 0) { players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_8) } }
            else { players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_9) } }
        }
        // Player
        else {
            // If First Time
            if (!playerLikeness.containsKey(damager.uniqueId)) {
                playerLikeness[damager.uniqueId] = 0.0
                players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_6) }
                return
            }
            // Continuing
            appeasement -= (damage + 2.0)
            patience -= damage
            val likenessValue = playerLikeness[damager.uniqueId]!!
            if (likenessValue >= 65.0) { players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_5) } }
            else if (likenessValue >= 25.0) { players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_4) } }
            else { players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_2, true) } }
            playerLikeness[damager.uniqueId] = likenessValue - damage
        }

        // Activate
        if (patience <= 0) {
            players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_7) }
            isAngered = true
            activateBoss()
        }
        else if (appeasement <= -5) {
            players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_3) }
        }
        else {
            players.forEach { it.sendAmbassadorChat(AmbassadorMessage.PATIENCE_MESSAGE_1, true) }
        }
    }

    // Appeasement Mechanic
    internal fun appeasementCheck(player: Player, item: Item) {
        // Check if players in gift cooldown map
        if (!playersGiftCooldown.containsKey(player.uniqueId)) {
            playersGiftCooldown[player.uniqueId] = System.currentTimeMillis()
            calculateGiftTable(player, item)
        }
        // Time
        val timeElapsed: Long = System.currentTimeMillis() - playersGiftCooldown[player.uniqueId]!!
        if (timeElapsed >= 1000 * 5) {
            playersGiftCooldown[player.uniqueId] = System.currentTimeMillis()
            calculateGiftTable(player, item)
        }
        else {
            player.sendAmbassadorChat(AmbassadorMessage.WAIT_FOR_GIFT_COOLDOWN, true)
        }
    }

    // random delay 2-5
    private fun calculateGiftTable(player: Player, gift: Item) {
        val material: Material = gift.itemStack.type
        // Check if item in table
        var giftAccepted = true
        // Check player likeness
        if (!playerLikeness.containsKey(player.uniqueId)) {
            playerLikeness[player.uniqueId] = 0.0
        }
        val likenessValue = playerLikeness[player.uniqueId]!!
        // Check if likeness maxed
        if (playerLikeness[player.uniqueId]!! >= 100) {
            player.sendAmbassadorChat(AmbassadorMessage.MAX_LIKENESS, true)
            playerLikeness[player.uniqueId]!! + 5
            return
        }

        // Check if special item make (MAKE SEPARATE FLAGS LATER)
        if ((gift.itemStack.itemMeta.hasEnchants() || material == Material.ENCHANTED_BOOK)) {
            appeasement -= 2
            patience -= 2
            player.sendAmbassadorChat(AmbassadorMessage.REJECT_ENCHANTS)
            return
        }

        var giftLikeness = 0
        val extraValue = ((likenessValue / 25).toInt() + 1)
        // Checks if gift in when, and deletes if accepted
        val nonMatchingReward = ItemStack(itemLootTable.random(), 1)
        val inventory = player.inventory

        when (material) {
            Material.NETHER_STAR -> {
                giftLikeness += 30
                val gravityBook = ItemStack(Material.BOOK)
                inventory.addItem(gravityBook)
            }
            Material.NETHERITE_INGOT -> {
                giftLikeness += 8
                if (appeasement > 40) { inventory.addItem(ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 3)) }
                else if (appeasement > 20) {
                    // inventory.addItem(Weapons.NETHERITE_SABER.createWeapon())
                }
                else { inventory.addItem(ItemStack(Material.NETHERITE_AXE, 1)) }
            }
            Material.DIAMOND -> {
                giftLikeness += 5
                //val weapon = listOf(Weapons.DIAMOND_CLAYMORE, Weapons.DIAMOND_KATANA, Weapons.DIAMOND_HALBERD, Weapons.DIAMOND_SICKLE, Weapons.DIAMOND_WARHAMMER).random()
                //inventory.addItem(weapon.createWeapon())
            }
            Material.AMETHYST_SHARD -> {
                giftLikeness += 2
                inventory.addItem(Miscellaneous.BLANK_TOME.newItemStack(maxOf(extraValue - 1, 1)))
            }
            Material.AMETHYST_BLOCK -> {
                giftLikeness += 8
                val tome = listOf(Miscellaneous.TOME_OF_PROMOTION, Miscellaneous.TOME_OF_EMBRACE, Miscellaneous.TOME_OF_DISCHARGE).random()
                inventory.addItem(tome.newItemStack(1))
            }
            Material.EMERALD -> {
                giftLikeness += 4
                val randomBricks = listOf(Material.BRICKS, Material.RED_NETHER_BRICKS, Material.TERRACOTTA, Material.MUD_BRICKS, Material.QUARTZ_BRICKS)
                inventory.addItem(ItemStack(randomBricks.random(), 3 + (2 * extraValue)))
                inventory.addItem(ItemStack(randomBricks.random(), 7))
                inventory.addItem(ItemStack(randomBricks.random(), 3 * extraValue))
            }
            Material.SCULK, Material.SCULK_CATALYST, Material.SCULK_SENSOR, Material.SCULK_SHRIEKER, Material.SCULK_VEIN -> {
                giftLikeness -= 45
                patience -= 45
                player.sendAmbassadorChat(AmbassadorMessage.SCULK_GIFT)
            }
            Material.ROTTEN_FLESH, Material.SPIDER_EYE -> {
                giftLikeness -= 2
                patience -= 2
            }
            Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK -> {
                giftLikeness -= 3
                val randomBlocks = listOf(Material.BARREL, Material.CHEST, Material.SMITHING_TABLE, Material.BLAST_FURNACE, Material.FURNACE)
                if (appeasement > 40) { inventory.addItem(ItemStack(Material.ENCHANTING_TABLE, 1)) }
                else { inventory.addItem(ItemStack(randomBlocks.random(), 4)) }
                inventory.addItem(ItemStack(randomBlocks.random(), extraValue))
            }
            Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT -> {
                giftLikeness += 1
                if (appeasement > 40) { inventory.addItem(ItemStack(Material.GRASS_BLOCK, extraValue)) }
                else { inventory.addItem(ItemStack(Material.GRASS_BLOCK, extraValue)) }
            }
            Material.WRITTEN_BOOK, Material.PAINTING -> {
                val randomTrim = listOf(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
                if (appeasement > 60) { inventory.addItem(ItemStack(randomTrim.random(), 1)) }
                else { inventory.addItem(ItemStack(Material.RAW_GOLD, extraValue)) }
            }
            Material.BEETROOT_SEEDS, Material.PUMPKIN_SEEDS, Material.WHEAT_SEEDS, Material.MELON_SEEDS -> {
                giftLikeness += 1
                val randomFood = listOf(Material.PUMPKIN_PIE, Material.BREAD, Material.BEETROOT_SOUP)
                inventory.addItem(ItemStack(randomFood.random(), extraValue))
            }
            Material.ENCHANTED_GOLDEN_APPLE -> {
                inventory.addItem(Miscellaneous.TOME_OF_POLYMERIZATION.newItemStack(1))
                giftLikeness += 15
            }
            Material.HEART_OF_THE_SEA -> {
                giftLikeness += 15
                if (appeasement > 50) { inventory.addItem(ItemStack(Material.TRIDENT, 1)) }
                else { inventory.addItem(ItemStack(Material.RAW_GOLD, extraValue * 6)) }
            }
            Material.ACACIA_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.ACACIA_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.ACACIA_PLANKS, extraValue * 5))
            }
            Material.OAK_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.OAK_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.ACACIA_PLANKS, extraValue * 5))
            }
            Material.BIRCH_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.BIRCH_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.BIRCH_PLANKS, extraValue * 5))
            }
            Material.CHERRY_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.CHERRY_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.CHERRY_PLANKS, extraValue * 5))
            }
            Material.DARK_OAK_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.DARK_OAK_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.DARK_OAK_PLANKS, extraValue * 5))
            }
            Material.JUNGLE_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.JUNGLE_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.JUNGLE_PLANKS, extraValue * 5))
            }
            Material.SPRUCE_SAPLING -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.SPRUCE_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.SPRUCE_PLANKS, extraValue * 5))
            }
            Material.MANGROVE_PROPAGULE -> {
                giftLikeness += 5
                inventory.addItem(ItemStack(Material.MANGROVE_LOG, extraValue * 3))
                inventory.addItem(ItemStack(Material.MANGROVE_PLANKS, extraValue * 5))
            }
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP,
            Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER,
            Material.LILAC, Material.ROSE_BUSH, Material.PEONY -> {
                giftLikeness += 7
                if (appeasement > 40) { inventory.addItem(ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1)) }
                else { inventory.addItem(ItemStack(Material.SNIFFER_EGG, extraValue)) }
            }
            Material.MUSIC_DISC_WARD, Material.MUSIC_DISC_11, Material.MUSIC_DISC_5, Material.MUSIC_DISC_13, Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_BLOCKS,
            Material.MUSIC_DISC_PIGSTEP, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_CAT -> {
                giftLikeness += 7
                inventory.addItem(ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 1))
            }
            else -> {
                giftAccepted = false
            }
        }

        // Particles
        if (giftLikeness >= 10) {
            entity.world.spawnParticle(Particle.WITCH, player.location, 35, 1.0, 1.0, 1.0)
        }
        else if (giftLikeness >= 5) {
            entity.world.spawnParticle(Particle.HAPPY_VILLAGER, player.location, 35, 1.0, 1.0, 1.0)
        }
        else if (giftLikeness < -2) {
            player.sendAmbassadorChat(AmbassadorMessage.BAD_GIFTS)
        }
        // Final
        if (giftAccepted) {
            gift.remove()
            appeasement += giftLikeness
            val newLikeness = likenessValue + giftLikeness
            playerLikeness[player.uniqueId] = newLikeness
            if (newLikeness > 50.0) player.sendAmbassadorChat(AmbassadorMessage.NICE_GIFTS)
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    // Chat Message
    internal fun Player.sendAmbassadorChat(content: AmbassadorMessage, sayPlayer: Boolean = false) {
        val message = Component.text("[The Ambassador] ", AMBASSADOR_COLOR)
        if (sayPlayer) {
            sendMessage(message.append(name().color(WHITE_COLOR)).append(Component.text(content.text, WHITE_COLOR)))
        }
        else {
            sendMessage(message.append(Component.text(content.text, WHITE_COLOR)))
        }
    }

}

