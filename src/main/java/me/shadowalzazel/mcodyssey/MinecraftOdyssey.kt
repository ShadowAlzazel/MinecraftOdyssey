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
import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.recipes.*
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.World

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileNotFoundException

class MinecraftOdyssey : JavaPlugin() {

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
    val resourcePackHash: String = "39bd2b57f97627f8c6b04ff672482cce44262e7b"

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
            // Odyssey Server Listeners
            it.pluginManager.registerEvents(OdysseyServerListeners, this)
            // Odyssey Alchemy Listeners
            it.pluginManager.registerEvents(OdysseyAlchemyListeners, this)
            // Odyssey Enigmatic Listeners
            it.pluginManager.registerEvents(OdysseyEnigmaticListeners, this)
            // Register Gilding Listeners
            it.pluginManager.registerEvents(OdysseyGildingListeners, this)
            // Odyssey Boss Listeners
            it.pluginManager.registerEvents(OdysseyBossListeners, this)
            it.pluginManager.registerEvents(AmbassadorListeners, this)
            it.pluginManager.registerEvents(HogRiderListeners, this)
            // Odyssey Enchantment listeners
            it.pluginManager.registerEvents(ArmorListeners, this)
            it.pluginManager.registerEvents(MeleeListeners, this)
            it.pluginManager.registerEvents(MiscListeners, this)
            it.pluginManager.registerEvents(RangedListeners, this)
            // Odyssey Effect Listeners
            it.pluginManager.registerEvents(OdysseyEffectTagListeners, this)
            // Odyssey Mob Drops Listeners
            it.pluginManager.registerEvents(OdysseyDropsListeners, this)
            // Odyssey Food Listeners
            it.pluginManager.registerEvents(OdysseyFoodListeners, this)
            // Odyssey Items Listeners
            it.pluginManager.registerEvents(OdysseyItemListeners, this)
            // Odyssey Weapon Listeners
            it.pluginManager.registerEvents(OdysseyWeaponListeners, this)
            // Odyssey Misc Listeners
            it.pluginManager.registerEvents(OdysseyMiscListeners, this)
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
                        val timerDelay = 20 * 10L
                        phenomenonCycle.runTaskTimer(this, timerDelay, 20 * 10)
                        break
                    }
                }

            }
        }

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
        try {
            val worldFolder = this.mainWorld!!.worldFolder
            val datapackDirectory = worldFolder.path + "/datapacks"
            val datapackFile = File(datapackDirectory)
            val odysseyPackDirectory = datapackDirectory + "/OdysseyDataPack"
            val odysseyPackFile = File(odysseyPackDirectory)
            println(datapackFile)
            println(odysseyPackFile)
            //"OdysseyDataPack\\data\\mcodyssey\\structures\\stone_pillars\\stone_pillars_1.nbt"

            val pillarFile = File(odysseyPackDirectory + "/data/mcodyssey/structures/stone_pillars/stone_pillars_1.nbt")
            val pillarStructure = server.structureManager.loadStructure(pillarFile)
            server.structureManager.registerStructure(NamespacedKey(instance, "stone_pillars_1"), pillarStructure)
            //server.structureManager.saveStructure(NamespacedKey(instance, "stone_pillars_1"))
            println(server.structureManager.getStructureFile(NamespacedKey(instance, "stone_pillars_1")))

            /*
            //val qPillar = server.structureManager.loadStructure(this.getResource("data/mcodyssey/structures/stone_pillars_1.nbt"))
            //OdysseyDataPack\data\mcodyssey\structures\stone_pillars\stone_pillars_1.nbt
            val pillarResource = this.getResource("OdysseyDataPack/data/mcodyssey/structures/stone_pillars/stone_pillars_1.nbt")
            if (pillarResource != null) {
                val pillars = server.structureManager.loadStructure(pillarResource)
                server.structureManager.registerStructure(NamespacedKey(instance, "stone_pillars_1"), pillars)
                println("Success!")
            }
            println("Input Stream: $pillarResource")

             */
        }
        catch(ex: FileNotFoundException) {
            logger.info(ex.message)
        }


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