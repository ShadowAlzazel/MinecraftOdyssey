package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

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
