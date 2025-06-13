package me.shadowalzazel.mcodyssey.common.arcane.util

import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

interface RayTracerAndDetector {

    // ---------- Newer --------

    fun getEntityRayTrace(
        start: Location,
        direction: Vector,
        filter: List<LivingEntity>,
        range: Double,
        raySize: Double): Entity? {
        val entityPredicate = { e: Entity -> e !in filter}
        val result = start.world.rayTrace(
            start,
            direction,
            range,
            FluidCollisionMode.NEVER,
            true,
            raySize,
            entityPredicate)
        // target
        val target = result?.hitEntity ?: return null
        val distance = start.distance(target.location)
        if (range < distance) return null
        return target
    }

    fun getHitLocationRayTrace(
        start: Location,
        direction: Vector,
        filter: List<LivingEntity>,
        maxRange: Double,
        raySize: Double): Location? {
        // Setup
        val filterPredicate = { e: Entity -> e !in filter}
        // Do a ray trace for entities first
        val entity = getEntityRayTrace(
            start,
            direction,
            filter,
            maxRange,
            raySize)
        if (entity is LivingEntity) {
            return entity.location
        } else {
            val rayTraceBlock = start.world.rayTraceBlocks(
                start,
                direction,
                maxRange,
                FluidCollisionMode.NEVER)?.hitBlock
            return if (rayTraceBlock != null) {
                rayTraceBlock.location.toCenterLocation().add(0.0, 0.5, 0.0)
            } else {
                start.clone().add(direction.clone().normalize().multiply(maxRange))
            }
        }

    }

    @Deprecated("Not Used")
    fun oldHitRayTrace(
        start: Location,
        direction: Vector,
        filter: List<LivingEntity>,
        range: Double,
        raySize: Double): Location? {
        // Setup
        val entityPredicate = { e: Entity -> e !in filter}
        // Do a ray trace for entities first
        val result = start.world.rayTrace(
            start,
            direction,
            range,
            FluidCollisionMode.NEVER,
            true,
            raySize,
            entityPredicate)

        if (result == null) return null
        return result.hitEntity?.location ?: result.hitBlock?.location?.add(0.0, 1.0, 0.0)
    }

    @Deprecated("Is bad kek")
    fun oldBlockRayTrace(player: Player, model: String): Location? {
        val reach = WeaponMaps.ARCANE_RANGES[model] ?: return null
        val result = player.rayTraceBlocks(reach, FluidCollisionMode.NEVER) ?: return null
        val location = result.hitPosition.toLocation(player.world)
        return location
    }

}