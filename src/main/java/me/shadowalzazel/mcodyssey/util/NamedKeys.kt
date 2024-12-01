package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey

object NamedKeys {

    val ITEM_KEY = NamespacedKey(Odyssey.instance, "item")
    val UUID_KEY = NamespacedKey(Odyssey.instance, "UUID")

    val newKey: (String) -> NamespacedKey = { key: String -> NamespacedKey(Odyssey.instance, key) }

}