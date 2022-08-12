package me.shadowalzazel.mcodyssey.alchemy.utility

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class OdysseyAlchemyCauldronRecipe(val result: OdysseyPotion, val firstIngredient: ItemStack, val secondIngredient: ItemStack, val fuelBlock: Material) {

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

    private fun alchemicalAntithesis(someMaterials: MutableCollection<Item>) {
        val someLocation = someMaterials.elementAt(0).location.clone()
        someMaterials.elementAt(0).remove()
        someMaterials.elementAt(1).remove()
        val alchemyTask = AlchemyTask(someLocation, result.createItemStack(1))
        // Every 0.5 secs
        alchemyTask.runTaskTimer(MinecraftOdyssey.instance, 0, 2)

    }


    /*
    private fun alchemicalAntithesis(someMaterials: MutableCollection<Item>) {
        val ingredientOne = someMaterials.elementAt(0)
        val ingredientTwo = someMaterials.elementAt(1)

        println("A")
        if (ingredientOne.itemStack == firstIngredient && ingredientTwo.itemStack == secondIngredient || ingredientOne.itemStack == secondIngredient && ingredientTwo.itemStack == firstIngredient) {
            val someLocation = ingredientOne.location.clone()
            ingredientOne.remove()
            ingredientTwo.remove()
            val alchemyTask = AlchemyTask(someLocation, result.createItemStack(1))
            // Every 0.5 secs
            alchemyTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10)
        }

    }

     */

}