package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.ARCANE_RANGES
import me.shadowalzazel.mcodyssey.tasks.arcane_tasks.MagicMissileLauncher
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.Damageable

interface ArcaneEquipmentManager {

    fun arcaneWandHandler(event: PlayerInteractEvent) {
        // Basic Vars
        val player = event.player
        val offHand = player.equipment.itemInOffHand
        val mainHand = player.equipment.itemInMainHand
        val model = offHand.itemMeta!!.customModelData
        if (mainHand.type != Material.BOOK && mainHand.type != Material.ENCHANTED_BOOK) return
        val entity = getRayTraceEntity(player, model)
        if (entity !is LivingEntity) return
        // Run Attack
        player.attack(entity)
        player.setCooldown(offHand.type, 3 * 20)
        if (offHand.itemMeta is Damageable) {
            offHand.damage(1, player)
        }
        // Particles
        var location = player.location.clone()
        val unitVector = player.eyeLocation.direction.clone()
        val distance = player.location.distance(entity.location)
        for (x in 0..(distance.toInt())) {
            location = location.add(unitVector)
            player.world.spawnParticle(Particle.WITCH, location, 3, 0.01, 0.01, 0.01)
            // Amethyst noise step/hit every %3
            if (x % 3 == 0) {
                entity.world.playSound(entity.location, Sound.BLOCK_AMETHYST_CLUSTER_HIT, 2F, 2F)
                entity.world.playSound(entity.location, Sound.BLOCK_AMETHYST_CLUSTER_STEP, 2F, 2F)
            }
        }
        player.world.playSound(player.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        entity.world.playSound(entity.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
    }

    fun arcaneBladeHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHand = player.equipment.itemInOffHand
        val mainHand = player.equipment.itemInMainHand
        if (mainHand.type != Material.BOOK && mainHand.type != Material.ENCHANTED_BOOK) return
    }

    fun arcaneScepterHandler(event: PlayerInteractEvent) {
        // Basic Variables
        val player = event.player
        val offHand = player.equipment.itemInOffHand
        val mainHand = player.equipment.itemInMainHand
        val model = offHand.itemMeta!!.customModelData
        if (mainHand.type != Material.BOOK && mainHand.type != Material.ENCHANTED_BOOK) return
        player.setCooldown(offHand.type, 3 * 20)
        if (offHand.itemMeta is Damageable) {
            offHand.damage(1, player)
        }
        // Get Directions
        val eyeDirection = player.eyeLocation.direction.clone().normalize()
        val launchDirection = eyeDirection.multiply(1.3)
        //val arcDirection = eyeDirection.clone().setY(0.0).normalize().setY(0.8).normalize().multiply(1.3) // PRESET OR OVERRIDE MODES
        // Get Target from block ray trace or vector addition
        val targetLocation = getRayTraceLocation(player, model) ?: eyeDirection.clone().multiply(48.0).toLocation(player.world)
        // Spawn Missile
        val missile = (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            it.item = mainHand.clone()
            it.addScoreboardTag(EntityTags.MAGIC_MISSILE)
            it.velocity = launchDirection
            it.shooter = player
            it.setHasLeftShooter(false)
            it.setGravity(false)
        }
        val maxTime = 20 * 10
        val delayTime = 20 * 2
        val period = 2
        val guided = true // Means will lock onto player target
        val launcher = MagicMissileLauncher(launchDirection, missile, maxTime, delayTime, guided, period, targetLocation)
        launcher.runTaskTimer(Odyssey.instance, 10, period.toLong())
    }

    fun ovalWandHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHandEquipment = player.equipment.itemInOffHand
        var location = player.location.clone()
        val unitVector = player.eyeLocation.direction.clone()
        // MAKE OVAL
        val centroid = event.interactionPoint?.clone() ?: return // CHANGE? if does not work
        val distanceFromPoint = player.location.distance(centroid)
        if (distanceFromPoint > 15.0) return
        // Find intersection
        val enemies = centroid.getNearbyLivingEntities(distanceFromPoint).filter {
            it.location.distance(player.location) <= distanceFromPoint
        }

    }

    fun warpingWandHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHandEquipment = player.equipment.itemInOffHand
        val mainHandEquipment = player.equipment.itemInMainHand
        if (mainHandEquipment.type != Material.BOOK && mainHandEquipment.type != Material.ENCHANTED_BOOK) return
        if (offHandEquipment.itemMeta is Damageable) {
            (offHandEquipment.itemMeta as Damageable).damage -= 1
        }
        // Location and math
        val bigR = 8.0 // Big circle radius (distance)
        val center = player.location.clone()
        val unitVector = player.eyeLocation.direction.clone().normalize()
        val midpoint = center.clone().add(unitVector.clone().multiply(bigR / 2))
        // Create a vector from center to entity
        val createTrace = { entity: LivingEntity -> entity.location.clone().subtract(center).toVector().normalize() }
        // Check if is in cone by getting angle and checking distance
        val isInCone = { entity: LivingEntity -> unitVector.angle(createTrace(entity)) < 0.61 && entity.location.distance(center) <= bigR}
        val radiusMid = (bigR / 2) + (0.2 * (bigR / 2))
        val coneEntities = midpoint.getNearbyLivingEntities(radiusMid).filter { isInCone(it) }
        if (coneEntities.isEmpty()) return
        // Run attack
        for (entity in coneEntities) {
            player.attack(entity)
        }
        player.setCooldown(offHandEquipment.type, 5 * 20)
        if (offHandEquipment.itemMeta is Damageable) {
            (offHandEquipment.itemMeta as Damageable).damage -= 1
        }
        // Particles

    }

    private fun getRayTraceEntity(player: Player, model: Int): Entity? {
        val reach = ARCANE_RANGES[model] ?: return null
        val result = player.rayTraceEntities(reach.toInt()) ?: return null
        val target = result.hitEntity ?: return null
        val distance = player.eyeLocation.distance(target.location)
        if (reach < distance) return null
        return target
    }

    private fun getRayTraceLocation(player: Player, model: Int): Location? {
        val reach = ARCANE_RANGES[model] ?: return null
        val result = player.rayTraceBlocks(reach, FluidCollisionMode.NEVER) ?: return null
        val location = result.hitPosition.toLocation(player.world)
        return location
    }

}