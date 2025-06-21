package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable

class RemoveEntityLater(
    val entity: Entity
)  : BukkitRunnable() {

    override fun run() {
        entity.remove()
        return
    }
}