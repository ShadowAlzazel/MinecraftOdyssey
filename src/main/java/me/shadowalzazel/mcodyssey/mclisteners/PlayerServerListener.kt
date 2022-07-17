package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object OdysseyPlayerJoinListener : Listener {

    // Join Message
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val serverName: String = MinecraftOdyssey.instance.config.getString("names.server-name") ?: return
        player.sendMessage("${ChatColor.LIGHT_PURPLE}Hello ${player.name}! Welcome to $serverName")
        event.joinMessage = ("${ChatColor.GOLD}${player.name} has Joined the Realm of Vail!")
    }

}

object OdysseyPlayerLeaveListener : Listener {

    // Leave Message
    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage = ("${ChatColor.GOLD}${player.name} has left the Realm of Vail!")
    }

}

object OdysseyPlayerDeathListener : Listener {

    // Death Message
}

