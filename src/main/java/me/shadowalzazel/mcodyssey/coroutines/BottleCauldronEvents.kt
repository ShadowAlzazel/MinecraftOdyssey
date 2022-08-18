package me.shadowalzazel.mcodyssey.coroutines

import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyAlchemyCauldronRecipe
import org.bukkit.Location
import org.bukkit.entity.Item


object BottleCauldronEvents {

    // Match Location to another map of recipe to items
    var eventMap = mutableMapOf<Location, Map<OdysseyAlchemyCauldronRecipe, MutableSet<Item>>>()

}