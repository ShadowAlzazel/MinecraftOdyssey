package me.shadowalzazel.mcodyssey.common.alchemy.utility

import me.shadowalzazel.mcodyssey.common.alchemy.CauldronAlchemyRecipe
import org.bukkit.entity.Item
import org.bukkit.scheduler.BukkitRunnable

class CauldronRunner(
    private val recipe: CauldronAlchemyRecipe,
    private val items: MutableCollection<Item>) : BukkitRunnable() {

    override fun run() {
        recipe.successHandler(items)
        this.cancel()
    }

}