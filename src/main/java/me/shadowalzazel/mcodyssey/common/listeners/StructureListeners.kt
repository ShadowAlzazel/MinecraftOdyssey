package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.StructureHelper
import org.bukkit.HeightMap
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object StructureListeners : Listener, StructureHelper {

    @EventHandler
    fun obeliskTeleportHandler(event: PlayerTeleportEndGatewayEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_GATEWAY) return
        val player = event.player
        val boundedStructures = getBoundedStructures(player) ?: return
        // Get from registry
        val structureRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE)
        val obelisk = structureRegistry.get(NamespacedKey(Odyssey.instance, "obelisk")) ?: return
        // Get dimension
        val edge = Odyssey.instance.edge
        val overworld = Odyssey.instance.overworld
        // Check if inside
        var obeliskTp = false
        for (struct in boundedStructures) {
            // Is Mesa
            if (struct == obelisk) {
                obeliskTp = true
                break
            }
        }
        if (!obeliskTp) return
        // Get the dimension to teleport to
        val newWorld: World = when (player.world) {
            edge -> {
                overworld
            }
            overworld -> {
                edge
            }
            else -> {
                overworld
            }
        }

        // Set new location in the other world
        val newLocation = player.location.clone()
        newLocation.world = newWorld

        // Get the opposite obelisk
        val oppositeObelisk = newLocation.world.locateNearestStructure(
            newLocation,
            obelisk,
            32,
            false
        )

        var tpInsideChamber = false
        // Try to TP inside chamber
        if (oppositeObelisk != null) {
            val oLoc = oppositeObelisk.location.toHighestLocation(HeightMap.MOTION_BLOCKING)
            var foundBlock = false
            var tpBlock: Block? = null
            // radius to search
            println("starting obelisk search at $oLoc")
            val r = 6 // Radius to scan
            for (dy in -30..5) {
                if (foundBlock) break
                for (dz in -r..r) {
                    if (foundBlock) break
                    for (dx in -r..r) {
                        if (foundBlock) break
                        val scanBlock: Block = oLoc.world.getBlockAt(
                            (oLoc.x + dx).toInt(),
                            (oLoc.y + dy).toInt(),
                            (oLoc.z + dz).toInt())
                        if (scanBlock.type == Material.END_GATEWAY) {
                            foundBlock = true
                            tpBlock = scanBlock
                        }
                    }
                }
            }
            // Found TP block run TP
            if (tpBlock != null) {
                println("Running Inside TP Chamber on $player")
                tpInsideChamber = true
                // Do tp logic
                val final = tpBlock.location.clone()
                final.y += 1.0
                final.x += listOf(-2.0, 0.0, 2.0).random()
                final.z += listOf(-2.0, 0.0, 2.0).random()
                player.addPotionEffect(
                    PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 5, 0)
                )
                event.to = final
                player.teleport(final)
            }
        }

        // Fall back if obelisk finder fails
        if (!tpInsideChamber) {
            println("Running Fallback Obelisk TP on $player")
            val final = newLocation.clone().toLocation(newWorld).toHighestLocation(HeightMap.MOTION_BLOCKING)
            final.y += 10.0

            player.addPotionEffect(
                PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 30, 0)
            )

            if (final.y < 0) {
                final.y += 40
            }
            event.to = final
            player.teleport(final)
        }

        event.isCancelled = true
        return
    }

}