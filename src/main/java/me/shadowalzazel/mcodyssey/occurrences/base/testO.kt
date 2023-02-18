package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun oMain() {

    val chillingNight = listOf(Trigger.BlockLight(1, 2, false),
        PersistentEntityAction.PersistentPotionEffect(listOf(PotionEffect(PotionEffectType.HUNGER, 2, 1))))

    // for each json, create occurrence based on sealed classes v
    // then add to list corresponding to type
    //

    val darkNight = OdysseyOccurrence(
        "DarkNight",
        listOf(OccurrenceType.LUNAR),
        listOf(EntityType.PLAYER, EntityType.ENDERMAN),
        triggers = listOf(Trigger.BlockLight(0, 6, false)),
        persistentEntityActions = listOf(PersistentEntityAction.PersistentPotionEffect(listOf(PotionEffect(PotionEffectType.HUNGER, 2, 1))))
    )





}