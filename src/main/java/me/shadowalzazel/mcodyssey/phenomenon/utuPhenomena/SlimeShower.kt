package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World

object SlimeShower : OdysseyPhenomenon("Slime Shower",
    PhenomenonTypes.SUEN,
    70,
    5,
    15,
    55,
    Component.text("There are strange flashes in the sky...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("Slime is falling from the Sky at ${someWorld.name}!")

        for (somePlayer in someWorld.players) { somePlayer.sendMessage(Component.text("Slime is falling from the sky?", TextColor.color(107, 162, 105))) }

    }

}