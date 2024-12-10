package me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ChargedProjectiles
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

@Suppress("UnstableApiUsage")
class LoadCrossbolter(
    private val entity: LivingEntity,
    private val crossbow: ItemStack,
    private val itemsToLoad: MutableList<ItemStack>
) : BukkitRunnable(), DataTagManager {

    override fun run() {
        if (entity.equipment!!.itemInMainHand == crossbow) {
            // Run
            val projectiles = ChargedProjectiles.chargedProjectiles().addAll(itemsToLoad)
            crossbow.setData(DataComponentTypes.CHARGED_PROJECTILES, projectiles)
            crossbow.removeTag(ItemDataTags.IS_LOADING_AMMO)
            crossbow.damage(1, entity)
        }
        this.cancel()
    }



}