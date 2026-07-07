package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.common.effects.StatusEffectManager
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.EntitiesLoadEvent


/*
    Master listener for loading, unloading and sweeping.
    USE ONLY for top level loads.
 */
object OdysseyListeners : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(e: PlayerQuitEvent) {
        StatusEffectManager.removeAll(e.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(e: PlayerJoinEvent) {
        OdysseyAssets.resourcePackHandler(e.player)
        StatusEffectManager.sweep(e.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDeath(e: EntityDeathEvent) {
        StatusEffectManager.removeAll(e.entity)
    }

    // Paper event — strips orphaned modifiers off mobs when their chunk loads
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLoad(e: EntitiesLoadEvent) {
        e.entities.filterIsInstance<LivingEntity>().forEach { StatusEffectManager.sweep(it) }
    }

}