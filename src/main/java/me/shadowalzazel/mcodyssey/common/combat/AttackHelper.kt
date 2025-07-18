package me.shadowalzazel.mcodyssey.common.combat

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.pow

@Suppress("UnstableApiUsage")
interface AttackHelper {

    fun doWeaponAOESweep(attacker: LivingEntity, victim: LivingEntity, damage: Double, angle: Double, radius: Double? = null) {
        val sweepRadius = radius ?: attacker.location.distance(victim.location)
        val targets = attacker.getNearbyEntities(sweepRadius, sweepRadius, sweepRadius).filter {
            it != victim && it != attacker && it is LivingEntity
        }.toMutableList()
        // Remove passengers
        targets.removeAll { it.passengers.contains(attacker) }

        val originDirection = victim.location.subtract(attacker.location).toVector()
        victim.scoreboardTags.add(EntityTags.HIT_BY_AOE_SWEEP)
        for (entity in targets) {
            if (entity.scoreboardTags.contains(EntityTags.HIT_BY_AOE_SWEEP)) continue
            if (entity !is LivingEntity) continue
            // Get attack angle
            val entityDirection = entity.location.subtract(attacker.location).toVector()
            val attackAngle = originDirection.angle(entityDirection)
            // Make sure to follow angle
            if (attackAngle > angle) continue
            entity.addScoreboardTag(EntityTags.HIT_BY_AOE_SWEEP)
            // Source
            val damageSource = createEntityDamageSource(attacker, null, DamageType.PLAYER_ATTACK)
            entity.damage(damage, damageSource)
            entity.world.spawnParticle(Particle.SWEEP_ATTACK, entity.location, 1, 0.0, 0.0, 0.0)
        }
        attacker.world.spawnParticle(Particle.ENCHANTED_HIT, attacker.location, 12, 0.1, 0.1, 0.1)
        attacker.world.playSound(victim.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.75F, 0.5F)
    }


    fun getEntitiesInArc(origin: LivingEntity, angle: Double, radius: Double): List<LivingEntity> {
        val originDirection = origin.eyeLocation.direction.clone()
        val nearby = origin.world.getNearbyLivingEntities(origin.location, radius + 0.25).filter {
            it != origin && it is LivingEntity
        }
        val entitiesInArc: MutableList<LivingEntity> = mutableListOf()
        for (entity in nearby) {
            if (entity !is LivingEntity) continue
            // Get angle
            val entityDirection = entity.location.subtract(origin.location).toVector()
            val deltaAngle = originDirection.angle(entityDirection)
            // Check if angle outside range
            if (deltaAngle > angle) continue
            entitiesInArc.add(entity)
        }
        return entitiesInArc
    }


    fun getWeaponAttack(item: ItemStack): Double {
        var damage = 0.0
        val attackModifiers = item.itemMeta.attributeModifiers?.get(Attribute.ATTACK_DAMAGE)
        if (!attackModifiers.isNullOrEmpty()) {
            for (modifier in attackModifiers) {
                damage += modifier.amount
            }
        }
        if (damage <= 1) {
            damage = 1.0
        }
        return damage
    }


    fun createEntityDamageSource(entity: Entity, projectile: Entity? = null, type: DamageType): DamageSource {
        val sourceBuilder = DamageSource.builder(type)
        sourceBuilder.withCausingEntity(entity)
        if (projectile != null) {
            sourceBuilder.withDirectEntity(projectile)
        } else {
            sourceBuilder.withDirectEntity(entity)
        }
        val damageSource = sourceBuilder.build()
        return damageSource
    }


    fun explosionHandler(entities: Collection<LivingEntity>, center: Location, radius: Double) {
        if (radius < 0) return
        center.world.spawnParticle(Particle.EXPLOSION, center, 1, 0.0, 0.04, 0.0)
        center.world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 1.2F)
        for (entity in entities) {
            // indirect distance square
            val distance = entity.location.distance(center)
            val power = (maxOf(radius - distance, 0.0)).pow(2.0) + (maxOf(radius - distance, 0.0)).times(1) + (radius * 0.5)
            val damageSource = DamageSource.builder(DamageType.EXPLOSION).build()
            entity.damage(power, damageSource) // Create Damage Source
        }

    }

    fun getRayTraceTarget(player: Player, weapon: String): Entity? {
        val reach = WeaponMaps.REACH_MAP[weapon] ?: return null
        val result = player.rayTraceEntities(reach.toInt() + 1) ?: return null
        val target = result.hitEntity ?: return null
        val closestDistance = if (target is LivingEntity) {
            minOf(player.location.distance(target.location), player.eyeLocation.distance(target.location)) }
        else {
            player.location.distance(target.location)
        }
        if (reach < closestDistance) { return null }
        return target
    }

    fun dualWieldAttack(player: Player, target: LivingEntity) {
        with(player.equipment!!) {
            val mainHand = itemInMainHand.clone()
            val offHand = itemInOffHand.clone()
            setItemInOffHand(mainHand)
            setItemInMainHand(offHand)
            player.attack(target)
            setItemInMainHand(mainHand)
            setItemInOffHand(offHand)
        }
        player.resetCooldown()
        player.swingOffHand()
    }

}