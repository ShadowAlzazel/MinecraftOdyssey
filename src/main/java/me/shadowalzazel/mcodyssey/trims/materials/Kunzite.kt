package me.shadowalzazel.mcodyssey.trims.materials

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.trim.TrimMaterial

object Kunzite : TrimMaterial {
    override fun getKey(): NamespacedKey {
        return NamespacedKey(Odyssey.instance, "kunzite")
    }

}