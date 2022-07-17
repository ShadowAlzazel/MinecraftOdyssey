package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.commands.SpawnAmbassador
import me.shadowalzazel.mcodyssey.mclisteners.*
import me.shadowalzazel.mcodyssey.phenomenons.*

import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MinecraftOdyssey : JavaPlugin() {

    var dailyPhenomenonActive: Boolean = false
    var nightlyPhenomenonActive: Boolean = false
    //var endGame: Boolean = MinecraftOdyssey.instance.config.getBoolean("end-game.enabled")

    var endGame: Boolean = true
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
            server.pluginManager.registerEvents(OdysseyNightlyPhenonenonListener, this)
        }
        server.pluginManager.registerEvents(AmbassadorBossListener, this)
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