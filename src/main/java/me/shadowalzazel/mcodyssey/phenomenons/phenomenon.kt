package me.shadowalzazel.mcodyssey.phenomenons

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.World

open class Phenomenon(name: String, rate: Int) {

    val phenomenonName = name
    val occurrenceRate = rate
    val currentlyActive = true
    val serverName: String? = MinecraftOdyssey.instance.config.getString("names.server-name")

    open fun phenomenonActivation(phenomenonWorld: World, rollRate: Int){
        if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
        }
        else {
            println("An uneventful day...")
        }
    }

    open fun phenomenonEffect(phenomenonWorld: World) {
        return
    }
}