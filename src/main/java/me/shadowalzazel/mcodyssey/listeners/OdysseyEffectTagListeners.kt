package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EffectTags
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
                    EffectTags.THORNY -> {
                        if (event.damager is LivingEntity) { (event.damager as LivingEntity).damage(2.0) }
                        remove = false
                    }
                    EffectTags.MIASMA -> {
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
                        val accursedZombie = (someVictim.world.spawnEntity(someVictim.location, EntityType.ZOMBIE) as Zombie).apply {
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



    fun todo() {
        TODO("Check if glowing then do more damage")
    }

}
