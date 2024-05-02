package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.hasOdysseyItemTag
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.Damageable

object ArcaneListeners: Listener {

    private val ARCANE_REACH_MAP = mapOf(
        ItemModels.ARCANE_WAND to 32.0, // Range
        ItemModels.WARPING_WAND to 8.0, // Cone
    )

    @EventHandler(priority = EventPriority.HIGHEST)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) {
            leftClickHandler(event)
        }
    }

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHandEquipment = player.equipment.itemInOffHand
        if (!offHandEquipment.hasItemMeta()) return
        if (!offHandEquipment.itemMeta!!.hasCustomModelData()) return
        if (!offHandEquipment.hasOdysseyItemTag()) return
        if (player.hasCooldown(offHandEquipment.type)) return
        val model = offHandEquipment.itemMeta!!.customModelData
        if (ARCANE_REACH_MAP[model] == null) return
        val mainHandBook = player.equipment.itemInOffHand
        if (!mainHandBook.hasItemMeta()) return
        if (!mainHandBook.itemMeta!!.hasCustomModelData()) return // Can only use volumes ??????
        // Sentries Passed
        val itemTag = offHandEquipment.getOdysseyTag() ?: return
        when (itemTag) {
            "arcane_wand" -> {
                arcaneWandHandler(event)
            }
            "warping_wand" -> {
                warpingWandHandler(event)
            }
        }

    }

    private fun arcaneWandHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHandEquipment = player.equipment.itemInOffHand
        val mainHandEquipment = player.equipment.itemInMainHand
        if (mainHandEquipment.type != Material.BOOK && mainHandEquipment.type != Material.ENCHANTED_BOOK) return
        val entity = getRayTraceTarget(player, offHandEquipment.itemMeta!!.customModelData)
        if (entity !is LivingEntity) return
        // Run Attack
        player.attack(entity)
        player.setCooldown(offHandEquipment.type, 3 * 20)
        if (offHandEquipment.itemMeta is Damageable) {
            (offHandEquipment.itemMeta as Damageable).damage -= 1
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

    private fun ovalWandHandler(event: PlayerInteractEvent) {
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

    private fun warpingWandHandler(event: PlayerInteractEvent) {
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

    private fun getRayTraceTarget(player: Player, model: Int): Entity? {
        val reach = ARCANE_REACH_MAP[model] ?: return null
        val result = player.rayTraceEntities(reach.toInt()) ?: return null
        val target = result.hitEntity ?: return null
        val distance = player.eyeLocation.distance(target.location)
        if (reach < distance) return null
        return target
    }

}