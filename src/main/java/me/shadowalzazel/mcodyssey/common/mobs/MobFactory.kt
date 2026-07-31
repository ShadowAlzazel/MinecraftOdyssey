package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.util.AttributeManager
import org.bukkit.entity.*

/**
 * Anything that can decorate a mob. The listener implements this; archetypes receive it as a
 * receiver so they can call `createArmoredMob`, `createShinyMob`, `updateToolTip`, etc.
 * without each archetype having to inherit the whole interface soup.
 */
interface MobFactory : MobEliteMaker, AttributeManager {

    companion object {
        val factory: MobFactory = object : MobFactory  {
            // TODO: REDO MobEliteMaker
            // Inherit from MORE classes and not just a line of classes
        }
    }

    /**
     * Generic way to add an archetype to a mob.
     *
     * This method takes in a mob and uses a pre-existing archetype to in [MobArchetypes]
     * to modify can apply the archetype stats and profile to the mob.
     */
    fun applyArchetype(mob: LivingEntity, archetype: MobArchetype) {
        val matched = archetype.matches(mob)
        if (!matched) return
        // Get decorate and use default factory
        archetype.applyTo(mob, this@MobFactory)
        // Apply stats
        archetype.stats.applyTo(mob)
    }
}

/**
 * A *kind* of mob, independent of where it spawned.
 *
 * `predicate` decides whether the archetype applies, `decorate` handles equipment and any
 * bespoke logic, `stats` is the declarative attribute bundle. Structure-specific buffs do
 * NOT live here — see [StructureSpawnProfile].
 */
data class MobArchetype(
    val id: String,
    val predicate: (LivingEntity) -> Boolean,
    val stats: StatProfile = StatProfile.EMPTY,
    val decorate: MobFactory.(LivingEntity) -> Unit = {},
) {
    fun matches(mob: LivingEntity) = predicate(mob)

    fun applyTo(mob: LivingEntity, factory: MobFactory) {
        factory.decorate(mob)
        stats.applyTo(mob)
    }

    /** Narrow an existing archetype, e.g. `GIANT.onlyIf { it is Spider }`. */
    fun onlyIf(extra: (LivingEntity) -> Boolean) =
        copy(predicate = { predicate(it) && extra(it) })
}

/* ---------- predicate helpers ---------- */

fun hasTag(tag: String): (LivingEntity) -> Boolean = { it.scoreboardTags.contains(tag) }
inline fun <reified T> isA(): (LivingEntity) -> Boolean = { it is T }
val UNTAGGED: (LivingEntity) -> Boolean = { it.scoreboardTags.isEmpty() }