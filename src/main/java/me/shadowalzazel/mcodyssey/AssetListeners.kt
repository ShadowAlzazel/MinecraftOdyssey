@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object AssetListeners : Listener {

    private fun resourcePackHandler(joiningPlayer: Player) {
        // PROD
        val resourcePackLink = "https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1"
        // TEST
        //val resourcePackLink = "https://www.dropbox.com/s/6hob8iad499vz95/odyssey-resource-pack-test.zip?dl=1"
        // Put hash into resource-pack-sha1 WITHIN server.properties
        val resourcePackHash = "4274bb16d48bf95aac016801b78a147ee0106730"
        // Prevent double loading screen
        val serverPackHash = joiningPlayer.server.resourcePackHash
        if (serverPackHash == resourcePackHash && joiningPlayer.server.isResourcePackRequired) return
        // Set if not pre-set
        joiningPlayer.setResourcePack(
            resourcePackLink,
            resourcePackHash,
            true,
            Component.text("Please allow Odyssey to download a server side resource pack!", TextColor.color(255, 170, 0))
        )

    }

    // Prompt player with resource pack
    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        resourcePackHandler(event.player)
    }

    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
    }

}