package me.shadowalzazel.mcodyssey.common.alchemy

import me.shadowalzazel.mcodyssey.common.alchemy.utility.BlockBase
import me.shadowalzazel.mcodyssey.common.alchemy.utility.IngredientChoice
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

class EngulfingRecipe (
    private val result: ItemStack,
    private val fuel: List<Material>, // What type of fire
    private val ingredient: IngredientChoice, // The item thrown in the fire
    private val engulfment: Set<Material>, // The blocks surrounding the source
    private val base: BlockBase=BlockBase.PLUS) : DataTagManager {

    fun validateRecipe(item: Item, fire: Block): Boolean  {
        if (fire.type !in fuel) return false
        if (!ingredient.validateIngredient(item.itemStack)) return false
        val location = fire.location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        base.blockCoords.forEach {
            if (location.clone().add(it.first, it.second, it.third).block.type !in engulfment) { return false }
        }
        return true
    }

    fun successHandler(entity: Item, fire: Block) {
        val location = fire.location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        location.block.type = Material.AIR
        base.blockCoords.forEach {
            location.clone().add(it.first, it.second, it.third).block.type = Material.AIR
        }
        // World Particles
        with(location.world) {
            spawnParticle(Particle.SOUL, location, 35, 0.05, 0.35, 0.05)
            spawnParticle(Particle.SCULK_SOUL, location, 35, 0.25, 0.35, 0.25)
            playSound(location, Sound.PARTICLE_SOUL_ESCAPE, 4.5F, 1.2F)
            playSound(location, Sound.BLOCK_SOUL_SAND_BREAK, 2.5F, 1.2F)
        }
        // Finish
        val item = result.clone()
        item.amount = entity.itemStack.amount
        location.world.dropItem(location.clone().add(0.0, 0.75, 0.0), item)
    }



}