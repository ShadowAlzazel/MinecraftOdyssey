package me.shadowalzazel.mcodyssey.phenomenon

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.ankiPhenomena.DrawOfFortunes
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable

class PhenomenonCycle(private val mainWorld: World) : BukkitRunnable() {

    private var sunCount = 0
    private var moonCount = 0

    // Cool down timers
    private var utuPhenomenonCallCooldown: Long = 0
    private var utuPhenomenonTimerConstant: Long = 100000L // 100000 ms -> 100 sec
    private var suenPhenomenonCallCooldown: Long = 0
    private var suenPhenomenonTimerConstant: Long = 100000L // 100000 ms -> 100 sec
    //
    private val suenSet: MutableList<OdysseyPhenomenon> = SuenPhenomena.phenomenaList.shuffled().toMutableList()
    private val utuSet: MutableList<OdysseyPhenomenon> = UtuPhenomena.phenomenaList.shuffled().toMutableList()

    // Main function call for sun phenomenon activations
    private fun utuPhenomenonActivation() {
        MinecraftOdyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - utuPhenomenonCallCooldown
            if (timeElapsed >= utuPhenomenonTimerConstant) {
                utuPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset
                //it.utuPhenomenonActive = false
                //it.currentUtuPhenomenon = null
                it.suenPhenomenonActive = false
                it.currentSuenPhenomenon = null
                // Check if end game
                if (it.endGame && !it.utuPhenomenonActive) {
                    val rolledRate = (0..100).random()
                    // Daily luck is not a true daily phenomenon
                    //val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
                    if (it.mainWorld!!.players.size >= it.playersRequiredForLuck) {
                        DrawOfFortunes.successfulActivation(it.mainWorld!!)
                    }

                    val leadingPhenomenon = utuSet[0]
                    val activated: Boolean = leadingPhenomenon.rollActivation(it.mainWorld!!)
                    if (activated) {
                        it.utuPhenomenonActive = true
                        it.currentUtuPhenomenon = leadingPhenomenon
                        utuSet.remove(leadingPhenomenon)
                        utuSet.add(leadingPhenomenon)
                        if (utuSet[0].criticalWarning()) { utuSet[0].criticalityActivation(it.mainWorld!!.players) }
                    }
                    else {
                        utuSet.remove(leadingPhenomenon)
                        if (!leadingPhenomenon.criticalWarning()) { utuSet.add(3, leadingPhenomenon) }
                        else { utuSet.add(1, leadingPhenomenon) }
                    }
                }
            }
        }
    }

    // Main function call for moon phenomenon activations
    private fun suenPhenomenonActivation() {
        MinecraftOdyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - suenPhenomenonCallCooldown
            if (timeElapsed >= suenPhenomenonTimerConstant) {
                suenPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset
                it.utuPhenomenonActive = false
                it.currentUtuPhenomenon = null
                //it.suenPhenomenonActive = false
                //it.currentSuenPhenomenon = null
                // Check if end game
                if (it.endGame && !it.suenPhenomenonActive) {
                    val leadingPhenomenon = suenSet[0]
                    val activated: Boolean = leadingPhenomenon.rollActivation(it.mainWorld!!)
                    if (activated) {
                        it.suenPhenomenonActive = true
                        it.currentSuenPhenomenon = leadingPhenomenon
                        suenSet.remove(leadingPhenomenon)
                        suenSet.add(leadingPhenomenon)
                        if (suenSet[0].criticalWarning()) { suenSet[0].criticalityActivation(it.mainWorld!!.players) }
                    }
                    else {
                        suenSet.remove(leadingPhenomenon)
                        if (!leadingPhenomenon.criticalWarning()) { suenSet.add(3, leadingPhenomenon) }
                        else { suenSet.add(1, leadingPhenomenon) }
                    }
                }
            }
        }
    }


    override fun run() {
        // Check day times
        if ((23500L <= mainWorld.time) || (mainWorld.time <= 500L)) {
            utuPhenomenonActivation()
            sunCount =+ 1
        }
        else if ((12500L <= mainWorld.time) && (mainWorld.time <= 13500L)) {
            suenPhenomenonActivation()
            moonCount =+ 1
        }
        // Check if active for persistent
        with(MinecraftOdyssey.instance) {
            if (utuPhenomenonActive) {
                currentUtuPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
            else if (suenPhenomenonActive) {
                currentSuenPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
        }
    }

}