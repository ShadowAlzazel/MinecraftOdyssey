package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
//import org.bukkit.entity.Player as McPlayer


// Events regarding players
object OdysseyPlayerEventListener : Listener {

    // Join Message
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val serverName: String = MinecraftOdyssey.instance.config.getString("names.server-name") ?: return
        player.sendMessage("Hello ${player.name}! Welcome to $serverName")
        //event.joinMessage()
    }

    // Bed Message
    @EventHandler
    fun onEnterBed(event: PlayerBedEnterEvent) {
        val player = event.player
        val worldPlayers = player.world.players
        for (aPlayer in worldPlayers) {
            aPlayer.sendMessage("${player.name} is trying to Sleep!")
        }

    }

    // Dual Wielding
    //open class McPlayer
    //class DualWielder: McPlayer()
    @EventHandler
    fun swingSword(event: PlayerInteractEntityEvent) {
        val player = event.player
        if (player.name == "ShadowAlzazel"){
            player.swingOffHand()
        }

    }

}


// Events regarding daily phenomenon
object  OdysseyDailyEventListener : Listener {

    @EventHandler
    fun onNewDay(event: TimeSkipEvent) {
        val currentWorld = event.world

        // Randomizer
        val worldPhenomenonList = listOf("Slime Rain Event", "SolarEclipseEvent", "BreezyDayEvent")
        val randomWorldPhenomenon = worldPhenomenonList.random()


        //Daily luck
        val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
        if (currentWorld.players.size > luckConfigAmount) {
            val luckyPlayer = currentWorld.players.random()
            val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 1)
            luckyPlayer.addPotionEffect(dailyLuckEffect)
            luckyPlayer.sendMessage("Luck and fortune follows you Today!")
        }


        when (randomWorldPhenomenon) {

            // Slime Rain Event
            worldPhenomenonList[0] -> {
                println("Slime is falling from the Sky at ${currentWorld.name}!")

            }

            // Solar Eclipse Event
            worldPhenomenonList[1] -> {
                println("A solar eclipse is happening at ${currentWorld.name}!")

            }

            // Breezy Day Event
            worldPhenomenonList[2] -> {
                println("A swift wind is happening at ${currentWorld.name}!")
                val worldPlayers = currentWorld.players
                val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)
                for (breezePlayer in worldPlayers) {
                    breezePlayer.addPotionEffect(breezeEffect)
                    breezePlayer.sendMessage("A swift winds follows your side...")
                }

            }
            else -> println("A calm day...")



        }
    }
}
