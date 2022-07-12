package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenons.*
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.world.TimeSkipEvent

object OdysseyDailyPhenomenonListener : Listener {

    // End-Game activation
    private var endActivation = true
    private var cooldown : Long = 123
    private var endGame: Boolean = MinecraftOdyssey.instance.config.getBoolean("end-game.enabled")
    private val cooldownTimer = 10000 //10000

    // Event Chooser
    @EventHandler
    fun onNewDay(event: TimeSkipEvent) {

        var timeElapsed = System.currentTimeMillis() - cooldown

        // Event Cool down timer
        if (timeElapsed >= cooldownTimer) {
            cooldown = System.currentTimeMillis()

            if (endActivation or endGame) {
                val currentWorld = event.world

                val worldPhenomenonList = listOf(GravityShift(), BreezyDay(), SolarFlare(), Earthquake(), WorldFamine(), BioluminescentDay(), FairyFollowDay(), ShimmerIntoxication(), SpiritsAwaken(), StoneFlash(), CometDay(), BlazingSoul())
                val randomWorldPhenomenon = worldPhenomenonList.random()
                val rolledRate = (0..100).random()

                //Daily luck
                val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
                if (currentWorld.players.size >= luckConfigAmount) {
                    val drawOfFortunes = DrawOfFortunes()
                    drawOfFortunes.phenomenonEffect(currentWorld)
                }

                randomWorldPhenomenon.phenomenonActivation(currentWorld, rolledRate)
            }
        }
        else {
            println("Cannot Trigger anymore daily events!")
        }

    }

    //Event Activation
    @EventHandler
    fun onDefeatEnderDragon(event: EntityDeathEvent) {
        val dragon = event.entity
        if (dragon.type == EntityType.ENDER_DRAGON) {
            println("The end has started at ${event.entity.world.name}")
            endActivation = true
        }
    }
}