package me.shadowalzazel.mcodyssey.common.arcane.util

import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

interface RayTracerAndDetector {

    fun getRayTraceLocation(
        entity: LivingEntity,
        range: Double,
        raySize: Double): Location? {
        val entityPredicate = { e: Entity -> e != entity}
        // TODO: start and end locations AND Nullable entity
        val result = entity.world.rayTrace(
            entity.eyeLocation, entity.eyeLocation.direction, range,
            FluidCollisionMode.NEVER, true, raySize, entityPredicate)
        if (result == null) return null
        return result.hitEntity?.location ?: result.hitBlock?.location?.add(0.0, 1.0, 0.0)
    }

    fun getRayTraceEntity(entity: LivingEntity, range: Double, raySize: Double): Entity? {
        val entityPredicate = { e: Entity -> e != entity}
        val result = entity.world.rayTrace(
            entity.eyeLocation, entity.eyeLocation.direction, range,
            FluidCollisionMode.NEVER, true, raySize, entityPredicate)
        val target = result?.hitEntity ?: return null
        val distance = entity.eyeLocation.distance(target.location)
        if (range < distance) return null
        return target
    }

    fun getRayTraceBlock(player: Player, model: String): Location? {
        val reach = WeaponMaps.ARCANE_RANGES[model] ?: return null
        val result = player.rayTraceBlocks(reach, FluidCollisionMode.NEVER) ?: return null
        val location = result.hitPosition.toLocation(player.world)
        return location
    }

}