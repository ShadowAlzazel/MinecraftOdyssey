package me.shadowalzazel.mcodyssey.util

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

    fun getWeaponAttack(item: ItemStack): Double {
        var damage = 0.0
        val attackModifiers = item.itemMeta.attributeModifiers?.get(Attribute.GENERIC_ATTACK_DAMAGE)
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
    fun createPlayerDamageSource(player: Entity, projectile: Entity? = null): DamageSource {
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