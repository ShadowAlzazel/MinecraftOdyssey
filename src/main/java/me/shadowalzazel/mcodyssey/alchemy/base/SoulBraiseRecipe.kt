package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
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
    private val braiseBase: Set<Material>,
    private val lattice: Set<Pair<Double, Double>>) {

    fun validateRecipe(someIngredients: Set<Item>, someAlterFire: Block): Boolean {
        for (item in someIngredients) {
            if (item.itemStack !in braiseIngredients) { return false }
        }
        val location = someAlterFire.location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        for (lat in lattice) {
            if (location.clone().add(lat.first, 0.0, lat.second).block.type !in braiseBase) { return false }
        }
        // Remove item entity for success
        someIngredients.forEach { it.remove() }
        return true
    }

    fun braiseHandler(amount: Int, location: Location) {
        val lattice = setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
        // Remove lattice
        for (lat in lattice) {
            val someBlock = location.clone().add(lat.first, -1.0, lat.second).block
            someBlock.type = Material.AIR
        }
        // Remove block
        location.clone().toCenterLocation().add(0.0, -1.0, 0.0).block.also { it.type = Material.AIR }

        // World Particles
        with(location.world) {
            spawnParticle(Particle.SOUL, location, 35, 0.05, 0.35, 0.05)
            spawnParticle(Particle.SCULK_SOUL, location, 35, 0.25, 0.35, 0.25)
            playSound(location, Sound.PARTICLE_SOUL_ESCAPE, 4.5F, 1.2F)
            playSound(location, Sound.BLOCK_SOUL_SAND_BREAK, 2.5F, 1.2F)
            if (itemResult != null) { dropItem(location.clone().add(0.0, 0.75, 0.0), itemResult.createItemStack(amount)) }
            mobSummon?.createMob(location.world, location)
        }


    }

}