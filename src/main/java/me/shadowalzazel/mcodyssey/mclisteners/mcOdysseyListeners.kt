package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.checkerframework.checker.units.qual.Speed


// Events regarding players
object OdysseyPlayerEventListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val serverName: String = MinecraftOdyssey.instance.config.getString("names.server-name") ?: return
        player.sendMessage("Hello ${player.name}! Welcome to $serverName")
        //event.joinMessage("Hello ${player.name}! Welcome to $serverName")
    }

    @EventHandler
    fun onEnterBed(event: PlayerBedEnterEvent) {
        val player = event.player
        val worldPlayers = player.world.players
        for (xPlayer in worldPlayers) {
            xPlayer.sendMessage("${player.name} is trying to Sleep!")
        }

    }

}


// Events regarding daily events
object  OdysseyDailyEventListener : Listener {

    @EventHandler
    fun onNewDay(event: TimeSkipEvent) {

        val worldEventList = listOf("SlimeDayEvent", "SolarEclipseEvent", "BreezyDayEvent")
        val randomElement = worldEventList.random()
        val currentWorld = event.world

        val ranPlayer = currentWorld.players.random()
        val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 1)
        ranPlayer.addPotionEffect(dailyLuckEffect)
        ranPlayer.sendMessage("Luck and fortune follows you Today!")

        when (randomElement) {
            worldEventList[0] -> {
                println("Slime is falling from the Sky! at ${currentWorld.name}")

            }
            worldEventList[1] -> {
                println("A solar eclipse is happening! at ${currentWorld.name}")

            }
            worldEventList[2] -> {
                println("A swift wind is at your side! at ${currentWorld.name}")
                val worldPlayers = currentWorld.players
                val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)
                for (xPlayer in worldPlayers) {
                    xPlayer.addPotionEffect(breezeEffect)
                }

            }
            else -> println("No RNG :(")



        }
    }
}
