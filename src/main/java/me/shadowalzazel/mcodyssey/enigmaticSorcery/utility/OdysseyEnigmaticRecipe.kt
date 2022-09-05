package me.shadowalzazel.mcodyssey.enigmaticSorcery.utility

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class OdysseyEnigmaticRecipe(private val itemResult: OdysseyItem?, private val mobSummon: OdysseyMob?, private val tribute: List<ItemStack>, private val alterMaterial: Set<Material>, private val lattice: Set<Pair<Double, Double>>) {

    fun validateRecipe(someTributes: Set<Item>, someAlterFire: Block): Boolean {
        for (item in someTributes) {
            if (item.itemStack !in tribute) { return false }
        }
        val location = someAlterFire.location.clone().toCenterLocation().add(0.0, -1.0, 0.0)
        for (lat in lattice) {
            if (location.clone().add(lat.first, 0.0, lat.second).block.type !in alterMaterial) { return false }
        }

        return true
    }

    fun enigmaticResonator(amount: Int, location: Location) {
        val lattice = setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
        for (lat in lattice) {
            val someBLock = location.clone().add(lat.first, -1.0, lat.second).block
            someBLock.type = Material.AIR
        }
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