package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.entity.Illager
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Vindicator
import org.bukkit.loot.Lootable
import java.util.concurrent.ThreadLocalRandom

private fun chance(percent: Int) = ThreadLocalRandom.current().nextInt(100) < percent

// ──────────────────────────────────────────────────────────────────────────────
// ────────────────────── SPAWN CLASS DEFINITIONS ───────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────

/**
 * What happens to a mob spawned by a spawner *inside* a given structure.
 *
 * Order of operations mirrors the original:
 *   1. every matching archetype runs (they can stack: a trial elite can also be a giant)
 *   2. if none matched, the fallback runs
 *   3. handler tags go on
 *   4. the structure-wide baseline buff lands on top
 *   5. factory is a generic object that has the interface [MobFactory]
 */
class StructureSpawnProfile(
    val key: String,
    private val archetypes: List<MobArchetype> = emptyList(),
    private val fallback: MobArchetype? = null,
    private val baseline: StatProfile = StatProfile.EMPTY,
    private val tags: List<String> = listOf(EntityTags.SPAWN_HANDLED),
) {
    fun applyTo(mob: LivingEntity, factory: MobFactory) {
        val matched = archetypes.filter { it.matches(mob) }
        val applied = matched.ifEmpty { listOfNotNull(fallback?.takeIf { it.matches(mob) }) }
        applied.forEach { it.applyTo(mob, factory) }

        tags.forEach(mob::addScoreboardTag)
        baseline.applyTo(mob)
    }
}

/** Natural spawns inside a structure: tag it, maybe reroll its loot table. */
class NaturalSpawnProfile(
    val key: String,
    private val tag: String,
    private val lootTablePath: String,
    private val lootTableChance: Int = 75,
) {
    fun applyTo(mob: LivingEntity) {
        mob.addScoreboardTag(tag)
        if (mob is Lootable && chance(lootTableChance)) {
            val lootTable = LootTableManager.getResourceLootTable(lootTablePath)
            if (lootTable != null) {
                mob.lootTable = lootTable
            }
        }
    }
}

/**
 * What happens when a structure chunk first generates: clone the hand-placed guards.
 * [bonusChance] is the odds of a third clone for mobs passing [bonusFilter].
 */
