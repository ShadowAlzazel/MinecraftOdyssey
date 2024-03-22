package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.BossManager
import me.shadowalzazel.mcodyssey.bosses.hog_rider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.the_ambassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.commands.admin.EnchantGilded
import me.shadowalzazel.mcodyssey.commands.admin.GiveGildedBook
import me.shadowalzazel.mcodyssey.commands.admin.GiveItem
import me.shadowalzazel.mcodyssey.commands.admin.SummonBoss
import me.shadowalzazel.mcodyssey.commands.spells.PlaceFeatureArchaicSeed
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.ReflectionUtils
import me.shadowalzazel.mcodyssey.listeners.*
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.ArmorListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MeleeListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MiscListeners
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.RangedListeners
import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.recipe_creators.RecipeManager
import me.shadowalzazel.mcodyssey.recipe_creators.brewing.BrewerMixes
import net.minecraft.core.registries.BuiltInRegistries
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileNotFoundException

class Odyssey : JavaPlugin() {

    // Managers
    private val assetManager: AssetManager
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
        assetManager = AssetManager(this)
        bossManager = BossManager(this)
    }

    private fun eventRegister(listener: Listener) {
        server.pluginManager.registerEvents(listener, this@Odyssey)
    }

    // Plugin startup logic
    override fun onEnable() {
        // Enchantment NMS (Allow new registries)
        ReflectionUtils.unfreezeRegistry(BuiltInRegistries.ENCHANTMENT)

        val timerStart: Long = System.currentTimeMillis()
        // Config start up
        config.options().copyDefaults()
        saveConfig()

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
        val foundPack = assetManager.findOdysseyDatapack()
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
        // Register Potion Mixes
        logger.info("Registering Brews...")
        BrewerMixes.getMixes().forEach {
            server.potionBrewer.addPotionMix(it)
        }

        // Register Events
        logger.info("Registering Events...")
        listOf(AssetListeners,
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
        ).forEach { eventRegister(it) }



        // Set Commands
        getCommand("summon_boss")?.setExecutor(SummonBoss)
        getCommand("enchant_gilded")?.setExecutor(EnchantGilded)
        getCommand("place_feature_archaic_seed")?.setExecutor(PlaceFeatureArchaicSeed)
        getCommand("give_item")?.setExecutor(GiveItem)
        getCommand("give_gilded_book")?.setExecutor(GiveGildedBook)

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
        logger.info("Odyssey Start Up sequence in ($timeElapsed) seconds!")
        logger.info("The Odyssey has just begun!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("The Odyssey will wait another day...")
    }

}