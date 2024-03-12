package me.shadowalzazel.mcodyssey.listeners.utility

import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.removeTag
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.scheduler.BukkitRunnable

class LoadAutoCrossbow(
    private val entity: LivingEntity,
    private val crossbow: ItemStack,
    private val loaded: MutableList<ItemStack>
) : BukkitRunnable() {

    override fun run() {
        if (entity.equipment!!.itemInMainHand == crossbow) {
            val meta = (crossbow.itemMeta).clone() as CrossbowMeta
            for (item in loaded) {
                meta.addChargedProjectile(item)
            }
            crossbow.itemMeta = meta
            crossbow.removeTag(ItemTags.AUTO_LOADER_LOADING)
            crossbow.damage(2, entity)
        }
    }

}