package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.effects.OdysseyEffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyEffectTagListeners : Listener {

    // Main Function regarding effects and tags
    @EventHandler
    fun mainTagAndEffectHandler(event: EntityDamageByEntityEvent) {
        if (event.entity is LivingEntity) {
            val someVictim = event.entity
            val tagsToRemove = mutableListOf<String>()
            // Check Tags
            someVictim.scoreboardTags.forEach {
                var remove = true
                when (it) {
                    "Vengeance_Marked_1" -> {
                        event.damage += 2
                    }
                    "Vengeance_Marked_2" -> {
                        event.damage += 4.5
                    }
                    "Vengeance_Marked_3" -> {
                        event.damage += 7
                    }
                    OdysseyEffectTags.THORNY -> {
                        if (event.damager is LivingEntity) { (event.damager as LivingEntity).damage(2.0) }
                        remove = false
                    }
                    OdysseyEffectTags.PUFFY_PRICKLY -> {
                        if (event.damager is LivingEntity) {
                            (event.damager as LivingEntity).damage(2.0)
                            (event.damager as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.POISON, 20 * 10, 0)) // TODO: Maybe make proper effect
                        }
                        remove = false
                    }
                    else -> {
                        remove = false
                    }
                }
                if (remove) { tagsToRemove.add(it) }
            }
            // Remove tags
            tagsToRemove.forEach { someVictim.scoreboardTags.remove(it) }
        }
    }

    fun todo() {
        TODO("Check if glowing then do more damage")
    }

}
