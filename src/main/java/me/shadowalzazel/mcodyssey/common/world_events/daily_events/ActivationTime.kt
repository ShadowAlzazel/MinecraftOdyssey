package me.shadowalzazel.mcodyssey.common.world_events.daily_events

sealed class ActivationTime(val startTime: Int, val endTime: Int) {

    data object Day: ActivationTime(500, 12500)
    data object Night: ActivationTime(13000, 23000)
    class Custom(start: Int, end: Int): ActivationTime(start, end)

}