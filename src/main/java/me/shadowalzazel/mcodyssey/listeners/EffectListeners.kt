package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.setIntTag
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

object EffectListeners : Listener {

    @EventHandler
    fun mainDamageHandler(event: EntityDamageEvent) {
        if (event.entity !is LivingEntity) return
        val entity = event.entity as LivingEntity
        // Accursed
        if (!entity.isDead) {
            if (entity.scoreboardTags.contains(EffectTags.ACCURSED)) event.damage *= 1.30
        }

        if (entity.scoreboardTags.contains(EffectTags.BARRIER)) {
            // maybe make a PDC
            event.damage = maxOf(event.damage - 3.0, 0.0)
        }

        if (entity.getIntTag(EntityTags.POLLEN_GUARD_STACKS) != null) {
            val pollen = entity.getIntTag(EntityTags.POLLEN_GUARD_STACKS) ?: 0
            event.damage = maxOf(event.damage - pollen, 0.0)
            entity.setIntTag(EntityTags.POLLEN_GUARD_STACKS, maxOf(pollen - 1, 0))
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