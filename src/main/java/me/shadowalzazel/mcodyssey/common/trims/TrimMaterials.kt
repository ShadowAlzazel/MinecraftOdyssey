package me.shadowalzazel.mcodyssey.common.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.trim.TrimMaterial

object TrimMaterials {

    // private val trimMaterialRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL)

    val KUNZITE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "kunzite")) ?: TrimMaterial.IRON
    val ALEXANDRITE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "alexandrite")) ?: TrimMaterial.IRON
    val JADE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "jade")) ?: TrimMaterial.IRON
    val RUBY: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "ruby")) ?: TrimMaterial.IRON
    val SOUL_QUARTZ: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "soul_quartz")) ?: TrimMaterial.IRON
    val JOVIANITE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "jovianite")) ?: TrimMaterial.IRON
    val NEPTUNIAN: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "neptunian")) ?: TrimMaterial.IRON

    val SOUL_STEEL: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "soul_steel")) ?: TrimMaterial.IRON
    val OBSIDIAN: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "obsidian")) ?: TrimMaterial.IRON
    val MITHRIL: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "mithril")) ?: TrimMaterial.IRON
    val SILVER: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "silver")) ?: TrimMaterial.IRON
    val TITANIUM: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "titanium")) ?: TrimMaterial.IRON
    val ANODIZED_TITANIUM: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "anodized_titanium")) ?: TrimMaterial.IRON
    val IRIDIUM: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "iridium")) ?: TrimMaterial.IRON

}