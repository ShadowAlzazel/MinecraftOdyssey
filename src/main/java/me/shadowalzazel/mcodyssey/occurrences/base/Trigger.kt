package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.Material
import org.bukkit.entity.LivingEntity

sealed class Trigger {

    class BlockLight(var min: Int, var max: Int, var illuminable: Boolean): Trigger()
    class SkyLight(var min: Int, var max: Int) : Trigger()
    class ItemConsume(var item_list: List<Material>): Trigger()
    object NoTrigger: Trigger()

    fun isTriggered(someEntity: LivingEntity): Boolean {
        return when (this) {
            is BlockLight -> {
                checkBlockLight(someEntity, this.min, this.max, this.illuminable)
            }
            is ItemConsume -> false
            NoTrigger -> false
            is SkyLight -> false
        }

    }


    private fun checkBlockLight(someEntity: LivingEntity, min: Int, max: Int, illuminable: Boolean): Boolean {
        val itemInHand: Material? = someEntity.equipment?.itemInOffHand?.type
        val holdingLight: Boolean = (itemInHand == Material.LANTERN || itemInHand == Material.TORCH) && illuminable
        val lightFromBlocks: Int = someEntity.location.block.lightFromBlocks.toInt()
        val totalLight: Int = if (holdingLight) { 13 + lightFromBlocks } else { lightFromBlocks }

        return (min <= totalLight) && (totalLight <= max)
    }


}
