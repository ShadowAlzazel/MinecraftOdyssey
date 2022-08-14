package me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.World

object SlimeStorm : DailyPhenomenon("Slime_Storm", 80, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World){
        println("Slime is falling from the Sky at ${phenomenonWorld.name}!")

    }

}