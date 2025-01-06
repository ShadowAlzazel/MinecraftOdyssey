package me.shadowalzazel.mcodyssey.common

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.StructureHelper
import org.bukkit.NamespacedKey
import org.bukkit.generator.structure.Structure
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class StructureDetector(val odyssey: Odyssey) : BukkitRunnable(), StructureHelper, RegistryTagManager {

    private val digSite: Structure
    private val shadowChambers: Structure
    private val trialKeep: Structure
    private val obelisk: Structure

    init {
        val structureRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE)
        //val defaultStructure = structureRegistry.get(Key.key("mineshaft"))!!
        shadowChambers = structureRegistry.get(NamespacedKey(Odyssey.instance, "shadow_chambers"))!!
        trialKeep = structureRegistry.get(NamespacedKey(Odyssey.instance, "trial_keep"))!!
        digSite = structureRegistry.get(NamespacedKey(Odyssey.instance, "dig_site"))!!
        obelisk = structureRegistry.get(NamespacedKey(Odyssey.instance, "obelisk"))!!
    }

    override fun run() {
        for (player in odyssey.edge.players) {
            if (entityInsideStructure(player, shadowChambers)) {
                player.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 15 * 20, 0))
                player.addPotionEffect(PotionEffect(PotionEffectType.MINING_FATIGUE, 15 * 20, 1))
            }
        }
    }
}