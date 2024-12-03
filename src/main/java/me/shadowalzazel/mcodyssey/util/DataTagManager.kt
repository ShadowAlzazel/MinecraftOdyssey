@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*

interface DataTagManager {

    private fun PersistentDataContainer.hasItemKeyTag(): Boolean {
        return has(NamedKeys.ITEM_KEY)
    }

    fun ItemStack.hasItemKeyTag(): Boolean {
        return itemMeta.persistentDataContainer.hasItemKeyTag()
    }

    fun ItemStack.getItemKeyTag(): String? {
        return itemMeta.persistentDataContainer[NamedKeys.ITEM_KEY, PersistentDataType.STRING]
    }

    // Tries to get item_name
    fun ItemStack.getItemIdentifier(): String? {
        return getItemKeyTag() ?: if (itemMeta.hasItemName()) {
            @Suppress("DEPRECATION")
            itemMeta.itemName
        } else getData(DataComponentTypes.ITEM_NAME)?.toString()?.lowercase()
    }

    fun ItemStack.getItemNameId(): String {
        return getItemIdentifier() ?: this.type.name.lowercase()
    }

    fun ItemStack.matchItem(tag: String): Boolean {
        return this.getItemKeyTag() == tag
    }

    fun ItemStack.setTag(tag: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(tagKey, PersistentDataType.BOOLEAN, true)
        }
    }

    fun ItemStack.hasTag(tag: String): Boolean {
        if (!hasItemMeta()) return false
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return itemMeta.persistentDataContainer.has(tagKey)
    }

    fun ItemStack.removeTag(tag: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.remove(tagKey)
        }
    }

    fun ItemStack.setIntTag(tag: String, count: Int) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(tagKey, PersistentDataType.INTEGER, count)
        }
    }

    fun ItemStack.getIntTag(tag: String): Int? {
        if (!hasItemMeta()) return null
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return itemMeta.persistentDataContainer[tagKey, PersistentDataType.INTEGER]
    }

    fun ItemStack.setStringTag(tag: String, text: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(tagKey, PersistentDataType.STRING, text)
        }
    }

    fun ItemStack.getStringTag(tag: String): String? {
        if (!hasItemMeta()) return null
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return itemMeta.persistentDataContainer[tagKey, PersistentDataType.STRING]
    }

    fun ItemStack.setUUIDTag(uuid: UUID) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(NamedKeys.UUID_KEY, PersistentDataType.STRING, uuid.toString())
        }
    }

    fun ItemStack.getUUIDTag(): String {
        return itemMeta.persistentDataContainer[NamedKeys.UUID_KEY, PersistentDataType.STRING] ?: UUID.randomUUID().toString()
    }


}