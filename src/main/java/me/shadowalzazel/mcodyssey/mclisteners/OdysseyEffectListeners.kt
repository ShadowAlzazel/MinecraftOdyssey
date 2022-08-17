package me.shadowalzazel.mcodyssey.mclisteners

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object OdysseyEffectListeners : Listener {

    // Main Function regarding effects and tags
    @EventHandler
    fun mainTagAndEffectHandler(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val someVictim = event.entity
            val tagsToRemove = mutableListOf<String>()
            // Check Tags
            for (tag in someVictim.scoreboardTags) {
                when (tag) {
                    "Vengeance_Marked_1" -> {
                        event.damage += 2
                        tagsToRemove.add("Vengeance_Marked_1")
                    }
                    "Vengeance_Marked_2" -> {
                        event.damage += 4.5
                        tagsToRemove.add("Vengeance_Marked_2")
                    }
                    "Vengeance_Marked_3" -> {
                        event.damage += 7
                        tagsToRemove.add("Vengeance_Marked_2")
                    }
                }
            }
            // Remove tags
            for (tag in tagsToRemove) {
                someVictim.scoreboardTags.remove(tag)
            }
        }
    }

    fun todo() {
        TODO("Check if glowing then do more damage")
    }

}
