package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack


// Change to list ingredient later
open class OdysseyAlchemyCauldronRecipe(val result: OdysseyPotion, val ingredientList: List<ItemStack> ,val ingredientSize: Int, val fuelBlock: Material) {

    /*
    fun validateRecipe(someEntities: MutableCollection<Entity?>, someFuel: Material): Boolean {
        var someItems: MutableSet<Item> = mutableSetOf()
        var valid = false
        if ((someEntities.size == 2) && someFuel == fuelBlock) {
            if (someEntities.elementAt(0) is Item && someEntities.elementAt(1) is Item) {
                val materialOne = someEntities.elementAt(0) as Item
                val materialTwo = someEntities.elementAt(1) as Item
                if (materialOne.itemStack == firstIngredient && materialTwo.itemStack == secondIngredient || materialOne.itemStack == secondIngredient && materialTwo.itemStack == firstIngredient) {
                    someItems = mutableSetOf(materialOne, materialTwo)
                    valid = true
                    println("Valid!")
                }
            }
        }
        if (valid) {
            alchemicalAntithesis(someItems)
            println("Alchemy!")
        }
        return valid
    }

     */
    fun validateRecipe(someItemEntities: MutableCollection<Item>, someFuel: Material): Boolean {
        var valid = false
        if ((someItemEntities.size == ingredientSize) && someFuel == fuelBlock) {
            val someItemEntitiesCollection: MutableCollection<Item> = someItemEntities.toCollection(mutableListOf())

            for (someItem: Item in someItemEntitiesCollection) {
                if (someItem.itemStack !in ingredientList) return false
            }
            /*
            val ingredientMap = ingredientList.associateWith { false }.toMutableMap<ItemStack, Boolean>()

            for (someIngredient: ItemStack in ingredientMap.keys) {
                var itemToRemove: Item? = null
                for (someItem: Item in someItemEntitiesCollection) {
                    if (someItem.itemStack == someIngredient) {
                        ingredientMap[someIngredient] = true
                        itemToRemove = someItem
                        break
                    }
                }
                //
                if (itemToRemove != null) someItemEntitiesCollection.remove(itemToRemove)

            }
            for (value in ingredientMap.values) {
                if (!value) return false
            }
            */
            valid = true
        }
        if (valid) {
            alchemicalAntithesis(someItemEntities)
            println("Alchemy!")
        }
        return valid
    }


    private fun alchemicalAntithesis(someMaterials: MutableCollection<Item>) {
        val someLocation = someMaterials.elementAt(0).location.clone()
        val someBlock = someLocation.block

        for (itemEntity in someMaterials) {
            itemEntity.remove()
        }

        val alchemyTask = AlchemyTask(someBlock, result.createItemStack(1))
        // Every 0.5 secs
        alchemyTask.runTaskTimer(MinecraftOdyssey.instance, 0, 2)

    }
}