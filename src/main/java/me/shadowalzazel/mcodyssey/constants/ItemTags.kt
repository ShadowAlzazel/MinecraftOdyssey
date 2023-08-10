package me.shadowalzazel.mcodyssey.constants

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*

object ItemTags {

    const val ALCHEMY_ARTILLERY_LOADED: String = "alchemy_artillery_loaded"
    const val ENGRAVED: String = "engraved"
    const val SOUL_STEEL_TOOL: String = "soul_steel_tool"
    const val NETHERITE_TOOL: String = "netherite_tool"

    const val POTION_CHARGES_LEFT: String = "potion_uses_left" // NEEDS AN INT DATA TYPE
    const val LARGE_POTION: String = "large_potion" // Many charges

    fun PersistentDataContainer.hasOdysseyTag(): Boolean {
        return has(NamespacedKey(Odyssey.instance, "item"))
    }

    fun ItemStack.hasOdysseyTag(): Boolean {
        return itemMeta.persistentDataContainer.hasOdysseyTag()
    }

    fun ItemStack.getOdysseyTag(): String? {
        return itemMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING]
    }

    fun ItemStack.isThisItem(tag: String): Boolean {
        return this.getOdysseyTag() == tag
    }

    fun ItemStack.addTag(tag: String) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(NamespacedKey(Odyssey.instance, tag), PersistentDataType.BOOLEAN, true)
        }
    }

    fun ItemStack.hasTag(tag: String): Boolean {
        return itemMeta.persistentDataContainer.has(NamespacedKey(Odyssey.instance, tag))
    }

    fun ItemStack.removeTag(tag: String) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.remove(NamespacedKey(Odyssey.instance, tag))
        }
    }

    fun ItemStack.addIntTag(tag: String, count: Int) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(NamespacedKey(Odyssey.instance, tag), PersistentDataType.INTEGER, count)
        }
    }


    fun ItemStack.getIntTag(tag: String): Int? {
        return itemMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, tag), PersistentDataType.INTEGER]
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