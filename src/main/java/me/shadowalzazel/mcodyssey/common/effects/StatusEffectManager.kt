package me.shadowalzazel.mcodyssey.common.effects

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

object StatusEffectManager {

    /**
     * How often the master loop runs, in ticks. This is the *resolution* of the
     * whole system: durations and callback intervals are accurate to within this
     * many ticks. Bigger = cheaper but less precise. 4 ticks (0.2s) is plenty
     * for status effects and 5x cheaper than running every tick.
     */
    private const val TICK_RATE = 1

    /**
     * One tracked instance of an effect on one entity.
     * Lives only in memory — nothing here is ever written to the entity except
     * the attribute modifiers, which the manager owns via deterministic keys.
     */
    private class Active(val effect: StatusEffect, var amplifier: Double, var remainingTicks: Int) {
        /** Countdown to the next onTick callback. Independent of remainingTicks. */
        var ticksUntilTrigger: Int = effect.tickInterval
    }

    // uuid -> (effectId -> Active). Outer map lets us find all effects on an entity;
    // inner map keyed by effect id guarantees one instance per effect (no self-stacking).
    private val active = HashMap<UUID, MutableMap<String, Active>>()
    private var task: BukkitTask? = null

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── LIFECYCLE ───────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    fun init(plugin: Plugin) {
        if (task != null) return
        // Strip any modifiers left behind by a previous session before we start.
        plugin.server.onlinePlayers.forEach { sweep(it) }
        task = plugin.server.scheduler.runTaskTimer(
            plugin, Runnable { tick() },
            TICK_RATE.toLong(), TICK_RATE.toLong()
        )
    }

    /** Call from onDisable so no modifiers survive to be serialized to disk. */
    fun shutdown() {
        for ((uuid, effects) in active) {
            val entity = Bukkit.getEntity(uuid) as? LivingEntity ?: continue
            effects.values.forEach { clearModifiers(entity, it.effect) }
        }
        active.clear()
        task?.cancel()
        task = null
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ─────────────────────────────── PUBLIC API ───────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    fun LivingEntity.applyOdysseyEffect(
        effect: StatusEffect,
        durationInTicks: Int,
        amplifier: Double = 1.0,
    ) {
        apply(this, effect, durationInTicks, amplifier)
    }

    fun apply(entity: LivingEntity, effect: StatusEffect, durationTicks: Int, amplifier: Double = 1.0) {
        val byId = active.getOrPut(entity.uniqueId) { HashMap() }
        val existing = byId[effect.id]
        val resolved = if (existing != null) {
            // Refresh, don't stack: keep the stronger amplifier and longer remaining time.
            existing.amplifier = maxOf(existing.amplifier, amplifier)
            existing.remainingTicks = maxOf(existing.remainingTicks, durationTicks)
            existing.amplifier * 1.0
        } else {
            byId[effect.id] = Active(effect, amplifier, durationTicks)
            amplifier
        }
        applyModifiers(entity, effect, resolved)
    }

    fun remove(entity: LivingEntity, effect: StatusEffect) {
        val byId = active[entity.uniqueId] ?: return
        if (byId.remove(effect.id) != null) clearModifiers(entity, effect)
        if (byId.isEmpty()) active.remove(entity.uniqueId)
    }

    fun removeAll(entity: LivingEntity) {
        active.remove(entity.uniqueId)?.values?.forEach { clearModifiers(entity, it.effect) }
    }

    fun has(entity: LivingEntity, effect: StatusEffect): Boolean =
        active[entity.uniqueId]?.containsKey(effect.id) == true

    /** Remove orphaned status modifiers not currently tracked (reload/unload safety). */
    fun sweep(entity: LivingEntity) {
        val liveIds = active[entity.uniqueId]?.keys ?: emptySet<String>()
        val attrs = StatusEffect.all.flatMap { it.changes.map(AttributeChange::attribute) }.toSet()
        for (attribute in attrs) {
            val inst = entity.getAttribute(attribute) ?: continue
            inst.modifiers
                .filter { it.key.namespace() == "odyssey" && it.key.value().startsWith("status.") }
                .filter { it.key.value().split('.').getOrElse(1) { "" } !in liveIds }
                .forEach { inst.removeModifier(it) }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── INTERNALS ───────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private fun tick() {
        if (active.isEmpty()) return  // nothing tracked -> zero work

        // Collect expirations separately so we don't mutate the maps mid-iteration.
        val expired = ArrayList<Pair<UUID, String>>()

        for ((uuid, byId) in active) {
            // One lookup per entity per interval (not per effect) — keep this cheap.
            val entity = Bukkit.getEntity(uuid) as? LivingEntity
            if (entity == null || entity.isDead || !entity.isValid) {
                // Entity gone/unloaded: drop all its effects. Modifiers die with the entity,
                // and sweep() will strip any that reappear on chunk reload.
                byId.keys.forEach { expired += uuid to it }
                continue
            }

            for (a in byId.values) {
                // Early-out check. Runs every loop pass (every TICK_RATE), independent of
                // tickInterval — so cancellation lands within ~0.2s, not on the next 20-tick beat.
                if (a.effect.cancelIf(entity)) {
                    clearModifiers(entity, a.effect)   // fires onRemove too
                    expired += uuid to a.effect.id     // deferred removal, safe during iteration
                    continue                           // skip this tick's callback + aging
                }

                // Periodic callback (particles, DoT, etc.), if this effect defines one.
                // Countdown approach handles any interval, even non-multiples of TICK_RATE.
                if (a.effect.tickInterval > 0) {
                    a.ticksUntilTrigger -= TICK_RATE
                    if (a.ticksUntilTrigger <= 0) {
                        a.effect.onTick(entity, a.amplifier)
                        a.ticksUntilTrigger += a.effect.tickInterval
                    }
                }

                // Age the effect; expire when its clock runs out.
                a.remainingTicks -= TICK_RATE
                if (a.remainingTicks <= 0) {
                    clearModifiers(entity, a.effect)
                    expired += uuid to a.effect.id
                }
            }
        }

        // Apply the collected removals.
        for ((uuid, id) in expired) {
            active[uuid]?.remove(id)
            if (active[uuid]?.isEmpty() == true) active.remove(uuid)
        }
    }

    private fun applyModifiers(entity: LivingEntity, effect: StatusEffect, amplifier: Double) {
        for (change in effect.changes) {
            val inst = entity.getAttribute(change.attribute) ?: continue
            val key = effect.keyFor(change.attribute)
            inst.byKey(key)?.let { inst.removeModifier(it) }  // remove-then-add => idempotent, never stacks
            inst.addModifier(AttributeModifier(key, change.amount(amplifier), change.operation, EquipmentSlotGroup.ANY))
        }
    }

    private fun clearModifiers(entity: LivingEntity, effect: StatusEffect) {
        for (change in effect.changes) {
            val inst = entity.getAttribute(change.attribute) ?: continue
            inst.byKey(effect.keyFor(change.attribute))?.let { inst.removeModifier(it) }
        }
        effect.onRemove(entity)  // non-attribute cleanup (tags, stacks, etc.)
    }

    private fun AttributeInstance.byKey(key: NamespacedKey) = modifiers.firstOrNull { it.key == key }
}