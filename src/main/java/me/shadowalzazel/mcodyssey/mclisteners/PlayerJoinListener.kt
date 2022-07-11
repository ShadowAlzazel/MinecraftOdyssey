package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object OdysseyPlayerJoinListener : Listener {

    // Join Message
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val serverName: String = MinecraftOdyssey.instance.config.getString("names.server-name") ?: return
        player.sendMessage("${ChatColor.GOLD}Hello ${player.name}! Welcome to $serverName")
        //event.joinMessage()

    }


    // Bed Message
    //@EventHandler
    //fun onEnterBed(event: PlayerBedEnterEvent) {
    //    val player = event.player
    //    val worldPlayers = player.world.players
    //    for (aPlayer in worldPlayers) {
    //        aPlayer.sendMessage("${player.name} is trying to Sleep!")
    //    }

    //}

}

