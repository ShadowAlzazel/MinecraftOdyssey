package me.shadowalzazel.mcodyssey.common.arcane.runes

@Suppress("UnstableApiUsage")
sealed class AugmentRune : ArcaneRune() {
    // Variable runes CHANGE how the sequence is READ
    // How the loop/run time behaves



    // Mimics/Clones the original casting context conditions
    // different from the ORIGIN rune, as that can have the `target` change
    data object Repeat : AugmentRune() {
        override val name = "repeat"
        override val displayName = "Repeat"
    }

    // BEHAVES like a music CODA
    // Goes back to the beginning, IGNORES codas
    data object Coda : AugmentRune() {
        override val name = "coda"
        override val displayName = "coda"
    }


    // ENVIRONMENT RUNES
    class Light()  : AugmentRune() {
        override val name = "light"
        override val displayName = "light"
    }

    class Break(val value: Double = 0.0)  : AugmentRune() {
        override val name = "break"
        override val displayName = "break"
    }


    class Vulnerability() : AugmentRune() {
        override val name = "vulnerability"
        override val displayName = "vulnerability"
    }

    // MAYBE ITEM runes
    // detects if ITEM

    // OR pick up ITEM

}