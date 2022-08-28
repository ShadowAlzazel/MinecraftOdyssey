package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class OdysseyAlchemyCauldronRecipe(private val odysseyPotion: OdysseyPotion, private val ingredientList: List<ItemStack>, private val ingredientSize: Int, private val fuelBlock: Material) {

    // Validation Recipe Function
    fun validateRecipe(someItemEntities: MutableCollection<Item>, someFuel: Material): Boolean {
        var valid = false
        // Check fuel
        if ((someItemEntities.size == ingredientSize) && someFuel == fuelBlock) {
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
    fun alchemicalAntithesis(someMaterials: MutableCollection<Item>) {
        val someLocation = someMaterials.elementAt(0).location.clone()
        someLocation.world.playSound(someLocation, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
        val someBlock = someLocation.block
        // removes items
        for (itemEntity in someMaterials) {
            itemEntity.remove()
        }
        // Runs alchemy task ticking every 0.5 seconds
        val alchemyTask = AlchemyTask(someBlock, odysseyPotion.createItemStack(1))
        alchemyTask.runTaskTimer(MinecraftOdyssey.instance, 0, 2)
    }
}