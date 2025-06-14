package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingBuilder
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity

class ArcaneSpell(
    val source: ArcaneSource,
    val kernelContext: CastingContext,
    val runeSequence: List<ArcaneRune>
) {

    /**
     * RULES:
     * - Whenever a `new` manifestRune is called, it takes the EXISTING context.
     * This context could have been changes by other runes. So when chaining new manifests,
     * do AFTER you change the domain.
     * - Context is preserved across the entire read. Meaning it is single channel and global. NOT local.
     *
     * - Manifest CREATES new target CONTEXT.
     * - Manifest consumes all the modifiers.
     */

    fun castSpell() {

        val runeCount = runeSequence.count()

        // Algo variables
        val currentContext = kernelContext.clone()
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
                rune.change(kernelContext, currentContext)
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
                runesToRun.add(0, rune)
                triggerCast = true
            }

            // When reaches the end
            if (index == runeCount - 1) {
                triggerCast = true
            }

            // Cast the runes BEFORE changing context
            if (triggerCast && castingRune != null) {
                // Call casting run function
                println("----- CASTING RUNES --------")
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

            // This HAS to be true
            // rune[0] in the call is ALWAYS the casting rune
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