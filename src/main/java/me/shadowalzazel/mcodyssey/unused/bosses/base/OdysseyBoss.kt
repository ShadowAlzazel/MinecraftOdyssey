package me.shadowalzazel.mcodyssey.unused.bosses.base

import me.shadowalzazel.mcodyssey.util.AttributeManager
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

open class OdysseyBoss(
    val name: String,
    val type: EntityType
) : AttributeManager {

    //protected val vailPrefix = Component.text("[Vail] ", TextColor.color(255, 170, 0), TextDecoration.OBFUSCATED)
    internal open var isActive: Boolean = false
    internal open var hasRemove: Boolean = false
    internal open lateinit var entity: Entity

    open fun spawnBossEntity(location: Location): Entity {
        return location.world.spawnEntity(location, type)
    }

}