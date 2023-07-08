package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.alchemy.SoulBraiseRecipes
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.items.Ingredients
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

object SoulBraisingListener : Listener {

    // Spirit Mobs,
    // HAVE IN-VIS AND GLOWING, are summoned to fight and other cool stuff.

    @EventHandler
    fun soulItemHandlers(event: EntityDeathEvent) {
        if (event.entity.location.world.environment == World.Environment.NETHER)  {
            itemDropHandler(event)
        }

        if (event.entity.killer != null && event.droppedExp > 0) {
            soulExpHandler(event)
        }

    }

    @EventHandler
    fun soulBraiseBurnHandler(event: EntityCombustByBlockEvent) {
        if (event.combuster == null) return
        if (event.entity !is Item) return
        if (event.combuster!!.type != Material.SOUL_FIRE) return

        val item = event.entity as Item
        // CAN ASYNC?!
        for (recipe in SoulBraiseRecipes.BRAISE_SET) {
            if (recipe.validateRecipe(listOf(item).toSet(), event.combuster!!)) {
                recipe.braiseSuccessHandler(item.itemStack.amount, event.combuster!!.location)
                break
            }
        }
    }

    private fun soulExpHandler(event: EntityDeathEvent) {
        var expDrop = 0.0
        val killer = event.entity.killer!!

        with(killer.equipment) {
            val hasSoulSteelWeapon = if (itemInMainHand.itemMeta?.hasCustomModelData() == true) {
                itemInMainHand.hasTag(ItemTags.SOUL_STEEL_TOOL)
            } else {
                false
            }
            val hasOmamori = if (itemInOffHand.itemMeta?.hasCustomModelData() == true) {
                itemInOffHand.itemMeta.customModelData == ItemModels.ENIGMATIC_OMAMORI
            } else {
                false
            }
            val hasSoulSteelHelmet = if (helmet?.itemMeta?.hasCustomModelData() == true) {
                helmet.itemMeta.customModelData  == ItemModels.SOUL_STEEL_HELMET
            } else {
                false
            }

            if (hasSoulSteelWeapon) expDrop += 0.15
            if (hasOmamori) expDrop += 0.1
            if (hasSoulSteelHelmet) expDrop += 0.15
        }

        event.droppedExp += (event.droppedExp * (1 + expDrop)).toInt()
        if (expDrop > 0.0) {
            with(killer.world) {
                spawnParticle(Particle.SOUL, killer.location, 8, 0.05, 0.25, 0.05)
                spawnParticle(Particle.SCULK_SOUL, killer.location, 9, 0.15, 0.25, 0.15)
            }
        }
    }

    private fun itemDropHandler(event: EntityDeathEvent) {
        val location = event.entity.location
        val blockUnder = event.entity.location.clone().add(0.0 , -0.5, 0.0).block
        if (blockUnder.biome != Biome.SOUL_SAND_VALLEY) return
        if (!(blockUnder.type == Material.SOUL_SAND || blockUnder.type == Material.SOUL_SOIL)) return

        if (event.droppedExp >= 6) {
            val randomRoll = (0..100).random()
            if (25 > randomRoll) {
                event.deathSound = Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD
                with(blockUnder.world) {
                    spawnParticle(Particle.SOUL, location, 35, 0.05, 0.35, 0.05)
                    spawnParticle(Particle.SCULK_SOUL, location, 35, 0.25, 0.35, 0.25)
                    playSound(location, Sound.PARTICLE_SOUL_ESCAPE, 4.5F, 1.2F)
                    playSound(location, Sound.BLOCK_SOUL_SAND_BREAK, 2.5F, 1.2F)
                    dropItem(location, Ingredients.ECTOPLASM.createItemStack((1..3).random()))
                }
            }
        }
    }


}