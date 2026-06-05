package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.RemoveEntityLater
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

object ToolListeners : Listener, EnchantmentManager {


    @EventHandler
    fun mainBLockDropHandler(event: BlockDropItemEvent) {
        if (event.items.isEmpty()) return
        val player = event.player
        if (!player.inventory.itemInMainHand.hasItemMeta()) return
        if (!player.inventory.itemInMainHand.itemMeta.hasEnchants()) return
        val hand = player.inventory.itemInMainHand
        // Loop
        for (enchant in hand.enchantments) {
            when (enchant.key.getNameId()) {
                "pluck" -> {
                    pluckEnchantment(event)
                }
            }
        }
    }

    @EventHandler
    fun mainBlockBreakHandler(event: BlockBreakEvent) {
        if (!event.isDropItems) return
        val player = event.player
        if (!player.inventory.itemInMainHand.hasItemMeta()) return
        if (!player.inventory.itemInMainHand.itemMeta.hasEnchants()) return
        val hand = player.inventory.itemInMainHand
        // Loop
        for (enchant in hand.enchantments) {
            when (enchant.key.getNameId()) {
                "metabolic" -> {
                    metabolicEnchantment(event, enchant.value)
                }
                "lodesight" -> {
                    lodesightEnchantment(event, enchant.value)
                }
            }
        }

    }

    private fun metabolicEnchantment(event: BlockBreakEvent, level: Int) {
        if (level < (0..9).random()) return
        // Regenerate some food
        val player = event.player
        if (player.foodLevel < 20) {
            player.foodLevel = minOf(player.foodLevel + (0..1).random(), 20)
        }
        else {
            player.saturation = minOf(player.saturation + 0.5F, 20F)
        }
    }

    private fun lodesightEnchantment(event: BlockBreakEvent, level: Int) {
        val player = event.player
        val minedBlock = event.block
        // Lists of blocks to skip
        val blockBlackList = listOf(Material.STONE, Material.COBBLESTONE, Material.DIRT, Material.GRASS_BLOCK)

        // Want to skip common blocks
        if (minedBlock.type in blockBlackList) {
            return
        }

        // Delete Old Lodesight Blocks
        player.getNearbyEntities(16.0, 16.0, 16.0).forEach {
            if (it is BlockDisplay && it.scoreboardTags.contains(EntityTags.LODESIGHT_BLOCK)) {
                it.remove()
            }
        }

        val heldItem = player.inventory.itemInMainHand
        var maxCount = 0
        val r = 6 // Radius to scan
        for (dx in -r..r) {
            if (maxCount >= 8) break
            for (dy in -r..r) {
                if (maxCount >= 8) break
                for (dz in -r..r) {
                    if (maxCount >= 8) break
                    // Check radial distance
                    if (dx*dx + dy*dy + dz*dz <= r*r) {
                        val scanBlock: Block = minedBlock.world.getBlockAt(minedBlock.x + dx, minedBlock.y + dy, minedBlock.z + dz)
                        if (scanBlock.type in blockBlackList) continue
                        // Skip if NOT matching
                        if (scanBlock.type != minedBlock.type) continue
                        // Do stuff
                        val blockDisplay = player.world.spawnEntity(scanBlock.location, EntityType.BLOCK_DISPLAY) as BlockDisplay
                        blockDisplay.also {
                            it.block = minedBlock.blockData
                            it.brightness = Display.Brightness(15, 15)
                            it.isGlowing = true
                            it.glowColorOverride = Color.YELLOW
                            it.viewRange = 16F
                            it.isPersistent = false
                            it.addScoreboardTag(EntityTags.LODESIGHT_BLOCK)
                        }
                        heldItem.damage(4, player)
                        // Set to remove
                        val removerTask = RemoveEntityLater(blockDisplay)
                        removerTask.runTaskLater(Odyssey.instance, 20 * 4)
                        maxCount += 1
                    }
                }
            }
        }

    }

    private fun pluckEnchantment(event: BlockDropItemEvent) {
        val items = event.items
        val player = event.player
        for (drop in items) {
            val overflow = player.inventory.addItem(drop.itemStack.clone())
            // Empty -> Success
            if (overflow.isEmpty()) {
                drop.remove()
            }
        }
    }

}