package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.events.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.world.TimeSkipEvent
//import org.bukkit.potion.PotionEffect
//import org.bukkit.potion.PotionEffectType
//import org.bukkit.event.player.PlayerInteractEntityEvent
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
    //@EventHandler
    //fun swingSword(event: PlayerInteractEntityEvent) {
    //    val player = event.player
    //    if (player.name == "ShadowAlzazel"){
    //        player.swingOffHand()
    //    }

    }



// Events regarding daily events
object OdysseyDailyEventListener : Listener {

    @EventHandler
    fun onNewDay(event: TimeSkipEvent) {
        val currentWorld = event.world

        val worldPhenomenonList = listOf(SolarEclipse(), BreezyDay(), SlimeDay(), Earthquake(), BloodMoon(), BlueMoon())
        val randomWorldPhenomenon = worldPhenomenonList.random()
        val rolledRate = (0..100).random()

        //Daily luck
        val luckConfigAmount = MinecraftOdyssey.instance.config.getInt("player-minimum-for-luck")
        if (currentWorld.players.size >= luckConfigAmount) {
            val drawOfFortunes = DrawOfFortunes()
            drawOfFortunes.phenomenonEffect(currentWorld)
        }

        randomWorldPhenomenon.phenomenonActivation(currentWorld, rolledRate)


    }
}
