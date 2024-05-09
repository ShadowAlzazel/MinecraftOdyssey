package me.shadowalzazel.mcodyssey.world_events.tasks

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.scheduler.BukkitRunnable

class DateTimeSyncer(val odyssey: Odyssey) : BukkitRunnable() {

    var currentDay: Int = 0
    var currentTime: Long = 0

    override fun run() {
        currentTime = odyssey.overworld.time // Set day time
        val thisDay = getDay()
        val worldEventManager = odyssey.worldEventManager
        println("Checked Day: $currentDay")
        println("This Day: $thisDay")
        // Create a new Day
        if (thisDay != currentDay) { // New Day called once
            println("NEW DAY!")
            worldEventManager.runOnNewDay(odyssey.overworld)
            currentDay = thisDay
            // Roll for event
            worldEventManager.rollForTrigger(odyssey.overworld)
        }
        // IF triggered wait on time to activate
        if (!worldEventManager.isDailyEventActive && worldEventManager.isDailyEventTriggered) {
            worldEventManager.checkTimeThenActivate(odyssey.overworld)
        }
        // Run Actives
        if (worldEventManager.isDailyEventActive) {
            worldEventManager.currentDailyEvent?.persistentActionHandler(odyssey.overworld)
        }

    }

    fun getDay(): Int {
        return (odyssey.overworld.gameTime).floorDiv(24000).toInt()
    }



}