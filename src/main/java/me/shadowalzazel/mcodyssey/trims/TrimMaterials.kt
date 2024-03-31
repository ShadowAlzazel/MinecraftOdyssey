package me.shadowalzazel.mcodyssey.trims

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.meta.trim.TrimMaterial

object TrimMaterials {

    val KUNZITE: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "kunzite")) ?: TrimMaterial.IRON
    val ALEXANDRITE: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "alexandrite")) ?: TrimMaterial.IRON
    val JADE: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "jade")) ?: TrimMaterial.IRON
    val RUBY: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "ruby")) ?: TrimMaterial.IRON
    val SOUL_QUARTZ: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "soul_quartz")) ?: TrimMaterial.IRON
    val SOUL_STEEL: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "soul_steel")) ?: TrimMaterial.IRON
    val OBSIDIAN: TrimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey(Odyssey.instance, "obsidian")) ?: TrimMaterial.IRON

}