package me.shadowalzazel.mcodyssey.world_events.daily_events

import me.shadowalzazel.mcodyssey.world_events.utility.EntityConditions
import net.kyori.adventure.text.Component
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent

open class DailyWorldEvent(
    internal val name: String,
    internal val rate: Int,
    internal val weight: Int,
    internal val activationTime: ActivationTime,
    internal val playerConditions: List<EntityConditions>,
) {

    var pityRate = 0

    // Roll if this event will activate this day
    fun rollTriggered(world: World, bonus: Int = 0): Boolean {
        val rolled = rate + bonus > (0..100).random()
        return rolled
    }

    // Call to various trigger effects
    fun triggerHandler(world: World, isTriggered: Boolean) {
        if (isTriggered) {
            successfulTrigger(world)
        } else {
            failedTrigger(world)
        }
    }

    // Handled after triggered
    fun activate(world: World) {
        activationHandler(world)
    }

    private fun successfulTrigger(world: World) {
        successfulTriggerHandler(world)
    }

    private fun failedTrigger(world: World) {
        failedToTriggerHandler(world)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Utility functions
    internal fun playerChecker(player: Player): Boolean {
        val passed = playerConditions.all { it.checkCondition(player) }
        return passed
    }

    internal fun World.messagePlayers(message: Component) {
        for (player in this.players) {
            player.sendMessage(message)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Open functions for all logic and effects

    // Runs once on activation
    open fun successfulTriggerHandler(world: World) {

    }

    open fun failedToTriggerHandler(world: World) {

    }

    // Runs on activation
    open fun activationHandler(world: World) {
        println("Activated the event ${this.name}")
    }

    // Runs every specified delay
    // create task
    open fun persistentActionHandler(world: World) {
        val players = world.players
        for (p in players) {
            //println("${p.name} has been Detected!!")
        }

    }

    // Pass this function into world listener
    open fun persistentSpawningHandler(event: CreatureSpawnEvent) {

    }



}