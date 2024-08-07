package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.tag.Tag
import io.papermc.paper.registry.tag.TagKey
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry

@Suppress("UnstableApiUsage")
interface RegistryTagManager {

    fun getRegistry(registryKey: RegistryKey<*>): Registry<Keyed> {
        return RegistryAccess.registryAccess().getRegistry(registryKey as RegistryKey<Keyed>)
    }

    fun createOdysseyKey(name: String): NamespacedKey {
        return NamespacedKey("odyssey", name)
    }

    fun createTagKey(name: String, registryKey: RegistryKey<*>): TagKey<Keyed> {
        return TagKey.create(registryKey as RegistryKey<Keyed>, createOdysseyKey(name))
    }

    fun getRegistryTag(name: String, registryKey: RegistryKey<*>): Tag<Keyed> {
        val registry = getRegistry(registryKey)
        val tagKey = createTagKey(name, registryKey)
        return registry.getTag(tagKey)
    }


}