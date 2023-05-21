package me.shadowalzazel.mcodyssey.constants

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

object ItemTags {

    const val ALCHEMY_ARTILLERY_LOADED: String = "alchemy_artillery_loaded"

    fun PersistentDataContainer.hasOdysseyTag(): Boolean {
        return has(NamespacedKey(Odyssey.instance, "item"))
    }

    fun ItemStack.hasOdysseyTag(): Boolean {
        return itemMeta.persistentDataContainer.hasOdysseyTag()
    }

    fun ItemStack.hasTag(tag: String): Boolean {
        return itemMeta.persistentDataContainer.has(NamespacedKey(Odyssey.instance, tag))
    }

    fun ItemStack.addTag(tag: String) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(NamespacedKey(Odyssey.instance, tag), PersistentDataType.INTEGER, 1)
        }
    }

    fun ItemStack.removeTag(tag: String) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.remove(NamespacedKey(Odyssey.instance, tag))
        }
    }

    fun ItemStack.setUUIDTag(uuid: UUID) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(NamespacedKey(Odyssey.instance, "UUID"), PersistentDataType.STRING, uuid.toString())
        }
    }

    fun ItemStack.getUUIDString(): String {
        return itemMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "UUID"), PersistentDataType.STRING] ?: UUID.randomUUID().toString()
    }

}