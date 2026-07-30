package me.shadowalzazel.mcodyssey.common.boss

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * Everything an attack needs to run, decoupled from any specific boss.
 * Because an attack only ever touches [AttackContext], the same attack object
 * can be reused by any boss (or fired manually anywhere).
 */
class AttackContext(
    val plugin: JavaPlugin,
    val source: LivingEntity,
    val world: World,
    val origin: Location,        // where the attack is centred
    val targets: List<Player>,   // the players this cast should primarily hit
    val nearby: List<Player>,    // everyone in range (for wider AoE / effects)
)

/** A reusable, self-contained boss attack. */
fun interface BossAttack {
    fun execute(ctx: AttackContext)
}

/** How the scheduler picks where an attack lands. */
enum class TargetMode {
    RANDOM_PLAYER,
    ALL_PLAYERS,
    SELF
}

/**
 * One entry in a boss's attack rotation: an attack, how heavily it's weighted,
 * who it targets, and an optional line of dialogue to fire when it triggers.
 */
class AttackOption(
    val weight: Int,
    val attack: BossAttack,
    val target: TargetMode = TargetMode.RANDOM_PLAYER,
    val line: DialogueKey? = null,
    val requiresPlayers: Boolean = true,
) {
    fun resolveContext(boss: OdysseyBoss, nearby: List<Player>): AttackContext {
        val (origin, affected) = when (target) {
            TargetMode.SELF -> boss.location to emptyList()
            TargetMode.ALL_PLAYERS -> boss.location to nearby
            TargetMode.RANDOM_PLAYER -> {
                val chosen = nearby.randomOrNull()
                (chosen?.location ?: boss.randomOffsetLocation(9)) to listOfNotNull(chosen)
            }
        }
        return AttackContext(boss.plugin, boss.entity, boss.world, origin, affected, nearby)
    }
}

/**
 * Runs a boss's weighted attack rotation on a timer. Owned by the base class;
 * you never touch this directly, you just supply [AttackOption]s.
 */
class AttackScheduler(private val boss: OdysseyBoss) {

    private var task: BukkitTask? = null

    fun start(periodTicks: Long, provider: () -> List<AttackOption>) {
        stop()
        task = boss.plugin.server.scheduler.runTaskTimer(
            boss.plugin, Runnable { castOnce(provider()) }, 0L, periodTicks,
        )
    }

    fun stop() {
        task?.cancel()
        task = null
    }

    private fun castOnce(options: List<AttackOption>) {
        if (!boss.isAlive || options.isEmpty()) return
        val nearby = boss.nearbyPlayers(boss.stats.attackRadius)
        val usable = options.filter { !it.requiresPlayers || nearby.isNotEmpty() }
        val choice = weightedPick(usable) ?: return
        val ctx = choice.resolveContext(boss, nearby)
        choice.attack.execute(ctx)
        choice.line?.let { boss.announce(it, nearby) }
    }

    private fun weightedPick(options: List<AttackOption>): AttackOption? {
        val total = options.sumOf { it.weight }
        if (total <= 0) return null
        var roll = (0 until total).random()
        for (o in options) {
            if (roll < o.weight) return o
            roll -= o.weight
        }
        return options.lastOrNull()
    }
}