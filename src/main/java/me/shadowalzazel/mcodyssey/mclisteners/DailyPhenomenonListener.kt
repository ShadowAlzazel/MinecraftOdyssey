package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenons.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.TimeSkipEvent

object OdysseyDailyPhenomenonListener : Listener {

    //make dictionary later
    // Daily Events
    var dailyPhenomenonList = listOf(
        GravityShift(), BreezyDay(), SolarFlare(), Earthquake(), WorldFamine(), BioluminescentDay(), FairyFollowDay(),
        ShimmerIntoxication(), SpiritsAwaken(), StoneFlash(), CometSighting(), BlazingSoul()
    )

    // Cool Down timers
    private var cooldown : Long = 0
    private val cooldownTimer = 10 //10000 -> 10 sec

    // Event Chooser
    @EventHandler
    fun onNewDay(event: TimeSkipEvent) {

        MinecraftOdyssey.instance.dailyPhenomenonActive = false
        MinecraftOdyssey.instance.nightlyPhenomenonActive = false
        val timeElapsed = System.currentTimeMillis() - cooldown

        // Event Cool down timer
        if (timeElapsed >= cooldownTimer) {
            cooldown = System.currentTimeMillis()

            // Check if end game
            if ((MinecraftOdyssey.instance.endGame) && (!MinecraftOdyssey.instance.dailyPhenomenonActive)) {
                val currentWorld = event.world
                var randomDailyPhenomenon = dailyPhenomenonList.random()
                val rolledRate = (0..100).random()

                //Daily luck is not daily phenomenon
                val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
                if (currentWorld.players.size >= luckConfigAmount) {
                    val drawOfFortunes = DrawOfFortunes()
                    drawOfFortunes.phenomenonEffect(currentWorld)
                }

                val phenomenonActivated: Boolean = randomDailyPhenomenon.phenomenonActivation(currentWorld, rolledRate)
                if (phenomenonActivated) {
                    MinecraftOdyssey.instance.dailyPhenomenonActive = true
                }
            }
        }
        else {
            println("Cannot Trigger anymore daily events!")
        }

    }

}