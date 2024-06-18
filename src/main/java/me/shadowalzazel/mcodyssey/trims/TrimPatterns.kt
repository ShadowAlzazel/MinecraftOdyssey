package me.shadowalzazel.mcodyssey.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.trim.TrimPattern

object TrimPatterns {

    private val trimMaterialRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN)

    val IMPERIAL: TrimPattern = trimMaterialRegistry.get(NamespacedKey(Odyssey.instance, "imperial")) ?: TrimPattern.COAST

}