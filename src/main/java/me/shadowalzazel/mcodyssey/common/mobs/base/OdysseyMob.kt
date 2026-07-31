package me.shadowalzazel.mcodyssey.common.mobs.base

import me.shadowalzazel.mcodyssey.common.mobs.MobEliteMaker
import me.shadowalzazel.mcodyssey.common.mobs.StatProfile
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

/**
 * Base for one-off mobs with real, bespoke spawn logic (riders, scheduled tasks,
 * custom hitboxes, whatever) that can't be reduced to "decorate an existing entity."
 *
 * If a mob is *just* stats + equipment, it's not an OdysseyMob — it's a MobArchetype.
 */
abstract class OdysseyMob(
    protected val displayName: String,
    protected val tagName: String,
    protected val type: EntityType,
    protected val stats: StatProfile = StatProfile.EMPTY,
) : MobEliteMaker {

    /**
     * Handles the boring shared part: raw spawn, tags, name, base stats.
     * Reuses the exact same StatProfile plumbing archetypes use, so a special
     * mob's base stats behave identically to an archetype's.
     * Subclasses call this first, then layer their real behavior on top.
     */
    protected fun spawnBase(world: World, location: Location): Entity {
        return world.spawnEntity(location, type).apply {
            addScoreboardTag(EntityTags.ODYSSEY_MOB)
            addScoreboardTag("odyssey.$tagName")
            if (this is LivingEntity) {
                customName(Component.text(displayName, TextColor.color(255, 170, 75)))
                isCustomNameVisible = true
                stats.applyTo(this)
            }
        }
    }

    /** Each special mob owns its full spawn sequence. */
    abstract fun spawn(world: World, location: Location): Entity
}