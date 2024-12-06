package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.StructureHelper
import org.bukkit.HeightMap
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
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
        val offset = Vector((-3..3).random(), 0, (-3..3).random())
        val location = player.location.clone()
        // Teleport
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
        // Final
        location.world = newWorld
        location.add(offset)
        //val final = location.toHighestLocation(HeightMap.WORLD_SURFACE)
        val final = location.toHighestLocation(HeightMap.WORLD_SURFACE).toLocation(newWorld)
        event.to = final
        //println(event.to)
        player.teleport(final)
        event.isCancelled = true
        return
    }

}