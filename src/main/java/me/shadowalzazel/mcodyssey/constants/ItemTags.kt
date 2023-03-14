package me.shadowalzazel.mcodyssey.constants

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer

object ItemTags {

    fun PersistentDataContainer.hasOdysseyTag(): Boolean {
        return has(NamespacedKey(Odyssey.instance, "item"))
    }

}