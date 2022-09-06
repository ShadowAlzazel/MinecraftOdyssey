package me.shadowalzazel.mcodyssey.phenomenon.utility

import org.bukkit.World

open class OdysseyPhenomenon(internal val phenomenonName: String, internal val occurrenceRate: Int, internal val growthRate: Int, internal val warningThreshold: Int) {

    open fun rollActivation(someWorld: World, modifier: Int) {
        if (occurrenceRate > ((0..100).random() + modifier)) {
            successfulActivation(someWorld)
        }
        else {
            failedActivation(someWorld)
        }

    }

    open fun successfulActivation(someWorld: World) {
        return
    }

    open fun failedActivation(someWorld: World) {
        return
    }

}
