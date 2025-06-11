package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.runes.ManifestationRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import me.shadowalzazel.mcodyssey.common.arcane.util.ArcaneContext
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.common.tasks.arcane_tasks.MagicMissileLauncher
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.ARCANE_RANGES
import org.bukkit.*
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.Vector

@Suppress("UnstableApiUsage")
interface ArcaneEquipmentManager : VectorParticles, AttackHelper {

    fun oldArcaneWandHandler(event: PlayerInteractEvent) {
        val attacker = event.player
        val equipment = attacker.equipment ?: return
        val arcaneHand = equipment.itemInOffHand
        val bookHand = equipment.itemInMainHand
        // Cooldown
        if (attacker.getCooldown(arcaneHand) > 0) return
        arcaneHand.damage(2, attacker)
        attacker.setCooldown(arcaneHand, 20)
        // Vars
        val damage = 4.0
        val range = 32.0
        val aimAssist = 0.5
        // Run
        arcaneBeam(attacker, damage, range, aimAssist)
    }

    fun arcaneBeam(user: LivingEntity, damage: Double, range: Double, aimAssist: Double) {
        // Logic
        val endLocation: Location
        val target = getRayTraceEntity(user, range, aimAssist)
        if (target is LivingEntity) {
            // Run Attack
            //player.attack(entity)
            //attacker.attack(target)
            val damageSource = createEntityDamageSource(user, null, type = DamageType.MAGIC)
            target.damage(damage, damageSource)

            endLocation = target.eyeLocation
        } else {
            endLocation = user.eyeLocation.clone().add(user.eyeLocation.direction.clone().normalize().multiply(range))
        }

        // Particles in Line
        val particleCount = endLocation.distance(user.location) * 6
        spawnLineParticles(
            particle = Particle.WITCH,
            start = user.location,
            end = endLocation,
            count = particleCount.toInt()
        )
        user.world.playSound(user.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
    }

    fun arcaneWandHandler(caster: LivingEntity) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return
        val arcaneHand = equipment.itemInOffHand
        // Cooldown
        if (caster.getCooldown(arcaneHand) > 0) return
        arcaneHand.damage(2, caster)
        // Temp Context

        // TODO: Create a general all purpose method to read RUNES from item.

        val manifestRune = ManifestationRune.Beam()
        val context = ArcaneContext(
            caster = caster,
            world = caster.world,
            target = null,
            targetLocation = null,
            modifiers = listOf(
                ModifierRune.Range(16.0),
                //ModifierRune.Source(DamageType.IN_FIRE, Particle.FLAME)
            )
        )
        manifestRune.cast(context)
        // TODO: Set to get from gem quality
        caster.setCooldown(arcaneHand, 20)
    }


    fun arcaneBladeHandler(event: PlayerInteractEvent) {
        val attacker = event.player
        val equipment = attacker.equipment ?: return
        val arcaneHand = equipment.itemInOffHand
        val bookHand = equipment.itemInMainHand
        // Cooldown
        if (attacker.getCooldown(arcaneHand) > 0) return
        arcaneHand.damage(2, attacker)
        attacker.setCooldown(arcaneHand, 20)
        // Vars
        val range = 6.0
        val angle = 70.0
        // Logic
        getEntitiesInArc(attacker, angle, range).forEach {
            val damageSource = createEntityDamageSource(attacker, null, type = DamageType.MAGIC)
            it.damage(4.0, damageSource)
        }
        // Spawn particles in Arc
        spawnArcParticles(
            particle = Particle.WITCH,
            center = attacker.eyeLocation,
            directionVector = attacker.eyeLocation.direction,
            pitchAngle = 0.0,
            radius = range,
            arcLength = Math.toRadians(angle),
            height = 0.1,
            count = 55)
    }

    fun arcaneScepterHandler(event: PlayerInteractEvent) {
        val attacker = event.player
        val equipment = attacker.equipment ?: return
        val arcaneHand = equipment.itemInOffHand
        val bookHand = equipment.itemInMainHand
        // Cooldown
        if (attacker.getCooldown(arcaneHand) > 0) return
        arcaneHand.damage(2, attacker)
        attacker.setCooldown(arcaneHand, 30)
        // Vars
        val damage = 4.0
        val range = 32.0
        val radius = 3.0
        val aimAssist = 0.25
        // Run
        arcaneCircle(attacker, damage, range, radius, aimAssist)
    }

    fun arcaneCircle(user: LivingEntity, damage: Double, range: Double, radius: Double, aimAssist: Double) {
        // Logic
        val circleLocation = getRayTraceLocation(user, range, aimAssist) ?: return
        val damageSource = createEntityDamageSource(user, null, type = DamageType.MAGIC)
        circleLocation.getNearbyLivingEntities(radius).forEach {
            it.damage(damage, damageSource)
        }
        // Particles
        spawnCircleParticles(
            particle = Particle.WITCH,
            center = circleLocation,
            upDirection = Vector(0, 1, 0),
            radius = radius,
            heightOffset = 0.25,
            count = 55)
        user.world.playSound(user.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
    }

    fun arcaneMissileHandler(event: PlayerInteractEvent) {
        // Basic Variables
        val player = event.player
        val equipment = player.equipment ?: return
        val offHand = equipment.itemInOffHand
        val mainHand = equipment.itemInMainHand
        val model = offHand.itemMeta!!.itemModel?.key ?: return
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
        val targetLocation = getRayTraceBlock(player, model) ?: eyeDirection.clone().multiply(48.0).toLocation(player.world)
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
        val offHandEquipment = player.equipment?.itemInOffHand
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
        val offHandEquipment = player.equipment!!.itemInOffHand
        val mainHandEquipment = player.equipment!!.itemInMainHand
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

    private fun getRayTraceLocation(entity: LivingEntity, range: Double, raySize: Double): Location? {
        val entityPredicate = { e: Entity -> e != entity}
        val result = entity.world.rayTrace(
            entity.eyeLocation, entity.eyeLocation.direction, range,
            FluidCollisionMode.NEVER, true, raySize, entityPredicate)
        if (result == null) return null
        return result.hitEntity?.location ?: result.hitBlock?.location?.add(0.0, 1.0, 0.0)
    }

    private fun getRayTraceEntity(entity: LivingEntity, range: Double, raySize: Double): Entity? {
        val entityPredicate = { e: Entity -> e != entity}
        val result = entity.world.rayTrace(
            entity.eyeLocation, entity.eyeLocation.direction, range,
            FluidCollisionMode.NEVER, true, raySize, entityPredicate)
        val target = result?.hitEntity ?: return null
        val distance = entity.eyeLocation.distance(target.location)
        if (range < distance) return null
        return target
    }

    private fun getRayTraceBlock(player: Player, model: String): Location? {
        val reach = ARCANE_RANGES[model] ?: return null
        val result = player.rayTraceBlocks(reach, FluidCollisionMode.NEVER) ?: return null
        val location = result.hitPosition.toLocation(player.world)
        return location
    }

}