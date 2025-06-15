package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.AsyncCastingManager
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingBuilder
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity

class ArcaneSpell(
    val source: ArcaneSource,
    val orginalContext: CastingContext,
    val runeSequence: List<ArcaneRune>
) {

    // Shared vars
    private val castingManager: AsyncCastingManager
    private val runeCount: Int
    private var seqCounter: Int
    private val sharedContext: CastingContext
    // Buffer vars
    private val runesInBuffer: MutableList<ArcaneRune>
    private var builderInBuffer: CastingBuilder?

    // Async vars
    var hasCastingSignal: Boolean
    var hasCreateSignal: Boolean
    var isCasting: Boolean
    private var isFinished: Boolean

    // Cycle Runes
    init {
        // Default vars
        runeCount = runeSequence.count()

        // Default Ignore self
        orginalContext.ignoredTargets.add(orginalContext.caster)
        // Finish Context
        sharedContext = orginalContext.clone()

        // Set UP the buffer and the sequence counter
        seqCounter = 0
        runesInBuffer = mutableListOf()
        builderInBuffer = null
        isFinished = false

        // Create the async cycle
        hasCastingSignal = true
        hasCreateSignal = true
        isCasting = false
        castingManager = AsyncCastingManager(this)
    }

    fun isFinished(): Boolean {
        if (seqCounter >= runeCount) {
            isFinished = true
        }
        return isFinished
    }

    /**
     * RULES:
     * - Context is preserved across the entire read. Meaning it is single channel and global. NOT local.
     *
     * - CastingRune:
     * These runes trigger a `CastCycle`. Similar to a domain rune, they also change context.
     * They change the target context
     */

    fun castSpell() {
        // ----------------------
        // Read runes
        // When READER READS a casting RUNE -> SEND runes to separate list (buffer)
        // CALL async runner
        // END this call

        // Async runner EXECUTES Read runes in BUFFER
        // has an AWAIT when it finishes its methods (while true)
        // Most Casting runes return the AWAIT INSTANTLY
        // Some like BALL need to wait.

        // When AWAIT is handled -> return to READER
        // ----------------------

        // Start Casting Manager
        // Run now and every tick and checks if has the signal to RUN the buffer
        castingManager.runTaskTimer(Odyssey.instance, 0, 1L)
    }

    /**
     * This loads a cast cycle into the BUFFER
     */
    fun createCastingCycle() {
        // Local Cycle Variables
        val cycleContext = sharedContext
        var castingRune: CastingRune? = null
        var triggerCast = false
        // Create a builder
        val cycleBuilder = CastingBuilder()
        cycleBuilder.apply {
            damageType = source.damageType
            particle = source.particle
        }
        // TODO:
        // Builder can be USED by non-casting RUNES
        // i.e. trace, break

        // READING
        while (seqCounter < runeCount) {
            val rune = runeSequence[seqCounter]
            runesInBuffer.add(rune)
            // when reaching casting rune
            if (rune is CastingRune) {
                castingRune = rune
                triggerCast = true
            }
            // When reaches the end or has REACHED a casting rune
            if (seqCounter == runeCount - 1 || castingRune != null) {
                triggerCast = true
            }
            seqCounter++
            // Break and put in BUFFER if triggerCast
            if (triggerCast) {
                castingRune = null
                builderInBuffer = cycleBuilder
                break
            }
        }

    }

    /**
     * This runs the casting cycle that was in the BUFFER
     */
    fun runCastingCycle() {
        // Get vars in the buffer
        val builder = builderInBuffer!!
        val context = sharedContext
        val runes = runesInBuffer

        var castingRune: CastingRune? = null

        // Start this loop
        for ((i, rune) in runes.withIndex()) {
            println("Rune [${i}] [${rune.name}] \n")
            // TODO: Have augment, and domain runes CONSUME modifiers that are read before
            when (rune) {
                is ModifierRune -> {
                    builder.storeRune(rune)
                }
                // Domain runes change context
                is DomainRune -> {
                    rune.change(orginalContext, context)
                }
                // Do the augment rune effect
                is AugmentRune -> {
                    rune.effect(context)
                }
                // This should be the final rune called
                is CastingRune -> {
                    castingRune = rune
                    // Assemble the casting rune with the builder
                    castingRune.assemble(builder)
                    builder.buildStored()
                    // Run the cast
                    castingRune.cast(context, builder)
                }
            }
        }
        // When done with loop, get signal from castingRune
        // Reset signal if no casting rune
        if (castingRune != null) {
            hasCastingSignal = true
            hasCreateSignal = true
        }
        else {
            val castingRuneFinished = true
            // TODO: Have the Ball timer have access to the signal
            // Then when isDead -> turn on the signal
            if (castingRuneFinished) {
                hasCastingSignal = true
                hasCreateSignal = true
            }
        }
        // Remove the builder in the buffer
        builderInBuffer = null
        // Remove all runes in buffer
        runesInBuffer.removeIf { true }
    }

}