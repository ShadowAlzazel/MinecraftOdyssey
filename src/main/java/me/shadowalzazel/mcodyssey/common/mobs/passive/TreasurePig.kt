package me.shadowalzazel.mcodyssey.common.mobs.passive

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import me.shadowalzazel.mcodyssey.common.mobs.statProfile
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Pig
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

object TreasurePig : OdysseyMob(
    displayName = "Treasure Pig",
    tagName = "treasure_pig",
    type = EntityType.PIG,
    stats = statProfile {
        health(100.0, AttributeTags.MOB_HEALTH)
    }
) {

    override fun spawn(world: World, location: Location): Pig {
        val fallingBlock = world.spawn(location.clone().add(0.0, 2.0, 0.0), FallingBlock::class.java) { block ->
            block.blockData = Material.BARREL.createBlockData()
            block.dropItem = false
            block.isPersistent = false
            block.ticksLived = 1
        }

        val pig = (spawnBase(world, location) as Pig).apply {
            addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60, 3))
            addPotionEffect(PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 60 * 60, 2))
            canPickupItems = true
            val treasureLootTable = LootTableManager.getResourceLootTable(
                "entities/treasure_pig", "odyssey")
            lootTable = treasureLootTable
            clearActiveItem()
            addPassenger(fallingBlock)
        }

        FallingBlockTimer(fallingBlock).runTaskTimer(Odyssey.instance, 20 * 5, 20 * 10)
        DroppingLootTask(pig).runTaskTimer(Odyssey.instance, 10, 10)

        return pig
    }

    /**
     * A FallingBlock riding a mob as a passenger never "lands," but vanilla still
     * ages it and will eventually discard/convert it once its internal tick
     * counter passes the safety threshold. This resets that counter on a timer
     * so the block persists indefinitely while it's still around, and cancels
     * itself once the block is gone.
     */
    class FallingBlockTimer(private val fallingBlock: FallingBlock) : BukkitRunnable() {
        override fun run() {
            if (fallingBlock.isDead || !fallingBlock.isValid) {
                cancel()
                return
            }
            fallingBlock.ticksLived = 1
        }
    }

    class DroppingLootTask(private val pig: Pig) : BukkitRunnable() {
        private var counter = 0
        override fun run() {
            if (pig.isDead || counter >= 40) { // 20 seconds
                cancel()
                return
            }
            counter++
            with(pig.world) {
                val runningLootTable = LootTableManager.getResourceLootTable(
                    "gameplay/treasure_pig_running", "odyssey") ?: return@with
                val item = LootTableManager.newItemsFromLootTable(runningLootTable).first()
                dropItemNaturally(pig.location, item)
                if (counter % 5 == 0) {
                    playSound(pig.location, Sound.BLOCK_HEAVY_CORE_BREAK, 0.5F, 0.5F)
                }
                spawnParticle(Particle.BLOCK, pig.location, 35, 0.95, 0.8, 0.95, Material.GOLD_BLOCK.createBlockData())
            }
        }
    }
}