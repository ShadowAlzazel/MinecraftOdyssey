package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.unused.bosses.BossManager
import me.shadowalzazel.mcodyssey.unused.bosses.hog_rider.HogRiderListeners
import me.shadowalzazel.mcodyssey.unused.bosses.the_ambassador.AmbassadorListeners
import me.shadowalzazel.mcodyssey.common.StructureDetector
import me.shadowalzazel.mcodyssey.common.listeners.*
import me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners.*
import me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners.OtherListeners
import me.shadowalzazel.mcodyssey.datagen.PotionMixes
import me.shadowalzazel.mcodyssey.datagen.RecipeLoader
import me.shadowalzazel.mcodyssey.server.commands.admin.*
import me.shadowalzazel.mcodyssey.common.world_events.WorldEventsManager
import me.shadowalzazel.mcodyssey.datagen.RecipeManager
import me.shadowalzazel.mcodyssey.server.commands.UpdateModel
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
    val worldEventManager: WorldEventsManager
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
        worldEventManager = WorldEventsManager(this)
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
        saveResource("config.yml",false)
        saveDefaultConfig()
        //saveConfig()

        // Datapack data and folder
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

        // Set Worlds
        edge = server.getWorld(NamespacedKey(instance, "edge"))!!

        // Enable Enchants
        logger.info("Enabling Enchantments...")

        // Register Recipes
        logger.info("Registering Recipes...")
        RecipeLoader().createAllRecipes().forEach {
            Bukkit.addRecipe(it)
        }

        // Register Potion Mixes
        logger.info("Registering Brews...")
        PotionMixes.getMixes().forEach {
            server.potionBrewer.addPotionMix(it)
        }

        // Register Events
        logger.info("Registering Events...")
        listOf(
            ArcaneListeners,
            SmithingListeners,
            AlchemyListeners,
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
            WeaponListeners,
            SpawningListeners,
            VillagerListeners,
            ItemListeners,
            OtherListeners,
            MobListeners,
            DragonListeners,
            SnifferListeners,
            ArtisanListeners,
            GlyphListeners,
            EffectListeners,
            PetListener,
            InventoryListeners,
            WorldEventsListener,
            StructureListeners,
            RecipeListeners
        ).forEach {
            registerEventListeners(it)
        }

        // Setting resource pack
        logger.info("Setting Resource Pack...")
        registerEventListeners(OdysseyAssets)

        // Set Commands
        logger.info("Setting Commands...")
        mapOf("summon_boss" to SummonBoss,
            "enchant_with_odyssey" to EnchantWithOdyssey,
            "get_item_data" to GetItemData,
            "give_item" to GiveItem,
            "give_weapon" to GiveWeapon,
            "summon_mob" to SummonMob,
            "update_model" to UpdateModel
        ).forEach {
            getCommand(it.key)?.setExecutor(it.value)
        }

        // Starting World Events
        logger.info("Initializing World Events...")
        //dateTimeSyncer.runTaskTimer(this, 20, 20 * 10) // Run every 200 ticks = 10 secs

        // Structures
        structureDetector.runTaskTimer(this, 20L, 20 * 10)

        // Wiki
        val wikiLink = "https://odyssey-wiki.netlify.app/"
        val wikiLinkDisplay = Component.text("Odyssey Wiki")
        server.serverLinks.addLink(wikiLinkDisplay, URI(wikiLink))
        // Github
        val pluginLink = "https://github.com/ShadowAlzazel/MinecraftOdyssey"
        val pluginLinkDisplay = Component.text("Github - Odyssey Plugin")
        server.serverLinks.addLink(pluginLinkDisplay, URI(pluginLink))
        // Server Map
        if (config.get("server-map.enabled") == true) {
            val mapLink = config.get("server-map.link") as String
            val mapLinkDisplay = Component.text(config.get("server-map.name") as String)
            server.serverLinks.addLink(mapLinkDisplay, URI(mapLink))
        }

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