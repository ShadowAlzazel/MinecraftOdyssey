package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.coroutines.BottleCauldronEvents
import org.bukkit.scheduler.BukkitRunnable

class OdysseyCoroutineDetector : BukkitRunnable() {

    override fun run() {
        if (MinecraftOdyssey.instance.didStuff) {
            for (stuff in MinecraftOdyssey.instance.stuffThatHappened) {
                when (stuff) {
                    "Bottle_Cauldron_Event" -> {
                        for (cauldronRecipeMap in BottleCauldronEvents.eventMap.values) {
                            cauldronRecipeMap.forEach { recipe ->
                                recipe.key.alchemicalAntithesis(recipe.value)
                            }
                        }
                        BottleCauldronEvents.eventMap = mutableMapOf()
                    }
                }
            }
            MinecraftOdyssey.instance.stuffThatHappened = mutableSetOf()
            MinecraftOdyssey.instance.didStuff = false
        }
    }

}