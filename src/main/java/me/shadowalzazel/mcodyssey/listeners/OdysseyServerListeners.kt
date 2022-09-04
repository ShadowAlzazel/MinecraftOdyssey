@file:Suppress("DEPRECATION")

package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import java.awt.TextComponent

object OdysseyServerListeners : Listener {

    // Join Messages
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        // Resource Pack
        if (MinecraftOdyssey.instance.config.getBoolean("server-resource-pack-force")) {

            player.setResourcePack(
                "https://www.dropbox.com/s/6hob8iad499vz95/odyssey-resource-pack-test.zip?dl=1",
                "5b83022a4a142ebee0e39acb4cd00d9a35f41b21",
                true,
                Component.text("Minecraft Odyssey requires its respective resource pack to operate! Please download it to begin your journey!", TextColor.color(255, 170, 0))
            )

            /*
            player.setResourcePack(
                "https://www.dropbox.com/s/6hob8iad499vz95/odyssey-resource-pack-test.zip?dl=1",
                null,
                Component.text("Minecraft Odyssey requires its respective resource pack to operate! Please download it to begin your journey!", TextColor.color(255, 170, 0)),
                true)

             */

            /*
            if (player.resourcePackStatus != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                player.kick(Component.text("Does Not Have Odyssey Resource Pack!"), PlayerKickEvent.Cause.RESOURCE_PACK_REJECTION) }

             */


        }

        // Join message
        val serverName: String = MinecraftOdyssey.instance.config.getString("names.server-name") ?: return
        player.sendMessage("${ChatColor.GRAY}Hello ${player.name}! Welcome!")
        event.joinMessage = ("${ChatColor.GOLD}${player.name} ${ChatColor.YELLOW}has logged into $serverName")


    }

    // Leave Message
    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage = ("${ChatColor.GOLD}${player.name} ${ChatColor.YELLOW}has left the Realm of Vail!")
    }

}



