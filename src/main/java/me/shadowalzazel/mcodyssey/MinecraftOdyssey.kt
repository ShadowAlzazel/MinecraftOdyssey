package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.hogRider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.commands.*
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.mclisteners.*
import me.shadowalzazel.mcodyssey.odysseyUtility.CookingRecipes
import me.shadowalzazel.mcodyssey.odysseyUtility.OdysseyRecipes
import org.bukkit.Bukkit

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
    var timeSinceBoss: Long = System.currentTimeMillis()
    var bossDespawnTimer: Long = System.currentTimeMillis()

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

        // Register Enchantments
        OdysseyEnchantments.register()


        // REGISTER DAILY

        // Register Utility Listeners
        server.pluginManager.registerEvents(MinecraftOdysseyListeners, this)
        // Daily Phenomenon listeners
        if (config.getBoolean("daily-world-phenomenon.enabled")) {
            server.pluginManager.registerEvents(OdysseyDailyPhenomenonListener, this)
            server.pluginManager.registerEvents(OdysseyNightlyPhenomenonListener, this)
        }
        // Boss Listeners
        server.pluginManager.registerEvents(AmbassadorListeners, this)
        server.pluginManager.registerEvents(HogRiderListeners, this)
        // Join and Leave Messages
        server.pluginManager.registerEvents(OdysseyPlayerJoinListener, this)
        server.pluginManager.registerEvents(OdysseyPlayerLeaveListener, this)
        // Enchantment listener
        server.pluginManager.registerEvents(EnchantmentListeners, this)
        // Mob drops
        server.pluginManager.registerEvents(OdysseyDropsListeners, this)
        // Odyssey Food
        server.pluginManager.registerEvents(OdysseyFoodListeners, this)

        // Custom Recipes
        Bukkit.addRecipe(OdysseyRecipes.odysseyNaming)
        Bukkit.addRecipe(OdysseyRecipes.odysseyGildedSmithing)
        Bukkit.addRecipe(OdysseyRecipes.gildedBookCombining)
        Bukkit.addRecipe(OdysseyRecipes.gildingUpgrading)

        //Cooking Recipes
        for (cookingRecipe in CookingRecipes.recipeSet) {
            Bukkit.addRecipe(cookingRecipe)
        }


        // Register Commands
        getCommand("SpawnAmbassador")?.setExecutor(SpawnAmbassador)
        getCommand("SpawnHogRider")?.setExecutor(SpawnHogRider)
        getCommand("GiveTestItem")?.setExecutor(GiveTestItem)
        getCommand("SpawnTestMob")?.setExecutor(SpawnTestMob)
        getCommand("SpawnTestKnight")?.setExecutor(SpawnTestKnight)

        // Hello World!
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {

        // Plugin shutdown logic
        logger.info("The Odyssey can wait another day...")
    }


}