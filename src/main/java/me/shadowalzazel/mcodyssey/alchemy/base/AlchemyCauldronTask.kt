package me.shadowalzazel.mcodyssey.alchemy.base

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.Levelled
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class AlchemyCauldronTask(private val cauldronBlock: Block, private val result: ItemStack) : BukkitRunnable() {
    private var cooldown = System.currentTimeMillis()
    private var counter = 0
    private val someLocation = cauldronBlock.location.clone().toCenterLocation().add(0.0, 0.15, 0.0)

    override fun run() {
        cauldronBlock.also {
            // Particle Effects
            with(it.world) {
                // Cancel task if not Water Cauldron
                if (getBlockAt(someLocation).type != Material.WATER_CAULDRON) { this@AlchemyCauldronTask.cancel() }
                // Static Particles
                spawnParticle(Particle.BUBBLE, someLocation.clone().add(0.0, 0.25, 0.0), 5, 0.45, 0.25, 0.45)
                // Directional Particles
                val randomLocation = someLocation.clone().add((0..10).random() * 0.15, 0.0, (0..10).random() * 0.15)
                spawnParticle(Particle.BUBBLE_POP, randomLocation, 0, 0.0, 0.2, 0.0)
            }

            counter += 1
            val timeElapsed = System.currentTimeMillis() - cooldown
            if (10 * 20 < counter || timeElapsed > 10 * 1000) {
                brewFinishedHandler()
            }
        }
    }

    private fun brewFinishedHandler() {
        cauldronBlock.run {

            // WORK WITH NBT TAGS!
            //if (cauldronResult.itemMeta.persistentDataContainer.has(NamespacedKey(Odyssey.instance, "item"))) println("KEY!")
            //println(cauldronResult.itemMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING])
            // Change Cauldron Water level
            val levelledData = blockData
            if (blockData is Levelled && (levelledData as Levelled).level != 1) {
                (blockData as Levelled).also {
                    it.level -= 1
                }
            }
            else {
                type = Material.CAULDRON
            }
            // Maker Color particles from color potion
            with(world) {
                dropItem(someLocation.clone().add(0.0, 0.5, 0.0), result)
                playSound(someLocation, Sound.BLOCK_BREWING_STAND_BREW, 2.5F, 0.5F)
                spawnParticle(Particle.DRAGON_BREATH, someLocation, 45, 0.25, 0.25, 0.25)
                getNearbyEntities(location, 2.0, 2.0, 2.0).forEach {
                    /*
                    if (it is Player) {
                        val advancement = it.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/apply_gilded_enchant")!!)
                        if (advancement != null) {
                            it.getAdvancementProgress(advancement).awardCriteria("requirement")
                        }
                    }

                     */
                }
            }
        }
        this.cancel()
    }

}