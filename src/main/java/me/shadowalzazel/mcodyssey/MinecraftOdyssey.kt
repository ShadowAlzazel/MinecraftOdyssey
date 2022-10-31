package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.hogRider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.commands.*
import me.shadowalzazel.mcodyssey.commands.spells.Necronomicon
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.*
import me.shadowalzazel.mcodyssey.listeners.enchantmentListeners.ArmorListeners
import me.shadowalzazel.mcodyssey.listeners.enchantmentListeners.MeleeListeners
import me.shadowalzazel.mcodyssey.listeners.enchantmentListeners.MiscListeners
import me.shadowalzazel.mcodyssey.listeners.enchantmentListeners.RangedListeners
import me.shadowalzazel.mcodyssey.listeners.OdysseyPhenomenaListeners
import me.shadowalzazel.mcodyssey.phenomenon.PhenomenonCycle
import me.shadowalzazel.mcodyssey.phenomenon.PhenomenonPersistentHandler
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.recipes.*
import me.shadowalzazel.mcodyssey.situation.SituationHandler
import org.bukkit.Bukkit
import org.bukkit.World

import org.bukkit.plugin.java.JavaPlugin

class MinecraftOdyssey : JavaPlugin(), OdysseyManager {

    // Main
    var mainWorld: World? = null

    // Phenomenon Stuff
    var utuPhenomenonActive: Boolean = false
    var suenPhenomenonActive: Boolean = false
    var currentSuenPhenomenon: OdysseyPhenomenon? = null
    var currentUtuPhenomenon: OdysseyPhenomenon? = null
    var playersRequiredForLuck: Int = 99

    // Config variables
    var endGame: Boolean = true
    val resourcePackHash: String = "86ce3595245cf2ad63ad5ed613e74b40790e449c"

    // Boss Progression
    // Change This LATER to read from storage
    var enderDragonDefeated: Boolean = true
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
        val timerStart: Long = System.currentTimeMillis()

        // Config start up
        config.options().copyDefaults()
        saveConfig()

        // Register Enchantments
        OdysseyEnchantments.register()

        // Registering Server related events
        logger.info("Registering Recipes...")
        server.also {
            with(it.pluginManager) {
                // Odyssey Server Listeners
                registerEvents(OdysseyServerListeners, this@MinecraftOdyssey)
                // Odyssey Alchemy Listeners
                registerEvents(OdysseyAlchemyListeners, this@MinecraftOdyssey)
                // Odyssey Enigmatic Listeners
                registerEvents(OdysseyEnigmaticListeners, this@MinecraftOdyssey)
                // Register Gilding Listeners
                registerEvents(OdysseyGildingListeners, this@MinecraftOdyssey)
                // Odyssey Boss Listeners
                registerEvents(OdysseyBossListeners, this@MinecraftOdyssey)
                registerEvents(AmbassadorListeners, this@MinecraftOdyssey)
                registerEvents(HogRiderListeners, this@MinecraftOdyssey)
                // Odyssey Enchantment listeners
                registerEvents(ArmorListeners, this@MinecraftOdyssey)
                registerEvents(MeleeListeners, this@MinecraftOdyssey)
                registerEvents(MiscListeners, this@MinecraftOdyssey)
                registerEvents(RangedListeners, this@MinecraftOdyssey)
                // Odyssey Effect Listeners
                registerEvents(OdysseyEffectTagListeners, this@MinecraftOdyssey)
                // Odyssey Mob Drops Listeners
                registerEvents(OdysseyDropsListeners, this@MinecraftOdyssey)
                //
                registerEvents(OdysseyMobListeners, this@MinecraftOdyssey)
                // Odyssey Food Listeners
                registerEvents(OdysseyFoodListeners, this@MinecraftOdyssey)
                // Odyssey Items Listeners
                registerEvents(OdysseyItemListeners, this@MinecraftOdyssey)
                // Block Listeners
                registerEvents(OdysseyBlockListeners, this@MinecraftOdyssey)
                // Odyssey Weapon Listeners
                registerEvents(OdysseyWeaponListeners, this@MinecraftOdyssey)
                // Odyssey Misc Listeners
                registerEvents(OdysseyMiscListeners, this@MinecraftOdyssey)
            }
            // Config
            if (config.getBoolean("world-phenomenon.enabled")) {
                // Register Daily Events
                it.pluginManager.registerEvents(OdysseyPhenomenaListeners, this)
                playersRequiredForLuck = config.getInt("world-phenomenon.player-minimum-for-luck")
                // Getting main world for phenomenon timer
                for (world in it.worlds) {
                    if (world.environment == World.Environment.NORMAL) {
                        mainWorld = world
                        val phenomenonCycle = PhenomenonCycle(mainWorld!!)
                        phenomenonCycle.runTaskTimer(this, 20 * 10L, 20 * 10)
                        break
                    }
                }
                val phenomenonPersistentHandler = PhenomenonPersistentHandler()
                phenomenonPersistentHandler.runTaskTimer(this, 20 * 5, 20 * 5)
            }
        }

        // Run situations
        val situationHandler = SituationHandler(mainWorld!!)
        val timerDelay = 20 * 10L
        situationHandler.runTaskTimer(this, timerDelay, 20 * 10)

        // Register Recipes
        logger.info("Registering Recipes...")
        // Smithing Recipes
        SmithingRecipes.registerRecipes().forEach { Bukkit.addRecipe(it) }

        // Item Recipes
        OdysseyRecipes.registerRecipes().forEach { Bukkit.addRecipe(it) }
        CookingRecipes.registerRecipes().forEach { Bukkit.addRecipe(it) }
        EnigmaticRecipes.registerRecipes().forEach { Bukkit.addRecipe(it) }
        WeaponRecipes.registerRecipes().forEach { Bukkit.addRecipe(it) }

        // Merchant Recipes
        TradingRecipes.registerRecipes().forEach { Bukkit.addRecipe(it) }

        // Register Commands
        logger.info("Registering Commands...")
        getCommand("SpawnAmbassador")?.setExecutor(SpawnAmbassador)
        getCommand("SpawnHogRider")?.setExecutor(SpawnHogRider)
        getCommand("GiveTestItem")?.setExecutor(GiveTestItem)
        getCommand("SpawnTestMob")?.setExecutor(SpawnTestMob)
        getCommand("SpawnTestKnight")?.setExecutor(SpawnTestKnight)
        getCommand("TriggerPhenomenon")?.setExecutor(TriggerPhenomenon)
        getCommand("LocateStructureAsync")?.setExecutor(LocateStructureAsync)
        getCommand("PlaceOdysseyStructure")?.setExecutor(PlaceOdysseyStructure)

        // Spell Commands
        getCommand("necronomicon")?.setExecutor(Necronomicon)

        // Structures
        logger.info("Registering Structures...")
        registerOdysseyStructures(this)


        // TODO: MAKE METHODS!!


        // Hello World!
        val timeElapsed = (System.currentTimeMillis() - timerStart).div(1000.0)
        logger.info("Odyssey Start Up sequence in ($timeElapsed) seconds!")
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {

        // Plugin shutdown logic
        logger.info("The Odyssey will wait another day...")
    }


}