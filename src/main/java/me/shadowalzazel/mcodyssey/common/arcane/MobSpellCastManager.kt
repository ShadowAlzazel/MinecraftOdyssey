package me.shadowalzazel.mcodyssey.common.arcane

import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

/**
 * Periodically casts spells for mobs, without scanning the full mob list every tick.
 *
 * Implementation: a "timing wheel" (same idea Netty's HashedWheelTimer uses). Each
 * registered caster is dropped directly into the future tick-slot it's next due on.
 * The per-tick cost is O(1) + (number of casters actually due *this* tick) -- never
 * O(total registered casters).
 *
 * Not thread-safe -- only call register/unregister/init/shutdown from the main
 * server thread (e.g. from inside event handlers), same as the rest of Bukkit's API.
 */
object MobSpellCastManager {

    /**
     * Number of slots in the wheel = the max delay (in ticks) you can schedule.
     * Must be strictly greater than the largest maxIntervalTicks you ever register with.
     * 200 ticks = 10 seconds at 20 TPS. Bump this if you want e.g. a boss with a
     * 30-second cooldown (600+).
     */
    private const val WHEEL_SIZE = 200

    private val wheel = Array(WHEEL_SIZE) { mutableListOf<CasterEntry>() }
    private var currentSlot = 0
    private var taskId = -1

    /** Entities currently registered. Doubles as the "is this mob still an active caster" check. */
    private val activeEntityIds = HashSet<UUID>()

    enum class MobSpell {
        SCROLL, SPELL_SCROLL, ARCANE_WAND, ARCANE_SCEPTER
    }

    private data class CasterEntry(
        val entityId: UUID,
        val worldName: String,
        val spell: MobSpell,
        val minIntervalTicks: Int,
        val maxIntervalTicks: Int,
        val castRange: Double
    )

    fun init(plugin: JavaPlugin) {
        taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable { tick() }, 1L, 1L).taskId
    }

    fun shutdown() {
        if (taskId != -1) Bukkit.getScheduler().cancelTask(taskId)
        wheel.forEach { it.clear() }
        activeEntityIds.clear()
        taskId = -1
    }

    /**
     * Register a mob as a periodic spell caster. Safe to call again for the same
     * entity -- duplicate registrations are ignored (call unregisterCaster first
     * if you want to change its spell/intervals).
     */
    fun registerCaster(
        entity: LivingEntity,
        spell: MobSpell,
        minIntervalTicks: Int = 60,
        maxIntervalTicks: Int = 140,
        castRange: Double = 16.0
    ) {
        require(maxIntervalTicks < WHEEL_SIZE) {
            "maxIntervalTicks ($maxIntervalTicks) must be less than WHEEL_SIZE ($WHEEL_SIZE), or increase WHEEL_SIZE"
        }
        if (!activeEntityIds.add(entity.uniqueId)) return // already registered

        val entry = CasterEntry(
            entityId = entity.uniqueId,
            worldName = entity.world.name,
            spell = spell,
            minIntervalTicks = minIntervalTicks,
            maxIntervalTicks = maxIntervalTicks,
            castRange = castRange
        )
        scheduleEntry(entry, ThreadLocalRandom.current().nextInt(minIntervalTicks, maxIntervalTicks + 1))
    }

    /** Stop a mob from casting. It'll be dropped silently the next time it's touched. */
    fun unregisterCaster(entity: LivingEntity) {
        activeEntityIds.remove(entity.uniqueId)
    }

    private fun scheduleEntry(entry: CasterEntry, delayTicks: Int) {
        val slot = (currentSlot + delayTicks.coerceAtLeast(1)) % WHEEL_SIZE
        wheel[slot].add(entry)
    }

    private fun tick() {
        val due = wheel[currentSlot]
        if (due.isNotEmpty()) {
            // Snapshot + clear up front: processEntry() may re-add entries into this
            // same slot on a future lap, and we don't want to mutate the list we're iterating.
            val toProcess = ArrayList(due)
            due.clear()
            for (entry in toProcess) processEntry(entry)
        }
        currentSlot = (currentSlot + 1) % WHEEL_SIZE
    }

    private fun processEntry(entry: CasterEntry) {
        if (entry.entityId !in activeEntityIds) return // unregistered, drop it

        val world = Bukkit.getWorld(entry.worldName)
        val entity = Bukkit.getEntity(entry.entityId) as? LivingEntity

        if (world == null || entity == null || !entity.isValid || entity.isDead) {
            activeEntityIds.remove(entry.entityId) // dead/removed, drop it
            return
        }

        // Cheap guard: don't do real work (nearby-entity search, casting) for mobs
        // sitting in unloaded/non-ticking chunks. Just requeue and check again later.
        if (!entity.location.chunk.isLoaded) {
            scheduleEntry(entry, entry.minIntervalTicks)
            return
        }

        // Only actually cast if there's a player worth casting at nearby.
        val hasTarget = entity.getNearbyEntities(entry.castRange, entry.castRange, entry.castRange)
            .any { it is Player }

        if (hasTarget) {
            castFor(entity, entry.spell)
        }

        val nextDelay = ThreadLocalRandom.current().nextInt(entry.minIntervalTicks, entry.maxIntervalTicks + 1)
        scheduleEntry(entry, nextDelay)
    }

    private fun castFor(caster: LivingEntity, spell: MobSpell) {
        when (spell) {
            MobSpell.SCROLL, MobSpell.SPELL_SCROLL -> {
                val item = caster.equipment?.itemInMainHand ?: return
                ArcaneSpellItems.castScroll(caster, item)
            }
            // NOTE: castBuiltInWand/castBuiltInScepter take a Player in your snippet.
            // Widen their parameter type to LivingEntity (like castScroll already is)
            // for these two branches to compile.
            MobSpell.ARCANE_WAND -> ArcaneSpellItems.castBuiltInWand(caster)
            MobSpell.ARCANE_SCEPTER -> ArcaneSpellItems.castBuiltInScepter(caster)
        }
    }
}