package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.mclisteners.*
import me.shadowalzazel.mcodyssey.phenomenons.*

import org.bukkit.plugin.java.JavaPlugin

class MinecraftOdyssey : JavaPlugin() {

    var dailyPhenomenonActive: Boolean = false
    var nightlyPhenomenonActive: Boolean = false
    //var endGame: Boolean = MinecraftOdyssey.instance.config.getBoolean("end-game.enabled")

    var endGame: Boolean = true

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

        server.pluginManager.registerEvents(MinecraftOdysseyListeners, this)

        if (config.getBoolean("daily-world-phenomenon.enabled")) {
            server.pluginManager.registerEvents(OdysseyDailyPhenomenonListener, this)
            //server.pluginManager.registerEvents(OdysseyNightlyPhenonmenonListener, this)
        }
        //temp activation
        server.pluginManager.registerEvents(AmbassadorBossListener, this)

        server.pluginManager.registerEvents(OdysseyPlayerJoinListener, this)

        // Hello World!
        logger.info("The Odyssey has just begun!")

    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("The Odyssey can wait another day...")
    }
}