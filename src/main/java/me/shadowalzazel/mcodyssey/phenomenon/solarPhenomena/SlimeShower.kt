package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import org.bukkit.World

object SlimeShower : SolarPhenomenon("Slime_Shower", 80, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World){
        println("Slime is falling from the Sky at ${phenomenonWorld.name}!")

    }
    fun g() {
        TODO("Create")
    }

}