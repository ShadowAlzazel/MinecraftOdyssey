package me.shadowalzazel.mcodyssey.util

import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

interface WeaponHelper {

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


}