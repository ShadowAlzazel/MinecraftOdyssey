package me.shadowalzazel.mcodyssey.world_events

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.world_events.daily_events.DailyWorldEvent
import me.shadowalzazel.mcodyssey.world_events.daily_events.DayEvents
import me.shadowalzazel.mcodyssey.world_events.daily_events.NightEvents
import org.bukkit.World

class DailyWorldEventManager(val odyssey: Odyssey) {

    var isDailyEventActive: Boolean = false
    var isDailyEventTriggered: Boolean = false
    var currentDailyEvent: DailyWorldEvent? = null
    var hasRolledForDailyEvent: Boolean = false

    // Have Different activation times
    val dayEvents = DayEvents.eventList
    val nightEvents = NightEvents.eventList
    val dailyEvents = (DayEvents.eventList + NightEvents.eventList).toSet()


    fun runOnNewDay(world: World) {
        // Reset Day
        resetDailyEvent()
        // Roll and store new event
        val randomEvent = dailyEvents.random() // CHANGE TO WEIGHT!!
        currentDailyEvent = randomEvent
        return
    }

    // Roll for daily event
    // If success -> activate if it is time (Check time type)
    fun rollForTrigger(world: World) {
        if (currentDailyEvent == null) return
        if (hasRolledForDailyEvent) return
        if (isDailyEventActive) return
        // Roll to see if this event can activate
        val rolled = currentDailyEvent!!.rollTriggered(world, 100) // TESTING
        currentDailyEvent!!.triggerHandler(world, rolled)
        if (rolled) {
            isDailyEventTriggered = true
        }
        hasRolledForDailyEvent = true
        return
    }

    fun checkTimeThenActivate(world: World) {
        val dayTime = world.time.toInt()
        val startTime = currentDailyEvent!!.activationTime.startTime
        val endTime = currentDailyEvent!!.activationTime.endTime
        val timeRange = (startTime..endTime)
        if (dayTime in timeRange) {
            activateDailyEvent(world)
        }
        return
    }

    fun forceActivate(world: World, dailyWorldEvent: DailyWorldEvent) {
        currentDailyEvent = dailyWorldEvent
        isDailyEventTriggered = true
        isDailyEventActive = true
        hasRolledForDailyEvent = true
        currentDailyEvent!!.triggerHandler(world, true)
        currentDailyEvent!!.activate(world)
        return
    }

    private fun activateDailyEvent(world: World) {
        if (currentDailyEvent == null) return
        if (!isDailyEventTriggered) return
        isDailyEventActive = true
        currentDailyEvent!!.activate(world)
        return
    }

    private fun resetDailyEvent() {
        hasRolledForDailyEvent = false
        isDailyEventTriggered = false
        isDailyEventActive = false
        currentDailyEvent = null
        return
    }


}