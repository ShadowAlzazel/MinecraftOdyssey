package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.events.*
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.world.TimeSkipEvent

object OdysseyDailyPhenomenonListener : Listener {

    var endActivation = false

    // Event Chooser
    @EventHandler
    fun onNewDay(event: TimeSkipEvent) {

        if (endActivation) {
            val currentWorld = event.world

            val worldPhenomenonList = listOf(SolarEclipse(), BreezyDay(), SlimeDay(), Earthquake(), BloodMoon(), BlueMoon())
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