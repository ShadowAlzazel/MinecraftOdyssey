package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Potions.createPotion
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class AlchemyCauldronRecipe(
    private val brewedPotion: OdysseyItem,
    private val ingredientList: List<ItemStack>,
    private val ingredientSize: Int,
    private val fireMaterial: Material) {

    // Validation Recipe Function
    fun validateRecipe(someItemEntities: MutableCollection<Item>, someFuel: Material): Boolean {
        var valid = false
        // Check fuel
        if ((someItemEntities.size == ingredientSize) && someFuel == fireMaterial) {
            val someItemEntitiesCollection: MutableCollection<Item> = someItemEntities.toCollection(mutableListOf())
            // Check if items not in ingredient list for recipe
            for (someItem: Item in someItemEntitiesCollection) {
                // Returns false if not in
                if (someItem.itemStack !in ingredientList) return false
            }
            valid = true
        }
        return valid
    }

    // Called on a successful validation
    fun successfulRecipeHandler(someMaterials: MutableCollection<Item>) {
        val someLocation = someMaterials.elementAt(0).location.clone()
        someLocation.world.playSound(someLocation, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
        val someBlock = someLocation.block
        // removes items
        for (itemEntity in someMaterials) {
            itemEntity.remove()
        }
        val result = if (brewedPotion.potionEffects != null) { brewedPotion.createPotion() } else { brewedPotion.createItemStack(1) }
        val alchemyTask = AlchemyCauldronTask(someBlock, result)
        alchemyTask.runTaskTimer(Odyssey.instance, 0, 2)
        // Runs alchemy task ticking every 0.5 seconds
    }
}