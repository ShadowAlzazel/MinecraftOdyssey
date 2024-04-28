@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object AssetListeners : Listener {

    private fun resourcePackHandler(joiningPlayer: Player) {

        // PROD
        //val resourcePackLink = "https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1"
        // TEST
        val resourcePackLink = "https://www.dropbox.com/s/6hob8iad499vz95/odyssey-resource-pack-test.zip?dl=1"

        // Put hash into resource-pack-sha1 WITHIN server.properties
        val resourcePackHash = "09a55f5160b677c033e1446ceb35258d450e01de"
        joiningPlayer.setResourcePack(
            resourcePackLink,
            resourcePackHash,
            true,
            Component.text("Please allow Odyssey to download a server side resource pack!", TextColor.color(255, 170, 0))
        )

    }

    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        val player = event.player
        // Force the player to use the resource pack
        resourcePackHandler(player)
        // Cookies
        // val nub = byteArrayOf(0x48)
        // player.storeCookie(NamespacedKey(Odyssey.instance, "test"), nub)
    }

    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
        //
    }

}