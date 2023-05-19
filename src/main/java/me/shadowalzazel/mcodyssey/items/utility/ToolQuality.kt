package me.shadowalzazel.mcodyssey.items.utility

enum class ToolQuality(flat: Int, crit: Double) {

    // CAN ONLY INCREASE QUALITY AT MAX DURABILITY

    // Flat affects normal
    // Percent affect crit

    MASTERPIECE(3, 1.75),
    PRISTINE(2, 1.5),
    GREAT(1, 1.25),
    MEDIOCRE(0, 1.0),
    POOR(-1, 0.75),
    BROKEN(-2, 0.5),
}