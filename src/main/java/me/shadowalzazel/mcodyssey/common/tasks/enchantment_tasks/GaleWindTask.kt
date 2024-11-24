package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable


class GaleWindTask(
    private val entity: LivingEntity,
    private val modifier: Int) : BukkitRunnable()
{
    override fun run() {
        entity.world.playSound(entity.location, Sound.ITEM_TRIDENT_RIPTIDE_2, 2.5F, 1.2F)
        entity.velocity = entity.eyeLocation.direction.clone().normalize().multiply(modifier * 0.5)
        this.cancel()
    }
}
