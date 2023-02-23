package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.generator.structure.Structure

sealed class Condition {

    // Triggers run per 200 ticks and return a boolean used to activate effects

    data class BlockLighting(var min: Int, var max: Int, var illuminable: Boolean): Condition()
    data class HasTag(var tag: String): Condition()
    data class HasTags(var tag_list: List<String>): Condition()
    data class InBiome(var biome_keys_list: List<String>): Condition()
    data class InStructure(var structure: Structure): Condition()
    data class InAffectedMobs(var entity_list: List<EntityType>): Condition()
    data class IsMobType(var entity_type: EntityType): Condition()
    data class IsSpecialEntity(var entity_type: EntityType, var special_tags: List<String>): Condition()
    data class IsWet(var in_rain: Boolean, var in_water: Boolean): Condition()
    data class ItemConsume(var item_list: List<Material>): Condition()
    data class SkyLighting(var min: Int, var max: Int, var illuminable: Boolean) : Condition()
    data class WithinHeight(var min: Int, var max: Int): Condition()
    object AlwaysTrue: Condition()
    object NeverTrue: Condition()


    // MAYBE MAKE EXTENSIONS?
    // SEPARATE LOGIC INTO ASYNC
    fun checkCondition(someEntity: LivingEntity): Boolean {
        return when (this) {
            is BlockLighting -> checkLight(someEntity)
            is HasTag -> someEntity.scoreboardTags.contains(tag)
            is HasTags -> tag_list.all { it in someEntity.scoreboardTags }
            is InBiome -> someEntity.location.block.biome.translationKey() in biome_keys_list
            is InStructure -> someEntity.world.locateNearestStructure(someEntity.location, structure, 1, false) != null
            is InAffectedMobs -> entity_list.contains(someEntity.type)
            is IsMobType -> someEntity.type == entity_type
            is IsSpecialEntity -> someEntity.type == entity_type && special_tags.all { it in someEntity.scoreboardTags }
            is IsWet -> (someEntity.isInRain && in_rain) || (someEntity.isInWaterOrBubbleColumn && in_water)
            is ItemConsume -> false
            is SkyLighting -> checkLight(someEntity)
            is WithinHeight -> (min < someEntity.location.y) && (someEntity.location.y < max)
            AlwaysTrue -> true
            NeverTrue -> false
        }
    }


    private fun BlockLighting.checkLight(someEntity: LivingEntity): Boolean {
        val itemInHand: Material? = someEntity.equipment?.itemInOffHand?.type
        val isHoldingLight: Boolean = (itemInHand == Material.LANTERN || itemInHand == Material.TORCH) && illuminable
        val lightSource = someEntity.location.block.lightFromBlocks.toInt()
        val totalLight = if (isHoldingLight) { 13 + lightSource } else { lightSource }

        return (min <= totalLight) && (totalLight <= max)
    }


    private fun SkyLighting.checkLight(someEntity: LivingEntity): Boolean {
        val itemInHand: Material? = someEntity.equipment?.itemInOffHand?.type
        val isHoldingLight: Boolean = (itemInHand == Material.LANTERN || itemInHand == Material.TORCH) && illuminable
        val lightSource = someEntity.location.block.lightFromSky.toInt()
        val totalLight = if (isHoldingLight) { 13 + lightSource } else { lightSource }

        return (min <= totalLight) && (totalLight <= max)
    }


    // Passes In Item Consume Event
    open fun hasConsumedItem(consumedItem: Material): Boolean {
        return false
        // FOR CONSUME CHECK RECENT CONSUMED FOOD
        // CREATE EVENT THAT ADDS TAGE OF RECENT FOOD
    }



}
