package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.common.boss.BossManager
import me.shadowalzazel.mcodyssey.common.mobs.MobFactory
import me.shadowalzazel.mcodyssey.common.mobs.SpawnProfiles
import me.shadowalzazel.mcodyssey.common.mobs.SpawnRegistry
import me.shadowalzazel.mcodyssey.util.StructureHelper
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.entity.Creeper
import org.bukkit.entity.Enemy
import org.bukkit.entity.Guardian
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Phantom
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent

/**
 * Pure event plumbing. Every "what does a mob get" decision lives in [SpawnProfiles].
 */
object SpawningListeners : Listener, MobFactory, StructureHelper, RegistryTagManager {

    /** Spawner-driven structure spawns. Runs first so NATURAL handling can bail out. */
    @EventHandler(priority = EventPriority.LOWEST)
    fun onStructureSpawn(event: CreatureSpawnEvent) {
        val mob = event.entity
        if (mob.isSpawnHandled) return

        val profile = mob.structureKeys().firstNotNullOfOrNull(SpawnRegistry::spawner) ?: return
        profile.applyTo(mob, this)

        // TODO: Temporary method
        if (mob.scoreboardTags.contains("odyssey.hog_rider")) {
            BossManager.summon(Odyssey.instance, "hog_rider", mob.location)
            mob.remove()
        }
    }

    /** Natural spawns: structure loot rules, elites, edge-world buffs. */
    @EventHandler(priority = EventPriority.LOW)
    fun onNaturalSpawn(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) return

        val mob = event.entity
        if (!mob.isNaturalSpawnCandidate) return
        if (mob.isSpawnHandled) return

        mob.structureKeys().mapNotNull(SpawnRegistry::natural).forEach { it.applyTo(mob) }

        mobEliteHandler(event)

        if (mob.location.world == Odyssey.instance.edge) SpawnProfiles.EDGE.applyTo(mob)
    }

    /** Structure chunks generating for the first time: duplicate the hand-placed guards. */
    @EventHandler(priority = EventPriority.LOWEST)
    fun onStructurePopulate(event: ChunkPopulateEvent) {
        val structures = event.chunk.structures.ifEmpty { return }
        val registry = getPaperRegistry(RegistryKey.STRUCTURE)
        val mobs = event.chunk.entities.filterIsInstance<LivingEntity>()
        if (mobs.isEmpty()) return

        for (placed in structures) {
            val key = registry.getKey(placed.structure)?.key ?: continue
            val profile = SpawnRegistry.populate(key) ?: continue
            profile.populate(mobs.filter { entityInsideStructure(it, placed.structure) }, this)
        }
    }

    /* ---------- helpers ---------- */

    /**
     * The `getBoundedStructures -> registry.getKey -> when (name)` dance appeared twice.
     * Now it's one lazy sequence and the `when` is a map lookup.
     */
    private fun LivingEntity.structureKeys(): Sequence<String> {
        val bounded = getBoundedStructures(this) ?: return emptySequence()
        val registry = getPaperRegistry(RegistryKey.STRUCTURE)
        return bounded.asSequence().mapNotNull { registry.getKey(it)?.key }
    }

    private val LivingEntity.isSpawnHandled: Boolean
        get() = scoreboardTags.contains(EntityTags.SPAWN_HANDLED)

    private val LivingEntity.isNaturalSpawnCandidate: Boolean
        get() = this is Enemy && this !is Creeper && this !is Phantom && this !is Guardian
}