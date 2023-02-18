package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.entity.EntityType

open class OdysseyOccurrence(
    val name: String,
    val types: List<OccurrenceType>,
    val affected: List<EntityType> = listOf(),
    val requirements: List<Requirement> = listOf(),
    val triggers: List<Trigger> = listOf(),
    val persistentEntityActions: List<PersistentEntityAction> = listOf(),


    ) {






}