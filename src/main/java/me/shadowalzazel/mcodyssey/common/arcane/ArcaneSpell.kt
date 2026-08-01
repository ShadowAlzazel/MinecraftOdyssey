package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.AugmentRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.CastingRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.DomainRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import org.bukkit.entity.LivingEntity

/**
 * Executes a linear rune sequence. Runes are read left to right; each casting
 * rune fires a sub-cast, and the runes after it continue from wherever that cast ended
 * (its impact point, the entity it hit, and so on).
 *
 * Chaining is driven by a completion callback rather than per-tick polling: a synchronous
 * form (Beam, Zone, Disperse) reports "done" immediately, while a projectile (Ball, Bolt)
 * reports "done" only when it impacts. That single difference is what lets a spell like
 * `[Bolt, Next, Zone]` wait for the bolt to land before dropping the zone on it — with no
 * signals, no manager, and no busy loop.
 */
class ArcaneSpell(
    val source: ArcaneSource,
    private val originalContext: CastingContext,
    private val runeSequence: List<ArcaneRune>,
    private val budget: CastBudget = CastBudget(),   // shared across the whole branch tree
    isBranch: Boolean = false
) {
    private val runeCount = runeSequence.size
    private var seqCounter = 0

    // Working context. The original is kept intact for Domain runes that reference it.
    private val context: CastingContext

    // Safety valve for future loop runes (Coda / Repeat) so a spell can never run away.
    private var castsRun = 0
    private val maxCasts = 64

    init {
        // Ignore the caster by default: convert to a target and add to the ignore list.
        // Branches inherit the parent's ignore list (via the cloned context), so only the
        // ROOT spell adds the caster — otherwise every hit re-appends it.
        if (!isBranch) originalContext.ignoredTargets.add(originalContext.caster.convertToTarget())
        context = originalContext.clone()
    }

    fun isFinished(): Boolean = seqCounter >= runeCount

    /** Public entry point. */
    fun castSpell() = advance()

    /** Step to the next cast cycle, unless the sequence is done or the safety cap is hit. */
    private fun advance() {
        if (seqCounter >= runeCount || castsRun >= maxCasts) return
        castsRun++
        runNextCycle(onComplete = ::advance)
    }

    /**
     * The reader. Consumes runes up to and including the next casting rune (or the end of
     * the sequence), applying each as it goes, then fires the form. [onComplete] runs once
     * the cycle has fully resolved. This is the ONE place the sequence "grammar" is
     * interpreted, so new rune categories only ever touch this `when`.
     */
    private fun runNextCycle(onComplete: () -> Unit) {
        val builder = CastingBuilder().apply {
            damageType = source.damageType
            particle = source.particle
        }
        var castingRune: CastingRune? = null

        while (seqCounter < runeCount) {
            val rune = runeSequence[seqCounter]
            seqCounter++
            when (rune) {
                is ModifierRune -> builder.storeRune(rune)               // tunes the upcoming cast
                is DomainRune   -> rune.change(originalContext, context) // re-aims / relocates
                is AugmentRune  -> rune.effect(context)                  // side effect on the context
                is CastingRune  -> { castingRune = rune; break }         // the form ends this cycle
            }
        }

        val form = castingRune
        if (form != null) {
            form.assemble(builder)
            builder.buildStored()

            if (form.chainsOnHit) {
                // The runes after a reactive form are its reaction, not a linear continuation.
                // Snapshot the tail and bind it; then end the MAIN thread so the tail runs
                // ONLY per-hit, never once-at-placement.
                val tail = runeSequence.subList(seqCounter, runeCount).toList()
                if (tail.isNotEmpty()) context.chainSpawner = { hit -> spawnBranch(context, tail, hit) }
                seqCounter = runeCount
            }

            form.cast(source, context, builder, onComplete)
        } else {
            onComplete()
        }
    }

    private fun spawnBranch(template: CastingContext, tail: List<ArcaneRune>, hit: ArcaneTarget) {
        if (tail.isEmpty()) return
        val child = template.clone()          // chainSpawner is NOT carried over
        child.target = hit
        val e = hit.entityTarget
        if (e is LivingEntity) child.targetLocation = e.eyeLocation
        ArcaneSpell(source, child, tail, budget, isBranch = true).castSpell()
    }
}

/** Shared cast budget so a whole tree of branches can never run away. */
class CastBudget(var remaining: Int = 64) {
    fun tryConsume(): Boolean = if (remaining > 0) { remaining--; true } else false
}