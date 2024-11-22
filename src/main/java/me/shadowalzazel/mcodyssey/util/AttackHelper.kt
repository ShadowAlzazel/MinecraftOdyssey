package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import kotlin.math.pow


interface AttackHelper {

    @Suppress("UnstableApiUsage")
    fun doWeaponAOESweep(attacker: LivingEntity, victim: LivingEntity, damage: Double, angle: Double, radius: Double? = null) {
        val sweepRadius = radius ?: attacker.location.distance(victim.location)
        val targets = attacker.getNearbyEntities(sweepRadius, sweepRadius, sweepRadius).filter {
            it != victim && it != attacker && it is LivingEntity
        }
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
            val damageSource = createPlayerDamageSource(attacker)
            entity.damage(damage, damageSource)
            entity.world.spawnParticle(Particle.SWEEP_ATTACK, entity.location, 1, 0.0, 0.0, 0.0)
        }
        attacker.world.spawnParticle(Particle.ENCHANTED_HIT, attacker.location, 12, 0.1, 0.1, 0.1)
        attacker.world.playSound(victim.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.75F, 0.5F)
    }

    fun getWeaponAttack(item: ItemStack): Double {
        var damage = 0.0
        val attackModifiers = item.itemMeta.attributeModifiers?.get(Attribute.ATTACK_DAMAGE)
        if (attackModifiers != null) {
            for (modifier in attackModifiers) { // FIX LATER
                damage += modifier.amount
            }
        }
        if (damage <= 1) {
            damage = 1.0
        }
        return damage
    }

    @Suppress("UnstableApiUsage")
    fun createPlayerDamageSource(player: Entity, projectile: Entity? = null, type: DamageType = DamageType.PLAYER_ATTACK): DamageSource {
        val sourceBuilder = DamageSource.builder(DamageType.PLAYER_ATTACK)
        sourceBuilder.withCausingEntity(player)
        if (projectile != null) {
            sourceBuilder.withDirectEntity(projectile)
        } else {
            sourceBuilder.withDirectEntity(player)
        }
        val damageSource = sourceBuilder.build()
        return damageSource
    }

    @Suppress("UnstableApiUsage")
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

}