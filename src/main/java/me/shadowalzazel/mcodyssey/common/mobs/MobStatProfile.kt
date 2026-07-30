package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.util.AttributeManager
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason

/**
 * An immutable, composable bundle of attribute modifiers.
 *
 * Replaces the copy-pasted `mob.apply { setHealthAttribute(...); heal(...); addAttackAttribute(...) }`
 * blocks. Because it is just data, a profile can be declared once and applied to any mob,
 * anywhere — structure spawns, boss rooms, commands, tests.
 *
 *     val EDGE = statProfile {
 *         attack(3.0, AttributeTags.MOB_EDGE_ATTACK_BONUS)
 *         health(15.0, AttributeTags.MOB_EDGE_HEALTH_BONUS)
 *     }
 *     EDGE.applyTo(mob)
 */
class StatProfile private constructor(private val ops: List<(LivingEntity) -> Unit>) {

    fun applyTo(mob: LivingEntity) = ops.forEach { it(mob) }

    /** Compose profiles: `BASE + ELITE_BONUS`. */
    operator fun plus(other: StatProfile) = StatProfile(ops + other.ops)

    companion object {
        val EMPTY = StatProfile(emptyList())
        internal fun of(ops: List<(LivingEntity) -> Unit>) = StatProfile(ops)
    }

    class Builder : AttributeManager {
        private val ops = mutableListOf<(LivingEntity) -> Unit>()

        /**
         * Raises max health by [amount] and immediately tops the mob up.
         *
         * In the original every `setHealthAttribute(x, tag)` was followed by `heal(x)` — one
         * of them silently omitted the RegainReason. Bundling them makes it impossible to
         * add health and forget to heal.
         */
        fun health(amount: Double, tag: String) = op {
            it.setHealthAttribute(amount, tag)
            it.heal(amount, RegainReason.CUSTOM)
        }

        fun attack(amount: Double, tag: String) = op { it.addAttackAttribute(amount, tag) }
        fun armor(amount: Double, tag: String) = op { it.addArmorAttribute(amount, tag) }
        fun speed(amount: Double, tag: String) = op { it.addSpeedAttribute(amount, tag) }
        fun step(amount: Double, tag: String) = op { it.addStepAttribute(amount, tag) }
        fun scale(amount: Double, tag: String) = op { it.addScaleAttribute(amount, tag) }
        fun reach(amount: Double, tag: String) = op { it.addReachAttribute(amount, tag) }

        /** Escape hatch for anything the DSL doesn't cover yet. */
        fun custom(action: (LivingEntity) -> Unit) = op(action)

        private fun op(action: (LivingEntity) -> Unit) = apply { ops += action }

        fun build() = StatProfile.of(ops.toList())
    }
}

fun statProfile(block: StatProfile.Builder.() -> Unit): StatProfile =
    StatProfile.Builder().apply(block).build()