class ChunkPopulateProfile<T : LivingEntity>(
    val key: String,
    private val type: Class<T>,
    private val bonusChance: Int,
    private val bonusFilter: (T) -> Boolean,
    private val clone: MobFactory.(T) -> Unit,
) {
    fun populate(candidates: List<LivingEntity>, factory: MobFactory) {
        candidates.filterIsInstance(type)
            .filterNot { it.scoreboardTags.contains(EntityTags.CLONED) }
            .forEach { mob ->
                factory.clone(mob)
                if (bonusFilter(mob) && chance(bonusChance)) factory.clone(mob)
            }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// ───────────────────────── SPAWN PROFILE DATA ─────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────

object SpawnProfiles {

    // ──────────────────────────────────────────────────────────────────────────────
    // spawner-driven structures

    val SHADOW_CHAMBERS = StructureSpawnProfile(
        key = "shadow_chambers",
        archetypes = listOf(
            MobArchetypes.TRIAL_ELITE_SHINY,
            MobArchetypes.GIANT,
            MobArchetypes.VANGUARD,
        ),
        fallback = MobArchetypes.SHADOW_BASIC,
        tags = listOf(EntityTags.SPAWN_HANDLED, EntityTags.SHADOW_MOB),
        // TODO: +20% per 16 blocks of dungeon depth
        baseline = statProfile {
            health(20.0, AttributeTags.SHADOW_CHAMBERS_HEALTH_BONUS)
            attack(3.0, AttributeTags.SHADOW_CHAMBERS_ATTACK_BONUS)
            armor(2.0, AttributeTags.SHADOW_CHAMBERS_ARMOR_BONUS)
            speed(0.0325, AttributeTags.SHADOW_CHAMBERS_SPEED_BONUS)
            step(0.5, AttributeTags.SHADOW_CHAMBERS_STEP_HEIGHT)
        },
    )

    val TERMINAL_GRID = StructureSpawnProfile(
        key = "terminal_grid",
        archetypes = listOf(MobArchetypes.creaking(health = 20.0, attack = 8.0, speed = 0.05)),
        fallback = MobArchetypes.TERMINAL_GRID_BASIC,
        baseline = statProfile {
            health(10.0, AttributeTags.TERMINAL_GRID_HEALTH_BONUS)
            attack(1.0, AttributeTags.TERMINAL_GRID_ATTACK_BONUS)
            speed(0.01, AttributeTags.TERMINAL_GRID_SPEED_BONUS)
            step(0.5, AttributeTags.TERMINAL_GRID_STEP_HEIGHT)
        },
    )

    val HYPERCUBIC_CHAMBER = StructureSpawnProfile(
        key = "hypercubic_chamber",
        archetypes = listOf(MobArchetypes.HYPERCUBIC_GUARD),
        baseline = statProfile {
            health(10.0, AttributeTags.HYPERCUBIC_CHAMBER_HEALTH_BONUS)
            attack(2.0, AttributeTags.HYPERCUBIC_CHAMBER_ATTACK_BONUS)
            speed(0.02, AttributeTags.HYPERCUBIC_CHAMBER_SPEED_BONUS)
            step(0.5, AttributeTags.HYPERCUBIC_CHAMBER_STEP_HEIGHT)
        },
    )

    val SUNKEN_LIBRARY = StructureSpawnProfile(
        key = "sunken_library",
        archetypes = listOf(
            MobArchetypes.SUNKEN_GUARD,
            MobArchetypes.creaking(health = 40.0, attack = 8.0),
            MobArchetypes.GIANT_SPIDER,
        ),
        baseline = statProfile {
            health(10.0, AttributeTags.SUNKEN_LIBRARY_HEALTH_BONUS)
            attack(2.0, AttributeTags.SUNKEN_LIBRARY_ATTACK_BONUS)
            armor(2.0, AttributeTags.SUNKEN_LIBRARY_ARMOR_BONUS)
            speed(0.02, AttributeTags.SUNKEN_LIBRARY_SPEED_BONUS)
            step(1.5, AttributeTags.SUNKEN_LIBRARY_STEP_HEIGHT)
        },
    )

    val GILDED_ARENA = StructureSpawnProfile(
        key = "gilded_arena",
        archetypes = listOf(
            MobArchetypes.GILDED_MARAUDER,
            MobArchetypes.BOSS_HOG_RIDER
        ),
    )

    // ──────────────────────────────────────────────────────────────────────────────
    // natural spawns

    val MINESHAFT = NaturalSpawnProfile("mineshaft", EntityTags.IN_MINESHAFT, "structure_spawns/mineshaft")
    val MINESHAFT_MESA = NaturalSpawnProfile("mineshaft_mesa", EntityTags.IN_MINESHAFT, "structure_spawns/mineshaft")
    val SUPERSHAFT = NaturalSpawnProfile("supershaft", EntityTags.IN_SUPERSHAFT, "structure_spawns/supershaft")
    val LINE_MINE = NaturalSpawnProfile("line_mine", EntityTags.IN_LINE_MINE, "structure_spawns/line_mine")

    // ──────────────────────────────────────────────────────────────────────────────
    // chunk populate

    val FORBIDDEN_CASTLE = ChunkPopulateProfile(
        key = "forbidden_castle",
        type = PiglinBrute::class.java,
        bonusChance = 30, // was `(0..10).random() > 3`, i.e. ~64% despite the "30%" comment
        bonusFilter = { it.scoreboardTags.contains("in.knight") },
        clone = { clonePiglinBrute(it) },
    )

    val SANCTUM = ChunkPopulateProfile(
        key = "sanctum",
        type = Illager::class.java,
        bonusChance = 30, // same comment/behaviour mismatch as above
        bonusFilter = { it is Vindicator },
        clone = { cloneIllagerSanctum(it) },
    )

    // ──────────────────────────────────────────────────────────────────────────────
    // world rules

    val EDGE = statProfile {
        attack(3.0, AttributeTags.MOB_EDGE_ATTACK_BONUS)
        health(15.0, AttributeTags.MOB_EDGE_HEALTH_BONUS)
    }
}

/**
 * The single source of truth for "structure key -> what to do".
 * Adding a structure means adding one profile above and one line here — no listener edits.
 */
object SpawnRegistry {
    private val spawner = listOf(
        SpawnProfiles.SHADOW_CHAMBERS,
        SpawnProfiles.TERMINAL_GRID,
        SpawnProfiles.HYPERCUBIC_CHAMBER,
        SpawnProfiles.SUNKEN_LIBRARY,
        SpawnProfiles.GILDED_ARENA,
    ).associateBy { it.key }

    private val natural = listOf(
        SpawnProfiles.MINESHAFT,
        SpawnProfiles.MINESHAFT_MESA,
        SpawnProfiles.SUPERSHAFT,
        SpawnProfiles.LINE_MINE,
    ).associateBy { it.key }

    private val populate = listOf(
        SpawnProfiles.FORBIDDEN_CASTLE,
        SpawnProfiles.SANCTUM,
    ).associateBy { it.key }

    fun spawner(key: String) = spawner[key]
    fun natural(key: String) = natural[key]
    fun populate(key: String) = populate[key]
}