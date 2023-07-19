package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.entity.EnderDragonFireballHitEvent
import com.destroystokyo.paper.event.entity.EnderDragonShootFireballEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.listeners.tasks.DragonLightningStormTask
import me.shadowalzazel.mcodyssey.listeners.tasks.LightningCloudTask
import me.shadowalzazel.mcodyssey.listeners.tasks.LightningEyeTask
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EnderDragonChangePhaseEvent
import org.bukkit.event.entity.ExplosionPrimeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object DragonListeners : Listener {

    @EventHandler
    fun dragonPhaseHandler(event: EnderDragonChangePhaseEvent) {
        when(event.currentPhase) {
            EnderDragon.Phase.LEAVE_PORTAL -> {
                event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                return
            }
            else ->{

            }
        }

        println("NEW PHASE: ${event.newPhase}")
        when (event.newPhase) {
            EnderDragon.Phase.ROAR_BEFORE_ATTACK -> {
                event.entity.getNearbyEntities(24.0, 16.0, 24.0).forEach {
                    if (it is Player) {
                        it.addPotionEffects(
                            listOf(
                                PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 0),
                                PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 0),
                                PotionEffect(PotionEffectType.CONFUSION, 1 * 20, 3)
                            )
                        )
                    }
                }
            }
            EnderDragon.Phase.HOVER -> {
                if (5 > (1..10).random()) {
                    event.newPhase = EnderDragon.Phase.STRAFING
                }
            }
            EnderDragon.Phase.STRAFING -> {
                val ground = event.entity.location.clone().toHighestLocation(HeightMap.MOTION_BLOCKING)
                ground.getNearbyEntities(14.0, 16.0, 14.0).forEach {
                    if (it is Player) {
                        it.world.spawnEntity(it.location.clone(), EntityType.LIGHTNING)
                        it.damage(3.0, event.entity)
                        it.world.spawnParticle(Particle.SONIC_BOOM, ground, 1, 0.0, 0.0, 0.0)
                        it.world.spawnParticle(Particle.ELECTRIC_SPARK, ground, 25, 0.4, 0.1, 0.5)

                    }
                }
                with(ground.world) {
                    spawnEntity(ground, EntityType.LIGHTNING)
                    spawnParticle(Particle.SONIC_BOOM, ground, 1, 0.0, 0.0, 0.0)
                    spawnParticle(Particle.ELECTRIC_SPARK, ground, 25, 0.4, 0.1, 0.5)
                }
            }
            EnderDragon.Phase.LAND_ON_PORTAL -> {
                if (1 > (0..1).random()) {
                    event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                    event.entity.addScoreboardTag(EntityTags.LEFT_PORTAL)
                }
            }
            EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET -> {
                if (1 > (0..3).random()) {
                    event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                }
            }
            EnderDragon.Phase.LEAVE_PORTAL -> {
                event.entity.addScoreboardTag(EntityTags.LEFT_PORTAL)
            }
            EnderDragon.Phase.CIRCLING -> {
                if (7 > (1..10).random()) {
                    event.newPhase = EnderDragon.Phase.STRAFING
                }
            }
            EnderDragon.Phase.FLY_TO_PORTAL -> {
                if (7 > (1..11).random()) {
                    event.newPhase = EnderDragon.Phase.CHARGE_PLAYER
                }
                else {
                    DragonLightningStormTask(event.entity).runTaskTimer(Odyssey.instance, 5, 15)
                }
            }
            else -> {
            }
        }


    }

    @EventHandler
    fun dragonShootHandler(event: EnderDragonShootFireballEvent) {
        val fireball = event.fireball
        fireball.teleport(fireball.location.clone().add(0.0, -2.0, 0.0))
        if (6 > (1..10).random()) {
            event.fireball.addScoreboardTag(EntityTags.DRAGON_BOMB)
        }
        else {
            event.entity.world.playSound(event.fireball.location, Sound.ENTITY_BLAZE_SHOOT, 24F, 0.6F)
            event.fireball.velocity.multiply(2.5)
            event.fireball.addScoreboardTag(EntityTags.LIGHTNING_BALL)
        }
    }

    @EventHandler
    fun dragonFireballHandler(event: EnderDragonFireballHitEvent) {
        if (event.targets?.contains(event.entity.shooter) == true) {
            event.isCancelled
            return
        }
        if (event.entity.scoreboardTags.contains(EntityTags.DRAGON_BOMB)) {
            // Fireball
            with(event.entity.world) {
                // Particles
                spawnParticle(Particle.SPELL_WITCH, event.entity.location, 45, 1.0, 0.2, 1.0)
                // Firework
                (spawnEntity(event.entity.location, org.bukkit.entity.EntityType.FIREWORK) as Firework).also {
                    val newMeta = it.fireworkMeta
                    newMeta.power = 110
                    newMeta.addEffect(
                        FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.FUCHSIA)
                            .withFade(Color.FUCHSIA).trail(false).flicker(false).build()
                    )
                    it.fireworkMeta = newMeta
                    it.velocity = Vector(0.0, -3.0, 0.0)
                    it.ticksToDetonate = 1
                }
            }
        }
        if (event.entity.scoreboardTags.contains(EntityTags.LIGHTNING_BALL)) {
            event.areaEffectCloud.particle = Particle.ELECTRIC_SPARK
            event.areaEffectCloud.radius += 2.25F
            LightningCloudTask(event.entity.shooter!! as EnderDragon, event.areaEffectCloud).runTaskTimer(Odyssey.instance, 5, 25)
        }
    }


    @EventHandler
    fun enderCrystalExplosion(event: ExplosionPrimeEvent) {
        if (event.entityType != EntityType.ENDER_CRYSTAL) return
        if (event.entity.location.world.enderDragonBattle == null) return
        val battle = event.entity.world.enderDragonBattle!!
        if (battle.enderDragon == null) return
        battle.enderDragon!!.phase = EnderDragon.Phase.CHARGE_PLAYER

        val eye = (event.entity.world.spawnEntity(event.entity.location.clone().add(0.0, 2.0, 0.0), EntityType.ITEM_DISPLAY) as ItemDisplay).apply {
            itemStack = ItemStack(Material.ENDER_EYE, 1)
            glowColorOverride = Color.FUCHSIA
            isGlowing = true
            viewRange = 100F
            billboard = Display.Billboard.CENTER
            displayHeight = 40.0F
            displayWidth = 40.0F
            brightness = Display.Brightness(14, 14)
        }
        LightningEyeTask(battle.enderDragon!!, eye).runTaskTimer(Odyssey.instance, 5, 25)
    }

}