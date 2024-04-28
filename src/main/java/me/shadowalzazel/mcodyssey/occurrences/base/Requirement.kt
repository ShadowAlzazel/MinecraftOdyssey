package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.World

sealed class Requirement {

    // Initial Conditions for an occurrence to run

    data class IsTime(var min: Int, var max: Int): Requirement() // 0 .. 24000
    object NoRequirement: Requirement()


    fun checkRequirement(someWorld: World): Boolean {
        return when (this) {
            is IsTime -> (min <= someWorld.time) && (someWorld.time <= max)
            NoRequirement -> true
        }
    }



}
