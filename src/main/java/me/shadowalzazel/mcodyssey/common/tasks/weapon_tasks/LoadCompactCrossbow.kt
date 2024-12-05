package me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable

@Suppress("UnstableApiUsage")
class LoadCompactCrossbow(
    private val entity: LivingEntity,
    private val handToLoad: EquipmentSlot,
) : BukkitRunnable(), DataTagManager {

    override fun run() {
        if (entity !is Player) return
        if (entity.inventory.itemInMainHand.type != Material.CROSSBOW) return
        if (entity.inventory.itemInOffHand.type != Material.CROSSBOW) return
        // Load
        val loadedCrossbow = if (handToLoad == EquipmentSlot.HAND) EquipmentSlot.OFF_HAND else EquipmentSlot.HAND
        val projectiles = entity.inventory.getItem(loadedCrossbow).getData(DataComponentTypes.CHARGED_PROJECTILES) ?: return
        val handCrossbow = entity.inventory.getItem(handToLoad)
        handCrossbow.setData(DataComponentTypes.CHARGED_PROJECTILES, projectiles)
    }

}