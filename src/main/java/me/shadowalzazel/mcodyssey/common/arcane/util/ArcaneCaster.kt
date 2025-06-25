package me.shadowalzazel.mcodyssey.common.arcane.util

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * This caster can be an ENTITY or a BLOCK
 */
class ArcaneCaster(
    val entityCaster: Entity? = null,
    val blockCaster: Block? = null
) {

    val isBlock: Boolean
    val isEntity: Boolean

    init {
        isEntity = entityCaster != null
        isBlock = blockCaster != null
    }

    fun convertToTarget(): ArcaneTarget {
        return if (isBlock) {
            ArcaneTarget(blockTarget = blockCaster)
        } else {
            ArcaneTarget(entityTarget = entityCaster!!)
        }
    }

    fun getLocation(): Location {
        return if (isEntity) {
            entityCaster!!.location
        }
        else { // isBlock
            blockCaster!!.location.toCenterLocation()
        }
    }

    fun toEntityList(): List<LivingEntity> {
        val entityList = mutableListOf<LivingEntity>()
        if (entityCaster is LivingEntity) entityList.add(entityCaster)
        return entityList
    }

}