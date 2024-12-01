package me.shadowalzazel.mcodyssey.common.alchemy.utility

enum class BlockBase(val blockCoords: Set<Triple<Double, Double, Double>>) {

    PLUS(setOf(
        Triple(1.0, 0.0, 0.0),
        Triple(-1.0, 0.0, 0.0),
        Triple(0.0, 0.0, 1.0),
        Triple(0.0, 0.0, -1.0))),
    RING(setOf(
        Triple(1.0, 0.0, 0.0),
        Triple(-1.0, 0.0, 0.0),
        Triple(0.0, 0.0, 1.0),
        Triple(0.0, 0.0, -1.0),
        Triple(1.0, 0.0, 1.0),
        Triple(-1.0, 0.0, 1.0),
        Triple(1.0, 0.0, -1.0),
        Triple(-1.0, 0.0, -1.0)))


}