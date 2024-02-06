package me.shadowalzazel.mcodyssey.listeners

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.data.type.TripwireHook
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

object ArtisanListeners : Listener {

    @EventHandler
    fun waterKeg(event: PlayerInteractEvent) {
        // Check WATER BOTTLE
        val bottle = event.player.equipment.itemInMainHand
        if (bottle.type != Material.POTION) return
        if (!bottle.hasItemMeta()) return
        if ((bottle.itemMeta as PotionMeta).basePotionData.type != PotionType.WATER) return
        // Check hook
        val hook = event.player.rayTraceBlocks(6.0)?.hitBlock ?: return
        if (hook.type != Material.TRIPWIRE_HOOK) return
        val blockData = hook.blockData as TripwireHook
        // Check Direction
        val cardinals = listOf(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)
        val face = blockData.facing
        if (face !in cardinals) return
        // Get keg
        val kegLocation = hook.location.clone().also {
            when(face) {
                BlockFace.NORTH -> {
                    it.z += 1
                }
                BlockFace.EAST -> {
                    it.x -= 1
                }
                BlockFace.SOUTH -> {
                    it.z -= 1
                }
                BlockFace.WEST -> {
                    it.x += 1
                }
                else -> {

                }
            }
        }
        // Get keg structure
        val keg = kegLocation.block
        if (keg.type != Material.BARREL) return
        val kegBottom = kegLocation.clone().add(0.0, -1.0, 0.0).block
        if (kegBottom.type != Material.SCAFFOLDING) return
        if ((keg.blockData as Directional).facing != face) return
        /*
        if (keg.blockData is Barrel) {
            (keg.blockData as Barrel).customName(Component.text("Keg"))
        }
         */
        //(keg as Barrel).customName(Component.text("Keg"))
        println("This is a keg at $kegLocation")



    }
// SELL FOR 64 emeralds

    // PUT OTHER KILN STUFF HERE


}