package me.shadowalzazel.mcodyssey

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

object AssetListeners : Listener {


    private fun resourcePackHandler(joiningPlayer: Player) {

        // PROD
        val resourcePackLink = "https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1"
        // TEST
        //val resourcePackLink = "https://www.dropbox.com/s/6hob8iad499vz95/odyssey-resource-pack-test.zip?dl=1"

        val resourcePackHash = "8a0de3e2e2045ff10010a9b3671cafa1dcd73bfa"
        joiningPlayer.setResourcePack(
            resourcePackLink,
            resourcePackHash,
            true,
            Component.text("Please Allow Odyssey to download a server side resource pack!", TextColor.color(255, 170, 0))
        )
    }

    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        // Force the player to use the resource pack
        resourcePackHandler(event.player)
        // RESET
        //event.player.equipment.setItemInMainHand(ItemStack(Material.STRING, 1))

    }

    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
        //
    }

}