package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.base.OdysseyBoss
import me.shadowalzazel.mcodyssey.bosses.hog_rider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.the_ambassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.*
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.ArmorListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MeleeListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MiscListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.RangedListeners
import me.shadowalzazel.mcodyssey.listeners.unused.OdysseyMobListeners
import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.recipe_creators.RecipeManager
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Odyssey : JavaPlugin(), AssetManager {

    // Main World
    var mainWorld: World? = null

    // Asset Variables for extra datapacks
    //var hasTerralith: Boolean = false
    //var hasIncendium: Boolean = false
    //var hasContinents: Boolean = false


    // Phenomenon Stuff
    var isSolarPhenomenonActive: Boolean = false
    var isLunarPhenomenonActive: Boolean = false
    var currentLunarPhenomenon: OdysseyPhenomenon? = null
    var currentSolarPhenomenon: OdysseyPhenomenon? = null
    var playersRequiredForLuck: Int = 99

    // Config variables
    var isBossProgressionEnabled: Boolean = true

    // Boss Progression
    // Change This LATER to read from storage
    var isEnderDragonDefeated: Boolean = true
    var isAmbassadorDefeated: Boolean = true

    // Boss Mechanics
    var currentBoss: OdysseyBoss? = null
    var isBossActive: Boolean = false
    var timeSinceBoss: Long = System.currentTimeMillis()
    var bossDespawnTimer: Long = System.currentTimeMillis()



    companion object {
        lateinit var instance : Odyssey
    }

    init {
        instance = this
    }

    private fun eventRegister(eventListener : Listener) {
        server.pluginManager.registerEvents(eventListener, this@Odyssey)
    }

    // Plugin startup logic
    override fun onEnable() {
        val timerStart: Long = System.currentTimeMillis()

        // Config start up
        config.options().copyDefaults()
        saveConfig()

        // Need to find the main world to locate datapacks
        logger.info("Finding Datapack World...")
        findMainWorld()

        // Find the Odyssey Datapack as it is required
        logger.info("Finding Odyssey Datapack...")
        val foundPack = findOdysseyDatapack()
        if (!foundPack) {
            logger.info("Disabling Odyssey Plugin! Can Not Find Datapack!")
            server.pluginManager.disablePlugin(this)
            return
        }

        // Register Enchantments
        logger.info("Registering Enchantments...")
        OdysseyEnchantments.register()

        // Register Recipes
        logger.info("Registering Recipes...")
        RecipeManager().createAllRecipes().forEach {
            Bukkit.addRecipe(it)
        }

        // Register Events
        logger.info("Registering Events...")
        listOf(AssetListeners,
            SmithingListeners,
            AlchemyListener,
            OdysseyEnigmaticListeners,
            EnchantingListeners,
            AmbassadorListeners,
            HogRiderListeners,
            ArmorListeners,
            MeleeListeners,
            MiscListeners,
            RangedListeners,
            ScoreboardTagListeners,
            LootListeners,
            OdysseyMobListeners,
            FoodListeners,
            OdysseyItemListeners,
            WeaponListeners,
            SpawningListeners,
        ).forEach { eventRegister(it) }

        //server.pluginManager.registerEvents(OdysseyPhenomenaListeners, this)
        playersRequiredForLuck = 4
        // Getting main world for phenomenon timer
        //val cycleHandler = PhenomenonCycleHandler(mainWorld!!)
        //cycleHandler.runTaskTimer(this, 20 * 10L, 20 * 10)
        //val persistentHandler = PersistentPhenomenonHandler()
        //persistentHandler.runTaskTimer(this, 20 * 5, 20 * 5)

        // Run situations
        //val situationHandler = OldOccurrenceHandler(mainWorld!!)
        //situationHandler.runTaskTimer(this, 20 * 10L, 20 * 10)

        // Hello World!
        val timeElapsed = (System.currentTimeMillis() - timerStart).div(1000.0)
        logger.info("Odyssey Start Up sequence in ($timeElapsed) seconds!")
        logger.info("The Odyssey has just begun!")

    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("The Odyssey will wait another day...")

        // TODO: Add Saving OdysseyOccurrenceCreator
    }


}