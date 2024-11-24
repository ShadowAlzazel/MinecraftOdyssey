package me.shadowalzazel.mcodyssey.common.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.trim.TrimPattern

object TrimPatterns {

    private val patternRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN)

    val IMPERIAL: TrimPattern = patternRegistry.get(NamespacedKey(Odyssey.instance, "imperial")) ?: TrimPattern.COAST

}