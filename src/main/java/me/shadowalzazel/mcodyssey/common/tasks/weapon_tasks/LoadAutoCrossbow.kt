package me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ChargedProjectiles
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.scheduler.BukkitRunnable

@Suppress("UnstableApiUsage")
class LoadAutoCrossbow(
    private val entity: LivingEntity,
    private val crossbow: ItemStack,
    private val itemsToLoad: MutableList<ItemStack>
) : BukkitRunnable(), DataTagManager {

    override fun run() {
        if (entity.equipment!!.itemInMainHand == crossbow) {
            val projectiles = ChargedProjectiles.chargedProjectiles().addAll(itemsToLoad)
            crossbow.setData(DataComponentTypes.CHARGED_PROJECTILES, projectiles)
            crossbow.removeTag(ItemDataTags.AUTO_LOADER_LOADING)
            crossbow.damage(2, entity)
            this.cancel()
        }
    }

}