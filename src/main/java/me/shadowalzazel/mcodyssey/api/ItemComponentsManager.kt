package me.shadowalzazel.mcodyssey.api

import io.papermc.paper.datacomponent.DataComponentType
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface ItemComponentsManager {


    fun <T: Any> transferComponents(item: ItemStack, input: ItemStack, componentTypes: List<DataComponentType.Valued<out T>>) {
        for (comp in componentTypes) {
            transferComponent(item, input, comp)
        }
    }


    fun <T: Any> transferComponent(item: ItemStack, input: ItemStack, componentType: DataComponentType.Valued<T>) {
        if (!input.hasData(componentType)) return
        val inputComponent = input.getData(componentType) ?: return
        item.setData(componentType, inputComponent)
    }


}
