package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.bosses.BossManager
import me.shadowalzazel.mcodyssey.bosses.hog_rider.HogRiderListeners
import me.shadowalzazel.mcodyssey.bosses.the_ambassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.commands.admin.*
import me.shadowalzazel.mcodyssey.commands.spells.PlaceFeatureArchaicSeed
import me.shadowalzazel.mcodyssey.common.listeners.*
import me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners.*
import me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners.OtherListeners
import me.shadowalzazel.mcodyssey.datagen.recipes.RecipeManager
import me.shadowalzazel.mcodyssey.datagen.recipes.brewing.BrewerMixes
import me.shadowalzazel.mcodyssey.common.StructureDetector
import me.shadowalzazel.mcodyssey.world_events.DailyWorldEventManager
import me.shadowalzazel.mcodyssey.world_events.DateTimeSyncer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileNotFoundException
import java.net.URI

@Suppress("UnstableApiUsage")
class Odyssey : JavaPlugin() {

    // Managers
    val bossManager: BossManager
    val worldEventManager: DailyWorldEventManager
    val dateTimeSyncer: DateTimeSyncer
    val structureDetector: StructureDetector

    // Overworld
    lateinit var overworld: World
    lateinit var edge: World

    companion object {
        lateinit var instance : Odyssey
    }

    init {
        instance = this
        bossManager = BossManager(this)
        worldEventManager = DailyWorldEventManager(this)
        dateTimeSyncer = DateTimeSyncer(this)
        structureDetector = StructureDetector(this)
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
        val datapackManager = OdysseyDatapack(this)

        // Need to find the main world for others
        logger.info("Finding Main World...")
        for (world in server.worlds) {
            overworld = world
            try {
                val folderPath = File("${world.worldFolder}/datapacks")
                logger.info("Datapacks in $folderPath")
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
        // Set Edge
        val foundEdge = server.getWorld(NamespacedKey(instance, "edge")) ?: server.worlds.first()
        edge = foundEdge

        // Enable Enchants
        logger.info("Enabling Enchantments...")
        /*
        println(OdysseyEnchantments.meleeSet)
        OdysseyEnchantments.meleeSet.forEach {
            println(it)
        }
         */

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
            OdysseyAssets,
            ArcaneListeners,
            SmithingListeners,
            AlchemyListener,
            SoulBraisingListener,
            EnchantingListeners,
            AmbassadorListeners,
            HogRiderListeners,
            ArmorListeners,
            MeleeListeners,
            OtherListeners,
            RangedListeners,
            MiscListeners,
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
            PetListener,
            InventoryListeners,
            WorldEventsListener,
            StructureListeners
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
        // Starting World Date
        logger.info("Initializing World Events...")
        dateTimeSyncer.currentDay = 0 //dateTimeSyncer.getDay()
        //dateTimeSyncer.runTaskTimer(this, 20, 20 * 10) // Run every 200 ticks = 10 secs
        structureDetector.runTaskTimer(this, 20L, 20 * 10)

        // Wiki
        val wikiLink = "https://minecraftodyssey.fandom.com/wiki/MinecraftOdyssey_Wiki"
        val wikiLinkDisplay = Component.text("Odyssey Wiki")
        server.serverLinks.addLink(wikiLinkDisplay, URI(wikiLink))
        // Map [VailCraft]
        val mapLink = "http://map.vailcraft.net:25563/#world:0:0:0:2250:0:0:0:0:perspective"
        val mapLinkDisplay = Component.text("Map")
        server.serverLinks.addLink(mapLinkDisplay, URI(mapLink))
        // Github
        val pluginLink = "https://github.com/ShadowAlzazel/MinecraftOdyssey"
        val pluginLinkDisplay = Component.text("Github - Odyssey Plugin")
        server.serverLinks.addLink(pluginLinkDisplay, URI(pluginLink))

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