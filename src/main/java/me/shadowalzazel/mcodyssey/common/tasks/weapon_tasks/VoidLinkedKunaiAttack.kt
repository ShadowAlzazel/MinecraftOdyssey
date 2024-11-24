package me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.log2

class VoidLinkedKunaiAttack(
    private val entity: LivingEntity,
    private val kunai: ItemStack,
    private val target: Entity
) : BukkitRunnable() {

    override fun run() {
        if (entity.equipment!!.itemInMainHand == kunai) {
            val voidLinkedChunk = target.chunk
            voidLinkedChunk.addPluginChunkTicket(Odyssey.instance)
            voidLinkedChunk.load()
            // Get log distance
            val distance = entity.location.distance(target.location)
            val log2Amount = maxOf(log2(distance).toInt(), 0)
            //if (target is ArmorStand) 0 else 3 // Change to mod?s
            entity.attack(target)// Change to damage source in 1.20.4/5
            kunai.damage(log2Amount, entity)
            voidLinkedChunk.removePluginChunkTicket(Odyssey.instance)
            this.cancel()
        }
    }

}