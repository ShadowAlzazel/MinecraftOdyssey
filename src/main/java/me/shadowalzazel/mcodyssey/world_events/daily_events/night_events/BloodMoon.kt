package me.shadowalzazel.mcodyssey.world_events.daily_events.night_events

import me.shadowalzazel.mcodyssey.world_events.daily_events.ActivationTime
import me.shadowalzazel.mcodyssey.world_events.daily_events.DailyWorldEvent
import me.shadowalzazel.mcodyssey.world_events.utility.EntityConditions

class BloodMoon : DailyWorldEvent(
    "blood_moon",
    4,
    4,
    ActivationTime.Night,
    listOf(EntityConditions.AlwaysTrue),
) {




}