package me.shadowalzazel.mcodyssey.occurrences.base

import me.shadowalzazel.mcodyssey.seasons.SeasonType

sealed class Requirement {

    class Season(var season: SeasonType): Requirement()
    class Time(var time: SeasonType): Requirement() // 0 .. 24000

}
