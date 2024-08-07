package me.shadowalzazel.mcodyssey.mobs.base

import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.MobCreationHelper
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity


open class OdysseyMob(
    internal val displayName: String,
    private val tagName: String,
    private val type: EntityType,
    private val bonusHealth: Double) : MobCreationHelper {

    // Custom Entity Type -> custom model
    open fun createMob(world: World, location: Location): Entity {
        return world.spawnEntity(location, type).apply {
            // Tags
            addScoreboardTag(EntityTags.ODYSSEY_MOB)
            addScoreboardTag("odyssey.$tagName")
            // Health
            if (this is LivingEntity) {
                addHealthAttribute(bonusHealth, AttributeTags.MOB_HEALTH)
                heal(bonusHealth)
            }
        }
    }

}