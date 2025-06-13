package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.ManifestBuilder
import org.bukkit.entity.Item

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
     *
     * - Manifest CREATES new target CONTEXT
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
        // Default Ignore self
        currentContext.ignoredTargets.add(currentContext.caster)

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

            // Cast the runes BEFORE changing context
            if (triggerCast && currentManifestRune != null) {
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
        val runeCount = runes.count()
        var usedCodas = 0
        val maxCodas = runes.count { it is AugmentRune.Coda }

        var i = 0
        while (i < runeCount) {
            val rune = runes[i]

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
                    else -> {

                    }
                }
            }

            // Counter
            i++
        }


        /*
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
                    else -> {

                    }
                }
            }
        }

         */

    }



}