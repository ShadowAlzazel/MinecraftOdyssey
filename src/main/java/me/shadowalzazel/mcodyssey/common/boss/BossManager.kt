package me.shadowalzazel.mcodyssey.common.boss

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID


/** Lifecycle state of a single boss instance. */
enum class BossState {
    DORMANT,
    SPAWNED,
    ACTIVE,
    DEFEATED,
    DEPARTED
}

/**
 * Everything a designer might want to tune without touching behaviour.
 * Give each boss its own [BossStats] and the base class handles the rest.
 */
data class BossStats(
    val maxHealth: Double,
    val armor: Double = 0.0,
    val activationRadius: Double = 32.0,
    val attackRadius: Double = 28.0,
    val attackPeriodTicks: Long = 20L * 10,
    val despawnAfterTicks: Long = 20L * 60 * 120, // 2 hours safety net
)


/**
 * A boss that reacts to items thrown to it (the Ambassador's trading mechanic).
 * Any boss can opt in; the listener wires the pickup event for you.
 */
interface GiftReceiver {
    fun onGift(giver: Player, item: Item)
}

/**
 * Central registry. Register each boss once (usually in onEnable), then summon
 * anywhere by key:
 *
 *     BossManager.register("ambassador") { plugin, loc -> TheAmbassador(plugin, loc) }
 *     BossManager.summon(plugin, "ambassador", someLocation)
 */
object BossManager {

    private val factories = mutableMapOf<String, (JavaPlugin, Location) -> OdysseyBoss>()
    private val active = mutableMapOf<UUID, OdysseyBoss>() // entity uuid -> boss instance

    fun register(key: String, factory: (JavaPlugin, Location) -> OdysseyBoss) {
        factories[key.lowercase()] = factory
    }

    fun summon(plugin: JavaPlugin, key: String, location: Location): OdysseyBoss? {
        val factory = factories[key.lowercase()] ?: return null
        val boss = factory(plugin, location).also { it.spawn(location) }
        active[boss.entity.uniqueId] = boss
        return boss
    }

    fun bossFor(entity: Entity): OdysseyBoss? = active[entity.uniqueId]

    fun activeBosses(): Collection<OdysseyBoss> = active.values.toList()

    fun keys(): List<String> = factories.keys.sorted()

    internal fun forget(boss: OdysseyBoss) {
        if (boss.isEntityReady) active.remove(boss.entity.uniqueId)
    }
}

/**
 * Single listener that turns raw Bukkit events into boss callbacks.
 * Register it once: server.pluginManager.registerEvents(BossListener(), plugin)
 */
class BossListener : Listener {

    @EventHandler
    fun onDamaged(event: EntityDamageByEntityEvent) {
        val boss = BossManager.bossFor(event.entity) ?: return
        boss.onDamaged(event.damager, event.finalDamage)
    }

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val boss = BossManager.bossFor(event.entity) ?: return
        boss.defeat(event.entity.killer)
    }

    @EventHandler
    fun onGiftPickup(event: EntityPickupItemEvent) {
        val boss = BossManager.bossFor(event.entity) as? GiftReceiver ?: return
        // The Item entity remembers who threw it; that's our "giver".
        val giverId = event.item.thrower ?: return
        val giver = event.entity.server.getPlayer(giverId) ?: return
        event.isCancelled = true
        boss.onGift(giver, event.item)
    }
}