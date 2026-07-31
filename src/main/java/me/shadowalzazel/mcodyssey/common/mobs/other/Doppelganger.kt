package me.shadowalzazel.mcodyssey.common.mobs.other

import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Pig


object Doppelganger : OdysseyMob(
    "Doppelganger",
    "doppelganger",
    EntityType.PLAYER
) {

    override fun spawn(world: World, location: Location): Entity {
        val doppelganger = (spawnBase(world, location) as HumanEntity).apply {

        }
        return doppelganger
    }



}