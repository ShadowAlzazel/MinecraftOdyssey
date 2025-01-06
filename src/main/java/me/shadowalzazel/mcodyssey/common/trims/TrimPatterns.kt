package me.shadowalzazel.mcodyssey.common.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import org.bukkit.inventory.meta.trim.TrimPattern

object TrimPatterns : RegistryTagManager {

    private val patternRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN)

    val IMPERIAL: TrimPattern = patternRegistry.get(createOdysseyKey("imperial"))!!
    val VOYAGER: TrimPattern = patternRegistry.get(createOdysseyKey("voyager"))!!
    val LEAF: TrimPattern = patternRegistry.get(createOdysseyKey("leaf"))!!
    val DANGER : TrimPattern = patternRegistry.get(createOdysseyKey("danger"))!!
    val RING: TrimPattern = patternRegistry.get(createOdysseyKey("ring"))!!
    val CROSS: TrimPattern = patternRegistry.get(createOdysseyKey("cross"))!!
    val SPINE: TrimPattern = patternRegistry.get(createOdysseyKey("spine"))!!
    val WINGS: TrimPattern = patternRegistry.get(createOdysseyKey("wings"))!!
    val TRACE : TrimPattern = patternRegistry.get(createOdysseyKey("trace"))!!
    val JEWEL: TrimPattern = patternRegistry.get(createOdysseyKey("jewel"))!!

}