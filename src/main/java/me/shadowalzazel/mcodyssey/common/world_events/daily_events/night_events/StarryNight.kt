package me.shadowalzazel.mcodyssey.common.world_events.daily_events.night_events

import me.shadowalzazel.mcodyssey.common.world_events.daily_events.ActivationTime
import me.shadowalzazel.mcodyssey.common.world_events.daily_events.DailyWorldEvent
import me.shadowalzazel.mcodyssey.common.world_events.utility.EntityConditions

class StarryNight : DailyWorldEvent(
    "starry_night",
    4,
    4,
    ActivationTime.Night,
    listOf(EntityConditions.AlwaysTrue),
) {




}