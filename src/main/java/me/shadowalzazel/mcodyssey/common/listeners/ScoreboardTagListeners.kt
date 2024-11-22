package me.shadowalzazel.mcodyssey.common.listeners

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
                EffectTags.MIASMA -> {
                    remove = false
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

}
