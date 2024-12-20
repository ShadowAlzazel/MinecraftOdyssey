package me.shadowalzazel.mcodyssey.common.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.trim.TrimMaterial

object TrimMaterials : RegistryTagManager {

    private val trimMaterialRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL)

    val KUNZITE: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("kunzite"))!!
    val ALEXANDRITE: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("alexandrite"))!!
    val JADE: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("jade"))!!
    val RUBY: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("ruby"))!!
    val SOUL_QUARTZ: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("soul_quartz"))!!
    val JOVIANITE: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("jovianite"))!!
    val NEPTUNIAN: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("neptunian"))!!
    val SOUL_STEEL: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("soul_steel"))!!
    val OBSIDIAN: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("obsidian"))!!
    val MITHRIL: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("mithril"))!!
    val SILVER: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("silver"))!!
    val TITANIUM: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("titanium"))!!
    val ANODIZED_TITANIUM: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("anodized_titanium"))!!
    val IRIDIUM: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("iridium"))!!
    val CRYSTAL_ALLOY: TrimMaterial = trimMaterialRegistry.get(createOdysseyKey("crystal_alloy"))!!

}