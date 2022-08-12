package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.hogRider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.commands.*
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.mclisteners.*
import me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners.ArmorListeners
import me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners.MeleeListeners
import me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners.MiscListeners
import me.shadowalzazel.mcodyssey.mclisteners.enchantmentListeners.RangedListeners
import me.shadowalzazel.mcodyssey.recipes.CookingRecipes
import me.shadowalzazel.mcodyssey.recipes.GildingRecipes
import me.shadowalzazel.mcodyssey.recipes.OdysseyRecipes
import org.bukkit.Bukkit
import org.bukkit.Material
  
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

        // Register Alchemy
        server.pluginManager.registerEvents(OdysseyAlchemyListeners, this)

        // Register Utility Listeners
        server.pluginManager.registerEvents(OdysseyGildingListeners, this)
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
        // Enchantment listeners
        server.pluginManager.registerEvents(ArmorListeners, this)
        server.pluginManager.registerEvents(MeleeListeners, this)
        server.pluginManager.registerEvents(MiscListeners, this)
        server.pluginManager.registerEvents(RangedListeners, this)
        // Mob drops
        server.pluginManager.registerEvents(OdysseyDropsListeners, this)
        // Odyssey Food
        server.pluginManager.registerEvents(OdysseyFoodListeners, this)
        // Items
        server.pluginManager.registerEvents(OdysseyItemListeners, this)

        // Custom Recipes
        Bukkit.addRecipe(GildingRecipes.ODYSSEY_NAMING)
        Bukkit.addRecipe(GildingRecipes.ODYSSEY_GILDED_SMITHING)
        Bukkit.addRecipe(GildingRecipes.GILDED_BOOK_COMBINING)
        Bukkit.addRecipe(GildingRecipes.GILDED_ITEM_UPGRADING)
        // Item Recipes
        for (itemRecipe in OdysseyRecipes.recipeSet) {
            Bukkit.addRecipe(itemRecipe)
        }
        // Cooking Recipes
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