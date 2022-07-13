package me.shadowalzazel.mcodyssey.phenomenons

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.World

open class Phenomenon(name: String, rate: Int) {

    val phenomenonName = name
    var occurrenceRate = rate
    open val hasWarning: Boolean = false
    open val currentlyActive = true
    val serverName: String? = MinecraftOdyssey.instance.config.getString("names.server-name")

    open fun phenomenonActivation(phenomenonWorld: World, rollRate: Int): Boolean {
        return if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
            true
        } else {
            println("An uneventful day...")
            false
        }
    }

    open fun phenomenonEffect(phenomenonWorld: World) {
        return
    }

    open fun phenomenonWarning(phenomenonWorld: World): String {
        return "No Warning"
    }
}