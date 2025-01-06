package me.shadowalzazel.mcodyssey.server

import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class TimedTag(private val entity: LivingEntity, private val tag: String) : BukkitRunnable() {

    override fun run() {
        entity.removeScoreboardTag(tag)
        this.cancel()
    }

}