package me.shadowalzazel.mcodyssey.situations

import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable

class SituationHandler(private val mainWorld: World) : BukkitRunnable() {

    private var utuCallCooldown: Long = 0
    private var utuTimerConstant: Long = 100000L // 100000 ms -> 100 sec





    override fun run() {
        // Check day times for main world
        if ((23500L <= mainWorld.time) || (mainWorld.time <= 1500L)) {


        }

    }


}