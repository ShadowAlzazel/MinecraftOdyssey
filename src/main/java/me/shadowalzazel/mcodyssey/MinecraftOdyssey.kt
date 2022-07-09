package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.mclisteners.OdysseyDailyPhenomenonListener
import me.shadowalzazel.mcodyssey.mclisteners.OdysseyPlayerJoinListener

import org.bukkit.plugin.java.JavaPlugin

class MinecraftOdyssey : JavaPlugin() {

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

        if (config.getBoolean("daily-world-phenomenon.enabled")) {
            server.pluginManager.registerEvents(OdysseyDailyPhenomenonListener, this)
        }
        server.pluginManager.registerEvents(OdysseyPlayerJoinListener, this)

        // Hello World!
        logger.info("The Odyssey has just begun!")

    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("The Odyssey can wait another day...")
    }
}