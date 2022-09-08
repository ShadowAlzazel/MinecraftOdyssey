package me.shadowalzazel.mcodyssey.phenomenon

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.ankiPhenomena.DrawOfFortunes
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


    // Main function call for sun phenomenon activations
    private fun utuPhenomenonActivation() {
        MinecraftOdyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - utuPhenomenonCallCooldown
            if (timeElapsed >= utuPhenomenonTimerConstant) {
                utuPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset Night
                it.suenPhenomenonActive = false
                it.currentSuenPhenomenon = null
                // Check if end game
                if ((it.endGame) && (!it.utuPhenomenonActive)) {
                    val rolledRate = (0..100).random()
                    // Daily luck is not a true daily phenomenon
                    //val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
                    if (it.mainWorld!!.players.size >= it.playersRequiredForLuck) {
                        DrawOfFortunes.successfulActivation(it.mainWorld!!)
                    }
                    // Roll for random phenomenon
                    val randomDailyPhenomenon = UtuPhenomena.phenomenaList.random()
                    val phenomenonActivated: Boolean = randomDailyPhenomenon.rollActivation(it.mainWorld!!, rolledRate)
                    if (phenomenonActivated) {
                        it.utuPhenomenonActive = true
                        it.currentUtuPhenomenon = randomDailyPhenomenon
                        println("Activated $randomDailyPhenomenon")
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
                // Reset Day
                it.utuPhenomenonActive = false
                it.currentUtuPhenomenon = null
                // Check if end game
                if ((it.endGame) && (!it.suenPhenomenonActive)) {
                    // Roll for random phenomenon
                    val rolledRate = (0..100).random()
                    val randomNightlyPhenomenon = SuenPhenomena.phenomenaList.random()
                    val phenomenonActivated: Boolean = randomNightlyPhenomenon.rollActivation(mainWorld, rolledRate)
                    if (phenomenonActivated) {
                        it.suenPhenomenonActive = true
                        it.currentSuenPhenomenon = randomNightlyPhenomenon
                        println("Activated $randomNightlyPhenomenon")
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
    }

}