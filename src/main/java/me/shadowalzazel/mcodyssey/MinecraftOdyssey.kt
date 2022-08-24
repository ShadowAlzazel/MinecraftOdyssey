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
import me.shadowalzazel.mcodyssey.phenomenon.PhenomenonTimer
import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.lunarPhenomena.utilty.LunarPhenomenon
import me.shadowalzazel.mcodyssey.recipes.*
import org.bukkit.Bukkit
import org.bukkit.World

import org.bukkit.plugin.java.JavaPlugin

class MinecraftOdyssey : JavaPlugin() {

    //
    var mainWorld: World? = null

    // Phenomenon Stuff
    var solarPhenomenonActive: Boolean = false
    var currentSolarPhenomenon: SolarPhenomenon? = null
    var lunarPhenomenonActive: Boolean = false
    var currentLunarPhenomenon: LunarPhenomenon? = null
    var playersRequiredForLuck: Int = 99

    // Config variables
    var endGame: Boolean = true

    // Boss Progression
    // Change This LATER to read from storage
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

        // Registering Server related events
        server.also {
            // Register Alchemy
            it.pluginManager.registerEvents(OdysseyAlchemyListeners, this)
            // Register Utility Listeners
            it.pluginManager.registerEvents(OdysseyGildingListeners, this)
            // Daily Phenomenon listeners
            // Odyssey Boss Listeners
            it.pluginManager.registerEvents(AmbassadorListeners, this)
            it.pluginManager.registerEvents(HogRiderListeners, this)
            // Odyssey Server Listeners
            it.pluginManager.registerEvents(OdysseyServerListeners, this)
            // Odyssey Enchantment listeners
            it.pluginManager.registerEvents(ArmorListeners, this)
            it.pluginManager.registerEvents(MeleeListeners, this)
            it.pluginManager.registerEvents(MiscListeners, this)
            it.pluginManager.registerEvents(RangedListeners, this)
            // Odyssey Effect Listeners
            it.pluginManager.registerEvents(OdysseyEffectListeners, this)
            // Odyssey Mob Drops Listeners
            it.pluginManager.registerEvents(OdysseyDropsListeners, this)
            // Odyssey Food Listeners
            it.pluginManager.registerEvents(OdysseyFoodListeners, this)
            // Odyssey Items Listeners
            it.pluginManager.registerEvents(OdysseyItemListeners, this)
            // Odyssey Misc Listeners
            it.pluginManager.registerEvents(OdysseyMiscListeners, this)
            // Config
            if (config.getBoolean("daily-world-phenomenon.enabled")) {
                // Register Daily Events
                it.pluginManager.registerEvents(OdysseyPhenomenaListeners, this)
                playersRequiredForLuck = config.getInt("daily-world-phenomenon.player-minimum-for-luck")
                // Getting main world for phenomenon timer
                for (world in it.worlds) {
                    if (world.environment == World.Environment.NORMAL) {
                        mainWorld = world
                        val phenomenonTimer = PhenomenonTimer(mainWorld!!)
                        val timerDelay = 20 * 10L
                        phenomenonTimer.runTaskTimer(this, timerDelay, 20 * 10)
                        break
                    }
                }
            }
        }

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

        // Hello World!
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {

        // Plugin shutdown logic
        logger.info("The Odyssey will wait another day...")
    }


}