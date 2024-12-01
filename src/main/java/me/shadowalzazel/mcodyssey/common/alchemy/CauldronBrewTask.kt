package me.shadowalzazel.mcodyssey.common.alchemy

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.Levelled
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class CauldronBrewTask(
    private val cauldron: Block,
    private val result: ItemStack) : BukkitRunnable() {

    private val location: Location = cauldron.location.clone().toCenterLocation().add(0.0, 0.15, 0.0)
    private var counter = 0
    private var cooldown = System.currentTimeMillis()

    // ??? Do mini-game
    // when hear ding, add chorus fruit for +10%

    override fun run() {

        // Particle Effects
        with(cauldron.world) {
            // Cancel task if not Water Cauldron
            if (getBlockAt(location).type != Material.WATER_CAULDRON) { this@CauldronBrewTask.cancel() }
            // Static Particles
            spawnParticle(Particle.BUBBLE, location.clone().add(0.0, 0.25, 0.0), 5, 0.45, 0.25, 0.45)
            // Directional Particles
            val randomLocation = location.clone().add((0..10).random() * 0.15, 0.0, (0..10).random() * 0.15)
            spawnParticle(Particle.BUBBLE_POP, randomLocation, 0, 0.0, 0.2, 0.0)
        }
        counter += 1
        val timeElapsed = System.currentTimeMillis() - cooldown
        if (10 * 20 < counter || timeElapsed > 10 * 1000) {
            finishHandler()
            this.cancel()
        }
    }


    @Suppress("UnstableApiUsage")
    private fun finishHandler() {
        // Lower Water
        val blockData = cauldron.blockData
        if (blockData is Levelled && blockData.level != 1) {
            blockData.level -= 1
            cauldron.blockData = blockData
        } else {
            cauldron.type = Material.CAULDRON
        }
        // Maker Color particles from color potion
        with(cauldron.world) {
            dropItem(location.clone().add(0.0, 0.5, 0.0), result.clone())
            playSound(location, Sound.BLOCK_BREWING_STAND_BREW, 2.5F, 0.5F)
            spawnParticle(Particle.DRAGON_BREATH, location, 15, 0.25, 0.25, 0.25)
            // Color
            val potionData = result.getData(DataComponentTypes.POTION_CONTENTS)
            if (potionData != null) {
                val color = potionData.customColor()
                if (color != null) {
                    location.world.spawnParticle(Particle.ENTITY_EFFECT, location, 20, 0.03, 0.1, 0.03, color)
                }
            }
        }
    }


}