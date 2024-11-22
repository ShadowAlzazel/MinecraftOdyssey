package me.shadowalzazel.mcodyssey.common.listeners.utility

import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class TimedTag(private val entity: LivingEntity, private val tag: String) : BukkitRunnable() {

    override fun run() {
        entity.scoreboardTags.remove(tag)
        this.cancel()
    }

}