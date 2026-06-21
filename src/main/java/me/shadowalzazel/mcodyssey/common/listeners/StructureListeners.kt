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
import org.bukkit.structure.Structure
import org.bukkit.util.Vector

object StructureListeners : Listener, StructureHelper {

    @EventHandler
    fun obeliskTeleportHandler(event: PlayerTeleportEndGatewayEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_GATEWAY) return
        val player = event.player
        val boundedStructures = getBoundedStructures(player) ?: return

        val structureRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE)
        val obelisk = structureRegistry.get(NamespacedKey(Odyssey.instance, "obelisk")) ?: return

        val edge = Odyssey.instance.edge
        val overworld = Odyssey.instance.overworld

        var foundObeliskStructure = false
        for (s in boundedStructures) {
            if (s == obelisk) {
                foundObeliskStructure = true
                break
            }
        }
        if (!foundObeliskStructure) return

        // Cancel the event FIRST to prevent the vanilla gateway teleport from firing
        event.isCancelled = true

        val newWorld: World = when (player.world) {
            edge -> overworld
            overworld -> edge
            else -> overworld
        }

        val newLocation = player.location.clone()
        newLocation.world = newWorld

        val oppositeObelisk = newLocation.world.locateNearestStructure(
            newLocation,
            obelisk,
            32,
            false
        )

        // Defer the actual teleport by 1 tick to fully escape the event call stack
        Odyssey.instance.server.scheduler.runTaskLater(Odyssey.instance, Runnable {
            var tpInsideChamber = false

            if (oppositeObelisk != null) {
                val oLoc = oppositeObelisk.location.toHighestLocation(HeightMap.MOTION_BLOCKING)
                var foundBlock = false
                var tpBlock: Block? = null
                val r = 6
                outer@ for (dy in -30..5) {
                    for (dz in -r..r) {
                        for (dx in -r..r) {
                            val scanBlock = oLoc.world.getBlockAt(
                                (oLoc.x + dx).toInt(),
                                (oLoc.y + dy).toInt(),
                                (oLoc.z + dz).toInt()
                            )
                            if (scanBlock.type == Material.END_GATEWAY) {
                                tpBlock = scanBlock
                                foundBlock = true
                                break@outer
                            }
                        }
                    }
                }

                if (tpBlock != null) {
                    tpInsideChamber = true
                    val final = tpBlock.location.clone()
                    final.y += 1.0
                    final.x += listOf(-2.0, 0.0, 2.0).random()
                    final.z += listOf(-2.0, 0.0, 2.0).random()
                    player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 5, 0))
                    player.teleport(final, PlayerTeleportEvent.TeleportCause.PLUGIN)
                }
            }

            if (!tpInsideChamber) {
                val final = newLocation.toHighestLocation(HeightMap.MOTION_BLOCKING)
                final.y += 10.0
                if (final.y < 0) final.y += 40
                player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 30, 0))
                player.teleport(final, PlayerTeleportEvent.TeleportCause.PLUGIN)
            }
        }, 1L)
    }

}