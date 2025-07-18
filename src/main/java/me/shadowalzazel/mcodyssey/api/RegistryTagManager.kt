package me.shadowalzazel.mcodyssey.api

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

    fun <T: Keyed> createTagKey(registryKey: RegistryKey<T>, namespacedKey: NamespacedKey): TagKey<T> {
        return TagKey.create(registryKey, namespacedKey)
    }

    /**
     *
     */
    fun <T: Keyed> getTagFromRegistry(registryKey: RegistryKey<T>, location: String, namespace: String="odyssey"): Tag<T> {
        //val blockRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK)
        //val tagKey = TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft("mineable/axe"))
        //return blockRegistry.getTag(tagKey)
        val namespacedKey = if (namespace == "odyssey") createOdysseyKey(location) else createMinecraftKey(location)
        val paperRegistry = RegistryAccess.registryAccess().getRegistry(registryKey)
        val tagKey = TagKey.create(registryKey, namespacedKey)
        return paperRegistry.getTag(tagKey)
    }

    /**
     *
     *
     *
     * Example: val armorSet = getCollectionFromTag(RegistryKey.ENCHANTMENT, "in_table/armor")
     */
    fun <T: Keyed> getCollectionFromKey(registryKey: RegistryKey<T>, location: String, namespace: String="odyssey"): Collection<T> {
        // Create a tag with given registry key and location
        val registry = getPaperRegistry(registryKey)
        val tag = getTagFromRegistry(registryKey, location, namespace)
        // Get Collection
        val listOfValues: MutableList<T> = mutableListOf()
        for (tagEntry in tag.values()) {
            val value = registry.get(tagEntry.key()) ?: continue
            listOfValues.add(value)
        }
        return listOfValues.toSet()
    }


    fun <T: Keyed> getCollectionFromTag(
        registryKey: RegistryKey<T>,
        tag: Tag<T>): Collection<T> {
        // Get Collection
        val registry = getPaperRegistry(registryKey)
        val listOfValues: MutableList<T> = mutableListOf()
        for (tagEntry in tag.values()) {
            val value = registry.get(tagEntry.key()) ?: continue
            listOfValues.add(value)
        }
        return listOfValues.toSet()
    }

}