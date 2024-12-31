package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent
import me.shadowalzazel.mcodyssey.common.alchemy.BrewingManager
import me.shadowalzazel.mcodyssey.common.alchemy.CauldronManager
import me.shadowalzazel.mcodyssey.common.alchemy.EngulfingManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.CauldronLevelChangeEvent
import org.bukkit.event.entity.EntityCombustByBlockEvent
import org.bukkit.event.inventory.BrewEvent

object AlchemyListeners : Listener, EngulfingManager,
    BrewingManager, CauldronManager {

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun engulfingListener(event: EntityCombustByBlockEvent) {
        if (event.entityType != EntityType.ITEM) return
        engulfingHandler(event)
    }

    @EventHandler
    fun brewingPotionListener(event: BrewEvent) {
        event.contents.ingredient ?: return
        customBrewingHandler(event)
    }

    // Listener for cauldron mechanic
    @EventHandler
    fun cauldronWaterListener(event: CauldronLevelChangeEvent) {
        // Sentry Clauses
        if (event.entity !is Player) return
        if (event.newState.type != Material.WATER_CAULDRON) return
        if (!(event.reason == CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY || event.reason == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY)) return
        // Run cauldron
        cauldronAlchemyHandler(event)
    }

    // Detect when potion is thrown
    @EventHandler
    fun potionThrowHandler(event: PlayerLaunchProjectileEvent) {
        if (event.itemStack.type != Material.SPLASH_POTION  && event.itemStack.type != Material.LINGERING_POTION ) return
        val thrownPotion = event.projectile
        if (thrownPotion !is ThrownPotion) return
        val item = event.itemStack
        if (item.hasTag(ItemDataTags.IS_POTION_VIAL)) {
            event.projectile.velocity = event.projectile.velocity.multiply(2.0)
        }
    }

}
