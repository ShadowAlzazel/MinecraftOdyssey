package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

object EffectListeners : Listener {

    @EventHandler
    fun mainDamageHandler(event: EntityDamageEvent) {
        // Accursed
        if (!event.entity.isDead) {
            val entity = event.entity
            if (entity.scoreboardTags.contains(EffectTags.ACCURSED)) event.damage *= 1.30
        }
    }


    @EventHandler
    fun hungerRegenHandler(event: FoodLevelChangeEvent) {
        if (event.entity.scoreboardTags.contains(EffectTags.MIASMA)) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun healthRegenHandler(event: EntityRegainHealthEvent) {
        if (event.entity.scoreboardTags.contains(EffectTags.IRRADIATED)) {
            event.isCancelled = true
            return
        }
    }

}