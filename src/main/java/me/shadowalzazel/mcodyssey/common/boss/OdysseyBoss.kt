package me.shadowalzazel.mcodyssey.common.boss

import me.shadowalzazel.mcodyssey.util.AttributeManager
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.UUID


/**
 * Base class for every Odyssey boss.
 *
 * A subclass only has to:
 *   1. supply [stats]
 *   2. implement [createEntity] (spawn + configure the mob)
 *   3. override the behaviour hooks it cares about (onSpawn / onActivate / etc.)
 *
 * The base handles the entity handle, the state machine, task cleanup,
 * the weighted attack loop, and dialogue plumbing.
 */
abstract class OdysseyBoss(
    val plugin: JavaPlugin,
    val key: String,
    val displayName: Component,
) {
    /** The living entity representing this boss in the world. */
    lateinit var entity: LivingEntity
        protected set

    var state: BossState = BossState.DORMANT
        protected set

    /** Id for this *instance* (survives even if the entity is replaced). */
    val instanceId: UUID = UUID.randomUUID()

    /** Optional dialogue pack. Wire one up in the subclass to get [announce]. */
    protected open val dialogue: Dialogue? = null

    private val tasks = mutableListOf<BukkitTask>()
    private val attacks = AttackScheduler(this)

    abstract val stats: BossStats

    val world get() = entity.world
    val location: Location get() = entity.location
    val isEntityReady get() = ::entity.isInitialized
    val isAlive get() = isEntityReady && !entity.isDead && state != BossState.DEPARTED

    // ------------------------------------------------------------------ lifecycle

    /** Spawn + configure the mob. Called once from [spawn]. */
    protected abstract fun createEntity(location: Location): LivingEntity

    /** Provide the boss's weighted attack rotation. Empty = no auto attacks. */
    protected open fun attackOptions(): List<AttackOption> = emptyList()

    open fun spawn(location: Location) {
        entity = createEntity(location)
        state = BossState.SPAWNED
        onSpawn(location)
        // Never let a forgotten boss linger forever.
        runLater(stats.despawnAfterTicks) { if (state != BossState.DEFEATED) depart() }
    }

    /** Flip into combat and start the attack loop. */
    open fun activate() {
        if (state == BossState.ACTIVE || !isAlive) return
        state = BossState.ACTIVE
        onActivate()
        attacks.start(stats.attackPeriodTicks) { attackOptions() }
    }

    open fun defeat(killer: Player?) {
        if (state == BossState.DEFEATED || state == BossState.DEPARTED) return
        state = BossState.DEFEATED
        onDefeat(killer)
        cleanup()
    }

    open fun depart() {
        if (state == BossState.DEFEATED || state == BossState.DEPARTED) return
        state = BossState.DEPARTED
        onDepart()
        if (isEntityReady) entity.remove()
        cleanup()
    }

    private fun cleanup() {
        attacks.stop()
        tasks.forEach { it.cancel() }
        tasks.clear()
        BossManager.forget(this)
    }

    // ------------------------------------------------------------- behaviour hooks

    protected open fun onSpawn(location: Location) {}
    protected open fun onActivate() {}
    protected open fun onDefeat(killer: Player?) {}
    protected open fun onDepart() {}

    /** Routed here by [BossListener] whenever the boss takes damage. */
    open fun onDamaged(source: Entity?, amount: Double) {}

    // -------------------------------------------------------------------- helpers

    fun nearbyPlayers(radius: Double = stats.activationRadius): List<Player> =
        entity.getNearbyEntities(radius, radius, radius).filterIsInstance<Player>()

    fun playSoundNearby(sound: Sound, volume: Float = 1f, pitch: Float = 1f, radius: Double = stats.activationRadius) {
        nearbyPlayers(radius).forEach { it.playSound(it.location, sound, volume, pitch) }
    }

    fun randomOffsetLocation(spread: Int): Location =
        location.clone().add((-spread..spread).random().toDouble(), 0.0, (-spread..spread).random().toDouble())

    /** Send a dialogue line (if a [dialogue] pack is present) to an audience. */
    fun announce(line: DialogueKey, audience: Collection<Player> = nearbyPlayers(), vars: Map<String, String> = emptyMap()) {
        dialogue?.send(audience, line, vars)
    }

    /** Standard attribute setup. Adjust the [Attribute] keys to match your MC mappings. */
    protected fun applyBaseAttributes() {
        entity.getAttribute(Attribute.MAX_HEALTH)?.baseValue = stats.maxHealth
        entity.getAttribute(Attribute.ARMOR)?.baseValue = stats.armor
        entity.health = stats.maxHealth
    }

    // --------------------------------------------------- task management (auto-cancelled)

    fun runLater(delayTicks: Long, action: () -> Unit) {
        tasks += plugin.server.scheduler.runTaskLater(plugin, Runnable { action() }, delayTicks)
    }

    fun runTimer(delayTicks: Long, periodTicks: Long, action: () -> Unit): BukkitTask =
        plugin.server.scheduler.runTaskTimer(plugin, Runnable { action() }, delayTicks, periodTicks)
            .also { tasks += it }
}