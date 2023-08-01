package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Villager
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ScoreboardTagListeners : Listener {

    // Main Function regarding effects and tags
    // TODO: Fix
    @EventHandler
    fun mainTagAndEffectHandler(event: EntityDamageByEntityEvent) {
        if (event.entity !is LivingEntity) {
            return
        }

        val tagsToRemove = mutableListOf<String>()
        // Check Tags
        event.entity.scoreboardTags.forEach {
            var remove = true
            when (it) {
                EntityTags.MARKED_FOR_VENGEANCE -> {
                    event.damage += 4
                }
                EffectTags.ASPHYXIATE -> {
                    remove = false
                    if (event.damager is LivingEntity) { (event.damager as LivingEntity).damage(2.0) }
                }
                EffectTags.MIASMA -> {
                    remove = false
                    if (event.damager is LivingEntity) {
                        (event.damager as LivingEntity).damage(2.0)
                        (event.damager as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.POISON, 20 * 10, 0)) // TODO: Maybe make proper effect
                    }
                }
                else -> {
                    remove = false
                }
            }
            if (remove) { tagsToRemove.add(it) }
        }
        // Remove tags
        tagsToRemove.forEach { event.entity.scoreboardTags.remove(it) }
    }

    @EventHandler
    fun mainTagAndEffectDeathHandler(event: EntityDeathEvent) {
        val someVictim = event.entity
        someVictim.scoreboardTags.forEach {
            when (it) {
                EffectTags.ACCURSED -> {
                    if (someVictim.type == EntityType.VILLAGER) {
                        (someVictim as Villager).zombify()
                        someVictim.scoreboardTags.remove(it)
                    }
                    else if (someVictim.type == EntityType.PLAYER) {
                        (someVictim.world.spawnEntity(someVictim.location, EntityType.ZOMBIE) as Zombie).apply {
                            setShouldBurnInDay(false)
                            canPickupItems = true
                            isPersistent = true
                            customName(Component.text("Accursed Poltergeist", TextColor.color(137, 24, 40)))
                            isCustomNameVisible = true
                            addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 9999, 4))
                        }
                        someVictim.scoreboardTags.remove(it)
                    }
                }
            }
        }
    }

}
