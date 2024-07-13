package me.shadowalzazel.mcodyssey.structures

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.generator.structure.Structure
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class StructureDetector(val odyssey: Odyssey) : BukkitRunnable(), StructureManager {

    private val shadowChambers: Structure

    init {
        val structureRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE)
        shadowChambers = structureRegistry.get(NamespacedKey(Odyssey.instance, "shadow_chambers"))!!
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