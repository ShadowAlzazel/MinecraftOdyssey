package me.shadowalzazel.mcodyssey.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.trim.TrimMaterial

//TODO!!
object TrimMaterials {

    // private val trimMaterialRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL)

    val KUNZITE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "kunzite")) ?: TrimMaterial.IRON
    val ALEXANDRITE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "alexandrite")) ?: TrimMaterial.IRON
    val JADE: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "jade")) ?: TrimMaterial.IRON
    val RUBY: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "ruby")) ?: TrimMaterial.IRON
    val SOUL_QUARTZ: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "soulquartz")) ?: TrimMaterial.IRON
    val SOUL_STEEL: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "soulsteel")) ?: TrimMaterial.IRON

    val OBSIDIAN: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "obsidian")) ?: TrimMaterial.IRON
    val MITHRIL: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "mithril")) ?: TrimMaterial.IRON
    val SILVER: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "silver")) ?: TrimMaterial.IRON
    val TITANIUM: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "titanium")) ?: TrimMaterial.IRON
    val ANODIZED_TITANIUM: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "anodizedtitanium")) ?: TrimMaterial.IRON
    val IRIDIUM: TrimMaterial = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey(Odyssey.instance, "iridium")) ?: TrimMaterial.IRON

}