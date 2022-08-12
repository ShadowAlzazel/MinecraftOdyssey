package me.shadowalzazel.mcodyssey.alchemy.utility

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.Levelled
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class AlchemyTask(private val someLocation: Location, private val someResult: ItemStack) : BukkitRunnable() {
    private var someCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        // some timer
        counter += 1

        val timeElapsed = System.currentTimeMillis() - someCooldown

        if (someLocation.world.getBlockAt(someLocation).type != Material.WATER_CAULDRON) {
            println(someLocation.world.getBlockAt(someLocation).type)
            println("Destroyed!")
            this.cancel()
        }
        someLocation.world.spawnParticle(Particle.WATER_BUBBLE, someLocation.clone().add(0.0, 0.25, 0.0), 5, 0.45, 0.25, 0.45)
        someLocation.world.spawnParticle(Particle.BUBBLE_POP, someLocation.clone().add(0.0, 0.5, 0.0), 1)


        if (20 * 10 < counter || timeElapsed > 10 * 1000) {
            println("Finished!")
            val someCauldron = someLocation.world.getBlockAt(someLocation)
            val levelledData = someCauldron.blockData as Levelled
            if (levelledData.level != 1) {
                val newLevelledData = someCauldron.blockData.clone() as Levelled
                newLevelledData.level -= 1
                someCauldron.blockData = newLevelledData
            }
            else {
                someCauldron.type = Material.CAULDRON
            }

            // Maker Color particles from color potion
            someLocation.world.dropItem(someLocation.clone().add(0.0, 0.5, 0.0), someResult)
            someLocation.world.playSound(someLocation, Sound.BLOCK_BREWING_STAND_BREW, 2.5F, 0.5F)
            someLocation.world.spawnParticle(Particle.DRAGON_BREATH, someLocation, 45, 0.25, 0.25, 0.25)
            this.cancel()
        }
    }
}