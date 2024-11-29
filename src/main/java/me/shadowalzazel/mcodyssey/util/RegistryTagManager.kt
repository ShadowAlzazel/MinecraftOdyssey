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

    fun <T: Keyed> getPaperRegistry(registryKey: RegistryKey<T>): Registry<T> {
        return RegistryAccess.registryAccess().getRegistry(registryKey)
    }

    fun createOdysseyKey(name: String): NamespacedKey {
        return NamespacedKey("odyssey", name)
    }

    fun createMinecraftKey(name: String): NamespacedKey {
        return NamespacedKey.minecraft(name)
    }

    fun <T: Keyed> createTagKeyFromNamedKey(registryKey: RegistryKey<T>, namespacedKey: NamespacedKey): TagKey<T> {
        return TagKey.create(registryKey, namespacedKey)
    }

    fun <T: Keyed> getTagFromRegistry(registryKey: RegistryKey<T>, location: String, namespace: String="odyssey"): Tag<T> {
        //val blockRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK)
        //val tagKey = TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft("mineable/axe"))
        //return blockRegistry.getTag(tagKey)
        val namespacedKey = if (namespace == "odyssey") createOdysseyKey(location) else createMinecraftKey(location)
        val paperRegistry = RegistryAccess.registryAccess().getRegistry(registryKey)
        val tagKey = TagKey.create(registryKey, namespacedKey)
        return paperRegistry.getTag(tagKey)
    }


}