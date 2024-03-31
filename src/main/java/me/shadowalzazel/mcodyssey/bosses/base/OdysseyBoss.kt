package me.shadowalzazel.mcodyssey.bosses.base

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

open class OdysseyBoss(
    val name: String,
    val type: EntityType
) {

    //protected val vailPrefix = Component.text("[Vail] ", TextColor.color(255, 170, 0), TextDecoration.OBFUSCATED)
    internal open var isActive: Boolean = false
    internal open var hasRemove: Boolean = false
    internal open lateinit var entity: Entity

    open fun spawnBossEntity(location: Location): Entity {
        return location.world.spawnEntity(location, type)
    }

}