package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.alchemy.utility.BraiseBase
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyItemTag
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class SoulBraiseRecipe(
    private val itemResult: OdysseyItem?,
    private val mobSummon: OdysseyMob?,
    private val braiseIngredients: List<ItemStack>,
    private val braiseMaterial: Set<Material>,
    private val braiseBase: BraiseBase) {

    fun validateRecipe(itemSet: Set<Item>, fireBlock: Block): Boolean {
        // Check items
        itemSet.forEach {
            if (it.itemStack !in braiseIngredients) {
                // Check for type
                for (ingredient in braiseIngredients) {
                    if (ingredient.type == it.itemStack.type && !ingredient.hasOdysseyItemTag()) {
                        return true
                    }
                }
                return false
            }
        }
        // Check blocks
        val braiseLocation = fireBlock.location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        braiseBase.blockCoords.forEach {
            if (braiseLocation.clone().add(it.first, it.second, it.third).block.type !in braiseMaterial) { return false }
        }

        // Remove item entity for success
        itemSet.forEach { it.remove() }
        return true
    }

    fun braiseSuccessHandler(amount: Int, location: Location): Entity? {
        // Remove blocks
        val braiseLocation = location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        braiseLocation.block.type = Material.AIR
        braiseBase.blockCoords.forEach {
            braiseLocation.clone().add(it.first, it.second, it.third).block.type = Material.AIR
        }

        // World Particles
        with(location.world) {
            spawnParticle(Particle.SOUL, location, 35, 0.05, 0.35, 0.05)
            spawnParticle(Particle.SCULK_SOUL, location, 35, 0.25, 0.35, 0.25)
            playSound(location, Sound.PARTICLE_SOUL_ESCAPE, 4.5F, 1.2F)
            playSound(location, Sound.BLOCK_SOUL_SAND_BREAK, 2.5F, 1.2F)
        }

        if (itemResult != null) {
            return location.world.dropItem(location.clone().add(0.0, 0.75, 0.0), itemResult.createItemStack(amount))
        } else {
            return mobSummon?.createMob(location.world, location)
        }

    }

}