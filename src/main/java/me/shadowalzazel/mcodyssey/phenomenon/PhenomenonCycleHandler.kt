package me.shadowalzazel.mcodyssey.phenomenon

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.recurring_phenomena.DrawOfFortunes
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable

class PhenomenonCycleHandler(private val mainWorld: World) : BukkitRunnable() {

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
        Odyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - utuPhenomenonCallCooldown
            if (timeElapsed >= utuPhenomenonTimerConstant) {
                utuPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset
                //it.utuPhenomenonActive = false
                //it.currentUtuPhenomenon = null
                it.isLunarPhenomenonActive = false
                it.currentLunarPhenomenon = null
                // Check if end game
                if (it.isBossProgressionEnabled && !it.isSolarPhenomenonActive) {
                    val rolledRate = (0..100).random()
                    // Daily luck is not a true daily phenomenon
                    //val luckConfigAmount = Odyssey.instance.config.getInt("player-minimum-for-luck")
                    if (it.mainWorld!!.players.size >= it.playersRequiredForLuck) {
                        DrawOfFortunes.successfulActivation(it.mainWorld!!)
                    }

                    val leadingPhenomenon = utuSet[0]
                    val activated: Boolean = leadingPhenomenon.rollActivation(it.mainWorld!!)
                    if (activated) {
                        it.isSolarPhenomenonActive = true
                        it.currentSolarPhenomenon = leadingPhenomenon
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
        Odyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - suenPhenomenonCallCooldown
            if (timeElapsed >= suenPhenomenonTimerConstant) {
                suenPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset
                it.isSolarPhenomenonActive = false
                it.currentSolarPhenomenon = null
                //it.suenPhenomenonActive = false
                //it.currentSuenPhenomenon = null
                // Check if end game
                if (it.isBossProgressionEnabled && !it.isLunarPhenomenonActive) {
                    val leadingPhenomenon = suenSet[0]
                    val activated: Boolean = leadingPhenomenon.rollActivation(it.mainWorld!!)
                    if (activated) {
                        it.isLunarPhenomenonActive = true
                        it.currentLunarPhenomenon = leadingPhenomenon
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
        with(Odyssey.instance) {
            if (isSolarPhenomenonActive) {
                currentSolarPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
            else if (isLunarPhenomenonActive) {
                currentLunarPhenomenon!!.persistentPlayerActives(mainWorld!!)
            }
        }
    }

}