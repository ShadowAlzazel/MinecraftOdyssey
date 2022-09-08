package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import org.bukkit.World

object SlimeShower : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.SUEN,
    70,
    5,
    55,
    Component.text("There is a faint glow emanating from the flora...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("Slime is falling from the Sky at ${someWorld.name}!")
    }

}