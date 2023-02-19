package me.shadowalzazel.mcodyssey.occurrences.base

import me.shadowalzazel.mcodyssey.seasons.SeasonType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface OccurrenceCreator {

    fun temp() {

        val newTemplate = mapOf(
            "name" to "Hello",
            "type" to listOf(
                OccurrenceType.LUNAR),
            "negatable_by_allay" to false,
            "requirements" to listOf(
                Requirement.IsSeason(listOf(SeasonType.AUTUMN)),
                Requirement.IsTime(1, 1121)
            ),
            "criteria" to mapOf(
                "is_high_and_wet" to listOf(
                    Condition.WithinHeight(0, 222),
                    Condition.IsWet(true, in_water = true)
                )
            ),
            "actions" to mapOf( // For actions -> each get their own list
                "persistent" to listOf(
                    mapOf(
                        "criteria_name" to "is_high_and_wet",
                        "entries" to listOf(
                            OccurrenceAction.ApplyFreeze(7),
                            OccurrenceAction.ApplyPotionEffect(listOf(
                                PotionEffect(PotionEffectType.HUNGER, 2, 1)))
                        )
                    )
                ),
                "spawning" to listOf(),
                "one_time" to listOf()
            )
        )
    }

    // 1. Create Conditions
    // 2. Create Criteria
    // 3. Create Actions link to criteria

}