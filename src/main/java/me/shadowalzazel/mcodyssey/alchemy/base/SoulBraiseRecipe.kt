package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.alchemy.utility.BraiseBase
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class SoulBraiseRecipe(
    private val itemResult: OdysseyItem?,
    private val mobSummon: OdysseyMob?,
    private val braiseIngredients: List<ItemStack>,
    private val braiseMaterial: Set<Material>,
    private val braiseBase: BraiseBase) {

    fun validateRecipe(someIngredients: Set<Item>, fireBlock: Block): Boolean {
        // Check items
        someIngredients.forEach {
            if (it.itemStack !in braiseIngredients) { return false }
        }
        // Check blocks
        val braiseLocation = fireBlock.location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        braiseBase.blockCoords.forEach {
            if (braiseLocation.clone().add(it.first, it.second, it.third).block.type !in braiseMaterial) { return false }
        }

        // Remove item entity for success
        someIngredients.forEach { it.remove() }
        return true
    }

    fun braiseSuccessHandler(amount: Int, someLocation: Location) {
        // Remove blocks
        val braiseLocation = someLocation.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        braiseLocation.block.type = Material.AIR
        braiseBase.blockCoords.forEach {
            braiseLocation.clone().add(it.first, it.second, it.third).block.type = Material.AIR
        }

        // World Particles
        with(someLocation.world) {
            spawnParticle(Particle.SOUL, someLocation, 35, 0.05, 0.35, 0.05)
            spawnParticle(Particle.SCULK_SOUL, someLocation, 35, 0.25, 0.35, 0.25)
            playSound(someLocation, Sound.PARTICLE_SOUL_ESCAPE, 4.5F, 1.2F)
            playSound(someLocation, Sound.BLOCK_SOUL_SAND_BREAK, 2.5F, 1.2F)
        }

        if (itemResult != null) { someLocation.world.dropItem(someLocation.clone().add(0.0, 0.75, 0.0), itemResult.createItemStack(amount)) }
        mobSummon?.createMob(someLocation.world, someLocation)


    }

}