package me.shadowalzazel.mcodyssey.world_events.utility

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.generator.structure.Structure

sealed class EntityConditions {

    data class BlockLighting(val min: Int, val max: Int, val illuminable: Boolean): EntityConditions()
    data class HasTag(val tag: String): EntityConditions()
    data class HasTags(val tagList: List<String>): EntityConditions()
    data class InBiome(val biomeList: List<String>): EntityConditions()
    data class InStructure(val conditionalStructure: Structure): EntityConditions()
    data class InAffectedMobs(val affectedMobs: List<EntityType>): EntityConditions()
    data class IsMobType(val affectedType: EntityType): EntityConditions()
    data class IsSpecialEntity(val affectedType: EntityType, var mobTags: List<String>): EntityConditions()
    data class IsWet(val checkRain: Boolean, val checkWater: Boolean): EntityConditions()
    data class ItemConsume(val itemList: List<Material>): EntityConditions() // TODO
    data class SkyLighting(val min: Int, val max: Int, val illuminable: Boolean) : EntityConditions()
    data class WithinHeight(var min: Int, var max: Int): EntityConditions()
    data object AlwaysTrue: EntityConditions()
    data object NeverTrue: EntityConditions()

    fun checkCondition(entity: LivingEntity): Boolean {
        return when(this) {
            is BlockLighting -> checkLight(entity)
            is HasTag -> entity.scoreboardTags.contains(tag)
            is HasTags -> tagList.all { it in entity.scoreboardTags }
            is InBiome -> entity.location.block.biome.translationKey() in biomeList
            is InStructure -> checkBounding(entity)
            is InAffectedMobs -> affectedMobs.contains(entity.type)
            is IsMobType -> entity.type == affectedType
            is IsSpecialEntity -> entity.type == affectedType && mobTags.all { it in entity.scoreboardTags }
            is IsWet -> (entity.isInRain && checkRain) || (entity.isInWaterOrBubbleColumn && checkWater)
            is ItemConsume -> false
            is SkyLighting -> checkLight(entity)
            is WithinHeight -> (min < entity.location.y) && (entity.location.y < max)
            AlwaysTrue -> true
            NeverTrue -> false
        }
    }

    private fun BlockLighting.checkLight(entity: LivingEntity): Boolean {
        val itemInHand: Material? = entity.equipment?.itemInOffHand?.type
        val isHoldingLight: Boolean = (itemInHand == Material.LANTERN || itemInHand == Material.TORCH) && illuminable
        val lightSource = entity.location.block.lightFromBlocks.toInt()
        val totalLight = if (isHoldingLight) { 13 + lightSource } else { lightSource }
        return (min <= totalLight) && (totalLight <= max)
    }

    private fun SkyLighting.checkLight(entity: LivingEntity): Boolean {
        val itemInHand: Material? = entity.equipment?.itemInOffHand?.type
        val isHoldingLight: Boolean = (itemInHand == Material.LANTERN || itemInHand == Material.TORCH) && illuminable
        val lightSource = entity.location.block.lightFromSky.toInt()
        val totalLight = if (isHoldingLight) { 13 + lightSource } else { lightSource }
        return (min <= totalLight) && (totalLight <= max)
    }

    private fun InStructure.checkBounding(entity: LivingEntity): Boolean {
        val structures = entity.location.chunk.structures
        if (structures.isEmpty()) return false
        val inside = structures.filter { entity.boundingBox.overlaps(it.boundingBox) }
        if (inside.isEmpty()) return false
        return inside.any { it.structure == this.conditionalStructure }
    }

    // Passes In Item Consume Event
    open fun hasConsumedItem(consumedItem: Material): Boolean {
        return false
        // FOR CONSUME CHECK RECENT CONSUMED FOOD
        // CREATE EVENT THAT ADDS TAGE OF RECENT FOOD
    }

}
