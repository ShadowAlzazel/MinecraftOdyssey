package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.ManifestBuilder

class ArcaneSpell(
    val source: ArcaneSource,
    val originContext: CastingContext,
    val runeSequence: List<ArcaneRune>
) {

    /**
     * RULES:
     * - Whenever a `new` manifestRune is called, it takes the EXISTING context.
     * This context could have been changes by other runes. So when chaining new manifests,
     * do AFTER you change the domain.
     * - Context is preserved across the entire read. Meaning it is single channel and global. NOT local.
     */

    fun castSpell() {

        val runeCount = runeSequence.count()

        // Algo variables
        val currentContext = originContext.clone()
        var currentManifestRune: ManifestationRune? = null
        var currentBuilder: ManifestBuilder = ManifestBuilder()

        // Casting
        var triggerCast = false
        val runesToRun = mutableListOf<ArcaneRune>()

        for ((index, rune) in runeSequence.withIndex()) { // up to runeCount - 1
            // Look for the manifest rune
            if (rune is ManifestationRune) {
                currentManifestRune = rune
                // Create the builder using the build and install default params
                currentBuilder = currentManifestRune.build(currentContext)
                currentBuilder.apply {
                    damageType = source.damageType
                    particle = source.particle
                }
                runesToRun.add(rune)
            }
            // Has manifest rune, look for other rune types
            if (currentManifestRune != null) {
                if (rune is ModifierRune) {
                    // Each manifest rune has its own builder
                    currentManifestRune.modify(currentBuilder, rune)
                }
                else if (rune is AugmentRune) {
                    runesToRun.add(rune)
                }
            }


            // IMPORTANT: Domain Runes trigger the CAST of the current stored sub-sequence
            if (rune is DomainRune) {
                triggerCast = true
            }
            // When reaches the end
            else if (index == runeCount - 1) {
                triggerCast = true
            }

            if (triggerCast) {
                println("Running Cast of runes")
                castRunes(runesToRun, currentContext, currentBuilder)
                currentManifestRune = null
                runesToRun.removeAll { true }
                triggerCast = false
            }

            // Effects of domain runes last
            if (rune is DomainRune) {
                rune.change(originContext, currentContext)
            }

        }

    }

    /**
     * Calls the manifest rune and subsequent augments
     */
    private fun castRunes(runes: List<ArcaneRune>, context: CastingContext, builder: ManifestBuilder) {
        if (runes.isEmpty()) return
        // Run this subsequence of cast + augments
        for (rune in runes) {
            // This HAS to be true
            if (rune is ManifestationRune) {
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
                            // TODO: Fix Order!!!
                        }
                    }
                    is AugmentRune.Coda -> {
                        // CHANGE INDEX !!!
                        // Maybe repeat in this loop?
                    }
                    else -> {

                    }
                }
            }
        }

    }



}