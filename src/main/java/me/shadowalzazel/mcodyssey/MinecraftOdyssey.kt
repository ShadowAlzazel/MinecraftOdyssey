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
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.plugin.Plugin

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

        // Config start up
        config.options().copyDefaults()
        saveConfig()

        // Register Enchantments
        OdysseyEnchantments.register()

        // Registering Server related events
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
            it.broadcast(Component.text("The Odyssey Awaits!"))
        }

        // Gilding Recipes
        Bukkit.addRecipe(BrandingRecipes.ODYSSEY_NAMING)
        Bukkit.addRecipe(GildingRecipes.ODYSSEY_GILDED_SMITHING)
        Bukkit.addRecipe(GildingRecipes.GILDED_BOOK_COMBINING)
        Bukkit.addRecipe(GildingRecipes.GILDED_ITEM_UPGRADING)
        Bukkit.addRecipe(GildingRecipes.GILDED_BOOK_LEGACY_ACTIVATION)
        Bukkit.addRecipe(GildingRecipes.SOUL_STEEL_SMITHING)


        // Item Recipes
        for (itemRecipe in OdysseyRecipes.recipeSet) { Bukkit.addRecipe(itemRecipe) }
        // Weapon Recipes
        for (weaponRecipe in WeaponRecipes.recipeSet) { Bukkit.addRecipe(weaponRecipe) }
        // Cooking Recipes
        for (cookingRecipe in CookingRecipes.recipeSet) { Bukkit.addRecipe(cookingRecipe) }
        // Enigmatic Recipes
        for (enigmaticRecipe in EnigmaticRecipes.recipeSet) { Bukkit.addRecipe(enigmaticRecipe) }
        // Final Recipes
        // TODO: Fix
        // for (someRecipe in FinalRecipes.recipeSet) { Bukkit.addRecipe(someRecipe) }

        // Register Commands
        getCommand("SpawnAmbassador")?.setExecutor(SpawnAmbassador)
        getCommand("SpawnHogRider")?.setExecutor(SpawnHogRider)
        getCommand("GiveTestItem")?.setExecutor(GiveTestItem)
        getCommand("SpawnTestMob")?.setExecutor(SpawnTestMob)
        getCommand("SpawnTestKnight")?.setExecutor(SpawnTestKnight)
        getCommand("TriggerPhenomenon")?.setExecutor(TriggerPhenomenon)
        getCommand("LocateStructureAsync")?.setExecutor(LocateStructureAsync)

        // Spell Commands
        getCommand("necronomicon")?.setExecutor(Necronomicon)

        // Structures
        try {
            //val qPillar = server.structureManager.loadStructure(this.getResource("data/mcodyssey/structures/stone_pillars_1.nbt"))
            val pillarResource = this.getResource("data/mcodyssey/structures/stone_pillars/stone_pillars_1.nbt")
            if (pillarResource != null) {
                println("Input Stream: $pillarResource")
                val pillars = server.structureManager.loadStructure(pillarResource)
                server.structureManager.registerStructure(NamespacedKey(instance, "stone_pillars_1"), pillars)
                println("Success!")
            }
        }
        catch(ex: FileNotFoundException) {
            logger.info(ex.message)
        }


        // TODO: MAKE METHODS!!


        // Hello World!
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {

        // Plugin shutdown logic
        logger.info("The Odyssey will wait another day...")
    }


}