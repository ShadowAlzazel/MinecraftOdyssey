package me.shadowalzazel.mcodyssey.world_events.daily_events.day_events

import me.shadowalzazel.mcodyssey.world_events.daily_events.ActivationTime
import me.shadowalzazel.mcodyssey.world_events.daily_events.DailyWorldEvent
import me.shadowalzazel.mcodyssey.world_events.utility.EntityConditions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World

class SlimeShower : DailyWorldEvent(
    "slime_shower",
    4,
    4,
    ActivationTime.Custom(3000, 15000),
    listOf(EntityConditions.AlwaysTrue),
) {


    override fun successfulTriggerHandler(world: World) {
        val text = "Blobs cloud and clump the sky..."
        val hint = Component.text(text, TextColor.color(155, 155, 155))
        world.messagePlayers(hint)
    }

    override fun activationHandler(world: World) {
        val text = "Slime is falling from the sky?"
        val message = Component.text(text, TextColor.color(107, 162, 105))
        world.messagePlayers(message)
    }


}