package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.common.mobs.hostile.Preacher
import me.shadowalzazel.mcodyssey.common.mobs.hostile.Ruined
import me.shadowalzazel.mcodyssey.common.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.common.mobs.passive.TreasurePig
import me.shadowalzazel.mcodyssey.common.mobs.hostile.Savage
import me.shadowalzazel.mcodyssey.common.mobs.preset.Vanguard
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity

fun interface MobSpawner {
    fun spawn(world: World, location: Location): Entity
}


object OdysseyMobs {

    private val registry = LinkedHashMap<String, MobSpawner>()

    // --- Hostile ---
    val SAVAGE              = register("savage")         { w, l -> Savage.spawn(w, l) }
    val SAVAGE_KNIGHT       = register("savage_knight")  { w, l -> Savage.spawnKnight(w, l).first }
    val VANGUARD            = register("vanguard")        { w, l -> Vanguard.spawn(w, l) }
    val VANGUARD_KNIGHT     = register("vanguard_knight") { w, l -> Vanguard.spawnKnight(w, l).first }
    val PREACHER            = register("preacher")        { w, l -> Preacher.spawn(w, l) }
    val RUINED              = register("ruined")          { w, l -> Ruined.spawn(w, l) }

    // --- Passive ---
    val TREASURE_PIG        = register("treasure_pig")   { w, l -> TreasurePig.spawn(w, l) }
    val DUBIOUS_DEALER      = register("dubious_dealer") { w, l -> DubiousDealer.spawn(w, l) }

    /** Registers a spawner and hands back its own id — so the constant and the registration are one line, and there's no separate string to keep in sync. */
    private fun register(id: String, spawner: MobSpawner): String {
        check(registry.putIfAbsent(id, spawner) == null) { "Duplicate Odyssey mob id: '$id'" }
        return id
    }

    /** All registered ids — feed this to a command's tab-completer instead of hardcoding a list. */
    val ids: Set<String> get() = registry.keys

    fun isRegistered(id: String): Boolean = registry.containsKey(id)

    /** Soft-fail: unknown id returns null instead of throwing, since this is the path user input (commands) hits. */
    fun spawn(id: String, world: World, location: Location): Entity? {
        return registry[id]?.spawn(world, location)
    }

    /** Hard-fail variant for call sites where the id is a hardcoded literal, not user input — an unknown id there is a real bug. */
    fun spawnOrThrow(id: String, world: World, location: Location): Entity {
        return spawn(id, world, location) ?: error("No Odyssey mob registered with id '$id'")
    }

}
