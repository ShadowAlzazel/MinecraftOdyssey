package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenons.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent

object OdysseyNightlyPhenonmenonListener : Listener {

    private var cooldown: Long = 0
    private val cooldownTimer = 100 //10000 -> 10 sec

    @EventHandler
    fun onNightTime(event: PlayerBedEnterEvent) {

        val timeElapsed = System.currentTimeMillis() - cooldown

        if (MinecraftOdyssey.instance.endGame) {
            val currentWorld = event.player.world
            if ((timeElapsed >= cooldownTimer) && (cooldown > 10)) {
                cooldown = System.currentTimeMillis()



                if (currentWorld.time > 12000) {
                    val nightlyPhenomenonList = listOf(BloodMoon(), BlueMoon())
                    val randomNightlyPhenomenon = nightlyPhenomenonList.random()
                    val rolledRate = (0..100).random()


                    if (!MinecraftOdyssey.instance.nightlyPhenomenonActive)
                        MinecraftOdyssey.instance.nightlyPhenomenonActive = true

                }

            }
        }
    }

}