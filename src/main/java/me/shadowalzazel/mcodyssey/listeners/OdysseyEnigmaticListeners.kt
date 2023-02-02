package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.alchemy.SoulBraiseRecipes
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustByBlockEvent
import org.bukkit.event.entity.EntityDeathEvent

object OdysseyEnigmaticListeners : Listener {


    @EventHandler
    fun soulItemHandlers(event: EntityDeathEvent) {
        if (event.entity.location.world.environment == World.Environment.NETHER)  {
            event.entity.also {
                val blockUnderneath = it.location.clone().add(0.0 , -0.5, 0.0).block
                if (blockUnderneath.biome == Biome.SOUL_SAND_VALLEY && (blockUnderneath.type == Material.SOUL_SAND || blockUnderneath.type == Material.SOUL_SOIL)) {
                    if (event.droppedExp >= 6) {
                        val randomRoll = (0..100).random()
                        if (25 > randomRoll) {
                            event.deathSound = Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD
                            with(blockUnderneath.world) {
                                spawnParticle(Particle.SOUL, it.location, 35, 0.05, 0.35, 0.05)
                                spawnParticle(Particle.SCULK_SOUL, it.location, 35, 0.25, 0.35, 0.25)
                                playSound(it.location, Sound.PARTICLE_SOUL_ESCAPE, 4.5F, 1.2F)
                                playSound(it.location, Sound.BLOCK_SOUL_SAND_BREAK, 2.5F, 1.2F)
                                dropItem(it.location, OdysseyItems.ECTOPLASM.createItemStack((1..3).random()))
                            }
                        }
                    }
                }
            }
        }

        // Checks if more exp dropped from items
        if (event.entity.killer != null && event.droppedExp > 0) {
            var expDrop = 0.0
            val someKiller = event.entity.killer!!
            // Weapon
            if (someKiller.equipment.itemInMainHand.hasItemMeta()) {
                if (someKiller.equipment.itemInMainHand.itemMeta.hasCustomModelData()) {
                    if (someKiller.equipment.itemInMainHand.itemMeta.customModelData == OdysseyItemModels.SOUL_STEEL_KATANA) {
                        expDrop += 0.15
                    }
                }
            }
            // Charm
            if (someKiller.equipment.itemInOffHand.hasItemMeta()) {
                if (someKiller.equipment.itemInOffHand.itemMeta.hasCustomModelData()) {
                    if (someKiller.equipment.itemInOffHand.itemMeta.customModelData == OdysseyItemModels.ENIGMATIC_OMAMORI) {
                        expDrop += 0.1
                    }
                }
            }
            // Helmet
            if (someKiller.equipment.helmet?.hasItemMeta() == true) {
                if (someKiller.equipment.helmet.itemMeta.hasCustomModelData()) {
                    if (someKiller.equipment.helmet.itemMeta.customModelData == OdysseyItemModels.SOUL_STEEL_HELMET) {
                        expDrop += 0.15
                    }
                }
            }

            // ADD EXP
            event.droppedExp += (event.droppedExp * (1 + expDrop)).toInt()
            // Check for particles
            if (expDrop > 0.0) {
                with(someKiller.world) {
                    spawnParticle(Particle.SOUL, someKiller.location, 8, 0.05, 0.25, 0.05)
                    spawnParticle(Particle.SCULK_SOUL, someKiller.location, 9, 0.15, 0.25, 0.15)
                }
            }
        }

    }


    //
    @EventHandler
    fun soulBraiseBurnHandler(event: EntityCombustByBlockEvent) {
        if (event.combuster != null) {
            if (event.combuster!!.type == Material.SOUL_FIRE && event.entity is Item) {
                val someItem = event.entity as Item
                for (enigmaticRecipe in SoulBraiseRecipes.recipeSet) {
                    if (enigmaticRecipe.validateRecipe(setOf(someItem), event.combuster!!)) {
                        enigmaticRecipe.braiseHandler(someItem.itemStack.amount, event.combuster!!.location.clone().toCenterLocation()) //?
                        break
                    }
                }
            }
        }

    }


}