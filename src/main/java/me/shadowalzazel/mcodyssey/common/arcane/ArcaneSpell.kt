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
        var currentContext = originContext.clone()
        var currentManifestRune: ManifestationRune? = null
        var currentBuilder: ManifestBuilder = ManifestBuilder()
        var callingManifest = false

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
                continue
            }
            else if (rune is VariableRune) {
                // LATER
            }
            // Has manifest rune, look for other rune types
            if (currentManifestRune != null) {
                if (rune is ModifierRune) {
                    // Each manifest rune has its own builder
                    currentManifestRune.modify(currentBuilder, rune)
                }

            }
            // Change context with Domain Runes
            // Call the DomainRune super method to change context

            // When reaches the end OR a new KERNEL Rune CALL the rune
            if (index == runeCount - 1) {
                callingManifest = true
            }

            // If we get the call, run the manifest cast
            if (callingManifest || rune is DomainRune) {
                currentManifestRune?.cast(currentContext, currentBuilder)
                callingManifest = false
            }
            if (rune is DomainRune) {
                rune.change(originContext, currentContext)
            }

        }


    }



}