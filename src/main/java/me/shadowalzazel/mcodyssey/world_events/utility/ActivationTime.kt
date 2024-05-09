package me.shadowalzazel.mcodyssey.world_events.utility

sealed class ActivationTime(val startTime: Int, val endTime: Int) {

    data object Day: ActivationTime(0, 13000)
    data object Night: ActivationTime(13000, 23000)

}