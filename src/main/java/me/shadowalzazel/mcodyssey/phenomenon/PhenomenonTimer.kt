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
        with(MinecraftOdyssey.instance) {
            solarPhenomenonActive = false
            lunarPhenomenonActive = false
            currentSolarPhenomenon = null
            currentLunarPhenomenon = null
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - solarPhenomenonCallCooldown
            if (timeElapsed >= solarPhenomenonTimerConstant) {
                solarPhenomenonCallCooldown = System.currentTimeMillis()
                // Check if end game
                if ((endGame) && (!solarPhenomenonActive)) {
                    val rolledRate = (0..100).random()
                    // Daily luck is not a true daily phenomenon
                    //val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
                    if (mainWorld!!.players.size >= playersRequiredForLuck) {
                        DrawOfFortunes.phenomenonEffect(mainWorld!!)
                    }
                    // Roll for random phenomenon
                    val randomDailyPhenomenon = SolarPhenomena.phenomenaList.random()
                    val phenomenonActivated: Boolean = randomDailyPhenomenon.phenomenonActivation(mainWorld!!, rolledRate)
                    if (phenomenonActivated) {
                        solarPhenomenonActive = true
                        currentSolarPhenomenon = randomDailyPhenomenon
                    }
                }
            }
        }
    }

    // Main function call for moon phenomenon activations
    private fun lunarPhenomenonActivation() {
        MinecraftOdyssey.instance.also {
            it.lunarPhenomenonActive = false
            it.solarPhenomenonActive = false
            it.currentLunarPhenomenon = null
            it.currentSolarPhenomenon = null
            // Event Cool down timer
            val timeElapsed = System.currentTimeMillis() - lunarPhenomenonCallCooldown
            if (timeElapsed >= lunarPhenomenonTimerConstant) {
                lunarPhenomenonCallCooldown = System.currentTimeMillis()
                // Check if end game
                if ((it.endGame) && (!it.lunarPhenomenonActive)) {
                    // Roll for random phenomenon
                    val rolledRate = (0..100).random()
                    val randomNightlyPhenomenon = LunarPhenomena.phenomenaList.random()
                    val phenomenonActivated: Boolean = randomNightlyPhenomenon.phenomenonActivation(mainWorld, rolledRate)
                    if (phenomenonActivated) {
                        it.lunarPhenomenonActive = true
                        it.currentLunarPhenomenon = randomNightlyPhenomenon
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