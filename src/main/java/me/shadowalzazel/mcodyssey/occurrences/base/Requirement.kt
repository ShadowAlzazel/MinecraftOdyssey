package me.shadowalzazel.mcodyssey.occurrences.base

import me.shadowalzazel.mcodyssey.seasons.SeasonType
import org.bukkit.World

sealed class Requirement {

    // Initial Conditions for an occurrence to run

    data class IsSeason(var seasonList: List<SeasonType>): Requirement()
    data class IsTime(var min: Int, var max: Int): Requirement() // 0 .. 24000
    object NoRequirement: Requirement()


    fun checkRequirement(someSeason: SeasonType, someWorld: World): Boolean {
        return when (this) {
            is IsSeason -> seasonList.contains(someSeason)
            is IsTime -> (min <= someWorld.time) && (someWorld.time <= max)
            NoRequirement -> true
        }
    }



}
