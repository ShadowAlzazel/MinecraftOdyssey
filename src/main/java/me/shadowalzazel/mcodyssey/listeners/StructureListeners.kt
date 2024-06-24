package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.HeightMap
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.util.Vector

object StructureListeners : Listener {

    @EventHandler
    fun obeliskTeleportHandler(event: PlayerTeleportEndGatewayEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_GATEWAY) return
        val player = event.player
        val structures = player.location.chunk.structures
        if (structures.isEmpty()) return
        val inside = structures.filter { player.boundingBox.overlaps(it.boundingBox) }
        if (inside.isEmpty()) return
        // Get from registry
        val structureRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE)
        val obelisk = structureRegistry.get(NamespacedKey(Odyssey.instance, "obelisk")) ?: return
        // Get dimension
        val edge = player.server.getWorld(NamespacedKey(Odyssey.instance, "edge")) ?: return
        val overworld = Odyssey.instance.overworld
        // Check if inside
        var obeliskTp = false
        for (struct in inside) {
            // Is Mesa
            if (struct.structure == obelisk) {
                obeliskTp = true
                break
            }
        }
        if (!obeliskTp) return
        val offset = Vector((-3..3).random(), 0, (-3..3).random())
        val location = player.location.clone()
        // Teleport
        when (player.world) {
            edge -> {
                location.world = overworld
            }
            overworld -> {
                location.world = edge
            }
            else -> {
                location.world = overworld
            }
        }
        // Final
        location.add(offset)
        //val final = location.toHighestLocation(HeightMap.WORLD_SURFACE)
        val final = location.toHighestLocation(HeightMap.WORLD_SURFACE_WG)
        event.to = final
        //println(event.to)
        player.teleport(event.to)
        event.isCancelled = true
        return
    }

}