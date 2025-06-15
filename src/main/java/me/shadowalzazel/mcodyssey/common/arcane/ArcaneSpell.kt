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


    private fun spellRunner() {
        val runeCount = runeSequence.count()

        // Algo variables
        val currentContext = orginalContext.clone()
        var castingRune: CastingRune? = null

        // Create a builder
        var currentBuilder: CastingBuilder = CastingBuilder()
        currentBuilder.apply {
            damageType = source.damageType
            particle = source.particle
        }

        // Casting
        var triggerCast = false
        val runesToRun = mutableListOf<ArcaneRune>()
        // Default Ignore self
        currentContext.ignoredTargets.add(currentContext.caster)

        for ((index, rune) in runeSequence.withIndex()) { // up to runeCount - 1
            // Look for runes
            if (rune is ModifierRune) {
                currentBuilder.storeRune(rune)
            }
            // Augments depend on run-time
            else if (rune is AugmentRune) {
                runesToRun.add(rune)
            }
            // Domain runes change context
            else if (rune is DomainRune) {
                rune.change(orginalContext, currentContext)
            }

            // Look for the manifest rune
            if (rune is CastingRune) {
                castingRune = rune
                // Assemble the casting rune with the builder
                castingRune.assemble(currentBuilder)
                currentBuilder.buildStored()
                // IMPORTANT
                // This adds the rune to the FIRST in the list, so it is the first rune called
                // Ideas: Maybe can change the calling index
                // or maybe can change the calling sequence
                val callingIndex = 0
                runesToRun.add(rune)
                triggerCast = true
            }

            // When reaches the end
            if (index == runeCount - 1) {
                triggerCast = true
            }

            // Cast the runes BEFORE changing context
            if (triggerCast && castingRune != null) {
                // Call casting run function
                castRunes(runesToRun, currentContext, currentBuilder)
                // Reset variables
                currentBuilder = CastingBuilder()
                currentBuilder.apply {
                    damageType = source.damageType
                    particle = source.particle
                }
                castingRune = null
                triggerCast = false
                runesToRun.removeAll { true }

            }

        }
    }

    /**
     * Calls the manifest rune and subsequent augments
     */
    private fun castRunes(runes: List<ArcaneRune>, context: CastingContext, builder: CastingBuilder) {
        if (runes.isEmpty()) return
        // Run this subsequence of cast + augments
        val runeCount = runes.count()
        var usedCodas = 0
        val maxCodas = runes.count { it is AugmentRune.Coda }

        var i = 0
        while (i < runeCount) {
            val rune = runes[i]
            println("Rune [${i}] [${rune.name}] \n")

            if (rune is CastingRune) {
                rune.cast(context, builder)
            }

            else if (rune is AugmentRune) {
                when (rune) {
                    is AugmentRune.Break -> {
                        val block = context.targetLocation?.block
                        if (block != null) {
                            // Use the wiki to find the values to break
                            // https://minecraft.wiki/w/Module:Blast_resistance_values
                            block.breakNaturally()
                        }
                    }
                    is AugmentRune.Coda -> {
                        // TODO: kinda useless, MOVE to main loop?
                        // Go back to 0
                        if (usedCodas >= maxCodas) {
                            continue
                        } else {
                            usedCodas++
                            i = 0
                        }
                    }
                    is AugmentRune.PickUp -> {
                        val pickUpLocation = context.targetLocation ?: continue
                        val nearby = pickUpLocation.getNearbyEntities(1.0, 1.0, 1.0)
                        if (nearby.isEmpty()) continue
                        val items = nearby.filter { it is Item }
                        if (items.isEmpty()) continue
                        val itemToPickUp = items.first()
                        if (itemToPickUp is Item) {
                            itemToPickUp.teleport(context.castingLocation)
                        }
                    }
                    is AugmentRune.Heal -> {
                        val target = context.target
                        if (target is LivingEntity) {
                            target.heal(rune.value)
                        }
                    }
                    is AugmentRune.Teleport -> {
                        context.target?.teleport(context.castingLocation)
                    }
                    else -> {

                    }
                }
            }

            // Counter
            i++
        }

    }



}