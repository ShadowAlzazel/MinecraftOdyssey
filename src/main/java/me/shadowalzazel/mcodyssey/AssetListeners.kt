package me.shadowalzazel.mcodyssey

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object AssetListeners : Listener {


    private fun resourcePackHandler(joiningPlayer: Player) {

        val resourcePackLink = "https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1"
        val resourcePackHash = "6ffda6dda0e4a3e4e0c8cdc951e58be0747f55f9"

        joiningPlayer.setResourcePack(
            resourcePackLink,
            resourcePackHash,
            true,
            Component.text("Odyssey requires its respective resource pack to function properly! Please download it to begin your journey!", TextColor.color(255, 170, 0))
        )
    }

    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        // Force the player to use the resource pack
        resourcePackHandler(event.player)

    }

    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
        val player = event.player
    }

}