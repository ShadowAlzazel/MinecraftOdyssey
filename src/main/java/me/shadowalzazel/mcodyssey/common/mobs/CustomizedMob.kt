package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

open class CustomizedMob(
    private val name: String,
    private val entityType: EntityType
) {

    open fun createMob(world: World, location: Location): Entity {
        return world.spawnEntity(location, this.entityType).apply {
            addScoreboardTag(EntityTags.ODYSSEY_MOB)
            addScoreboardTag("odyssey.mob.$name")
        }
    }


}