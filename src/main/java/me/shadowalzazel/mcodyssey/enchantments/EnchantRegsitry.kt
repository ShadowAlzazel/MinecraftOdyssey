package me.shadowalzazel.mcodyssey.enchantments

import net.kyori.adventure.key.Keyed
import org.bukkit.NamespacedKey

class EnchantRegsitry <T : Keyed?> {
    private val map = mutableMapOf<NamespacedKey, T>()

    operator fun set(key: NamespacedKey, value: T) {
        map[key] = value
    }

    operator fun get(key: NamespacedKey): T? {
        return map[key]
    }
}