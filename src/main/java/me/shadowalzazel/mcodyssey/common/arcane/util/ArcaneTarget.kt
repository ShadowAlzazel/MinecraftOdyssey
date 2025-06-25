package me.shadowalzazel.mcodyssey.common.arcane.util

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * This target can be an ENTITY or a BLOCK
 */
class ArcaneTarget(
    val entityTarget: Entity? = null,
    val blockTarget: Block? = null
) {

    val isBlock: Boolean
    val isEntity: Boolean

    init {
        isEntity = entityTarget != null
        isBlock = blockTarget != null
    }


    fun getLocation(): Location {
        return if (isEntity) {
            entityTarget!!.location
        }
        else { // isBlock
            blockTarget!!.location
        }

    }

    fun toEntityList(): List<LivingEntity> {
        val entityList = mutableListOf<LivingEntity>()
        if (entityTarget is LivingEntity) entityList.add(entityTarget)
        return entityList
    }

}