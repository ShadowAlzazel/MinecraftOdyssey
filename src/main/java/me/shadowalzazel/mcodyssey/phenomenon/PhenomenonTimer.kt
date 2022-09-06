package me.shadowalzazel.mcodyssey.phenomenon

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable

class PhenomenonTimer(private val mainWorld: World) : BukkitRunnable() {

    private var sunCount = 0
    private var moonCount = 0

    // Cool down timers
    private var solarPhenomenonCallCooldown: Long = 0
    private var solarPhenomenonTimerConstant: Long = 100000L // 100000 ms -> 100 sec
    private var lunarPhenomenonCallCooldown: Long = 0
    private var lunarPhenomenonTimerConstant: Long = 100000L // 100000 ms -> 100 sec


    // Main function call for sun phenomenon activations
    private fun solarPhenomenonActivation() {
        MinecraftOdyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - solarPhenomenonCallCooldown
            if (timeElapsed >= solarPhenomenonTimerConstant) {
                solarPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset Night
                it.lunarPhenomenonActive = false
                it.currentLunarPhenomenon = null
                // Check if end game
                if ((it.endGame) && (!it.solarPhenomenonActive)) {
                    val rolledRate = (0..100).random()
                    // Daily luck is not a true daily phenomenon
                    //val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
                    if (it.mainWorld!!.players.size >= it.playersRequiredForLuck) {
                        DrawOfFortunes.phenomenonEffect(it.mainWorld!!)
                    }
                    // Roll for random phenomenon
                    val randomDailyPhenomenon = SolarPhenomena.phenomenaList.random()
                    val phenomenonActivated: Boolean = randomDailyPhenomenon.phenomenonActivation(it.mainWorld!!, rolledRate)
                    if (phenomenonActivated) {
                        it.solarPhenomenonActive = true
                        it.currentSolarPhenomenon = randomDailyPhenomenon
                        println("Activated $randomDailyPhenomenon")
                    }
                }
            }
        }
    }

    // Main function call for moon phenomenon activations
    private fun lunarPhenomenonActivation() {
        MinecraftOdyssey.instance.also {
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - lunarPhenomenonCallCooldown
            if (timeElapsed >= lunarPhenomenonTimerConstant) {
                lunarPhenomenonCallCooldown = System.currentTimeMillis()
                // Reset Day
                it.solarPhenomenonActive = false
                it.currentSolarPhenomenon = null
                // Check if end game
                if ((it.endGame) && (!it.lunarPhenomenonActive)) {
                    // Roll for random phenomenon
                    val rolledRate = (0..100).random()
                    val randomNightlyPhenomenon = LunarPhenomena.phenomenaList.random()
                    val phenomenonActivated: Boolean = randomNightlyPhenomenon.phenomenonActivation(mainWorld, rolledRate)
                    if (phenomenonActivated) {
                        it.lunarPhenomenonActive = true
                        it.currentLunarPhenomenon = randomNightlyPhenomenon
                        println("Activated $randomNightlyPhenomenon")
                    }
                }
            }
        }
    }




    override fun run() {
        // Check day times
        if ((23500L <= mainWorld.time) || (mainWorld.time <= 500L)) {
            solarPhenomenonActivation()
            sunCount =+ 1
        }
        else if ((12500L <= mainWorld.time) && (mainWorld.time <= 13500L)) {
            lunarPhenomenonActivation()
            moonCount =+ 1
        }
    }

}