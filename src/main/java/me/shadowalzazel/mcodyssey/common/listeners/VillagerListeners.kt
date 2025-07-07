package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.datagen.RecipeManager
import org.bukkit.NamespacedKey
import org.bukkit.block.Biome
import org.bukkit.entity.Villager
import org.bukkit.entity.ZombieVillager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.VillagerAcquireTradeEvent

@Suppress("UnstableApiUsage")
object VillagerListeners : Listener, RegistryTagManager {

    val IS_PLAINS_BIOMES: Collection<Biome>
    val IS_SWAMP_BIOMES: Collection<Biome>
    val IS_DESERT_BIOMES: Collection<Biome>
    val IS_JUNGLE_BIOMES: Collection<Biome>
    val IS_SAVANNA_BIOMES: Collection<Biome>
    val IS_TAIGA_BIOMES: Collection<Biome>
    val IS_SNOW_BIOMES: Collection<Biome>

    // HUMIDITY, TEMPERATURE
    val VILLAGE_PARAMETERS = mapOf(
        "plains" to Pair(0.4, 0.8),
        "swamp" to Pair(0.9, 0.2),
        "desert" to Pair(-0.5, 2.0),
        "jungle" to Pair(0.9, 0.95),
        "savanna" to Pair(0.0, 2.0),
        "taiga" to Pair(0.8, 0.25),
        "snow" to Pair(0.4, 0.0)
    )

    init {
        // Get registry
        val biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME)
        // Get relevant tags
        /*
        val savannaKey = createTagKey(RegistryKey.BIOME, NamespacedKey("minecraft", "is_savanna"))
        val isSavannaTag = biomeRegistry.getTag(savannaKey)
        val savannaBiomes = getCollectionFromTag(RegistryKey.BIOME, isSavannaTag)
         */
        // Method to get biomes similar to above
        val getBiomesUsingTag : (String) -> Collection<Biome> = { x ->
            getCollectionFromTag(RegistryKey.BIOME, biomeRegistry.getTag(
                createTagKey(RegistryKey.BIOME, NamespacedKey("minecraft", x))
            ))
        }

        IS_SWAMP_BIOMES = getBiomesUsingTag("has_structure/swamp_hut").toMutableList().also { r ->
            biomeRegistry.get(createOdysseyKey("bayou"))?.let { biome -> r.add(biome) }
        }
        IS_PLAINS_BIOMES = getBiomesUsingTag("has_structure/village_plains")
        IS_DESERT_BIOMES = getBiomesUsingTag("has_structure/village_desert")
        IS_SNOW_BIOMES = getBiomesUsingTag("has_structure/village_snowy")
        IS_JUNGLE_BIOMES = getBiomesUsingTag("is_jungle")
        IS_SAVANNA_BIOMES = getBiomesUsingTag("is_savanna")
        IS_TAIGA_BIOMES = getBiomesUsingTag("is_taiga")

    }


    @EventHandler
    fun villagerLevelUpHandler(event: VillagerAcquireTradeEvent) {
        val villager = if (event.entity is Villager) event.entity as Villager else return
        //println("Villager Level Up ${villager.profession} from" + " (${villager.villagerLevel - 1}) to (${villager.villagerLevel})")
        when (villager.profession) {
            Villager.Profession.WEAPONSMITH -> weaponSmithVillagerHandler(event)
        }
    }

    //@EventHandler
    // DISABLED FOR NOW
    fun villagerSpawn(event: CreatureSpawnEvent) {
        val mob = event.entity
        // Only care for custom biomes
        val biome = mob.location.block.biome
        val namespace = biome.key.namespace
        // Get a mapping
        val biomeName = biome.key.key
        val biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME)

        // Ignore vanilla biomes
        if (namespace == "minecraft") return
        if (mob is Villager) {
            when (biome) {
                in IS_SWAMP_BIOMES ->  mob.villagerType = Villager.Type.SWAMP
                in IS_PLAINS_BIOMES -> mob.villagerType = Villager.Type.PLAINS
                in IS_DESERT_BIOMES-> mob.villagerType = Villager.Type.DESERT
                in IS_SNOW_BIOMES -> mob.villagerType = Villager.Type.SNOW
                in IS_JUNGLE_BIOMES-> mob.villagerType = Villager.Type.JUNGLE
                in IS_SAVANNA_BIOMES -> mob.villagerType = Villager.Type.SAVANNA
                in IS_TAIGA_BIOMES-> mob.villagerType = Villager.Type.TAIGA
            }
        }
        if (mob is ZombieVillager) {
            when (biome) {
                in IS_SWAMP_BIOMES ->  mob.villagerType = Villager.Type.SWAMP
                in IS_PLAINS_BIOMES -> mob.villagerType = Villager.Type.PLAINS
                in IS_DESERT_BIOMES-> mob.villagerType = Villager.Type.DESERT
                in IS_SNOW_BIOMES -> mob.villagerType = Villager.Type.SNOW
                in IS_JUNGLE_BIOMES-> mob.villagerType = Villager.Type.JUNGLE
                in IS_SAVANNA_BIOMES -> mob.villagerType = Villager.Type.SAVANNA
                in IS_TAIGA_BIOMES-> mob.villagerType = Villager.Type.TAIGA
            }
            /*
            println("TAGS: ")
            for (x in biomeRegistry.tags) {
                println("TAG -> ${x.tagKey()} ")
                println(x)
                println()
            }
             */
            //val savannaKey = createTagKey(RegistryKey.BIOME, NamespacedKey("minecraft", "is_savanna"))
            //val isSavannaTag = biomeRegistry.getTag(savannaKey)
            //println("TAG -> ${isSavannaTag}")
            // HERE IS VILLAGER
            /*
            if (biome in IS_SWAMP_BIOMES) {
                mob.villagerType = Villager.Type.SWAMP
            }
             */
        }

    }


    private fun cartographerVillagerHandler(event: VillagerAcquireTradeEvent) {
        val villager = event.entity as Villager
        if (villager.villagerLevel == 1) {
            // 
        }

    }


    private fun weaponSmithVillagerHandler(event: VillagerAcquireTradeEvent) {
        val villager = event.entity as Villager
        // TEMP TODO -> Make this a weighted method later
        // level 1
        if (villager.villagerLevel == 1) {   // Apprentice -> Journeyman
            if ((0..10).random() <= 3) {
                println("Changing Recipe to part Upgrade")
                event.recipe = RecipeManager.merchantRecipes.createPartUpgradeTemplateTrade()
            }
        }

        // Rank
        if (villager.villagerLevel == 3) {   // Apprentice -> Journeyman
            if ((0..10).random() <= 3) {
                println("Changing Recipe to Iron Weapon")
                event.recipe = RecipeManager.merchantRecipes.createIronWeaponTrade()
            }
        }
        else if (villager.villagerLevel == 4) { // Journeyman -> Expert
            if ((0..10).random() <= 4) {
                println("Changing Recipe to Customized Iron Weapon")
                val viableParts = listOf(listOf("blade", "hilt").random()) // Just 1 part
                event.recipe = RecipeManager.merchantRecipes.createCustomizedIronWeaponTrade(viableParts)
            }
        }
        else if (villager.villagerLevel == 5) { // Expert -> Master
            if ((0..10).random() <= 5) {
                println("Changing Recipe to Customized Diamond Weapon")
                val viableParts = listOf("blade", "pommel", "hilt")
                event.recipe = RecipeManager.merchantRecipes.createCustomizedDiamondWeaponTrade(viableParts)
            }
        }

    }




}