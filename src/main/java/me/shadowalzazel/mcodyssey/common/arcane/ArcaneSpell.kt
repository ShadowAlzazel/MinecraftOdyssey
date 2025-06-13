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
        var currentManifestBuilder: ManifestBuilder = ManifestBuilder()
        var calledManifestRune = false

        for (i in 0..runeCount) {
            val runeAtI = runeSequence[i]
            // Look for the manifest rune
            if (runeAtI is ManifestationRune) {
                calledManifestRune = true
                currentManifestRune = runeAtI
                // Create the builder using the build and install default params
                currentManifestBuilder = currentManifestRune.build(currentContext)
                currentManifestBuilder.apply {
                    damageType = source.damageType
                    particle = source.particle
                }
                continue
            }
            // Call the DomainRune super method to change context
            else if (runeAtI is DomainRune) {
                runeAtI.change(originContext, currentContext)
            }
            else if (runeAtI is VariableRune) {
                // LATER
            }

            // Has manifest rune, look for other rune types
            if (currentManifestRune != null) {
                if (runeAtI is ModifierRune) {
                    // Each manifest rune has its own builder
                    currentManifestRune.modify(currentManifestBuilder, runeAtI)
                }

            }
            // Change context with Domain Runes

            // When reaches the end OR a new KERNEL Rune
            if (i >= runeCount - 1) {

            }

        }


    }



}