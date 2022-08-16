package me.shadowalzazel.mcodyssey.mclisteners

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

object OdysseyEffectListeners {

    // Main Function regarding effects and tags
    @EventHandler
    fun mainTagAndEffectHandler(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val someVictim = event.entity
            for (tag in someVictim.scoreboardTags) {
                when (tag) {
                    "TEMP" -> {
                        println("Temp")
                    }
                }
            }
        }
    }


}
