package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenons.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent

object OdysseyNightlyPhenonmenonListener : Listener {

    private val nightlyPhenomenonList = listOf(BloodMoon(), BlueMoon())

    // Cool Down timers
    private var cooldown : Long = 0
    private val cooldownTimer = 10000 //10000 -> 10 sec

    private var cooldownNightPhenomenon: Long = 0
    private val cooldownNightTimer = 12000 // 10 minutes

    @EventHandler
    fun onNightTime(event: PlayerBedEnterEvent) {

        if (MinecraftOdyssey.instance.endGame) {
            val currentWorld = event.player.world

            if (currentWorld.time > 12000) {

                if (!MinecraftOdyssey.instance.nightlyPhenomenonActive) {

                    // Nightly Event Change
                    val timeElapsedMC = event.player.world.fullTime - cooldownNightPhenomenon
                    if (timeElapsedMC > cooldownNightTimer) {
                        cooldownNightPhenomenon = currentWorld.fullTime

                        // Event Cool down timer
                        val timeElapsed = System.currentTimeMillis() - cooldown
                        if (timeElapsed >= cooldownTimer) {
                            cooldown = System.currentTimeMillis()

                            val randomNightlyPhenomenon = nightlyPhenomenonList.random()
                            val rolledRate = (0..100).random()

                            val phenomenonActivated: Boolean = randomNightlyPhenomenon.phenomenonActivation(currentWorld, rolledRate)
                            if (phenomenonActivated) {
                                MinecraftOdyssey.instance.nightlyPhenomenonActive = true
                                event.isCancelled = true
                            }
                        }
                    }
                }
                else {
                    event.isCancelled = true
                    event.player.sendMessage("The night prevents you from sleeping.")
                }
            }
        }
    }
}