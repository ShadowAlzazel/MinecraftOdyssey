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
import me.shadowalzazel.mcodyssey.listeners.OdysseyPhenomenaListener
import me.shadowalzazel.mcodyssey.phenomenon.PhenomenonTimer
import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.lunarPhenomena.utilty.LunarPhenomenon
import me.shadowalzazel.mcodyssey.recipes.*
import org.bukkit.Bukkit
import org.bukkit.World

import org.bukkit.plugin.java.JavaPlugin

class MinecraftOdyssey : JavaPlugin() {

    // Coroutines and Async variables and values
    var didStuff: Boolean = false
    var stuffThatHappened: MutableSet<String> = mutableSetOf()

    // Phenomenon Stuff
    var solarPhenomenonActive: Boolean = false
    var currentSolarPhenomenon: SolarPhenomenon? = null
    var lunarPhenomenonActive: Boolean = false
    var currentLunarPhenomenon: LunarPhenomenon? = null
    var mainWorld: World? = null

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
            server.pluginManager.registerEvents(OdysseyPhenomenaListener, this)
        }
        for (world in server.worlds) {
            if (world.environment == World.Environment.NORMAL) {
                mainWorld = world
                break
            }
        }
        if (mainWorld != null ) {
            val phenomenonTimer = PhenomenonTimer(mainWorld!!)
            //val someTime = mainWorld?.time
            //val timerDelay = if (someTime!! < 23500) { 23500 - someTime } else { (24000 - someTime) + 23500 }
            val timerDelay = 20 * 10L
            phenomenonTimer.runTaskTimer(this, timerDelay, 20 * 10)
        }

        // Odyssey Boss Listeners
        server.pluginManager.registerEvents(AmbassadorListeners, this)
        server.pluginManager.registerEvents(HogRiderListeners, this)
        // Odyssey Server Listeners
        server.pluginManager.registerEvents(OdysseyServerListeners, this)
        // Odyssey Enchantment listeners
        server.pluginManager.registerEvents(ArmorListeners, this)
        server.pluginManager.registerEvents(MeleeListeners, this)
        server.pluginManager.registerEvents(MiscListeners, this)
        server.pluginManager.registerEvents(RangedListeners, this)
        // Odyssey Effect Listeners
        server.pluginManager.registerEvents(OdysseyEffectListeners, this)
        // Odyssey Mob Drops Listeners
        server.pluginManager.registerEvents(OdysseyDropsListeners, this)
        // Odyssey Food Listeners
        server.pluginManager.registerEvents(OdysseyFoodListeners, this)
        // Odyssey Items Listeners
        server.pluginManager.registerEvents(OdysseyItemListeners, this)
        // Odyssey Misc Listeners
        server.pluginManager.registerEvents(OdysseyMiscListeners, this)

        // Gilding Recipes
        Bukkit.addRecipe(BrandingRecipes.ODYSSEY_NAMING)
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
        // Weapon Recipes
        for (weaponRecipe in WeaponRecipes.recipeSet) {
            Bukkit.addRecipe(weaponRecipe)
        }

        // Register Commands
        getCommand("SpawnAmbassador")?.setExecutor(SpawnAmbassador)
        getCommand("SpawnHogRider")?.setExecutor(SpawnHogRider)
        getCommand("GiveTestItem")?.setExecutor(GiveTestItem)
        getCommand("SpawnTestMob")?.setExecutor(SpawnTestMob)
        getCommand("SpawnTestKnight")?.setExecutor(SpawnTestKnight)

        // Spell Commands
        getCommand("necronomicon")?.setExecutor(Necronomicon)



        // Experimental coroutine stuff
        /*
        val odysseyTickTimer = OdysseyCoroutineDetector()
        odysseyTickTimer.runTaskTimer(this, 0, 1)

         */


        // Hello World!
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {



        // Plugin shutdown logic
        logger.info("The Odyssey can wait another day...")
    }


}