package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getUUIDString
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.removeTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.setUUIDTag
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.tasks.BurstBarrageTask
import me.shadowalzazel.mcodyssey.tasks.GaleWindTask
import me.shadowalzazel.mcodyssey.tasks.OverchargeTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin


object RangedListeners : Listener {

    private var ALCHEMY_ARTILLERY_AMMO = mutableMapOf<UUID, ItemStack?>() // DOES NOT SAVE AFTER SERVER SHUTDOWN
    private var ALCHEMY_ARTILLERY_COUNTER = mutableMapOf<UUID, Int>()

    private var galewindCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to shooting bows
    @EventHandler
    fun mainBowShotHandler(event: EntityShootBowEvent) {
        if (event.bow?.hasItemMeta() == false) { return }
        val projectile = event.projectile
        if (projectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW)) { return }
        val shooter = event.entity
        if (event.bow == null) { return }
        val bow = event.bow!!
        // TODO: Make priority for some enchants

        // Loop for all enchants
        for (enchant in bow.enchantments) {
            when (enchant.key) {
                OdysseyEnchantments.ALCHEMY_ARTILLERY -> {
                    event.projectile = alchemyArtilleryEnchantmentShoot(projectile, bow, shooter, enchant.value) ?: event.projectile
                }
                OdysseyEnchantments.BOLA_SHOT -> {
                    bolaShotEnchantmentShoot(projectile, enchant.value)
                }
                OdysseyEnchantments.BURST_BARRAGE -> {
                    burstBarrageEnchantmentShoot(projectile, shooter, enchant.value)
                }
                OdysseyEnchantments.CHAIN_REACTION -> {
                    chainReactionEnchantmentShoot(projectile, enchant.value)
                }
                OdysseyEnchantments.CLUSTER_SHOT -> {
                    clusterShotEnchantmentShoot(projectile, enchant.value)
                }
                OdysseyEnchantments.LUCKY_DRAW -> {
                    event.setConsumeItem(!luckyDrawEnchantmentShoot(enchant.value))
                }
                OdysseyEnchantments.GALE_WIND -> {
                    if (cooldownManager(shooter, "Gale Wind", galewindCooldown, 3.0)) {
                        galeWindEnchantmentShoot(shooter, enchant.value)
                    }
                }
                OdysseyEnchantments.OVERCHARGE -> {
                    overchargeEnchantmentShoot(shooter, projectile)
                }
                OdysseyEnchantments.PERPETUAL_PROJECTILE -> {
                    perpetualProjectileEnchantmentShoot(projectile, enchant.value)
                }
                OdysseyEnchantments.RICOCHET -> {
                    ricochetEnchantmentShoot(projectile, enchant.value)
                }
                OdysseyEnchantments.SHARPSHOOTER -> {
                    sharpshooterEnchantmentShoot(projectile, enchant.value)
                }
                OdysseyEnchantments.SOUL_REND -> {
                    soulRendEnchantmentShoot(projectile, enchant.value)
                }
            }
        }
    }
    // Main function for enchantments relating to projectile damage
    @EventHandler
    fun mainProjectileDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) { return }
        if (event.damager !is Projectile) { return }
        if (event.entity !is LivingEntity) { return }
        val projectile = event.damager as Projectile
        if (projectile.shooter == null) { return }
        val shooter: LivingEntity = projectile.shooter as LivingEntity
        val victim: LivingEntity = event.entity as LivingEntity

        // Loop over arrow projectile tags
        // TODO: Maybe sort in priority the tags
        for (tag in projectile.scoreboardTags) {
            when (tag) {
                EntityTags.OVERCHARGE_ARROW -> {
                    event.damage += overchargeEnchantmentHit(projectile)
                }
                EntityTags.RICOCHET_ARROW -> {
                    event.damage += ricochetEnchantmentEntityHit(projectile)
                }
                EntityTags.SHARPSHOOTER_ARROW -> {
                    event.damage += sharpshooterEnchantmentHit(projectile)
                }
                EntityTags.SOUL_REND_ARROW -> {
                    soulRendEnchantmentHit(shooter, projectile, victim)
                }
            }
        }
    }


    // Main function for enchantments relating to projectile hits
    @EventHandler
    fun mainProjectileHitHandler(event: ProjectileHitEvent) {
        if (event.entity.shooter !is LivingEntity) { return }
        val shooter: LivingEntity = event.entity.shooter as LivingEntity
        val projectile: Projectile = event.entity

        if (event.hitEntity is LivingEntity) {
            val victim: LivingEntity = event.hitEntity as LivingEntity
            for (tag in projectile.scoreboardTags) {
                when (tag) {
                    EntityTags.BOLA_SHOT_ARROW -> {
                        bolaShotEnchantmentHit(projectile, victim)
                    }
                    EntityTags.CHAIN_REACTION_ARROW -> {
                        chainReactionEnchantmentHit(projectile, victim)
                    }
                    EntityTags.CLUSTER_SHOT_ARROW -> {
                        clusterShotEnchantmentHit(projectile, victim)
                    }
                }
            }
        }
        else if (event.hitBlock != null) {
            if (projectile.scoreboardTags.contains(EntityTags.RICOCHET_ARROW)) {
                ricochetEnchantmentBlockHit(projectile, event.hitBlockFace!!.direction)
            }
        }
    }

    // Main function for enchantments relating to loading crossbows
    @EventHandler
    fun crossbowLoadingHandler(event: EntityLoadCrossbowEvent) {
        val someEntity = event.entity
        if (event.crossbow?.hasItemMeta() == true) {
            val someCrossbow = event.crossbow!!
            for (enchant in someCrossbow.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.ALCHEMY_ARTILLERY -> {
                        event.isCancelled = alchemyArtilleryEnchantmentLoad(someEntity, someCrossbow)
                    }
                }
            }
        }
    }

    // Main function for readying arrows
    @EventHandler
    fun bowReadyHandler(event: PlayerReadyArrowEvent) {
        // Only works in survival
        if (event.bow.hasItemMeta()) {
            val bow = event.bow
            val player = event.player
            for (enchant in bow.enchantments) {
                when (enchant.key) {
                    OdysseyEnchantments.OVERCHARGE -> {
                        overchargeEnchantmentLoad(player, bow, enchant.value)
                    }
                }
            }
        }
    }

    // Helper Function for item drop
    @EventHandler
    fun itemDropHandler(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.hasItemMeta()) {
            if (event.itemDrop.itemStack.enchantments.containsKey(OdysseyEnchantments.SOUL_REND)) {
                event.isCancelled = soulRendEnchantmentActivate(event.player)
                // TODO: Weird Activation
            }
        }
    }

    // Helper function for cooldown
    private fun cooldownManager(eventHitter: LivingEntity, someMessage: String, someCooldownMap: MutableMap<UUID, Long>, cooldownTimer: Double): Boolean {
        if (!someCooldownMap.containsKey(eventHitter.uniqueId)) {
            someCooldownMap[eventHitter.uniqueId] = 0L
        }
        // Cooldown Timer
        val timeElapsed: Long = System.currentTimeMillis() - someCooldownMap[eventHitter.uniqueId]!!
        return if (timeElapsed > cooldownTimer * 1000) {
            someCooldownMap[eventHitter.uniqueId] = System.currentTimeMillis()
            true
        } else {
            eventHitter.sendActionBar(
                Component.text(
                    "$someMessage on Cooldown (Time Remaining: ${cooldownTimer - ((timeElapsed / 1) * 0.001)}s)",
                    TextColor.color(155, 155, 155)
                )
            )
            false
        }

    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // ------------------------------- ALCHEMY_ARTILLERY ------------------------------------

    private fun alchemyArtilleryEnchantmentLoad(loader: LivingEntity, crossbow: ItemStack): Boolean {
        if (!(loader.equipment!!.itemInOffHand.type == Material.SPLASH_POTION || loader.equipment!!.itemInOffHand.type == Material.LINGERING_POTION)) {
            return false
        }
        val potionOffHand = loader.equipment!!.itemInOffHand
        return if (!crossbow.hasTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)) {
            val newUUID = UUID.randomUUID()
            crossbow.addTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)
            crossbow.setUUIDTag(newUUID)
            // Load Item to UUID
            ALCHEMY_ARTILLERY_AMMO[newUUID] = potionOffHand
            val multiCounter = if (crossbow.itemMeta.hasEnchant(Enchantment.MULTISHOT)) 3 else 1
            // Load Counter to UUID
            ALCHEMY_ARTILLERY_COUNTER[newUUID] = multiCounter
            loader.equipment!!.setItemInOffHand(ItemStack(Material.AIR, 1))
            false
        } else {
            true
        }
    }

    private fun alchemyArtilleryEnchantmentShoot(projectile: Entity, crossbow: ItemStack, shooter: LivingEntity, enchantmentStrength: Int): ThrownPotion? {
        var lastShot = false
        var potion: ThrownPotion? = null
        if (!crossbow.hasTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)) {
            return null
        }
        val bowUUID = UUID.fromString(crossbow.getUUIDString())
        val count = ALCHEMY_ARTILLERY_COUNTER[bowUUID] ?: 1
        if (count >= 1) {
            // Spawn potion with item
            potion = (shooter.world.spawnEntity(projectile.location, EntityType.SPLASH_POTION) as ThrownPotion).also {
                it.item = ALCHEMY_ARTILLERY_AMMO[bowUUID] ?: ItemStack(Material.SPLASH_POTION, 1)
                it.velocity = projectile.velocity.clone().multiply((enchantmentStrength * 0.2) + 0.1)
                it.shooter = shooter
            }
            ALCHEMY_ARTILLERY_COUNTER[bowUUID] = count - 1
            if (ALCHEMY_ARTILLERY_COUNTER[bowUUID] == 0) {
                lastShot = true
            }
        }
        if (lastShot) {
            crossbow.removeTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)
            ALCHEMY_ARTILLERY_AMMO[bowUUID] = null
        }
        return potion
    }

    // ------------------------------- BOLA_SHOT ------------------------------------
    private fun bolaShotEnchantmentShoot(projectile: Entity, enchantmentStrength: Int) {
        projectile.addScoreboardTag(EntityTags.BOLA_SHOT_ARROW)
        projectile.addScoreboardTag(EntityTags.BOLA_SHOT_MODIFIER + enchantmentStrength)
    }

    private fun bolaShotEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        for (x in 1..3) {
            if (projectile.scoreboardTags.contains(EntityTags.BOLA_SHOT_MODIFIER + x)) {
                if (victim.location.block.type == Material.AIR) {
                    victim.location.block.type = Material.COBWEB
                    victim.addPotionEffects(listOf(
                        PotionEffect(PotionEffectType.SLOW, 20 * (4 + x), 0),
                        PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * (4 + x), 0)))
                }
                break
            }
        }
    }

    // ------------------------------- BURST_BARRAGE ------------------------------------
    private fun burstBarrageEnchantmentShoot(projectile: Entity, shooter: LivingEntity, enchantmentStrength: Int) {
        if (!shooter.scoreboardTags.contains(EntityTags.IS_BURST_BARRAGING) && !projectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW) && projectile is Projectile)  {
            projectile.run {
                if (this is Arrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                if (this is SpectralArrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                if (this is AbstractArrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
            }
            shooter.addScoreboardTag(EntityTags.IS_BURST_BARRAGING)
            val initialVelocity = projectile.velocity.clone()
            BurstBarrageTask(shooter, enchantmentStrength, initialVelocity, projectile).runTaskTimer(Odyssey.instance, 3, 3)
        }

    }

    // ------------------------------- CHAIN_REACTION ------------------------------------
    private fun chainReactionEnchantmentShoot(projectile: Entity, enchantmentStrength: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.CHAIN_REACTION_ARROW)
            addScoreboardTag(EntityTags.CHAIN_REACTION_MODIFIER + enchantmentStrength)
        }
    }

    private fun chainReactionEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        val closeEntities = victim.location.getNearbyLivingEntities(10.0).filter { it != projectile.shooter!! }
        for (x in 1..6) {
            if (projectile.scoreboardTags.contains(EntityTags.CHAIN_REACTION_MODIFIER + (x - 1))) { break }
            // Spawn projectiles
            if (x <= closeEntities.size) {
                val chainVelocity = closeEntities.elementAt(x - 1).location.clone().subtract(victim.location.add(0.0, 0.5, 0.0)).toVector().normalize().multiply(1.5)
                projectile.world.spawnEntity(projectile.location, projectile.type).also {
                    it.velocity = chainVelocity
                }
            }
            // TODO: Maybe shoot back!? both directions, Static Shiv
        }
    }

    // ------------------------------- CLUSTER_SHOT ------------------------------------
    private fun clusterShotEnchantmentShoot(projectile: Entity, enchantmentStrength: Int) {
        projectile.run {
            addScoreboardTag(EntityTags.CLUSTER_SHOT_ARROW)
            addScoreboardTag(EntityTags.CLUSTER_SHOT_MODIFIER + enchantmentStrength)
            if (this is Arrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            if (this is SpectralArrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            if (this is AbstractArrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
        }
    }

    private fun clusterShotEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        var modifier = 0
        for (x in 1..5) {
            if (projectile.scoreboardTags.contains(EntityTags.CLUSTER_SHOT_MODIFIER + x)) {
                modifier = x
                break
            }
        }

        // TODO: Maybe Async each new arrow creation
        for (x in 1..(10 + (modifier * 5))) {
            // Math
            val angle = Math.random() * Math.PI * 2
            val coordinates: Pair<Double, Double> = Pair(cos(angle) * (5..12).random() * 0.1, sin(angle) * (5..12).random() * 0.1)
            // Make Random radii?
            val velocity = projectile.location.clone().add(coordinates.first, 7.5 + ((-20..20).random() * 0.01), coordinates.second).subtract(projectile.location).toVector().normalize().multiply(1.0)
            //victim.launchProjectile(projectile.javaClass).also { }
            victim.world.spawnEntity(victim.location.clone().add(0.0, 0.5, 0.0), projectile.type).also {
                when (it) {
                    is Arrow -> {
                        it.isPersistent = false
                        it.fireTicks = projectile.fireTicks
                        it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                        if (it.hasCustomEffects()) {
                            it.basePotionData = (projectile as Arrow).basePotionData
                        }
                    }
                    is SpectralArrow -> {
                        it.isPersistent = false
                        it.fireTicks = projectile.fireTicks
                        it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                    }
                    is AbstractArrow -> {
                        it.isPersistent = false
                        it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                    }
                    is ThrownPotion -> {
                        it.item = (projectile as ThrownPotion).item
                    }
                }
                // Projectile
                if (it is Projectile) {
                    it.shooter = projectile.shooter
                }
                it.velocity = velocity
                for (tag in projectile.scoreboardTags) {
                    if (tag != EntityTags.CLUSTER_SHOT_ARROW) { it.scoreboardTags.add(tag) }
                }
            }
        }
    }

    // ------------------------------- GALE_WIND ------------------------------------
    private fun galeWindEnchantmentShoot(shooter: LivingEntity, enchantmentStrength: Int) {
        shooter.world.playSound(shooter.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 2.5F, 1.5F)
        GaleWindTask(shooter, enchantmentStrength).runTaskLater(Odyssey.instance, 6)
    }

    // ------------------------------- LUCKY_DRAW ------------------------------------
    private fun luckyDrawEnchantmentShoot(enchantmentStrength: Int): Boolean {
        return (enchantmentStrength * 10) + 7 > (0..100).random()
    }

    // ------------------------------- OVERCHARGE ------------------------------------
    private fun overchargeEnchantmentLoad(player: Player, bow: ItemStack, enchantmentStrength: Int) {
        // TODO: Move Overcharge cooldown to bow
        if (player.scoreboardTags.contains(EntityTags.OVERCHARGE_COOLDOWN)) {
            player.scoreboardTags.remove(EntityTags.OVERCHARGE_COOLDOWN)
            return
        }

        if (!player.scoreboardTags.contains(EntityTags.OVERCHARGING)) {
            player.scoreboardTags.add(EntityTags.OVERCHARGING)
            player.scoreboardTags.add(EntityTags.OVERCHARGE_MODIFIER + 0)
            val overchargeTask = OverchargeTask(player, bow, enchantmentStrength)
            overchargeTask.runTaskTimer(Odyssey.instance, (20 * 2) + 10, 20 * 2)
        }
    }

    private fun overchargeEnchantmentShoot(shooter: LivingEntity, eventProjectile: Entity) {
        with(shooter.scoreboardTags) {
            for (x in 1..5) {
                if (contains(EntityTags.OVERCHARGE_MODIFIER + x) && contains(EntityTags.OVERCHARGING)) {
                    remove(EntityTags.OVERCHARGE_MODIFIER + x)
                    remove(EntityTags.OVERCHARGING)
                    //if (x != 0) { add(EntityTags.OVERCHARGE_COOLDOWN) }
                    eventProjectile.velocity.multiply(1 + (x * 0.2))
                    eventProjectile.scoreboardTags.add(EntityTags.OVERCHARGE_MODIFIER + x)
                    break
                }
            }
        }
        eventProjectile.scoreboardTags.add(EntityTags.OVERCHARGE_ARROW)
    }

    private fun overchargeEnchantmentHit(projectile: Projectile): Double {
        for (x in 1..5) {
            if (projectile.scoreboardTags.contains(EntityTags.OVERCHARGE_MODIFIER + x)) {
                return x * 3.0
            }
        }
        return 0.0
    }

    // ------------------------------- PERPETUAL_PROJECTILE ------------------------------------
    private fun perpetualProjectileEnchantmentShoot(projectile: Entity, enchantmentStrength: Int) {
        projectile.run {
            addScoreboardTag(EntityTags.PERPETUAL_ARROW)
            addScoreboardTag(EntityTags.PERPETUAL_MODIFIER + enchantmentStrength)
            setGravity(false)
            isPersistent = false
        }
    }

    // ------------------------------- RICOCHET ------------------------------------
    private fun ricochetEnchantmentShoot(projectile: Entity, enchantmentStrength: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.RICOCHET_ARROW)
            addScoreboardTag(EntityTags.RICOCHET_BOUNCE + 0)
            addScoreboardTag(EntityTags.RICOCHET_MODIFIER + enchantmentStrength)
        }
    }

    private fun ricochetEnchantmentEntityHit(projectile: Projectile): Double {
        for (x in 1..5) {
            if (projectile.scoreboardTags.contains(EntityTags.RICOCHET_BOUNCE + x)) {
                return x * 2.0
            }
        }
        return 0.0
    }

    private fun ricochetEnchantmentBlockHit(projectile: Entity, eventNormalVector: Vector) {
        var bounce = 0
        var modifier = 0
        with(projectile.scoreboardTags) {
            for (x in 1..4) {
                if (contains(EntityTags.RICOCHET_BOUNCE + x)) { bounce = x }
                if (contains(EntityTags.RICOCHET_MODIFIER + x)) {
                    modifier = x
                    break
                }
            }
            if (bounce < modifier) {
                remove(EntityTags.RICOCHET_BOUNCE + bounce)
                add(EntityTags.RICOCHET_BOUNCE + (bounce + 1))
            }
            else {
                return
            }
        }
        // Spawn New Arrow
        projectile.world.spawnEntity(projectile.location.clone(), projectile.type).also {
            if (it is Arrow) {
                it.basePotionData = it.basePotionData
                it.isPersistent = false
                it.fireTicks = projectile.fireTicks
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            else if (it is ThrownPotion) {
                it.item = (projectile as ThrownPotion).item
            }
            // Projectile
            if (it is Projectile) {
                it.scoreboardTags.addAll(projectile.scoreboardTags)
                it.shooter = (projectile as Projectile).shooter
                // Math
                val normal = eventNormalVector.clone().normalize()
                val input = projectile.velocity.clone().normalize()
                it.velocity = input.subtract(normal.multiply(input.dot(normal) * 2.0)).normalize().multiply(max(projectile.velocity.length() - 0.3, 0.0))
            }
        }
        projectile.remove()
    }

    // ------------------------------- SHARPSHOOTER ------------------------------------
    private fun sharpshooterEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            if (velocity.length() >= 2.5) {
                addScoreboardTag(EntityTags.SHARPSHOOTER_ARROW)
                addScoreboardTag(EntityTags.SHARPSHOOTER_MODIFIER + level)
            }
        }
    }

    private fun sharpshooterEnchantmentHit(projectile: Entity): Double {
        for (x in 1..3) {
            if (projectile.scoreboardTags.contains(EntityTags.SHARPSHOOTER_MODIFIER + x)) {
                return x + 1.0
            }
        }
        return 0.0
    }

    // ------------------------------- SOUL_REND ------------------------------------
    private fun soulRendEnchantmentShoot(projectile: Entity, enchantmentStrength: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.SOUL_REND_ARROW)
            addScoreboardTag(EntityTags.SOUL_REND_MODIFIER + enchantmentStrength)
        }
    }

    private fun soulRendEnchantmentHit(shooter: LivingEntity, projectile: Projectile, victim: LivingEntity) {
        val shooterRendID: String = EntityTags.SOUL_RENDED_BY + shooter.uniqueId
        if (shooterRendID !in victim.scoreboardTags) {
            victim.scoreboardTags.add(shooterRendID)
            for (x in 1..3) {
                if (projectile.scoreboardTags.contains(EntityTags.SOUL_REND_MODIFIER + x)) {
                    victim.scoreboardTags.add(EntityTags.SOUL_REND_MODIFIER + x)
                    break
                }
            }
            victim.world.spawnParticle(Particle.SCULK_SOUL, victim.location, 25, 0.15, 0.15, 0.15)
        }
    }

    private fun soulRendEnchantmentActivate(player: Player): Boolean {
        var soulRendMultiplier = 0
        player.location.getNearbyLivingEntities(32.0).forEach {
            if (it.scoreboardTags.contains(EntityTags.SOUL_RENDED_BY + player.uniqueId)) {
                // Soul Rend Modifier Multiplier
                for (x in 1..3) {
                    if (it.scoreboardTags.contains(EntityTags.SOUL_REND_MODIFIER + x)) {
                        it.scoreboardTags.remove(EntityTags.SOUL_REND_MODIFIER + x)
                        soulRendMultiplier = x
                        break
                    }
                }
                // Damage
                val soulRendDamage = (it.arrowsInBody * soulRendMultiplier) + 2.0
                it.scoreboardTags.remove(EntityTags.SOUL_RENDED_BY + player.uniqueId)
                it.damage(soulRendDamage, player)
                it.world.spawnParticle(Particle.SOUL, it.location, 25, 0.05, 0.35, 0.05)
                it.world.spawnParticle(Particle.SCULK_SOUL, it.location, 25, 0.25, 0.35, 0.25)
            }
        }
        return soulRendMultiplier != 0
    }

}