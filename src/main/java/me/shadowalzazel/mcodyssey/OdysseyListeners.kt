package me.shadowalzazel.mcodyssey

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object OdysseyListeners : Listener {

    private const val RESOURCE_PACK_HASH: String = "6ffda6dda0e4a3e4e0c8cdc951e58be0747f55f9"

    private fun resourcePackHandler(joiningPlayer: Player) {
        if (MinecraftOdyssey.instance.config.getBoolean("server-resource-pack-force")) {
            joiningPlayer.setResourcePack(
                "https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1",
                RESOURCE_PACK_HASH,
                true,
                Component.text("Minecraft Odyssey requires its respective resource pack to operate! Please download it to begin your journey!", TextColor.color(255, 170, 0))
            )
        }
    }


    // Join Messages
    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        val player = event.player
        // Join message
        val serverName: String = MinecraftOdyssey.instance.config.getString("names.server-name") ?: return
        player.sendMessage("${ChatColor.GRAY}Hello ${player.name}! Welcome!")
        event.joinMessage = ("${ChatColor.GOLD}${player.name} ${ChatColor.YELLOW}has logged into $serverName")

        // Resource Pack
        resourcePackHandler(player)

    }

    // Leave Message
    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage = ("${ChatColor.GOLD}${player.name} ${ChatColor.YELLOW}has left the Realm of Vail!")
    }

}