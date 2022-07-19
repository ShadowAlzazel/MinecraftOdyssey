package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.commands.SpawnAmbassador
import me.shadowalzazel.mcodyssey.mclisteners.*

import org.bukkit.plugin.java.JavaPlugin

class MinecraftOdyssey : JavaPlugin() {

    var dailyPhenomenonActive: Boolean = false
    var nightlyPhenomenonActive: Boolean = false
    //var endGame: Boolean = MinecraftOdyssey.instance.config.getBoolean("end-game.enabled")

    // Boss Progression
    // Change This LATER to read from storage
    var endGame: Boolean = true
    var ambassadorDefeated: Boolean = true

    // Boss Mechanics
    var currentBoss: OdysseyBoss? = null
    var activeBoss: Boolean = false

    companion object {
        lateinit var instance : MinecraftOdyssey
    }

    init {
        instance = this
    }

    // Plugin startup logic
    override fun onEnable() {

        // Config start up
        config.options().copyDefaults()
        saveConfig()


        // Register Events
        server.pluginManager.registerEvents(MinecraftOdysseyListeners, this)
        if (config.getBoolean("daily-world-phenomenon.enabled")) {
            server.pluginManager.registerEvents(OdysseyDailyPhenomenonListener, this)
            server.pluginManager.registerEvents(OdysseyNightlyPhenomenonListener, this)
        }
        server.pluginManager.registerEvents(AmbassadorListeners, this)
        server.pluginManager.registerEvents(OdysseyPlayerJoinListener, this)
        server.pluginManager.registerEvents(OdysseyPlayerLeaveListener, this)


        // Register Commands
        getCommand("SpawnAmbassador")?.setExecutor(SpawnAmbassador)



        // Hello World!
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("The Odyssey can wait another day...")
    }
}