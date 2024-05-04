package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.BossManager
import me.shadowalzazel.mcodyssey.bosses.hog_rider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.the_ambassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.commands.admin.*
import me.shadowalzazel.mcodyssey.commands.spells.PlaceFeatureArchaicSeed
import me.shadowalzazel.mcodyssey.listeners.*
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.ArmorListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MeleeListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MiscListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.RangedListeners
import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.recipes.RecipeManager
import me.shadowalzazel.mcodyssey.recipes.brewing.BrewerMixes
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileNotFoundException

class Odyssey : JavaPlugin() {

    // Managers
    val bossManager: BossManager

    // Overworld
    lateinit var overworld: World

    // Phenomenon Stuff
    var isSolarPhenomenonActive: Boolean = false
    var isLunarPhenomenonActive: Boolean = false
    var currentLunarPhenomenon: OdysseyPhenomenon? = null
    var currentSolarPhenomenon: OdysseyPhenomenon? = null
    var playersRequiredForLuck: Int = 99

    companion object {
        lateinit var instance : Odyssey
    }

    init {
        instance = this
        bossManager = BossManager(this)
    }

    private fun registerEventListeners(listener: Listener) {
        server.pluginManager.registerEvents(listener, this@Odyssey)
    }

    // Plugin startup logic
    override fun onEnable() {
        // Start Timer
        val timerStart: Long = System.currentTimeMillis()
        // Config start up
        config.options().copyDefaults()
        saveConfig()

        // Asset Loader
        val datapackManager = DatapackManager(this)
        // Find Worlds
        // Need to find the main world to locate datapacks
        logger.info("Finding Datapack World...")
        for (world in server.worlds) {
            overworld = world
            try {
                val dataPackFolder = File("${world.worldFolder}/datapacks")
                logger.info("Datapacks in $dataPackFolder")
                break
            }
            catch (ex: FileNotFoundException) {
                continue
            }
        }

        // Find the Odyssey Datapack as it is required
        logger.info("Finding Odyssey Datapack...")
        val foundPack = datapackManager.findOdysseyDatapack()
        if (!foundPack) {
            logger.info("Disabling Odyssey Plugin! Can Not Find Datapack!")
            server.pluginManager.disablePlugin(this)
            return
        }
        // Enable Enchants
        logger.info("Enabling Enchantments...")

        // Register Recipes
        logger.info("Registering Recipes...")
        RecipeManager().createAllRecipes().forEach {
            Bukkit.addRecipe(it)
        }
        // Register Potion Mixes
        logger.info("Registering Brews...")
        BrewerMixes.getMixes().forEach {
            server.potionBrewer.addPotionMix(it)
        }

        // Register Events
        logger.info("Registering Events...")
        listOf(
            AssetListeners,
            ArcaneListeners,
            SmithingListeners,
            AlchemyListener,
            SoulBraisingListener,
            EnchantingListeners,
            AmbassadorListeners,
            HogRiderListeners,
            ArmorListeners,
            MeleeListeners,
            MiscListeners,
            RangedListeners,
            ScoreboardTagListeners,
            LootListeners,
            FoodListeners,
            WeaponListeners,
            SpawningListeners,
            ItemListeners,
            OtherListeners,
            MobListeners,
            DragonListeners,
            SnifferListeners,
            ArtisanListeners,
            RunesherdListeners,
            EffectListeners,
            PetListener
        ).forEach {
            registerEventListeners(it)
        }

        // Set Commands
        logger.info("Setting Commands...")
        mapOf("summon_boss" to SummonBoss,
            "enchant_with_odyssey" to EnchantWithOdyssey,
            "place_feature_archaic_seed" to PlaceFeatureArchaicSeed,
            "get_item_data" to GetItemData,
            "give_item" to GiveItem,
            "give_weapon" to GiveWeapon,
            "give_arcane_book" to GiveArcaneBook,
            "give_spacerune_tablet" to GiveSpaceRuneTablet,
            "summon_mob" to SummonMob,
            "summon_doppelganger" to SummonDoppelganger,
        ).forEach {
            getCommand(it.key)?.setExecutor(it.value)
        }

        //server.pluginManager.registerEvents(OdysseyPhenomenaListeners, this)
        //playersRequiredForLuck = 4
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
        logger.info("Odyssey start up sequence in ($timeElapsed) seconds!")
        logger.info("The Odyssey has just begun! Good luck.")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("The Odyssey will wait another day...")
    }

}