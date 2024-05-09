package me.shadowalzazel.mcodyssey.world_events.day_events

import me.shadowalzazel.mcodyssey.world_events.utility.ActivationTime
import me.shadowalzazel.mcodyssey.world_events.utility.DailyWorldEvent
import me.shadowalzazel.mcodyssey.world_events.utility.EntityConditions

class SlimeShower : DailyWorldEvent(
    "slime_shower",
    4,
    4,
    ActivationTime.Day,
    listOf(EntityConditions.AlwaysTrue),
) {




}