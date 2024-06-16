package me.shadowalzazel.mcodyssey.mobs.base

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.items.Runesherds.addHealthAttribute
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity


open class OdysseyMob(
    internal val displayName: String,
    private val tagName: String,
    private val type: EntityType,
    private val bonusHealth: Double) {

    // Custom Entity Type -> custom model
    open fun createMob(world: World, location: Location): Entity {
        val mob = world.spawnEntity(location, type).apply {
            customName(Component.text(displayName))
            addScoreboardTag(EntityTags.ODYSSEY_MOB)
            addScoreboardTag(tagName)
            // Health
            if (this is LivingEntity) {
                // Extra mob health
                addHealthAttribute(bonusHealth)
                health += (bonusHealth - 1)
            }
        }
        return mob
    }

}