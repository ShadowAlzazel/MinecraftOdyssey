package me.shadowalzazel.mcodyssey.common.arcane.runes

@Suppress("UnstableApiUsage")
sealed class VariableRune : ArcaneRune() {
    // Variable runes CHANGE how the sequence is READ
    // How the loop/run time behaves

    class Vulnerability() : VariableRune() {
        override val name = "vulnerability"
        override val displayName = "vulnerability"
    }


    // Mimics/Clones the original casting context conditions
    // different from the ORIGIN rune, as that can have the `target` change
    data object Repeat : VariableRune() {
        override val name = "repeat"
        override val displayName = "Repeat"
    }

    // BEHAVES like a music CODA
    // Goes back to the beginning, IGNORES codas
    data object Coda : VariableRune() {
        override val name = "coda"
        override val displayName = "coda"
    }


    // ENVIRONMENT RUNES
    class Light()  : VariableRune() {
        override val name = "light"
        override val displayName = "light"
    }

    // MAYBE ITEM runes
    // detects if ITEM

    // OR pick up ITEM

}