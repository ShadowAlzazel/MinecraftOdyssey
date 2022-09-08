package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.enigmaticSorcery.EnigmaticSorceryRecipes
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
    fun entityEnigmaticDeath(event: EntityDeathEvent) {
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
    }

    //
    @EventHandler
    fun enigmaticCrucible(event: EntityCombustByBlockEvent) {
        if (event.combuster != null) {
            if (event.combuster!!.type == Material.SOUL_FIRE && event.entity is Item) {
                val someItem = event.entity as Item
                for (enigmaticRecipe in EnigmaticSorceryRecipes.recipeSet) {
                    if (enigmaticRecipe.validateRecipe(setOf(someItem), event.combuster!!)) {
                        enigmaticRecipe.enigmaticResonator(someItem.itemStack.amount, event.combuster!!.location.clone().toCenterLocation()) //?
                        break
                    }
                }
            }
        }

    }


}